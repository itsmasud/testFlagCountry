package com.fieldnation;

import android.app.Application;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;

import com.fieldnation.data.workorder.ExpenseCategories;
import com.fieldnation.rpc.server.DataCacheNode;
import com.fieldnation.rpc.server.PhotoCacheNode;
import com.fieldnation.rpc.server.Ws;
import com.fieldnation.topics.GaTopic;
import com.fieldnation.topics.TopicReceiver;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Logger;
import com.google.android.gms.analytics.Tracker;

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

//    private AuthenticationServer _authServer = null;

//    public static final int USER_ID = 375;

//    public String authority;
//    public String accountType;

//    private long _waitTime = 5000;
//    private long _lastDelayed = 0;

    private Tracker _tracker;

    public GlobalState() {
        super();

        Ws.USE_HTTPS = BuildConfig.USE_HTTPS;
    }

    private synchronized Tracker getTracker() {

        if (_tracker == null) {
            GoogleAnalytics analytics = GoogleAnalytics.getInstance(this);
            analytics.getLogger().setLogLevel(Logger.LogLevel.VERBOSE);
            analytics.enableAutoActivityReports(this);
            analytics.setLocalDispatchPeriod(60);
            analytics.setDryRun(false);
            _tracker = analytics.newTracker(R.xml.ga_config);
            _tracker.enableAdvertisingIdCollection(true);
            _tracker.enableAutoActivityTracking(true);
            _tracker.enableExceptionReporting(false);
        }
        return _tracker;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        DataCacheNode.flush(this);
        PhotoCacheNode.flush(this);

        new ExpenseCategories(this);

        getTracker();

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.FROYO) {
            System.setProperty("http.keepalive", "false");
        }

        GaTopic.subscribeEvent(this, TAG, _gaevent_receiver);
        GaTopic.subscribeScreenView(this, TAG, _gaevent_receiver);
        GaTopic.subscribeTiming(this, TAG, _gaevent_receiver);
    }

    private TopicReceiver _gaevent_receiver = new TopicReceiver(new Handler()) {
        @Override
        public void onTopic(int resultCode, String topicId, Bundle parcel) {
            if (GaTopic.EVENT.equals(topicId)) {
                String category = parcel.getString(GaTopic.EVENT_PARAM_CATEGORY);
                String action = parcel.getString(GaTopic.EVENT_PARAM_ACTION);
                String label = parcel.getString(GaTopic.EVENT_PARAM_LABEL);

                Long value = null;
                if (parcel.containsKey(GaTopic.EVENT_PARAM_VALUE)) {
                    value = parcel.getLong(GaTopic.EVENT_PARAM_VALUE);
                }

                sendGaEvent(category, action, label, value);
            } else if (GaTopic.SCREENVIEW.equals(topicId)) {
                String screenName = parcel.getString(GaTopic.SCREENVIEW_PARAM_NAME);

                sendGaScreen(screenName);
            } else if (GaTopic.TIMING.equals(topicId)) {
                String category = null;
                String variable = null;
                String label = null;
                Long value = null;

                if (parcel.containsKey(GaTopic.TIMING_PARAM_CATEGORY))
                    category = parcel.getString(GaTopic.TIMING_PARAM_CATEGORY);

                if (parcel.containsKey(GaTopic.TIMING_PARAM_LABEL))
                    label = parcel.getString(GaTopic.EVENT_PARAM_LABEL);

                if (parcel.containsKey(GaTopic.TIMING_PARAM_VARIABLE))
                    variable = parcel.getString(GaTopic.TIMING_PARAM_VARIABLE);

                if (parcel.containsKey(GaTopic.TIMING_PARAM_VALUE))
                    value = parcel.getLong(GaTopic.TIMING_PARAM_VALUE);

                sendGaTiming(category, variable, label, value);
            }
        }
    };

    public void sendGaEvent(String category, String action, String label, Long value) {
        Tracker t = getTracker();
        HitBuilders.EventBuilder event = new HitBuilders.EventBuilder();

        event.setCategory(category).setAction(action).setLabel(label);

        if (value != null) {
            event.setValue(value);
        }

        t.send(event.build());
    }

    public void sendGaScreen(String screenName) {
        Tracker t = getTracker();
        t.setScreenName(screenName);
        t.send(new HitBuilders.AppViewBuilder().build());
    }

    public void sendGaTiming(String category, String variable, String label, Long value) {
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

    public boolean hasShownReviewDialog() {
        SharedPreferences settings = getSharedPreferences(PREF_NAME, 0);
        return settings.contains(PREF_SHOWN_REVIEW_DIALOG);
    }

    public boolean hasCompletedWorkorder() {
        SharedPreferences settings = getSharedPreferences(PREF_NAME, 0);
        return settings.contains(PREF_COMPLETED_WORKORDER);
    }

    public boolean shouldShowReviewDialog() {
        return !hasShownReviewDialog() && hasCompletedWorkorder();
    }

    public void setShownReviewDialog() {
        SharedPreferences settings = getSharedPreferences(PREF_NAME, 0);
        SharedPreferences.Editor edit = settings.edit();
        edit.putBoolean(PREF_SHOWN_REVIEW_DIALOG, true);
        edit.commit();
    }

    public void setCompletedWorkorder() {
        SharedPreferences settings = getSharedPreferences(PREF_NAME, 0);
        SharedPreferences.Editor edit = settings.edit();
        edit.putBoolean(PREF_COMPLETED_WORKORDER, true);
        edit.commit();
    }


//    private long getNextDelay() {
//        return 3000;
//    }

    /**
     * Call this to connect an authentication server. Note, only one server can
     * be registered at a time.
     *
     * @param server
     */
//    public void setAuthenticationServer(AuthenticationServer server) {
//        _authServer = server;
//    }
//
//    public void requestAuthentication(AuthenticationClient client) {
//        if (_authServer == null) {
//            client.waitForObject(this, "_authServer");
//        } else {
//            _authServer.requestAuthentication(client);
//        }
//    }

//    public void requestAuthenticationDelayed(AuthenticationClient client) {
//        if (_authServer == null) {
//            client.waitForObject(this, "_authServer");
//        } else {
//            client.waitForTime(getNextDelay());
//        }
//    }

//    public void requestRemoveAccount(AuthenticationClient client) {
//        if (_authServer == null) {
//            client.waitForObject(this, "_authServer");
//        } else {
//            _authServer.removeAccount(client);
//        }
//    }
//
//    public void invalidateAuthToken(String token) {
//        _authServer.invalidateToken(token);
//    }
}
