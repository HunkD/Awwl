package com.hunk.nobank.model;

import android.net.Uri;

import com.android.volley.Request;
import com.google.gson.reflect.TypeToken;
import com.hunk.nobank.contract.AccountSummary;
import com.hunk.nobank.contract.RealResp;
import com.hunk.nobank.extension.network.BaseReqPackage;
import com.hunk.nobank.extension.network.ServerConfig;

import java.lang.reflect.Type;

/**
 *
 */
public class AccountSummaryPackage extends BaseReqPackage {

    @Override
    public Uri getUri(ServerConfig serverConfig) {
        return serverConfig.getUriBuilder()
                .appendEncodedPath("Account/Summary")
                .build();
    }

    @Override
    public Type getResponseType() {
        return new TypeToken<RealResp<AccountSummary>>(){}.getType();
    }

    @Override
    public int getHttpMethod() {
        return Request.Method.GET;
    }
}
