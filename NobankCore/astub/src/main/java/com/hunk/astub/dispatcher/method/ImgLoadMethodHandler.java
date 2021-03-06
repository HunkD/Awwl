package com.hunk.astub.dispatcher.method;

import com.hunk.astub.Core;
import com.hunk.astub.FileUtil;
import com.hunk.nobank.contract.ContractGson;
import com.hunk.nobank.contract.RealResp;

import java.io.IOException;

import fi.iki.elonen.NanoHTTPD;

/**
 * @author HunkDeng
 * @since 2016/6/19
 */
public class ImgLoadMethodHandler implements MethodHandler {
    private static final String PARAMETER_ID = "id";
    private Core mCore = Core.getInstance();
    @Override
    public String handle(NanoHTTPD.IHTTPSession session) {
        String id = session.getParms().get(PARAMETER_ID);
        return loadImageStr(id);
    }

    private String loadImageStr(String id) {
        String imgPath = mCore.getImgFolderPath();
        if (imgPath != null) {
            try {
                RealResp<String> realResp = new RealResp<>();
                realResp.Response = FileUtil.encodeBase64File(mCore.getImgFolderPath() + id);
                return ContractGson.getInstance().toJson(realResp);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        // default behavior is return error.
        RealResp<?> realResp = new RealResp<>();
        realResp.Code = -1;
        return ContractGson.getInstance().toJson(realResp);
    }
}
