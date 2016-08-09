package com.fieldnation.data.workorder;

import com.fieldnation.fnlog.Log;
import com.fieldnation.json.JsonObject;
import com.fieldnation.json.Serializer;
import com.fieldnation.json.Unserializer;
import com.fieldnation.json.annotations.Json;

/**
 * Created by Shoaib on 03/31/2016.
 */

public class KeyEvents {
    private static final String TAG = "KeyEvents";

    @Json(name = "createdTime")
    private String _createdTime;
    @Json(name = "publishedTime")
    private String _publishedTime;
    @Json(name = "routedTime")
    private String _routedTime;
    @Json(name = "assignedTime")
    private String _assignedTime;
    @Json(name = "workDoneTime")
    private String _workDoneTime;
    @Json(name = "approvedTime")
    private String _approvedTime;
    @Json(name = "paidTime")
    private String _paidTime;
    @Json(name = "canceledTime")
    private String _canceledTime;
    @Json(name = "workDoneTimeISO")
    private String _workDoneTimeISO;

    public KeyEvents() {
    }

    public String getCreatedTime() {
        return _createdTime;
    }

    public String getPublishedTime() {
        return _publishedTime;
    }

    public String getRoutedTime() {
        return _routedTime;
    }

    public String getAssignedTime() {
        return _assignedTime;
    }

    public String getWorkDoneTime() {
        return _workDoneTime;
    }

    public String getApprovedTime() {
        return _approvedTime;
    }

    public String getPaidTime() {
        return _paidTime;
    }

    public String getCanceledTime() {
        return _canceledTime;
    }

    public String getWorkDoneTimeISO() {
        return _workDoneTimeISO;
    }

    public JsonObject toJson() {
        return toJson(this);
    }

    public static JsonObject toJson(KeyEvents buyerRatingInfo) {
        try {
            return Serializer.serializeObject(buyerRatingInfo);
        } catch (Exception ex) {
            Log.v(TAG, ex);
            return null;
        }
    }

    public static KeyEvents fromJson(JsonObject json) {
        try {
            return Unserializer.unserializeObject(KeyEvents.class, json);
        } catch (Exception ex) {
            Log.v(TAG, ex);
            return null;
        }
    }

}
