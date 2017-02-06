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

public class CountryAddress2 implements Parcelable {
    private static final String TAG = "CountryAddress2";

    @Json(name = "label")
    private String _label;

    @Json(name = "required")
    private Boolean _required;

    public CountryAddress2() {
    }

    public void setLabel(String label) {
        _label = label;
    }

    public String getLabel() {
        return _label;
    }

    public CountryAddress2 label(String label) {
        _label = label;
        return this;
    }

    public void setRequired(Boolean required) {
        _required = required;
    }

    public Boolean getRequired() {
        return _required;
    }

    public CountryAddress2 required(Boolean required) {
        _required = required;
        return this;
    }

    /*-*****************************-*/
    /*-             Json            -*/
    /*-*****************************-*/
    public static CountryAddress2[] fromJsonArray(JsonArray array) {
        CountryAddress2[] list = new CountryAddress2[array.size()];
        for (int i = 0; i < array.size(); i++) {
            list[i] = fromJson(array.getJsonObject(i));
        }
        return list;
    }

    public static CountryAddress2 fromJson(JsonObject obj) {
        try {
            return Unserializer.unserializeObject(CountryAddress2.class, obj);
        } catch (Exception ex) {
            Log.v(TAG, TAG, ex);
            return null;
        }
    }

    public JsonObject toJson() {
        return toJson(this);
    }

    public static JsonObject toJson(CountryAddress2 countryAddress2) {
        try {
            return Serializer.serializeObject(countryAddress2);
        } catch (Exception ex) {
            Log.v(TAG, TAG, ex);
            return null;
        }
    }

    /*-*********************************************-*/
    /*-			Parcelable Implementation           -*/
    /*-*********************************************-*/
    public static final Parcelable.Creator<CountryAddress2> CREATOR = new Parcelable.Creator<CountryAddress2>() {

        @Override
        public CountryAddress2 createFromParcel(Parcel source) {
            try {
                return CountryAddress2.fromJson((JsonObject) source.readParcelable(JsonObject.class.getClassLoader()));
            } catch (Exception ex) {
                Log.v(TAG, ex);
                return null;
            }
        }

        @Override
        public CountryAddress2[] newArray(int size) {
            return new CountryAddress2[size];
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
