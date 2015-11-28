package com.hunk.nobank.service.registration;

import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;

import com.hunk.nobank.R;

/**
 *
 */
public class RegistrationForegroundService extends Service {
    private static final int NOTIFICATION_ID = 11;
    public static final String ACTION_START = "ACTION_START";
    public static final String ACTION_STOP = "ACTION_STOP";

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        if (ACTION_START.equals(intent.getAction())) {
            Context context = getBaseContext();

            PendingIntent pi =
                    PendingIntent.getBroadcast(getApplicationContext(), 0, new Intent(), PendingIntent.FLAG_UPDATE_CURRENT);
            NotificationCompat.Builder mBuilder =
                    new NotificationCompat.Builder(context)
                            .setContentIntent(pi)
                            .setSmallIcon(R.drawable.ic_notification)
                            .setContentTitle(context.getString(R.string.notify_registration_now_title))
                            .setContentText(context.getString(R.string.notify_registration_now_text))
                            .setStyle(new NotificationCompat.BigTextStyle()
                                    .bigText(context.getString(R.string.notify_registration_now_explain)));

            // Builds the notification and issues it.
            startForeground(NOTIFICATION_ID, mBuilder.build());
        } else if (ACTION_STOP.equals(intent.getAction())) {
            stopForeground(true);
            stopSelf();
        }
        return START_NOT_STICKY;
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        super.onTaskRemoved(rootIntent);

        stopForeground(true);
        stopSelf();
    }
}
