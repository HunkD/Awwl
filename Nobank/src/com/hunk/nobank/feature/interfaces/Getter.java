package com.hunk.nobank.feature.interfaces;

import java.util.Collection;

public interface Getter<Req extends BaseRequest, Resp extends BaseResponse> extends Poster<Req, Resp> {	
	public void setExpireList(Collection<Expireable> getters);
}
