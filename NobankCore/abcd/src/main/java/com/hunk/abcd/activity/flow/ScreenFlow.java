package com.hunk.abcd.activity.flow;

import android.app.Activity;
import android.content.Intent;

import java.util.HashMap;
import java.util.Map;

/**
 * @author HunkDeng
 * @since 2016/2/7
 */
public abstract class ScreenFlow {

    private final ScreenFlowManager mScreenFlowManager;
    private Map<Class<? extends Activity>, ScreenNode> mScreenNodeMap = new HashMap<>();

    public ScreenFlow(ScreenFlowManager screenFlowManager) {
        mScreenFlowManager = screenFlowManager;
    }

    /**
     * Start screen flow
     * @param startActivity
     */
    public void start(Activity startActivity, Integer condition) {
        mScreenFlowManager.setCurrentScreenFlow(this);
        // configure screen flow at runtime
        // TODO change it to static?
        configureScreenFlow(startActivity.getClass());
        // start next screen
        next(startActivity, condition);
    }

    /**
     * Start screen flow
     * @param startActivity
     * @see #start(Activity, Integer)
     */
    public void start(Activity startActivity) {
        start(startActivity, ScreenNode.NORMAL_COMPLETE);
    }

    /**
     * Start next screen which known from configuration.
     * @param activity
     *  previous screen.
     */
    public void next(Activity activity, Integer condition) {
        // get current screen node
        ScreenNode screenNode = mScreenNodeMap.get(activity.getClass());
        ScreenNode nextScreenNode = screenNode.getNext(condition);
        Class<? extends Activity> nextActivityClass = nextScreenNode.getCurrent();
        // start next screen
        Intent intent = new Intent(activity, nextActivityClass);
        activity.startActivity(intent);
    }

    /**
     * Start next screen which known from configuration.
     * @param activity
     * @see #next(Activity, Integer)
     */
    public void next(Activity activity) {
        next(activity, ScreenNode.NORMAL_COMPLETE);
    }

    /**
     * Current screen node which hold next step information
     * @param activityClass
     * @return
     */
    public ScreenNode from(Class<? extends Activity> activityClass) {
        ScreenNode screenNode;
        if (mScreenNodeMap.containsKey(activityClass)) {
            screenNode = mScreenNodeMap.get(activityClass);
        } else {
            screenNode = new ScreenNode();
            screenNode.to(activityClass);
            mScreenNodeMap.put(activityClass, screenNode);
        }
        return screenNode;
    }

    protected abstract void configureScreenFlow(Class<? extends Activity> startedActivity);
}
