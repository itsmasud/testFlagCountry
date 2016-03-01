package com.fieldnation.data.profile;

import com.fieldnation.Log;
import com.fieldnation.json.JsonObject;
import com.fieldnation.json.Serializer;
import com.fieldnation.json.annotations.Json;

public class Photo {
    private static final String TAG = "Photo";

    @Json(name = "large")
    private String _large;
    @Json(name = "thumb")
    private String _thumb;

    public Photo() {
    }

    public String getLarge() {
        if ("/images/missing.png".equals(_large))
            return null;

        if ("https://app.fieldnation.com/images/missing.png".equals(_large))
            return null;

        return _large;
    }

    public String getThumb() {
        if ("/images/missing.png".equals(_thumb))
            return null;

        if ("https://app.fieldnation.com/images/missing.png".equals(_thumb))
            return null;

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
            return Serializer.unserializeObject(Photo.class, json);
        } catch (Exception ex) {
            Log.v(TAG, ex);
            return null;
        }
    }

}
