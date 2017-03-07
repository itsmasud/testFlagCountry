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

public class PayRange implements Parcelable {
    private static final String TAG = "PayRange";

    @Json(name = "max")
    private Double _max;

    @Json(name = "min")
    private Double _min;

    @Source
    private JsonObject SOURCE;

    public PayRange() {
        SOURCE = new JsonObject();
    }

    public PayRange(JsonObject obj) {
        SOURCE = obj;
    }

    public void setMax(Double max) throws ParseException {
        _max = max;
        SOURCE.put("max", max);
    }

    public Double getMax() {
        try {
            if (_max != null)
                return _max;

            if (SOURCE.has("max") && SOURCE.get("max") != null)
                _max = SOURCE.getDouble("max");

        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _max;
    }

    public PayRange max(Double max) throws ParseException {
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
            if (_min != null)
                return _min;

            if (SOURCE.has("min") && SOURCE.get("min") != null)
                _min = SOURCE.getDouble("min");

        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _min;
    }

    public PayRange min(Double min) throws ParseException {
        _min = min;
        SOURCE.put("min", min);
        return this;
    }

    /*-*****************************-*/
    /*-             Json            -*/
    /*-*****************************-*/
    public static JsonArray toJsonArray(PayRange[] array) {
        JsonArray list = new JsonArray();
        for (PayRange item : array) {
            list.add(item.getJson());
        }
        return list;
    }

    public static PayRange[] fromJsonArray(JsonArray array) {
        PayRange[] list = new PayRange[array.size()];
        for (int i = 0; i < array.size(); i++) {
            list[i] = fromJson(array.getJsonObject(i));
        }
        return list;
    }

    public static PayRange fromJson(JsonObject obj) {
        try {
            return new PayRange(obj);
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
    public static final Parcelable.Creator<PayRange> CREATOR = new Parcelable.Creator<PayRange>() {

        @Override
        public PayRange createFromParcel(Parcel source) {
            try {
                return PayRange.fromJson((JsonObject) source.readParcelable(JsonObject.class.getClassLoader()));
            } catch (Exception ex) {
                Log.v(TAG, ex);
                return null;
            }
        }

        @Override
        public PayRange[] newArray(int size) {
            return new PayRange[size];
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
