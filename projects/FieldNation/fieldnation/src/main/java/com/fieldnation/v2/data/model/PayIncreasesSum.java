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
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by dmgen from swagger.
 */

public class PayIncreasesSum implements Parcelable {
    private static final String TAG = "PayIncreasesSum";

    @Json(name = "all")
    private Double _all;

    @Json(name = "charged")
    private Double _charged;

    @Json(name = "uncharged")
    private Double _uncharged;

    @Source
    private JsonObject SOURCE = new JsonObject();

    public PayIncreasesSum() {
    }

    public void setAll(Double all) throws ParseException {
        _all = all;
        SOURCE.put("all", all);
    }

    public Double getAll() {
        return _all;
    }

    public PayIncreasesSum all(Double all) throws ParseException {
        _all = all;
        SOURCE.put("all", all);
        return this;
    }

    public void setCharged(Double charged) throws ParseException {
        _charged = charged;
        SOURCE.put("charged", charged);
    }

    public Double getCharged() {
        return _charged;
    }

    public PayIncreasesSum charged(Double charged) throws ParseException {
        _charged = charged;
        SOURCE.put("charged", charged);
        return this;
    }

    public void setUncharged(Double uncharged) throws ParseException {
        _uncharged = uncharged;
        SOURCE.put("uncharged", uncharged);
    }

    public Double getUncharged() {
        return _uncharged;
    }

    public PayIncreasesSum uncharged(Double uncharged) throws ParseException {
        _uncharged = uncharged;
        SOURCE.put("uncharged", uncharged);
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
            return Unserializer.unserializeObject(PayIncreasesSum.class, obj);
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
    public static final Creator<PayIncreasesSum> CREATOR = new Creator<PayIncreasesSum>() {

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
}
