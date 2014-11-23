package com.hunk.stubserver;

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

}
