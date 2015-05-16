package com.hunk.stubserver.feature;

import com.google.gson.Gson;
import com.hunk.nobank.extension.network.interfaces.BaseResponse;
import com.hunk.stubserver.ActionHandler;

public class LoginActionHandler implements ActionHandler {

    @Override
    public String execute(String json) {
        try {
            Thread.sleep(5 * 1000);
            return new Gson().toJson(new BaseResponse<Object>(true));
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

}
