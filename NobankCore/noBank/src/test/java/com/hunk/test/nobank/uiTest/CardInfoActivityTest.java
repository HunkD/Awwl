package com.hunk.test.nobank.uiTest;

import android.content.Intent;

import com.hunk.nobank.BuildConfig;
import com.hunk.nobank.R;
import com.hunk.nobank.activity.registration.CardInfoActivity;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;

@RunWith(RobolectricGradleTestRunner.class)
/**Only support JELLY_BEAN and above isn't good :( **/
@Config(constants = BuildConfig.class,
        application = TestNoBankApplication.class,
        sdk = 21)
public class CardInfoActivityTest extends NBAbstractTest {
    @Test
    public void testClickOnSignUpButton() {
        Intent gotoSignUpPage = new Intent();
        gotoSignUpPage.setPackage(RuntimeEnvironment.application.getPackageName());
        gotoSignUpPage.setAction(RetailerFeatureList.Registration.SignUp.ACTION);
        gotoSignUpPage.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        CardInfoActivity activity = Robolectric.setupActivity(CardInfoActivity.class);
        activity.findViewById(R.id.btn_sign_up).performClick();

        ShadowActivity si = Shadows.shadowOf(activity);
        EqualHelper.assertIntentEquals(gotoSignUpPage, si.getNextStartedActivity());
    }
}
