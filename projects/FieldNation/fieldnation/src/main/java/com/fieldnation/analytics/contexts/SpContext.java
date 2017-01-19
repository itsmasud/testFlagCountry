package com.fieldnation.analytics.contexts;

import android.content.Context;

import com.snowplowanalytics.snowplow.tracker.payload.SelfDescribingJson;

/**
 * Created by Michael on 9/15/2016.
 */
public interface SpContext {

    SelfDescribingJson toSelfDescribingJson(Context context);
}
