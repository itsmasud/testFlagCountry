package com.fieldnation.data.accounting;

import com.fieldnation.Log;
import com.fieldnation.json.JsonObject;
import com.fieldnation.json.Serializer;
import com.fieldnation.json.annotations.Json;

public class Fee {
    private static final String TAG = "Fee";

    @Json(name = "amount")
    private Double _amount;
    @Json(name = "clientName")
    private String _clientName;
    @Json(name = "description")
    private String _description;
    @Json(name = "endTime")
    private String _endTime;
    @Json(name = "startTime")
    private String _startTime;
    @Json(name = "title")
    private String _title;
    @Json(name = "workorderId")
    private Long _workorderId;

    public Fee() {
    }

    public Double getAmount() {
        return _amount;
    }

    public String getClientName() {
        return _clientName;
    }

    public String getDescription() {
        return _description;
    }

    public String getEndTime() {
        return _endTime;
    }

    public String getStartTime() {
        return _startTime;
    }

    public String getTitle() {
        return _title;
    }

    public Long getWorkorderId() {
        return _workorderId;
    }

    public JsonObject toJson() {
        return toJson(this);
    }

    public static JsonObject toJson(Fee fee) {
        try {
            return Serializer.serializeObject(fee);
        } catch (Exception ex) {
            Log.v(TAG, ex);
            return null;
        }
    }

    public static Fee fromJson(JsonObject json) {
        try {
            return Serializer.unserializeObject(Fee.class, json);
        } catch (Exception ex) {
            Log.v(TAG, ex);
            return null;
        }
    }

}
