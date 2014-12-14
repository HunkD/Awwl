package com.hunk.nobank.feature.base.manager;

import com.google.gson.reflect.TypeToken;
import com.hunk.nobank.feature.base.model.LoginReq;
import com.hunk.nobank.feature.interfaces.BaseResponse;
import com.hunk.nobank.feature.interfaces.NBPoster;

public class AccountManager {

	public static NBPoster<LoginReq, Object> loginPoster = 
			new NBPoster<LoginReq, Object>("login", new TypeToken<BaseResponse<Object>>(){});

}
