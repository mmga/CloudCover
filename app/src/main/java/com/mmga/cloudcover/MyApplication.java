package com.mmga.cloudcover;

import android.app.Application;
import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.request.target.ViewTarget;
import com.mmga.cloudcover.ui.MainActivity;

public class MyApplication extends Application {

    private RequestQueue mRequestQueue;
    private static MyApplication mInstance;
    private static Context context;

    private static final String TAG = MainActivity.class.getName();

    @Override
    public void onCreate() {
        super.onCreate();
//        LeakCanary.install(this);
        mInstance = this;
        context = getApplicationContext();
        mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        ViewTarget.setTagId(R.id.glide_request);

//        Realm.setDefaultConfiguration(new RealmConfiguration.Builder(this)
//                .schemaVersion(1)
//                .deleteRealmIfMigrationNeeded()
//                .build());
    }

    public static synchronized MyApplication getInstance() {
        return mInstance;
    }

    public RequestQueue getRequestQueue() {
        return mRequestQueue;
    }

// --Commented out by Inspection START (2015/11/28 13:35):
//    public <T> void add(Request<T> req) {
//        req.setTag(TAG);
//        mRequestQueue.add(req);
//    }
// --Commented out by Inspection STOP (2015/11/28 13:35)

    public static Context getContext() {
        return context;
    }
}
