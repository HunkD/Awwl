package com.hunk.test.utils;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.ResolveInfo;

import com.hunk.nobank.Core;

import org.junit.Before;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.manifest.ActivityData;
import org.robolectric.manifest.AndroidManifest;
import org.robolectric.manifest.IntentFilterData;
import org.robolectric.res.builder.RobolectricPackageManager;
import org.robolectric.shadows.ShadowApplication;
import org.robolectric.shadows.ShadowResourceManager;
import org.robolectric.shadows.ShadowResources;

import java.util.Map;

import static org.robolectric.Shadows.shadowOf;

public abstract class NBAbstractTest {
    /**
     * This is for add all implicit intent to Package Manager,
     * so Intent.resolveActivity() can run normally in our code.
     */
    @Before
    public void setup() {
        addImplicitIntentToPM();

        Core.clearCache();
    }

    private void addImplicitIntentToPM() {
        RobolectricPackageManager rpm =
                (RobolectricPackageManager) RuntimeEnvironment.application.getPackageManager();

        rpm.addManifest(ShadowApplication.getInstance().getAppManifest(),
                shadowOf(RuntimeEnvironment.application.getResources().getAssets()).getResourceLoader());

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
}
