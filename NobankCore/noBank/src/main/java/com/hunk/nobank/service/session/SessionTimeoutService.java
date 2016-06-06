package com.hunk.nobank.service.session;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.hunk.nobank.Core;
import com.hunk.nobank.activity.BaseActivity;
import com.hunk.nobank.manager.UserManager;
import com.hunk.nobank.util.Logging;
import com.hunk.nobank.util.ViewHelper;

/**
 * Session Timeout service <br>
 * After user login, we will clean user session when user idle more than 5 mins. <br>
 * Every user action will reset the timeout counter clock. <br>
 *
 * @author HunkDeng
 * @since 2016/5/7
 */
public class SessionTimeoutService extends Service {

    /**
     * Default timeout time period
     */
    public static final long DEFAULT_TIMEOUT_STAMP = 5*60*1000;

    /**
     * User alarm manager to set timeout alarm
     *
     * @param context
     *      Context to get alarm manager.
     * @param timeoutTime
     *      could be {@link #DEFAULT_TIMEOUT_STAMP}, or other period you like.
     */
    public static void setAlarm(Context context, long timeoutTime) {
        AlarmManager am =
                (AlarmManager) context.getApplicationContext().getSystemService(Context.ALARM_SERVICE);
        PendingIntent pendingIntent =
                PendingIntent.getService(
                        context,
                        0,
                        new Intent(context, SessionTimeoutService.class),
                        PendingIntent.FLAG_UPDATE_CURRENT);
        am.set(AlarmManager.RTC_WAKEUP,
                System.currentTimeMillis() + timeoutTime,
                pendingIntent);
    }

    /**
     * when this service has been start, that means we should clean the user session
     */
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Logging.d("SessionTimeoutService has been triggered. Execute timeout operation!");
        UserManager userManager = Core.getInstance().getUserManager();
        userManager.logout(getApplicationContext());
        return super.onStartCommand(intent, flags, startId);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
