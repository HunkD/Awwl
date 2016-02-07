package com.hunk.test.nobank.flowTest;

import android.app.Activity;
import android.os.Build;

import com.hunk.nobank.BuildConfig;
import com.hunk.nobank.Core;
import com.hunk.nobank.manager.flow.ScreenFlow;
import com.hunk.test.utils.TestNoBankApplication;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowActivity;

import static org.junit.Assert.assertTrue;
import static org.robolectric.Shadows.shadowOf;

/**
 * @author HunkDeng
 * @since 2016/2/7
 */
@RunWith(RobolectricGradleTestRunner.class)
/**Only support JELLY_BEAN and above isn't good :( **/
@Config(constants = BuildConfig.class,
        application = TestNoBankApplication.class,
        sdk = Build.VERSION_CODES.LOLLIPOP)
public class ScreenFlowTest {
    private static class FirstActivity extends Activity {}
    private static class SecondActivity extends Activity {}
    /**
     * Test start screen flow
     */
    @Test
    public void start() {
        ScreenFlow screenFlow = new ScreenFlow(Core.getInstance().getScreenFlowManager()) {
            @Override
            protected void configureScreenFlow(Class<? extends Activity> startedActivity) {

            }
        };

        FirstActivity firstActivity = new FirstActivity();
        ShadowActivity shadowActivity = shadowOf(firstActivity);
        screenFlow.start(firstActivity);

        assertTrue(Core.getInstance().getScreenFlowManager().getCurrentScreenFlow() == screenFlow);
//        assertEquals(SecondActivity.class.getName(), shadowActivity.getNextStartedActivity().getComponent().getClassName());
    }
}
