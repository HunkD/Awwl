package com.hunk.stubserver;

import com.google.gson.Gson;
import com.hunk.nobank.feature.interfaces.BaseResponse;
import com.hunk.nobank.feature.interfaces.Client;

public class StubClient extends Client {

	Dispather dispather = new Dispather();
	
	@Override
	public void post(String url, String json, Callback callback) {
		ActionHandler handler = dispather.dispather(url);
		if (handler != null) {
			String result = handler.execute(json);
			callback.onSuccess(result);	
		} else {
			callback.onFailed();
		}
	}

	@Override
	public String post(String url, String json) {
		ActionHandler handler = dispather.dispather(url);
		if (handler != null) {
			return handler.execute(json);
		} else {
			return new Gson().toJson(new BaseResponse<Object>(false));
		}
	}

}
