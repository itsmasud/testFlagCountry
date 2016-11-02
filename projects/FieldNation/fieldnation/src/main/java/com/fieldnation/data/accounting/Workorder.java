package com.fieldnation.data.accounting;

import com.fieldnation.fnjson.JsonObject;
import com.fieldnation.fnjson.Serializer;
import com.fieldnation.fnjson.Unserializer;
import com.fieldnation.fnjson.annotations.Json;
import com.fieldnation.fnlog.Log;

public class Workorder {
    private static final String TAG = "Workorder";

    @Json(name = "amount")
    private Double _amount;
    @Json(name = "clientName")
    private String _clientName;
    @Json(name = "endTime")
    private String _endTime;
    @Json(name = "fullWorkDescription")
    private String _fullWorkDescription;
    @Json(name = "startTime")
    private String _startTime;
    @Json(name = "title")
    private String _title;
    @Json(name = "workorderId")
    private long _workorderId;

    public Workorder() {
    }

    public Double getAmount() {
        return _amount;
    }

    public String getClientName() {
        return _clientName;
    }

    public String getEndTime() {
        return _endTime;
    }

    public String getFullWorkDescription() {
        return _fullWorkDescription;
    }

    public String getStartTime() {
        return _startTime;
    }

    public String getTitle() {
        return _title;
    }

    public long getWorkorderId() {
        return _workorderId;
    }

    public JsonObject toJson() {
        return toJson(this);
    }

    public static JsonObject toJson(Workorder workorder) {
        try {
            return Serializer.serializeObject(workorder);
        } catch (Exception ex) {
            Log.v(TAG, ex);
            return null;
        }
    }

    public static Workorder fromJson(JsonObject json) {
        try {
            return Unserializer.unserializeObject(Workorder.class, json);
        } catch (Exception ex) {
            Log.v(TAG, ex);
            return null;
        }
    }

}
