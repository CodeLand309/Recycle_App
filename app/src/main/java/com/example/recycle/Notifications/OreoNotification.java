package com.example.recycle.Notifications;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;

import androidx.core.app.NotificationCompat;

import com.example.recycle.R;

public class OreoNotification extends ContextWrapper {

    public static final String CHANNEL_ID = "com.example.recycle";
    public static final String CHANNEL_NAME = "Chat";

    private NotificationManager notificationManager;

    public OreoNotification(Context base) {
        super(base);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            createChannel();
        }
    }

    @TargetApi(Build.VERSION_CODES.O)
    private void createChannel() {
        NotificationChannel channel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH);
        channel.enableLights(false);
        channel.enableVibration(true);
        channel.setDescription("Notifications of Recent Messages send by other users");
        channel.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);

        getManager().createNotificationChannel(channel);
    }

    public NotificationManager getManager(){
        if(notificationManager == null){
            notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        }

        return notificationManager;
    }

    @TargetApi(Build.VERSION_CODES.O)
    public Notification.Builder getOreoNotification(String title, String body, PendingIntent pendingIntent, Uri soundUri, String icon){
        return new Notification.Builder(getApplicationContext(), CHANNEL_ID)
                .setContentIntent(pendingIntent)
                .setContentTitle(title)
                .setContentText(body)
                .setPriority(Notification.PRIORITY_HIGH)
                .setShowWhen(true)
                .setSmallIcon(R.mipmap.ic_launcher_round)
                .setStyle(new Notification.BigTextStyle().bigText(body).setBigContentTitle(title))
                .setSound(soundUri)
                .setAutoCancel(true);
    }
}
