package com.fieldnation.analytics;

import android.content.Context;

import com.fieldnation.R;
import com.fieldnation.analytics.contexts.SpContext;
import com.fieldnation.analytics.contexts.SpScreenDisplayUiContext;
import com.fieldnation.analytics.contexts.SpSearchContext;
import com.fieldnation.analytics.contexts.SpUIContext;
import com.fieldnation.analytics.contexts.SpWorkOrderContext;
import com.fieldnation.fnanalytics.Event;
import com.fieldnation.fnanalytics.Screen;
import com.fieldnation.fnanalytics.TrackerWrapper;
import com.fieldnation.fnjson.JsonArray;
import com.fieldnation.fnjson.JsonObject;
import com.fieldnation.fnjson.Unserializer;
import com.fieldnation.fnlog.Log;
import com.fieldnation.fntools.Stopwatch;
import com.snowplowanalytics.snowplow.tracker.DevicePlatforms;
import com.snowplowanalytics.snowplow.tracker.Emitter;
import com.snowplowanalytics.snowplow.tracker.Subject;
import com.snowplowanalytics.snowplow.tracker.Tracker;
import com.snowplowanalytics.snowplow.tracker.events.ScreenView;
import com.snowplowanalytics.snowplow.tracker.events.SelfDescribing;
import com.snowplowanalytics.snowplow.tracker.events.Structured;
import com.snowplowanalytics.snowplow.tracker.events.Timing;
import com.snowplowanalytics.snowplow.tracker.payload.SelfDescribingJson;

import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Michael on 9/8/2016.
 */
public class SnowplowWrapper implements TrackerWrapper {
    public static final String TAG = "SnowplowWrapper";

    private static Hashtable<String, Class<? extends SpContext>> contexts = new Hashtable<>();

    static {
        contexts.put(SpUIContext.TAG, SpUIContext.class);
        contexts.put(SpScreenDisplayUiContext.TAG, SpScreenDisplayUiContext.class);
        contexts.put(SpSearchContext.TAG, SpSearchContext.class);
        contexts.put(SpWorkOrderContext.TAG, SpWorkOrderContext.class);
    }

    private static Tracker _tracker = null;

    private static Tracker getTracker(Context context) {
        Stopwatch stopwatch = new Stopwatch(true);
        try {
            Context appContext = context.getApplicationContext();
            if (_tracker == null) {
                // Build the emitter
                Emitter emitter = new Emitter.EmitterBuilder(
                        appContext.getString(R.string.sp_collector_uri), appContext)
                        .build();

                // Build the snowplow tracker
                _tracker = Tracker.init(
                        new Tracker.TrackerBuilder
                                (
                                        emitter,
                                        appContext.getString(R.string.sp_namespace),
                                        appContext.getString(R.string.sp_app_id),
                                        appContext
                                )
                                .subject(new Subject.SubjectBuilder().build())
                                .platform(DevicePlatforms.Mobile)
                                .geoLocationContext(true)
                                .mobileContext(true)
                                .sessionContext(true)
                                .build());
            }
            return _tracker;
        } finally {
            Log.v(TAG, "getTracker time: " + stopwatch.finish() + "ms");
        }
    }

    private List<SelfDescribingJson> buildExtraContexts(Context context, JsonArray extraContext) {
        List<SelfDescribingJson> list = new LinkedList<>();

        if (extraContext != null) {
            for (int i = 0; i < extraContext.size(); i++) {
                JsonObject obj = extraContext.getJsonObject(i);
                try {
                    if (obj.has("tag") && contexts.containsKey(obj.getString("tag"))) {
                        Class<? extends SpContext> clazz = contexts.get(obj.getString("tag"));
                        list.add(Unserializer.unserializeObject(clazz, obj).toSelfDescribingJson(context));
                    }
                } catch (Exception ex) {
                    Log.v(TAG, ex);
                }
            }
        }

        return list;
    }

