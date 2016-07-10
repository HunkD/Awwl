package com.hunk.nobank.model;

import android.net.Uri;

import com.android.volley.Request;
import com.google.gson.reflect.TypeToken;
import com.hunk.nobank.contract.AccountSummary;
import com.hunk.nobank.contract.RealResp;
import com.hunk.nobank.contract.TransactionFields;
import com.hunk.nobank.extension.network.BaseReqPackage;
import com.hunk.nobank.extension.network.ServerConfig;
import com.hunk.nobank.manager.TransactionListCache;

import java.lang.reflect.Type;
import java.util.List;

/**
 *
 */
public class TransactionReqPackage extends BaseReqPackage implements Cacheable<List<TransactionFields>> {
    public static TransactionListCache cache = new TransactionListCache();

    private final AccountSummary mAccountSummary;
    private final long mTimestamp;

    public TransactionReqPackage(AccountSummary accountSummary, long timestamp) {
        this.mAccountSummary = accountSummary;
        this.mTimestamp = timestamp;
    }

    @Override
    public Uri getUri(ServerConfig serverConfig) {
        Uri.Builder builder = serverConfig.getUriBuilder()
                .appendEncodedPath("Transaction/List");
        builder.appendQueryParameter("timestamp", String.valueOf(mTimestamp));
        return builder.build();
    }

    @Override
    public int getHttpMethod() {
        return Request.Method.GET;
    }

    @Override
    public Type getResponseType() {
        return new TypeToken<RealResp<List<TransactionFields>>>(){}.getType();
    }

    @Override
    public boolean requestEquals(BaseReqPackage cachePack) {
        return true;
    }

    @Override
    public void setCache(List<TransactionFields> realResp, BaseReqPackage req) {
        cache.setCache(realResp, req);
    }
}
