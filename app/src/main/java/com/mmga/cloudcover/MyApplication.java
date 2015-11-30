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
    }

    public static synchronized MyApplication getInstance() {
        return mInstance;
    }

    public RequestQueue getRequestQueue() {
        return mRequestQueue;
    }

    public static Context getContext() {
        return context;
    }
}
