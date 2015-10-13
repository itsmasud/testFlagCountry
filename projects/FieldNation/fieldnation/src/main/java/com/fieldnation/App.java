package com.fieldnation;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.BatteryManager;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.UserManager;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.multidex.MultiDex;
import android.text.TextUtils;

import com.crashlytics.android.Crashlytics;
import com.fieldnation.data.profile.Profile;
import com.fieldnation.data.workorder.ExpenseCategories;
import com.fieldnation.service.auth.AuthTopicClient;
import com.fieldnation.service.auth.AuthTopicService;
import com.fieldnation.service.auth.OAuth;
import com.fieldnation.service.crawler.WebCrawlerService;
import com.fieldnation.service.data.profile.ProfileClient;
import com.fieldnation.service.topics.TopicService;
import com.fieldnation.service.transaction.WebTransactionService;
import com.fieldnation.utils.Stopwatch;
import com.fieldnation.utils.misc;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import io.fabric.sdk.android.Fabric;

import java.io.File;
import java.net.URLConnection;
import java.util.Calendar;

/**
 * Defines some global values that will be shared between all objects.
 *
 * @author michael.carver
 */
public class App extends Application {
    private static final String STAG = "FNApplication";
    private final String TAG = UniqueTag.makeTag(STAG);

    public static final long DAY = 86400000;

    public static final String PREF_NAME = "GlobalPreferences";
    public static final String PREF_COMPLETED_WORKORDER = "PREF_HAS_COMPLETED_WORKORDER";
    public static final String PREF_SHOWN_REVIEW_DIALOG = "PREF_SHOWN_REVIEW_DIALOG";
    public static final String PREF_TOS_TIMEOUT = "PREF_TOS_TIMEOUT";
    public static final String PREF_COI_TIMEOUT = "PREF_COI_TIMEOUT";
    public static final String PREF_COI_NEVER = "PREF_COI_NEVER";
    public static final String PREF_INSTALL_TIME = "PREF_INSTALL_TIME";
    public static final String PREF_RATE_INTERACTION = "PREF_RATE_INTERACTION";
    public static final String PREF_RATE_SHOWN = "PREF_RATE_SHOWN";

    private static App _context;

