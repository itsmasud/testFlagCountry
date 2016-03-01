package com.fieldnation.data.workorder;

import com.fieldnation.Log;
import com.fieldnation.json.JsonObject;
import com.fieldnation.json.Serializer;
import com.fieldnation.json.annotations.Json;

public class ProposedSchedule {
    private static final String TAG = "ProposedSchedule";

    @Json(name = "endTime")
    private String _endTime;
    @Json(name = "endTimeStamp")
    private Integer _endTimeStamp;
    @Json(name = "startTime")
    private String _startTime;
    @Json(name = "startTimeStamp")
    private Integer _startTimeStamp;

    public ProposedSchedule() {
    }

    public String getEndTime() {
        return _endTime;
    }

    public Integer getEndTimeStamp() {
        return _endTimeStamp;
    }

    public String getStartTime() {
        return _startTime;
    }

    public Integer getStartTimeStamp() {
        return _startTimeStamp;
    }

    public JsonObject toJson() {
        return toJson(this);
    }

    public static JsonObject toJson(ProposedSchedule proposedSchedule) {
        try {
            return Serializer.serializeObject(proposedSchedule);
        } catch (Exception ex) {
            Log.v(TAG, ex);
            return null;
        }
    }

    public static ProposedSchedule fromJson(JsonObject json) {
        try {
            return Serializer.unserializeObject(ProposedSchedule.class, json);
        } catch (Exception ex) {
            Log.v(TAG, ex);
            return null;
        }
    }
}
