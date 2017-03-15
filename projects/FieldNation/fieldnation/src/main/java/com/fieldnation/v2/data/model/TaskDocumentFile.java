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

public class TaskDocumentFile implements Parcelable {
    private static final String TAG = "TaskDocumentFile";

    @Json(name = "name")
    private String _name;

    @Json(name = "size")
    private Integer _size;

    @Json(name = "type")
    private String _type;

    @Source
    private JsonObject SOURCE;

    public TaskDocumentFile() {
        SOURCE = new JsonObject();
    }

    public TaskDocumentFile(JsonObject obj) {
        SOURCE = obj;
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

    public TaskDocumentFile name(String name) throws ParseException {
        _name = name;
        SOURCE.put("name", name);
        return this;
    }

    public void setSize(Integer size) throws ParseException {
        _size = size;
        SOURCE.put("size", size);
    }

    public Integer getSize() {
        try {
            if (_size == null && SOURCE.has("size") && SOURCE.get("size") != null)
                _size = SOURCE.getInt("size");
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _size;
    }

    public TaskDocumentFile size(Integer size) throws ParseException {
        _size = size;
        SOURCE.put("size", size);
        return this;
    }

    public void setType(String type) throws ParseException {
        _type = type;
        SOURCE.put("type", type);
    }

    public String getType() {
        try {
            if (_type == null && SOURCE.has("type") && SOURCE.get("type") != null)
                _type = SOURCE.getString("type");
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _type;
    }

    public TaskDocumentFile type(String type) throws ParseException {
        _type = type;
        SOURCE.put("type", type);
        return this;
    }

    /*-*****************************-*/
    /*-             Json            -*/
    /*-*****************************-*/
    public static JsonArray toJsonArray(TaskDocumentFile[] array) {
        JsonArray list = new JsonArray();
        for (TaskDocumentFile item : array) {
            list.add(item.getJson());
        }
        return list;
    }

    public static TaskDocumentFile[] fromJsonArray(JsonArray array) {
        TaskDocumentFile[] list = new TaskDocumentFile[array.size()];
        for (int i = 0; i < array.size(); i++) {
            list[i] = fromJson(array.getJsonObject(i));
        }
        return list;
    }

    public static TaskDocumentFile fromJson(JsonObject obj) {
        try {
            return new TaskDocumentFile(obj);
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
    public static final Parcelable.Creator<TaskDocumentFile> CREATOR = new Parcelable.Creator<TaskDocumentFile>() {

        @Override
        public TaskDocumentFile createFromParcel(Parcel source) {
            try {
                return TaskDocumentFile.fromJson((JsonObject) source.readParcelable(JsonObject.class.getClassLoader()));
            } catch (Exception ex) {
                Log.v(TAG, ex);
                return null;
            }
        }

        @Override
        public TaskDocumentFile[] newArray(int size) {
            return new TaskDocumentFile[size];
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
