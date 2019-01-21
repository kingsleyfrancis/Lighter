package com.app.ciza.lighter.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.app.ciza.lighter.R;
import com.app.ciza.lighter.infrastructure.KeyRing;
import com.app.ciza.lighter.infrastructure.LighterService;

public class TurnOffTouchReceiver extends BroadcastReceiver{

    @Override
    public void onReceive(Context context, Intent intent) {

        Intent offTouchIntent = new Intent(context, LighterService.class);
        offTouchIntent.putExtra(KeyRing.TOGGLE_TOUCH, KeyRing.TOGGLE_TOURCH_OFF);
        offTouchIntent.putExtra(KeyRing.SOUND_ID, R.raw.beep);

        //start service.
        context.startService(offTouchIntent);
    }
}
