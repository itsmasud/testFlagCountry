package com.fieldnation.fnanalytics;

import android.content.Context;

/**
 * Created by Michael on 9/9/2016.
 */
public interface TrackerWrapper {

    String getTag();

    void setUserId(Context context, Long userId);

    void event(Context context, Event event);

    void screen(Context context, Screen screen);

    void timing(Context context, Timing timing);
}