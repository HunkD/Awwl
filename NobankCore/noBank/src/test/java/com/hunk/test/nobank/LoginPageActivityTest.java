package com.hunk.test.nobank;

import android.content.Intent;
import android.os.Build;
import android.widget.EditText;

import com.hunk.nobank.BaseActivity;
import com.hunk.nobank.BuildConfig;
import com.hunk.nobank.NConstants;
import com.hunk.nobank.R;
import com.hunk.nobank.feature.Feature;
import com.hunk.nobank.feature.login.activity.LoginPageActivity;
import com.hunk.test.utils.NBAbstractTest;
import com.hunk.test.utils.TestNoBankApplication;

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
    @Test
    public void testLoginToDashboard() {
        Intent gotoDashboard = new Intent();
        gotoDashboard.setPackage(RuntimeEnvironment.application.getPackageName());
        gotoDashboard.setAction(BaseActivity.generateAction(Feature.dashboard, NConstants.OPEN_MAIN));

        LoginPageActivity activity = Robolectric.setupActivity(LoginPageActivity.class);

        ((EditText)activity.findViewById(R.id.login_page_input_login_name)).setText("hello");
        ((EditText)activity.findViewById(R.id.login_page_input_password)).setText("psd");

        activity.doLogin(activity.getLoginReq());

        ShadowLooper.runUiThreadTasksIncludingDelayedTasks();

        ShadowActivity si = Shadows.shadowOf(activity);
        assertThat(si.getNextStartedActivity()).isEqualTo(gotoDashboard);
    }

    @Test
    public void testCheckInput() {
        LoginPageActivity activity = Robolectric.setupActivity(LoginPageActivity.class);

        ((EditText)activity.findViewById(R.id.login_page_input_login_name)).setText("hello");
        ((EditText)activity.findViewById(R.id.login_page_input_password)).setText("psd");

        assertThat(activity.checkInput()).isTrue();
    }
}
