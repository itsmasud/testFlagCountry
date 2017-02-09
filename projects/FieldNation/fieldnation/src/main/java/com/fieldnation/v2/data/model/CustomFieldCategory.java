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
 * Created by dmgen from swagger.
 */

public class CustomFieldCategory implements Parcelable {
    private static final String TAG = "CustomFieldCategory";

    @Json(name = "id")
    private Integer _id;

    @Json(name = "metadata")
    private ListEnvelope _metadata;

    @Json(name = "name")
    private String _name;

    @Json(name = "results")
    private CustomField[] _results;

    @Json(name = "role")
    private String _role;

    public CustomFieldCategory() {
    }

    public void setId(Integer id) {
        _id = id;
    }

    public Integer getId() {
        return _id;
    }

    public CustomFieldCategory id(Integer id) {
        _id = id;
        return this;
    }

    public void setMetadata(ListEnvelope metadata) {
        _metadata = metadata;
    }

    public ListEnvelope getMetadata() {
        return _metadata;
    }

    public CustomFieldCategory metadata(ListEnvelope metadata) {
        _metadata = metadata;
        return this;
    }

    public void setName(String name) {
        _name = name;
    }

    public String getName() {
        return _name;
    }

    public CustomFieldCategory name(String name) {
        _name = name;
        return this;
    }

    public void setResults(CustomField[] results) {
        _results = results;
    }

    public CustomField[] getResults() {
        return _results;
    }

    public CustomFieldCategory results(CustomField[] results) {
        _results = results;
        return this;
    }

    public void setRole(String role) {
        _role = role;
    }

    public String getRole() {
        return _role;
    }

    public CustomFieldCategory role(String role) {
        _role = role;
        return this;
    }

    /*-*****************************-*/
    /*-             Json            -*/
    /*-*****************************-*/
    public static CustomFieldCategory[] fromJsonArray(JsonArray array) {
        CustomFieldCategory[] list = new CustomFieldCategory[array.size()];
        for (int i = 0; i < array.size(); i++) {
            list[i] = fromJson(array.getJsonObject(i));
        }
        return list;
    }

    public static CustomFieldCategory fromJson(JsonObject obj) {
        try {
            return Unserializer.unserializeObject(CustomFieldCategory.class, obj);
        } catch (Exception ex) {
            Log.v(TAG, TAG, ex);
            return null;
        }
    }

    public JsonObject toJson() {
        return toJson(this);
    }

    public static JsonObject toJson(CustomFieldCategory customFieldCategory) {
        try {
            return Serializer.serializeObject(customFieldCategory);
        } catch (Exception ex) {
            Log.v(TAG, TAG, ex);
            return null;
        }
    }

    /*-*********************************************-*/
    /*-			Parcelable Implementation           -*/
    /*-*********************************************-*/
    public static final Parcelable.Creator<CustomFieldCategory> CREATOR = new Parcelable.Creator<CustomFieldCategory>() {

        @Override
        public CustomFieldCategory createFromParcel(Parcel source) {
            try {
                return CustomFieldCategory.fromJson((JsonObject) source.readParcelable(JsonObject.class.getClassLoader()));
            } catch (Exception ex) {
                Log.v(TAG, ex);
                return null;
            }
        }

        @Override
        public CustomFieldCategory[] newArray(int size) {
            return new CustomFieldCategory[size];
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
