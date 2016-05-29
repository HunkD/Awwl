package com.hunk.test.utils;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.ResolveInfo;
import android.support.annotation.StringRes;

import com.google.gson.Gson;
import com.hunk.nobank.BuildConfig;
import com.hunk.nobank.Core;
import com.hunk.nobank.contract.ContractGson;

import org.junit.Before;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;
import org.robolectric.manifest.ActivityData;
import org.robolectric.manifest.AndroidManifest;
import org.robolectric.manifest.IntentFilterData;
import org.robolectric.res.builder.RobolectricPackageManager;
import org.robolectric.shadows.ShadowApplication;
import org.robolectric.shadows.ShadowResourceManager;
import org.robolectric.shadows.ShadowResources;

import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.robolectric.Shadows.shadowOf;

@RunWith(RobolectricGradleTestRunner.class)
/**Only support JELLY_BEAN and above isn't good :( **/
@Config(constants = BuildConfig.class,
        application = TestNoBankApplication.class,
        sdk = 21)
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

    public static <T> void compareObj(T obj1, T obj2) {
        Gson contractGson = ContractGson.getInstance();
        String str1 = contractGson.toJson(obj1);
        String str2 = contractGson.toJson(obj2);
        assertEquals(str1, str2);
    }

    public static CharSequence getString(@StringRes int resId) {
        return RuntimeEnvironment.application.getString(resId);
    }
}
