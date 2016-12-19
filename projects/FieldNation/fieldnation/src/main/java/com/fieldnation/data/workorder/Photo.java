package com.fieldnation.data.workorder;

import com.fieldnation.fnjson.JsonObject;
import com.fieldnation.fnjson.Serializer;
import com.fieldnation.fnjson.Unserializer;
import com.fieldnation.fnjson.annotations.Json;
import com.fieldnation.fnlog.Log;

public class Photo {
    private static final String TAG = "Photo";

    @Json(name = "image")
    private String _image;
    @Json(name = "thumb")
    private String _thumb;

    public Photo() {
    }

    public String getImage() {
        return _image;
    }

    public String getThumb() {
        return _thumb;
    }

    public JsonObject toJson() {
        return toJson(this);
    }

    public static JsonObject toJson(Photo photo) {
        try {
            return Serializer.serializeObject(photo);
        } catch (Exception ex) {
            Log.v(TAG, ex);
            return null;
        }
    }

    public static Photo fromJson(JsonObject json) {
        try {
            return Unserializer.unserializeObject(Photo.class, json);
        } catch (Exception ex) {
            Log.v(TAG, ex);
            return null;
        }
    }
}