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

public class CustomFields implements Parcelable {
    private static final String TAG = "CustomFields";

    @Json(name = "metadata")
    private ListEnvelope _metadata;

    @Json(name = "results")
    private CustomFieldCategory[] _results;

    public CustomFields() {
    }

    public void setMetadata(ListEnvelope metadata) {
        _metadata = metadata;
    }

    public ListEnvelope getMetadata() {
        return _metadata;
    }

    public CustomFields metadata(ListEnvelope metadata) {
        _metadata = metadata;
        return this;
    }

    public void setResults(CustomFieldCategory[] results) {
        _results = results;
    }

    public CustomFieldCategory[] getResults() {
        return _results;
    }

    public CustomFields results(CustomFieldCategory[] results) {
        _results = results;
        return this;
    }

    /*-*****************************-*/
    /*-             Json            -*/
    /*-*****************************-*/
    public static CustomFields[] fromJsonArray(JsonArray array) {
        CustomFields[] list = new CustomFields[array.size()];
        for (int i = 0; i < array.size(); i++) {
            list[i] = fromJson(array.getJsonObject(i));
        }
        return list;
    }

    public static CustomFields fromJson(JsonObject obj) {
        try {
            return Unserializer.unserializeObject(CustomFields.class, obj);
        } catch (Exception ex) {
            Log.v(TAG, TAG, ex);
            return null;
        }
    }

    public JsonObject toJson() {
        return toJson(this);
    }

    public static JsonObject toJson(CustomFields customFields) {
        try {
            return Serializer.serializeObject(customFields);
        } catch (Exception ex) {
            Log.v(TAG, TAG, ex);
            return null;
        }
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
        dest.writeParcelable(toJson(), flags);
    }
}
