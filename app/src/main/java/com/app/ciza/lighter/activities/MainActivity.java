package com.app.ciza.lighter.activities;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.SystemClock;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.app.ciza.lighter.R;
import com.app.ciza.lighter.infrastructure.AppSettings;
import com.app.ciza.lighter.infrastructure.Beeper;
import com.app.ciza.lighter.infrastructure.CameraController;
import com.app.ciza.lighter.infrastructure.CommonHelpers;
import com.app.ciza.lighter.infrastructure.KeyRing;
import com.app.ciza.lighter.infrastructure.LighterService;
import com.app.ciza.lighter.receivers.ServiceNotificationReceiver;
import com.google.android.gms.ads.*;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private ImageButton switchButton;
    private AdView mAdView;

    public Context mContext = this;
    private AlarmManager _alarmManager = null;
    private PendingIntent _turnOffAfterIntent = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //check if flash light is supported
        checkIfFlashLightIsSupported();

        //load app settings.
        AppSettings.loadSettings(mContext);

        switchButton = (ImageButton) findViewById(R.id.activity_main_switchButton);
        ImageButton settingButton = (ImageButton) findViewById(R.id.activity_main_settingsButton);

        switchButton.setOnClickListener(this);
        settingButton.setOnClickListener(this);

        //create notification channel.
        createNotificationChannel();

        //register notification channel receiver
        registerNotificationReceiver();

        mAdView = (AdView) findViewById(R.id.activity_main_adView);
        AdRequest adRequest = new AdRequest.Builder()
                                //.addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                                .build();
        mAdView.loadAd(adRequest);

        //load beeper
        Beeper.init(mContext);
    }

    private void turnOffTouchEvent() {
        //set up turn touch off intent
        Intent turnOffIntent = new Intent("TurnOffTouchReceiver");
        _turnOffAfterIntent = PendingIntent.getBroadcast(mContext, 0, turnOffIntent, 0);
    }

    private void createNotificationChannel() {
        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        String id = KeyRing.NOTIFICATION_MANAGER_ID;

        //notification manager name
        String notificationName = getString(R.string.app_name);
        notificationName += " Notifications";

        String description = getString(R.string.notification_description);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel mChannel = null;

            mChannel = new NotificationChannel(id, notificationName, importance);
            mChannel.setDescription(description);
            mChannel.enableLights(true);
            mChannel.setLightColor(Color.GREEN);
            mChannel.enableVibration(true);
            mChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
            mNotificationManager.createNotificationChannel(mChannel);
        }
    }

    @Override
    public void onClick(View view) {
        int typeId = view.getId();

        switch (typeId){
            case R.id.activity_main_switchButton:{
                switchTouch();
                break;
            }
            case R.id.activity_main_settingsButton:{
                openSetting();
                break;
            }
        }
    }


    private void checkIfFlashLightIsSupported() {

        //check if device supports flash light
        boolean hasFlash = getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);

        if(!hasFlash){
            AlertDialog alertDialog = new AlertDialog.Builder(mContext).create();
            alertDialog.setTitle("Error");
            alertDialog.setMessage("Sorry, your device doesn't support flash light!");

            alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "OK",
                    new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finish();
                }
            });

            //show dialog
            alertDialog.show();
        }
    }

    private void openSetting() {
        Intent intent = new Intent(this, SettingActivity.class);
        startActivityForResult(intent, KeyRing.OPEN_SETTING_RESULT_CODE);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == RESULT_OK && requestCode == KeyRing.OPEN_SETTING_RESULT_CODE){
            Toast.makeText(this, "Settings updated", Toast.LENGTH_SHORT).show();

            if(AppSettings.isTouchOn()){
                //set up turn off touch event.
                setupTurnOffTouchEvent();
            }

        }
    }

    private void setupTurnOffTouchEvent() {
        String turnOffAfter = AppSettings.turnOffAfter();
        int turnOffTimeInMinutes = 0;

        switch(turnOffAfter){
            case "2 Minutes":{
                turnOffTimeInMinutes = 2;
                break;
            }
            case "5 Minutes":{
                turnOffTimeInMinutes = 5;
                break;
            }
            case "15 Minutes":{
                turnOffTimeInMinutes = 15;
                break;
            }
            case "1 Hour":{
                turnOffTimeInMinutes = 60;
                break;
            }
            default:{
                turnOffTimeInMinutes = (4 * 60);
                break;
            }
        }
        //setup turn off touch event
        turnOffTouchEvent();


        _alarmManager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);

        long executeTime = turnOffTimeInMinutes * 60 * 1000;
        executeTime = SystemClock.elapsedRealtime() + executeTime;
        _alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, executeTime, _turnOffAfterIntent);
    }

    private void turnOnTouch(){

        if(AppSettings.runAppInBackground()){

            //set up turn off torch event.
            setupTurnOffTouchEvent();

            Intent intent = new Intent(this, LighterService.class);
            intent.putExtra(KeyRing.TOGGLE_TOUCH, KeyRing.TOGGLE_TOURCH_ON);
            intent.putExtra(KeyRing.SOUND_ID, R.raw.beep);

            startService(intent);
        }else{
            //On touch using specified settings
            configureSettings(R.raw.beep);
        }
    }

    private void configureSettings(int soundId) {
        Intent notifierIntent = new Intent(KeyRing.BROADCAST_ACTION);
        CharSequence message = getString(R.string.service_started_message);
        notifierIntent.putExtra(KeyRing.NOTIFICATION_MESSAGE, message);

        Beeper.play(mContext, soundId);
        LocalBroadcastManager.getInstance(this).sendBroadcast(notifierIntent);

        CameraController.turnOnTouch(mContext);
    }

    private void turnOffTouch(){
        if(AppSettings.runAppInBackground()){
            Intent intent = new Intent(this, LighterService.class);
            intent.putExtra(KeyRing.TOGGLE_TOUCH, KeyRing.TOGGLE_TOURCH_OFF);
            intent.putExtra(KeyRing.SOUND_ID, R.raw.beep);

            //start service.
            startService(intent);
        }else {
            Intent notifierIntent = new Intent(KeyRing.BROADCAST_ACTION);
            String message = getString(R.string.service_stopped_message);
            notifierIntent.putExtra(KeyRing.NOTIFICATION_MESSAGE, message);

            Beeper.play(mContext, R.raw.beep);
            LocalBroadcastManager.getInstance(this).sendBroadcast(notifierIntent);
            CameraController.turnOffTouch(mContext);
        }
    }

    private void switchTouch() {
        boolean toggle = false;
        if(AppSettings.isTouchOn()){
            turnOffTouch();
            toggle = false;
        }else{
            turnOnTouch();
            toggle = true;
        }
        //toggle image button
        toggleButtonImage(toggle);
    }

    private void toggleButtonImage(boolean toggleOn) {
        Uri resourceUri = null;
        if(toggleOn){
            resourceUri = CommonHelpers.getResourceUri(mContext, R.drawable.offer);
        }else{
            resourceUri = CommonHelpers.getResourceUri(mContext, R.drawable.oner);
        }
        switchButton.setImageURI(resourceUri);
    }

    private void playSound() {
    }


    private void registerNotificationReceiver() {
        IntentFilter statusIntentFilter = new IntentFilter(KeyRing.BROADCAST_ACTION);

        ServiceNotificationReceiver receiver = new ServiceNotificationReceiver();
        LocalBroadcastManager.getInstance(this).registerReceiver(receiver, statusIntentFilter);
    }

}
