package com.app.ciza.lighter.receivers;


import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.app.ciza.lighter.R;
import com.app.ciza.lighter.infrastructure.AppSettings;
import com.app.ciza.lighter.infrastructure.KeyRing;
import com.app.ciza.lighter.infrastructure.Notifier;

public class ServiceNotificationReceiver extends BroadcastReceiver
{

    @Override
    public void onReceive(Context context, Intent intent) {
        if(intent == null)
            return;

        String message = intent.getExtras().getString(KeyRing.NOTIFICATION_MESSAGE);
        if(message.isEmpty())
            return;

        String title = context.getString(R.string.current_status);
        Notifier.createNotification(title, message, context);
    }

}
