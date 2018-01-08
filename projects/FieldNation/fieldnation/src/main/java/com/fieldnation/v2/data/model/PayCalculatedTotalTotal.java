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

public class PayCalculatedTotalTotal implements Parcelable {
    private static final String TAG = "PayCalculatedTotalTotal";

    @Json(name = "max")
    private Double _max;

    @Json(name = "min")
    private Double _min;

    @Source
    private JsonObject SOURCE;

    public PayCalculatedTotalTotal() {
        SOURCE = new JsonObject();
    }

    public PayCalculatedTotalTotal(JsonObject obj) {
        SOURCE = obj;
    }

    public void setMax(Double max) throws ParseException {
        _max = max;
        SOURCE.put("max", max);
    }

    public Double getMax() {
        try {
            if (_max == null && SOURCE.has("max") && SOURCE.get("max") != null)
                _max = SOURCE.getDouble("max");
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _max;
    }

    public PayCalculatedTotalTotal max(Double max) throws ParseException {
        _max = max;
        SOURCE.put("max", max);
        return this;
    }

    public void setMin(Double min) throws ParseException {
        _min = min;
        SOURCE.put("min", min);
    }

    public Double getMin() {
        try {
            if (_min == null && SOURCE.has("min") && SOURCE.get("min") != null)
                _min = SOURCE.getDouble("min");
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _min;
    }

    public PayCalculatedTotalTotal min(Double min) throws ParseException {
        _min = min;
        SOURCE.put("min", min);
        return this;
    }

    /*-*****************************-*/
    /*-             Json            -*/
    /*-*****************************-*/
    public static JsonArray toJsonArray(PayCalculatedTotalTotal[] array) {
        JsonArray list = new JsonArray();
        for (PayCalculatedTotalTotal item : array) {
            list.add(item.getJson());
        }
        return list;
    }

    public static PayCalculatedTotalTotal[] fromJsonArray(JsonArray array) {
        PayCalculatedTotalTotal[] list = new PayCalculatedTotalTotal[array.size()];
        for (int i = 0; i < array.size(); i++) {
            list[i] = fromJson(array.getJsonObject(i));
        }
        return list;
    }

    public static PayCalculatedTotalTotal fromJson(JsonObject obj) {
        try {
            return new PayCalculatedTotalTotal(obj);
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
    public static final Parcelable.Creator<PayCalculatedTotalTotal> CREATOR = new Parcelable.Creator<PayCalculatedTotalTotal>() {

        @Override
        public PayCalculatedTotalTotal createFromParcel(Parcel source) {
            try {
                return PayCalculatedTotalTotal.fromJson((JsonObject) source.readParcelable(JsonObject.class.getClassLoader()));
            } catch (Exception ex) {
                Log.v(TAG, ex);
                return null;
            }
        }

        @Override
        public PayCalculatedTotalTotal[] newArray(int size) {
            return new PayCalculatedTotalTotal[size];
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

}
