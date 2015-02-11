package com.fieldnation;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import com.fieldnation.auth.client.AuthTopicReceiver;
import com.fieldnation.auth.client.AuthTopicService;
import com.fieldnation.data.profile.Profile;
import com.fieldnation.data.workorder.ExpenseCategories;
import com.fieldnation.json.JsonObject;
import com.fieldnation.rpc.client.ProfileService;
import com.fieldnation.rpc.common.WebResultReceiver;
import com.fieldnation.rpc.common.WebServiceConstants;
import com.fieldnation.rpc.server.DataCacheNode;
import com.fieldnation.rpc.server.PhotoCacheNode;
import com.fieldnation.rpc.server.Ws;
import com.fieldnation.topics.GaTopic;
import com.fieldnation.topics.TopicReceiver;
import com.fieldnation.topics.Topics;
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
    public static final String PREF_PROFILE_ID = "PREF_PROFILE_ID";

    private Tracker _tracker;
    private ProfileService _service;
    private Profile _profile;
    private static GlobalState _context;

    public GlobalState() {
        super();

        Ws.USE_HTTPS = BuildConfig.USE_HTTPS;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        _context = this;
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

        AuthTopicService.subscribeAuthState(this, 0, TAG + ":AuthTopicService", _authReceiver);
        Topics.subscribeProfileInvalidated(this, TAG + ":ProfileService", _profile_topicReceiver);
    }

    @Override
    public void onTerminate() {
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
        public void onAuthentication(String username, String authToken, boolean isNew) {
            if (_service == null || isNew) {
                _service = new ProfileService(GlobalState.this, username, authToken, _resultReciever);
                if (_profile == null)
                    startService(_service.getMyUserInformation(0, true));
            }
        }

        @Override
        public void onAuthenticationFailed(boolean networkDown) {
            _service = null;
        }

        @Override
        public void onAuthenticationInvalidated() {
            _service = null;
        }

        @Override
        public void onRegister(int resultCode, String topicId) {
            AuthTopicService.requestAuthentication(GlobalState.this);
        }
    };

    private final WebResultReceiver _resultReciever = new WebResultReceiver(new Handler()) {
        private boolean isCached;

        @Override
        public void onSuccess(int resultCode, Bundle resultData) {
            new AsyncTaskEx<Bundle, Object, Profile>() {
                @Override
                protected Profile doInBackground(Bundle... params) {
                    Bundle resultData = params[0];
                    try {
                        JsonObject obj = new JsonObject(new String(
                                resultData.getByteArray(WebServiceConstants.KEY_RESPONSE_DATA)));
                        isCached = resultData.getBoolean(WebServiceConstants.KEY_RESPONSE_CACHED);
                        return Profile.fromJson(obj);
                    } catch (Exception e) {

                    }
                    return null;
                }

                @Override
                protected void onPostExecute(Profile profile) {
                    super.onPostExecute(profile);
                    if (profile == null) {
                        if (_service != null)
                            startService(_service.getMyUserInformation(0, false));
                    } else {
                        _profile = profile;
                        Topics.dispatchProfileUpdated(GlobalState.this, _profile);

                        if (isCached && _service != null)
                            startService(_service.getMyUserInformation(0, false));
                    }
                }
            }.executeEx(resultData);
        }

        @Override
        public Context getContext() {
            return GlobalState.this;
        }

        @Override
        public void onError(int resultCode, Bundle resultData, String errorType) {
            super.onError(resultCode, resultData, errorType);
            _service = null;
            AuthTopicService.requestAuthInvalid(GlobalState.this);
        }
    };

    /*-*************************-*/
    /*-         Profile         -*/
    /*-*************************-*/
    public Profile getProfile() {
        return _profile;
    }

    private final TopicReceiver _profile_topicReceiver = new TopicReceiver(new Handler()) {
        @Override
        public void onTopic(int resultCode, String topicId, Bundle parcel) {
            if (Topics.TOPIC_PROFILE_INVALIDATED.equals(topicId)) {
                if (_service != null) {
                    startService(
                            _service.getMyUserInformation(0, false));
                } else {
                    // TODO Profile invalid, but no service... what to do?
                    Log.v(TAG, "Profile invalid, but no service... what to do?");
                }
            }
        }
    };

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
            if (!BuildConfig.FLAVOR.equals("prod")) {
                _tracker.setAppVersion(BuildConfig.VERSION_NAME + ":" + BuildConfig.FLAVOR);
            }
        }
        return _tracker;
    }

    private final TopicReceiver _gaevent_receiver = new TopicReceiver(new Handler()) {
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
}
