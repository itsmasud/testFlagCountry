package com.fieldnation.analytics.contexts;

import android.content.Context;

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
    public String uuid;
    @Json
    public String parent;

    static {
        // FIXME enable
        // SnowplowWrapper.registerContext(TAG, SpTracingContext.class);
    }

    public SpTracingContext() {
    }

    public SpTracingContext(Builder builder) {
        this.uuid = builder.uuid;
        this.parent = builder.parent;
    }

    @Override
    public SelfDescribingJson toSelfDescribingJson(Context context) {
        Map<String, Object> dataMap = new HashMap<>();

        dataMap.put("uuid", "");
        dataMap.put("parent", "");

        if (!misc.isEmptyOrNull(uuid))
            dataMap.put("uuid", uuid);

        if (!misc.isEmptyOrNull(parent))
            dataMap.put("parent", parent);

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

    public static SpTracingContext fromJson(JsonObject object) {
        try {
            return Unserializer.unserializeObject(SpTracingContext.class, object);
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }
        return null;
    }

    public static class Builder {
        private String uuid;
        private String parent;

        public Builder() {
        }

        public SpTracingContext build() {
            return new SpTracingContext(this);
        }

        public Builder uuid(String uuid) {
            this.uuid = uuid;
            return this;
        }

        public Builder parent(String parent) {
            this.parent = parent;
            return this;
        }

        public Builder uuidGroup(UUIDGroup group) {
            this.uuid = group.uuid;
            this.parent = group.parentUUID;
            return this;
        }
    }

}
