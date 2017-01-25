package com.fieldnation.data.bv2.model;

import com.fieldnation.fnjson.JsonObject;
import com.fieldnation.fnjson.Serializer;
import com.fieldnation.fnjson.Unserializer;
import com.fieldnation.fnjson.annotations.Json;
import com.fieldnation.fnlog.Log;

public class UpdateModelMetadataData {
    private static final String TAG = "UpdateModelMetadataData";

    @Json(name = "route")
    private String _route;

    @Json(name = "role")
    private String _role;

    @Json(name = "company_id")
    private Integer _companyId;

    @Json(name = "user_id")
    private Integer _userId;

    @Json(name = "group_id")
    private Integer _groupId;

    @Json(name = "work_order_id")
    private Integer _workOrderId;

    public UpdateModelMetadataData() {
    }

    public String getRoute() {
        return _route;
    }

    public String getRole() {
        return _role;
    }

    public Integer getCompanyId() {
        return _companyId;
    }

    public Integer getUserId() {
        return _userId;
    }

    public Integer getGroupId() {
        return _groupId;
    }

    public Integer getWorkOrderId() {
        return _workOrderId;
    }

    /*-*****************************-*/
    /*-             Json            -*/
    /*-*****************************-*/
    public static UpdateModelMetadataData fromJson(JsonObject obj) {
        try {
            return Unserializer.unserializeObject(UpdateModelMetadataData.class, obj);
        } catch (Exception ex) {
            Log.v(TAG, TAG, ex);
            return null;
        }
    }

    public JsonObject toJson() {
        return toJson(this);
    }

    public static JsonObject toJson(UpdateModelMetadataData updateModelMetadataData) {
        try {
            return Serializer.serializeObject(updateModelMetadataData);
        } catch (Exception ex) {
            Log.v(TAG, TAG, ex);
            return null;
        }
    }
}
