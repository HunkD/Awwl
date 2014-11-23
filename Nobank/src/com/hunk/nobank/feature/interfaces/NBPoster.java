package com.hunk.nobank.feature.interfaces;

import com.google.gson.Gson;
import com.hunk.nobank.NoBankApplication;
import com.hunk.nobank.util.Logging;

public class NBPoster<Req extends BaseRequest, Resp extends BaseResponse> implements Poster<Req, Resp> {
		
	public NBPoster(String url, Class<Resp> resultClazz) {
		super();
		this.resultClazz = resultClazz;
		this.url = url;
	}

	private Class<Resp> resultClazz;
	private String url;
	
	@Override
	public void fetch(Req req, final FetchListener<Resp> listener) {
		Client client = NoBankApplication.getInstance().getClient();
		client.post(url, new Gson().toJson(req), new Client.Callback() {

			@Override
			public void onSuccess(String result) {
				Logging.d(result);
				Resp resp = (Resp)new Gson().fromJson(result, resultClazz);
				if (resp.isSuccess)
					listener.onSuccess(resp);
				else
					listener.onFailed();
			}

			@Override
			public void onFailed() {
				listener.onFailed();
			}
			
		});
	}
	
}
