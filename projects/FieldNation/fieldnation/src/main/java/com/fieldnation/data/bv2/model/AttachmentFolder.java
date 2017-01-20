package com.fieldnation.data.bv2.model;

import com.fieldnation.fnjson.JsonObject;
import com.fieldnation.fnjson.Serializer;
import com.fieldnation.fnjson.Unserializer;
import com.fieldnation.fnjson.annotations.Json;
import com.fieldnation.fnlog.Log;

/**
 * AttachmentFolder
 */
public class AttachmentFolder {
    private static final String TAG = "AttachmentFolder";
    
    @Json(name = "id")
    private Integer id = null;

    @Json(name = "type")
    private String type = null;

    @Json(name = "name")
    private String name = null;

    @Json(name = "notes")
    private String notes = null;

    @Json(name = "task")
    private Task task = null;

    public enum TypeEnum {
        SLOT("slot"),
        DOCUMENT("document");

        private String value;

        TypeEnum(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return String.valueOf(value);
        }

        public static TypeEnum fromValue(String value) {
            TypeEnum[] values = values();
            for (TypeEnum e : values) {
                if (e.value.equals(value))
                    return e;
            }
            return null;
        }
    }

    public AttachmentFolder() {
    }

    public Integer getId() {
        return id;
    }

    public TypeEnum getType() {
        return TypeEnum.fromValue(type);
    }

    public String getName() {
        return name;
    }

    public String getNotes() {
        return notes;
    }

    public Task getTask() {
        return task;
    }

    /*-*****************************-*/
    /*-             Json            -*/
    /*-*****************************-*/
    public static AttachmentFolder fromJson(JsonObject obj) {
        try {
            return Unserializer.unserializeObject(AttachmentFolder.class, obj);
        } catch (Exception ex) {
            Log.v(TAG, ex);
            return null;
        }
    }

    public JsonObject toJson() {
        return toJson(this);
    }

    public static JsonObject toJson(AttachmentFolder attachmentFolder) {
        try {
            return Serializer.serializeObject(attachmentFolder);
        } catch (Exception ex) {
            Log.v(TAG, ex);
            return null;
        }
    }
}

