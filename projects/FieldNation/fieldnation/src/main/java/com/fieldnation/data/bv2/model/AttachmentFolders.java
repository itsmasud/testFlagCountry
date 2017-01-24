package com.fieldnation.data.bv2.model;

import com.fieldnation.fnjson.JsonObject;
import com.fieldnation.fnjson.Serializer;
import com.fieldnation.fnjson.Unserializer;
import com.fieldnation.fnjson.annotations.Json;
import com.fieldnation.fnlog.Log;

public class AttachmentFolders {
    private static final String TAG = "AttachmentFolders";

    @Json(name = "metadata")
    private ListEnvelope metadata;

    @Json(name = "results")
    private AttachmentFolder[] results;

    @Json(name = "actions")
    private String[] actions;

    public AttachmentFolders() {
    }

    public ListEnvelope getMetadata() {
        return metadata;
    }

    public AttachmentFolder[] getResults() {
        return results;
    }

    public String[] getActions() {
        return actions;
    }

    /*-*****************************-*/
    /*-             Json            -*/
    /*-*****************************-*/
    public static AttachmentFolders fromJson(JsonObject obj) {
        try {
            return Unserializer.unserializeObject(AttachmentFolders.class, obj);
        } catch (Exception ex) {
            Log.v(TAG, ex);
            return null;
        }
    }

    public JsonObject toJson() {
        return toJson(this);
    }

    public static JsonObject toJson(AttachmentFolders attachmentFolders) {
        try {
            return Serializer.serializeObject(attachmentFolders);
        } catch (Exception ex) {
            Log.v(TAG, ex);
            return null;
        }
    }
}
