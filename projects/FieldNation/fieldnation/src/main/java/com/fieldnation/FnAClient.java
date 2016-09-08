package com.fieldnation;

import android.content.Context;

import com.snowplowanalytics.snowplow.tracker.DevicePlatforms;
import com.snowplowanalytics.snowplow.tracker.Emitter;
import com.snowplowanalytics.snowplow.tracker.Subject;
import com.snowplowanalytics.snowplow.tracker.Tracker;
import com.snowplowanalytics.snowplow.tracker.events.ScreenView;
import com.snowplowanalytics.snowplow.tracker.events.Structured;
import com.snowplowanalytics.snowplow.tracker.payload.SelfDescribingJson;

import java.util.List;

/**
 * Created by Michael on 9/8/2016.
 */
public class FnAClient {

    // fn:dev -- appId
    // fn:live

    private static Tracker _tracker = null;

    private static Tracker getTracker(Context context) {
        Context appContext = context.getApplicationContext();
        if (_tracker == null) {
            Emitter emitter = new Emitter.EmitterBuilder("collector.fieldnation.com", appContext)
                    .build();

            String appId = "fn:dev";

            if (BuildConfig.FLAVOR.equals("prod"))
                appId = "fn:live";

            _tracker = Tracker.init(new Tracker
                    .TrackerBuilder(emitter, "myNamespace", appId, appContext)
                    .subject(new Subject
                            .SubjectBuilder()
                            .build()
                    )
                    .platform(DevicePlatforms.Mobile)
                    .build());
        }
        return _tracker;
    }

    public static void setUserId(Context context, Long userId) {
        getTracker(context).getSubject().setUserId(userId + "");
    }

    public static void event(Context context, String category, String action, String label,
                             String property, Double value) {
        event(context, category, action, label, property, value, null, null, null, null);
    }

    public static void event(Context context, String category, String action, String label,
                             String property, Double value, List<SelfDescribingJson> customContext,
                             Long deviceCreatedTimestamp, Long trueTimestamp, String eventId) {
        Structured.Builder<?> builder = Structured.builder();
        if (category != null)
            builder.category(category);
        if (action != null)
            builder.action(action);
        if (label != null)
            builder.label(label);
        if (property != null)
            builder.property(property);
        if (value != null)
            builder.value(value);
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

    public static void screen(Context context, String name, String id) {
        screen(context, name, id, null, null, null, null);
    }

    public static void screen(Context context, String name, String id, List<SelfDescribingJson> customContext, Long deviceCreatedTimestamp, Long trueTimestamp, String eventId) {
        ScreenView.Builder<?> builder = ScreenView.builder();
        if (name != null)
            builder.name(name);
        if (id != null)
            builder.id(id);
        if (customContext != null)
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
