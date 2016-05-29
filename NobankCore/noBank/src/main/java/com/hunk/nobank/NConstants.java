package com.hunk.nobank;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;

import com.hunk.nobank.activity.dashboard.DashboardViewImplActivity;

public class NConstants {
    public final static int ANIMATION_DURATION_MEDIUM = 2000;

    public final static String OPEN_MAIN = "open_main";

    /**
     * Parameter intent key:
     *  Pass menu start action into it, so RootActivity will start it.
     *  @see
     *  com.hunk.nobank.views.MenuProxy.MenuBaseAdapter#onItemClick(AdapterView, View, int, long)
     *  @see
     *  com.hunk.nobank.activity.RootActivity#onNewIntent(Intent)
     */
    public final static String INTENT_EXTRA_START_MENU = "INTENT_EXTRA_START_MENU";
    /**
     * Flag intent key:
     *  Pass true, so RootActivity will handle it as start menu action, vice versa
     *  @see
     *  com.hunk.nobank.views.MenuProxy.MenuBaseAdapter#onItemClick(AdapterView, View, int, long)
     *  @see
     *  com.hunk.nobank.activity.RootActivity#onNewIntent(Intent)
     */
    public final static String INTENT_EXTRA_IS_START_MENU = "INTENT_EXTRA_IS_START_MENU";
    /**
     * Flag intent key:
     *  Pass true, so RootActivity will handle it as exit action, vice versa
     *  @see
     *  com.hunk.nobank.activity.RootActivity#onNewIntent(Intent)
     *  @see
     *  DashboardViewImplActivity#onBackPressed()
     */
    public static final String INTENT_EXTRA_IS_EXIT = "INTENT_EXTRA_IS_EXIT";
}
