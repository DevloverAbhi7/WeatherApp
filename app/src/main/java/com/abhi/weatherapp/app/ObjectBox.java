package com.abhi.weatherapp.app;

import android.content.Context;
import android.util.Log;
import com.abhi.weatherapp.BuildConfig;
import com.abhi.weatherapp.model.db.MyObjectBox;
import io.objectbox.BoxStore;

public class ObjectBox {

    private static BoxStore boxStore;

    static void init(Context context) {
        boxStore = MyObjectBox.builder()
                .androidContext(context.getApplicationContext())
                .build();

        if (BuildConfig.DEBUG) {
            Log.d(App.TAG, String.format("Using ObjectBox %s (%s)",
                    BoxStore.getVersion(), BoxStore.getVersionNative()));
        }
    }

    // Use for any new database code
    public static BoxStore get() {
        return boxStore;
    }


}