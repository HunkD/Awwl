package com.hunk.nobank.activity.login;

import com.hunk.nobank.activity.base.BasePresenter;
import com.hunk.nobank.activity.base.BaseView;

/**
 * @author HunkDeng
 * @since 2016/5/22
 */
public interface LoginView<P extends BasePresenter> extends BaseView<P> {
    void navigateToDashboard();
    void clearPassword();
    void showRememberedUserName(String username);
    void showLoading();
    void dismissLoading();
    void showErrorMessage(String msg);
    // getter
    boolean isCheckedRememberMe();
    String getUserName();
    String getPsd();
}
