package com.hunk.nobank.manager;

import android.content.Context;
import android.content.SharedPreferences;

import com.hunk.nobank.Core;
import com.hunk.nobank.contract.AccountSummary;
import com.hunk.nobank.contract.AccountType;
import com.hunk.nobank.contract.LoginResp;
import com.hunk.nobank.contract.RealResp;
import com.hunk.nobank.contract.type.LoginStateEnum;
import com.hunk.nobank.manager.dataBasic.DataManager;
import com.hunk.nobank.manager.dataBasic.ManagerListener;
import com.hunk.nobank.model.AccountSummaryPackage;
import com.hunk.nobank.model.LoginReqPackage;

/**
 * DataManager which hold all user data and session
 */
public class UserManager extends DataManager {

    private static final String MANAGER_ID = "LoginManager";

    private static final String APP_SHARED_PREFERENCES_NAME = "NobankSharedPref";

    private static final String KEY_IS_REMEMBER_ME = "KEY_IS_REMEMBER_ME";
    private static final String KEY_IS_REMEMBER_ME_USERNAME = "KEY_IS_REMEMBER_ME_USERNAME";

    private Context mCtx;

    public UserSession mCurrentUserSession;

    public UserManager(Context mCtx) {
        this.mCtx = mCtx;
    }

    public boolean isRememberMe() {
        SharedPreferences pref = getSharedPreferences();
        return pref.getBoolean(KEY_IS_REMEMBER_ME, false);
    }

    public String getRememberMeUserName() {
        SharedPreferences pref = getSharedPreferences();
        return pref.getString(KEY_IS_REMEMBER_ME_USERNAME, null);
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

    public static final String METHOD_LOGIN = "METHOD_LOGIN";
    public void fetchLogin(LoginReqPackage req, final ManagerListener listener) {
        Core.getInstance().getNetworkHandler()
                .fireRequest(new ManagerListener() {
                    @Override
                    public void success(String managerId, String messageId, Object data) {
                        RealResp<LoginResp> realResp = (RealResp<LoginResp>) data;
                        LoginResp loginResp = realResp.Response;
                        mCurrentUserSession = new UserSession();
                        mCurrentUserSession.setLoginState(
                                loginResp.loginState == null ?
                                        LoginStateEnum.UnAuthorized : loginResp.loginState);
                        listener.success(managerId, messageId, data);
                    }

                    @Override
                    public void failed(String managerId, String messageId, Object data) {
                        listener.failed(managerId, messageId, data);
                    }
                }, req, getManagerId(), METHOD_LOGIN);
    }


    public final String getManagerId() {
        return MANAGER_ID;
    }

    public static final String METHOD_ACCOUNT_SUMMARY = "METHOD_ACCOUNT_SUMMARY";

    /**
     * @param req
     * @param listener
     * @return
     *  Whether application call network request
     */
    public boolean fetchAccountSummary(
            AccountSummaryPackage req, final ManagerListener listener) {
        if (AccountSummaryPackage.cache.shouldFetch(req)) {
            Core.getInstance().getNetworkHandler()
                    .fireRequest(new ManagerListener() {
                        @Override
                        public void success(String managerId, String messageId, Object data) {
                            RealResp<AccountSummary> realResp = (RealResp<AccountSummary>) data;
                            AccountSummary accountSummary = realResp.Response;
                            if (UserSession.isPostLogin(mCurrentUserSession)) {
                                mCurrentUserSession.generateAccountDataManager(accountSummary);
                                mCurrentUserSession.generateTransactionDataManager(accountSummary);
                                listener.success(managerId, messageId, data);
                            }
                        }

                        @Override
                        public void failed(String managerId, String messageId, Object data) {
                            listener.failed(managerId, messageId, data);
                        }
                    }, req, getManagerId(), METHOD_ACCOUNT_SUMMARY);
            return true;
        } else {
            listener.success(getManagerId(), METHOD_ACCOUNT_SUMMARY, AccountSummaryPackage.cache.get());
            return false;
        }
    }

    public UserSession getCurrentUserSession() {
        return mCurrentUserSession;
    }

    public void setCurrentUserSession(UserSession currentUserSession) {
        this.mCurrentUserSession = currentUserSession;
    }
}
