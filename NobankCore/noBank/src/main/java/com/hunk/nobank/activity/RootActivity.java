package com.hunk.nobank.activity;

import android.content.Intent;
import android.os.Bundle;

import com.hunk.nobank.Core;
import com.hunk.nobank.NConstants;
import com.hunk.nobank.manager.UserManager;
import com.hunk.nobank.util.Logging;
import com.hunk.whitelabel.retailer.RetailerFeatureList;

/**
 * The Root for Activity Stack of this application
 */
public class RootActivity extends BaseActivity {
    public static final String ACTION = "action.root.open_main";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        onNewIntent(getIntent());
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (isExit(intent)) {
            finish();
            return;
        }

        // ask Login feature if the user already login.
        UserManager userManager = Core.getInstance().getLoginManager();

        if (userManager.isLogInSuccessfully()) {
            if (isStartMenu(intent)) {
                String action = intent.getStringExtra(NConstants.INTENT_EXTRA_START_MENU);
                Intent gotoMenuItem = new Intent();
                gotoMenuItem.setPackage(getApplicationContext().getPackageName());
                gotoMenuItem.setAction(action);
                if (gotoMenuItem.resolveActivity(getPackageManager()) != null) {
                    startActivity(gotoMenuItem);
                } else {
                    Logging.w("No menu feature when calling in RootActivity : " + action);
                }
            } else if (RetailerFeatureList.Dashboard.ENABLE) {
                // go to Welcome Screen
                Intent gotoDashboard = new Intent();
                gotoDashboard.setPackage(getApplicationContext().getPackageName());
                gotoDashboard.setAction(RetailerFeatureList.Dashboard.ACTION);
                if (gotoDashboard.resolveActivity(getPackageManager()) != null) {
                    startActivity(gotoDashboard);
                } else {
                    Logging.w("No welcome feature when calling in RootActivity.");
                }
            }
        } else if (RetailerFeatureList.Welcome.ENABLE) {
            // go to Welcome Screen
            Intent gotoWelcome = new Intent();
            gotoWelcome.setPackage(getApplicationContext().getPackageName());
            gotoWelcome.setAction(RetailerFeatureList.Welcome.ACTION);
            if (gotoWelcome.resolveActivity(getPackageManager()) != null) {
                startActivity(gotoWelcome);
            } else {
                Logging.w("No welcome feature when calling in RootActivity.");
            }
        }
    }

    private boolean isStartMenu(Intent intent) {
        return intent.getBooleanExtra(NConstants.INTENT_EXTRA_IS_START_MENU, false);
    }

    public boolean isExit(Intent newIntent) {
        return newIntent.getBooleanExtra(NConstants.INTENT_EXTRA_IS_EXIT, false);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        getHijackingNotification().dismiss();
    }
}
