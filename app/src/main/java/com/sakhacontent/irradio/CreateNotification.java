package com.sakhacontent.irradio;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.support.v4.media.session.MediaSessionCompat;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;


import com.sakhacontent.irradio.Services.NotificationActionService;

import java.io.Console;

public class CreateNotification {

    public static final String CHANNEL_ID = "channel1";

    public static final String ACTION_PREVIUOS = "actionprevious";
    public static final String ACTION_PLAY = "actionplay";
    public static final String ACTION_NEXT = "actionnext";

    public static Notification notification;

    public static void createNotification(Context context, int playbutton) {

        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

                NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(context);
                MediaSessionCompat mediaSessionCompat = new MediaSessionCompat(context, "tag");

                Bitmap icon = BitmapFactory.decodeResource(context.getResources(), R.drawable.t1);

                PendingIntent pendingIntentPrevious;
                int drw_previous;
                /*
                if (pos == 0) {
                    pendingIntentPrevious = null;
                    drw_previous = 0;
                } else {
                    Intent intentPrevious = new Intent(context, NotificationActionService.class)
                            .setAction(ACTION_PREVIUOS);
                    pendingIntentPrevious = PendingIntent.getBroadcast(context, 0,
                            intentPrevious, PendingIntent.FLAG_UPDATE_CURRENT);
                    drw_previous = R.drawable.ic_skip_previous;
                }
*/
                Intent intentPlay = new Intent(context, NotificationActionService.class)
                        .setAction(ACTION_PLAY);
                PendingIntent pendingIntentPlay;
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S) {
                    pendingIntentPlay = PendingIntent.getBroadcast(context, 0,
                            intentPlay, PendingIntent.FLAG_IMMUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);
                } else {
                    pendingIntentPlay = PendingIntent.getBroadcast(context, 0,
                            intentPlay, PendingIntent.FLAG_UPDATE_CURRENT);
                }
                PendingIntent pendingIntentNext;
                int drw_next;
/*                if (pos == 12) {
                    pendingIntentNext = null;
                    drw_next = 0;
                } else {
                    Intent intentNext = new Intent(context, NotificationActionService.class)
                            .setAction(ACTION_NEXT);
                    pendingIntentNext = PendingIntent.getBroadcast(context, 0,
                            intentNext, PendingIntent.FLAG_UPDATE_CURRENT);
                    drw_next = R.drawable.ic_skip_next;
                }*/

                //create notification
                notification = new NotificationCompat.Builder(context, CHANNEL_ID)
                        .setSmallIcon(R.drawable.ic_music_note)
                        .setContentTitle("Ыччат радиота")
                        .setContentText(" ")
                        .setLargeIcon(icon)
                        .setOnlyAlertOnce(true)//show notification for only first time
                        .setShowWhen(false)
//                        .addAction(drw_previous, "Previous", pendingIntentPrevious)
                        .addAction(playbutton, "Play", pendingIntentPlay)
//                        .addAction(drw_next, "Next", pendingIntentNext)
                        .setStyle(new androidx.media.app.NotificationCompat.MediaStyle().setShowActionsInCompactView(0).setMediaSession(mediaSessionCompat.getSessionToken()))
//                        .setStyle(new androidx.media.app.NotificationCompat.MediaStyle().setMediaSession(mediaSessionCompat.getSessionToken()))
                        .setPriority(NotificationCompat.PRIORITY_LOW)
                        .build();
                notificationManagerCompat.notify(1, notification);

            }

        } catch (Exception e){
//            System.out.println(" exception " + e.getMessage());
        }
    }
}
/*

public class CreateNotification {
    public static final String CHANNEL_ID = "channel1";

    public static final String ACTION_PREVIUOS = "actionprevious";
    public static final String ACTION_PLAY = "actionplay";
    public static final String ACTION_NEXT = "actionnext";

    public static Notification notification;

    public static void createNotification(Context context, Track track, int playbutton, int pos, int size){
        System.out.println("createNotification in\n");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){

            NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(context);
            MediaSessionCompat mediaSessionCompat = new MediaSessionCompat(context, "tag");
            Bitmap icon = BitmapFactory.decodeResource(context.getResources(), track.getImage());
            Intent intentPlay = new Intent(context, NotificationActionService.class)
                    .setAction(ACTION_PLAY);
            PendingIntent pendingIntentPlay = PendingIntent.getBroadcast(context, 0,
                    intentPlay, PendingIntent.FLAG_UPDATE_CURRENT);
            notification = new NotificationCompat.Builder(context, CHANNEL_ID)
                    .setSmallIcon(R.drawable.ic_music_note)
                    .setContentTitle(track.getTitle())
                    .setContentText(track.getArtist())
                    .setLargeIcon(icon)
                    .setOnlyAlertOnce(true)//show notification for only first time
                    .setShowWhen(false)
                    .setOngoing(true)
                    .addAction(playbutton, "Play", pendingIntentPlay)
                    .setStyle(new androidx.media.app.NotificationCompat.MediaStyle()
                            .setShowActionsInCompactView(0, 1, 2)
                            .setMediaSession(mediaSessionCompat.getSessionToken()))
                    .setPriority(NotificationCompat.PRIORITY_MAX)
                    .build();

            notificationManagerCompat.notify(1, notification);
        }
        System.out.println("createNotification out\n");
    }



}
*/
