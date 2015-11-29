package com.hunk.nobank.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.hunk.nobank.NoBankApplication;
import com.hunk.nobank.R;
import com.hunk.nobank.util.HijackingNotification;
import com.hunk.nobank.util.Logging;
import com.hunk.nobank.util.ViewHelper;
import com.hunk.nobank.views.LoadingDialogFragment;
import com.hunk.nobank.views.MenuProxy;
import com.hunk.nobank.views.TitleBarPoxy;
import com.hunk.whitelabel.Feature;

/**
 * Base Activity to provide base function. Each Activity should extends it
 * such as custom title bar, left slide menu, unrollActivity
 */
public class BaseActivity extends FragmentActivity {

    public final static String ACTION_GOTO_ROOT = "action.root.open_main";
    private static final String DIALOG_LOADING_TAG = "DIALOG_LOADING_TAG";

    protected NoBankApplication application;
    private DrawerLayout mDrawLayout;

    private TitleBarPoxy mTitleBarPoxy;
    private MenuProxy mMenuProxy;

    private HijackingNotification mHijackingNotification;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        application = (NoBankApplication) getApplication();

        super.setContentView(R.layout.activity_base_with_titlebar);
        mHijackingNotification = new HijackingNotification(this);
        setupUI();
    }

    private void setupUI() {
        mDrawLayout = (DrawerLayout) findViewById(R.id.base_drawer_layout);

        mTitleBarPoxy = new TitleBarPoxy(findViewById(R.id.activity_base_title_bar));
        mMenuProxy = new MenuProxy(findViewById(R.id.activity_base_menu), mDrawLayout);
    }

    public static void unrollActivity(Context ctx) {
        String packageName = ctx.getPackageName();

        Intent unroll = new Intent();
        unroll.setPackage(packageName);
        unroll.setAction(ACTION_GOTO_ROOT);
        unroll.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        ctx.startActivity(unroll);
    }

    public static String generateAction(Feature feature, String realAction) {
        return String.format("action.%s.%s", feature.toString(), realAction);
    }

    @Override
    public void startActivity(Intent intent) {
        Logging.d("go to screen : " + intent.getAction());
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        super.startActivity(intent);
    }

    public void showLoading() {
        LoadingDialogFragment frag = LoadingDialogFragment.newInstance();
        FragmentManager fragMgr = getSupportFragmentManager();
        frag.show(fragMgr.beginTransaction(), DIALOG_LOADING_TAG);
    }

    public void dismissLoading() {
        FragmentManager fragMgr = getSupportFragmentManager();
        LoadingDialogFragment frag = (LoadingDialogFragment) fragMgr.findFragmentByTag(DIALOG_LOADING_TAG);
        if (frag != null && frag.isVisible()) {
            frag.dismiss();
        }
    }

    @Override
    public void setContentView(int layoutResID) {
        // get root container
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        inflater.inflate(layoutResID, (FrameLayout) findViewById(R.id.activity_base_main_content), true);

        setFontsStyle();
    }

    private void setFontsStyle() {
        // tree walk root view, change every text component to using custom font style.
        ViewGroup vg = (ViewGroup) getWindow().getDecorView().findViewById(android.R.id.content);
        ViewHelper.updateFontsStyle(vg);
    }

    public void setContentView(int layoutResID, Base style) {
        this.setContentView(layoutResID);
        setBaseStyle(style);
    }

    public void setBaseStyle(Base style) {
        switch (style) {
            case NO_DRAW_LAYOUT:
                mDrawLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
                break;
            case NO_TITLE_BAR:
                findViewById(R.id.activity_base_title_bar).setVisibility(View.GONE);
                findViewById(R.id.activity_base_shadow_under_title_bar).setVisibility(View.GONE);
                mMenuProxy.prepareMenuButtons();
                break;
            default:
                // set Default Left button onclick listener
                mTitleBarPoxy.getLeftIcon().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mDrawLayout.isDrawerOpen(Gravity.LEFT)) {
                            mDrawLayout.closeDrawer(Gravity.LEFT);
                        } else {
                            mDrawLayout.openDrawer(Gravity.LEFT);
                        }
                    }
                });
                mMenuProxy.prepareMenuButtons();
                break;
        }
    }

    public enum Base {
        NO_DRAW_LAYOUT,
        NO_TITLE_BAR,
        NORMAL;
    }

    public TitleBarPoxy getTitleBarPoxy() {
        return mTitleBarPoxy;
    }

    @Override
    protected void onResume() {
        super.onResume();

        mHijackingNotification.dismiss();
    }

    @Override
    protected void onPause() {
        super.onPause();

        mHijackingNotification.show();
    }

    public HijackingNotification getHijackingNotification() {
        return mHijackingNotification;
    }
}
