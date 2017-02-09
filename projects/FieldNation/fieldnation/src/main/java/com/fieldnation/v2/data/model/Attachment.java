package com.fieldnation.v2.data.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.fieldnation.fnjson.JsonArray;
import com.fieldnation.fnjson.JsonObject;
import com.fieldnation.fnjson.Serializer;
import com.fieldnation.fnjson.Unserializer;
import com.fieldnation.fnjson.annotations.Json;
import com.fieldnation.fnlog.Log;

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

    public Attachment() {
    }

    public void setAuthor(User author) {
        _author = author;
    }

    public User getAuthor() {
        return _author;
    }

    public Attachment author(User author) {
        _author = author;
        return this;
    }

    public void setCreated(Date created) {
        _created = created;
    }

    public Date getCreated() {
        return _created;
    }

    public Attachment created(Date created) {
        _created = created;
        return this;
    }

    public void setFile(File file) {
        _file = file;
    }

    public File getFile() {
        return _file;
    }

    public Attachment file(File file) {
        _file = file;
        return this;
    }

    public void setFolderId(Integer folderId) {
        _folderId = folderId;
    }

    public Integer getFolderId() {
        return _folderId;
    }

    public Attachment folderId(Integer folderId) {
        _folderId = folderId;
        return this;
    }

    public void setId(Integer id) {
        _id = id;
    }

    public Integer getId() {
        return _id;
    }

    public Attachment id(Integer id) {
        _id = id;
        return this;
    }

    public void setNotes(String notes) {
        _notes = notes;
    }

    public String getNotes() {
        return _notes;
    }

    public Attachment notes(String notes) {
        _notes = notes;
        return this;
    }

    public void setReviewed(Date reviewed) {
        _reviewed = reviewed;
    }

    public Date getReviewed() {
        return _reviewed;
    }

    public Attachment reviewed(Date reviewed) {
        _reviewed = reviewed;
        return this;
    }

    public void setReviewer(User reviewer) {
        _reviewer = reviewer;
    }

    public User getReviewer() {
        return _reviewer;
    }

    public Attachment reviewer(User reviewer) {
        _reviewer = reviewer;
        return this;
    }

    public void setShowBeforeAssignment(Boolean showBeforeAssignment) {
        _showBeforeAssignment = showBeforeAssignment;
    }

    public Boolean getShowBeforeAssignment() {
        return _showBeforeAssignment;
    }

    public Attachment showBeforeAssignment(Boolean showBeforeAssignment) {
        _showBeforeAssignment = showBeforeAssignment;
        return this;
    }

    public void setStatus(StatusEnum status) {
        _status = status;
    }

    public StatusEnum getStatus() {
        return _status;
    }

    public Attachment status(StatusEnum status) {
        _status = status;
        return this;
    }

    public void setStatusDescription(String statusDescription) {
        _statusDescription = statusDescription;
    }

    public String getStatusDescription() {
        return _statusDescription;
    }

    public Attachment statusDescription(String statusDescription) {
        _statusDescription = statusDescription;
        return this;
    }

    public void setTask(Task task) {
        _task = task;
    }

    public Task getTask() {
        return _task;
    }

    public Attachment task(Task task) {
        _task = task;
        return this;
    }

    /*-******************************-*/
    /*-             Enums            -*/
    /*-******************************-*/
    public enum StatusEnum {
        @Json(name = "approved")
        APPROVED("approved"),
        @Json(name = "pending")
        PENDING("pending"),
        @Json(name = "denied")
        DENIED("denied");

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

    public JsonObject toJson() {
        return toJson(this);
    }

    public static JsonObject toJson(Attachment attachment) {
        try {
            return Serializer.serializeObject(attachment);
        } catch (Exception ex) {
            Log.v(TAG, TAG, ex);
            return null;
        }
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
        dest.writeParcelable(toJson(), flags);
    }
}
