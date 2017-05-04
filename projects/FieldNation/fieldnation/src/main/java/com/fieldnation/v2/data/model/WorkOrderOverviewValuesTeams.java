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

public class WorkOrderOverviewValuesTeams implements Parcelable {
    private static final String TAG = "WorkOrderOverviewValuesTeams";

    @Json(name = "label")
    private String _label;

    @Json(name = "value")
    private Integer _value;

    @Source
    private JsonObject SOURCE;

    public WorkOrderOverviewValuesTeams() {
        SOURCE = new JsonObject();
    }

    public WorkOrderOverviewValuesTeams(JsonObject obj) {
        SOURCE = obj;
    }

    public void setLabel(String label) throws ParseException {
        _label = label;
        SOURCE.put("label", label);
    }

    public String getLabel() {
        try {
            if (_label == null && SOURCE.has("label") && SOURCE.get("label") != null)
                _label = SOURCE.getString("label");
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _label;
    }

    public WorkOrderOverviewValuesTeams label(String label) throws ParseException {
        _label = label;
        SOURCE.put("label", label);
        return this;
    }

    public void setValue(Integer value) throws ParseException {
        _value = value;
        SOURCE.put("value", value);
    }

    public Integer getValue() {
        try {
            if (_value == null && SOURCE.has("value") && SOURCE.get("value") != null)
                _value = SOURCE.getInt("value");
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _value;
    }

    public WorkOrderOverviewValuesTeams value(Integer value) throws ParseException {
        _value = value;
        SOURCE.put("value", value);
        return this;
    }

    /*-*****************************-*/
    /*-             Json            -*/
    /*-*****************************-*/
    public static JsonArray toJsonArray(WorkOrderOverviewValuesTeams[] array) {
        JsonArray list = new JsonArray();
        for (WorkOrderOverviewValuesTeams item : array) {
            list.add(item.getJson());
        }
        return list;
    }

    public static WorkOrderOverviewValuesTeams[] fromJsonArray(JsonArray array) {
        WorkOrderOverviewValuesTeams[] list = new WorkOrderOverviewValuesTeams[array.size()];
        for (int i = 0; i < array.size(); i++) {
            list[i] = fromJson(array.getJsonObject(i));
        }
        return list;
    }

    public static WorkOrderOverviewValuesTeams fromJson(JsonObject obj) {
        try {
            return new WorkOrderOverviewValuesTeams(obj);
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
    public static final Parcelable.Creator<WorkOrderOverviewValuesTeams> CREATOR = new Parcelable.Creator<WorkOrderOverviewValuesTeams>() {

        @Override
        public WorkOrderOverviewValuesTeams createFromParcel(Parcel source) {
            try {
                return WorkOrderOverviewValuesTeams.fromJson((JsonObject) source.readParcelable(JsonObject.class.getClassLoader()));
            } catch (Exception ex) {
                Log.v(TAG, ex);
                return null;
            }
        }

        @Override
        public WorkOrderOverviewValuesTeams[] newArray(int size) {
            return new WorkOrderOverviewValuesTeams[size];
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
