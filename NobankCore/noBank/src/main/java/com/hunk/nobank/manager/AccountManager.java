package com.hunk.nobank.manager;

import com.google.gson.reflect.TypeToken;
import com.hunk.nobank.extension.network.interfaces.BaseResponse;
import com.hunk.nobank.extension.network.interfaces.NBPoster;
import com.hunk.nobank.model.LoginReq;

public class AccountManager {

    public static NBPoster<LoginReq, Object> loginPoster =
            new NBPoster<LoginReq, Object>("login", new TypeToken<BaseResponse<Object>>() {
            });

}
