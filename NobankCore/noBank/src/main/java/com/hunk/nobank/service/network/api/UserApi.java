package com.hunk.nobank.service.network.api;

import com.hunk.nobank.contract.AccountSummary;
import com.hunk.nobank.contract.LoginReq;
import com.hunk.nobank.contract.LoginResp;
import com.hunk.nobank.contract.RealResp;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;

/**
 * @author HunkDeng
 * @since 2016/7/3
 */
public interface UserApi {
    @POST("User/Login")
    Call<RealResp<LoginResp>> login(LoginReq loginReq);

    @GET("User/Summary")
    Call<RealResp<AccountSummary>> accountSummary();
}
