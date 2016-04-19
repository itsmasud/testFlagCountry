package com.fieldnation.data.workorder;

import com.fieldnation.Log;
import com.fieldnation.json.JsonObject;
import com.fieldnation.json.Serializer;
import com.fieldnation.json.Unserializer;
import com.fieldnation.json.annotations.Json;

public class ScheduleRange {
    private static final String TAG = "ScheduleRange";

    @Json(name = "endDate")
    private String _endDate;
    @Json(name = "startDate")
    private String _startDate;

    public ScheduleRange() {
    }

    public String getEndDate() {
        return _endDate;
    }

    public String getStartDate() {
        return _startDate;
    }

    public JsonObject toJson() {
        return toJson(this);
    }

    public static JsonObject toJson(ScheduleRange scheduleRange) {
        try {
            return Serializer.serializeObject(scheduleRange);
        } catch (Exception ex) {
            Log.v(TAG, ex);
            return null;
        }
    }

    public static ScheduleRange fromJson(JsonObject json) {
        try {
            return Unserializer.unserializeObject(ScheduleRange.class, json);
        } catch (Exception ex) {
            Log.v(TAG, ex);
            return null;
        }
    }

}
