package com.fieldnation.data.workorder;

import com.fieldnation.fnlog.Log;
import com.fieldnation.fnjson.JsonObject;
import com.fieldnation.fnjson.Serializer;
import com.fieldnation.fnjson.Unserializer;
import com.fieldnation.fnjson.annotations.Json;

public class IncreaseRequestInfo {
    private static final String TAG = "IncreaseRequestInfo";

    @Json(name = "denyReason")
    private Object _denyReason;
    @Json(name = "pay")
    private Pay _pay;
    @Json(name = "payTermDescription")
    private String _payTermDescription;
    @Json(name = "requestReason")
    private String _requestReason;
    @Json(name = "status")
    private Integer _status;
    @Json(name = "statusName")
    private String _statusName;

    public IncreaseRequestInfo() {
    }

    public Object getDenyReason() {
        return _denyReason;
    }

    public Pay getPay() {
        return _pay;
    }

    public String getPayTermDescription() {
        return _payTermDescription;
    }

    public String getRequestReason() {
        return _requestReason;
    }

    public Integer getStatus() {
        return _status;
    }

    public String getStatusName() {
        return _statusName;
    }

    public JsonObject toJson() {
        return toJson(this);
    }

    public static JsonObject toJson(IncreaseRequestInfo increaseRequestInfo) {
        try {
            return Serializer.serializeObject(increaseRequestInfo);
        } catch (Exception ex) {
            Log.v(TAG, ex);
            return null;
        }
    }

    public static IncreaseRequestInfo fromJson(JsonObject json) {
        try {
            return Unserializer.unserializeObject(IncreaseRequestInfo.class, json);
        } catch (Exception ex) {
            Log.v(TAG, ex);
            return null;
        }
    }
}
