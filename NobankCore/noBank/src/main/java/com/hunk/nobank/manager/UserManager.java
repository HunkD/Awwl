package com.hunk.nobank.manager;

import android.content.Context;
import android.content.SharedPreferences;

import com.hunk.nobank.Core;
import com.hunk.nobank.activity.BaseActivity;
import com.hunk.nobank.contract.AccountSummary;
import com.hunk.nobank.contract.LoginResp;
import com.hunk.nobank.contract.type.LoginStateEnum;
import com.hunk.nobank.manager.dataBasic.DataManager;
import com.hunk.nobank.model.AccountSummaryPackage;
import com.hunk.nobank.model.LoginReqPackage;
import com.hunk.nobank.util.Hmg;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;

/**
 * DataManager which hold all user data and session
 */
public class UserManager extends DataManager {

    private static final String APP_SHARED_PREFERENCES_NAME = "NobankSharedPref";

    private static final String KEY_IS_REMEMBER_ME = "KEY_IS_REMEMBER_ME";
    private static final String KEY_IS_REMEMBER_ME_USERNAME = "KEY_IS_REMEMBER_ME_USERNAME";

    private Context mCtx;

    public UserSession mCurrentUserSession;

    public UserManager(Context mCtx) {
        this.mCtx = mCtx;
    }

    public Observable<Boolean> isRememberMe() {
        final SharedPreferences pref = getSharedPreferences();
        return Observable.create(new Observable.OnSubscribe<Boolean>() {
            @Override
            public void call(Subscriber<? super Boolean> subscriber) {
                subscriber.onNext(pref.getBoolean(KEY_IS_REMEMBER_ME, false));
                subscriber.onCompleted();
            }
        });
    }

    public Observable<String> getRememberMeUserName() {
        final SharedPreferences pref = getSharedPreferences();
        return Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                subscriber.onNext(pref.getString(KEY_IS_REMEMBER_ME_USERNAME, null));
                subscriber.onCompleted();
            }
        });
    }

    public void setRememberMe(boolean isRememberMe, String rememberMeUserName) {
        SharedPreferences pref = getSharedPreferences();
        pref.edit()
                .putBoolean(KEY_IS_REMEMBER_ME, isRememberMe)
                .putString(KEY_IS_REMEMBER_ME_USERNAME, rememberMeUserName)
                .apply();
    }

    public SharedPreferences getSharedPreferences() {
        return mCtx.getSharedPreferences(APP_SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
    }

    public Observable<LoginResp> fetchLogin(String username, String psd, boolean rememberMe) {
        LoginReqPackage req = new LoginReqPackage(username, psd, rememberMe);
        Observable<LoginResp> observable =
                Core.getInstance().getNetworkHandler().fireRequest(req);
        return observable
                .observeOn(AndroidSchedulers.mainThread())
                .map(new Func1<LoginResp, LoginResp>() {
                    @Override
                    public LoginResp call(LoginResp loginResp) {
                        if (loginResp.loginState == null
                                || loginResp.loginState == LoginStateEnum.UnAuthorized) {
                            throw new RuntimeException();
                        } else {
                            mCurrentUserSession = new UserSession();
                            mCurrentUserSession.setLoginState(loginResp.loginState);
                            return loginResp;
                        }
                    }
                });
    }

    /**
     * @return
     *  Whether application call network request
     */
    public Observable<AccountSummary> fetchAccountSummary() {
        AccountSummaryPackage req = new AccountSummaryPackage();
        return invokeNetwork(AccountSummaryPackage.cache, req)
                .observeOn(AndroidSchedulers.mainThread())
                .map(new Func1<AccountSummary, AccountSummary>() {
                    @Override
                    public AccountSummary call(AccountSummary accountSummary) {
                        mCurrentUserSession.generateAccountDataManager(accountSummary);
                        mCurrentUserSession.generateTransactionDataManager(accountSummary);
                        return accountSummary;
                    }
                });
    }

    /**
     * Check if current user session has passed login.
     * @param userManager
     *          application's user manager
     * @return
     *          current user session has login or not
     */
    public static boolean isPostLogin(UserManager userManager) {
        if (userManager != null) {
            UserSession currentUserSession = userManager.getCurrentUserSession();
            if (currentUserSession != null
                        && currentUserSession.getLoginState() == LoginStateEnum.Logined) {
                return true;
            }
        }
        return false;
    }

    public UserSession getCurrentUserSession() {
        return mCurrentUserSession;
    }

    public void setCurrentUserSession(UserSession currentUserSession) {
        this.mCurrentUserSession = currentUserSession;
    }

    /**
     * Logout method
     * @param context
     *    context which can start activity
     */
    public void logout(Context context) {
        // reset current user session
        setCurrentUserSession(null);
        // clean session object cache
        Core.clearCache();
        // Unroll activity if it's foreground
        if (BaseActivity.IS_APP_FOREGROUND) {
            BaseActivity.unrollActivity(context);
        }
        // clear img cache
        Hmg.getInstance().clear();
    }
}
