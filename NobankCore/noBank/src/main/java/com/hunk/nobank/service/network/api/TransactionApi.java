package com.hunk.nobank.service.network.api;

import com.hunk.nobank.contract.RealResp;
import com.hunk.nobank.contract.TransactionFields;

import java.util.List;

import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * @author HunkDeng
 * @since 2016/7/3
 */
public interface TransactionApi {
    @GET("Transaction/List")
    RealResp<List<TransactionFields>> transactions(@Query("timestamp") long timestamp);
}
