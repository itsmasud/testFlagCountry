package com.fieldnation.data.bv2.model;

import com.fieldnation.fnjson.JsonObject;
import com.fieldnation.fnjson.Serializer;
import com.fieldnation.fnjson.Unserializer;
import com.fieldnation.fnjson.annotations.Json;
import com.fieldnation.fnlog.Log;

public class AttachmentFolder {
    private static final String TAG = "AttachmentFolder";

    @Json(name = "task")
    private Task _task;

    @Json(name = "name")
    private String _name;

    @Json(name = "id")
    private Integer _id;

    @Json(name = "type")
    private TypeEnum _type;

    public AttachmentFolder() {
    }

    public Task getTask() {
        return _task;
    }

    public String getName() {
        return _name;
    }

    public Integer getId() {
        return _id;
    }

    public TypeEnum getType() {
        return _type;
    }

    /*-*****************************-*/
    /*-             Json            -*/
    /*-*****************************-*/
    public static AttachmentFolder fromJson(JsonObject obj) {
        try {
            return Unserializer.unserializeObject(AttachmentFolder.class, obj);
        } catch (Exception ex) {
            Log.v(TAG, TAG, ex);
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
            Log.v(TAG, TAG, ex);
            return null;
        }
    }
}
