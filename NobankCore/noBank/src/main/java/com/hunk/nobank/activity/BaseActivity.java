package com.hunk.nobank.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.hunk.abcd.activity.mvp.AbstractViewActivity;
import com.hunk.abcd.activity.mvp.BasePresenter;
import com.hunk.abcd.extension.font.UpdateFont;
import com.hunk.abcd.extension.log.Logging;
import com.hunk.abcd.views.HijackingNotification;
import com.hunk.nobank.Core;
import com.hunk.nobank.NConstants;
import com.hunk.nobank.NoBankApplication;
import com.hunk.nobank.R;
import com.hunk.nobank.activity.root.RootActivity;
import com.hunk.nobank.manager.UserManager;
import com.hunk.nobank.service.session.SessionTimeoutService;
import com.hunk.nobank.views.LoadingDialogFragment;
import com.hunk.nobank.views.MenuProxy;
import com.hunk.nobank.views.TitleBarPoxy;

/**
 * Base Activity to provide base function. Each Activity should extends it
 * such as custom title bar, left slide menu, unrollActivity
 */
public abstract class BaseActivity<P extends BasePresenter> extends AbstractViewActivity<P> {

    /**
     * Flag to record whether app is running in the foreground
     */
    public static volatile boolean IS_APP_FOREGROUND = false;

    private static final String DIALOG_LOADING_TAG = "DIALOG_LOADING_TAG";

    protected NoBankApplication application;
    private DrawerLayout mDrawLayout;

    private TitleBarPoxy mTitleBarPoxy;
    private MenuProxy mMenuProxy;
    protected RelativeLayout baseRootContainer;

    private HijackingNotification mHijackingNotification;

    /**
     * Is this subclass activity need a logined user session
     * @return
     *      need : true, vise versa
     */
    protected boolean isRequiredLoginedUserSession() {
        return false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        application = (NoBankApplication) getApplication();

        super.setContentView(R.layout.activity_base_with_titlebar);
        mHijackingNotification = new HijackingNotification(this);
        setupUI();

        unrollActivityIfSessionTimeout();
    }

    private void setupUI() {
        mDrawLayout = (DrawerLayout) findViewById(R.id.base_drawer_layout);
        mTitleBarPoxy = new TitleBarPoxy(findViewById(R.id.activity_base_title_bar));
        mMenuProxy = new MenuProxy(findViewById(R.id.activity_base_menu), mDrawLayout);
        baseRootContainer = (RelativeLayout) findViewById(R.id.activity_base_root_container);
    }

    public static void unrollActivity(Context ctx) {
        String packageName = ctx.getPackageName();

        Intent unrollIntent = getUnrollIntent(packageName);
        ctx.startActivity(unrollIntent);
    }

    @NonNull
    public static Intent getUnrollIntent(String packageName) {
        Intent unrollIntent = new Intent();
        unrollIntent.setPackage(packageName);
        unrollIntent.setAction(RootActivity.ACTION);
        unrollIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        unrollIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        return unrollIntent;
    }

    @Override
    public void startActivity(@NonNull Intent intent) {
        Logging.d("go to screen : " + intent.getAction());
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        if (intent.resolveActivity(getPackageManager()) != null) {
            super.startActivity(intent);
        }
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
        UpdateFont.updateFontsStyle(vg);
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
            case NO_TITLE_BAR_NO_DRAW_LAYOUT:
                mDrawLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
                findViewById(R.id.activity_base_title_bar).setVisibility(View.GONE);
                findViewById(R.id.activity_base_shadow_under_title_bar).setVisibility(View.GONE);
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

    public static void exitApplication(Context ctx) {
        String packageName = ctx.getPackageName();

        Intent exitIntent = getExitApplicationIntent(packageName);
        ctx.startActivity(exitIntent);
    }

    @NonNull
    private static Intent getExitApplicationIntent(String packageName) {
        Intent exitIntent = new Intent();
        exitIntent.setPackage(packageName);
        exitIntent.setAction(RootActivity.ACTION);
        exitIntent.putExtra(NConstants.INTENT_EXTRA_IS_EXIT, true);
        exitIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        return exitIntent;
    }

    public enum Base {
        NO_DRAW_LAYOUT,
        NO_TITLE_BAR,
        NORMAL,
        NO_TITLE_BAR_NO_DRAW_LAYOUT
    }

    public TitleBarPoxy getTitleBarPoxy() {
        return mTitleBarPoxy;
    }

    @Override
    protected void onResume() {
        super.onResume();
        IS_APP_FOREGROUND = true;
        mHijackingNotification.dismiss();

        unrollActivityIfSessionTimeout();
    }

    @Override
    protected void onPause() {
        super.onPause();
        IS_APP_FOREGROUND = false;
        mHijackingNotification.show();
    }

    public HijackingNotification getHijackingNotification() {
        return mHijackingNotification;
    }

    @Override
    public void onUserInteraction() {
        super.onUserInteraction();
        setupSessionTimeoutServiceIfNeed();
    }

    /**
     * setup Session Timeout Service
     * when user session is login state.
     */
    private void setupSessionTimeoutServiceIfNeed() {
        UserManager userManager = Core.getInstance().getUserManager();
        if (UserManager.isPostLogin(userManager)) {
            SessionTimeoutService.setAlarm(this, SessionTimeoutService.DEFAULT_TIMEOUT_STAMP);
        }
    }

    /**
     * Unroll activity if current user session has timeout
     */
    private void unrollActivityIfSessionTimeout() {
        UserManager userManager = Core.getInstance().getUserManager();
        if (isRequiredLoginedUserSession()
                && !UserManager.isPostLogin(userManager)) {
            unrollActivity(this);
        }
    }

    public void showErrorMessage(String msg) {
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
    }

    @Override
    public void showError(Throwable e) {

    }
}
