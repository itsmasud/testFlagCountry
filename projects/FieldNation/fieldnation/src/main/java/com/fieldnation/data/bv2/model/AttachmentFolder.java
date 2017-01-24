package com.fieldnation.data.bv2.model;

import com.fieldnation.fnjson.JsonObject;
import com.fieldnation.fnjson.Serializer;
import com.fieldnation.fnjson.Unserializer;
import com.fieldnation.fnjson.annotations.Json;
import com.fieldnation.fnlog.Log;

public class AttachmentFolder {
    private static final String TAG = "AttachmentFolder";

    @Json(name = "task")
    private Task task;

    @Json(name = "name")
    private String name;

    @Json(name = "id")
    private Integer id;

    @Json(name = "type")
    private TypeEnum type;

    public AttachmentFolder() {
    }

    public Task getTask() {
        return task;
    }

    public String getName() {
        return name;
    }

    public Integer getId() {
        return id;
    }

    public TypeEnum getType() {
        return type;
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
