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
    private JsonObject SOURCE;

    public File() {
        SOURCE = new JsonObject();
    }

    public File(JsonObject obj) {
        SOURCE = obj;
    }

    public void setDescription(String description) throws ParseException {
        _description = description;
        SOURCE.put("description", description);
    }

    public String getDescription() {
        try {
            if (_description != null)
                return _description;

            if (SOURCE.has("description") && SOURCE.get("description") != null)
                _description = SOURCE.getString("description");

        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

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
        try {
            if (_icon != null)
                return _icon;

            if (SOURCE.has("icon") && SOURCE.get("icon") != null)
                _icon = SOURCE.getString("icon");

        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

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
        try {
            if (_link != null)
                return _link;

            if (SOURCE.has("link") && SOURCE.get("link") != null)
                _link = SOURCE.getString("link");

        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

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
        try {
            if (_mime != null)
                return _mime;

            if (SOURCE.has("mime") && SOURCE.get("mime") != null)
                _mime = SOURCE.getString("mime");

        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

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
        try {
            if (_name != null)
                return _name;

            if (SOURCE.has("name") && SOURCE.get("name") != null)
                _name = SOURCE.getString("name");

        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

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
        try {
            if (_previewFull != null)
                return _previewFull;

            if (SOURCE.has("preview_full") && SOURCE.get("preview_full") != null)
                _previewFull = SOURCE.getString("preview_full");

        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

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
        try {
            if (_sizeBytes != null)
                return _sizeBytes;

            if (SOURCE.has("size_bytes") && SOURCE.get("size_bytes") != null)
                _sizeBytes = SOURCE.getInt("size_bytes");

        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

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
        try {
            if (_storage != null)
                return _storage;

            if (SOURCE.has("storage") && SOURCE.get("storage") != null)
                _storage = StorageEnum.fromString(SOURCE.getString("storage"));

        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

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
        try {
            if (_thumbnail != null)
                return _thumbnail;

            if (SOURCE.has("thumbnail") && SOURCE.get("thumbnail") != null)
                _thumbnail = SOURCE.getString("thumbnail");

        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

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
        try {
            if (_type != null)
                return _type;

            if (SOURCE.has("type") && SOURCE.get("type") != null)
                _type = TypeEnum.fromString(SOURCE.getString("type"));

        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

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

        public static StorageEnum fromString(String value) {
            StorageEnum[] values = values();
            for (StorageEnum v : values) {
                if (v.value.equals(value))
                    return v;
            }
            return null;
        }

        public static StorageEnum[] fromJsonArray(JsonArray jsonArray) {
            StorageEnum[] list = new StorageEnum[jsonArray.size()];
            for (int i = 0; i < list.length; i++) {
                list[i] = fromString(jsonArray.getString(i));
            }
            return list;
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

        public static TypeEnum fromString(String value) {
            TypeEnum[] values = values();
            for (TypeEnum v : values) {
                if (v.value.equals(value))
                    return v;
            }
            return null;
        }

        public static TypeEnum[] fromJsonArray(JsonArray jsonArray) {
            TypeEnum[] list = new TypeEnum[jsonArray.size()];
            for (int i = 0; i < list.length; i++) {
                list[i] = fromString(jsonArray.getString(i));
            }
            return list;
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
            return new File(obj);
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
