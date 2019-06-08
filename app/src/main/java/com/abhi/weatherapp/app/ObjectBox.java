package com.abhi.weatherapp.app;


import android.content.Context;
import android.util.Log;

import com.abhi.weatherapp.BuildConfig;
import com.abhi.weatherapp.model.db.MyObjectBox;
import io.objectbox.BoxStore;
import io.objectbox.android.AndroidObjectBrowser;

public class ObjectBox {
    private static BoxStore boxStore;

    public static void init(Context context) {
        boxStore = MyObjectBox.builder()
                .androidContext(context.getApplicationContext())
                .build();

        if (BuildConfig.DEBUG) {
            new AndroidObjectBrowser(boxStore).start(context.getApplicationContext());
            Log.d("testxy", String.format("Using ObjectBox %s (%s)",
                    BoxStore.getVersion(), BoxStore.getVersionNative()));
        }
    }

    public static BoxStore get() { return boxStore; }
}