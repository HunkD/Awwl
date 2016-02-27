package com.hunk.nobank.extension.network;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

import com.hunk.nobank.contract.RealResp;
import com.hunk.nobank.manager.dataBasic.ManagerListener;
import com.hunk.nobank.model.Cacheable;
import com.hunk.nobank.util.Logging;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import java.io.IOException;

/**
 * This is a workaround class to make a daley(5s) between each request
 */
public class MyNetworkHandler extends NetworkHandler {

    private final OkHttpClient okHttpClient;
    private Handler handler;
    private ServerConfig serverConfig;

    public MyNetworkHandler(Context ctx) {
        super(ctx);
        handler = new MyHandler();
        okHttpClient = new OkHttpClient();
        serverConfig = ServerConfig.getCurrentServerConfig();
    }

    @Override
    public void fireRequest(final ManagerListener listener, final BaseReqPackage req, final String managerId, final String messageId) {
        String url = req.getUri(serverConfig).toString();
        Request.Builder requestBuilder = new Request.Builder()
                .url(url);
        String json = null;
        if (req.getHttpMethod() == com.android.volley.Request.Method.POST) {
            json = getRealRequest(req.getRequest());
            requestBuilder.post(RequestBody.create(MediaType.parse("text"), json));
        } else {
            requestBuilder.get();
        }

        Logging.d("fire request : " + url);
        Logging.d("fire request data : " + json);
        Request request = requestBuilder.build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                }
                RealResp<?> data = generateVolleyErrorResp(req, e);
                Message message = handler.obtainMessage(0,
                        new Wrapper(listener, data, managerId, messageId));
                handler.sendMessage(message);
            }

            @Override
            public void onResponse(Response response) throws IOException {
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                }
                Message message = handler.obtainMessage();
                String json = response.body().string();
                Logging.d("network response = " + json);
                if (response.isSuccessful()) {
                    RealResp realResp = getRealResponse(json, req);
                    if (isRespSuccessfully(realResp)) {
                        message.what = 1;
                        message.obj = new Wrapper(listener, realResp, managerId, messageId);
                        if (req instanceof Cacheable) {
                            ((Cacheable)req).setCache(realResp, req);
                        }
                    } else {
                        message.what = 0;
                        message.obj = new Wrapper(listener, realResp, managerId, messageId);
                    }
                } else {
                    RealResp<?> data = generateVolleyErrorResp(req, new RuntimeException("response.isSuccessful()" + response.isSuccessful()));

                    message.what = 0;
                    message.obj = new Wrapper(listener, data, managerId, messageId);
                }
                handler.sendMessage(message);
            }
        });
    }

    private RealResp<?> generateVolleyErrorResp(BaseReqPackage req, Exception e) {
        Logging.w("network error: " + e.getMessage());
        RealResp<?> realResp = new RealResp<>();
        realResp.Code = NetworkHandler.NETWORK_ERROR;
        return realResp;
    }

    public static class Wrapper {
        public final String managerId;
        public final ManagerListener listener;
        public final Object realResp;
        public final String messageId;


        public Wrapper(ManagerListener listener, RealResp realResp, String managerId, String messageId) {
            this.listener = listener;
            this.realResp = realResp;
            this.managerId = managerId;
            this.messageId = messageId;
        }
    }

    public static class MyHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            Wrapper wrapper = (Wrapper) msg.obj;
            if (msg.what == 0) {
                wrapper.listener.failed(wrapper.managerId, wrapper.messageId, wrapper.realResp);
            } else {
                wrapper.listener.success(wrapper.managerId, wrapper.messageId, wrapper.realResp);
            }
        }
    }
}
