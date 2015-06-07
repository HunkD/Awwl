package com.hunk.test.nobank.utilTest;

import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;

import com.hunk.nobank.BuildConfig;
import com.hunk.nobank.util.HijackingNotification;
import com.hunk.test.utils.NBAbstractTest;
import com.hunk.test.utils.TestNoBankApplication;

import junit.framework.Assert;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowNotificationManager;

import static org.robolectric.Shadows.shadowOf;

/**
 *
 */
@RunWith(RobolectricGradleTestRunner.class)
/**Only support JELLY_BEAN and above isn't good :( **/
@Config(constants = BuildConfig.class, emulateSdk = Build.VERSION_CODES.JELLY_BEAN,
        application = TestNoBankApplication.class)
public class HijackingNotificationTest extends NBAbstractTest {
    @Test
    public void testShowBackgroundRunningNotification() {
        HijackingNotification hijackingNotification = new HijackingNotification(RuntimeEnvironment.application);
        hijackingNotification.show();

        ShadowNotificationManager nm = shadowOf((NotificationManager) RuntimeEnvironment.application
                .getSystemService(Context.NOTIFICATION_SERVICE));
        Assert.assertNotNull(nm.getNotification(HijackingNotification.NOTIFICATION_ID));
    }

    @Test
    public void testDismissBackgroundRunningNotification() {
        HijackingNotification hijackingNotification = new HijackingNotification(RuntimeEnvironment.application);
        hijackingNotification.show();
        hijackingNotification.dismiss();

        ShadowNotificationManager nm = shadowOf((NotificationManager) RuntimeEnvironment.application
                .getSystemService(Context.NOTIFICATION_SERVICE));
        Assert.assertNull(nm.getNotification(HijackingNotification.NOTIFICATION_ID));
    }
}
