package com.hunk.nobank.model;

import android.net.Uri;

import com.android.volley.Request;
import com.google.gson.reflect.TypeToken;
import com.hunk.nobank.contract.adapter.DateAdapter;
import com.hunk.nobank.contract.RealResp;
import com.hunk.nobank.contract.TransactionFields;
import com.hunk.nobank.extension.network.BaseReqPackage;
import com.hunk.nobank.extension.network.ServerConfig;

import java.lang.reflect.Type;
import java.util.Date;
import java.util.List;

/**
 *
 */
public class TransListReqPackage extends BaseReqPackage {

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    private Date timestamp;

    @Override
    public Uri getUri(ServerConfig serverConfig) {
        return serverConfig.getUriBuilder()
                .appendEncodedPath("Account/TransactionList")
                .appendQueryParameter("timestamp", DateAdapter.formatDateToString(timestamp))
                .build();
    }

    @Override
    public Type getResponseType() {
        return new TypeToken<RealResp<List<TransactionFields>>>(){}.getType();
    }

    @Override
    public int getHttpMethod() {
        return Request.Method.GET;
    }
}
