package com.hunk.nobank.extension.network;


import android.net.Uri;

import com.android.volley.Request;
import com.google.gson.Gson;
import com.hunk.nobank.contract.ContractGson;

import java.lang.reflect.Type;

/**
 *  business logic field to network request
 */
public abstract class BaseReqPackage {

    public Uri getUri() {
        ServerConfig serverConfig = ServerConfig.getCurrentServerConfig();
        return getUri(serverConfig);
    }

    public abstract Uri getUri(ServerConfig serverConfig);

    public int getHttpMethod() {
        return Request.Method.POST;
    }

    public Object getRequest() {
        return new Object();
    }

    public abstract Type getResponseType();

    public boolean requestEquals(BaseReqPackage cachePack) {
        Gson gson = ContractGson.getInstance();

        return getUri().toString().equals(cachePack.getUri().toString())
                && gson.toJson(getRequest()).equals(gson.toJson(cachePack.getRequest()));
    }
}
