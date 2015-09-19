package com.hunk.test.utils;

import android.content.Context;

import com.hunk.nobank.contract.RealResp;
import com.hunk.nobank.extension.network.BaseReqPackage;
import com.hunk.nobank.extension.network.NetworkHandler;
import com.hunk.nobank.manager.ManagerListener;
import com.hunk.nobank.model.Cacheable;

import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;

public class NetworkHandlerStub extends NetworkHandler {
    private Queue<RealResp<?>> prepareList = new ArrayBlockingQueue<>(50);

    public NetworkHandlerStub(Context ctx) {
        super(ctx);
    }

    @Override
    public void fireRequest(ManagerListener listener, BaseReqPackage req, String managerId, String messageId) {
        RealResp<?> nextRealResp = prepareList.poll();
        if (isRespSuccessfully(nextRealResp)) {
            if (req instanceof Cacheable) {
                ((Cacheable) req).setCache(nextRealResp, req);
            }
            listener.success(managerId, messageId, nextRealResp);
        } else {
            listener.failed(managerId, messageId, nextRealResp);
        }
    }

    public void setNextResponse(RealResp<?> realResp) {
        prepareList.add(realResp);
    }

    public void clear() {
        prepareList.clear();
    }
}
