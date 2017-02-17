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

public class Attachment implements Parcelable {
    private static final String TAG = "Attachment";

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
    private JsonObject SOURCE = new JsonObject();

    public Attachment() {
    }

    public void setAuthor(User author) throws ParseException {
        _author = author;
        SOURCE.put("author", author.getJson());
    }

    public User getAuthor() {
        return _author;
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
        return _created;
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
        return _file;
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
        return _reviewed;
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
        return _reviewer;
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
        return _task;
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
            return Unserializer.unserializeObject(Attachment.class, obj);
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
}
