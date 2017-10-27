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

public class SpFileContext implements EventContext, SpContext {
    public static final String TAG = "SpFileContext";

    @Json
    public String tag = TAG;
    @Json
    public String name;
    @Json
    public Integer size;
    @Json
    public Long create;
    @Json
    public Integer id;

    static {
        // FIXME uncomment to enable
        //SnowplowWrapper.registerContext(TAG, SpFileContext.class);
    }

    public SpFileContext() {
    }

    public SpFileContext(Builder builder) {
        this.name = builder.name;
        this.size = builder.size;
        this.create = builder.create;
        this.id = builder.id;
    }

    @Override
    public SelfDescribingJson toSelfDescribingJson(Context context) {
        Map<String, Object> dataMap = new HashMap<>();

        dataMap.put("name", name);
        dataMap.put("size", size);
        dataMap.put("create", create);
        dataMap.put("id", id);

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

    public static SpFileContext fromJson(JsonObject object) {
        try {
            return Unserializer.unserializeObject(SpFileContext.class, object);
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }
        return null;
    }

    public static class Builder {
        private String name;
        private Integer size;
        private Long create;
        private Integer id;

        public Builder() {
        }

        public SpFileContext build() {
            return new SpFileContext(this);
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder size(Integer size) {
            this.size = size;
            return this;
        }

        public Builder create(Long create) {
            this.create = create;
            return this;
        }

        public Builder id(Integer id) {
            this.id = id;
            return this;
        }
    }
}
