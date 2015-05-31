package com.hunk.nobank.extension.network;

import android.content.Context;
import android.net.Uri;

import com.android.volley.AuthFailureError;
import com.android.volley.Cache;
import com.android.volley.Network;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.hunk.nobank.contract.ContractGson;
import com.hunk.nobank.contract.RealReq;
import com.hunk.nobank.contract.RealResp;
import com.hunk.nobank.manager.ManagerListener;
import com.hunk.nobank.util.Logging;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

public class NetworkHandler {
    public static final int NETWORK_ERROR = -5000;

    private RequestQueue mQueue;

    public ServerConfig getCurrentServerConfig() {
        return mCurrentServerConfig;
    }

    private ServerConfig mCurrentServerConfig = new ServerConfig("http", "localhost", 8466);

    public NetworkHandler(Context ctx) {
        mQueue = Volley.newRequestQueue(ctx, null, 1024*1024);
    }

    public void setCurrentServerConfig(ServerConfig serverConfig) {
        this.mCurrentServerConfig = serverConfig;
    }

    public void fireRequest(final ManagerListener listener, final BaseReqPackage req, final String managerId, final String messageId) {
        Uri uri = req.getUri(mCurrentServerConfig);
        Logging.d("request to url : " + uri.toString());
        String jsonReq = null;
        if (req.getHttpMethod() == Request.Method.POST) {
//            jsonReq = getRealRequest(req.getRequest());
        }

        StringRequest stringRequest =
                new MyStringRequest(
                        req.getHttpMethod(),
                        uri.toString(),
                        jsonReq,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                Logging.d("network resp=" + response);
                                RealResp realResp = getRealResponse(response, req);
                                if (isRespSuccessfully(realResp)) {
                                    listener.success(managerId, messageId, realResp);
                                } else {
                                    listener.failed(managerId, messageId, realResp);
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                listener.failed(managerId, messageId, generateVolleyErrorResp(req, error));
                            }
                        }
                );
        // Add the request to the RequestQueue.
        mQueue.add(stringRequest);
    }

    public RealResp getRealResponse(String jsonStr, BaseReqPackage req) {
        return getGson().fromJson(jsonStr, req.getResponseType());
    }

    public String getRealRequest(Object request) {
        RealReq realReq = new RealReq();
        realReq.Request = request;
        return getGson().toJson(realReq);
    }

    private Object generateVolleyErrorResp(BaseReqPackage req, VolleyError error) {
        Logging.w("network cost time: " + error.getNetworkTimeMs());
        Logging.w("network error: " + error.getMessage());
        RealResp<?> realResp = new RealResp<>();
        realResp.Code = NetworkHandler.NETWORK_ERROR;
        return realResp;
    }

    public boolean isRespSuccessfully(RealResp<?> realResp) {
        return realResp != null && realResp.Code == 0;
    }

    public Gson getGson() {
        return ContractGson.getInstance();
    }

    public static class MyStringRequest extends StringRequest {


        private static final String PROTOCOL_CHARSET = "utf-8";
        private final String json;

        public MyStringRequest(int method, String url, String json, Response.Listener<String> listener, Response.ErrorListener errorListener) {
            super(method, url, listener, errorListener);
            this.json = json;
        }

        @Override
        public byte[] getBody() throws AuthFailureError {
            try {
                return json == null ? super.getBody() : json.getBytes(PROTOCOL_CHARSET);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}
