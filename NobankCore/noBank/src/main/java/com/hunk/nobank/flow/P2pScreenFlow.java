package com.hunk.nobank.flow;

import android.app.Activity;

import com.hunk.nobank.manager.flow.ScreenFlow;
import com.hunk.nobank.manager.flow.ScreenFlowManager;

/**
 * @author HunkDeng
 * @since 2016/2/7
 */
public class P2pScreenFlow extends ScreenFlow {
    public P2pScreenFlow(ScreenFlowManager screenFlowManager) {
        super(screenFlowManager);
    }

    @Override
    protected void configureScreenFlow(Class<? extends Activity> startedActivity) {

    }
}
