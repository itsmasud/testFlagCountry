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

public class WorkOrderOverviewValuesTypesOfWork implements Parcelable {
    private static final String TAG = "WorkOrderOverviewValuesTypesOfWork";

    @Json(name = "label")
    private String _label;

    @Json(name = "value")
    private Integer _value;

    @Source
    private JsonObject SOURCE;

    public WorkOrderOverviewValuesTypesOfWork() {
        SOURCE = new JsonObject();
    }

    public WorkOrderOverviewValuesTypesOfWork(JsonObject obj) {
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

    public WorkOrderOverviewValuesTypesOfWork label(String label) throws ParseException {
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

    public WorkOrderOverviewValuesTypesOfWork value(Integer value) throws ParseException {
        _value = value;
        SOURCE.put("value", value);
        return this;
    }

    /*-*****************************-*/
    /*-             Json            -*/
    /*-*****************************-*/
    public static JsonArray toJsonArray(WorkOrderOverviewValuesTypesOfWork[] array) {
        JsonArray list = new JsonArray();
        for (WorkOrderOverviewValuesTypesOfWork item : array) {
            list.add(item.getJson());
        }
        return list;
    }

    public static WorkOrderOverviewValuesTypesOfWork[] fromJsonArray(JsonArray array) {
        WorkOrderOverviewValuesTypesOfWork[] list = new WorkOrderOverviewValuesTypesOfWork[array.size()];
        for (int i = 0; i < array.size(); i++) {
            list[i] = fromJson(array.getJsonObject(i));
        }
        return list;
    }

    public static WorkOrderOverviewValuesTypesOfWork fromJson(JsonObject obj) {
        try {
            return new WorkOrderOverviewValuesTypesOfWork(obj);
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
    public static final Parcelable.Creator<WorkOrderOverviewValuesTypesOfWork> CREATOR = new Parcelable.Creator<WorkOrderOverviewValuesTypesOfWork>() {

        @Override
        public WorkOrderOverviewValuesTypesOfWork createFromParcel(Parcel source) {
            try {
                return WorkOrderOverviewValuesTypesOfWork.fromJson((JsonObject) source.readParcelable(JsonObject.class.getClassLoader()));
            } catch (Exception ex) {
                Log.v(TAG, ex);
                return null;
            }
        }

        @Override
        public WorkOrderOverviewValuesTypesOfWork[] newArray(int size) {
            return new WorkOrderOverviewValuesTypesOfWork[size];
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
