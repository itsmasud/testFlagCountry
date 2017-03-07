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

public class CountryAddress1 implements Parcelable {
    private static final String TAG = "CountryAddress1";

    @Json(name = "label")
    private String _label;

    @Json(name = "required")
    private Boolean _required;

    @Source
    private JsonObject SOURCE;

    public CountryAddress1() {
        SOURCE = new JsonObject();
    }

    public CountryAddress1(JsonObject obj) {
        SOURCE = obj;
    }

    public void setLabel(String label) throws ParseException {
        _label = label;
        SOURCE.put("label", label);
    }

    public String getLabel() {
        try {
            if (_label != null)
                return _label;

            if (SOURCE.has("label") && SOURCE.get("label") != null)
                _label = SOURCE.getString("label");

        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _label;
    }

    public CountryAddress1 label(String label) throws ParseException {
        _label = label;
        SOURCE.put("label", label);
        return this;
    }

    public void setRequired(Boolean required) throws ParseException {
        _required = required;
        SOURCE.put("required", required);
    }

    public Boolean getRequired() {
        try {
            if (_required != null)
                return _required;

            if (SOURCE.has("required") && SOURCE.get("required") != null)
                _required = SOURCE.getBoolean("required");

        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _required;
    }

    public CountryAddress1 required(Boolean required) throws ParseException {
        _required = required;
        SOURCE.put("required", required);
        return this;
    }

    /*-*****************************-*/
    /*-             Json            -*/
    /*-*****************************-*/
    public static JsonArray toJsonArray(CountryAddress1[] array) {
        JsonArray list = new JsonArray();
        for (CountryAddress1 item : array) {
            list.add(item.getJson());
        }
        return list;
    }

    public static CountryAddress1[] fromJsonArray(JsonArray array) {
        CountryAddress1[] list = new CountryAddress1[array.size()];
        for (int i = 0; i < array.size(); i++) {
            list[i] = fromJson(array.getJsonObject(i));
        }
        return list;
    }

    public static CountryAddress1 fromJson(JsonObject obj) {
        try {
            return new CountryAddress1(obj);
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
    public static final Parcelable.Creator<CountryAddress1> CREATOR = new Parcelable.Creator<CountryAddress1>() {

        @Override
        public CountryAddress1 createFromParcel(Parcel source) {
            try {
                return CountryAddress1.fromJson((JsonObject) source.readParcelable(JsonObject.class.getClassLoader()));
            } catch (Exception ex) {
                Log.v(TAG, ex);
                return null;
            }
        }

        @Override
        public CountryAddress1[] newArray(int size) {
            return new CountryAddress1[size];
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