    private Tracker _tracker;
    private Profile _profile;
    private GoogleAnalyticsTopicClient _gaTopicClient;
    private GlobalTopicClient _globalTopicClient;
    private ProfileClient _profileClient;
    private AuthTopicClient _authTopicClient;
    private int _memoryClass;
    private Typeface _iconFont;
    private Handler _handler = new Handler();
    private boolean _switchingUser = false;


    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);

        MultiDex.install(this);
    }

    public App() {
        super();
        Log.v(TAG, "GlobalState");
    }

    @Override
    public void onCreate() {
        // enable when trying to find ANRs and other weird bugs
//        if (BuildConfig.DEBUG) {
//            StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
//                    .detectAll()    // detect everything potentially suspect
//                    .penaltyLog()   // penalty is to write to log
//                    .build());
//            StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
//                    .detectAll()
//                    .penaltyLog()
//                    .build());
//        }

        super.onCreate();
        Stopwatch mwatch = new Stopwatch(true);
        Stopwatch watch = new Stopwatch(true);
        Log.v(TAG, "onCreate");
        // set the app context
        _context = this;

        // start up the debugging tools
        Debug.init();

        Debug.setBool("is_User_A_Monkey", ActivityManager.isUserAMonkey());

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            UserManager um = (UserManager) getSystemService(Context.USER_SERVICE);
            Debug.setBool("is_User_A_Goat", um.isUserAGoat());
        }

        Log.v(TAG, "debug init time: " + watch.finishAndRestart());

        // configure preferences
        PreferenceManager.setDefaultValues(getBaseContext(), R.xml.pref_general, false);
        Log.v(TAG, "preferenceManager time: " + watch.finishAndRestart());

        // discover the memory class of the device
        _memoryClass = ((ActivityManager) getSystemService(ACTIVITY_SERVICE)).getMemoryClass();
        Log.v(TAG, "memoryClass " + _memoryClass);
        Log.v(TAG, "memoryClass time: " + watch.finishAndRestart());
        Debug.setInt("memory_class", _memoryClass);

        // trigger authentication and web crawler
        new AsyncTaskEx<Context, Object, Object>() {
            @Override
            protected Object doInBackground(Context... params) {
                Context context = params[0];
                startService(new Intent(context, TopicService.class));
                startService(new Intent(context, AuthTopicService.class));
                startService(new Intent(context, WebCrawlerService.class));
                return null;
            }
        }.executeEx(this);
        Log.v(TAG, "start services time: " + watch.finishAndRestart());

        // load the icon fonts
        _iconFont = Typeface.createFromAsset(getAssets(), "fonts/fnicons.ttf");
        Log.v(TAG, "load iconfont time: " + watch.finishAndRestart());

        // read in exepense categories
        new ExpenseCategories(this);

        // GoogleAnalytics.getInstance(context) has been causing ANRs, so I'm running this in a separate thread for now
        new AsyncTaskEx<App, Object, Tracker>() {
            @Override
            protected Tracker doInBackground(App... params) {
                Stopwatch stopwatch = new Stopwatch(true);
                App app = params[0];
                GoogleAnalytics analytics = GoogleAnalytics.getInstance(app);
                analytics.enableAutoActivityReports(app);
                analytics.setLocalDispatchPeriod(app.getResources().getInteger(R.integer.ga_local_dispatch_period));
                analytics.setDryRun(app.getResources().getBoolean(R.bool.ga_dry_run) || BuildConfig.DEBUG);
                Tracker tracker = analytics.newTracker(R.xml.ga_config);
                tracker.enableAdvertisingIdCollection(true);
                tracker.enableAutoActivityTracking(true);
                tracker.enableExceptionReporting(false);
                Log.v(TAG, "Get Tracker time: " + stopwatch.finish());
                return tracker;
            }

            @Override
            protected void onPostExecute(Tracker tracker) {
                _tracker = tracker;
            }
        }.executeEx(this);

        watch.finishAndRestart();
        // TODO look at async task
        // in pre FROYO keepalive = true is buggy. disable for those versions
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.FROYO) {
            System.setProperty("http.keepalive", "false");
        }
        Log.v(TAG, "set keep alives time: " + watch.finishAndRestart());

        // set up event listeners
        // TODO look at using async task here
        _gaTopicClient = new GoogleAnalyticsTopicClient(_gaTopicClient_listener);
        _gaTopicClient.connect(this);

        _globalTopicClient = new GlobalTopicClient(_globalTopic_listener);
        _globalTopicClient.connect(this);

        _profileClient = new ProfileClient(_profile_listener);
        _profileClient.connect(this);

        _authTopicClient = new AuthTopicClient(_authTopic_listener);
        _authTopicClient.connect(this);
        Log.v(TAG, "start topic clients time: " + watch.finishAndRestart());

