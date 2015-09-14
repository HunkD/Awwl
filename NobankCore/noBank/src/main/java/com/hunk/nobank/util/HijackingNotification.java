package com.hunk.nobank.util;

import android.app.NotificationManager;
import android.content.Context;
import android.support.v4.app.NotificationCompat;

import com.hunk.nobank.R;

/**
 *
 */
public class HijackingNotification {
    private final Context context;
    public static final int NOTIFICATION_ID = 10;

    public HijackingNotification(Context context) {
        this.context = context;
    }

    public void dismiss() {
        // Gets an instance of the NotificationManager service
        NotificationManager mNotifyMgr =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        mNotifyMgr.cancel(NOTIFICATION_ID);
    }

    public void show() {
        // Gets an instance of the NotificationManager service
        NotificationManager mNotifyMgr =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(context)
                        .setSmallIcon(R.drawable.ic_notification)
                        .setContentTitle(context.getString(R.string.notify_background))
                        .setContentText(context.getString(R.string.notify_background_now))
                        .setStyle(new NotificationCompat.BigTextStyle()
                                .bigText(context.getString(R.string.notify_background_now)));

        // Builds the notification and issues it.
//        mNotifyMgr.notify(NOTIFICATION_ID, mBuilder.build());
    }
}
