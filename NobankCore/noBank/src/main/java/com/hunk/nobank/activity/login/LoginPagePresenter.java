package com.hunk.nobank.activity.login;

import android.support.annotation.VisibleForTesting;

import com.hunk.nobank.Core;
import com.hunk.abcd.activity.mvp.AbstractPresenter;
import com.hunk.nobank.contract.type.LoginStateEnum;
import com.hunk.nobank.manager.UserManager;
import com.hunk.nobank.manager.dataBasic.ViewManagerListener;
import com.hunk.nobank.model.AccountSummaryPackage;
import com.hunk.nobank.model.LoginReqPackage;
import com.hunk.abcd.extension.util.StringUtils;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;

/**
 * @author HunkDeng
 * @since 2016/5/22
 */
public class LoginPagePresenter extends AbstractPresenter<LoginView>{
    private final UserManager mUserManager;

    public LoginPagePresenter() {
        mUserManager = Core.getInstance().getUserManager();
        mUserManager.registerViewManagerListener(mManagerListener);
    }

    @VisibleForTesting
    private ViewManagerListener mManagerListener = new ViewManagerListener(this) {
        @Override
        public void onSuccess(String managerId, String messageId, Object data) {
            if (managerId.equals(UserManager.MANAGER_ID)) {
                if (messageId.equals(UserManager.METHOD_LOGIN)) {
                    if (mUserManager.getCurrentUserSession() != null) {
                        if (mUserManager.getCurrentUserSession().getLoginState() == LoginStateEnum.Logined) {
                            AccountSummaryPackage accountSummaryPackage = new AccountSummaryPackage();
                            mUserManager.fetchAccountSummary(accountSummaryPackage, this);
                        } else if (mUserManager.getCurrentUserSession().getLoginState() == LoginStateEnum.NeedVerifySecurityQuestion) {
                            mView.dismissLoading();
                            mView.showErrorMessage("verifySecurityQuestion");
                        }
                    } else {
                        //  TODO: throw exception in debug mode
                    }
                } else if (messageId.equals(UserManager.METHOD_ACCOUNT_SUMMARY)) {
                    mView.dismissLoading();
                    mView.navigateToDashboard();
                }
            } else {

            }
        }

        @Override
        public void onFailed(String managerId, String messageId, Object data) {
            if (managerId.equals(UserManager.MANAGER_ID)) {
                if (messageId.equals(UserManager.METHOD_LOGIN)) {
                    mView.dismissLoading();
                    mView.showErrorMessage("failed");
                } else if (messageId.equals(UserManager.METHOD_ACCOUNT_SUMMARY)) {
                    mView.dismissLoading();
                    mView.showErrorMessage("sorry, we can't load your balance.");
                    // logout and clear session
                    mUserManager.setCurrentUserSession(null);
                }
            }
        }
    };

    /**
     * login interface
     * will trigger communication with server to login
     */
    public void loginAction() {
        String name = mView.getUserName();
        String psd = mView.getPsd();
        boolean rememberMe = mView.isCheckedRememberMe();

        mView.clearPassword();

        if (checkInput(name, psd)) {
            submit(name, psd, rememberMe);
        } else {
            mView.showErrorMessage("Fill all info, please.");
        }
    }

    private void submit(String name, String psd, boolean rememberMe) {
        final LoginReqPackage req = new LoginReqPackage(name, psd, rememberMe);
        mView.showLoading();
        mUserManager.fetchLogin(req, mManagerListener);
    }

    private boolean checkInput(String name, String psd) {
        boolean pass = true;
        if (StringUtils.isNullOrEmpty(name)) {
            pass = false;
        }
        if (StringUtils.isNullOrEmpty(psd)) {
            pass = false;
        }
        return pass;
    }

    public void detach() {
        super.detach();
        // TODO: remove this
        mUserManager.unregisterViewManagerListener(mManagerListener);
    }

    public void onResume() {
        mUserManager
                .isRememberMe()
                .flatMap(new Func1<Boolean, Observable<String>>() {
                    @Override
                    public Observable<String> call(Boolean isRemembered) {
                        if (isRemembered) {
                            return mUserManager.getRememberMeUserName();
                        } else {
                            return Observable.never();
                        }
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<String>() {
                    @Override
                    public void call(String username) {
                        mView.showRememberedUserName(username);
                    }
                });
    }

    /**
     * Save persistent data
     * TODO: change name
     */
    public void onPause() {
        // save Remember Me Status
        if (mView.isCheckedRememberMe()) {
            mUserManager.setRememberMe(true, mView.getUserName());
        } else {
            mUserManager.setRememberMe(false, null);
        }
    }
}
