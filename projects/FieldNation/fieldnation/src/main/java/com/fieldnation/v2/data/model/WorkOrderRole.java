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

public class WorkOrderRole implements Parcelable {
    private static final String TAG = "WorkOrderRole";

    @Json(name = "role")
    private String _role;

    @Source
    private JsonObject SOURCE;

    public WorkOrderRole() {
        SOURCE = new JsonObject();
    }

    public WorkOrderRole(JsonObject obj) {
        SOURCE = obj;
    }

    public void setRole(String role) throws ParseException {
        _role = role;
        SOURCE.put("role", role);
    }

    public String getRole() {
        try {
            if (_role == null && SOURCE.has("role") && SOURCE.get("role") != null)
                _role = SOURCE.getString("role");
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _role;
    }

    public WorkOrderRole role(String role) throws ParseException {
        _role = role;
        SOURCE.put("role", role);
        return this;
    }

    /*-*****************************-*/
    /*-             Json            -*/
    /*-*****************************-*/
    public static JsonArray toJsonArray(WorkOrderRole[] array) {
        JsonArray list = new JsonArray();
        for (WorkOrderRole item : array) {
            list.add(item.getJson());
        }
        return list;
    }

    public static WorkOrderRole[] fromJsonArray(JsonArray array) {
        WorkOrderRole[] list = new WorkOrderRole[array.size()];
        for (int i = 0; i < array.size(); i++) {
            list[i] = fromJson(array.getJsonObject(i));
        }
        return list;
    }

    public static WorkOrderRole fromJson(JsonObject obj) {
        try {
            return new WorkOrderRole(obj);
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
    public static final Parcelable.Creator<WorkOrderRole> CREATOR = new Parcelable.Creator<WorkOrderRole>() {

        @Override
        public WorkOrderRole createFromParcel(Parcel source) {
            try {
                return WorkOrderRole.fromJson((JsonObject) source.readParcelable(JsonObject.class.getClassLoader()));
            } catch (Exception ex) {
                Log.v(TAG, ex);
                return null;
            }
        }

        @Override
        public WorkOrderRole[] newArray(int size) {
            return new WorkOrderRole[size];
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

}
