package com.hunk.nobank.feature.interfaces;

import java.util.Collection;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hunk.nobank.NoBankApplication;
import com.hunk.nobank.util.Logging;

public class NBPoster<Req extends BaseRequest, Resp> implements Poster<Req, Resp> {
		
	public NBPoster(String url, TypeToken<BaseResponse<Resp>> resultClazz) {
		super();
		this.resultClazz = resultClazz;
		this.url = url;
	}

	private TypeToken<BaseResponse<Resp>> resultClazz;
	private String url;
	
	@Override
	public void fetch(Req req, final FetchListener<Resp> listener) {
		Client client = NoBankApplication.getInstance().getClient();
		client.post(url, new Gson().toJson(req), new Client.Callback() {

			@Override
			public void onSuccess(String result) {
				Logging.d(result);
				@SuppressWarnings("unchecked")
				BaseResponse<Resp> resp = (BaseResponse<Resp>)new Gson().fromJson(result, resultClazz.getType());
				if (resp.isSuccess)
					listener.onSuccess(resp.result);
				else
					listener.onFailed();
			}

			@Override
			public void onFailed() {
				listener.onFailed();
			}
			
		});
	}

	@Override
	public void setExpireList(Collection<Expireable> getters) {
		// TODO Auto-generated method stub
		
	}
	
	public RequestHandler generate(final Req req) {
		return new RequestHandler() {
			
			@Override
			public BaseResponse<?> sendRequest() {
				return fetch(req);
			}
		};
	}

	@Override
	public BaseResponse<Resp> fetch(Req req) {
		Client client = NoBankApplication.getInstance().getClient();
		String result = client.post(url, new Gson().toJson(req));
		return new Gson().fromJson(result, resultClazz.getType());
	}
}
