package com.hunk.nobank.activity.test;

import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hunk.abcd.extension.util.ViewHelper;
import com.hunk.nobank.R;
import com.hunk.nobank.activity.BaseActivity;

/**
 * @author HunkDeng
 * @since 2017/2/18
 */
public class TestActivity extends BaseActivity<TestBasePresenter> implements TestView<TestBasePresenter> {
    private LinearLayout dashboardList;
    private DashboardTop dashboardTop;
    private DashboardScrollView dashboardScrollView;

    {
        setPresenter(new TestBasePresenter());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        // find views
        dashboardScrollView = (DashboardScrollView) findViewById(R.id.dashboard_scroll_view);
        dashboardList = (LinearLayout) findViewById(R.id.dashboard_list);
        dashboardTop = (DashboardTop) findViewById(R.id.dashboard_top);
        // setup views
        dashboardScrollView.setTopView(dashboardTop);
        // insert fake data
        for (int i = 0; i < 30; i++) {
            TextView textView = new TextView(this);
            textView.setText("Fake Text + " + i);
            textView.setTextSize(ViewHelper.pxFromDp(this, 18f));
            ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            dashboardList.addView(textView, layoutParams);
        }
    }
}
