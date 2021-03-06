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

public class PayAdditional implements Parcelable {
    private static final String TAG = "PayAdditional";

    @Json(name = "amount")
    private Double _amount;

    @Json(name = "units")
    private Double _units;

    @Source
    private JsonObject SOURCE;

    public PayAdditional() {
        SOURCE = new JsonObject();
    }

    public PayAdditional(JsonObject obj) {
        SOURCE = obj;
    }

    public void setAmount(Double amount) throws ParseException {
        _amount = amount;
        SOURCE.put("amount", amount);
    }

    public Double getAmount() {
        try {
            if (_amount == null && SOURCE.has("amount") && SOURCE.get("amount") != null)
                _amount = SOURCE.getDouble("amount");
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _amount;
    }

    public PayAdditional amount(Double amount) throws ParseException {
        _amount = amount;
        SOURCE.put("amount", amount);
        return this;
    }

    public void setUnits(Double units) throws ParseException {
        _units = units;
        SOURCE.put("units", units);
    }

    public Double getUnits() {
        try {
            if (_units == null && SOURCE.has("units") && SOURCE.get("units") != null)
                _units = SOURCE.getDouble("units");
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _units;
    }

    public PayAdditional units(Double units) throws ParseException {
        _units = units;
        SOURCE.put("units", units);
        return this;
    }

    /*-*****************************-*/
    /*-             Json            -*/
    /*-*****************************-*/
    public static JsonArray toJsonArray(PayAdditional[] array) {
        JsonArray list = new JsonArray();
        for (PayAdditional item : array) {
            list.add(item.getJson());
        }
        return list;
    }

    public static PayAdditional[] fromJsonArray(JsonArray array) {
        PayAdditional[] list = new PayAdditional[array.size()];
        for (int i = 0; i < array.size(); i++) {
            list[i] = fromJson(array.getJsonObject(i));
        }
        return list;
    }

    public static PayAdditional fromJson(JsonObject obj) {
        try {
            return new PayAdditional(obj);
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
    public static final Parcelable.Creator<PayAdditional> CREATOR = new Parcelable.Creator<PayAdditional>() {

        @Override
        public PayAdditional createFromParcel(Parcel source) {
            try {
                return PayAdditional.fromJson((JsonObject) source.readParcelable(JsonObject.class.getClassLoader()));
            } catch (Exception ex) {
                Log.v(TAG, ex);
                return null;
            }
        }

        @Override
        public PayAdditional[] newArray(int size) {
            return new PayAdditional[size];
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
        return getAmount() != null && getAmount() != 0;
    }
}
