package com.fieldnation.data.workorder;

import com.fieldnation.fnjson.JsonObject;
import com.fieldnation.fnjson.Serializer;
import com.fieldnation.fnjson.Unserializer;
import com.fieldnation.fnjson.annotations.Json;
import com.fieldnation.fnlog.Log;

public class Status {
    private static final String TAG = "Status";

    @Json(name = "colorIntent")
    private String _colorIntent;
    @Json(name = "status")
    private String _status;
    @Json(name = "subStatus")
    private String _subStatus;

    public Status() {
    }

    public String getColorIntent() {
        return _colorIntent;
    }

    public String getStatus() {
        return _status;
    }

    public String getSubStatus() {
        return _subStatus;
    }

    public JsonObject toJson() {
        return toJson(this);
    }

    public static JsonObject toJson(Status status) {
        try {
            return Serializer.serializeObject(status);
        } catch (Exception ex) {
            Log.v(TAG, ex);
            return null;
        }
    }

    public static Status fromJson(JsonObject json) {
        try {
            return Unserializer.unserializeObject(Status.class, json);
        } catch (Exception ex) {
            Log.v(TAG, ex);
            return null;
        }
    }

	/*-*************************************************-*/
    /*-				Human Generated Code				-*/
    /*-*************************************************-*/

    public WorkorderSubstatus getWorkorderSubstatus() {
        return WorkorderSubstatus.fromValue(_subStatus);
    }

    public WorkorderStatus getWorkorderStatus() {
        return WorkorderStatus.fromValue(_status);
    }

    public StatusIntent getStatusIntent() {
        return StatusIntent.fromString(_colorIntent);
    }
}
