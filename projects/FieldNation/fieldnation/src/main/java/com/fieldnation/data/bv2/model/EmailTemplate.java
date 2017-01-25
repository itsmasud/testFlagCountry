package com.fieldnation.data.bv2.model;

import com.fieldnation.fnjson.JsonObject;
import com.fieldnation.fnjson.Serializer;
import com.fieldnation.fnjson.Unserializer;
import com.fieldnation.fnjson.annotations.Json;
import com.fieldnation.fnlog.Log;

public class EmailTemplate {
    private static final String TAG = "EmailTemplate";

    @Json(name = "id")
    private Integer _id;

    @Json(name = "label")
    private String _label;

    public EmailTemplate() {
    }

    public Integer getId() {
        return _id;
    }

    public String getLabel() {
        return _label;
    }

    /*-*****************************-*/
    /*-             Json            -*/
    /*-*****************************-*/
    public static EmailTemplate fromJson(JsonObject obj) {
        try {
            return Unserializer.unserializeObject(EmailTemplate.class, obj);
        } catch (Exception ex) {
            Log.v(TAG, TAG, ex);
            return null;
        }
    }

    public JsonObject toJson() {
        return toJson(this);
    }

    public static JsonObject toJson(EmailTemplate emailTemplate) {
        try {
            return Serializer.serializeObject(emailTemplate);
        } catch (Exception ex) {
            Log.v(TAG, TAG, ex);
            return null;
        }
    }
}
