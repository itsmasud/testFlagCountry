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

public class Attachment implements Parcelable {
    private static final String TAG = "Attachment";

    @Json(name = "actions")
    private ActionsEnum[] _actions;

    @Json(name = "author")
    private User _author;

    @Json(name = "created")
    private Date _created;

    @Json(name = "file")
    private File _file;

    @Json(name = "folder_id")
    private Integer _folderId;

    @Json(name = "id")
    private Integer _id;

    @Json(name = "notes")
    private String _notes;

    @Json(name = "reviewed")
    private Date _reviewed;

    @Json(name = "reviewer")
    private User _reviewer;

    @Json(name = "show_before_assignment")
    private Boolean _showBeforeAssignment;

    @Json(name = "status")
    private StatusEnum _status;

    @Json(name = "status_description")
    private String _statusDescription;

    @Json(name = "task")
    private Task _task;

    @Source
    private JsonObject SOURCE;

    public Attachment() {
        SOURCE = new JsonObject();
    }

    public Attachment(JsonObject obj) {
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

    public Attachment actions(ActionsEnum[] actions) throws ParseException {
        _actions = actions;
        JsonArray ja = new JsonArray();
        for (ActionsEnum item : actions) {
            ja.add(item.toString());
        }
        SOURCE.put("actions", ja, true);
        return this;
    }

    public void setAuthor(User author) throws ParseException {
        _author = author;
        SOURCE.put("author", author.getJson());
    }

    public User getAuthor() {
        try {
            if (_author == null && SOURCE.has("author") && SOURCE.get("author") != null)
                _author = User.fromJson(SOURCE.getJsonObject("author"));
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        if (_author != null && _author.isSet())
            return _author;

        return null;
    }

    public Attachment author(User author) throws ParseException {
        _author = author;
        SOURCE.put("author", author.getJson());
        return this;
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

    public Attachment created(Date created) throws ParseException {
        _created = created;
        SOURCE.put("created", created.getJson());
        return this;
    }

    public void setFile(File file) throws ParseException {
        _file = file;
        SOURCE.put("file", file.getJson());
    }

    public File getFile() {
        try {
            if (_file == null && SOURCE.has("file") && SOURCE.get("file") != null)
                _file = File.fromJson(SOURCE.getJsonObject("file"));
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        if (_file != null && _file.isSet())
            return _file;

        return null;
    }

    public Attachment file(File file) throws ParseException {
        _file = file;
        SOURCE.put("file", file.getJson());
        return this;
    }

    public void setFolderId(Integer folderId) throws ParseException {
        _folderId = folderId;
        SOURCE.put("folder_id", folderId);
    }

    public Integer getFolderId() {
        try {
            if (_folderId == null && SOURCE.has("folder_id") && SOURCE.get("folder_id") != null)
                _folderId = SOURCE.getInt("folder_id");
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _folderId;
    }

    public Attachment folderId(Integer folderId) throws ParseException {
        _folderId = folderId;
        SOURCE.put("folder_id", folderId);
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

    public Attachment id(Integer id) throws ParseException {
        _id = id;
        SOURCE.put("id", id);
        return this;
    }

    public void setNotes(String notes) throws ParseException {
        _notes = notes;
        SOURCE.put("notes", notes);
    }

    public String getNotes() {
        try {
            if (_notes == null && SOURCE.has("notes") && SOURCE.get("notes") != null)
                _notes = SOURCE.getString("notes");
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _notes;
    }

    public Attachment notes(String notes) throws ParseException {
        _notes = notes;
        SOURCE.put("notes", notes);
        return this;
    }

    public void setReviewed(Date reviewed) throws ParseException {
        _reviewed = reviewed;
        SOURCE.put("reviewed", reviewed.getJson());
    }

    public Date getReviewed() {
        try {
            if (_reviewed == null && SOURCE.has("reviewed") && SOURCE.get("reviewed") != null)
                _reviewed = Date.fromJson(SOURCE.getJsonObject("reviewed"));
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        if (_reviewed != null && _reviewed.isSet())
            return _reviewed;

        return null;
    }

    public Attachment reviewed(Date reviewed) throws ParseException {
        _reviewed = reviewed;
        SOURCE.put("reviewed", reviewed.getJson());
        return this;
    }

    public void setReviewer(User reviewer) throws ParseException {
        _reviewer = reviewer;
        SOURCE.put("reviewer", reviewer.getJson());
    }

    public User getReviewer() {
        try {
            if (_reviewer == null && SOURCE.has("reviewer") && SOURCE.get("reviewer") != null)
                _reviewer = User.fromJson(SOURCE.getJsonObject("reviewer"));
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        if (_reviewer != null && _reviewer.isSet())
            return _reviewer;

        return null;
    }

    public Attachment reviewer(User reviewer) throws ParseException {
        _reviewer = reviewer;
        SOURCE.put("reviewer", reviewer.getJson());
        return this;
    }

    public void setShowBeforeAssignment(Boolean showBeforeAssignment) throws ParseException {
        _showBeforeAssignment = showBeforeAssignment;
        SOURCE.put("show_before_assignment", showBeforeAssignment);
    }

    public Boolean getShowBeforeAssignment() {
        try {
            if (_showBeforeAssignment == null && SOURCE.has("show_before_assignment") && SOURCE.get("show_before_assignment") != null)
                _showBeforeAssignment = SOURCE.getBoolean("show_before_assignment");
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _showBeforeAssignment;
    }

    public Attachment showBeforeAssignment(Boolean showBeforeAssignment) throws ParseException {
        _showBeforeAssignment = showBeforeAssignment;
        SOURCE.put("show_before_assignment", showBeforeAssignment);
        return this;
    }

    public void setStatus(StatusEnum status) throws ParseException {
        _status = status;
        SOURCE.put("status", status.toString());
    }

    public StatusEnum getStatus() {
        try {
            if (_status == null && SOURCE.has("status") && SOURCE.get("status") != null)
                _status = StatusEnum.fromString(SOURCE.getString("status"));
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _status;
    }

    public Attachment status(StatusEnum status) throws ParseException {
        _status = status;
        SOURCE.put("status", status.toString());
        return this;
    }

    public void setStatusDescription(String statusDescription) throws ParseException {
        _statusDescription = statusDescription;
        SOURCE.put("status_description", statusDescription);
    }

    public String getStatusDescription() {
        try {
            if (_statusDescription == null && SOURCE.has("status_description") && SOURCE.get("status_description") != null)
                _statusDescription = SOURCE.getString("status_description");
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _statusDescription;
    }

    public Attachment statusDescription(String statusDescription) throws ParseException {
        _statusDescription = statusDescription;
        SOURCE.put("status_description", statusDescription);
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

    public Attachment task(Task task) throws ParseException {
        _task = task;
        SOURCE.put("task", task.getJson());
        return this;
    }

    /*-******************************-*/
    /*-             Enums            -*/
    /*-******************************-*/
    public enum StatusEnum {
        @Json(name = "approved")
        APPROVED("approved"),
        @Json(name = "denied")
        DENIED("denied"),
        @Json(name = "pending")
        PENDING("pending");

        private String value;

        StatusEnum(String value) {
            this.value = value;
        }

        public static StatusEnum fromString(String value) {
            StatusEnum[] values = values();
            for (StatusEnum v : values) {
                if (v.value.equals(value))
                    return v;
            }
            return null;
        }

        public static StatusEnum[] fromJsonArray(JsonArray jsonArray) {
            StatusEnum[] list = new StatusEnum[jsonArray.size()];
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
        @Json(name = "edit")
        EDIT("edit");

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
    public static JsonArray toJsonArray(Attachment[] array) {
        JsonArray list = new JsonArray();
        for (Attachment item : array) {
            list.add(item.getJson());
        }
        return list;
    }

    public static Attachment[] fromJsonArray(JsonArray array) {
        Attachment[] list = new Attachment[array.size()];
        for (int i = 0; i < array.size(); i++) {
            list[i] = fromJson(array.getJsonObject(i));
        }
        return list;
    }

    public static Attachment fromJson(JsonObject obj) {
        try {
            return new Attachment(obj);
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
    public static final Parcelable.Creator<Attachment> CREATOR = new Parcelable.Creator<Attachment>() {

        @Override
        public Attachment createFromParcel(Parcel source) {
            try {
                return Attachment.fromJson((JsonObject) source.readParcelable(JsonObject.class.getClassLoader()));
            } catch (Exception ex) {
                Log.v(TAG, ex);
                return null;
            }
        }

        @Override
        public Attachment[] newArray(int size) {
            return new Attachment[size];
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

    private Set<Attachment.ActionsEnum> _actionsSet = null;

    public Set<Attachment.ActionsEnum> getActionsSet() {
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
