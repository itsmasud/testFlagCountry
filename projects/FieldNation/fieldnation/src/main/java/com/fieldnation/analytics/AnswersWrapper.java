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

        if (event == null || !(event instanceof SimpleEvent))
            return;

        SimpleEvent simpleEvent = (SimpleEvent) event;

        if (simpleEvent.category != null) {
            CustomEvent customEvent = new CustomEvent(simpleEvent.category);
            if (simpleEvent.action != null) {
                customEvent.putCustomAttribute("Actions", simpleEvent.action);

                if (simpleEvent.label != null)
                    customEvent.putCustomAttribute(simpleEvent.action, simpleEvent.label);
                else
                    customEvent.putCustomAttribute(simpleEvent.action, 1);

            } else if (simpleEvent.label != null)
                customEvent.putCustomAttribute("Labels", simpleEvent.value);

            if (simpleEvent.property != null) {
                customEvent.putCustomAttribute("Properties", simpleEvent.property);

                if (simpleEvent.value != null)
                    customEvent.putCustomAttribute(simpleEvent.property, simpleEvent.value);

                if (simpleEvent.label != null)
                    customEvent.putCustomAttribute(simpleEvent.property, simpleEvent.label);

                if (simpleEvent.value == null && simpleEvent.label == null)
                    customEvent.putCustomAttribute(simpleEvent.property, 1);

            } else if (simpleEvent.value != null)
                customEvent.putCustomAttribute("Value", simpleEvent.value);

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
                            .putCustomAttribute("Screen", screen.name));
        }
    }

    @Override
    public void timing(Context context, Timing timing) {
        if (!Debug.isCrashlyticsRunning())
            return;

        if (timing != null && timing.category != null && timing.label != null && timing.timing != null) {
            Answers.getInstance().logCustom(
                    new CustomEvent(timing.category)
                            .putCustomAttribute(timing.label, timing.timing)
                            .putCustomAttribute("Labels", timing.label));
        }
    }
}
