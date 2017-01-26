package com.fieldnation.data.bv2.model;
import android.os.Parcel;
import android.os.Parcelable;

import com.fieldnation.fnjson.JsonObject;
import com.fieldnation.fnjson.Serializer;
import com.fieldnation.fnjson.Unserializer;
import com.fieldnation.fnjson.annotations.Json;
import com.fieldnation.fnlog.Log;

/**
 * Created by dmgen from swagger on 1/26/17.
 */

public class UpdateModelMetadataData implements Parcelable {
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

    public void setRoute(String route) {
        _route = route;
    }

    public String getRoute() {
        return _route;
    }

    public UpdateModelMetadataData route(String route) {
        _route = route;
        return this;
    }

    public void setRole(String role) {
        _role = role;
    }

    public String getRole() {
        return _role;
    }

    public UpdateModelMetadataData role(String role) {
        _role = role;
        return this;
    }

    public void setCompanyId(Integer companyId) {
        _companyId = companyId;
    }

    public Integer getCompanyId() {
        return _companyId;
    }

    public UpdateModelMetadataData companyId(Integer companyId) {
        _companyId = companyId;
        return this;
    }

    public void setUserId(Integer userId) {
        _userId = userId;
    }

    public Integer getUserId() {
        return _userId;
    }

    public UpdateModelMetadataData userId(Integer userId) {
        _userId = userId;
        return this;
    }

    public void setGroupId(Integer groupId) {
        _groupId = groupId;
    }

    public Integer getGroupId() {
        return _groupId;
    }

    public UpdateModelMetadataData groupId(Integer groupId) {
        _groupId = groupId;
        return this;
    }

    public void setWorkOrderId(Integer workOrderId) {
        _workOrderId = workOrderId;
    }

    public Integer getWorkOrderId() {
        return _workOrderId;
    }

    public UpdateModelMetadataData workOrderId(Integer workOrderId) {
        _workOrderId = workOrderId;
        return this;
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

    /*-*********************************************-*/
    /*-			Parcelable Implementation           -*/
    /*-*********************************************-*/
    public static final Parcelable.Creator<UpdateModelMetadataData> CREATOR = new Parcelable.Creator<UpdateModelMetadataData>() {

        @Override
        public UpdateModelMetadataData createFromParcel(Parcel source) {
            try {
                return UpdateModelMetadataData.fromJson((JsonObject) source.readParcelable(JsonObject.class.getClassLoader()));
            } catch (Exception ex) {
                Log.v(TAG, ex);
                return null;
            }
        }

        @Override
        public UpdateModelMetadataData[] newArray(int size) {
            return new UpdateModelMetadataData[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(toJson(), flags);
    }
}
