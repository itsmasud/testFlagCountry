package com.fieldnation;

import android.content.Context;

import com.fieldnation.fnanalytics.EventAction;
import com.fieldnation.fnanalytics.EventCategory;
import com.fieldnation.fnanalytics.EventLabel;
import com.fieldnation.fnanalytics.EventProperty;
import com.fieldnation.fnanalytics.ScreenName;
import com.fieldnation.fnanalytics.TrackerWrapper;
import com.snowplowanalytics.snowplow.tracker.DevicePlatforms;
import com.snowplowanalytics.snowplow.tracker.Emitter;
import com.snowplowanalytics.snowplow.tracker.Subject;
import com.snowplowanalytics.snowplow.tracker.Tracker;
import com.snowplowanalytics.snowplow.tracker.events.ScreenView;
import com.snowplowanalytics.snowplow.tracker.events.Structured;
import com.snowplowanalytics.snowplow.tracker.events.Timing;
import com.snowplowanalytics.snowplow.tracker.payload.SelfDescribingJson;

import java.util.List;

/**
 * Created by Michael on 9/8/2016.
 */
public class SnowplowWrapper implements TrackerWrapper {
    private static Tracker _tracker = null;

    private static Tracker getTracker(Context context) {
        Context appContext = context.getApplicationContext();
        if (_tracker == null) {
            Emitter emitter = new Emitter.EmitterBuilder(
                    appContext.getString(R.string.sp_collector_uri), appContext)
                    .build();

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
                            .build());
        }
        return _tracker;
    }

    public void setUserId(Context context, Long userId) {
        getTracker(context).getSubject().setUserId(userId + "");
    }

    @Override
    public void event(Context context, EventCategory category, EventAction action,
                      EventLabel label, EventProperty property, Double value) {
        event(context, category.getValue(), action.getValue(), label.getValue(), property.getValue(), value, null, null, null, null);
    }

    private void event(Context context, String category, String action, String label,
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

    @Override
    public void screen(Context context, ScreenName name) {
        screen(context, name.getValue(), null, null, null, null, null);
    }

    private void screen(Context context, String name, String id, List<SelfDescribingJson> customContext, Long deviceCreatedTimestamp, Long trueTimestamp, String eventId) {
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

    @Override
    public void timing(Context context, String category, String label, Integer timing, String variable) {
        timing(context, category, label, timing, variable, null, null, null, null);
    }

    private void timing(Context context, String category, String label, Integer timing, String variable, List<SelfDescribingJson> customContext, Long deviceCreatedTimestamp, Long trueTimestamp, String eventId) {
        Timing.Builder<?> builder = Timing.builder();
        if (category != null)
            builder.category(category);
        if (label != null)
            builder.label(label);
        if (timing != null)
            builder.timing(timing);
        if (variable != null)
            builder.variable(variable);
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
