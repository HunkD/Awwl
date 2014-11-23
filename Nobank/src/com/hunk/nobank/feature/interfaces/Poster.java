package com.hunk.nobank.feature.interfaces;


public interface Poster<Req extends BaseRequest, Resp extends BaseResponse> {	
	public void fetch(Req req, FetchListener<Resp> listener);
}
