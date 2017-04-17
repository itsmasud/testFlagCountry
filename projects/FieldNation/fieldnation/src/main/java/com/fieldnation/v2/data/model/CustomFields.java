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
import com.fieldnation.fntools.misc;

import java.text.ParseException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by dmgen from swagger.
 */

public class CustomFields implements Parcelable {
    private static final String TAG = "CustomFields";

    @Json(name = "metadata")
    private ListEnvelope _metadata;

    @Json(name = "results")
    private CustomFieldCategory[] _results;

    @Source
    private JsonObject SOURCE;

    public CustomFields() {
        SOURCE = new JsonObject();
    }

    public CustomFields(JsonObject obj) {
        SOURCE = obj;
    }

    public void setMetadata(ListEnvelope metadata) throws ParseException {
        _metadata = metadata;
        SOURCE.put("metadata", metadata.getJson());
    }

    public ListEnvelope getMetadata() {
        try {
            if (_metadata == null && SOURCE.has("metadata") && SOURCE.get("metadata") != null)
                _metadata = ListEnvelope.fromJson(SOURCE.getJsonObject("metadata"));
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        if (_metadata != null && _metadata.isSet())
        return _metadata;

        return null;
    }

    public CustomFields metadata(ListEnvelope metadata) throws ParseException {
        _metadata = metadata;
        SOURCE.put("metadata", metadata.getJson());
        return this;
    }

    public void setResults(CustomFieldCategory[] results) throws ParseException {
        _results = results;
        SOURCE.put("results", CustomFieldCategory.toJsonArray(results));
    }

    public CustomFieldCategory[] getResults() {
        try {
            if (_results != null)
                return _results;

            if (SOURCE.has("results") && SOURCE.get("results") != null) {
                _results = CustomFieldCategory.fromJsonArray(SOURCE.getJsonArray("results"));
            }

        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _results;
    }

    public CustomFields results(CustomFieldCategory[] results) throws ParseException {
        _results = results;
        SOURCE.put("results", CustomFieldCategory.toJsonArray(results), true);
        return this;
    }

    /*-*****************************-*/
    /*-             Json            -*/
    /*-*****************************-*/
    public static JsonArray toJsonArray(CustomFields[] array) {
        JsonArray list = new JsonArray();
        for (CustomFields item : array) {
            list.add(item.getJson());
        }
        return list;
    }

    public static CustomFields[] fromJsonArray(JsonArray array) {
        CustomFields[] list = new CustomFields[array.size()];
        for (int i = 0; i < array.size(); i++) {
            list[i] = fromJson(array.getJsonObject(i));
        }
        return list;
    }

    public static CustomFields fromJson(JsonObject obj) {
        try {
            return new CustomFields(obj);
        } catch (Exception ex) {
            Log.v(TAG, ex);
            return null;
        }
    }

    public JsonObject getJson() {
        return SOURCE;
    }

    /*-*********************************************-*/
    /*-			Parcelable Implementation           -*/
    /*-*********************************************-*/
    public static final Parcelable.Creator<CustomFields> CREATOR = new Parcelable.Creator<CustomFields>() {

        @Override
        public CustomFields createFromParcel(Parcel source) {
            try {
                return CustomFields.fromJson((JsonObject) source.readParcelable(JsonObject.class.getClassLoader()));
            } catch (Exception ex) {
                Log.v(TAG, ex);
                return null;
            }
        }

        @Override
        public CustomFields[] newArray(int size) {
            return new CustomFields[size];
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

    /*-*****************************-*/
    /*-         Human Code          -*/
    /*-*****************************-*/

    public boolean isSet() {
        return true;
    }
}
