package com.mmga.cloudcover;

import android.app.Application;
import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.request.target.ViewTarget;

public class MyApplication extends Application {

    private RequestQueue mRequestQueue;
    private static MyApplication mInstance;
    private static Context context;

    public static final String TAG = MainActivity.class.getName();

    @Override
    public void onCreate() {
        super.onCreate();
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

    public <T> void add(Request<T> req) {
        req.setTag(TAG);
        mRequestQueue.add(req);
    }

    public void cancle() {
        mRequestQueue.cancelAll(TAG);
    }

    public static Context getContext() {
        return context;
    }
}
