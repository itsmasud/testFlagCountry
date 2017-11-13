package com.fieldnation.analytics.contexts;

import android.content.Context;

import com.fieldnation.R;
import com.fieldnation.analytics.SnowplowWrapper;
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
 * Created by mc on 10/27/17.
 */

public class SpStatusContext implements EventContext, SpContext {
    public static final String TAG = "SpStatusContext";

    public enum Status {
        @Json(name = "START")START,
        @Json(name = "INFO")INFO,
        @Json(name = "COMPLETE")COMPLETE,
        @Json(name = "FAIL")FAIL,;
    }

    @Json
    public String tag = TAG;
    @Json
    public Integer code;
    @Json
    public Status name;
    @Json
    public String message;

    static {
        SnowplowWrapper.registerContext(TAG, SpStatusContext.class);
    }

    public SpStatusContext() {
    }

    public SpStatusContext(Builder builder) {
        this.code = builder.code;
        this.name = builder.name;
        this.message = builder.message;

        Log.v(TAG, toString());
    }

    public SpStatusContext(Status name) {
        this.name = name;

        Log.v(TAG, toString());
    }

    public SpStatusContext(Status name, String message) {
        this.name = name;
        this.message = message;

        Log.v(TAG, toString());
    }

    @Override
    public SelfDescribingJson toSelfDescribingJson(Context context) {
        Map<String, Object> dataMap = new HashMap<>();

        dataMap.put("name", name.name());
        dataMap.put("message", message);

        if (code != null)
            dataMap.put("code", code);

        return new SelfDescribingJson(context.getString(R.string.sp_status_context_schema_uri), dataMap);
    }

    @Override
    public JsonObject toJson() {
        try {
            return Serializer.serializeObject(this);
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }
        return null;
    }

    public static SpStatusContext fromJson(JsonObject object) {
        try {
            return Unserializer.unserializeObject(SpStatusContext.class, object);
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }
        return null;
    }

    @Override
    public String toString() {
        String str = "";

        if (code != null)
            str += "c: " + code + " ";

        str += "S: " + name.name() + " M: " + message;

        return str;
    }

    public static class Builder {
        private int code;
        private Status name;
        private String message;

        public Builder() {
        }

        public SpStatusContext build() {
            return new SpStatusContext(this);
        }

        public Builder code(int code) {
            this.code = code;
            return this;
        }

        public Builder status(Status name) {
            this.name = name;
            return this;
        }

        public Builder message(String message) {
            this.message = message;
            return this;
        }
    }
}
