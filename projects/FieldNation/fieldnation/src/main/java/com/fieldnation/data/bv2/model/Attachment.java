package com.fieldnation.data.bv2.model;

import com.fieldnation.fnjson.JsonObject;
import com.fieldnation.fnjson.Serializer;
import com.fieldnation.fnjson.Unserializer;
import com.fieldnation.fnjson.annotations.Json;
import com.fieldnation.fnlog.Log;

public class Attachment {
    private static final String TAG = "Attachment";

    @Json(name = "status_description")
    private String _statusDescription;

    @Json(name = "file")
    private File _file;

    @Json(name = "notes")
    private String _notes;

    @Json(name = "task")
    private Task _task;

    @Json(name = "created")
    private Date _created;

    @Json(name = "author")
    private User _author;

    @Json(name = "show_before_assignment")
    private Boolean _showBeforeAssignment;

    @Json(name = "reviewed")
    private Date _reviewed;

    @Json(name = "id")
    private Integer _id;

    @Json(name = "reviewer")
    private User _reviewer;

    @Json(name = "folder_id")
    private Integer _folderId;

    @Json(name = "status")
    private StatusEnum _status;

    public Attachment() {
    }

    public String getStatusDescription() {
        return _statusDescription;
    }

    public File getFile() {
        return _file;
    }

    public String getNotes() {
        return _notes;
    }

    public Task getTask() {
        return _task;
    }

    public Date getCreated() {
        return _created;
    }

    public User getAuthor() {
        return _author;
    }

    public Boolean getShowBeforeAssignment() {
        return _showBeforeAssignment;
    }

    public Date getReviewed() {
        return _reviewed;
    }

    public Integer getId() {
        return _id;
    }

    public User getReviewer() {
        return _reviewer;
    }

    public Integer getFolderId() {
        return _folderId;
    }

    public StatusEnum getStatus() {
        return _status;
    }

    /*-*****************************-*/
    /*-             Json            -*/
    /*-*****************************-*/
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
}
