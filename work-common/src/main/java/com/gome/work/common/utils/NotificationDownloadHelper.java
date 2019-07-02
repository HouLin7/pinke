
package com.gome.work.common.utils;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.support.v4.app.NotificationCompat;

import com.gome.work.common.R;
import com.gome.work.core.SystemFramework;

/**
 * Created by songzhiyang on 2016/6/14.
 */
public class NotificationDownloadHelper {
    private static final int NOTIFICATION_PROGRESS_ID = 8888;

    private static NotificationCompat.Builder sBuilder;
    private static NotificationManager notificationManager;

    public static void showProgressNotification() {
        notificationManager = (NotificationManager) SystemFramework
                .getInstance().getGlobalContext().getSystemService(Context.NOTIFICATION_SERVICE);
        sBuilder = new NotificationCompat.Builder(SystemFramework
                .getInstance().getGlobalContext()).setSmallIcon(R.drawable.ic_notification)
                        .setColor(SystemFramework.getInstance().getGlobalContext().getResources()
                                .getColor(R.color.blue));
        sBuilder.setContentTitle(SystemFramework.getInstance().getGlobalContext().getResources()
                .getString(R.string.app_name))
                .setContentText(SystemFramework.getInstance().getGlobalContext().getResources()
                        .getString(R.string.downloading))
                .setTicker(SystemFramework.getInstance().getGlobalContext().getResources()
                        .getString(R.string.downloading))
                .setOngoing(true);

        sBuilder.setProgress(100, 0, false);

        Notification notify = sBuilder.build();
        notificationManager.notify(NOTIFICATION_PROGRESS_ID, notify);
    }


}
