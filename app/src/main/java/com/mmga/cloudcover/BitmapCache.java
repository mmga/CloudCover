package com.mmga.cloudcover;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.LruCache;

import com.android.volley.toolbox.ImageLoader;
import com.jakewharton.disklrucache.DiskLruCache;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class BitmapCache implements ImageLoader.ImageCache{

    private LruCache<String, Bitmap> lruCache;
    private DiskLruCache diskLruCache;
    private Context context = MyApplication.getInstance().getApplicationContext();


    int MAX_MEMORY = (int) (Runtime.getRuntime().maxMemory() / 1024);
    final long DISK_MAX_SIZE = 10 * 1024 * 1024;

    public BitmapCache() {
        lruCache = new LruCache<String, Bitmap>(MAX_MEMORY / 8) {

            @Override
            protected int sizeOf(String key, Bitmap bitmap) {
                return bitmap.getByteCount();
            }
        };

        File cacheDir = getDiskCacheDir(context, "bitmap");
        if (!cacheDir.exists()) {
            cacheDir.mkdir();
        }
        try {
            diskLruCache = DiskLruCache.open(cacheDir, getAppVersion(), 1, DISK_MAX_SIZE);
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    //获取应用程序版本号
    private int getAppVersion() {
        try {
            PackageInfo info = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            return info.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return 1;
    }


    @Override
    public Bitmap getBitmap(String url) {
        String key = generateKey(url);
        Bitmap bmp = lruCache.get(key);
        if (bmp == null) {
            bmp = getBitmapFromDiskLruCache(key);
            if (bmp != null) {
                lruCache.put(key, bmp);
            }
        }
        return bmp;
    }

    private Bitmap getBitmapFromDiskLruCache(String key) {
        try {
            DiskLruCache.Snapshot snapshot = diskLruCache.get(key);
            if (snapshot != null) {
                InputStream inputStream = snapshot.getInputStream(0);
                if (inputStream != null) {
                    Bitmap bmp = BitmapFactory.decodeStream(inputStream);
                    inputStream.close();
                    return bmp;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void putBitmap(String url, Bitmap bitmap) {
        String key = generateKey(url);
        lruCache.put(url, bitmap);
        putBitmapToDiskLruCache(key, bitmap);
    }

    private void putBitmapToDiskLruCache(String key, Bitmap bitmap) {
        try {
            DiskLruCache.Editor editor = diskLruCache.edit(key);
            if (editor != null) {
                OutputStream outputStream = editor.newOutputStream(0);
                bitmap.compress(Bitmap.CompressFormat.PNG, 0, outputStream);
                editor.commit();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private File getDiskCacheDir(Context context, String uniqueName) {
        String cachePath = null;
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())
                || !Environment.isExternalStorageRemovable()) {
            cachePath = context.getExternalCacheDir().getPath();
        } else {
            cachePath = context.getCacheDir().getPath();
        }
        return new File(cachePath + File.separator + uniqueName);
    }

    private String generateKey(String url) {
        return MD5Utils.hashKeyForDisk(url);
    }
}

