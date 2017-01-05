package com.fieldnation.analytics.contexts;

import android.content.Context;

import com.fieldnation.BuildConfig;
import com.fieldnation.R;
import com.fieldnation.fnanalytics.EventContext;
import com.fieldnation.fnjson.JsonObject;
import com.fieldnation.fnjson.Serializer;
import com.fieldnation.fnjson.Unserializer;
import com.fieldnation.fnjson.annotations.Json;
import com.fieldnation.fnlog.Log;
import com.snowplowanalytics.snowplow.tracker.payload.SelfDescribingJson;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by mc on 1/4/17.
 */

public class SpScreenDisplayUiContext implements EventContext, SpContext {
    public static final String TAG = "SpScreenDisplayUiContext";

    @Json
    public String tag = TAG;
    @Json
    public String page;

    public SpScreenDisplayUiContext() {
    }

    public SpScreenDisplayUiContext(Builder builder) {
        this.page = builder.page;
    }


    @Override
    public SelfDescribingJson toSelfDescribingJson(Context context) {
        Map<String, Object> dataMap = new HashMap<>();
        dataMap.put("interface", "android");
        dataMap.put("version", (BuildConfig.VERSION_NAME + "-" + BuildConfig.BUILD_FLAVOR_NAME).trim());
        dataMap.put("page", "na");

        if (page != null)
            dataMap.put("page", page);

        return new SelfDescribingJson(context.getString(R.string.sp_ui_context_schema_uri), dataMap);
    }

    public JsonObject toJson() {
        try {
            return Serializer.serializeObject(this);
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }
        return null;
    }

    public static SpScreenDisplayUiContext fromJson(JsonObject object) {
        try {
            return Unserializer.unserializeObject(SpScreenDisplayUiContext.class, object);
        } catch (Exception e) {
            Log.v(TAG, e);
        }
        return null;
    }

    public static class Builder {
        private String page;

        public Builder() {
        }

        public SpScreenDisplayUiContext build() {
            return new SpScreenDisplayUiContext(this);
        }

        public Builder page(String page) {
            this.page = page;
            return this;
        }
    }
}
