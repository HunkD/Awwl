package com.hunk.abcd.views;

import android.app.NotificationManager;
import android.content.Context;

import com.hunk.abcd.Testable;

import junit.framework.Assert;

import org.junit.Test;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.shadows.ShadowNotificationManager;

import static org.robolectric.Shadows.shadowOf;

/**
 * test {@link HijackingNotification}
 */
public class HijackingNotificationTest extends Testable {
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
