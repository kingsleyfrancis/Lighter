package com.app.ciza.lighter.infrastructure;


import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

import com.app.ciza.lighter.R;
import com.app.ciza.lighter.activities.MainActivity;

public class Notifier {

    public static void createNotification(String title, String message, Context context){

        String channelId = KeyRing.NOTIFICATION_MANAGER_ID;

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context, channelId)
                .setSmallIcon(R.drawable.notifier)
                .setContentTitle(title)
                .setContentText(message);

        //creates explicit intent for activity in the app
        Intent resultIntent = new Intent(context, MainActivity.class);

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addParentStack(MainActivity.class);
        stackBuilder.addNextIntent(resultIntent);

        PendingIntent resultPendingIntent = stackBuilder
                .getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

        mBuilder.setContentIntent(resultPendingIntent);
        NotificationManager mNotificationManager = (NotificationManager)
                context.getSystemService(Context.NOTIFICATION_SERVICE);

        if(mNotificationManager != null){
            mNotificationManager.notify(KeyRing.NOTIFICATION_ID, mBuilder.build());
        }

    }

}
