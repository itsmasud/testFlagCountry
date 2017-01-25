package com.fieldnation.data.bv2.model;

import com.fieldnation.fnjson.JsonObject;
import com.fieldnation.fnjson.Serializer;
import com.fieldnation.fnjson.Unserializer;
import com.fieldnation.fnjson.annotations.Json;
import com.fieldnation.fnlog.Log;

public class File {
    private static final String TAG = "File";

    @Json(name = "thumbnail")
    private String _thumbnail;

    @Json(name = "size_bytes")
    private Integer _sizeBytes;

    @Json(name = "mime")
    private String _mime;

    @Json(name = "preview_full")
    private String _previewFull;

    @Json(name = "name")
    private String _name;

    @Json(name = "icon")
    private String _icon;

    @Json(name = "link")
    private String _link;

    @Json(name = "description")
    private String _description;

    @Json(name = "storage")
    private StorageEnum _storage;

    @Json(name = "type")
    private TypeEnum _type;

    public File() {
    }

    public String getThumbnail() {
        return _thumbnail;
    }

    public Integer getSizeBytes() {
        return _sizeBytes;
    }

    public String getMime() {
        return _mime;
    }

    public String getPreviewFull() {
        return _previewFull;
    }

    public String getName() {
        return _name;
    }

    public String getIcon() {
        return _icon;
    }

    public String getLink() {
        return _link;
    }

    public String getDescription() {
        return _description;
    }

    public StorageEnum getStorage() {
        return _storage;
    }

    public TypeEnum getType() {
        return _type;
    }

    /*-*****************************-*/
    /*-             Json            -*/
    /*-*****************************-*/
    public static File fromJson(JsonObject obj) {
        try {
            return Unserializer.unserializeObject(File.class, obj);
        } catch (Exception ex) {
            Log.v(TAG, TAG, ex);
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
            Log.v(TAG, TAG, ex);
            return null;
        }
    }
}
