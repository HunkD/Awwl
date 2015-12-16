package com.hunk.test.nobank.uiTest;

import android.content.Intent;
import android.os.Build;
import android.widget.EditText;

import com.hunk.nobank.BuildConfig;
import com.hunk.nobank.Core;
import com.hunk.nobank.NConstants;
import com.hunk.nobank.R;
import com.hunk.nobank.activity.BaseActivity;
import com.hunk.nobank.activity.LoginPageActivity;
import com.hunk.nobank.contract.AccountSummary;
import com.hunk.nobank.contract.LoginResp;
import com.hunk.nobank.contract.RealResp;
import com.hunk.nobank.extension.view.TestDialog;
import com.hunk.test.utils.NBAbstractTest;
import com.hunk.test.utils.NetworkHandlerStub;
import com.hunk.test.utils.TestNoBankApplication;
import com.hunk.whitelabel.Feature;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.Shadows;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowActivity;
import org.robolectric.shadows.ShadowDialog;
import org.robolectric.shadows.ShadowLooper;

import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(RobolectricGradleTestRunner.class)
/**Only support JELLY_BEAN and above isn't good :( **/
@Config(constants = BuildConfig.class,
        application = TestNoBankApplication.class,
        sdk = 21)
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

        LoginResp loginResp = new LoginResp();
        loginResp.NeedSecurityQuestionCheck = false;
        RealResp<LoginResp> realResp = new RealResp<>();
        realResp.Response = loginResp;
        mNetworkHandlerStub.setNextResponse(realResp);

        AccountSummary accountSummary = new AccountSummary();
        accountSummary.Accounts = new ArrayList<>();
        RealResp<AccountSummary> realResp1 = new RealResp<>();
        realResp1.Response = accountSummary;
        mNetworkHandlerStub.setNextResponse(realResp1);

        activity.submit();

        // check next started activity
        ShadowLooper.runUiThreadTasksIncludingDelayedTasks();

        ShadowActivity si = Shadows.shadowOf(activity);
        Intent gotoRootActivity = new Intent();
        gotoRootActivity.setPackage(RuntimeEnvironment.application.getPackageName());
        gotoRootActivity.setAction(BaseActivity.generateAction(Feature.root, NConstants.OPEN_MAIN));
        assertThat(si.getNextStartedActivity()).isEqualTo(gotoRootActivity);
    }

//    @Test
//    public void testLoginToSecurityQuestion() {
//        // set Activity fake data
//        LoginPageActivity activity = Robolectric.setupActivity(LoginPageActivity.class);
//
//        ((EditText) activity.findViewById(R.id.login_page_input_login_name)).setText("hello");
//        ((EditText) activity.findViewById(R.id.login_page_input_password)).setText("psd");
//
//        LoginResp loginResp = new LoginResp();
//        loginResp.NeedSecurityQuestionCheck = true;
//        RealResp<LoginResp> realResp = new RealResp<>();
//        realResp.Response = loginResp;
//        mNetworkHandlerStub.setNextResponse(realResp);
//
//        activity.submit();
//
//        // check next started activity
//        ShadowLooper.runUiThreadTasksIncludingDelayedTasks();
//
//        ShadowActivity si = Shadows.shadowOf(activity);
//        Intent gotoDashboard = new Intent();
//        gotoDashboard.setPackage(RuntimeEnvironment.application.getPackageName());
//        gotoDashboard.setAction(BaseActivity.generateAction(Feature.dashboard, NConstants.OPEN_MAIN));
//        assertThat(si.getNextStartedActivity()).isEqualTo(gotoDashboard);
//    }

    @Test
    public void testCheckInput() {
        LoginPageActivity activity = Robolectric.setupActivity(LoginPageActivity.class);

        ((EditText) activity.findViewById(R.id.login_page_input_login_name)).setText("hello");
        ((EditText) activity.findViewById(R.id.login_page_input_password)).setText("psd");

        assertThat(activity.checkInput()).isTrue();
    }

    @Test
    public void testDialog() {
        // set Activity fake data
        LoginPageActivity activity = Robolectric.setupActivity(LoginPageActivity.class);

        ((EditText) activity.findViewById(R.id.login_page_input_login_name)).setText("hello");
        ((EditText) activity.findViewById(R.id.login_page_input_password)).setText("psd");

        activity.submit();

        TestDialog dialog = (TestDialog) ShadowDialog.getLatestDialog();
        Assert.assertNotNull(dialog);
        Assert.assertEquals(activity.getString(R.string.loading_message),
                dialog.getMsgView().getText().toString());
    }
}
