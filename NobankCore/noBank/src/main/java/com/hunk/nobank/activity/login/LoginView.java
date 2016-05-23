package com.hunk.nobank.activity.login;

/**
 * @author HunkDeng
 * @since 2016/5/22
 */
public interface LoginView {
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
