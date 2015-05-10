package com.hunk.test.nobank;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.ResolveInfo;
import android.os.Build;

import com.hunk.nobank.BaseActivity;
import com.hunk.nobank.BuildConfig;
import com.hunk.nobank.NConstants;
import com.hunk.nobank.R;
import com.hunk.nobank.feature.Feature;
import com.hunk.nobank.feature.welcome.activity.WelcomePageActivity;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.Shadows;
import org.robolectric.annotation.Config;
import org.robolectric.manifest.ActivityData;
import org.robolectric.manifest.AndroidManifest;
import org.robolectric.manifest.IntentFilterData;
import org.robolectric.res.builder.RobolectricPackageManager;
import org.robolectric.shadows.ShadowActivity;
import org.robolectric.shadows.ShadowApplication;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Please set the working directory for this unit test configuration is 'NobankCore/nobank'
 */
@RunWith(RobolectricGradleTestRunner.class)
/**Only support JELLY_BEAN isn't good :( **/
@Config(constants = BuildConfig.class, emulateSdk = Build.VERSION_CODES.JELLY_BEAN)
public class WelcomePageActivityTest {

    /**
     * This is for add all implicit intent to Package Manager,
     * so Intent.resolveActivity() can run normally in our code.
     */
    @Before
    public void setup() {
        RobolectricPackageManager rpm =
                (RobolectricPackageManager) RuntimeEnvironment.application.getPackageManager();

        rpm.addManifest(ShadowApplication.getInstance().getAppManifest(), ShadowApplication.getInstance().getResourceLoader());

        AndroidManifest appManifest = ShadowApplication.getInstance().getAppManifest();
        for (Map.Entry<String, ActivityData> activity : appManifest.getActivityDatas().entrySet()) {
            String activityName = activity.getKey();
            ActivityData activityData = activity.getValue();
            if (activityData.getTargetActivity() != null) {
                activityName = activityData.getTargetActivityName();
            }

            ApplicationInfo applicationInfo = new ApplicationInfo();
            applicationInfo.packageName = RuntimeEnvironment.application.getPackageName();
            ResolveInfo resolveInfo = new ResolveInfo();
            resolveInfo.resolvePackageName = RuntimeEnvironment.application.getPackageName();
            resolveInfo.activityInfo = new ActivityInfo();
            resolveInfo.activityInfo.applicationInfo = applicationInfo;
            resolveInfo.activityInfo.name = activityName;

            for (IntentFilterData intentFilterData : activityData.getIntentFilters()) {
                for (String intentFilterAction : intentFilterData.getActions()) {
                    Intent i = new Intent(intentFilterAction);
                    i.setPackage(RuntimeEnvironment.application.getPackageName());
                    rpm.addResolveInfoForIntent(i, resolveInfo);
                }
            }
        }
    }

    @Test
    public void testClickOnSignInButton() {
        Intent gotoLogin = new Intent();
        gotoLogin.setPackage(RuntimeEnvironment.application.getPackageName());
        gotoLogin.setAction(BaseActivity.generateAction(Feature.login, NConstants.OPEN_MAIN));

        WelcomePageActivity activity = Robolectric.setupActivity(WelcomePageActivity.class);
        activity.findViewById(R.id.welcome_btn_sign_in).performClick();

        ShadowActivity si = Shadows.shadowOf(activity);
        assertThat(si.getNextStartedActivity()).isEqualTo(gotoLogin);
    }
}
