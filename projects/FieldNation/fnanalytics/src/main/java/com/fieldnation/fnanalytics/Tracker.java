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

    public static void event(Context context, Event event) {
        for (int i = 0; i < _wrappers.size(); i++) {
            TrackerWrapper wrapper = _wrappers.get(i);
            if (event.tag == null || wrapper.getTag().equals(event.tag))
                wrapper.event(context, event);
        }
    }

    public static void screen(Context context, Screen screen) {
        for (int i = 0; i < _wrappers.size(); i++) {
            TrackerWrapper wrapper = _wrappers.get(i);
            if (screen.tag == null || wrapper.getTag().equals(screen.tag))
                wrapper.screen(context, screen);
        }
    }

    public static void timing(Context context, Timing timing) {
        for (int i = 0; i < _wrappers.size(); i++) {
            TrackerWrapper wrapper = _wrappers.get(i);
            if (timing.tag == null || wrapper.getTag().equals(timing.tag))
                wrapper.timing(context, timing);

        }
    }

}
