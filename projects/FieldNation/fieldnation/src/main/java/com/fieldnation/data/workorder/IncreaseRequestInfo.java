package com.fieldnation.data.workorder;

import com.fieldnation.Log;
import com.fieldnation.json.JsonObject;
import com.fieldnation.json.Serializer;
import com.fieldnation.json.annotations.Json;

public class IncreaseRequestInfo {
    private static final String TAG = "IncreaseRequestInfo";

    @Json(name = "createdTime")
    private String _createdTime;
    @Json(name = "denyReason")
    private Object _denyReason;
    @Json(name = "flagId")
    private Integer _flagId;
    @Json(name = "pay")
    private Pay _pay;
    @Json(name = "payRateDiff")
    private Integer _payRateDiff;
    @Json(name = "payTermDescription")
    private String _payTermDescription;
    @Json(name = "payTermId")
    private Integer _payTermId;
    @Json(name = "requestReason")
    private String _requestReason;
    @Json(name = "status")
    private Integer _status;
    @Json(name = "statusName")
    private String _statusName;
    @Json(name = "techFullName")
    private String _techFullName;
    @Json(name = "techUserId")
    private Integer _techUserId;
    @Json(name = "workorderId")
    private Integer _workorderId;
    @Json(name = "workorderIncreaseId")
    private Integer _workorderIncreaseId;

    public IncreaseRequestInfo() {
    }

    public String getCreatedTime() {
        return _createdTime;
    }

    public Object getDenyReason() {
        return _denyReason;
    }

    public Integer getFlagId() {
        return _flagId;
    }

    public Pay getPay() {
        return _pay;
    }

    public Integer getPayRateDiff() {
        return _payRateDiff;
    }

    public String getPayTermDescription() {
        return _payTermDescription;
    }

    public Integer getPayTermId() {
        return _payTermId;
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

    public String getTechFullName() {
        return _techFullName;
    }

    public Integer getTechUserId() {
        return _techUserId;
    }

    public Integer getWorkorderId() {
        return _workorderId;
    }

    public Integer getWorkorderIncreaseId() {
        return _workorderIncreaseId;
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
            return Serializer.unserializeObject(IncreaseRequestInfo.class, json);
        } catch (Exception ex) {
            Log.v(TAG, ex);
            return null;
        }
    }
}
