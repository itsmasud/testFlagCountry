package com.fieldnation.analytics.contexts;

import android.content.Context;

import com.fieldnation.R;
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
 * Created by mc on 10/18/17.
 */

public class SpTrackingContext implements EventContext, SpContext {
    public static final String TAG = "SpTrackingContext";

    @Json
    public String tag = TAG;
    @Json
    public Integer workOrderId;
    @Json
    public UUIDGroup uuid;
    @Json
    public String stage;

    public SpTrackingContext() {
    }

    public SpTrackingContext(Builder builder) {
        this.workOrderId = builder.workOrderId;
        this.uuid = builder.uuid;
        this.stage = builder.stage;
    }

    @Override
    public SelfDescribingJson toSelfDescribingJson(Context context) {
        Map<String, Object> dataMap = new HashMap<>();

        dataMap.put("workOrderId", "na");

        if (workOrderId != null)
            dataMap.put("workOrderId", workOrderId);

        if (uuid != null) {
            if (!misc.isEmptyOrNull(uuid.uuid))
                dataMap.put("uuid", uuid.uuid);

            if (!misc.isEmptyOrNull(uuid.parentUUID))
                dataMap.put("parentUUID", uuid.parentUUID);
        }

        if (!misc.isEmptyOrNull(stage))
            dataMap.put("stage", stage);

        return new SelfDescribingJson(context.getString(R.string.sp_work_order_context_schema_uri), dataMap);
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

    public static SpTrackingContext fromJson(JsonObject object) {
        try {
            return Unserializer.unserializeObject(SpTrackingContext.class, object);
        } catch (Exception e) {
            Log.v(TAG, e);
        }
        return null;
    }

    public static class Builder {
        private Integer workOrderId;
        private UUIDGroup uuid;
        private String stage;

        public Builder() {
        }

        public SpTrackingContext build() {
            return new SpTrackingContext(this);
        }

        public Builder workOrderId(Integer workOrderId) {
            this.workOrderId = workOrderId;
            return this;
        }

        public Builder uuid(UUIDGroup uuid) {
            this.uuid = uuid;
            return this;
        }

        public Builder stage(String stage) {
            this.stage = stage;
            return this;
        }
    }
}
