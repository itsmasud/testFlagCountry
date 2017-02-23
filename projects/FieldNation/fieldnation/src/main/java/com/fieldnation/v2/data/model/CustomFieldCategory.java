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

    @Source
    private JsonObject SOURCE;

    public CustomFieldCategory() {
        SOURCE = new JsonObject();
    }

    public CustomFieldCategory(JsonObject obj) {
        SOURCE = obj;
    }

    public void setId(Integer id) throws ParseException {
        _id = id;
        SOURCE.put("id", id);
    }

    public Integer getId() {
        try {
            if (_id != null)
                return _id;

            if (SOURCE.has("id") && SOURCE.get("id") != null)
                _id = SOURCE.getInt("id");

        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _id;
    }

    public CustomFieldCategory id(Integer id) throws ParseException {
        _id = id;
        SOURCE.put("id", id);
        return this;
    }

    public void setMetadata(ListEnvelope metadata) throws ParseException {
        _metadata = metadata;
        SOURCE.put("metadata", metadata.getJson());
    }

    public ListEnvelope getMetadata() {
        try {
            if (_metadata != null)
                return _metadata;

            if (SOURCE.has("metadata") && SOURCE.get("metadata") != null)
                _metadata = ListEnvelope.fromJson(SOURCE.getJsonObject("metadata"));

        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _metadata;
    }

    public CustomFieldCategory metadata(ListEnvelope metadata) throws ParseException {
        _metadata = metadata;
        SOURCE.put("metadata", metadata.getJson());
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

    public CustomFieldCategory name(String name) throws ParseException {
        _name = name;
        SOURCE.put("name", name);
        return this;
    }

    public void setResults(CustomField[] results) throws ParseException {
        _results = results;
        SOURCE.put("results", CustomField.toJsonArray(results));
    }

    public CustomField[] getResults() {
        try {
            if (_results != null)
                return _results;

            if (SOURCE.has("results") && SOURCE.get("results") != null) {
                _results = CustomField.fromJsonArray(SOURCE.getJsonArray("results"));
            }

        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _results;
    }

    public CustomFieldCategory results(CustomField[] results) throws ParseException {
        _results = results;
        SOURCE.put("results", CustomField.toJsonArray(results), true);
        return this;
    }

    public void setRole(String role) throws ParseException {
        _role = role;
        SOURCE.put("role", role);
    }

    public String getRole() {
        try {
            if (_role != null)
                return _role;

            if (SOURCE.has("role") && SOURCE.get("role") != null)
                _role = SOURCE.getString("role");

        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _role;
    }

    public CustomFieldCategory role(String role) throws ParseException {
        _role = role;
        SOURCE.put("role", role);
        return this;
    }

    /*-*****************************-*/
    /*-             Json            -*/
    /*-*****************************-*/
    public static JsonArray toJsonArray(CustomFieldCategory[] array) {
        JsonArray list = new JsonArray();
        for (CustomFieldCategory item : array) {
            list.add(item.getJson());
        }
        return list;
    }

    public static CustomFieldCategory[] fromJsonArray(JsonArray array) {
        CustomFieldCategory[] list = new CustomFieldCategory[array.size()];
        for (int i = 0; i < array.size(); i++) {
            list[i] = fromJson(array.getJsonObject(i));
        }
        return list;
    }

    public static CustomFieldCategory fromJson(JsonObject obj) {
        try {
            return new CustomFieldCategory(obj);
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
        dest.writeParcelable(getJson(), flags);
    }
}
