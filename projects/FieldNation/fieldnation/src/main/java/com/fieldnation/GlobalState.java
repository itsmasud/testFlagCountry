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
import android.preference.PreferenceManager;
import android.support.multidex.MultiDex;

import com.crashlytics.android.Crashlytics;
import com.fieldnation.data.profile.Profile;
import com.fieldnation.data.workorder.ExpenseCategories;
import com.fieldnation.service.auth.AuthTopicClient;
import com.fieldnation.service.auth.AuthTopicService;
import com.fieldnation.service.auth.OAuth;
import com.fieldnation.service.crawler.WebCrawlerService;
import com.fieldnation.service.data.profile.ProfileClient;
import com.fieldnation.service.transaction.WebTransactionService;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import java.io.File;
import java.util.Calendar;

import io.fabric.sdk.android.Fabric;

/**
 * Defines some global values that will be shared between all objects.
 *
 * @author michael.carver
 */
public class GlobalState extends Application {
    private final String TAG = UniqueTag.makeTag("GlobalState");

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

    private static GlobalState _context;

    private Tracker _tracker;
    private Profile _profile;
    private GoogleAnalyticsTopicClient _gaTopicClient;
    private GlobalTopicClient _globalTopicClient;
    private ProfileClient _profileClient;
    private AuthTopicClient _authTopicClient;
    private int _memoryClass;
    private Typeface _iconFont;

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);

        MultiDex.install(this);
    }

    public GlobalState() {
        super();
        Log.v(TAG, "GlobalState");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Fabric.with(this, new Crashlytics());



        Log.v(TAG, "onCreate");

        PreferenceManager.setDefaultValues(getBaseContext(), R.xml.pref_general, false);

        _memoryClass = ((ActivityManager) getSystemService(ACTIVITY_SERVICE)).getMemoryClass();
        Log.v(TAG, "memoryClass " + _memoryClass);

        startService(new Intent(this, AuthTopicService.class));
        startService(new Intent(this, WebCrawlerService.class));

        _iconFont = Typeface.createFromAsset(getAssets(), "fonts/fnicons.ttf");
        _context = this;
        new ExpenseCategories(this);

        getTracker();

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.FROYO) {
            System.setProperty("http.keepalive", "false");
        }

        _gaTopicClient = new GoogleAnalyticsTopicClient(_gaTopicClient_listener);
        _gaTopicClient.connect(this);

        _globalTopicClient = new GlobalTopicClient(_globalTopic_listener);
        _globalTopicClient.connect(this);

        _profileClient = new ProfileClient(_profile_listener);
        _profileClient.connect(this);

        _authTopicClient = new AuthTopicClient(_authTopic_listener);
        _authTopicClient.connect(this);

        SharedPreferences syncSettings = PreferenceManager.getDefaultSharedPreferences(this);
        Log.v(TAG, "BP: " + syncSettings.getLong("pref_key_sync_start_time", 0));

        setInstallTime();
    }

    public int getMemoryClass() {
        return _memoryClass;
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

    public static GlobalState getContext() {
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
            _authTopicClient.registerAuthState();
            AuthTopicClient.dispatchRequestCommand(GlobalState.this);
        }

        @Override
        public void onAuthenticated(OAuth oauth) {
            Log.v(TAG, "onAuthenticated");
        }

        @Override
        public void onNotAuthenticated() {
            Log.v(TAG, "onNotAuthenticated");
            AuthTopicClient.dispatchRequestCommand(GlobalState.this);
        }
    };

    /*-*************************-*/
    /*-         Profile         -*/
    /*-*************************-*/
    private final GlobalTopicClient.Listener _globalTopic_listener = new GlobalTopicClient.Listener() {
        @Override
        public void onConnected() {
            _globalTopicClient.registerProfileInvalid(GlobalState.this);
            _globalTopicClient.registerNetworkConnect();
            _globalTopicClient.registerNetworkState();
        }

        @Override
        public void onProfileInvalid() {
            ProfileClient.get(GlobalState.this);
        }

        @Override
        public void onNetworkConnected() {
            AuthTopicClient.dispatchRequestCommand(GlobalState.this);
        }

        @Override
        public void onNetworkConnecting() {
        }

        @Override
        public void onNetworkConnect() {
            AuthTopicClient.dispatchRequestCommand(GlobalState.this);
            startService(new Intent(GlobalState.this, WebTransactionService.class));
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
            ProfileClient.get(GlobalState.this);
        }

        @Override
        public void onGet(Profile profile, boolean failed) {
            Log.v(TAG, "onProfile");
            if (profile != null) {
                _profile = profile;

                Crashlytics.setLong("userId", _profile.getUserId());

                GlobalTopicClient.dispatchGotProfile(GlobalState.this, profile);

                try {
                    Class<?> clazz = Class.forName("com.fieldnation.gcm.RegistrationIntentService");
                    Intent intent = new Intent(GlobalState.this, clazz);
                    startService(intent);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            } else {
                // TODO should do something... like retry or logout
            }
        }
    };

    public Profile getProfile() {
        return _profile;
    }

    /*-*********************-*/
    /*-         GA          -*/
    /*-*********************-*/
    private synchronized Tracker getTracker() {
        if (_tracker == null) {
            GoogleAnalytics analytics = GoogleAnalytics.getInstance(this);
//            analytics.getLogger().setLogLevel(Logger.LogLevel.VERBOSE);
            analytics.enableAutoActivityReports(this);
            analytics.setLocalDispatchPeriod(getResources().getInteger(R.integer.ga_local_dispatch_period));
            analytics.setDryRun(getResources().getBoolean(R.bool.ga_dry_run) || BuildConfig.DEBUG);
            _tracker = analytics.newTracker(R.xml.ga_config);
            _tracker.enableAdvertisingIdCollection(true);
            _tracker.enableAutoActivityTracking(true);
            _tracker.enableExceptionReporting(false);
        }
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
        public void onGaEvent(String category, String action, String label, Long value) {
            Tracker t = getTracker();

            HitBuilders.EventBuilder event = new HitBuilders.EventBuilder();

            event.setCategory(category).setAction(action).setLabel(label);

            if (value != null) {
                event.setValue(value);
            }

            t.send(event.build());
        }

        @Override
        public void onGaScreen(String screenName) {
            Tracker t = getTracker();
            t.setScreenName(screenName);
            t.send(new HitBuilders.AppViewBuilder().build());
        }

        @Override
        public void onGaTiming(String category, String variable, String label, Long value) {
            HitBuilders.TimingBuilder timing = new HitBuilders.TimingBuilder();
            Tracker t = getTracker();

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


    public void setInstallTime() {
        SharedPreferences settings = getSharedPreferences(PREF_NAME, 0);

        if (settings.contains(PREF_INSTALL_TIME))
            return;

        SharedPreferences.Editor edit = settings.edit();
        edit.putLong(PREF_INSTALL_TIME, System.currentTimeMillis());
        edit.apply();
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

    public String getStoragePath() {
        File externalPath = Environment.getExternalStorageDirectory();
        String packageName = getPackageName();
        File temppath = new File(externalPath.getAbsolutePath() + "/Android/data/" + packageName);
        temppath.mkdirs();
        return temppath.getAbsolutePath();
    }

    public boolean haveWifi() {
        ConnectivityManager connManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo wifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        return wifi.isConnected();
    }

    public boolean isCharging() {
        Intent intent = registerReceiver(null, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
        int plugged = intent.getIntExtra(BatteryManager.EXTRA_PLUGGED, -1);
        return (plugged == BatteryManager.BATTERY_PLUGGED_AC)
                || (plugged == BatteryManager.BATTERY_PLUGGED_USB);
    }
}
