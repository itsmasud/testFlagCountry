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
import com.fieldnation.fntools.misc;

import java.text.ParseException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by dmgen from swagger.
 */

public class TechnicianW9Status implements Parcelable {
    private static final String TAG = "TechnicianW9Status";

    @Json(name = "status_name")
    private String _statusName;

    @Json(name = "technician_w9_status_id")
    private Integer _technicianW9StatusId;

    @Source
    private JsonObject SOURCE;

    public TechnicianW9Status() {
        SOURCE = new JsonObject();
    }

    public TechnicianW9Status(JsonObject obj) {
        SOURCE = obj;
    }

    public void setStatusName(String statusName) throws ParseException {
        _statusName = statusName;
        SOURCE.put("status_name", statusName);
    }

    public String getStatusName() {
        try {
            if (_statusName == null && SOURCE.has("status_name") && SOURCE.get("status_name") != null)
                _statusName = SOURCE.getString("status_name");
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _statusName;
    }

    public TechnicianW9Status statusName(String statusName) throws ParseException {
        _statusName = statusName;
        SOURCE.put("status_name", statusName);
        return this;
    }

    public void setTechnicianW9StatusId(Integer technicianW9StatusId) throws ParseException {
        _technicianW9StatusId = technicianW9StatusId;
        SOURCE.put("technician_w9_status_id", technicianW9StatusId);
    }

    public Integer getTechnicianW9StatusId() {
        try {
            if (_technicianW9StatusId == null && SOURCE.has("technician_w9_status_id") && SOURCE.get("technician_w9_status_id") != null)
                _technicianW9StatusId = SOURCE.getInt("technician_w9_status_id");
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _technicianW9StatusId;
    }

    public TechnicianW9Status technicianW9StatusId(Integer technicianW9StatusId) throws ParseException {
        _technicianW9StatusId = technicianW9StatusId;
        SOURCE.put("technician_w9_status_id", technicianW9StatusId);
        return this;
    }

    /*-*****************************-*/
    /*-             Json            -*/
    /*-*****************************-*/
    public static JsonArray toJsonArray(TechnicianW9Status[] array) {
        JsonArray list = new JsonArray();
        for (TechnicianW9Status item : array) {
            list.add(item.getJson());
        }
        return list;
    }

    public static TechnicianW9Status[] fromJsonArray(JsonArray array) {
        TechnicianW9Status[] list = new TechnicianW9Status[array.size()];
        for (int i = 0; i < array.size(); i++) {
            list[i] = fromJson(array.getJsonObject(i));
        }
        return list;
    }

    public static TechnicianW9Status fromJson(JsonObject obj) {
        try {
            return new TechnicianW9Status(obj);
        } catch (Exception ex) {
            Log.v(TAG, ex);
            return null;
        }
    }

    public JsonObject getJson() {
        return SOURCE;
    }

    /*-*********************************************-*/
    /*-			Parcelable Implementation           -*/
    /*-*********************************************-*/
    public static final Parcelable.Creator<TechnicianW9Status> CREATOR = new Parcelable.Creator<TechnicianW9Status>() {

        @Override
        public TechnicianW9Status createFromParcel(Parcel source) {
            try {
                return TechnicianW9Status.fromJson((JsonObject) source.readParcelable(JsonObject.class.getClassLoader()));
            } catch (Exception ex) {
                Log.v(TAG, ex);
                return null;
            }
        }

        @Override
        public TechnicianW9Status[] newArray(int size) {
            return new TechnicianW9Status[size];
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

    /*-*****************************-*/
    /*-         Human Code          -*/
    /*-*****************************-*/

    public boolean isSet() {
        return true;
    }
}
