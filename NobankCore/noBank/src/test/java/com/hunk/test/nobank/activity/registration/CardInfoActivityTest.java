package com.hunk.test.nobank.activity.registration;

import android.content.Intent;

import com.hunk.nobank.R;
import com.hunk.nobank.activity.registration.CardInfoActivity;
import com.hunk.test.utils.NBAbstractTest;
import com.hunk.whitelabel.retailer.RetailerFeatureList;

import org.junit.Test;
import org.robolectric.Robolectric;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.Shadows;
import org.robolectric.shadows.ShadowActivity;

import static org.junit.Assert.assertTrue;

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
        assertTrue(gotoSignUpPage.filterEquals(si.getNextStartedActivity()));
    }
}
