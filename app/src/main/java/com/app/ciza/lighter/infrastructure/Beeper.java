package com.app.ciza.lighter.infrastructure;


import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;

import com.app.ciza.lighter.R;

import java.util.HashMap;

public class Beeper {
    public static final int beepSource = R.raw.beep;
    private static boolean _isLoaded = false;
    private static boolean _isPoolLoaded = false;


    private static SoundPool _soundPool;
    private static HashMap<Integer, Integer> _soundList;

    private static void load(){
        _soundPool = new SoundPool(10, AudioManager.STREAM_MUSIC, 100);
        _soundPool.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
            @Override
            public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
                _isPoolLoaded = true;
            }
        });
        _soundList = new HashMap<>(2);
        _isLoaded = true;
    }

    public static void init(Context context){
        if(!_isLoaded){
            load();
            _isLoaded = true;
        }

        _soundList.put(beepSource, _soundPool.load(context, R.raw.beep, 1));
    }

    public static void play(Context context, int soundId){
        float volume = 0.6f;

        if(AppSettings.shouldTurnOffSound())
        {
            return;
        }

        if(!_isLoaded){
            init(context);
        }


        //play sound
        _soundPool.play(_soundList.get(soundId), volume, volume, 1, 0, 1f);
    }
}
