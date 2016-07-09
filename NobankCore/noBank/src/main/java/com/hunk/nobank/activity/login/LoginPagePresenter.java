package com.hunk.nobank.activity.login;

import com.hunk.abcd.activity.mvp.AbstractPresenter;
import com.hunk.abcd.extension.util.StringUtils;
import com.hunk.nobank.Core;
import com.hunk.nobank.activity.BaseViewObserver;
import com.hunk.nobank.contract.AccountSummary;
import com.hunk.nobank.contract.LoginResp;
import com.hunk.nobank.contract.type.LoginStateEnum;
import com.hunk.nobank.manager.UserManager;

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
    }

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
        mView.showLoading();
        mUserManager.fetchLogin(name, psd, rememberMe)
                .observeOn(AndroidSchedulers.mainThread())
                .flatMap(new Func1<LoginResp, Observable<AccountSummary>>() {
                    @Override
                    public Observable<AccountSummary> call(LoginResp loginResp) {
                        if (loginResp.loginState == LoginStateEnum.Logined) {
                            return mUserManager.fetchAccountSummary();
                        } else if (loginResp.loginState == LoginStateEnum.NeedVerifySecurityQuestion) {
                            if (mView != null) {
                                mView.dismissLoading();
                                mView.navigateToSecurityQuestion();
                            }
                            return Observable.never();
                        }
                        mView.dismissLoading();
                        throw new RuntimeException();
                    }
                })
                .subscribe(new BaseViewObserver<AccountSummary>(this) {
                    @Override
                    public void onNext(AccountSummary accountSummary) {
                        mView.dismissLoading();
                        mView.navigateToDashboard();
                    }

                    @Override
                    public void onError(Throwable e) {
                        mView.dismissLoading();
                        super.onError(e);
                        // logout and clear session
                        mUserManager.setCurrentUserSession(null);
                    }
                });

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
