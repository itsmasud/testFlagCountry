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

public class TaskDocument implements Parcelable {
    private static final String TAG = "TaskDocument";

    @Json(name = "created")
    private Date _created;

    @Json(name = "file")
    private TaskDocumentFile _file;

    @Json(name = "id")
    private Integer _id;

    @Json(name = "link")
    private TaskDocumentLink _link;

    @Json(name = "revision")
    private Integer _revision;

    @Source
    private JsonObject SOURCE;

    public TaskDocument() {
        SOURCE = new JsonObject();
    }

    public TaskDocument(JsonObject obj) {
        SOURCE = obj;
    }

    public void setCreated(Date created) throws ParseException {
        _created = created;
        SOURCE.put("created", created.getJson());
    }

    public Date getCreated() {
        try {
            if (_created == null && SOURCE.has("created") && SOURCE.get("created") != null)
                _created = Date.fromJson(SOURCE.getJsonObject("created"));
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        if (_created != null && _created.isSet())
            return _created;

        return null;
    }

    public TaskDocument created(Date created) throws ParseException {
        _created = created;
        SOURCE.put("created", created.getJson());
        return this;
    }

    public void setFile(TaskDocumentFile file) throws ParseException {
        _file = file;
        SOURCE.put("file", file.getJson());
    }

    public TaskDocumentFile getFile() {
        try {
            if (_file == null && SOURCE.has("file") && SOURCE.get("file") != null)
                _file = TaskDocumentFile.fromJson(SOURCE.getJsonObject("file"));
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        if (_file != null && _file.isSet())
            return _file;

        return null;
    }

    public TaskDocument file(TaskDocumentFile file) throws ParseException {
        _file = file;
        SOURCE.put("file", file.getJson());
        return this;
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

    public TaskDocument id(Integer id) throws ParseException {
        _id = id;
        SOURCE.put("id", id);
        return this;
    }

    public void setLink(TaskDocumentLink link) throws ParseException {
        _link = link;
        SOURCE.put("link", link.getJson());
    }

    public TaskDocumentLink getLink() {
        try {
            if (_link == null && SOURCE.has("link") && SOURCE.get("link") != null)
                _link = TaskDocumentLink.fromJson(SOURCE.getJsonObject("link"));
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        if (_link != null && _link.isSet())
            return _link;

        return null;
    }

    public TaskDocument link(TaskDocumentLink link) throws ParseException {
        _link = link;
        SOURCE.put("link", link.getJson());
        return this;
    }

    public void setRevision(Integer revision) throws ParseException {
        _revision = revision;
        SOURCE.put("revision", revision);
    }

    public Integer getRevision() {
        try {
            if (_revision == null && SOURCE.has("revision") && SOURCE.get("revision") != null)
                _revision = SOURCE.getInt("revision");
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _revision;
    }

    public TaskDocument revision(Integer revision) throws ParseException {
        _revision = revision;
        SOURCE.put("revision", revision);
        return this;
    }

    /*-*****************************-*/
    /*-             Json            -*/
    /*-*****************************-*/
    public static JsonArray toJsonArray(TaskDocument[] array) {
        JsonArray list = new JsonArray();
        for (TaskDocument item : array) {
            list.add(item.getJson());
        }
        return list;
    }

    public static TaskDocument[] fromJsonArray(JsonArray array) {
        TaskDocument[] list = new TaskDocument[array.size()];
        for (int i = 0; i < array.size(); i++) {
            list[i] = fromJson(array.getJsonObject(i));
        }
        return list;
    }

    public static TaskDocument fromJson(JsonObject obj) {
        try {
            return new TaskDocument(obj);
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
    public static final Parcelable.Creator<TaskDocument> CREATOR = new Parcelable.Creator<TaskDocument>() {

        @Override
        public TaskDocument createFromParcel(Parcel source) {
            try {
                return TaskDocument.fromJson((JsonObject) source.readParcelable(JsonObject.class.getClassLoader()));
            } catch (Exception ex) {
                Log.v(TAG, ex);
                return null;
            }
        }

        @Override
        public TaskDocument[] newArray(int size) {
            return new TaskDocument[size];
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
}
