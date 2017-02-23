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

public class CountryState implements Parcelable {
    private static final String TAG = "CountryState";

    @Json(name = "label")
    private String _label;

    @Json(name = "required")
    private Boolean _required;

    @Json(name = "values")
    private CountryStateValues[] _values;

    @Source
    private JsonObject SOURCE;

    public CountryState() {
        SOURCE = new JsonObject();
    }

    public CountryState(JsonObject obj) {
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

    public CountryState label(String label) throws ParseException {
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

    public CountryState required(Boolean required) throws ParseException {
        _required = required;
        SOURCE.put("required", required);
        return this;
    }

    public void setValues(CountryStateValues[] values) throws ParseException {
        _values = values;
        SOURCE.put("values", CountryStateValues.toJsonArray(values));
    }

    public CountryStateValues[] getValues() {
        try {
            if (_values != null)
                return _values;

            if (SOURCE.has("values") && SOURCE.get("values") != null) {
                _values = CountryStateValues.fromJsonArray(SOURCE.getJsonArray("values"));
            }

        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _values;
    }

    public CountryState values(CountryStateValues[] values) throws ParseException {
        _values = values;
        SOURCE.put("values", CountryStateValues.toJsonArray(values), true);
        return this;
    }

    /*-*****************************-*/
    /*-             Json            -*/
    /*-*****************************-*/
    public static JsonArray toJsonArray(CountryState[] array) {
        JsonArray list = new JsonArray();
        for (CountryState item : array) {
            list.add(item.getJson());
        }
        return list;
    }

    public static CountryState[] fromJsonArray(JsonArray array) {
        CountryState[] list = new CountryState[array.size()];
        for (int i = 0; i < array.size(); i++) {
            list[i] = fromJson(array.getJsonObject(i));
        }
        return list;
    }

    public static CountryState fromJson(JsonObject obj) {
        try {
            return new CountryState(obj);
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
        dest.writeParcelable(getJson(), flags);
    }
}
