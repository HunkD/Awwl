package com.hunk.nobank.manager.flow;

import android.app.Activity;

/**
 * @author HunkDeng
 * @since 2016/2/7
 */
public abstract class ScreenFlow {

    private final ScreenFlowManager mScreenFlowManager;
    private Class<? extends Activity> mStartedActivity;

    public ScreenFlow(ScreenFlowManager screenFlowManager) {
        mScreenFlowManager = screenFlowManager;
    }

    public void start(Activity startActivity) {
        mScreenFlowManager.setCurrentScreenFlow(this);
        this.mStartedActivity = startActivity.getClass();
        // configure screen flow at runtime
        // TODO change it to static?
        configureScreenFlow(mStartedActivity);
        // start next screen
        next(startActivity);
    }

    /**
     * Start next screen which known from configuration.
     * @param activity
     *  previous screen.
     */
    public void next(Activity activity) {

    }

    protected abstract void configureScreenFlow(Class<? extends Activity> startedActivity);
}
