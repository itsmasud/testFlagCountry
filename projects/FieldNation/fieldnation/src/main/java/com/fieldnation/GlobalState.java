package com.fieldnation;

import android.app.Application;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;

import com.fieldnation.data.profile.Profile;
import com.fieldnation.data.workorder.ExpenseCategories;
import com.fieldnation.service.auth.AuthTopicService;
import com.fieldnation.service.data.oauth.OAuth;
import com.fieldnation.service.data.profile.ProfileDataClient;
import com.fieldnation.utils.misc;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Logger;
import com.google.android.gms.analytics.Tracker;

import java.io.File;

/**
 * Defines some global values that will be shared between all objects.
 *
 * @author michael.carver
 */
public class GlobalState extends Application {
    private static final String TAG = "GlobalState";

    public static final String PREF_NAME = "GlobalPreferences";
    public static final String PREF_COMPLETED_WORKORDER = "PREF_HAS_COMPLETED_WORKORDER";
    public static final String PREF_SHOWN_REVIEW_DIALOG = "PREF_SHOWN_REVIEW_DIALOG";
    public static final String PREF_TOS_TIMEOUT = "PREF_TOS_TIMEOUT";
    public static final String PREF_COI_TIMEOUT = "PREF_COI_TIMEOUT";
    public static final String PREF_COI_NEVER = "PREF_COI_NEVER";

    private static GlobalState _context;

    private Tracker _tracker;
    private Profile _profile;
    private GoogleAnalyticsTopicClient _gaTopicClient;
    private GlobalTopicClient _globalTopicClient;
    private ProfileDataClient _profileClient;

    public GlobalState() {
        super();
    }

    @Override
    public void onCreate() {
        super.onCreate();
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

        _profileClient = new ProfileDataClient(_profile_listener);
        _profileClient.connect(this);

        AuthTopicService.subscribeAuthState(this, 0, TAG + ":AuthTopicService", _authReceiver);
        Topics.subscribeProfileInvalidated(this, TAG + ":ProfileService", _profile_topicReceiver);
    }

    @Override
    public void onTerminate() {
        _gaTopicClient.disconnect(this);
        _profileClient.disconnect(this);
        _globalTopicClient.disconnect(this);
        super.onTerminate();
        _context = null;
    }

    public static GlobalState getContext() {
        return _context;
    }

    /*-**********************-*/
    /*-         Auth         -*/
    /*-**********************-*/
    private final AuthTopicReceiver _authReceiver = new AuthTopicReceiver(new Handler()) {
        @Override
        public void onAuthentication(OAuth auth, boolean isNew) {
            ProfileDataClient.getMyUserInformation(GlobalState.this);
        }

        @Override
        public void onRegister(int resultCode, String topicId) {
            AuthTopicService.requestAuthentication(GlobalState.this);
        }

    };

    /*-*************************-*/
    /*-         Profile         -*/
    /*-*************************-*/
    private final GlobalTopicClient.Listener _globalTopic_listener = new GlobalTopicClient.Listener() {
        @Override
        public void onConnected() {
            _globalTopicClient.registerProfileInvalid(GlobalState.this);
        }

        @Override
        public void onProfileInvalid() {
            ProfileDataClient.getProfile(GlobalState.this);
        }
    };

    private final ProfileDataClient.Listener _profile_listener = new ProfileDataClient.Listener() {
        @Override
        public void onConnected() {
            _profileClient.registerProfile();
        }

        @Override
        public void onProfile(Profile profile) {
            _profile = profile;
            GlobalTopicClient.dispatchGotProfile(GlobalState.this, profile);
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
            analytics.getLogger().setLogLevel(Logger.LogLevel.VERBOSE);
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
        misc.printStackTrace("setCoiReminded");
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

    public boolean hasShownReviewDialog() {
        SharedPreferences settings = getSharedPreferences(PREF_NAME, 0);
        return settings.contains(PREF_SHOWN_REVIEW_DIALOG);
    }

    public boolean hasCompletedWorkorder() {
        SharedPreferences settings = getSharedPreferences(PREF_NAME, 0);
        return settings.contains(PREF_COMPLETED_WORKORDER);
    }

    public boolean shouldShowReviewDialog() {
        return !hasShownReviewDialog() && hasCompletedWorkorder() && BuildConfig.FLAVOR.equals("prod");
    }

    public void setShownReviewDialog() {
        SharedPreferences settings = getSharedPreferences(PREF_NAME, 0);
        SharedPreferences.Editor edit = settings.edit();
        edit.putBoolean(PREF_SHOWN_REVIEW_DIALOG, true);
        edit.apply();
    }

    public void setCompletedWorkorder() {
        SharedPreferences settings = getSharedPreferences(PREF_NAME, 0);
        SharedPreferences.Editor edit = settings.edit();
        edit.putBoolean(PREF_COMPLETED_WORKORDER, true);
        edit.apply();
    }

    public String getStoragePath() {
        File externalPath = Environment.getExternalStorageDirectory();
        String packageName = getPackageName();
        File temppath = new File(externalPath.getAbsolutePath() + "/Android/data/" + packageName);
        temppath.mkdirs();
        return temppath.getAbsolutePath();
    }
}
