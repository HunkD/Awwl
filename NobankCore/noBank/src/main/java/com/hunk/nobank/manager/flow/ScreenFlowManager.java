package com.hunk.nobank.manager.flow;

/**
 * @author HunkDeng
 * @since 2016/2/7
 */
public class ScreenFlowManager {

    private ScreenFlow mCurrentScreenFlow;

    public void setCurrentScreenFlow(ScreenFlow currentScreenFlow) {
        mCurrentScreenFlow = currentScreenFlow;
    }

    public ScreenFlow getCurrentScreenFlow() {
        return mCurrentScreenFlow;
    }
}
