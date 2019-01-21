package com.app.ciza.lighter.infrastructure;


import android.app.Application;

import com.app.ciza.lighter.R;
import com.google.android.gms.ads.MobileAds;

public class App extends Application{

    @Override
    public void onCreate() {
        super.onCreate();

        boolean isTest = AppSettings.isTestMode();
        String resid = isTest ? getString(R.string.admob_id_test) : getString(R.string.admob_id);

        //initialize mobile ads
        MobileAds.initialize(this, resid);
    }
}
