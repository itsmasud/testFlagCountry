package com.fieldnation.v2.data.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.fieldnation.fnjson.JsonArray;
import com.fieldnation.fnjson.JsonObject;
import com.fieldnation.fnjson.annotations.Json;
import com.fieldnation.fnjson.annotations.Source;
import com.fieldnation.fnlog.Log;

import java.text.ParseException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by dmgen from swagger.
 */

public class AttachmentFolder implements Parcelable {
    private static final String TAG = "AttachmentFolder";

    @Json(name = "actions")
    private ActionsEnum[] _actions;

    @Json(name = "id")
    private Integer _id;

    @Json(name = "name")
    private String _name;

    @Json(name = "results")
    private Attachment[] _results;

    @Json(name = "task")
    private Task _task;

    @Json(name = "type")
    private TypeEnum _type;

    @Source
    private JsonObject SOURCE;

    public AttachmentFolder() {
        SOURCE = new JsonObject();
    }

    public AttachmentFolder(JsonObject obj) {
        SOURCE = obj;
    }

    public void setActions(ActionsEnum[] actions) throws ParseException {
        _actions = actions;
        JsonArray ja = new JsonArray();
        for (ActionsEnum item : actions) {
            ja.add(item.toString());
        }
        SOURCE.put("actions", ja);
    }

    public ActionsEnum[] getActions() {
        try {
            if (_actions != null)
                return _actions;

            if (SOURCE.has("actions") && SOURCE.get("actions") != null) {
                _actions = ActionsEnum.fromJsonArray(SOURCE.getJsonArray("actions"));
            }

        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _actions;
    }

    public AttachmentFolder actions(ActionsEnum[] actions) throws ParseException {
        _actions = actions;
        JsonArray ja = new JsonArray();
        for (ActionsEnum item : actions) {
            ja.add(item.toString());
        }
        SOURCE.put("actions", ja, true);
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

    public AttachmentFolder id(Integer id) throws ParseException {
        _id = id;
        SOURCE.put("id", id);
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

    public AttachmentFolder name(String name) throws ParseException {
        _name = name;
        SOURCE.put("name", name);
        return this;
    }

    public void setResults(Attachment[] results) throws ParseException {
        _results = results;
        SOURCE.put("results", Attachment.toJsonArray(results));
    }

    public Attachment[] getResults() {
        try {
            if (_results != null)
                return _results;

            if (SOURCE.has("results") && SOURCE.get("results") != null) {
                _results = Attachment.fromJsonArray(SOURCE.getJsonArray("results"));
            }

        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _results;
    }

    public AttachmentFolder results(Attachment[] results) throws ParseException {
        _results = results;
        SOURCE.put("results", Attachment.toJsonArray(results), true);
        return this;
    }

    public void setTask(Task task) throws ParseException {
        _task = task;
        SOURCE.put("task", task.getJson());
    }

    public Task getTask() {
        try {
            if (_task == null && SOURCE.has("task") && SOURCE.get("task") != null)
                _task = Task.fromJson(SOURCE.getJsonObject("task"));
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        if (_task != null && _task.isSet())
            return _task;

        return null;
    }

    public AttachmentFolder task(Task task) throws ParseException {
        _task = task;
        SOURCE.put("task", task.getJson());
        return this;
    }

    public void setType(TypeEnum type) throws ParseException {
        _type = type;
        SOURCE.put("type", type.toString());
    }

    public TypeEnum getType() {
        try {
            if (_type == null && SOURCE.has("type") && SOURCE.get("type") != null)
                _type = TypeEnum.fromString(SOURCE.getString("type"));
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _type;
    }

    public AttachmentFolder type(TypeEnum type) throws ParseException {
        _type = type;
        SOURCE.put("type", type.toString());
        return this;
    }

    /*-******************************-*/
    /*-             Enums            -*/
    /*-******************************-*/
    public enum TypeEnum {
        @Json(name = "document")
        DOCUMENT("document"),
        @Json(name = "slot")
        SLOT("slot");

        private String value;

        TypeEnum(String value) {
            this.value = value;
        }

        public static TypeEnum fromString(String value) {
            TypeEnum[] values = values();
            for (TypeEnum v : values) {
                if (v.value.equals(value))
                    return v;
            }
            return null;
        }

        public static TypeEnum[] fromJsonArray(JsonArray jsonArray) {
            TypeEnum[] list = new TypeEnum[jsonArray.size()];
            for (int i = 0; i < list.length; i++) {
                list[i] = fromString(jsonArray.getString(i));
            }
            return list;
        }

        @Override
        public String toString() {
            return String.valueOf(value);
        }
    }

    public enum ActionsEnum {
        @Json(name = "delete")
        DELETE("delete"),
        @Json(name = "upload")
        UPLOAD("upload");

        private String value;

        ActionsEnum(String value) {
            this.value = value;
        }

        public static ActionsEnum fromString(String value) {
            ActionsEnum[] values = values();
            for (ActionsEnum v : values) {
                if (v.value.equals(value))
                    return v;
            }
            return null;
        }

        public static ActionsEnum[] fromJsonArray(JsonArray jsonArray) {
            ActionsEnum[] list = new ActionsEnum[jsonArray.size()];
            for (int i = 0; i < list.length; i++) {
                list[i] = fromString(jsonArray.getString(i));
            }
            return list;
        }

        @Override
        public String toString() {
            return String.valueOf(value);
        }
    }

    /*-*****************************-*/
    /*-             Json            -*/
    /*-*****************************-*/
    public static JsonArray toJsonArray(AttachmentFolder[] array) {
        JsonArray list = new JsonArray();
        for (AttachmentFolder item : array) {
            list.add(item.getJson());
        }
        return list;
    }

    public static AttachmentFolder[] fromJsonArray(JsonArray array) {
        AttachmentFolder[] list = new AttachmentFolder[array.size()];
        for (int i = 0; i < array.size(); i++) {
            list[i] = fromJson(array.getJsonObject(i));
        }
        return list;
    }

    public static AttachmentFolder fromJson(JsonObject obj) {
        try {
            return new AttachmentFolder(obj);
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
    public static final Parcelable.Creator<AttachmentFolder> CREATOR = new Parcelable.Creator<AttachmentFolder>() {

        @Override
        public AttachmentFolder createFromParcel(Parcel source) {
            try {
                return AttachmentFolder.fromJson((JsonObject) source.readParcelable(JsonObject.class.getClassLoader()));
            } catch (Exception ex) {
                Log.v(TAG, ex);
                return null;
            }
        }

        @Override
        public AttachmentFolder[] newArray(int size) {
            return new AttachmentFolder[size];
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
    private Set<ActionsEnum> _actionsSet = null;

    public Set<ActionsEnum> getActionsSet() {
        if (_actionsSet == null) {
            _actionsSet = new HashSet<>();
            _actionsSet.addAll(Arrays.asList(getActions()));
        }
        return _actionsSet;
    }

    public boolean isSet() {
        return getId() != null && getId() != 0;
    }
}
