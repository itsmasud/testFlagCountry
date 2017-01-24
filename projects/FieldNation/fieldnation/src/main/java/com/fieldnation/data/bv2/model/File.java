package com.fieldnation.data.bv2.model;

import com.fieldnation.fnjson.JsonObject;
import com.fieldnation.fnjson.Serializer;
import com.fieldnation.fnjson.Unserializer;
import com.fieldnation.fnjson.annotations.Json;
import com.fieldnation.fnlog.Log;

public class File {
    private static final String TAG = "File";

    @Json(name = "thumbnail")
    private String thumbnail;

    @Json(name = "size_bytes")
    private Integer sizeBytes;

    @Json(name = "mime")
    private String mime;

    @Json(name = "preview_full")
    private String previewFull;

    @Json(name = "name")
    private String name;

    @Json(name = "icon")
    private String icon;

    @Json(name = "link")
    private String link;

    @Json(name = "description")
    private String description;

    @Json(name = "storage")
    private StorageEnum storage;

    @Json(name = "type")
    private TypeEnum type;

    public File() {
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public Integer getSizeBytes() {
        return sizeBytes;
    }

    public String getMime() {
        return mime;
    }

    public String getPreviewFull() {
        return previewFull;
    }

    public String getName() {
        return name;
    }

    public String getIcon() {
        return icon;
    }

    public String getLink() {
        return link;
    }

    public String getDescription() {
        return description;
    }

    public StorageEnum getStorage() {
        return storage;
    }

    public TypeEnum getType() {
        return type;
    }

    /*-*****************************-*/
    /*-             Json            -*/
    /*-*****************************-*/
    public static File fromJson(JsonObject obj) {
        try {
            return Unserializer.unserializeObject(File.class, obj);
        } catch (Exception ex) {
            Log.v(TAG, ex);
            return null;
        }
    }

    public JsonObject toJson() {
        return toJson(this);
    }

    public static JsonObject toJson(File file) {
        try {
            return Serializer.serializeObject(file);
        } catch (Exception ex) {
            Log.v(TAG, ex);
            return null;
        }
    }
}
