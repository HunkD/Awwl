package com.hunk.nobank.activity.test;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hunk.abcd.extension.util.ViewHelper;
import com.hunk.nobank.R;
import com.hunk.nobank.activity.BaseActivity;

import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;

/**
 * @author HunkDeng
 * @since 2017/2/18
 */
public class TestActivity extends BaseActivity<TestBasePresenter> implements TestView<TestBasePresenter> {
    private LinearLayout dashboardList;
    private DashboardTop dashboardTop;
    private DashboardScrollView dashboardScrollView;
    private PtrFrameLayout ptrFrame;
    private Toast ptrToast;

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
        ptrToast = Toast.makeText(TestActivity.this, "Pull To Refresh!", Toast.LENGTH_LONG);
        // pull to refresh header setting
        ptrFrame = (PtrFrameLayout) findViewById(R.id.ptr_frame);
        ptrFrame.setPtrHandler(new PtrHandler() {
            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                return !dashboardScrollView.canScrollVertically(-1) && dashboardTop.getState() == DashboardTop.State.Expand;
            }

            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                ptrToast.show();
                dashboardScrollView.postDelayed(() -> {
                    if (ptrFrame.isRefreshing()) {
                        ptrFrame.refreshComplete();
                    }
                }, 8000);
            }
        });
        TestPtrHeader header = new TestPtrHeader(this);
        ptrFrame.setHeaderView(header);
        ptrFrame.addPtrUIHandler(header);
        ptrFrame.setResistance(1.7f);
        ptrFrame.setRatioOfHeaderHeightToRefresh(1.5f);
        ptrFrame.setPullToRefresh(true);
        ptrFrame.setKeepHeaderWhenRefresh(true);
    }
}
