package com.fieldnation.data.bv2.model;

import com.fieldnation.fnjson.JsonObject;
import com.fieldnation.fnjson.Serializer;
import com.fieldnation.fnjson.Unserializer;
import com.fieldnation.fnjson.annotations.Json;
import com.fieldnation.fnlog.Log;

public class TechnicianW9Status {
    private static final String TAG = "TechnicianW9Status";

    @Json(name = "status_name")
    private String statusName = null;

    @Json(name = "technician_w9_status_id")
    private Integer technicianW9StatusId = null;

    public TechnicianW9Status() {
    }

    public String getStatusName() {
        return statusName;
    }

    public Integer getTechnicianW9StatusId() {
        return technicianW9StatusId;
    }

    /*-*****************************-*/
    /*-             Json            -*/
    /*-*****************************-*/
    public static TechnicianW9Status fromJson(JsonObject obj) {
        try {
            return Unserializer.unserializeObject(TechnicianW9Status.class, obj);
        } catch (Exception ex) {
            Log.v(TAG, ex);
            return null;
        }
    }

    public JsonObject toJson() {
        return toJson(this);
    }

    public static JsonObject toJson(TechnicianW9Status technicianW9Status) {
        try {
            return Serializer.serializeObject(technicianW9Status);
        } catch (Exception ex) {
            Log.v(TAG, ex);
            return null;
        }
    }
}