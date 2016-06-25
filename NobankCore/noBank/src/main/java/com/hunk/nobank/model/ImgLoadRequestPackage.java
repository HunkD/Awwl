package com.hunk.nobank.model;

import android.net.Uri;

import com.android.volley.Request;
import com.google.gson.reflect.TypeToken;
import com.hunk.nobank.contract.RealResp;
import com.hunk.nobank.extension.network.BaseReqPackage;
import com.hunk.nobank.extension.network.ServerConfig;

import java.lang.reflect.Type;

/**
 * @author HunkDeng
 * @since 2016/6/19
 */
public class ImgLoadRequestPackage extends BaseReqPackage {
    private final String mImgId;

    public ImgLoadRequestPackage(String imgId) {
        this.mImgId = imgId;
    }

    @Override
    public Uri getUri(ServerConfig serverConfig) {
        Uri.Builder builder = serverConfig.getUriBuilder()
                .appendEncodedPath("Image/Load");
        builder.appendQueryParameter("id", mImgId);
        return builder.build();
    }

    @Override
    public Type getResponseType() {
        return new TypeToken<RealResp<String>>(){}.getType();
    }

    @Override
    public int getHttpMethod() {
        return Request.Method.GET;
    }

    @Override
    public boolean requestEquals(BaseReqPackage cachePack) {
        return false;
    }
}
