package com.fieldnation.data.bv2.model;

import com.fieldnation.fnjson.JsonObject;
import com.fieldnation.fnjson.Serializer;
import com.fieldnation.fnjson.Unserializer;
import com.fieldnation.fnjson.annotations.Json;
import com.fieldnation.fnlog.Log;

public class technicianW9Status {
    private static final String TAG = "technicianW9Status";

    @Json(name = "status_name")
    private String _statusName;

    @Json(name = "technician_w9_status_id")
    private Integer _technicianW9StatusId;

    public technicianW9Status() {
    }

    public String getStatusName() {
        return _statusName;
    }

    public Integer getTechnicianW9StatusId() {
        return _technicianW9StatusId;
    }

    /*-*****************************-*/
    /*-             Json            -*/
    /*-*****************************-*/
    public static technicianW9Status fromJson(JsonObject obj) {
        try {
            return Unserializer.unserializeObject(technicianW9Status.class, obj);
        } catch (Exception ex) {
            Log.v(TAG, TAG, ex);
            return null;
        }
    }

    public JsonObject toJson() {
        return toJson(this);
    }

    public static JsonObject toJson(technicianW9Status technicianW9Status) {
        try {
            return Serializer.serializeObject(technicianW9Status);
        } catch (Exception ex) {
            Log.v(TAG, TAG, ex);
            return null;
        }
    }
}
