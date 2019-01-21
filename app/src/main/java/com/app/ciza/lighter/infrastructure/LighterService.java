package com.app.ciza.lighter.infrastructure;


import android.app.IntentService;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;

import com.app.ciza.lighter.R;


public class LighterService extends IntentService {

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */

    public LighterService(String name) {
        super(name);
    }

    public LighterService(){
        super("LighterService");
    }


    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if(intent == null){
            return;
        }

        String instruction = intent.getExtras().getString(KeyRing.TOGGLE_TOUCH);
        int soundId = intent.getExtras().getInt(KeyRing.SOUND_ID);
        if(instruction.isEmpty()){
            return;
        }

        Context context = getApplicationContext();
        Intent notifierIntent = new Intent(KeyRing.BROADCAST_ACTION);


        //turn on touch if on-instruction.
        if(instruction.equals(KeyRing.TOGGLE_TOURCH_ON)){
           onTouch(context, notifierIntent);

           //turn off touch if off-instruction.
        }else if(instruction.equals(KeyRing.TOGGLE_TOURCH_OFF)){
           offTouch(context, notifierIntent);
        }
        Beeper.play(context, soundId);
        LocalBroadcastManager.getInstance(this).sendBroadcast(notifierIntent);
    }

    private void offTouch(Context context, Intent notifierIntent) {
        if(AppSettings.isTouchOn()){
            CameraController.turnOffTouch(context);

            String message = getString(R.string.service_stopped_message);
            notifierIntent.putExtra(KeyRing.NOTIFICATION_MESSAGE, message);
        }
    }

    private void onTouch(Context context, Intent notifierIntent) {
        if(!AppSettings.isTouchOn()){
            CameraController.turnOnTouch(context);
            AppSettings.setTouchOn(true);

            CharSequence message = getString(R.string.service_started_message);
            notifierIntent.putExtra(KeyRing.NOTIFICATION_MESSAGE, message);
        }

    }
}
