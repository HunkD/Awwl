package com.hunk.test.utils;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

import com.hunk.nobank.contract.RealResp;
import com.hunk.nobank.extension.network.BaseReqPackage;
import com.hunk.nobank.extension.network.MyNetworkHandler;
import com.hunk.nobank.extension.network.NetworkHandler;
import com.hunk.nobank.manager.dataBasic.ManagerListener;
import com.hunk.nobank.model.Cacheable;

import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;

public class NetworkHandlerStub extends NetworkHandler {
    private final Handler mHandler;
    private Queue<RealResp<?>> prepareList = new ArrayBlockingQueue<>(50);

    public NetworkHandlerStub(Context ctx) {
        super(ctx);
        mHandler = new MyNetworkHandler.MyHandler();
    }

    @Override
    protected void setupVolley(Context ctx) {
    }

    @Override
    public void fireRequest(final ManagerListener listener, final BaseReqPackage req, final String managerId, final String messageId) {
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Message message = Message.obtain();
                RealResp<?> nextRealResp = prepareList.poll();
                if (isRespSuccessfully(nextRealResp)) {
                    if (req instanceof Cacheable) {
                        ((Cacheable) req).setCache(nextRealResp, req);
                    }
                    message.what = 1;
                } else {
                    message.what = 0;
                }
                message.obj = new MyNetworkHandler.Wrapper(listener, nextRealResp, managerId, messageId);
                mHandler.sendMessage(message);
            }
        }, 100);
    }

    public void setNextResponse(RealResp<?> realResp) {
        prepareList.add(realResp);
    }

    public void clear() {
        prepareList.clear();
    }
}
