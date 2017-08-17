package com.fieldnation;

import android.Manifest;
import android.app.ActivityManager;
import android.app.Application;
import android.app.PendingIntent;
import android.content.ComponentCallbacks2;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.BatteryManager;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.UserManager;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.design.widget.Snackbar;
import android.support.multidex.MultiDex;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;

import com.fieldnation.analytics.AnswersWrapper;
import com.fieldnation.analytics.SnowplowWrapper;
import com.fieldnation.analytics.contexts.SpUIContext;
import com.fieldnation.data.profile.Profile;
import com.fieldnation.fnanalytics.Tracker;
import com.fieldnation.fnhttpjson.HttpJson;
import com.fieldnation.fnlog.Log;
import com.fieldnation.fntoast.ToastClient;
import com.fieldnation.fntools.AsyncTaskEx;
import com.fieldnation.fntools.ContextProvider;
import com.fieldnation.fntools.DateUtils;
import com.fieldnation.fntools.Stopwatch;
import com.fieldnation.fntools.UniqueTag;
import com.fieldnation.fntools.misc;
import com.fieldnation.gcm.RegistrationIntentService;
import com.fieldnation.service.auth.AuthTopicClient;
import com.fieldnation.service.auth.OAuth;
import com.fieldnation.service.data.photo.PhotoClient;
import com.fieldnation.service.data.profile.ProfileClient;
import com.fieldnation.service.transaction.WebTransactionService;
import com.google.android.gms.security.ProviderInstaller;

