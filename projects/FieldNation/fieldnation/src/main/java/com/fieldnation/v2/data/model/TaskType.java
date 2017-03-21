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

public class TaskType implements Parcelable {
    private static final String TAG = "TaskType";

    @Json(name = "id")
    private Integer _id;

    @Json(name = "key")
    private String _key;

    @Json(name = "name")
    private String _name;

    @Source
    private JsonObject SOURCE;

    public TaskType() {
        SOURCE = new JsonObject();
    }

    public TaskType(JsonObject obj) {
        SOURCE = obj;
    }

    public void setId(Integer id) throws ParseException {
        _id = id;
        SOURCE.put("id", id);
    }

    public Integer getId() {
        try {
            if (_id == null && SOURCE.has("id") && SOURCE.get("id") != null)
                _id = SOURCE.getInt("id");
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _id;
    }

    public TaskType id(Integer id) throws ParseException {
        _id = id;
        SOURCE.put("id", id);
        return this;
    }

    public void setKey(String key) throws ParseException {
        _key = key;
        SOURCE.put("key", key);
    }

    public String getKey() {
        try {
            if (_key == null && SOURCE.has("key") && SOURCE.get("key") != null)
                _key = SOURCE.getString("key");
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _key;
    }

    public TaskType key(String key) throws ParseException {
        _key = key;
        SOURCE.put("key", key);
        return this;
    }

    public void setName(String name) throws ParseException {
        _name = name;
        SOURCE.put("name", name);
    }

    public String getName() {
        try {
            if (_name == null && SOURCE.has("name") && SOURCE.get("name") != null)
                _name = SOURCE.getString("name");
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _name;
    }

    public TaskType name(String name) throws ParseException {
        _name = name;
        SOURCE.put("name", name);
        return this;
    }

    /*-*****************************-*/
    /*-             Json            -*/
    /*-*****************************-*/
    public static JsonArray toJsonArray(TaskType[] array) {
        JsonArray list = new JsonArray();
        for (TaskType item : array) {
            list.add(item.getJson());
        }
        return list;
    }

    public static TaskType[] fromJsonArray(JsonArray array) {
        TaskType[] list = new TaskType[array.size()];
        for (int i = 0; i < array.size(); i++) {
            list[i] = fromJson(array.getJsonObject(i));
        }
        return list;
    }

    public static TaskType fromJson(JsonObject obj) {
        try {
            return new TaskType(obj);
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
    public static final Parcelable.Creator<TaskType> CREATOR = new Parcelable.Creator<TaskType>() {

        @Override
        public TaskType createFromParcel(Parcel source) {
            try {
                return TaskType.fromJson((JsonObject) source.readParcelable(JsonObject.class.getClassLoader()));
            } catch (Exception ex) {
                Log.v(TAG, ex);
                return null;
            }
        }

        @Override
        public TaskType[] newArray(int size) {
            return new TaskType[size];
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
        return getId() != null && getId() != 0;
    }

    public TaskType(int id, String title) throws ParseException {
        this();
        setId(id);
        setName(title);
    }

    @Override
    public String toString() {
        return getName();
    }
}
