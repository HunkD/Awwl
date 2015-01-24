package com.hunk.nobank.feature.login.manager;

import com.hunk.nobank.NConstants;
import com.hunk.nobank.core.FeatureManager;

/**
 * Created by HunkDeng on 2015/1/24.
 */
public class LoginManager implements FeatureManager {
    @Override
    public String onHandle(String realAction, String requestData) {
        String result = "";
        if (NConstants.GET_REMEMBER_ME.equals(realAction)) {
            Boolean rememeberMe = isRememeberMe();
            result = rememeberMe.toString();
        }
        return result;
    }

    public boolean isRememeberMe() {
        return false;
    }
}
