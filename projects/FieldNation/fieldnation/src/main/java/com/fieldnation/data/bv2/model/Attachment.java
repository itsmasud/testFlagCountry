package com.fieldnation.data.bv2.model;

import com.fieldnation.fnjson.JsonObject;
import com.fieldnation.fnjson.Serializer;
import com.fieldnation.fnjson.Unserializer;
import com.fieldnation.fnjson.annotations.Json;
import com.fieldnation.fnlog.Log;

/**
 * Attachment
 */
public class Attachment {
    private static final String TAG = "Attachment";

    @Json(name = "id")
    private Integer id = null;

    @Json(name = "folder_id")
    private Integer folderId = null;

    @Json(name = "file")
    private String file = null;

    @Json(name = "created")
    private String created = null;

    @Json(name = "author")
    private User author = null;

    @Json(name = "reviewer")
    private User reviewer = null;

    @Json(name = "status")
    private String status = null;

    @Json(name = "status_description")
    private String statusDescription = null;

    @Json(name = "show_before_assignment")
    private Boolean showBeforeAssignment = null;

    @Json(name = "task")
    private Task task = null;

    @Json(name = "reviewed")
    private String reviewed = null;

    public enum StatusEnum {
        PENDING("pending"),
        APPROVED("approved"),
        DENIED("denied");

        private String value;

        StatusEnum(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return String.valueOf(value);
        }

        public static StatusEnum fromString(String name) {
            StatusEnum[] values = values();

            for (StatusEnum se : values) {
                if (se.value.equals(name))
                    return se;
            }
            return null;
        }
    }

    public Attachment() {
    }

    public Integer getId() {
        return id;
    }

    public Integer getFolderId() {
        return folderId;
    }

    public String getFile() {
        return file;
    }

    public String getCreated() {
        return created;
    }

    public User getAuthor() {
        return author;
    }

    public User getReviewer() {
        return reviewer;
    }

    public StatusEnum getStatus() {
        return StatusEnum.fromString(status);
    }

    public String getStatusDescription() {
        return statusDescription;
    }

    public Boolean getShowBeforeAssignment() {
        return showBeforeAssignment;
    }

    public Task getTask() {
        return task;
    }

    public String getReviewed() {
        return reviewed;
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

