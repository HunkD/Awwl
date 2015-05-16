package com.hunk.nobank.model;

import com.hunk.nobank.extension.network.interfaces.BaseRequest;

public class LoginReq extends BaseRequest {
    public String username;
    public String password;
}
