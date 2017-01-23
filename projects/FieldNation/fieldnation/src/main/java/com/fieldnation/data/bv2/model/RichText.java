package com.fieldnation.data.bv2.model;

import com.fieldnation.fnjson.JsonObject;
import com.fieldnation.fnjson.Serializer;
import com.fieldnation.fnjson.Unserializer;
import com.fieldnation.fnjson.annotations.Json;
import com.fieldnation.fnlog.Log;

public class RichText {
    private static final String TAG = "RichText";

    @Json(name = "html")
    private String html = null;

    @Json(name = "markdown")
    private String markdown = null;

    public RichText() {
    }

    public String getHtml() {
        return html;
    }

    public String getMarkdown() {
        return markdown;
    }

    /*-*****************************-*/
    /*-             Json            -*/
    /*-*****************************-*/
    public static RichText fromJson(JsonObject obj) {
        try {
            return Unserializer.unserializeObject(RichText.class, obj);
        } catch (Exception ex) {
            Log.v(TAG, ex);
            return null;
        }
    }

    public JsonObject toJson() {
        return toJson(this);
    }

    public static JsonObject toJson(RichText richText) {
        try {
            return Serializer.serializeObject(richText);
        } catch (Exception ex) {
            Log.v(TAG, ex);
            return null;
        }
    }
}