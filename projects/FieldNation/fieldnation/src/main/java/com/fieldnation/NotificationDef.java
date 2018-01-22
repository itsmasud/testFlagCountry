package com.fieldnation;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;

/**
 * Created by mc on 8/2/17.
 */

public class NotificationDef {
    private static final long[] default_ringtone = new long[]{0, 500};
    public static String FILE_UPLOAD_CHANNEL = "fn_channel_file_upload";
    public static String PHOTO_UPLOAD_CHANNEL = "fn_channel_photo_upload";
    public static String PUSH_NOTIFICATION_CHANNEL = "fn_channel_push_notification";
    public static String OTHER_CHANNEL = "fn_channel_other";

    public static void configureNotifications(Context context) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O)
            return;

        NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        buildChannelFileUpload(mNotificationManager, context);
        buildChannelPhotoUpload(mNotificationManager, context);
        buildChannelPushNotification(mNotificationManager, context);
        buildChannelOther(mNotificationManager, context);
    }

    private static void buildChannelFileUpload(NotificationManager mNotificationManager, Context context) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O)
            return;

        String name = "File Upload Notifications";
        String description = "Notifications around uploading files";
        int importance = NotificationManager.IMPORTANCE_MIN;

        NotificationChannel mChannel = new NotificationChannel(FILE_UPLOAD_CHANNEL, name, importance);
        mChannel.setDescription(description);
        mNotificationManager.createNotificationChannel(mChannel);
    }

    private static void buildChannelPhotoUpload(NotificationManager mNotificationManager, Context context) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O)
            return;

        String name = "Photo Upload Notifications";
        String description = "Notifications around uploading profile photos";
        int importance = NotificationManager.IMPORTANCE_MIN;

        NotificationChannel mChannel = new NotificationChannel(PHOTO_UPLOAD_CHANNEL, name, importance);
        mChannel.setDescription(description);
        mChannel.setImportance(NotificationManager.IMPORTANCE_HIGH);
        mNotificationManager.createNotificationChannel(mChannel);
    }

    private static void buildChannelPushNotification(NotificationManager mNotificationManager, Context context) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O)
            return;

        String name = "Push Notifications";
        String description = "Notifications sent to you from Field Nation";
        int importance = NotificationManager.IMPORTANCE_MAX;

        NotificationChannel mChannel = new NotificationChannel(PUSH_NOTIFICATION_CHANNEL, name, importance);
        mChannel.setDescription(description);
        mChannel.setVibrationPattern(default_ringtone);
        mNotificationManager.createNotificationChannel(mChannel);
    }

    private static void buildChannelOther(NotificationManager mNotificationManager, Context context) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O)
            return;

        String name = "General Notifications";
        String description = "Generic notifications";
        int importance = NotificationManager.IMPORTANCE_DEFAULT;

        NotificationChannel mChannel = new NotificationChannel(OTHER_CHANNEL, name, importance);
        mChannel.setDescription(description);
        mNotificationManager.createNotificationChannel(mChannel);
    }
}
