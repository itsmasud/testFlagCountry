package com.fieldnation.data.workorder;

import com.fieldnation.Log;
import com.fieldnation.json.JsonObject;
import com.fieldnation.json.Serializer;
import com.fieldnation.json.Unserializer;
import com.fieldnation.json.annotations.Json;

public class Bundle {
    private static final String TAG = "Bundle";

    @Json(name = "averageDistance")
    private Double _averageDistance;
    @Json(name = "bundleId")
    private Integer _bundleId;
    @Json(name = "scheduleRange")
    private ScheduleRange _scheduleRange;
    @Json(name = "workorder")
    private Workorder[] _workorder;

    public Bundle() {
    }

    public Double getAverageDistance() {
        return _averageDistance;
    }

    public Integer getBundleId() {
        return _bundleId;
    }

    public ScheduleRange getScheduleRange() {
        return _scheduleRange;
    }

    public Workorder[] getWorkorder() {
        return _workorder;
    }

    public JsonObject toJson() {
        return toJson(this);
    }

    public static JsonObject toJson(Bundle bundle) {
        try {
            return Serializer.serializeObject(bundle);
        } catch (Exception ex) {
            Log.v(TAG, ex);
            return null;
        }
    }

    public static Bundle fromJson(JsonObject json) {
        try {
            return Unserializer.unserializeObject(Bundle.class, json);
        } catch (Exception ex) {
            Log.v(TAG, ex);
            return null;
        }
    }

}
