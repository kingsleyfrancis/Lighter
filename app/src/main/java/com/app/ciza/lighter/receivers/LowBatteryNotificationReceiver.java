package com.app.ciza.lighter.receivers;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.app.ciza.lighter.R;
import com.app.ciza.lighter.infrastructure.AppSettings;
import com.app.ciza.lighter.infrastructure.KeyRing;
import com.app.ciza.lighter.infrastructure.LighterService;
import com.app.ciza.lighter.infrastructure.Notifier;

public class LowBatteryNotificationReceiver extends BroadcastReceiver{
    @Override
    public void onReceive(Context context, Intent intent) {

        //check if prompt is set and if the touch is on, then notify and off the touch.
        if(AppSettings.promptWhenLowBattery() && AppSettings.isTouchOn()){

            String lowBatteryNotificationMessage = context.getString(R.string.low_battery_notification_message);
            Notifier.createNotification(context.getString(R.string.low_battery), lowBatteryNotificationMessage, context);

            Intent serviceIntent = new Intent(context, LighterService.class);
            serviceIntent.putExtra(KeyRing.TOGGLE_TOUCH, KeyRing.TOGGLE_TOURCH_OFF);

            //start service.
            context.startService(intent);
        }
    }
}
