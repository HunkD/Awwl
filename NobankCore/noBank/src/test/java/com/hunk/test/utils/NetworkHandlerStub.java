package com.hunk.test.utils;

import android.content.Context;

import com.hunk.nobank.extension.network.NetworkHandler;
import com.hunk.nobank.contract.RealResp;
import com.hunk.nobank.extension.network.BaseReqPackage;
import com.hunk.nobank.manager.ManagerListener;

import java.util.Stack;

public class NetworkHandlerStub extends NetworkHandler {
    private Stack<RealResp<?>> prepareList = new Stack<>();

    public NetworkHandlerStub(Context ctx) {
        super(ctx);
    }

    @Override
    public void fireRequest(ManagerListener listener, BaseReqPackage req, String managerId, String messageId) {
        RealResp<?> nextRealResp = prepareList.pop();
        if (isRespSuccessfully(nextRealResp)) {
            listener.success(managerId, messageId, nextRealResp);
        } else {
            listener.failed(managerId, messageId, nextRealResp);
        }
    }

    public void setNextResponse(RealResp<?> realResp) {
        prepareList.push(realResp);
    }

    public void clear() {
        prepareList.clear();
    }
}
