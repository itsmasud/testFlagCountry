package com.fieldnation.analytics.contexts;

import android.content.Context;

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
        START, INFO, COMPLETE, FAIL,;
    }

    @Json
    public String tag = TAG;
    @Json
    public Integer code;
    @Json
    public Status status;
    @Json
    public String message;

    static {
        // TODO uncomment to enable
        //SnowplowWrapper.registerContext(TAG, SpStatusContext.class);
    }

    public SpStatusContext() {
    }

    public SpStatusContext(Builder builder) {
        this.code = builder.code;
        this.status = builder.status;
        this.message = builder.message;

        Log.v(TAG, toString());
    }

    @Override
    public SelfDescribingJson toSelfDescribingJson(Context context) {
        Map<String, Object> dataMap = new HashMap<>();

        dataMap.put("status", status.name());
        dataMap.put("message", message);

        if (code != null)
            dataMap.put("code", code);

        // TODO Schema needed
        return new SelfDescribingJson("TODO SCHEMA NEEDED", dataMap);
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

    public static class Builder {
        private int code;
        private Status status;
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

        public Builder status(Status status) {
            this.status = status;
            return this;
        }

        public Builder message(String message) {
            this.message = message;
            return this;
        }
    }
}
