package com.fieldnation.data.bv2.model;

import com.fieldnation.fnjson.JsonObject;
import com.fieldnation.fnjson.Serializer;
import com.fieldnation.fnjson.Unserializer;
import com.fieldnation.fnjson.annotations.Json;
import com.fieldnation.fnlog.Log;

public class WorkOrders {
    private static final String TAG = "WorkOrders";

    @Json(name = "metadata")
    private ListEnvelope metadata = null;

    @Json(name = "results")
    private WorkOrder[] results;

    public WorkOrders() {
    }

    public ListEnvelope getMetadata() {
        return metadata;
    }

    public WorkOrder[] getResults() {
        return results;
    }

    /*-*****************************-*/
    /*-             Json            -*/
    /*-*****************************-*/
    public static WorkOrders fromJson(JsonObject obj) {
        try {
            return Unserializer.unserializeObject(WorkOrders.class, obj);
        } catch (Exception ex) {
            Log.v(TAG, ex);
            return null;
        }
    }

    public JsonObject toJson() {
        return toJson(this);
    }

    public static JsonObject toJson(WorkOrders workOrders) {
        try {
            return Serializer.serializeObject(workOrders);
        } catch (Exception ex) {
            Log.v(TAG, ex);
            return null;
        }
    }
}