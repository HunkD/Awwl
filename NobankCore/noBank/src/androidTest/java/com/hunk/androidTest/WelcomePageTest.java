package com.hunk.androidTest;

import android.app.Instrumentation;
import android.test.ActivityInstrumentationTestCase2;

import com.hunk.nobank.activity.LoginPageActivity;
import com.hunk.nobank.activity.WelcomePageActivity;

public class WelcomePageTest extends ActivityInstrumentationTestCase2<WelcomePageActivity> {
    public WelcomePageTest() {
        super(WelcomePageActivity.class);
    }

    public void testOpenWelcomePageTest() throws Throwable {
        final Instrumentation.ActivityMonitor monitor =
                getInstrumentation().addMonitor(LoginPageActivity.class.getName(),
                        null, false);

        final WelcomePageActivity activity = getActivity();
        getInstrumentation().waitForIdleSync();
        activity.forInstrumentTest();

        final LoginPageActivity loginPageActivity = (LoginPageActivity)
                monitor.waitForActivity();

        assertNotNull("LoginPageActivity is null", loginPageActivity);
        getInstrumentation().removeMonitor(monitor);
    }
}
