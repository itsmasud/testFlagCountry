package com.fieldnation;

import android.os.Build;

import com.crashlytics.android.Crashlytics;
import com.github.anrwatchdog.ANRError;
import com.github.anrwatchdog.ANRWatchDog;

import io.fabric.sdk.android.Fabric;

/**
 * Created by Michael Carver on 8/31/2015.
 */
public class Debug {

    private static boolean _started = false;
    private static ANRWatchDog _anrWatchDog;
    private static Crashlytics _crashlytics = null;

    public static void init() {
        if (_started)
            return;

        _started = true;

        if (!BuildConfig.DEBUG) {
            _crashlytics = new Crashlytics();
            Fabric.with(App.get(), _crashlytics);
            setString("app_version", (BuildConfig.VERSION_NAME + " " + BuildConfig.BUILD_FLAVOR_NAME).trim());
            setString("sdk", Build.VERSION.SDK_INT + "");
            setBool("debug", BuildConfig.DEBUG);

            _anrWatchDog = new ANRWatchDog(5000);
            _anrWatchDog.setANRListener(new ANRWatchDog.ANRListener() {
                @Override
                public void onAppNotResponding(ANRError error) {
                    logException(error);
                }
            });
            _anrWatchDog.setInterruptionListener(new ANRWatchDog.InterruptionListener() {
                @Override
                public void onInterrupted(InterruptedException exception) {
                    logException(exception);
                }
            });
            _anrWatchDog.start();
        } else {
            _anrWatchDog = new ANRWatchDog(5000);
            _anrWatchDog.start();
        }
    }

    public static boolean isCrashlyticsRunning() {
        return _crashlytics != null;
    }

    public static void setLong(String name, long value) {
        if (_crashlytics == null)
            return;
        Crashlytics.setLong(name, value);
    }

    public static void setUserIdentifier(String id) {
        if (_crashlytics == null)
            return;
        Crashlytics.setUserIdentifier(id);
    }

    public static void logException(Throwable throwable) {
        if (_crashlytics == null)
            return;
        Crashlytics.logException(throwable);
    }

    public static void log(String msg) {
        if (_crashlytics == null)
            return;
        Crashlytics.log(msg);
    }

    public static void log(int priority, String tag, String msg) {
        if (_crashlytics == null)
            return;
        Crashlytics.log(priority, tag, msg);
    }

    public static void setUserName(String name) {
        if (_crashlytics == null)
            return;
        Crashlytics.setUserName(name);
    }

    public static void setUserEmail(String email) {
        if (_crashlytics == null)
            return;
        Crashlytics.setUserEmail(email);
    }

    public static void setString(String key, String value) {
        if (_crashlytics == null)
            return;
        Crashlytics.setString(key, value);
    }

    public static void setBool(String key, boolean value) {
        if (_crashlytics == null)
            return;
        Crashlytics.setBool(key, value);
    }

    public static void setDouble(String key, double value) {
        if (_crashlytics == null)
            return;
        Crashlytics.setDouble(key, value);
    }

    public static void setFloat(String key, float value) {
        if (_crashlytics == null)
            return;
        Crashlytics.setFloat(key, value);
    }

    public static void setInt(String key, int value) {
        if (_crashlytics == null)
            return;
        Crashlytics.setInt(key, value);
    }

}
