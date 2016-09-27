package com.fieldnation;

import android.os.Build;

import com.crashlytics.android.Crashlytics;
import com.crashlytics.android.answers.Answers;
import com.fieldnation.fntools.AsyncTaskEx;
import com.github.anrwatchdog.ANRError;
import com.github.anrwatchdog.ANRWatchDog;

import java.util.LinkedList;
import java.util.List;

import io.fabric.sdk.android.Fabric;

/**
 * Created by Michael Carver on 8/31/2015.
 */
public class Debug {
    private static final String TAG = "Debug";
    //private static final boolean USE_CRASHLYTICS = !BuildConfig.DEBUG;
    private static final boolean USE_CRASHLYTICS = true;
    private static boolean _started = false;
    private static ANRWatchDog _anrWatchDog;
    private static Crashlytics _crashlytics = null;
    private static final List<Runnable> _todo = new LinkedList<>();

    public static void init() {
        if (_started)
            return;

        _started = true;

        if (USE_CRASHLYTICS) {
            new AsyncTaskEx<Object, Object, Object>() {
                @Override
                protected Object doInBackground(Object... params) {
                    Crashlytics c = new Crashlytics();
                    Fabric.with(App.get(), c);
                    Fabric.with(App.get(), new Answers());
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
                    _crashlytics = c;
                    dumpTodo();
                    return null;
                }
            }.executeEx();
        } else {
//            _anrWatchDog = new ANRWatchDog(5000);
//            _anrWatchDog.start();
        }
    }

    public static boolean isCrashlyticsRunning() {
        return _crashlytics != null;
    }

    private static void dumpTodo() {
        synchronized (TAG) {
            try {
                if (_crashlytics != null && _todo.size() > 0) {
                    // we do a for loop just in case the runnable adds items to the list
                    // we don't want to get stuck here forever
                    int count = _todo.size();
                    for (int i = 0; i < count; i++) {
                        Runnable r = _todo.remove(0);
                        if (r != null)
                            r.run();
                    }
                }
            } catch (Exception ex) {
            }
        }
    }

    public static void setLong(final String name, final long value) {
        if (_crashlytics == null) {
            if (USE_CRASHLYTICS) {
                _todo.add(new Runnable() {
                    @Override
                    public void run() {
                        setLong(name, value);
                    }
                });
            }
            return;
        }
        Crashlytics.setLong(name, value);
    }

    public static void setUserIdentifier(final String id) {
        if (_crashlytics == null) {
            if (USE_CRASHLYTICS) {
                _todo.add(new Runnable() {
                    @Override
                    public void run() {
                        setUserIdentifier(id);
                    }
                });
            }
            return;
        }
        Crashlytics.setUserIdentifier(id);
    }

    public static void logException(final Throwable throwable) {
        if (_crashlytics == null) {
            if (USE_CRASHLYTICS) {
                _todo.add(new Runnable() {
                    @Override
                    public void run() {
                        logException(throwable);
                    }
                });
            }
            return;
        }
        Crashlytics.logException(throwable);
    }

    public static void log(final String msg) {
        if (_crashlytics == null) {
            if (USE_CRASHLYTICS) {
                _todo.add(new Runnable() {
                    @Override
                    public void run() {
                        log(msg);
                    }
                });
            }
            android.util.Log.println(android.util.Log.VERBOSE, "Debug", msg);
            return;
        }
        Crashlytics.log(msg);
    }

    public static void log(final int priority, final String tag, final String msg) {
        if (_crashlytics == null) {
            if (USE_CRASHLYTICS) {
                _todo.add(new Runnable() {
                    @Override
                    public void run() {
                        log(priority, tag, msg);
                    }
                });
            }
            android.util.Log.println(priority, tag, msg);
            return;
        }
        Crashlytics.log(priority, tag, msg);
    }

    public static void setUserName(final String name) {
        if (_crashlytics == null) {
            if (USE_CRASHLYTICS) {
                _todo.add(new Runnable() {
                    @Override
                    public void run() {
                        setUserName(name);
                    }
                });
            }
            return;
        }
        Crashlytics.setUserName(name);
    }

    public static void setUserEmail(final String email) {
        if (_crashlytics == null) {
            if (USE_CRASHLYTICS) {
                _todo.add(new Runnable() {
                    @Override
                    public void run() {
                        setUserEmail(email);
                    }
                });
            }
            return;
        }
        Crashlytics.setUserEmail(email);
    }

    public static void setString(final String key, final String value) {
        if (_crashlytics == null) {
            if (USE_CRASHLYTICS) {
                _todo.add(new Runnable() {
                    @Override
                    public void run() {
                        setString(key, value);
                    }
                });
            }
            return;
        }
        Crashlytics.setString(key, value);
    }

    public static void setBool(final String key, final boolean value) {
        if (_crashlytics == null) {
            if (USE_CRASHLYTICS) {
                _todo.add(new Runnable() {
                    @Override
                    public void run() {
                        setBool(key, value);
                    }
                });
            }
            return;
        }
        Crashlytics.setBool(key, value);
    }

    public static void setDouble(final String key, final double value) {
        if (_crashlytics == null) {
            if (USE_CRASHLYTICS) {
                _todo.add(new Runnable() {
                    @Override
                    public void run() {
                        setDouble(key, value);
                    }
                });
            }
            return;
        }
        Crashlytics.setDouble(key, value);
    }

    public static void setFloat(final String key, final float value) {
        if (_crashlytics == null) {
            if (USE_CRASHLYTICS) {
                _todo.add(new Runnable() {
                    @Override
                    public void run() {
                        setFloat(key, value);
                    }
                });
            }
            return;
        }
        Crashlytics.setFloat(key, value);
    }

    public static void setInt(final String key, final int value) {
        if (_crashlytics == null) {
            if (USE_CRASHLYTICS) {
                _todo.add(new Runnable() {
                    @Override
                    public void run() {
                        setInt(key, value);
                    }
                });
            }
            return;
        }
        Crashlytics.setInt(key, value);
    }
}
