package com.android.androidosample.notification_channel;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationChannelGroup;
import android.app.NotificationManager;
import android.content.Context;
import android.graphics.Color;
import android.support.v4.app.NotificationCompat;

import com.android.androidosample.R;

import java.util.List;

class NotificationChannelHelper {

    private static NotificationChannelHelper mChannelHandler;
    private NotificationManager mNotificationManager;
    // The id of the channel.
    private static final String CHANNEL_ID = "com.android.androidosample.channel_1";
    // The id of the group.
    private static final String GROUP_ID = "com.android.androidosample.group_1";

    private NotificationChannelHelper(Context context) {
        mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
    }

    static NotificationChannelHelper getInstance(Context context) {
        if (mChannelHandler == null) {
            mChannelHandler = new NotificationChannelHelper(context);
        }
        return mChannelHandler;
    }

    void generateNotificationThroughChannel(Context context) {
        createChannelGroup(context);
        createChannel(context);
        createNotification(context);
    }

    private void createNotification(Context context) {
        // Sets an ID for the notification, so it can be updated.
        int notifyID = 1;
        // Create a notification and set the notification channel.
        Notification notification = new Notification.Builder(context)
                .setContentTitle("New Message")
                .setContentText("You've received new messages.")
                .setSmallIcon(R.drawable.ic_notify_status)
                .setChannel(CHANNEL_ID)
//                .setNumber(1)    //Set numbers to be displayed in badge.
//                .setBadgeIconType(Notification.BADGE_ICON_SMALL)
                .build();
        // Issue the notification.
        mNotificationManager.notify(notifyID, notification);


    }

    private void createChannel(Context context) {
        // The user-visible name of the channel.
        CharSequence name = context.getString(R.string.channel_name);
        // The user-visible description of the channel.
        String description = context.getString(R.string.channel_description);
        int importance = NotificationManager.IMPORTANCE_DEFAULT;
        //Create Notification Channel object
        NotificationChannel mChannel = new NotificationChannel(CHANNEL_ID, name, importance);
        // Configure the notification channel.
        mChannel.setDescription(description);
        mChannel.enableLights(true);
        // Sets the notification light color for notifications posted to this
        // channel, if the device supports this feature.
        mChannel.setLightColor(Color.RED);
        mChannel.enableVibration(true);
//        mChannel.setShowBadge(true);
        mChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});

        //Set the group id for Notification channel
        mChannel.setGroup(GROUP_ID);

        mNotificationManager.createNotificationChannel(mChannel);
    }

    private void createChannelGroup(Context context) {
        // The user-visible name of the group.
        CharSequence name = context.getString(R.string.group_name);
        mNotificationManager.createNotificationChannelGroup(new NotificationChannelGroup(GROUP_ID, name));
    }

    List<NotificationChannel> getNotificationChannels() {
        return mNotificationManager.getNotificationChannels();
    }

    void deleteChannel(NotificationChannel channel) {
        mNotificationManager.deleteNotificationChannel(channel.getId());
    }
}
