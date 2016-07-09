package com.hunk.nobank.extension.network;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hunk.nobank.contract.ContractGson;
import com.hunk.nobank.contract.RealReq;
import com.hunk.nobank.contract.RealResp;

import java.lang.reflect.Type;

import rx.Observable;

public abstract class NetworkHandler {
    public static final int NETWORK_ERROR = -5000;

    public abstract <R> Observable<R> fireRequest(final BaseReqPackage req);

    public <R> RealResp<R> getRealResponse(String jsonStr, Type responseType) {
        return getGson().fromJson(jsonStr, responseType);
    }

    public String getRealRequest(Object request) {
        RealReq realReq = new RealReq();
        realReq.Request = request;
        return getGson().toJson(realReq);
    }

    public boolean isRespSuccessfully(RealResp<?> realResp) {
        return realResp != null && realResp.Code == 0;
    }

    public Gson getGson() {
        return ContractGson.getInstance();
    }

}
