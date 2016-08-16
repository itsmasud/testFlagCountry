package com.fieldnation.fnlogexample;

import android.app.Application;

import com.fieldnation.fnlog.DefaultRoller;
import com.fieldnation.fnlog.Log;

/**
 * Created by Michael on 8/15/2016.
 */
public class App extends Application {

    static {
        Log.setRoller(new DefaultRoller() {
            @Override
            public void println(int priority, String tag, String msg) {
                android.util.Log.println(priority, "TEST-" + tag, msg);
            }
        });
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }
}
