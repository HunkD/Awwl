package com.hunk.astub.dispatcher.method;

import com.hunk.nobank.contract.ContractGson;
import com.hunk.nobank.contract.LoginResp;
import com.hunk.nobank.contract.RealResp;
import com.hunk.nobank.contract.type.LoginStateEnum;

import java.util.Arrays;

import fi.iki.elonen.NanoHTTPD;

/**
 *
 */
public class LoginMethodHandler implements MethodHandler {
    @Override
    public String handle(NanoHTTPD.IHTTPSession session) {
        //"{\"Code\":0,\"Response\":{\"NeedSecurityQuestionCheck\":true,\"AllAccountIds\":[\"123\",\"456\"]}}"
        RealResp<LoginResp> loginRespRealResp = new RealResp<>();
        LoginResp loginResp = new LoginResp();
        loginResp.AllAccountIds = Arrays.asList("123", "456");
        loginResp.loginState = LoginStateEnum.UnAuthorized;
        loginRespRealResp.Response = loginResp;
        return ContractGson.getInstance().toJson(loginRespRealResp);
    }
}
