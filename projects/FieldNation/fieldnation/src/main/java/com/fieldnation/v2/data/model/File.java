package com.fieldnation.v2.data.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.fieldnation.fnjson.JsonArray;
import com.fieldnation.fnjson.JsonObject;
import com.fieldnation.fnjson.Serializer;
import com.fieldnation.fnjson.Unserializer;
import com.fieldnation.fnjson.annotations.Json;
import com.fieldnation.fnjson.annotations.Source;
import com.fieldnation.fnlog.Log;

import java.text.ParseException;

/**
 * Created by dmgen from swagger.
 */

public class File implements Parcelable {
    private static final String TAG = "File";

    @Json(name = "description")
    private String _description;

    @Json(name = "icon")
    private String _icon;

    @Json(name = "link")
    private String _link;

    @Json(name = "mime")
    private String _mime;

    @Json(name = "name")
    private String _name;

    @Json(name = "preview_full")
    private String _previewFull;

    @Json(name = "size_bytes")
    private Integer _sizeBytes;

    @Json(name = "storage")
    private StorageEnum _storage;

    @Json(name = "thumbnail")
    private String _thumbnail;

    @Json(name = "type")
    private TypeEnum _type;

    @Source
    private JsonObject SOURCE = new JsonObject();

    public File() {
    }

    public void setDescription(String description) throws ParseException {
        _description = description;
        SOURCE.put("description", description);
    }

    public String getDescription() {
        return _description;
    }

    public File description(String description) throws ParseException {
        _description = description;
        SOURCE.put("description", description);
        return this;
    }

    public void setIcon(String icon) throws ParseException {
        _icon = icon;
        SOURCE.put("icon", icon);
    }

    public String getIcon() {
        return _icon;
    }

    public File icon(String icon) throws ParseException {
        _icon = icon;
        SOURCE.put("icon", icon);
        return this;
    }

    public void setLink(String link) throws ParseException {
        _link = link;
        SOURCE.put("link", link);
    }

    public String getLink() {
        return _link;
    }

    public File link(String link) throws ParseException {
        _link = link;
        SOURCE.put("link", link);
        return this;
    }

    public void setMime(String mime) throws ParseException {
        _mime = mime;
        SOURCE.put("mime", mime);
    }

    public String getMime() {
        return _mime;
    }

    public File mime(String mime) throws ParseException {
        _mime = mime;
        SOURCE.put("mime", mime);
        return this;
    }

    public void setName(String name) throws ParseException {
        _name = name;
        SOURCE.put("name", name);
    }

    public String getName() {
        return _name;
    }

    public File name(String name) throws ParseException {
        _name = name;
        SOURCE.put("name", name);
        return this;
    }

    public void setPreviewFull(String previewFull) throws ParseException {
        _previewFull = previewFull;
        SOURCE.put("preview_full", previewFull);
    }

    public String getPreviewFull() {
        return _previewFull;
    }

    public File previewFull(String previewFull) throws ParseException {
        _previewFull = previewFull;
        SOURCE.put("preview_full", previewFull);
        return this;
    }

    public void setSizeBytes(Integer sizeBytes) throws ParseException {
        _sizeBytes = sizeBytes;
        SOURCE.put("size_bytes", sizeBytes);
    }

    public Integer getSizeBytes() {
        return _sizeBytes;
    }

    public File sizeBytes(Integer sizeBytes) throws ParseException {
        _sizeBytes = sizeBytes;
        SOURCE.put("size_bytes", sizeBytes);
        return this;
    }

    public void setStorage(StorageEnum storage) throws ParseException {
        _storage = storage;
        SOURCE.put("storage", storage.toString());
    }

    public StorageEnum getStorage() {
        return _storage;
    }

    public File storage(StorageEnum storage) throws ParseException {
        _storage = storage;
        SOURCE.put("storage", storage.toString());
        return this;
    }

    public void setThumbnail(String thumbnail) throws ParseException {
        _thumbnail = thumbnail;
        SOURCE.put("thumbnail", thumbnail);
    }

    public String getThumbnail() {
        return _thumbnail;
    }

    public File thumbnail(String thumbnail) throws ParseException {
        _thumbnail = thumbnail;
        SOURCE.put("thumbnail", thumbnail);
        return this;
    }

    public void setType(TypeEnum type) throws ParseException {
        _type = type;
        SOURCE.put("type", type.toString());
    }

    public TypeEnum getType() {
        return _type;
    }

    public File type(TypeEnum type) throws ParseException {
        _type = type;
        SOURCE.put("type", type.toString());
        return this;
    }

    /*-******************************-*/
    /*-             Enums            -*/
    /*-******************************-*/
    public enum StorageEnum {
        @Json(name = "s3")
        S3("s3");

        private String value;

        StorageEnum(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return String.valueOf(value);
        }
    }

    public enum TypeEnum {
        @Json(name = "file")
        FILE("file"),
        @Json(name = "link")
        LINK("link");

        private String value;

        TypeEnum(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return String.valueOf(value);
        }
    }

    /*-*****************************-*/
    /*-             Json            -*/
    /*-*****************************-*/
    public static JsonArray toJsonArray(File[] array) {
        JsonArray list = new JsonArray();
        for (File item : array) {
            list.add(item.getJson());
        }
        return list;
    }

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

    public JsonObject getJson() {
        return SOURCE;
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
        dest.writeParcelable(getJson(), flags);
    }
}
