package com.app.ciza.lighter.infrastructure;


import android.content.ContentResolver;
import android.content.Context;
import android.content.res.Resources;
import android.net.Uri;

public class CommonHelpers {

    public static Uri getResourceUri(Context context, int resourceId){
        Resources resources = context.getResources();

        Uri uri = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" +
        resources.getResourcePackageName(resourceId) + "/" +
                resources.getResourceTypeName(resourceId) + "/" +
        resources.getResourceEntryName(resourceId));

        return uri;
    }
}
