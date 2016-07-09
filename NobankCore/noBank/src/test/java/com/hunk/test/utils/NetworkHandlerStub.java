package com.hunk.test.utils;

import com.hunk.nobank.contract.RealResp;
import com.hunk.nobank.extension.network.BaseReqPackage;
import com.hunk.nobank.extension.network.NetworkHandler;
import com.hunk.nobank.extension.network.ServerError;
import com.hunk.nobank.model.Cacheable;

import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;

import rx.Observable;

/**
 * Stub to mock network layer.
 * You can prepare fake response in this class, we will return them in the sequence you put them.
 */
public class NetworkHandlerStub extends NetworkHandler {
    private Queue<RealResp<?>> prepareList = new ArrayBlockingQueue<>(50);

    @Override
    public <R> Observable<R> fireRequest(BaseReqPackage req) {
        RealResp<R> nextRealResp = (RealResp<R>) prepareList.poll();
        if (isRespSuccessfully(nextRealResp)) {
            if (req instanceof Cacheable) {
                ((Cacheable<R>) req).setCache(nextRealResp.Response, req);
            }
            return Observable.just(nextRealResp.Response);
        } else {
            return Observable.error(new ServerError(nextRealResp.Code));
        }
    }

    public void setNextResponse(RealResp<?> realResp) {
        prepareList.add(realResp);
    }

    public void clear() {
        prepareList.clear();
    }

}
