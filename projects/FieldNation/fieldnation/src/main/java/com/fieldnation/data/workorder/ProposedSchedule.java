package com.fieldnation.data.workorder;

import com.fieldnation.fnjson.JsonObject;
import com.fieldnation.fnjson.Serializer;
import com.fieldnation.fnjson.Unserializer;
import com.fieldnation.fnjson.annotations.Json;
import com.fieldnation.fnlog.Log;

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
            return Unserializer.unserializeObject(ProposedSchedule.class, json);
        } catch (Exception ex) {
            Log.v(TAG, ex);
            return null;
        }
    }
}
