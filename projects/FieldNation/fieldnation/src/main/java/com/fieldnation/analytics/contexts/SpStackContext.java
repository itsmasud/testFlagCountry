package com.fieldnation.analytics.contexts;

import android.content.Context;
import android.os.Build;

import com.fieldnation.analytics.SnowplowWrapper;
import com.fieldnation.fnanalytics.EventContext;
import com.fieldnation.fnjson.JsonObject;
import com.fieldnation.fnjson.Serializer;
import com.fieldnation.fnjson.Unserializer;
import com.fieldnation.fnjson.annotations.Json;
import com.fieldnation.fnlog.Log;
import com.fieldnation.fntools.misc;
import com.snowplowanalytics.snowplow.tracker.payload.SelfDescribingJson;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by mc on 10/27/17.
 */

public class SpStackContext implements EventContext, SpContext {
    public static final String TAG = "SpStackContext";

    @Json
    public String tag = TAG;
    @Json(name = "class")
    public String clazz;
    @Json
    public String method;
    @Json
    public int line;
    @Json
    public String trace;

    static {
        // FIXME uncomment to enable
        //SnowplowWrapper.registerContext(TAG, SpStackContext.class);
    }

    public SpStackContext() {
    }

    public SpStackContext(Builder builder) {
        this.clazz = builder.clazz;
        this.line = builder.line;
        this.method = builder.method;
        this.trace = builder.trace;

        Log.v(TAG, toString());
    }

    @Override
    public SelfDescribingJson toSelfDescribingJson(Context context) {
        Map<String, Object> dataMap = new HashMap<>();

        dataMap.put("class", clazz);
        dataMap.put("line", line);
        dataMap.put("method", method);

        if (!misc.isEmptyOrNull(trace))
            dataMap.put("trace", trace);

        // FIXME need schema
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

    public static SpStackContext fromJson(JsonObject object) {
        try {
            return Unserializer.unserializeObject(SpStackContext.class, object);
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }
        return null;
    }

    @Override
    public String toString() {
        String str = "";

        str += clazz + "." + method + ":" + line;

        if (!misc.isEmptyOrNull(trace))
            str += "\n" + trace;

        return str;
    }

    public static class Builder {
        private String clazz;
        private int line;
        private String method;
        private String trace;

        public Builder() {
        }

        public SpStackContext build() {
            return new SpStackContext(this);
        }

        public Builder clazz(String className) {
            this.clazz = className;
            return this;
        }

        public Builder line(int line) {
            this.line = line;
            return this;
        }

        public Builder method(String method) {
            this.method = method;
            return this;
        }

        public Builder stackElement(StackTraceElement stackTraceElement) {
            this.clazz = stackTraceElement.getClassName();
            this.line = stackTraceElement.getLineNumber();
            this.method = stackTraceElement.getMethodName();
            return this;
        }

        public Builder trace(String trace) {
            this.trace = trace;
            return this;
        }
    }
}
