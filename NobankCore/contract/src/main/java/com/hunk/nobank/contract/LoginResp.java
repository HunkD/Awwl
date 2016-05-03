package com.hunk.nobank.contract;

import com.hunk.nobank.contract.type.LoginStateEnum;

import java.util.List;

public class LoginResp {
    public LoginStateEnum loginState;
    public List<String> AllAccountIds;
}
