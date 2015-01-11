package com.hunk.nobank.feature.interfaces;

import java.util.Collection;


public interface Poster<Req extends BaseRequest, Resp> extends Getter<Req, Resp> {	
	public void setExpireList(Collection<Expireable> getters);
}
