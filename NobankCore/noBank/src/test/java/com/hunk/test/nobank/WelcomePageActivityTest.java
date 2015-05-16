package com.hunk.test.nobank;

import android.content.Intent;
import android.os.Build;

import com.hunk.nobank.BaseActivity;
import com.hunk.nobank.BuildConfig;
import com.hunk.nobank.NConstants;
import com.hunk.nobank.R;
import com.hunk.nobank.feature.Feature;
import com.hunk.nobank.feature.welcome.activity.WelcomePageActivity;
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

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Please set the working directory for this unit test configuration is 'NobankCore/nobank'
 */
@RunWith(RobolectricGradleTestRunner.class)
/**Only support JELLY_BEAN and above isn't good :( **/
@Config(constants = BuildConfig.class, emulateSdk = Build.VERSION_CODES.JELLY_BEAN,
        application = TestNoBankApplication.class)
public class WelcomePageActivityTest extends NBAbstractTest {

    @Test
    public void testClickOnSignInButton() {
        Intent gotoLogin = new Intent();
        gotoLogin.setPackage(RuntimeEnvironment.application.getPackageName());
        gotoLogin.setAction(BaseActivity.generateAction(Feature.login, NConstants.OPEN_MAIN));

        WelcomePageActivity activity = Robolectric.setupActivity(WelcomePageActivity.class);
        activity.findViewById(R.id.welcome_btn_sign_in).performClick();

        ShadowActivity si = Shadows.shadowOf(activity);
        assertThat(si.getNextStartedActivity()).isEqualTo(gotoLogin);
    }

    @Test
    public void testClickOnSignOnButton() {
        Intent gotoRegistration = new Intent();
        gotoRegistration.setPackage(RuntimeEnvironment.application.getPackageName());
        gotoRegistration.setAction(BaseActivity.generateAction(Feature.registration, NConstants.OPEN_MAIN));

        WelcomePageActivity activity = Robolectric.setupActivity(WelcomePageActivity.class);
        activity.findViewById(R.id.welcome_btn_sign_up).performClick();

        ShadowActivity si = Shadows.shadowOf(activity);
        assertThat(si.getNextStartedActivity()).isEqualTo(gotoRegistration);
    }
}
