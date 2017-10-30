package com.fieldnation.v2.data.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.fieldnation.fnjson.JsonArray;
import com.fieldnation.fnjson.JsonObject;
import com.fieldnation.fnjson.annotations.Json;
import com.fieldnation.fnjson.annotations.Source;
import com.fieldnation.fnlog.Log;

import java.text.ParseException;

/**
 * Created by dmgen from swagger.
 */

public class PayIncreasesSum implements Parcelable {
    private static final String TAG = "PayIncreasesSum";

    @Json(name = "all")
    private Double _all;

    @Source
    private JsonObject SOURCE;

    public PayIncreasesSum() {
        SOURCE = new JsonObject();
    }

    public PayIncreasesSum(JsonObject obj) {
        SOURCE = obj;
    }

    public void setAll(Double all) throws ParseException {
        _all = all;
        SOURCE.put("all", all);
    }

    public Double getAll() {
        try {
            if (_all == null && SOURCE.has("all") && SOURCE.get("all") != null)
                _all = SOURCE.getDouble("all");
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _all;
    }

    public PayIncreasesSum all(Double all) throws ParseException {
        _all = all;
        SOURCE.put("all", all);
        return this;
    }

    /*-*****************************-*/
    /*-             Json            -*/
    /*-*****************************-*/
    public static JsonArray toJsonArray(PayIncreasesSum[] array) {
        JsonArray list = new JsonArray();
        for (PayIncreasesSum item : array) {
            list.add(item.getJson());
        }
        return list;
    }

    public static PayIncreasesSum[] fromJsonArray(JsonArray array) {
        PayIncreasesSum[] list = new PayIncreasesSum[array.size()];
        for (int i = 0; i < array.size(); i++) {
            list[i] = fromJson(array.getJsonObject(i));
        }
        return list;
    }

    public static PayIncreasesSum fromJson(JsonObject obj) {
        try {
            return new PayIncreasesSum(obj);
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
    public static final Parcelable.Creator<PayIncreasesSum> CREATOR = new Parcelable.Creator<PayIncreasesSum>() {

        @Override
        public PayIncreasesSum createFromParcel(Parcel source) {
            try {
                return PayIncreasesSum.fromJson((JsonObject) source.readParcelable(JsonObject.class.getClassLoader()));
            } catch (Exception ex) {
                Log.v(TAG, ex);
                return null;
            }
        }

        @Override
        public PayIncreasesSum[] newArray(int size) {
            return new PayIncreasesSum[size];
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
