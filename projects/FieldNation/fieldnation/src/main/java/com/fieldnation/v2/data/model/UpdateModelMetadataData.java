package com.fieldnation.v2.data.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.fieldnation.fnjson.JsonArray;
import com.fieldnation.fnjson.JsonObject;
import com.fieldnation.fnjson.Serializer;
import com.fieldnation.fnjson.Unserializer;
import com.fieldnation.fnjson.annotations.Json;
import com.fieldnation.fnjson.annotations.Source;
import com.fieldnation.fnlog.Log;

import java.text.ParseException;

/**
 * Created by dmgen from swagger.
 */

public class UpdateModelMetadataData implements Parcelable {
    private static final String TAG = "UpdateModelMetadataData";

    @Json(name = "company_id")
    private Integer _companyId;

    @Json(name = "group_id")
    private Integer _groupId;

    @Json(name = "role")
    private String _role;

    @Json(name = "route")
    private String _route;

    @Json(name = "user_id")
    private Integer _userId;

    @Json(name = "work_order_id")
    private Integer _workOrderId;

    @Source
    private JsonObject SOURCE;

    public UpdateModelMetadataData() {
        SOURCE = new JsonObject();
    }

    public UpdateModelMetadataData(JsonObject obj) {
        SOURCE = obj;
    }

    public void setCompanyId(Integer companyId) throws ParseException {
        _companyId = companyId;
        SOURCE.put("company_id", companyId);
    }

    public Integer getCompanyId() {
        try {
            if (_companyId != null)
                return _companyId;

            if (SOURCE.has("company_id") && SOURCE.get("company_id") != null)
                _companyId = SOURCE.getInt("company_id");

        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _companyId;
    }

    public UpdateModelMetadataData companyId(Integer companyId) throws ParseException {
        _companyId = companyId;
        SOURCE.put("company_id", companyId);
        return this;
    }

    public void setGroupId(Integer groupId) throws ParseException {
        _groupId = groupId;
        SOURCE.put("group_id", groupId);
    }

    public Integer getGroupId() {
        try {
            if (_groupId != null)
                return _groupId;

            if (SOURCE.has("group_id") && SOURCE.get("group_id") != null)
                _groupId = SOURCE.getInt("group_id");

        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _groupId;
    }

    public UpdateModelMetadataData groupId(Integer groupId) throws ParseException {
        _groupId = groupId;
        SOURCE.put("group_id", groupId);
        return this;
    }

    public void setRole(String role) throws ParseException {
        _role = role;
        SOURCE.put("role", role);
    }

    public String getRole() {
        try {
            if (_role != null)
                return _role;

            if (SOURCE.has("role") && SOURCE.get("role") != null)
                _role = SOURCE.getString("role");

        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _role;
    }

    public UpdateModelMetadataData role(String role) throws ParseException {
        _role = role;
        SOURCE.put("role", role);
        return this;
    }

    public void setRoute(String route) throws ParseException {
        _route = route;
        SOURCE.put("route", route);
    }

    public String getRoute() {
        try {
            if (_route != null)
                return _route;

            if (SOURCE.has("route") && SOURCE.get("route") != null)
                _route = SOURCE.getString("route");

        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _route;
    }

    public UpdateModelMetadataData route(String route) throws ParseException {
        _route = route;
        SOURCE.put("route", route);
        return this;
    }

    public void setUserId(Integer userId) throws ParseException {
        _userId = userId;
        SOURCE.put("user_id", userId);
    }

    public Integer getUserId() {
        try {
            if (_userId != null)
                return _userId;

            if (SOURCE.has("user_id") && SOURCE.get("user_id") != null)
                _userId = SOURCE.getInt("user_id");

        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _userId;
    }

    public UpdateModelMetadataData userId(Integer userId) throws ParseException {
        _userId = userId;
        SOURCE.put("user_id", userId);
        return this;
    }

    public void setWorkOrderId(Integer workOrderId) throws ParseException {
        _workOrderId = workOrderId;
        SOURCE.put("work_order_id", workOrderId);
    }

    public Integer getWorkOrderId() {
        try {
            if (_workOrderId != null)
                return _workOrderId;

            if (SOURCE.has("work_order_id") && SOURCE.get("work_order_id") != null)
                _workOrderId = SOURCE.getInt("work_order_id");

        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _workOrderId;
    }

    public UpdateModelMetadataData workOrderId(Integer workOrderId) throws ParseException {
        _workOrderId = workOrderId;
        SOURCE.put("work_order_id", workOrderId);
        return this;
    }

    /*-*****************************-*/
    /*-             Json            -*/
    /*-*****************************-*/
    public static JsonArray toJsonArray(UpdateModelMetadataData[] array) {
        JsonArray list = new JsonArray();
        for (UpdateModelMetadataData item : array) {
            list.add(item.getJson());
        }
        return list;
    }

    public static UpdateModelMetadataData[] fromJsonArray(JsonArray array) {
        UpdateModelMetadataData[] list = new UpdateModelMetadataData[array.size()];
        for (int i = 0; i < array.size(); i++) {
            list[i] = fromJson(array.getJsonObject(i));
        }
        return list;
    }

    public static UpdateModelMetadataData fromJson(JsonObject obj) {
        try {
            return new UpdateModelMetadataData(obj);
        } catch (Exception ex) {
            Log.v(TAG, TAG, ex);
            return null;
        }
    }

    public JsonObject getJson() {
        return SOURCE;
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
        dest.writeParcelable(getJson(), flags);
    }
}