//        SharedPreferences syncSettings = PreferenceManager.getDefaultSharedPreferences(this);
//        Log.v(TAG, "BP: " + syncSettings.getLong("pref_key_sync_start_time", 0));

        // set the app's install date
        setInstallTime();
        Log.v(TAG, "set install time: " + watch.finishAndRestart());
        // new Thread(_anrReport).start();
        Log.v(TAG, "onCreate time: " + mwatch.finish());
    }

    private Runnable _anrReport = new Runnable() {
        @Override
        public void run() {
            while (true) {
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                anrReport();
            }
        }
    };

    public static void anrReport() {
        final Thread mainThread = Looper.getMainLooper().getThread();
        final StackTraceElement[] mainStackTrace = mainThread.getStackTrace();

        StringBuilder trace = new StringBuilder();
        for (StackTraceElement elem : mainStackTrace) {
            trace.append(elem.getClassName() + "." + elem.getMethodName() + "(" + elem.getFileName() + ":" + String.valueOf(elem.getLineNumber()) + ")\n");
        }

        Log.v(STAG, trace.toString());
    }

    public boolean isLowMemDevice() {
        return _memoryClass <= 70;
    }

    @Override
    public void onTerminate() {
        _gaTopicClient.disconnect(this);
        _profileClient.disconnect(this);
        _globalTopicClient.disconnect(this);
        _authTopicClient.disconnect(this);
        super.onTerminate();
        _context = null;
    }

    public static App get() {
        return _context;
    }

    public Typeface getIconFont() {
        return _iconFont;
    }

    /*-**********************-*/
    /*-         Auth         -*/
    /*-**********************-*/
    private final AuthTopicClient.Listener _authTopic_listener = new AuthTopicClient.Listener() {
        @Override
        public void onConnected() {
            Log.v(TAG, "onConnected");
            _authTopicClient.subAuthStateChange();
            AuthTopicClient.requestCommand(App.this);
        }

        @Override
        public void onAuthenticated(OAuth oauth) {
        }

        @Override
        public void onNotAuthenticated() {
            Log.v(TAG, "onNotAuthenticated");
            AuthTopicClient.requestCommand(App.this);
        }
    };

    /*-*************************-*/
    /*-         Profile         -*/
    /*-*************************-*/
    private final GlobalTopicClient.Listener _globalTopic_listener = new GlobalTopicClient.Listener() {
        @Override
        public void onConnected() {
            _globalTopicClient.subProfileInvalid(App.this);
            _globalTopicClient.subNetworkConnect();
            _globalTopicClient.subNetworkState();
        }

        @Override
        public void onProfileInvalid() {
            ProfileClient.get(App.this);
        }

        @Override
        public void onNetworkConnected() {
            AuthTopicClient.requestCommand(App.this);
        }

        @Override
        public void onNetworkConnecting() {
        }

        @Override
        public void onNetworkConnect() {
            AuthTopicClient.requestCommand(App.this);
            startService(new Intent(App.this, WebTransactionService.class));
        }

        @Override
        public void onNetworkDisconnected() {
        }
    };

    private final ProfileClient.Listener _profile_listener = new ProfileClient.Listener() {
        @Override
        public void onConnected() {
            Log.v(TAG, "_profile_listener.onConnected");
            _profileClient.subGet();
            _profileClient.subSwitchUser();
            ProfileClient.get(App.this);
        }

        @Override
        public void onGet(Profile profile, boolean failed) {
            Log.v(TAG, "onProfile");
            if (profile != null) {
                _profile = profile;

                Debug.setLong("user_id", _profile.getUserId());
                Debug.setUserIdentifier(_profile.getUserId() + "");

                if (!misc.isEmptyOrNull(_profile.getEmail()))
                    Debug.setUserEmail(_profile.getEmail());

                if (!misc.isEmptyOrNull(_profile.getFirstname()) && !misc.isEmptyOrNull(_profile.getLastname())) {
                    Debug.setUserName(_profile.getFirstname() + " " + _profile.getLastname());
                }

                GlobalTopicClient.gotProfile(App.this, profile);

                if (_switchingUser) {
                    GlobalTopicClient.userSwitched(App.this, profile);
                    _switchingUser = false;
                }

                try {
                    Class<?> clazz = Class.forName("com.fieldnation.gcm.RegistrationIntentService");
                    Intent intent = new Intent(App.this, clazz);
                    startService(intent);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            } else {
                // TODO should do something... like retry or logout
            }
        }

        @Override
        public void onSwitchUser(long userId, boolean failed) {
            if (!failed) {
                _switchingUser = true;
                ProfileClient.get(App.this, false);
            }
        }
    };

    public Profile getProfile() {
        return _profile;
    }

    public static long getProfileId() {
        Profile profile = get().getProfile();
        if (profile != null && profile.getUserId() != null) {
            return profile.getUserId();
        }
        return -1;
    }

    /*-*********************-*/
    /*-         GA          -*/
    /*-*********************-*/

    /**
     * Get's the google analytics tracker.
     *
     * @return The tracker object, can be null!
     */
    private synchronized Tracker getTracker() {
        return _tracker;
    }

    private final GoogleAnalyticsTopicClient.Listener _gaTopicClient_listener = new GoogleAnalyticsTopicClient.Listener() {
        @Override
        public void onConnected() {
            _gaTopicClient.registerEvents();
            _gaTopicClient.registerScreenView();
            _gaTopicClient.registerTiming();
        }

        @Override
        public void onGaEvent(final String category, final String action, final String label, final Long value) {
            Tracker t = getTracker();

            // if tracker is null, then queue this event to be handled later
            if (t == null) {
                _handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        onGaEvent(category, action, label, value);
                    }
                }, 100);
                return;
            }

            HitBuilders.EventBuilder event = new HitBuilders.EventBuilder();

            event.setCategory(category).setAction(action).setLabel(label);

            if (value != null) {
                event.setValue(value);
            }

            t.send(event.build());
        }

        @Override
        public void onGaScreen(final String screenName) {
            Tracker t = getTracker();

            // if tracker is null, then queue this event to be handled later
            if (t == null) {
                _handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        onGaScreen(screenName);
                    }
                }, 100);
                return;
            }

            t.setScreenName(screenName);
            t.send(new HitBuilders.AppViewBuilder().build());
        }

        @Override
        public void onGaTiming(final String category, final String variable, final String label, final Long value) {
            HitBuilders.TimingBuilder timing = new HitBuilders.TimingBuilder();
            Tracker t = getTracker();

            // if tracker is null, then queue this event to be handled later
            if (t == null) {
                _handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        onGaTiming(category, variable, label, value);
                    }
                }, 100);
                return;
            }

            if (category != null)
                timing.setCategory(category);

            if (variable != null)
                timing.setVariable(variable);

            if (label != null)
                timing.setLabel(label);

            if (value != null)
                timing.setValue(value);

            t.send(timing.build());
        }
    };


    public boolean canRemindCoi() {
        SharedPreferences settings = getSharedPreferences(PREF_NAME, 0);
        if (settings.contains(PREF_COI_NEVER))
            return false;

        return System.currentTimeMillis() > settings.getLong(PREF_COI_TIMEOUT, 0);
    }

    public void setCoiReminded() {
        Log.v(TAG, "setCoiReminded");
        // misc.printStackTrace("setCoiReminded");
        SharedPreferences settings = getSharedPreferences(PREF_NAME, 0);
        SharedPreferences.Editor edit = settings.edit();
        edit.putLong(PREF_COI_TIMEOUT, System.currentTimeMillis() + 604800000); // two weeks
        edit.apply();
    }

    public void setNeverRemindCoi() {
        SharedPreferences settings = getSharedPreferences(PREF_NAME, 0);
        SharedPreferences.Editor edit = settings.edit();
        edit.putBoolean(PREF_COI_NEVER, true);
        edit.apply();
    }

    public boolean canRemindTos() {
        SharedPreferences settings = getSharedPreferences(PREF_NAME, 0);
        return System.currentTimeMillis() > settings.getLong(PREF_TOS_TIMEOUT, 0);
    }


    public void setTosReminded() {
        SharedPreferences settings = getSharedPreferences(PREF_NAME, 0);
        SharedPreferences.Editor edit = settings.edit();
        edit.putLong(PREF_TOS_TIMEOUT, System.currentTimeMillis() + 172800000); // two days
        edit.apply();
    }

    public boolean shouldShowReviewDialog() {
        return !hasShownReviewDialog() && hasCompletedWorkorder() && BuildConfig.FLAVOR.equals("prod");
    }

    public boolean hasShownReviewDialog() {
        SharedPreferences settings = getSharedPreferences(PREF_NAME, 0);
        return settings.contains(PREF_SHOWN_REVIEW_DIALOG);
    }

    public void setShownReviewDialog() {
        SharedPreferences settings = getSharedPreferences(PREF_NAME, 0);
        SharedPreferences.Editor edit = settings.edit();
        edit.putBoolean(PREF_SHOWN_REVIEW_DIALOG, true);
        edit.apply();
    }

    public boolean hasCompletedWorkorder() {
        SharedPreferences settings = getSharedPreferences(PREF_NAME, 0);
        return settings.contains(PREF_COMPLETED_WORKORDER);
    }

    public void setCompletedWorkorder() {
        SharedPreferences settings = getSharedPreferences(PREF_NAME, 0);
        SharedPreferences.Editor edit = settings.edit();
        edit.putBoolean(PREF_COMPLETED_WORKORDER, true);
        edit.apply();
    }


    private void setInstallTime() {
        new AsyncTaskEx<Object, Object, Object>() {

            @Override
            protected Object doInBackground(Object... params) {
                SharedPreferences settings = getSharedPreferences(PREF_NAME, 0);

                if (settings.contains(PREF_INSTALL_TIME))
                    return null;

                SharedPreferences.Editor edit = settings.edit();
                edit.putLong(PREF_INSTALL_TIME, System.currentTimeMillis());
                edit.apply();
                return null;
            }
        }.executeEx();
    }

    public long getInstallTime() {
        SharedPreferences settings = getSharedPreferences(PREF_NAME, 0);

        if (!settings.contains(PREF_INSTALL_TIME)) {
            SharedPreferences.Editor edit = settings.edit();
            edit.putLong(PREF_INSTALL_TIME, System.currentTimeMillis());
            edit.apply();
            return System.currentTimeMillis();
        }

        return settings.getLong(PREF_INSTALL_TIME, System.currentTimeMillis());
    }

    public void setRateMeInteracted() {
        SharedPreferences settings = getSharedPreferences(PREF_NAME, 0);
        SharedPreferences.Editor edit = settings.edit();
        edit.putLong(PREF_RATE_INTERACTION, System.currentTimeMillis());
        edit.apply();
    }

    public long getRateMeInteracted() {
        SharedPreferences settings = getSharedPreferences(PREF_NAME, 0);

        if (!settings.contains(PREF_RATE_INTERACTION)) {
            return -1;
        }

        return settings.getLong(PREF_RATE_INTERACTION, System.currentTimeMillis());
    }

    public void setRateMeShown() {
        SharedPreferences settings = getSharedPreferences(PREF_NAME, 0);
        SharedPreferences.Editor edit = settings.edit();
        edit.putLong(PREF_RATE_SHOWN, System.currentTimeMillis());
        edit.apply();
    }

    public long getRateMeShown() {
        SharedPreferences settings = getSharedPreferences(PREF_NAME, 0);

        if (!settings.contains(PREF_RATE_SHOWN))
            return -1;

        return settings.getLong(PREF_RATE_SHOWN, 0);
    }

    public boolean showRateMe() {
        // if under 10 days, then no
        if (System.currentTimeMillis() - getInstallTime() < DAY * 10) {
            Log.v(TAG, "showRateMe: 10 day check failed");
            return false;
        }

        // if hasn't completed a work order, then no
        if (!hasCompletedWorkorder()) {
            Log.v(TAG, "showRateMe: completed check failed");
            return false;
        }

        // if have interacted before, then no
        if (System.currentTimeMillis() - getRateMeInteracted() < DAY) {
            Log.v(TAG, "showRateMe:  failed");
            return false;
        }

        // if not in the time restraints, then no
        Calendar cal = Calendar.getInstance();
        if (cal.get(Calendar.HOUR_OF_DAY) <= 11) {
            Log.v(TAG, "showRateMe:  time check failed");
            return false;
        }

        // if shown before, check time.
        if (System.currentTimeMillis() - getRateMeShown() < DAY * 10) {
            Log.v(TAG, "showRateMe:  shown before check failed");
            return false;
        }

        Log.v(TAG, "showRateMe:  ok!");
        return true;
    }

    public boolean isSdCardAvailable() {
        return android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED);
    }

    public String getStoragePath() {
        File externalPath = Environment.getExternalStorageDirectory();
        String packageName = getPackageName();
        File temppath = new File(externalPath.getAbsolutePath() + "/Android/data/" + packageName);
        temppath.mkdirs();
        return temppath.getAbsolutePath();
    }

    public String getDownloadsFolder() {
        File externalPath = Environment.getExternalStorageDirectory();
        File temppath = new File(externalPath.getAbsolutePath() + "/Download");
        temppath.mkdirs();
        return temppath.getAbsolutePath();
    }

    public boolean haveWifi() {
        ConnectivityManager connManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo wifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        return wifi.isConnected();
    }

    public boolean isCharging() {
        try {
            Intent intent = registerReceiver(null, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
            int plugged = intent.getIntExtra(BatteryManager.EXTRA_PLUGGED, -1);
            return (plugged == BatteryManager.BATTERY_PLUGGED_AC)
                    || (plugged == BatteryManager.BATTERY_PLUGGED_USB);
        } catch (Exception ex) {
            return false;
        }
    }

    public boolean isLocationEnabled() {
        int locationMode = 0;
        String locationProviders;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            try {
                locationMode = Settings.Secure.getInt(getContentResolver(), Settings.Secure.LOCATION_MODE);

            } catch (Settings.SettingNotFoundException e) {
                e.printStackTrace();
            }

            return locationMode != Settings.Secure.LOCATION_MODE_OFF;

        } else {
            locationProviders = Settings.Secure.getString(getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
            return !TextUtils.isEmpty(locationProviders);
        }
    }

    public static String guessContentTypeFromName(String url) {
        try {
            return URLConnection.guessContentTypeFromName(url);
        } catch (Exception ex) {
        }
        return "application/octet-stream";
    }
}
