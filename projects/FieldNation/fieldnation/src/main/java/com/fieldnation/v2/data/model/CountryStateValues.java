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

public class CountryStateValues implements Parcelable {
    private static final String TAG = "CountryStateValues";

    @Json(name = "label")
    private String _label;

    @Json(name = "value")
    private String _value;

    public CountryStateValues() {
    }

    public void setLabel(String label) {
        _label = label;
    }

    public String getLabel() {
        return _label;
    }

    public CountryStateValues label(String label) {
        _label = label;
        return this;
    }

    public void setValue(String value) {
        _value = value;
    }

    public String getValue() {
        return _value;
    }

    public CountryStateValues value(String value) {
        _value = value;
        return this;
    }

    /*-*****************************-*/
    /*-             Json            -*/
    /*-*****************************-*/
    public static CountryStateValues[] fromJsonArray(JsonArray array) {
        CountryStateValues[] list = new CountryStateValues[array.size()];
        for (int i = 0; i < array.size(); i++) {
            list[i] = fromJson(array.getJsonObject(i));
        }
        return list;
    }

    public static CountryStateValues fromJson(JsonObject obj) {
        try {
            return Unserializer.unserializeObject(CountryStateValues.class, obj);
        } catch (Exception ex) {
            Log.v(TAG, TAG, ex);
            return null;
        }
    }

    public JsonObject toJson() {
        return toJson(this);
    }

    public static JsonObject toJson(CountryStateValues countryStateValues) {
        try {
            return Serializer.serializeObject(countryStateValues);
        } catch (Exception ex) {
            Log.v(TAG, TAG, ex);
            return null;
        }
    }

    /*-*********************************************-*/
    /*-			Parcelable Implementation           -*/
    /*-*********************************************-*/
    public static final Parcelable.Creator<CountryStateValues> CREATOR = new Parcelable.Creator<CountryStateValues>() {

        @Override
        public CountryStateValues createFromParcel(Parcel source) {
            try {
                return CountryStateValues.fromJson((JsonObject) source.readParcelable(JsonObject.class.getClassLoader()));
            } catch (Exception ex) {
                Log.v(TAG, ex);
                return null;
            }
        }

        @Override
        public CountryStateValues[] newArray(int size) {
            return new CountryStateValues[size];
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
