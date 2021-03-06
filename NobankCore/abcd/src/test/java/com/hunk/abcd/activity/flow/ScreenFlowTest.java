package com.hunk.abcd.activity.flow;

import android.app.Activity;

import com.hunk.abcd.Testable;

import org.junit.Test;
import org.robolectric.shadows.ShadowActivity;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.robolectric.Robolectric.buildActivity;
import static org.robolectric.Shadows.shadowOf;

/**
 * @author HunkDeng
 * @since 2016/2/7
 */
public class ScreenFlowTest extends Testable {
    private static class FirstActivity extends Activity {}
    private static class SecondActivity extends Activity {}
    private static class ThirdActivity extends Activity {}
    private static class FourthActivity extends Activity {}

    final ScreenFlowManager mScreenFlowManager = new ScreenFlowManager();
    /**
     * Test start screen flow
     */
    @Test
    public void start() {
        // prepare fake screen flow

        ScreenFlow screenFlow = new ScreenFlow(mScreenFlowManager) {
            @Override
            protected void configureScreenFlow(Class<? extends Activity> startedActivity) {
                from(FirstActivity.class).when(ScreenNode.NORMAL_COMPLETE).to(SecondActivity.class);
                from(SecondActivity.class).when(ScreenNode.NORMAL_COMPLETE).to(ThirdActivity.class);
            }
        };

        // test next started screen from FirstActivity
        FirstActivity firstActivity = buildActivity(FirstActivity.class).setup().get();
        ShadowActivity shadowFirstActivity = shadowOf(firstActivity);
        screenFlow.start(firstActivity);

        assertTrue(mScreenFlowManager.getCurrentScreenFlow() == screenFlow);
        assertEquals(SecondActivity.class.getName(), shadowFirstActivity.getNextStartedActivity().getComponent().getClassName());

        // test next started screen from SecondActivity
        SecondActivity secondActivity = buildActivity(SecondActivity.class).setup().get();
        ShadowActivity shadowSecondActivity = shadowOf(secondActivity);
        screenFlow.next(secondActivity);

        assertEquals(ThirdActivity.class.getName(), shadowSecondActivity.getNextStartedActivity().getComponent().getClassName());
    }
    /**
     *
     */
    @Test
    public void startWithCondition() {
        ScreenFlow screenFlow = new FakeScreenFlow1(mScreenFlowManager);

        // test next started screen from FirstActivity
        FirstActivity firstActivity = buildActivity(FirstActivity.class).setup().get();

        ShadowActivity shadowFirstActivity = shadowOf(firstActivity);
        screenFlow.start(firstActivity, FakeScreenFlow1.THIRD_CONDITION);

        assertTrue(mScreenFlowManager.getCurrentScreenFlow() == screenFlow);
        assertEquals(ThirdActivity.class.getName(), shadowFirstActivity.getNextStartedActivity().getComponent().getClassName());
    }

    /**
     * @see #startWithCondition()
     */
    static class FakeScreenFlow1 extends ScreenFlow {
        public static final Integer THIRD_CONDITION = ScreenNode.NORMAL_COMPLETE + 1;

        public FakeScreenFlow1(ScreenFlowManager screenFlowManager) {
            super(screenFlowManager);
        }

        @Override
        protected void configureScreenFlow(Class<? extends Activity> startedActivity) {
            from(FirstActivity.class).when(ScreenNode.NORMAL_COMPLETE).to(SecondActivity.class);
            from(FirstActivity.class).when(THIRD_CONDITION).to(ThirdActivity.class);
        }
    }

    @Test
    public void nextWithCondition() {
        ScreenFlow screenFlow = new FakeScreenFlow2(mScreenFlowManager);

        // test next started screen from FirstActivity
        FirstActivity firstActivity = buildActivity(FirstActivity.class).setup().get();
        ShadowActivity shadowFirstActivity = shadowOf(firstActivity);
        screenFlow.start(firstActivity, ScreenNode.NORMAL_COMPLETE);

        assertEquals(SecondActivity.class.getName(), shadowFirstActivity.getNextStartedActivity().getComponent().getClassName());

        // 
        SecondActivity secondActivity = buildActivity(SecondActivity.class).setup().get();
        ShadowActivity shadowSecondActivity = shadowOf(secondActivity);
        screenFlow.next(secondActivity, FakeScreenFlow2.THIRD_CONDITION);

        assertEquals(FourthActivity.class.getName(), shadowSecondActivity.getNextStartedActivity().getComponent().getClassName());
    }

    static class FakeScreenFlow2 extends ScreenFlow {
        public static final Integer THIRD_CONDITION = ScreenNode.NORMAL_COMPLETE + 1;

        public FakeScreenFlow2(ScreenFlowManager screenFlowManager) {
            super(screenFlowManager);
        }

        @Override
        protected void configureScreenFlow(Class<? extends Activity> startedActivity) {
            from(FirstActivity.class).when(ScreenNode.NORMAL_COMPLETE).to(SecondActivity.class);
            from(SecondActivity.class).when(ScreenNode.NORMAL_COMPLETE).to(ThirdActivity.class);
            from(SecondActivity.class).when(THIRD_CONDITION).to(FourthActivity.class);
        }
    }
}
