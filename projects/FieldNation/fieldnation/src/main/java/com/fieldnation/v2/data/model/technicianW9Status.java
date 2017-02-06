package com.fieldnation.v2.data.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.fieldnation.fnjson.JsonArray;
import com.fieldnation.fnjson.JsonObject;
import com.fieldnation.fnjson.Serializer;
import com.fieldnation.fnjson.Unserializer;
import com.fieldnation.fnjson.annotations.Json;
import com.fieldnation.fnlog.Log;

/**
 * Created by dmgen from swagger on 1/31/17.
 */

public class technicianW9Status implements Parcelable {
    private static final String TAG = "technicianW9Status";

    @Json(name = "status_name")
    private String _statusName;

    @Json(name = "technician_w9_status_id")
    private Integer _technicianW9StatusId;

    public technicianW9Status() {
    }

    public void setStatusName(String statusName) {
        _statusName = statusName;
    }

    public String getStatusName() {
        return _statusName;
    }

    public technicianW9Status statusName(String statusName) {
        _statusName = statusName;
        return this;
    }

    public void setTechnicianW9StatusId(Integer technicianW9StatusId) {
        _technicianW9StatusId = technicianW9StatusId;
    }

    public Integer getTechnicianW9StatusId() {
        return _technicianW9StatusId;
    }

    public technicianW9Status technicianW9StatusId(Integer technicianW9StatusId) {
        _technicianW9StatusId = technicianW9StatusId;
        return this;
    }

    /*-*****************************-*/
    /*-             Json            -*/
    /*-*****************************-*/
    public static technicianW9Status[] fromJsonArray(JsonArray array) {
        technicianW9Status[] list = new technicianW9Status[array.size()];
        for (int i = 0; i < array.size(); i++) {
            list[i] = fromJson(array.getJsonObject(i));
        }
        return list;
    }

    public static technicianW9Status fromJson(JsonObject obj) {
        try {
            return Unserializer.unserializeObject(technicianW9Status.class, obj);
        } catch (Exception ex) {
            Log.v(TAG, TAG, ex);
            return null;
        }
    }

    public JsonObject toJson() {
        return toJson(this);
    }

    public static JsonObject toJson(technicianW9Status technicianW9Status) {
        try {
            return Serializer.serializeObject(technicianW9Status);
        } catch (Exception ex) {
            Log.v(TAG, TAG, ex);
            return null;
        }
    }

    /*-*********************************************-*/
    /*-			Parcelable Implementation           -*/
    /*-*********************************************-*/
    public static final Parcelable.Creator<technicianW9Status> CREATOR = new Parcelable.Creator<technicianW9Status>() {

        @Override
        public technicianW9Status createFromParcel(Parcel source) {
            try {
                return technicianW9Status.fromJson((JsonObject) source.readParcelable(JsonObject.class.getClassLoader()));
            } catch (Exception ex) {
                Log.v(TAG, ex);
                return null;
            }
        }

        @Override
        public technicianW9Status[] newArray(int size) {
            return new technicianW9Status[size];
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
