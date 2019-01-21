package com.app.ciza.lighter.infrastructure;


import android.content.Context;
import android.hardware.Camera;
import android.util.Log;

import com.app.ciza.lighter.R;

public class CameraController {
    private static Camera camera = null;
    private static Camera.Parameters params = null;


    private static void getCamera(Context context){
        if(camera == null){
            try{
                camera = Camera.open();
                params = camera.getParameters();
            }catch (RuntimeException ex){
                Log.e(context.getString(R.string.app_name), ex.getMessage());
                ex.printStackTrace();
            }
        }
    }

    public static void turnOnTouch(Context context){
        if(camera == null || params ==  null){
            getCamera(context);
        }

        params = camera.getParameters();
        params.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
        camera.setParameters(params);
        camera.startPreview();
        AppSettings.setTouchOn(true);
    }

    public static void turnOffTouch(Context context){
        if(AppSettings.isTouchOn()){
            if(camera == null || params ==  null){
                getCamera(context);
            }

            params = camera.getParameters();
            params.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
            camera.setParameters(params);
            camera.startPreview();
            camera.release();
            camera = null;
            params = null;
            AppSettings.setTouchOn(false);
        }
    }



}
