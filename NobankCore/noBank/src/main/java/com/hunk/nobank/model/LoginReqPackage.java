package com.hunk.nobank.model;

import android.net.Uri;

import com.google.gson.reflect.TypeToken;
import com.hunk.nobank.contract.LoginReq;
import com.hunk.nobank.contract.LoginResp;
import com.hunk.nobank.contract.RealResp;
import com.hunk.nobank.extension.network.BaseReqPackage;
import com.hunk.nobank.extension.network.ServerConfig;

import java.lang.reflect.Type;

public class LoginReqPackage extends BaseReqPackage {

    private final LoginReq req;

    public LoginReqPackage(String username, String password, boolean rememberMe) {
        req = new LoginReq(username, password, rememberMe);
    }

    @Override
    public Type getResponseType() {
        return new TypeToken<RealResp<LoginResp>>(){}.getType();
    }

    @Override
    public Uri getUri(ServerConfig serverConfig) {
        return serverConfig.getUriBuilder()
                .appendEncodedPath("User/Login")
                .build();
    }

    @Override
    public Object getRequest() {
        return req;
    }
}
