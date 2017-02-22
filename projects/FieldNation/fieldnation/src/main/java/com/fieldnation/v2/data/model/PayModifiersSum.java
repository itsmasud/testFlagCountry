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

public class PayModifiersSum implements Parcelable {
    private static final String TAG = "PayModifiersSum";

    @Json(name = "all")
    private Double _all;

    @Json(name = "charged")
    private Double _charged;

    @Json(name = "uncharged")
    private Double _uncharged;

    @Source
    private JsonObject SOURCE = new JsonObject();

    public PayModifiersSum() {
    }

    public void setAll(Double all) throws ParseException {
        _all = all;
        SOURCE.put("all", all);
    }

    public Double getAll() {
        return _all;
    }

    public PayModifiersSum all(Double all) throws ParseException {
        _all = all;
        SOURCE.put("all", all);
        return this;
    }

    public void setCharged(Double charged) throws ParseException {
        _charged = charged;
        SOURCE.put("charged", charged);
    }

    public Double getCharged() {
        return _charged == null ? 0.0 : _charged;
    }

    public PayModifiersSum charged(Double charged) throws ParseException {
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

    public PayModifiersSum uncharged(Double uncharged) throws ParseException {
        _uncharged = uncharged;
        SOURCE.put("uncharged", uncharged);
        return this;
    }

    /*-*****************************-*/
    /*-             Json            -*/
    /*-*****************************-*/
    public static JsonArray toJsonArray(PayModifiersSum[] array) {
        JsonArray list = new JsonArray();
        for (PayModifiersSum item : array) {
            list.add(item.getJson());
        }
        return list;
    }

    public static PayModifiersSum[] fromJsonArray(JsonArray array) {
        PayModifiersSum[] list = new PayModifiersSum[array.size()];
        for (int i = 0; i < array.size(); i++) {
            list[i] = fromJson(array.getJsonObject(i));
        }
        return list;
    }

    public static PayModifiersSum fromJson(JsonObject obj) {
        try {
            return Unserializer.unserializeObject(PayModifiersSum.class, obj);
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
    public static final Parcelable.Creator<PayModifiersSum> CREATOR = new Parcelable.Creator<PayModifiersSum>() {

        @Override
        public PayModifiersSum createFromParcel(Parcel source) {
            try {
                return PayModifiersSum.fromJson((JsonObject) source.readParcelable(JsonObject.class.getClassLoader()));
            } catch (Exception ex) {
                Log.v(TAG, ex);
                return null;
            }
        }

        @Override
        public PayModifiersSum[] newArray(int size) {
            return new PayModifiersSum[size];
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
