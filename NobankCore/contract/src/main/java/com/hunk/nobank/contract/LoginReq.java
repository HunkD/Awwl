package com.hunk.nobank.contract;

public class LoginReq {
    public final String Username;
    public final String Password;
    public final boolean RememberMe;

    public LoginReq(String username, String password, boolean rememberMe) {
        Username = username;
        Password = password;
        RememberMe = rememberMe;
    }
}
