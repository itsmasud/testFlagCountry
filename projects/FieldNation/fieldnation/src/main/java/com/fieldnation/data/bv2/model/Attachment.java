package com.fieldnation.data.bv2.model;

import com.fieldnation.fnjson.JsonObject;
import com.fieldnation.fnjson.Serializer;
import com.fieldnation.fnjson.Unserializer;
import com.fieldnation.fnjson.annotations.Json;
import com.fieldnation.fnlog.Log;

public class Attachment {
    private static final String TAG = "Attachment";

    @Json(name = "status_description")
    private String statusDescription;

    @Json(name = "file")
    private File file;

    @Json(name = "notes")
    private String notes;

    @Json(name = "task")
    private Task task;

    @Json(name = "created")
    private Date created;

    @Json(name = "author")
    private User author;

    @Json(name = "show_before_assignment")
    private Boolean showBeforeAssignment;

    @Json(name = "reviewed")
    private Date reviewed;

    @Json(name = "id")
    private Integer id;

    @Json(name = "reviewer")
    private User reviewer;

    @Json(name = "folder_id")
    private Integer folderId;

    @Json(name = "status")
    private StatusEnum status;

    public Attachment() {
    }

    public String getStatusDescription() {
        return statusDescription;
    }

    public File getFile() {
        return file;
    }

    public String getNotes() {
        return notes;
    }

    public Task getTask() {
        return task;
    }

    public Date getCreated() {
        return created;
    }

    public User getAuthor() {
        return author;
    }

    public Boolean getShowBeforeAssignment() {
        return showBeforeAssignment;
    }

    public Date getReviewed() {
        return reviewed;
    }

    public Integer getId() {
        return id;
    }

    public User getReviewer() {
        return reviewer;
    }

    public Integer getFolderId() {
        return folderId;
    }

    public StatusEnum getStatus() {
        return status;
    }

    /*-*****************************-*/
    /*-             Json            -*/
    /*-*****************************-*/
    public static Attachment fromJson(JsonObject obj) {
        try {
            return Unserializer.unserializeObject(Attachment.class, obj);
        } catch (Exception ex) {
            Log.v(TAG, ex);
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
            Log.v(TAG, ex);
            return null;
        }
    }
}
