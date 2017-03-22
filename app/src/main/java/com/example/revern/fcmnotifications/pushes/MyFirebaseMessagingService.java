package com.example.revern.fcmnotifications.pushes;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;

import com.example.revern.fcmnotifications.MainActivity;
import com.example.revern.fcmnotifications.R;
import com.example.revern.fcmnotifications.pushes.models.NotificationModel;
import com.example.revern.fcmnotifications.utils.StringUntils;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.google.gson.Gson;
import com.google.gson.JsonElement;

import java.util.Map;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final Uri RINGTONE_URI =
            RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

    private Gson gson = new Gson();

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // TODO(developer): Handle FCM messages here.
        // If the application is in the foreground handle both data and notification messages here.
        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated. See sendNotification method below.

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ic_launcher)
                .setContentTitle(getTitle(remoteMessage))
                .setContentText(getMessage(remoteMessage))
                .setAutoCancel(true)
                .setSound(RINGTONE_URI)
                .setVibrate(new long[]{0, 200, 200, 200})
                .setContentIntent(getContentIntent());

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
    }

    @NonNull
    private NotificationModel getNotificationModel(@Nullable Map<String, String> map) {
        JsonElement json = gson.toJsonTree(map);
        return gson.fromJson(json, NotificationModel.class);
    }

    @NonNull
    private String getMessage(@NonNull RemoteMessage remoteMessage) {
        NotificationModel model = getNotificationModel(remoteMessage.getData());
        return model.isEmpty() ? remoteMessage.getNotification().getBody() :
                model.getName() + " - " + model.getAge() + "age: " + model.getText();
    }

    @NonNull
    private String getTitle(@NonNull RemoteMessage remoteMessage) {
        return StringUntils.isEmpty(remoteMessage.getNotification().getTitle()) ?
                getString(R.string.app_name) :
                remoteMessage.getNotification().getTitle();
    }

    @NonNull
    private PendingIntent getContentIntent() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        return PendingIntent.getActivity(this, 0 /* Request code */, intent, PendingIntent.FLAG_ONE_SHOT);
    }


}
