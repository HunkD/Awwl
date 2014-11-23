package com.hunk.nobank.feature.base.manager;

import com.hunk.nobank.feature.base.model.LoginReq;
import com.hunk.nobank.feature.base.model.LoginResp;
import com.hunk.nobank.feature.interfaces.NBPoster;
import com.hunk.nobank.feature.interfaces.Poster;

public class AccountManager {

	public static Poster<LoginReq, LoginResp> loginPoster = 
			new NBPoster<LoginReq, LoginResp>("login", LoginResp.class);

}
