package com.app.ciza.lighter.infrastructure;

import android.content.Context;
import android.content.SharedPreferences;

import static android.content.Context.MODE_PRIVATE;

public class AppSettings {
    private static String turnOffAfter = "2 Minutes";

    private static boolean _isTestMode = false;

    private static boolean _promptWhenLowBattery = true;
    private static boolean _runAppInBackground = false;
    private static boolean _turnOffSound = false;
    private static boolean _isTouchOn = false;
    private static String _turnOffAfter = turnOffAfter;


    public static void updateSettings(boolean runInBackground, boolean turnOffWhenLowBattery, boolean turnOffSound, String turnOffAfter){
        _runAppInBackground = runInBackground;
        _turnOffAfter = turnOffAfter;
        _promptWhenLowBattery = turnOffWhenLowBattery;
        _turnOffSound = turnOffSound;
    }

    public static void loadSettings(Context context){
        SharedPreferences preferences = context.getSharedPreferences(KeyRing.PREFERENCE_NAME, MODE_PRIVATE);

        _promptWhenLowBattery = preferences.getBoolean(KeyRing.ALERT_ON_LOW_BATTERY, true);
        _runAppInBackground = preferences.getBoolean(KeyRing.RUN_IN_BACKGROUND, false);
        _turnOffAfter = preferences.getString(KeyRing.TURN_OFF_AFTER, turnOffAfter);

    }

    public static void storeSettings(Context context, boolean runInBackground, boolean turnOffOnLowBattery, boolean turnOffSound, String turnOffAfter){
        updateSettings(runInBackground, turnOffOnLowBattery, turnOffSound, turnOffAfter);

        SharedPreferences preferences = context.getSharedPreferences(KeyRing.PREFERENCE_NAME, MODE_PRIVATE);

        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(KeyRing.RUN_IN_BACKGROUND, runInBackground);
        editor.putBoolean(KeyRing.ALERT_ON_LOW_BATTERY, turnOffOnLowBattery);
        editor.putBoolean(KeyRing.TURN_OFF_SOUND, turnOffSound);
        editor.putString(KeyRing.TURN_OFF_AFTER, turnOffAfter);
        editor.apply();
    }

    public static boolean shouldTurnOffSound(){
        return _turnOffSound;
    }

    public static boolean promptWhenLowBattery() {
        return _promptWhenLowBattery;
    }

    public static boolean runAppInBackground() {
        return _runAppInBackground;
    }

    public static String turnOffAfter() {
        return _turnOffAfter;
    }

    public static boolean isTouchOn() {
        return _isTouchOn;
    }

    public static void setTouchOn(boolean isTouchOn) {
        _isTouchOn = isTouchOn;
    }

    public static boolean isTestMode(){
        return _isTestMode;
    }
}
