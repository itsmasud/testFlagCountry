package com.fieldnation.v2.data.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.fieldnation.fnjson.JsonArray;
import com.fieldnation.fnjson.JsonObject;
import com.fieldnation.fnjson.annotations.Json;
import com.fieldnation.fnjson.annotations.Source;
import com.fieldnation.fnlog.Log;
import com.fieldnation.fntools.misc;

import java.text.ParseException;

/**
 * Created by dmgen from swagger.
 */

public class TaskGroup implements Parcelable {
    private static final String TAG = "TaskGroup";

    @Json(name = "id")
    private String _id;

    @Json(name = "label")
    private String _label;

    @Source
    private JsonObject SOURCE;

    public TaskGroup() {
        SOURCE = new JsonObject();
    }

    public TaskGroup(JsonObject obj) {
        SOURCE = obj;
    }

    public void setId(String id) throws ParseException {
        _id = id;
        SOURCE.put("id", id);
    }

    public String getId() {
        try {
            if (_id == null && SOURCE.has("id") && SOURCE.get("id") != null)
                _id = SOURCE.getString("id");
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _id;
    }

    public TaskGroup id(String id) throws ParseException {
        _id = id;
        SOURCE.put("id", id);
        return this;
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

    public TaskGroup label(String label) throws ParseException {
        _label = label;
        SOURCE.put("label", label);
        return this;
    }

    /*-*****************************-*/
    /*-             Json            -*/
    /*-*****************************-*/
    public static JsonArray toJsonArray(TaskGroup[] array) {
        JsonArray list = new JsonArray();
        for (TaskGroup item : array) {
            list.add(item.getJson());
        }
        return list;
    }

    public static TaskGroup[] fromJsonArray(JsonArray array) {
        TaskGroup[] list = new TaskGroup[array.size()];
        for (int i = 0; i < array.size(); i++) {
            list[i] = fromJson(array.getJsonObject(i));
        }
        return list;
    }

    public static TaskGroup fromJson(JsonObject obj) {
        try {
            return new TaskGroup(obj);
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
    public static final Parcelable.Creator<TaskGroup> CREATOR = new Parcelable.Creator<TaskGroup>() {

        @Override
        public TaskGroup createFromParcel(Parcel source) {
            try {
                return TaskGroup.fromJson((JsonObject) source.readParcelable(JsonObject.class.getClassLoader()));
            } catch (Exception ex) {
                Log.v(TAG, ex);
                return null;
            }
        }

        @Override
        public TaskGroup[] newArray(int size) {
            return new TaskGroup[size];
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
        return !misc.isEmptyOrNull(getId());
    }
}
