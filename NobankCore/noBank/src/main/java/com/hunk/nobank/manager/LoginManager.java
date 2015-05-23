package com.hunk.nobank.manager;

import android.content.Context;
import android.content.SharedPreferences;

import com.hunk.nobank.Core;
import com.hunk.nobank.extension.network.LoginResp;
import com.hunk.nobank.extension.network.NetworkHandler;
import com.hunk.nobank.extension.network.RealResp;
import com.hunk.nobank.model.login.LoginReqPackage;

/**
 *
 */
public class LoginManager extends DataManager {

    private static final String MANAGER_ID = "LoginManager";

    private static final String APP_SHARED_PREFERENCES_NAME = "NobankSharedPref";

    private static final String KEY_IS_REMEMBER_ME = "KEY_IS_REMEMBER_ME";
    private static final String KEY_IS_REMEMBER_ME_USERNAME = "KEY_IS_REMEMBER_ME_USERNAME";

    private Context mCtx;
    private NetworkHandler mNetworkHandler;
    private boolean mLoginSuccess;

    public LoginManager(Context mCtx) {
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

    public boolean isLogInSuccessfully() {
        return false;
    }

    public static final String METHOD_LOGIN = "METHOD_LOGIN";
    public void fetchLogin(LoginReqPackage req, final ManagerListener listener) {
        Core.getInstance().getNetworkHandler()
                .fireRequest(new ManagerListener() {
                    @Override
                    public void success(String managerId, String messageId, Object data) {
                        RealResp<LoginResp> realResp = (RealResp<LoginResp>) data;
                        LoginResp loginResp = realResp.Response;
                        if (!loginResp.NeedSecurityQuestionCheck) {
                            setLogInSuccessfully(true);
                        }
                        listener.success(managerId, messageId, data);
                    }

                    @Override
                    public void failed(String managerId, String messageId, Object data) {
                        listener.failed(managerId, messageId, data);
                    }
                }, req, getManagerId(), METHOD_LOGIN);
    }

    private void setLogInSuccessfully(boolean loginSuccess) {
        this.mLoginSuccess = loginSuccess;
    }

    public final String getManagerId() {
        return MANAGER_ID;
    }
}
