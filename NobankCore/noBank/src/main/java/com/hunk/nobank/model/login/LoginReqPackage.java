package com.hunk.nobank.model.login;

import com.google.gson.reflect.TypeToken;
import com.hunk.nobank.contract.LoginReq;
import com.hunk.nobank.contract.LoginResp;
import com.hunk.nobank.contract.RealResp;
import com.hunk.nobank.extension.network.interfaces.BaseReqPackage;

import java.lang.reflect.Type;

public class LoginReqPackage extends BaseReqPackage {

    private final LoginReq req;

    public LoginReqPackage(String username, String password, boolean rememberMe) {
        req = new LoginReq(username, password, rememberMe);
    }

    @Override
    public String getPath() {
        return "/User/Login";
    }

    @Override
    public Type getResponseType() {
        return new TypeToken<RealResp<LoginResp>>(){}.getType();
    }

    @Override
    public Object getRequest() {
        return req;
    }
}
