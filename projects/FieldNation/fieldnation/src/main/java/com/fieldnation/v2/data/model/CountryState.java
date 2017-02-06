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

public class CountryState implements Parcelable {
    private static final String TAG = "CountryState";

    @Json(name = "values")
    private CountryStateValues[] _values;

    @Json(name = "label")
    private String _label;

    @Json(name = "required")
    private Boolean _required;

    public CountryState() {
    }

    public void setValues(CountryStateValues[] values) {
        _values = values;
    }

    public CountryStateValues[] getValues() {
        return _values;
    }

    public CountryState values(CountryStateValues[] values) {
        _values = values;
        return this;
    }

    public void setLabel(String label) {
        _label = label;
    }

    public String getLabel() {
        return _label;
    }

    public CountryState label(String label) {
        _label = label;
        return this;
    }

    public void setRequired(Boolean required) {
        _required = required;
    }

    public Boolean getRequired() {
        return _required;
    }

    public CountryState required(Boolean required) {
        _required = required;
        return this;
    }

    /*-*****************************-*/
    /*-             Json            -*/
    /*-*****************************-*/
    public static CountryState[] fromJsonArray(JsonArray array) {
        CountryState[] list = new CountryState[array.size()];
        for (int i = 0; i < array.size(); i++) {
            list[i] = fromJson(array.getJsonObject(i));
        }
        return list;
    }

    public static CountryState fromJson(JsonObject obj) {
        try {
            return Unserializer.unserializeObject(CountryState.class, obj);
        } catch (Exception ex) {
            Log.v(TAG, TAG, ex);
            return null;
        }
    }

    public JsonObject toJson() {
        return toJson(this);
    }

    public static JsonObject toJson(CountryState countryState) {
        try {
            return Serializer.serializeObject(countryState);
        } catch (Exception ex) {
            Log.v(TAG, TAG, ex);
            return null;
        }
    }

    /*-*********************************************-*/
    /*-			Parcelable Implementation           -*/
    /*-*********************************************-*/
    public static final Parcelable.Creator<CountryState> CREATOR = new Parcelable.Creator<CountryState>() {

        @Override
        public CountryState createFromParcel(Parcel source) {
            try {
                return CountryState.fromJson((JsonObject) source.readParcelable(JsonObject.class.getClassLoader()));
            } catch (Exception ex) {
                Log.v(TAG, ex);
                return null;
            }
        }

        @Override
        public CountryState[] newArray(int size) {
            return new CountryState[size];
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
