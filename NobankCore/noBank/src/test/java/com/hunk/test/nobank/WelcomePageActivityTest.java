package com.hunk.test.nobank;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.ResolveInfo;
import android.os.Build;
import android.provider.Settings;

import com.hunk.nobank.BaseActivity;
import com.hunk.nobank.NConstants;
import com.hunk.nobank.R;
import com.hunk.nobank.feature.Feature;
import com.hunk.nobank.feature.login.activity.LoginPageActivity;
import com.hunk.nobank.feature.welcome.activity.WelcomePageActivity;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;
import org.robolectric.res.builder.RobolectricPackageManager;
import org.robolectric.shadows.ShadowActivity;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(RobolectricTestRunner.class)
@Config(manifest = "noBank/src/main/AndroidManifest.xml", emulateSdk = Build.VERSION_CODES.JELLY_BEAN)
public class WelcomePageActivityTest {
    @Test
    public void testClickOnSignInButton() {
        RobolectricPackageManager rpm =
                (RobolectricPackageManager) Robolectric.application.getPackageManager();
        Intent gotoLogin = new Intent();
        gotoLogin.setPackage(Robolectric.getShadowApplication().getPackageName());
        gotoLogin.setAction(BaseActivity.generateAction(Feature.login, NConstants.OPEN_MAIN));

        ResolveInfo info = new ResolveInfo();
        info.isDefault = true;

        ApplicationInfo applicationInfo = new ApplicationInfo();
        applicationInfo.packageName = Robolectric.getShadowApplication().getPackageName();
        info.activityInfo = new ActivityInfo();
        info.activityInfo.applicationInfo = applicationInfo;
        info.activityInfo.name = LoginPageActivity.class.getName();

        rpm.addResolveInfoForIntent(gotoLogin, info);

        WelcomePageActivity activity = Robolectric.setupActivity(WelcomePageActivity.class);
        activity.findViewById(R.id.welcome_btn_sign_in).performClick();

        Robolectric.runUiThreadTasksIncludingDelayedTasks();

        ShadowActivity si = Robolectric.shadowOf(activity);
        assertThat(si.getNextStartedActivity()).isEqualTo(gotoLogin);
    }
}
