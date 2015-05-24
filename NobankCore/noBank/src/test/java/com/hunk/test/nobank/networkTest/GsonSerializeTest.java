package com.hunk.test.nobank.networkTest;

import android.os.Build;

import com.hunk.nobank.BuildConfig;
import com.hunk.nobank.Core;
import com.hunk.nobank.contract.LoginResp;
import com.hunk.nobank.contract.RealResp;
import com.hunk.nobank.model.login.LoginReqPackage;
import com.hunk.test.utils.NetworkHandlerStub;
import com.hunk.test.utils.TestNoBankApplication;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import java.util.Arrays;

@RunWith(RobolectricGradleTestRunner.class)
/**Only support JELLY_BEAN and above isn't good :( **/
@Config(constants = BuildConfig.class, emulateSdk = Build.VERSION_CODES.JELLY_BEAN,
        application = TestNoBankApplication.class)
public class GsonSerializeTest {

    private NetworkHandlerStub mNetworkHandlerStub;

    @Before
    public void prepare() {
        // set next response
        mNetworkHandlerStub =
                (NetworkHandlerStub) Core.getInstance().getNetworkHandler();

        mNetworkHandlerStub.clear();
    }

    @Test
    public void testSerializeRealRequest() {
        String expected =
                "{\"Request\":{\"Username\":\"username\",\"Password\":\"userPassword\",\"RememberMe\":false}}";
        LoginReqPackage loginReqPackage = new LoginReqPackage("username", "userPassword", false);

        String jsonReq = mNetworkHandlerStub.getRealRequest(loginReqPackage.getRequest());

        Assert.assertEquals(expected, jsonReq);
    }

    @Test
    public void testDeSerializeRealResponse() {
        LoginReqPackage loginReqPackage = new LoginReqPackage("username", "userPassword", false);

        LoginResp resp = new LoginResp();
        resp.AllAccountIds = Arrays.asList("123", "456");
        resp.NeedSecurityQuestionCheck = true;
        RealResp<LoginResp> realResp = new RealResp<>();
        realResp.Response = resp;
        String json = mNetworkHandlerStub.getGson().toJson(realResp);

        RealResp<LoginResp> realResp1 = mNetworkHandlerStub.getRealResponse(json, loginReqPackage);

        Assert.assertEquals(realResp.Response, realResp1.Response);
        Assert.assertEquals(realResp, realResp1);
    }
}
