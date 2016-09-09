package com.fieldnation.fnanalytics;

import android.content.Context;

/**
 * Created by Michael on 9/9/2016.
 */
public interface TrackerWrapper {
    void setUserId(Context context, Long userId);

    void event(Context context, EventCategory category, EventAction action,
               EventLabel label, EventProperty property, Double value);

    void screen(Context context, ScreenName name);

    void timing(Context context, String category, String label, Integer timing, String variable);
}