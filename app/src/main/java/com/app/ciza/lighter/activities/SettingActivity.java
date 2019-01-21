package com.app.ciza.lighter.activities;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Switch;

import com.app.ciza.lighter.R;
import com.app.ciza.lighter.infrastructure.AppSettings;
import com.app.ciza.lighter.infrastructure.KeyRing;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.util.ArrayList;

public class SettingActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener, View.OnClickListener {

    private Context _mContext = this;
    private AdView mAdView;
    private Spinner spinner = null;

    private Switch runInBackgroundSwitch = null;
    private Switch alertOnLowBatterySwitch = null;
    private Switch turnOffSoundSwitch = null;
    private Button doneButton = null;

    private String timePeriod = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        runInBackgroundSwitch = (Switch) findViewById(R.id.activity_setting_runInBackground);
        alertOnLowBatterySwitch = (Switch) findViewById(R.id.activity_setting_offOnLowBattery);
        turnOffSoundSwitch = (Switch) findViewById(R.id.activity_setting_turnOffSound);
        doneButton = (Button) findViewById(R.id.activity_setting_doneButton);
        spinner = (Spinner) findViewById(R.id.activity_setting_stayOnTimePeriod);

        refill();

        doneButton.setOnClickListener(this);
        spinner.setOnItemSelectedListener(this);

        mAdView = (AdView) findViewById(R.id.activity_main_adView);
        AdRequest adRequest = new AdRequest.Builder()
                //.addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .build();
        mAdView.loadAd(adRequest);

    }

    private void refill() {
        runInBackgroundSwitch.setChecked(AppSettings.runAppInBackground());
        alertOnLowBatterySwitch.setChecked(AppSettings.promptWhenLowBattery());
        turnOffSoundSwitch.setChecked(AppSettings.shouldTurnOffSound());
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String item = (String)parent.getItemAtPosition(position);
        timePeriod = item;
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onClick(View view) {
        int itemId = view.getId();

        switch (itemId){
            case R.id.activity_setting_doneButton:{
                doneButtonClicked();
                break;
            }
        }
    }

    private void doneButtonClicked() {
        boolean alertOnBatteryLow = alertOnLowBatterySwitch.isChecked();
        boolean runInBackground = runInBackgroundSwitch.isChecked();
        boolean turnOffSound = turnOffSoundSwitch.isChecked();

        AppSettings.storeSettings(this, runInBackground, alertOnBatteryLow, turnOffSound, timePeriod);
        setResult(RESULT_OK);
        finish();
    }
}
