package com.abhi.weatherapp.app;


import android.app.Application;
import com.abhi.weatherapp.model.db.MyObjectBox;
import io.objectbox.BoxStore;

public class App extends Application {

    private static App sApp;
    private BoxStore mBoxStore;

    @Override
    public void onCreate() {
        super.onCreate();
        sApp = this;
        mBoxStore = MyObjectBox.builder().androidContext(App.this).build();
    }

    public static App getApp() {
        return sApp;
    }

    public BoxStore getBoxStore() {
        return mBoxStore;
    }
}

