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
    public int lineOfCode;
    @Json
    public String methodName;

    static {
        // FIXME uncomment to enable
        //SnowplowWrapper.registerContext(TAG, SpStackContext.class);
    }

    public SpStackContext() {
    }

    public SpStackContext(Builder builder) {
        this.clazz = builder.clazz;
        this.lineOfCode = builder.lineOfCode;
        this.methodName = builder.methodName;
    }

    @Override
    public SelfDescribingJson toSelfDescribingJson(Context context) {
        Map<String, Object> dataMap = new HashMap<>();

        dataMap.put("class", clazz);
        dataMap.put("lineOfCode", lineOfCode);
        dataMap.put("methodName", methodName);

        // FIXME might need default values

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

    public static class Builder {
        private String clazz;
        private int lineOfCode;
        private String methodName;

        public Builder() {
        }

        public SpStackContext build() {
            return new SpStackContext(this);
        }

        public Builder clazz(String className) {
            this.clazz = className;
            return this;
        }

        public Builder lineOfCode(int lineOfCode) {
            this.lineOfCode = lineOfCode;
            return this;
        }

        public Builder methodName(String methodName) {
            this.methodName = methodName;
            return this;
        }

        public Builder stackElement(StackTraceElement stackTraceElement) {
            this.clazz = stackTraceElement.getClassName();
            this.lineOfCode = stackTraceElement.getLineNumber();
            this.methodName = stackTraceElement.getMethodName();
            return this;
        }
    }
}
