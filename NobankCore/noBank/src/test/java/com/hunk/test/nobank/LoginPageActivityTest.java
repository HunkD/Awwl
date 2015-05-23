package com.hunk.test.nobank;

import android.content.Intent;
import android.os.Build;
import android.widget.EditText;

import com.hunk.nobank.BuildConfig;
import com.hunk.nobank.Core;
import com.hunk.nobank.NConstants;
import com.hunk.nobank.R;
import com.hunk.nobank.activity.BaseActivity;
import com.hunk.nobank.activity.LoginPageActivity;
import com.hunk.nobank.extension.network.RealResp;
import com.hunk.test.utils.NBAbstractTest;
import com.hunk.test.utils.NetworkHandlerStub;
import com.hunk.test.utils.TestNoBankApplication;
import com.hunk.whitelabel.Feature;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.Shadows;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowActivity;
import org.robolectric.shadows.ShadowLooper;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(RobolectricGradleTestRunner.class)
/**Only support JELLY_BEAN and above isn't good :( **/
@Config(constants = BuildConfig.class, emulateSdk = Build.VERSION_CODES.JELLY_BEAN,
        application = TestNoBankApplication.class)
public class LoginPageActivityTest extends NBAbstractTest {

    private NetworkHandlerStub mNetworkHandlerStub;

    @Before
    public void prepare() {
        // set next response
        mNetworkHandlerStub =
                (NetworkHandlerStub) Core.getInstance().getNetworkHandler();

        mNetworkHandlerStub.clear();
    }
    @Test
    public void testLoginToDashboard() {
        // set Activity fake data
        LoginPageActivity activity = Robolectric.setupActivity(LoginPageActivity.class);

        ((EditText) activity.findViewById(R.id.login_page_input_login_name)).setText("hello");
        ((EditText) activity.findViewById(R.id.login_page_input_password)).setText("psd");


        RealResp realResp = new RealResp();
        mNetworkHandlerStub.setNextResponse(realResp);

        activity.submit();

        // check next started activity
        ShadowLooper.runUiThreadTasksIncludingDelayedTasks();

        ShadowActivity si = Shadows.shadowOf(activity);
        Intent gotoDashboard = new Intent();
        gotoDashboard.setPackage(RuntimeEnvironment.application.getPackageName());
        gotoDashboard.setAction(BaseActivity.generateAction(Feature.dashboard, NConstants.OPEN_MAIN));
        assertThat(si.getNextStartedActivity()).isEqualTo(gotoDashboard);
    }

    @Test
    public void testCheckInput() {
        LoginPageActivity activity = Robolectric.setupActivity(LoginPageActivity.class);

        ((EditText) activity.findViewById(R.id.login_page_input_login_name)).setText("hello");
        ((EditText) activity.findViewById(R.id.login_page_input_password)).setText("psd");

        assertThat(activity.checkInput()).isTrue();
    }
}
