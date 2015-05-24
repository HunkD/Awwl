package com.hunk.nobank.manager;

import com.google.gson.reflect.TypeToken;
import com.hunk.nobank.extension.network.interfaces.BaseResponse;
import com.hunk.nobank.extension.network.interfaces.NBPoster;
import com.hunk.nobank.model.login.LoginReqPackage;

public class AccountManager {

    public static NBPoster<LoginReqPackage, Object> loginPoster =
            new NBPoster<LoginReqPackage, Object>("login", new TypeToken<BaseResponse<Object>>() {
            });

}