    @Override
    public String getTag() {
        return TAG;
    }

    public void setUserId(Context context, Long userId) {
        getTracker(context).getSubject().setUserId(userId + "");
    }

    @Override
    public void event(Context context, Event event) {
        if (event instanceof SimpleEvent)
            event(context, (SimpleEvent) event, buildExtraContexts(context, ((SimpleEvent) event).extraContext), null, null, null);
        else if (event instanceof CustomEvent)
            event(context, (CustomEvent) event, buildExtraContexts(context, ((CustomEvent) event).extraContext));
    }

    private void event(Context context, SimpleEvent event, List<SelfDescribingJson> customContext,
                       Long deviceCreatedTimestamp, Long trueTimestamp, String eventId) {
        Structured.Builder<?> builder = Structured.builder();
        if (event != null) {
            if (event.category != null)
                builder.category(event.category);
            if (event.action != null)
                builder.action(event.action);
            if (event.label != null)
                builder.label(event.label);
            if (event.property != null)
                builder.property(event.property);
            if (event.value != null)
                builder.value(event.value.doubleValue());
        }
        if (customContext != null && customContext.size() > 0)
            builder.customContext(customContext);
        if (deviceCreatedTimestamp != null)
            builder.deviceCreatedTimestamp(deviceCreatedTimestamp);
        if (trueTimestamp != null)
            builder.trueTimestamp(trueTimestamp);
        if (eventId != null)
            builder.eventId(eventId);

        Tracker t = getTracker(context);
        t.track(builder.build());
    }

    private void event(Context context, CustomEvent event, List<SelfDescribingJson> customContext) {
        SelfDescribingJson sdj = customContext.remove(0);
        Tracker t = getTracker(context);
        t.track(SelfDescribing.builder()
                .eventData(sdj)
                .customContext(customContext)
                .build());
    }

    @Override
    public void screen(Context context, Screen screen) {
        screen(context, screen, buildExtraContexts(context, screen.extraContext), null, null, null);
    }

    private void screen(Context context, Screen screen, List<SelfDescribingJson> customContext, Long deviceCreatedTimestamp, Long trueTimestamp, String eventId) {
        ScreenView.Builder<?> builder = ScreenView.builder();
        if (screen != null) {
            if (screen.name != null)
                builder.name(screen.name);
//            if (id != null)
//                builder.id(id);
        }
        if (customContext != null && customContext.size() > 0)
            builder.customContext(customContext);
        if (deviceCreatedTimestamp != null)
            builder.deviceCreatedTimestamp(deviceCreatedTimestamp);
        if (trueTimestamp != null)
            builder.trueTimestamp(trueTimestamp);
        if (eventId != null)
            builder.eventId(eventId);

        Tracker t = getTracker(context);
        t.track(builder.build());
    }

    @Override
    public void timing(Context context, com.fieldnation.fnanalytics.Timing timing) {
        timing(context, timing, buildExtraContexts(context, timing.extraContext), null, null, null);
    }

    private void timing(Context context, com.fieldnation.fnanalytics.Timing timing, List<SelfDescribingJson> customContext, Long deviceCreatedTimestamp, Long trueTimestamp, String eventId) {
        Timing.Builder<?> builder = Timing.builder();
        if (timing != null) {
            if (timing.category != null)
                builder.category(timing.category);
            if (timing.label != null)
                builder.label(timing.label);
            if (timing.timing != null)
                builder.timing(timing.timing);
            if (timing.variable != null)
                builder.variable(timing.variable);
        }
        if (customContext != null && customContext.size() > 0)
            builder.customContext(customContext);
        if (deviceCreatedTimestamp != null)
            builder.deviceCreatedTimestamp(deviceCreatedTimestamp);
        if (trueTimestamp != null)
            builder.trueTimestamp(trueTimestamp);
        if (eventId != null)
            builder.eventId(eventId);

        Tracker t = getTracker(context);
        t.track(builder.build());
    }
}