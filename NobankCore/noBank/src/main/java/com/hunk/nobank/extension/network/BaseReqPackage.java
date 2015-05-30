package com.hunk.nobank.extension.network;


import android.net.Uri;

import com.android.volley.Request;

import java.lang.reflect.Type;

public abstract class BaseReqPackage {

    public abstract Uri getUri(ServerConfig serverConfig);

    public int getHttpMethod() {
        return Request.Method.POST;
    }

    public Object getRequest() {
        return new Object();
    }

    public abstract Type getResponseType();
}
