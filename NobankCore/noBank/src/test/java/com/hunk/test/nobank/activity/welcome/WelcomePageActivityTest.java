package com.hunk.test.nobank.activity.welcome;

import android.content.Intent;
import android.view.View;

import com.hunk.nobank.BuildConfig;
import com.hunk.nobank.R;
import com.hunk.nobank.activity.LoginPageActivity;
import com.hunk.nobank.activity.welcome.WelcomePageActivity;
import com.hunk.test.utils.EqualHelper;
import com.hunk.test.utils.NBAbstractTest;
import com.hunk.test.utils.TestNoBankApplication;
import com.hunk.whitelabel.retailer.RetailerFeatureList;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.Shadows;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowActivity;

import static org.junit.Assert.assertEquals;

/**
 * Please set the working directory for this unit test configuration is 'NobankCore/nobank'
 */
@RunWith(RobolectricGradleTestRunner.class)
/**Only support JELLY_BEAN and above isn't good :( **/
@Config(constants = BuildConfig.class,
        application = TestNoBankApplication.class,
        sdk = 21)
public class WelcomePageActivityTest extends NBAbstractTest {

    @Test
    public void testClickOnSignInButton() {
        Intent gotoLogin = new Intent();
        gotoLogin.setPackage(RuntimeEnvironment.application.getPackageName());
        gotoLogin.setAction(LoginPageActivity.ACTION);
        gotoLogin.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        WelcomePageActivity activity = Robolectric.setupActivity(WelcomePageActivity.class);
        activity.onClickSignIn(null);

        ShadowActivity si = Shadows.shadowOf(activity);
        EqualHelper.assertIntentEquals(gotoLogin, si.getNextStartedActivity());
    }

    @Test
    public void testClickOnSignOnButton() {
        Intent gotoRegistration = new Intent();
        gotoRegistration.setPackage(RuntimeEnvironment.application.getPackageName());
        gotoRegistration.setAction(RetailerFeatureList.Registration.CardInfo.ACTION);
        gotoRegistration.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        WelcomePageActivity activity = Robolectric.setupActivity(WelcomePageActivity.class);
        activity.onClickSignUp(null);

        ShadowActivity si = Shadows.shadowOf(activity);
        EqualHelper.assertIntentEquals(gotoRegistration, si.getNextStartedActivity());
    }

    @Test
    public void testShowSignUp() {
        WelcomePageActivity activity = Robolectric.setupActivity(WelcomePageActivity.class);
        activity.showSignUp(true);
        assertEquals(View.VISIBLE,
                activity.findViewById(R.id.welcome_btn_sign_up).getVisibility());

        activity.showSignUp(false);
        assertEquals(View.GONE,
                activity.findViewById(R.id.welcome_btn_sign_up).getVisibility());
    }
}
