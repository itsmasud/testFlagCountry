package com.fieldnation.analytics;

import android.content.Context;

import com.fieldnation.R;
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
 * Created by Michael on 9/15/2016.
 */
public class SpWorkOrderContext implements EventContext, SpContext {
    public static final String TAG = "SpWorkOrderContext";

    @Json
    public String tag = TAG;
    @Json
    public Long workOrderId;

    public SpWorkOrderContext() {
    }

    public SpWorkOrderContext(Builder builder) {
        this.workOrderId = builder.workOrderId;
    }

    @Override
    public SelfDescribingJson toSelfDescribingJson(Context context) {
        Map<String, Object> dataMap = new HashMap<>();

        dataMap.put("workOrderId", "na");

        if (workOrderId != null)
            dataMap.put("workOrderId", workOrderId);

        return new SelfDescribingJson(context.getString(R.string.sp_work_order_context_schema_uri), dataMap);
    }

    @Override
    public JsonObject toJson() {
        try {
            return Serializer.serializeObject(this);
        } catch (Exception e) {
            Log.v(TAG, e);
        }
        return null;
    }

    public static SpWorkOrderContext fromJson(JsonObject object) {
        try {
            return Unserializer.unserializeObject(SpWorkOrderContext.class, object);
        } catch (Exception e) {
            Log.v(TAG, e);
        }
        return null;
    }

    public static class Builder {
        private Long workOrderId;

        public Builder() {
        }

        public SpWorkOrderContext build() {
            return new SpWorkOrderContext(this);
        }

        public Builder workOrderId(Long workOrderId) {
            this.workOrderId = workOrderId;
            return this;
        }
    }

}
