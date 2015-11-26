package com.mmga.cloudcover.util;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;


public class ShareUtils {

    public static void shareImage(Context context, Uri uri) {
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
        shareIntent.setType("image/jpg");
        Log.d("mmga", "shareUri = " + uri);
        context.startActivity(Intent.createChooser(shareIntent, "图片分享到.."));

    }


}
