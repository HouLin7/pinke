package com.gome.update;

import android.app.Notification;
import android.app.Notification.Builder;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.gome.applibrary.R;
import com.gome.utils.CommonUtils;
import com.gome.utils.ContentUriUtils;
import com.liulishuo.okdownload.DownloadTask;
import com.liulishuo.okdownload.core.cause.EndCause;
import com.liulishuo.okdownload.core.cause.ResumeFailedCause;
import com.liulishuo.okdownload.core.listener.DownloadListener1;
import com.liulishuo.okdownload.core.listener.assist.Listener1Assist;

import java.io.File;

public class UpdateService extends Service {

    public static final String EXTRA_DOWNLOAD_URL = "download.url";
    public static final String EXTRA_LAUNCHER_CLASS = "launcher.class";

    public static final String EXTRA_DOWNLOAD_FILE_NAME = "file.name";

    private NotificationManager notificationManager;

    private RemoteViews contentView;

    private Notification notification;

    private int notification_id = 0;

    private Class<?> mLauncherclass;

    @Override
    public void onCreate() {
        super.onCreate();
        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
    }

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent == null || !intent.hasExtra(EXTRA_LAUNCHER_CLASS)) {
            return super.onStartCommand(intent, flags, startId);
        }
        mLauncherclass = (Class<?>) intent.getSerializableExtra(EXTRA_LAUNCHER_CLASS);
        String downloadUrl = intent.getStringExtra(EXTRA_DOWNLOAD_URL);
        String fileName = intent.getStringExtra(EXTRA_DOWNLOAD_FILE_NAME);
        if (TextUtils.isEmpty(fileName)) {
            fileName = Uri.parse(downloadUrl).getLastPathSegment();
        }
        File file = new File(getExternalFilesDir("apk").getAbsolutePath(), fileName);
        if (file.exists() && file.isFile()) {
            file.delete();
        }
        downloadFile(downloadUrl, file.getAbsolutePath());
        return super.onStartCommand(intent, flags, startId);
    }

    private void downloadFile(String url, final String filePath) {

        DownloadTask task = new DownloadTask.Builder(url, new File(filePath))
                .setMinIntervalMillisCallbackProcess(200)
                .setPassIfAlreadyCompleted(false)
                .setAutoCallbackToUIThread(true)
                .setConnectionCount(1)
                .build();

        task.enqueue(new DownloadListener1() {
            @Override
            public void taskStart(@NonNull DownloadTask task, @NonNull Listener1Assist.Listener1Model model) {
                createNotification();
                Toast.makeText(getBaseContext(), R.string.down_ing, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void retry(@NonNull DownloadTask task, @NonNull ResumeFailedCause cause) {

            }

            @Override
            public void connected(@NonNull DownloadTask task, int blockCount, long currentOffset, long totalLength) {

            }

            @Override
            public void progress(@NonNull DownloadTask task, long currentOffset, long totalLength) {
                int updateCount = (int) (currentOffset * 100 / totalLength);
                contentView.setTextViewText(R.id.notificationPercent, updateCount + "%");
                contentView.setProgressBar(R.id.notificationProgress, 100, updateCount, false);
                notificationManager.notify(notification_id, notification);
            }

            @Override
            public void taskEnd(@NonNull DownloadTask task, @NonNull EndCause cause, @Nullable Exception realCause, @NonNull Listener1Assist.Listener1Model model) {
                if (cause == EndCause.COMPLETED) {
                    File targetFile = new File(filePath);
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    Uri uri = ContentUriUtils.getOutputUriForFile(getBaseContext(), targetFile);
                    intent.setDataAndType(uri, "application/vnd.android.package-archive");
                    PendingIntent pendingIntent = PendingIntent.getActivity(UpdateService.this, 0, intent, 0);

                    Builder builder = new Notification.Builder(getBaseContext()).setSmallIcon(R.drawable.ic_launcher).setAutoCancel(true)
                            .setContentIntent(pendingIntent).setContentText(getResources().getString(R.string.down_ok));
                    notification = builder.build();

                    notificationManager.cancel(notification_id);
                    Toast.makeText(UpdateService.this, R.string.down_ok, Toast.LENGTH_SHORT).show();
                    CommonUtils.installApk(getBaseContext(), uri);
                    stopSelf();
                } else if (cause == EndCause.CANCELED) {
                    notificationManager.cancel(notification_id);
                } else {
                    Intent updateIntent = new Intent(UpdateService.this, mLauncherclass);
                    PendingIntent pendingIntent = PendingIntent.getActivity(UpdateService.this, 0, updateIntent, 0);
                    Builder builder = new Notification.Builder(getBaseContext()).setSmallIcon(R.drawable.ic_launcher).setAutoCancel(true)
                            .setContentIntent(pendingIntent).setContentText(getResources().getString(R.string.down_error));
                    notification = builder.build();
                    notificationManager.notify(notification_id, notification);
                    stopSelf();
                }
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        notificationManager.cancel(notification_id);
    }

    public void createNotification() {
        notification = new Notification();
        contentView = new RemoteViews(getPackageName(), R.layout.notification_item);
        contentView.setTextViewText(R.id.notificationTitle, getResources().getString(R.string.down_ing));
        contentView.setTextViewText(R.id.notificationPercent, "0%");
        contentView.setProgressBar(R.id.notificationProgress, 100, 0, false);
        notification.contentView = contentView;

        Intent updateIntent = new Intent(this, mLauncherclass);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, updateIntent, 0);

//		notification.contentIntent = pendingIntent;
//		notification.icon = R.drawable.ic_launcher;
//		notification.flags |= Notification.FLAG_AUTO_CANCEL;


        Builder builder = new Notification.Builder(this).setSmallIcon(R.drawable.ic_launcher).setContent(contentView).setAutoCancel(true)
                .setContentIntent(pendingIntent);

        notification = builder.build();
        notificationManager.notify(notification_id, notification);

    }

}
