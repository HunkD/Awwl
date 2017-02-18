package com.hunk.nobank.activity.test;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ScrollView;

/**
 * @author HunkDeng
 * @since 2017/2/18
 */

public class DashboardScrollView extends ScrollView {
    private DashboardTop dashboardTop;

    public DashboardScrollView(Context context) {
        this(context, null);
    }

    public DashboardScrollView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DashboardScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setTopView(DashboardTop dashboardTop) {
        this.dashboardTop = dashboardTop;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (dashboardTop == null || getScrollY() > dashboardTop.getMeasuredHeight() || !dashboardTop.handleMotionEvent(ev)) {
            return super.onTouchEvent(ev);
        } else {
            return true;
        }
    }
}
