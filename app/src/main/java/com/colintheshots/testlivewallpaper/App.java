package com.colintheshots.testlivewallpaper;

import android.app.Application;
import android.content.Context;

/**
 * Created by colin on 1/18/16.
 */
public class App extends Application {

    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        App.context = getApplicationContext();
    }

    public static Context getAppContext() {
        return App.context;
    }
}
