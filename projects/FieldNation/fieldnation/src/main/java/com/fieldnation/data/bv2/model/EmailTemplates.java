package com.fieldnation.data.bv2.model;

import com.fieldnation.fnjson.JsonObject;
import com.fieldnation.fnjson.Serializer;
import com.fieldnation.fnjson.Unserializer;
import com.fieldnation.fnjson.annotations.Json;
import com.fieldnation.fnlog.Log;

public class EmailTemplates {
    private static final String TAG = "EmailTemplates";

    @Json(name = "results")
    private EmailTemplate[] results;

    public EmailTemplates() {
    }

    public EmailTemplate[] getResults() {
        return results;
    }

    /*-*****************************-*/
    /*-             Json            -*/
    /*-*****************************-*/
    public static EmailTemplates fromJson(JsonObject obj) {
        try {
            return Unserializer.unserializeObject(EmailTemplates.class, obj);
        } catch (Exception ex) {
            Log.v(TAG, ex);
            return null;
        }
    }

    public JsonObject toJson() {
        return toJson(this);
    }

    public static JsonObject toJson(EmailTemplates emailTemplates) {
        try {
            return Serializer.serializeObject(emailTemplates);
        } catch (Exception ex) {
            Log.v(TAG, ex);
            return null;
        }
    }
}
