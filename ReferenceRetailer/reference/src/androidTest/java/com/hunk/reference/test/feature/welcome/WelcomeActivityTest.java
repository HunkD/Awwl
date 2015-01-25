package com.hunk.reference.test.feature.welcome;

import android.app.Instrumentation;
import android.test.ActivityInstrumentationTestCase2;
import android.view.View;

import com.hunk.nobank.feature.welcome.activity.WelcomePageActivity;
import com.hunk.nobank.util.Logging;
import com.hunk.reference.R;

/**
 * Created by HunkDeng on 2015/1/25.
 */
public class WelcomeActivityTest extends ActivityInstrumentationTestCase2<WelcomePageActivity> {

    public WelcomeActivityTest() {
        super(WelcomePageActivity.class);
    }

    public void testInit() {
        final WelcomePageActivity act = getActivity();
        Logging.d("run test operation 1." + Thread.currentThread().getId());
        act.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                View btnSignUp = act.findViewById(R.id.welcome_btn_sign_up);
                btnSignUp.performClick();
            }
        });
        Logging.d("run test operation 2." + Thread.currentThread().getId());
        getInstrumentation().waitForIdleSync();
        Logging.d("run test operation 3." + Thread.currentThread().getId());

    }
}
