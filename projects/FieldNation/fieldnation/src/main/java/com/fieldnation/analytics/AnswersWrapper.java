package com.fieldnation.analytics;

import android.content.Context;

import com.crashlytics.android.answers.Answers;
import com.crashlytics.android.answers.CustomEvent;
import com.fieldnation.Debug;
import com.fieldnation.fnanalytics.Event;
import com.fieldnation.fnanalytics.Screen;
import com.fieldnation.fnanalytics.Timing;
import com.fieldnation.fnanalytics.TrackerWrapper;

/**
 * Created by Michael on 9/13/2016.
 */
public class AnswersWrapper implements TrackerWrapper {
    public static final String TAG = "AnswersWrapper";

    @Override
    public String getTag() {
        return TAG;
    }

    @Override
    public void setUserId(Context context, Long userId) {
    }

    @Override
    public void event(Context context, Event event) {
        if (!Debug.isCrashlyticsRunning())
            return;

        if (event != null && event.category != null) {
            CustomEvent customEvent = new CustomEvent(event.category);
            if (event.action != null)
                customEvent.putCustomAttribute("action", event.action);
            if (event.label != null)
                customEvent.putCustomAttribute("label", event.label);
            if (event.property != null)
                customEvent.putCustomAttribute("property", event.property);
            if (event.value != null)
                customEvent.putCustomAttribute("value", event.value);

            Answers.getInstance().logCustom(customEvent);
        }
    }

    @Override
    public void screen(Context context, Screen screen) {
        if (!Debug.isCrashlyticsRunning())
            return;

        if (screen != null && screen.name != null) {
            Answers.getInstance().logCustom(
                    new CustomEvent("Screen View")
                            .putCustomAttribute("screen", screen.name));
        }
    }

    @Override
    public void timing(Context context, Timing timing) {
        if (!Debug.isCrashlyticsRunning())
            return;

        if (timing != null && timing.category != null && timing.label != null && timing.timing != null) {
            Answers.getInstance().logCustom(
                    new CustomEvent(timing.category)
                            .putCustomAttribute("label", timing.label)
                            .putCustomAttribute("time", timing.timing));
        }
    }
}
