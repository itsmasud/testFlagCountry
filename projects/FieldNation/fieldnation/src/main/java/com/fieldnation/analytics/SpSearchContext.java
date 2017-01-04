package com.fieldnation.analytics;

import android.content.Context;

import com.fieldnation.R;
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

public class SpSearchContext implements SpContext {
    public static final String TAG = "SpSearchContext";


    @Json
    public String tag = TAG;
    @Json
    public String name;
    @Json
    public String value;

    public SpSearchContext() {
    }

    public SpSearchContext(Builder builder) {
        this.name = builder.name;
        this.value = builder.value;
    }

    @Override
    public SelfDescribingJson toSelfDescribingJson(Context context) {
        Map<String, Object> dataMap = new HashMap<>();

        dataMap.put("parameter_name", "na");
        dataMap.put("parameter_value", "na");

        if (name != null)
            dataMap.put("parameter_name", name);

        if (value != null)
            dataMap.put("parameter_value", value);


        return new SelfDescribingJson(context.getString(R.string.sp_search_context_schema_uri), dataMap);
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
        private String name;
        private String value;

        public Builder() {
        }

        public SpSearchContext builder() {
            return new SpSearchContext(this);
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder value(String value) {
            this.value = value;
            return this;
        }
    }

}