import java.io.File;
import java.security.SecureRandom;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

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
    public static final String PREF_INTERACTED_WORKORDER = "PREF_HAS_INTERACTED_WORKORDER";
    public static final String PREF_TOS_TIMEOUT = "PREF_TOS_TIMEOUT";
    public static final String PREF_COI_TIMEOUT = "PREF_COI_TIMEOUT";
    public static final String PREF_COI_NEVER = "PREF_COI_NEVER";
    public static final String PREF_INSTALL_TIME = "PREF_INSTALL_TIME";
    public static final String PREF_RATE_INTERACTION = "PREF_RATE_INTERACTION";
    public static final String PREF_RATE_SHOWN = "PREF_RATE_SHOWN";
    public static final String PREF_RATE_SHOWN_VERSION = "PREF_RATE_SHOWN_VERSION";
    public static final String PREF_RELEASE_NOTE_SHOWN = "PREF_RELEASE_NOTE_SHOWN";
    public static final String PREF_TOC_ACCEPTED = "PREF_TOC_ACCEPTED";
    public static final String PREF_NEEDS_CONFIRMATION = "PREF_NEEDS_CONFIRMATION";

    private static App _context;

    private Profile _profile;
    private GlobalTopicClient _globalTopicClient;
    private ProfileClient _profileClient;
    private AuthTopicClient _authTopicClient;
    private int _memoryClass;
    private Typeface _iconFont;
    private final Handler _handler = new Handler();
    private boolean _switchingUser = false;
    public String deviceToken = null;
    private boolean _isConnected = false;
    private OAuth _auth = null;
    private boolean _hasInteracted = false;

    // UI context hack
    private SpUIContext _spUiContext = new SpUIContext();

    private static final int BYTES_IN_MB = 1024 * 1024;
    private static final int THRESHOLD_FREE_MB = 5;

    public static final SecureRandom secureRandom = new SecureRandom();

    static {
        Log.setRoller(new DebugLogRoller());
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);

        MultiDex.install(this);
    }

    public App() {
        super();
        Log.v(TAG, "GlobalState");
        ContextProvider.setProvider(new ContextProvider.Provider() {
            @Override
            public Context get() {
                return App.this;
            }
        });
        Tracker.addTrackerWrapper(new SnowplowWrapper());
        Tracker.addTrackerWrapper(new AnswersWrapper());
    }

    public SpUIContext getSpUiContext() {
        return _spUiContext;
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

        HttpJson.setTempFolder(getTempFolder());
        HttpJson.setVersionName(BuildConfig.VERSION_NAME);

        try {
            ProviderInstaller.installIfNeeded(this);
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

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
/*
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
*/
        Log.v(TAG, "start services time: " + watch.finishAndRestart());

        // load the icon fonts
        _iconFont = Typeface.createFromAsset(getAssets(), "fonts/fnicons.ttf");
        Log.v(TAG, "load iconfont time: " + watch.finishAndRestart());

        watch.finishAndRestart();
        // in pre FROYO keepalive = true is buggy. disable for those versions
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.FROYO) {
            System.setProperty("http.keepalive", "false");
        }
        Log.v(TAG, "set keep alives time: " + watch.finishAndRestart());

        // set up event listeners
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

        NotificationDef.configureNotifications(this);
        Log.v(TAG, "onCreate time: " + mwatch.finish());
    }

    private Runnable _anrReport = new Runnable() {
        @Override
        public void run() {
            while (true) {
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    Log.v(TAG, e);
                }

                anrReport();
            }
        }
    };

    private SharedPreferences _userPreferences = null;

    public SharedPreferences getSharedPreferences() {
        if (_userPreferences == null) {
            _userPreferences = getSharedPreferences(getPackageName() + "_preferences",
                    Context.MODE_MULTI_PROCESS | Context.MODE_PRIVATE);
        }

        return _userPreferences;
    }

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
    public OAuth getAuth() {
        synchronized (STAG) {
            return _auth;
        }
    }

    private void setAuth(OAuth auth) {
        synchronized (STAG) {
            _auth = auth;
        }
    }

    private final AuthTopicClient.Listener _authTopic_listener = new AuthTopicClient.Listener() {
        @Override
        public void onConnected() {
            Log.v(TAG, "onConnected");
            _authTopicClient.subAuthStateChange();
            AuthTopicClient.requestCommand(App.this);
        }

        @Override
        public void onAuthenticated(OAuth oauth) {
            _isConnected = true;
            setAuth(oauth);
        }

        @Override
        public void onNotAuthenticated() {
            Log.v(TAG, "onNotAuthenticated");
            setAuth(null);
            AuthTopicClient.requestCommand(App.this);
        }
    };

    /*-*****************************-*/
    /*-         Permissions         -*/
    /*-*****************************-*/

    public static String[] getPermissions() {
        List<String> perms = new LinkedList<>();

        perms.add("com.google.android.c2dm.permission.RECEIVE");
        perms.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        perms.add(Manifest.permission.INTERNET);
        perms.add(Manifest.permission.READ_SYNC_SETTINGS);
        perms.add(Manifest.permission.WRITE_SYNC_SETTINGS);
        perms.add(Manifest.permission.VIBRATE);
        perms.add(Manifest.permission.ACCESS_NETWORK_STATE);
        perms.add(Manifest.permission.WAKE_LOCK);

        if (Build.VERSION.SDK_INT >= 19) {
            // this permission is implicit pre 19. After 19 we have to ask for it.
            perms.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        }

/* not needed since the permissions checking is only needed 23+
        if (Build.VERSION.SDK_INT <= 22) {
            // These were removed 23+
            perms.add(Manifest.permission.GET_ACCOUNTS);
            perms.add("android.permission.MANAGE_ACCOUNTS");
            perms.add("android.permission.AUTHENTICATE_ACCOUNTS");
            perms.add("android.permission.USE_CREDENTIALS");
        }
*/

        return perms.toArray(new String[perms.size()]);
    }

    public static boolean[] getPermissionsRequired() {
        boolean[] required = new boolean[getPermissions().length];
        int i = 0;

        //perms.add("com.google.android.c2dm.permission.RECEIVE");
        required[i++] = false;
        //perms.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        required[i++] = true;
        //perms.add(Manifest.permission.INTERNET);
        required[i++] = true;
        //perms.add(Manifest.permission.READ_SYNC_SETTINGS);
        required[i++] = false;
        //perms.add(Manifest.permission.WRITE_SYNC_SETTINGS);
        required[i++] = false;
        //perms.add(Manifest.permission.VIBRATE);
        required[i++] = false;
        //perms.add(Manifest.permission.ACCESS_NETWORK_STATE);
        required[i++] = true;
        //perms.add(Manifest.permission.WAKE_LOCK);
        required[i++] = true;

        if (Build.VERSION.SDK_INT >= 19) {
            // this permission is implicit pre 19. After 19 we have to ask for it.
            //perms.add(Manifest.permission.READ_EXTERNAL_STORAGE);
            required[i++] = true;
        }

        return required;
    }

    /*-*************************-*/
    /*-         Profile         -*/
    /*-*************************-*/

    public boolean isConnected() {
        return _isConnected;
    }

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
            _isConnected = true;
            Log.v(TAG, "onNetworkConnected");
            AuthTopicClient.requestCommand(App.this);
            ToastClient.dismissSnackbar(App.this, 1);
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
            Log.v(TAG, "onNetworkDisconnected");
            _isConnected = false;
            Intent intent = GlobalTopicClient.networkConnectIntent(App.this);
            if (intent != null) {
                PendingIntent pi = PendingIntent.getService(App.this, App.secureRandom.nextInt(), intent, 0);
                ToastClient.snackbar(App.this, 1, "Can't connect to servers.", "RETRY", pi, Snackbar.LENGTH_INDEFINITE);
            }
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

                Tracker.setUserId(App.this, _profile.getUserId());

                Debug.setLong("user_id", _profile.getUserId());
                Debug.setUserIdentifier(_profile.getUserId() + "");

                if (!misc.isEmptyOrNull(_profile.getEmail()))
                    Debug.setUserEmail(_profile.getEmail());

                if (!misc.isEmptyOrNull(_profile.getFirstname()) && !misc.isEmptyOrNull(_profile.getLastname())) {
                    Debug.setUserName(_profile.getFirstname() + " " + _profile.getLastname());
                }

                if (deviceToken != null) {
                    ProfileClient.actionRegisterDevice(App.this, deviceToken, _profile.getUserId());
                    deviceToken = null;
                }

                GlobalTopicClient.gotProfile(App.this, profile);

                if (_switchingUser) {
                    GlobalTopicClient.userSwitched(App.this, profile);
                    _switchingUser = false;
                }

                try {
                    Intent intent = new Intent(App.this, RegistrationIntentService.class);
                    startService(intent);
                } catch (Exception ex) {
                    Log.v(TAG, ex);
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

    public void setNeedsConfirmation(boolean needsConfirmation) {
        SharedPreferences settings = getSharedPreferences(PREF_NAME, 0);
        SharedPreferences.Editor edit = settings.edit();
        edit.putBoolean(PREF_NEEDS_CONFIRMATION, needsConfirmation);
        edit.apply();
    }

    public boolean needsConfirmation() {
        SharedPreferences settings = getSharedPreferences(PREF_NAME, 0);
        return settings.getBoolean(PREF_NEEDS_CONFIRMATION, false);
    }

    public void setReleaseNoteShownReminded() {
        Log.v(TAG, "setReleaseNoteReminded");
        // misc.printStackTrace("setCoiReminded");
        SharedPreferences settings = getSharedPreferences(PREF_NAME, 0);
        SharedPreferences.Editor edit = settings.edit();
        edit.putLong(PREF_RELEASE_NOTE_SHOWN, BuildConfig.VERSION_CODE);
        edit.apply();
    }

    public boolean hasReleaseNoteShownForThisVersion() {
        SharedPreferences settings = getSharedPreferences(PREF_NAME, 0);
        return BuildConfig.VERSION_CODE == settings.getLong(PREF_RELEASE_NOTE_SHOWN, -1);
    }

    public void setToCAccepted() {
        Log.v(TAG, "setToCAccepted");
        SharedPreferences settings = getSharedPreferences(PREF_NAME, 0);
        SharedPreferences.Editor edit = settings.edit();
        edit.putLong(PREF_TOC_ACCEPTED, System.currentTimeMillis());
        edit.apply();
    }

    public boolean shouldShowTermsAndConditionsDialog() {
        SharedPreferences settings = getSharedPreferences(PREF_NAME, 0);
        if (settings.contains(PREF_TOC_ACCEPTED)) {
            Date tosDate = null;
            DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
            try {
                tosDate = df.parse(getString(R.string.tos_date));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            return (new DateUtils().isBeforeDay(new Date(settings.getLong(PREF_TOC_ACCEPTED, -1)), tosDate));
        } else return true;
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

    public boolean hasInteractedWorkorder() {
        if (_hasInteracted)
            return true;

        SharedPreferences settings = getSharedPreferences(PREF_NAME, 0);
        _hasInteracted = settings.contains(PREF_INTERACTED_WORKORDER);
        return _hasInteracted;
    }

    public void setInteractedWorkorder() {
        if (_hasInteracted)
            return;

        SharedPreferences settings = getSharedPreferences(PREF_NAME, 0);
        SharedPreferences.Editor edit = settings.edit();
        edit.putBoolean(PREF_INTERACTED_WORKORDER, true);
        edit.apply();
        _hasInteracted = true;
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
        edit.putLong(PREF_RATE_SHOWN_VERSION, BuildConfig.VERSION_CODE);
        edit.apply();
    }

    public long getRateMeInteracted() {
        SharedPreferences settings = getSharedPreferences(PREF_NAME, 0);

        if (!settings.contains(PREF_RATE_INTERACTION)) {
            return -1;
        }

        return settings.getLong(PREF_RATE_INTERACTION, System.currentTimeMillis());
    }

    public boolean getRateMeInteractedThisVersion() {
        SharedPreferences settings = getSharedPreferences(PREF_NAME, 0);
        return BuildConfig.VERSION_CODE == settings.getLong(PREF_RATE_SHOWN_VERSION, -1);
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

    public boolean onlyUploadWithWifi() {
        Stopwatch stopwatch = new Stopwatch(true);
        try {
            boolean flag = getSharedPreferences().getBoolean(getString(R.string.pref_key_use_wifi_to_upload), false);
            Log.v(TAG, "onlyUploadWithWifi " + flag);
            return flag;
        } finally {
            Log.v(TAG, "onlyUploadWithWifi time:" + stopwatch.finish());
        }
    }

    public boolean showRateMe() {
        // if hasn't completed a work order, then no
        if (!hasInteractedWorkorder()) {
            Log.v(TAG, "showRateMe: completed check failed");
            return false;
        }

        // if not in the time restraints (10am to 5pm), then no
        Calendar cal = Calendar.getInstance();
        if (cal.get(Calendar.HOUR_OF_DAY) < 10 || cal.get(Calendar.HOUR_OF_DAY) > 17) {
            Log.v(TAG, "showRateMe:  time check failed");
            return false;
        }

        // if shown before, check time.
        if (System.currentTimeMillis() - getRateMeShown() < DAY * 14) {
            Log.v(TAG, "showRateMe:  shown before check failed");
            return false;
        }

        // if shown this version, leave
        if (getRateMeInteractedThisVersion()) {
            Log.v(TAG, "showRateMe: shown this version check failed");
            return false;
        }

        Log.v(TAG, "showRateMe:  ok!");
        return true;
    }

    public boolean isSdCardAvailable() {
        return android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED);
    }

    public String getPicturePath() {
        File path = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).toString() + "/FieldNation");
        path.mkdirs();
        return path.getAbsolutePath();
    }

    public String getStoragePath() {
        File temppath = getFilesDir();
        temppath.mkdirs();
        return temppath.getAbsolutePath();
    }

    public String getDownloadsFolder() {
        File externalPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        File temppath = new File(externalPath.getAbsolutePath() + "/FieldNation");
        temppath.mkdirs();
        return temppath.getAbsolutePath();
    }

    public String getTempFolder() {
        File tempFolder = getCacheDir();
        tempFolder.mkdirs();
        return tempFolder.getAbsolutePath();
    }

    private boolean _haveWifi = false;
    private long _haveWifiLast = 0;
    private static final long HAVE_WIFI_TIMEOUT = 1000;

    public boolean haveWifi() {
        if (_haveWifiLast < System.currentTimeMillis()) {
            ConnectivityManager connManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo wifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            _haveWifi = wifi.isConnected();
            _haveWifiLast = System.currentTimeMillis() + HAVE_WIFI_TIMEOUT;
        }

        return _haveWifi;
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
                Log.v(TAG, e);
            }

            return locationMode != Settings.Secure.LOCATION_MODE_OFF;

        } else {
            locationProviders = Settings.Secure.getString(getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
            return !TextUtils.isEmpty(locationProviders);
        }
    }

    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
        Log.i(TAG, "Memory Trim Level: " + level);

        PhotoClient.clearPhotoClientCache();
        switch (level) {
            case ComponentCallbacks2.TRIM_MEMORY_BACKGROUND:
                break;
            case ComponentCallbacks2.TRIM_MEMORY_COMPLETE:
                break;
            case ComponentCallbacks2.TRIM_MEMORY_MODERATE:
                break;
            case ComponentCallbacks2.TRIM_MEMORY_RUNNING_CRITICAL:
                break;
            case ComponentCallbacks2.TRIM_MEMORY_RUNNING_LOW:
                break;
            case ComponentCallbacks2.TRIM_MEMORY_RUNNING_MODERATE:
                break;
            case ComponentCallbacks2.TRIM_MEMORY_UI_HIDDEN:
                break;
            default:
                break;
        }
    }

    public boolean isFreeSpaceAvailable() {
        try {
            final long freeMBInternal = new File(getFilesDir().getAbsoluteFile().toString()).getFreeSpace() / BYTES_IN_MB;
            final long freeMBExternal = new File(getExternalFilesDir(null).toString()).getFreeSpace() / BYTES_IN_MB;

            Log.v(TAG, "Free internal space:" + freeMBInternal);
            Log.v(TAG, "Free external space:" + freeMBExternal);

            return freeMBInternal >= THRESHOLD_FREE_MB || freeMBExternal >= THRESHOLD_FREE_MB;
        } catch (Exception ex) {
            Log.v(TAG, ex);
            return true;
        }
    }

    public static boolean isNcns() {
        return BuildConfig.BUILD_FLAVOR_NAME.equals("NCNS");
    }

    public static Uri getUriFromFile(File file) {
        return FileProvider.getUriForFile(get(), get().getApplicationContext().getPackageName() + ".provider", file);
    }

}
