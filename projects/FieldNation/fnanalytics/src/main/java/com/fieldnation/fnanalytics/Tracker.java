package com.fieldnation.fnanalytics;

import android.content.Context;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by Michael on 9/9/2016.
 */
public class Tracker {
    private static List<TrackerWrapper> _wrappers = new LinkedList<>();

    public static void addTrackerWrapper(TrackerWrapper wrapper) {
        _wrappers.add(wrapper);
    }

    public static void setUserId(Context context, Long userId) {
        for (int i = 0; i < _wrappers.size(); i++) {
            _wrappers.get(i).setUserId(context, userId);
        }
    }

    public static void event(Context context, EventCategory category, EventAction action,
                             EventLabel label, EventProperty property, Long value) {
        if (value != null)
            event(context, category, action, label, property, value.doubleValue());
        else
            event(context, category, action, label, property, (Double) null);
    }

    public static void event(Context context, EventCategory category, EventAction action,
                             EventLabel label, EventProperty property, Double value) {
        for (int i = 0; i < _wrappers.size(); i++) {
            _wrappers.get(i).event(context, category, action, label, property, value);
        }
    }

    public static void screen(Context context, ScreenName name) {
        for (int i = 0; i < _wrappers.size(); i++) {
            _wrappers.get(i).screen(context, name);
        }
    }

    public static void timing(Context context, String category, String label, Integer timing, String variable) {
        for (int i = 0; i < _wrappers.size(); i++) {
            _wrappers.get(i).timing(context, category, label, timing, variable);

        }
    }

}
