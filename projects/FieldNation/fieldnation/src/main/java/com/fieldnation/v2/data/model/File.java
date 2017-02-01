package com.fieldnation.v2.data.model;
import android.os.Parcel;
import android.os.Parcelable;

import com.fieldnation.fnjson.JsonArray;
import com.fieldnation.fnjson.JsonObject;
import com.fieldnation.fnjson.Serializer;
import com.fieldnation.fnjson.Unserializer;
import com.fieldnation.fnjson.annotations.Json;
import com.fieldnation.fnlog.Log;

/**
 * Created by dmgen from swagger on 1/31/17.
 */

public class File implements Parcelable {
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

    public void setThumbnail(String thumbnail) {
        _thumbnail = thumbnail;
    }

    public String getThumbnail() {
        return _thumbnail;
    }

    public File thumbnail(String thumbnail) {
        _thumbnail = thumbnail;
        return this;
    }

    public void setSizeBytes(Integer sizeBytes) {
        _sizeBytes = sizeBytes;
    }

    public Integer getSizeBytes() {
        return _sizeBytes;
    }

    public File sizeBytes(Integer sizeBytes) {
        _sizeBytes = sizeBytes;
        return this;
    }

    public void setMime(String mime) {
        _mime = mime;
    }

    public String getMime() {
        return _mime;
    }

    public File mime(String mime) {
        _mime = mime;
        return this;
    }

    public void setPreviewFull(String previewFull) {
        _previewFull = previewFull;
    }

    public String getPreviewFull() {
        return _previewFull;
    }

    public File previewFull(String previewFull) {
        _previewFull = previewFull;
        return this;
    }

    public void setName(String name) {
        _name = name;
    }

    public String getName() {
        return _name;
    }

    public File name(String name) {
        _name = name;
        return this;
    }

    public void setIcon(String icon) {
        _icon = icon;
    }

    public String getIcon() {
        return _icon;
    }

    public File icon(String icon) {
        _icon = icon;
        return this;
    }

    public void setLink(String link) {
        _link = link;
    }

    public String getLink() {
        return _link;
    }

    public File link(String link) {
        _link = link;
        return this;
    }

    public void setDescription(String description) {
        _description = description;
    }

    public String getDescription() {
        return _description;
    }

    public File description(String description) {
        _description = description;
        return this;
    }

    public void setStorage(StorageEnum storage) {
        _storage = storage;
    }

    public StorageEnum getStorage() {
        return _storage;
    }

    public File storage(StorageEnum storage) {
        _storage = storage;
        return this;
    }

    public void setType(TypeEnum type) {
        _type = type;
    }

    public TypeEnum getType() {
        return _type;
    }

    public File type(TypeEnum type) {
        _type = type;
        return this;
    }

    /*-*****************************-*/
    /*-             Json            -*/
    /*-*****************************-*/
    public static File[] fromJsonArray(JsonArray array) {
        File[] list = new File[array.size()];
        for (int i = 0; i < array.size(); i++) {
            list[i] = fromJson(array.getJsonObject(i));
        }
        return list;
    }

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

    /*-*********************************************-*/
    /*-			Parcelable Implementation           -*/
    /*-*********************************************-*/
    public static final Parcelable.Creator<File> CREATOR = new Parcelable.Creator<File>() {

        @Override
        public File createFromParcel(Parcel source) {
            try {
                return File.fromJson((JsonObject) source.readParcelable(JsonObject.class.getClassLoader()));
            } catch (Exception ex) {
                Log.v(TAG, ex);
                return null;
            }
        }

        @Override
        public File[] newArray(int size) {
            return new File[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(toJson(), flags);
    }
}
