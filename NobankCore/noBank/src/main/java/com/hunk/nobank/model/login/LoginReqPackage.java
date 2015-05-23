package com.hunk.nobank.model.login;

import com.google.gson.reflect.TypeToken;
import com.hunk.nobank.extension.network.LoginResp;
import com.hunk.nobank.extension.network.RealResp;
import com.hunk.nobank.extension.network.interfaces.BaseReqPackage;

import java.lang.reflect.Type;

public class LoginReqPackage extends BaseReqPackage {

    private final LoginReq req;

    public LoginReqPackage(String username, String password, boolean rememberMe) {
        req = new LoginReq(username, password, rememberMe);
    }

    @Override
    public String getPath() {
        return "/Login";
    }

    @Override
    public Type getResponseType() {
        return new TypeToken<RealResp<LoginResp>>(){}.getType();
    }

    @Override
    public Object getRequest() {
        return req;
    }

    public static class LoginReq {
        public final String Username;
        public final String Password;
        public final boolean RememberMe;

        public LoginReq(String username, String password, boolean rememberMe) {
            Username = username;
            Password = password;
            RememberMe = rememberMe;
        }
    }
}
