package com.hunk.nobank.extension.network;

import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.hunk.nobank.extension.network.interfaces.BaseReqPackage;
import com.hunk.nobank.manager.ManagerListener;
import com.hunk.nobank.util.Logging;

import org.json.JSONObject;

public class NetworkHandler {
    private static final int NETWORK_ERROR = -5000;

    private RequestQueue mQueue;
    private ServerConfig mCurrentServerConfig = new ServerConfig("http://localhost", 8451);

    public NetworkHandler(Context ctx) {
        mQueue = Volley.newRequestQueue(ctx);
    }

    public void setCurrentServerConfig(ServerConfig serverConfig) {
        this.mCurrentServerConfig = serverConfig;
    }

    public void fireRequest(final ManagerListener listener, final BaseReqPackage req, final String managerId, final String messageId) {
        String url = mCurrentServerConfig.getURL() + req.getPath();
        String jsonReq = getRealRequest(req.getRequest());
        // We make the request to json string, so
        JsonObjectRequest stringRequest =
                new JsonObjectRequest(
                        req.getHttpMethod(),
                        url,
                        jsonReq,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                String jsonStr = response.toString();
                                Logging.d("network resp=" + jsonStr);
                                RealResp realResp = getRealResponse(jsonStr, req);
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
        return new Gson();
    }
}
