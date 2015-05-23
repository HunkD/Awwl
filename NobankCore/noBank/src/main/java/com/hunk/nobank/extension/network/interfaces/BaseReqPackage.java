package com.hunk.nobank.extension.network.interfaces;

import com.android.volley.Request;

import java.lang.reflect.Type;

public abstract class BaseReqPackage {

    public abstract String getPath();

    public int getHttpMethod() {
        return Request.Method.POST;
    }

    public Object getRequest() {
        return new Object();
    }

    public abstract Type getResponseType();
}
