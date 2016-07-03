package com.hunk.nobank.service.network.api;

import com.hunk.nobank.contract.RealResp;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * @author HunkDeng
 * @since 2016/7/3
 */
public interface ImgApi {
    @GET("Image/Load")
    Call<RealResp<String>> loadImg(@Query("id") String id);
}
