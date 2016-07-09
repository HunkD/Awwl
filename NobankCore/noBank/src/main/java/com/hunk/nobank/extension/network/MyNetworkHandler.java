package com.hunk.nobank.extension.network;

import com.hunk.nobank.contract.RealResp;
import com.hunk.nobank.model.Cacheable;
import com.hunk.abcd.extension.log.Logging;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import java.io.IOException;

import rx.Observable;
import rx.Subscriber;
import rx.schedulers.Schedulers;

/**
 * This is a workaround class to make a daley(5s) between each request
 */
public class MyNetworkHandler extends NetworkHandler {

    private final OkHttpClient okHttpClient;
    private ServerConfig serverConfig;

    public MyNetworkHandler() {
        okHttpClient = new OkHttpClient();
        serverConfig = ServerConfig.getCurrentServerConfig();
    }

    @Override
    public <R> Observable<R> fireRequest(final BaseReqPackage req) {
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
        final Request request = requestBuilder.build();
        return Observable.create(new Observable.OnSubscribe<R>() {
            @Override
            public void call(final Subscriber<? super R> subscriber) {
                okHttpClient.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(Request request, IOException e) {
                        // TODO: NanoHTTPD have problem on quickly sequence call, so add interval time here.
                        try {
                            Thread.sleep(5000);
                        } catch (InterruptedException e1) {
                            e1.printStackTrace();
                        }
                        subscriber.onError(e);
                        subscriber.onCompleted();
                    }

                    @Override
                    public void onResponse(Response response) throws IOException {
                        // TODO: NanoHTTPD have problem on quickly sequence call, so add interval time here.
                        try {
                            Thread.sleep(5000);
                        } catch (InterruptedException e1) {
                            e1.printStackTrace();
                        }
                        String json = response.body().string();
                        Logging.d("network response = " + json);
                        if (response.isSuccessful()) {
                            RealResp<R> realResp = getRealResponse(json, req.getResponseType());
                            if (isRespSuccessfully(realResp)) {
                                if (req instanceof Cacheable) {
                                    ((Cacheable<R>)req).setCache(realResp.Response, req);
                                }
                                subscriber.onNext(realResp.Response);
                            } else {
                                subscriber.onError(new ServerError(0));
                            }
                        } else {
                            subscriber.onError(new ServerError(NetworkHandler.NETWORK_ERROR));

                        }
                        subscriber.onCompleted();
                    }
                });
            }
        }).subscribeOn(Schedulers.io());
    }
}
