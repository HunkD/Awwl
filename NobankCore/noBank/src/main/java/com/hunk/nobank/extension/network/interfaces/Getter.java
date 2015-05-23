package com.hunk.nobank.extension.network.interfaces;


public interface Getter<Req extends BaseReqPackage, Resp> {
    public void fetch(Req req, FetchListener<Resp> listener);

    public BaseResponse<Resp> fetch(Req req);
}
