package com.abhi.weatherapp.app;


import android.app.Application;

public class App extends Application {

    public static final String TAG = "DaocompatExample";

    @Override
    public void onCreate() {
        super.onCreate();
        ObjectBox.init(this);
    }

}
