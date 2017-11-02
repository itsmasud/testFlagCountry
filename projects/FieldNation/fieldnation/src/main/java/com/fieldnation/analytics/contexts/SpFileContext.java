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

public class SpFileContext implements EventContext, SpContext {
    public static final String TAG = "SpFileContext";

    @Json
    public String tag = TAG;
    @Json
    public Integer id;
    @Json
    public String name;
    @Json
    public Integer size;
    @Json
    public Long createdAt;

    static {
        SnowplowWrapper.registerContext(TAG, SpFileContext.class);
    }

    public SpFileContext() {
    }

    public SpFileContext(Builder builder) {
        this.name = builder.name;
        this.size = builder.size;
        this.createdAt = builder.createdAt;
        this.id = builder.id;
        Log.v(TAG, toString());
    }

    @Override
    public SelfDescribingJson toSelfDescribingJson(Context context) {
        Map<String, Object> dataMap = new HashMap<>();

        if (id != null)
            dataMap.put("id", id);

        if (createdAt != null)
            dataMap.put("created_at", createdAt);


        dataMap.put("name", name);
        dataMap.put("size", size);

        return new SelfDescribingJson(context.getString(R.string.sp_file_context_schema_uri), dataMap);
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

    @Override
    public String toString() {
        String str = "";
        str += "s:" + size + " c:" + createdAt + " n:" + name;
        return str;
    }

    public static class Builder {
        private String name;
        private Integer size;
        private Long createdAt;
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

        public Builder createdAt(Long createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public Builder id(Integer id) {
            this.id = id;
            return this;
        }
    }
}
