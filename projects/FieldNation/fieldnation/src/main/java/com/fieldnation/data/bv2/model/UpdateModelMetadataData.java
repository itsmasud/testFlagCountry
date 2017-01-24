package com.fieldnation.data.bv2.model;

import com.fieldnation.fnjson.JsonObject;
import com.fieldnation.fnjson.Serializer;
import com.fieldnation.fnjson.Unserializer;
import com.fieldnation.fnjson.annotations.Json;
import com.fieldnation.fnlog.Log;

public class UpdateModelMetadataData {
    private static final String TAG = "UpdateModelMetadataData";

    @Json(name = "route")
    private String route;

    @Json(name = "role")
    private String role;

    @Json(name = "company_id")
    private Integer companyId;

    @Json(name = "user_id")
    private Integer userId;

    @Json(name = "group_id")
    private Integer groupId;

    @Json(name = "work_order_id")
    private Integer workOrderId;

    public UpdateModelMetadataData() {
    }

    public String getRoute() {
        return route;
    }

    public String getRole() {
        return role;
    }

    public Integer getCompanyId() {
        return companyId;
    }

    public Integer getUserId() {
        return userId;
    }

    public Integer getGroupId() {
        return groupId;
    }

    public Integer getWorkOrderId() {
        return workOrderId;
    }

    /*-*****************************-*/
    /*-             Json            -*/
    /*-*****************************-*/
    public static UpdateModelMetadataData fromJson(JsonObject obj) {
        try {
            return Unserializer.unserializeObject(UpdateModelMetadataData.class, obj);
        } catch (Exception ex) {
            Log.v(TAG, ex);
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
            Log.v(TAG, ex);
            return null;
        }
    }
}
