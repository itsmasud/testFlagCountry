package com.fieldnation.analytics.contexts;

import android.content.Context;

import com.fieldnation.MonotonicNumber;
import com.fieldnation.R;
import com.fieldnation.analytics.SnowplowWrapper;
import com.fieldnation.analytics.trackers.UUIDGroup;
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

public class SpTracingContext implements EventContext, SpContext {
    public static final String TAG = "SpTracingContext";

    @Json
    public String tag = TAG;
    @Json
    public String name;
    @Json
    public String uuid;
    @Json
    public String parentUUID;
    @Json
    public int order;
    @Json
    public long timestamp;

    static {
        SnowplowWrapper.registerContext(TAG, SpTracingContext.class);
    }

    public SpTracingContext() {
    }

    public SpTracingContext(Builder builder) {
        this.name = builder.name;
        this.uuid = builder.uuid;
        this.parentUUID = builder.parentUUID;
        this.order = MonotonicNumber.next();
        this.timestamp = System.currentTimeMillis();

        Log.v(TAG, toString());
    }

    public SpTracingContext(String parentUUID, String uuid) {
        this.uuid = uuid;
        this.parentUUID = parentUUID;
        this.order = MonotonicNumber.next();
        this.timestamp = System.currentTimeMillis();

        Log.v(TAG, toString());
    }

    public SpTracingContext(UUIDGroup uuidGroup) {
        this.uuid = uuidGroup.uuid;
        this.parentUUID = uuidGroup.parentUUID;
        this.order = MonotonicNumber.next();
        this.timestamp = System.currentTimeMillis();

        Log.v(TAG, toString());
    }

    public SpTracingContext(String name, UUIDGroup uuidGroup) {
        this.uuid = uuidGroup.uuid;
        this.parentUUID = uuidGroup.parentUUID;
        this.name = name;
        this.order = MonotonicNumber.next();
        this.timestamp = System.currentTimeMillis();

        Log.v(TAG, toString());
    }

    @Override
    public SelfDescribingJson toSelfDescribingJson(Context context) {
        Map<String, Object> dataMap = new HashMap<>();

        dataMap.put("uuid", uuid);

        dataMap.put("timestamp", timestamp);
        dataMap.put("order", order);

        if (!misc.isEmptyOrNull(parentUUID))
            dataMap.put("parent_uuid", parentUUID);

        if (!misc.isEmptyOrNull(name))
            dataMap.put("name", name);

        return new SelfDescribingJson(context.getString(R.string.sp_tracing_context_schema_uri), dataMap);
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

    public static SpTracingContext fromJson(JsonObject object) {
        try {
            return Unserializer.unserializeObject(SpTracingContext.class, object);
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }
        return null;
    }

    @Override
    public String toString() {
        String str = "O:" + order + " ";

        if (!misc.isEmptyOrNull(name)) {
            str = name;
        }

        if (!misc.isEmptyOrNull(parentUUID) && parentUUID.length() > 12) {
            str += "P:" + parentUUID.substring(parentUUID.length() - 12) + " ";
        }

        if (!misc.isEmptyOrNull(uuid) && uuid.length() > 12) {
            str += "U:" + uuid.substring(uuid.length() - 12);
        }

        return str;
    }

    public static class Builder {
        private String name;
        private String uuid;
        private String parentUUID;

        public Builder() {
        }

        public SpTracingContext build() {
            return new SpTracingContext(this);
        }

        public Builder uuid(String uuid) {
            this.uuid = uuid;
            return this;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder parentUUID(String parentUUID) {
            this.parentUUID = parentUUID;
            return this;
        }

        public Builder uuidGroup(UUIDGroup group) {
            this.uuid = group.uuid;
            this.parentUUID = group.parentUUID;
            return this;
        }
    }

}
