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

public class PayIncreases implements Parcelable {
    private static final String TAG = "PayIncreases";

    @Json(name = "metadata")
    private ListEnvelope _metadata;

    @Json(name = "results")
    private PayIncrease[] _results;

    @Json(name = "sum")
    private PayIncreasesSum _sum;

    @Source
    private JsonObject SOURCE;

    public PayIncreases() {
        SOURCE = new JsonObject();
    }

    public PayIncreases(JsonObject obj) {
        SOURCE = obj;
    }

    public void setMetadata(ListEnvelope metadata) throws ParseException {
        _metadata = metadata;
        SOURCE.put("metadata", metadata.getJson());
    }

    public ListEnvelope getMetadata() {
        try {
            if (_metadata != null)
                return _metadata;

            if (SOURCE.has("metadata") && SOURCE.get("metadata") != null)
                _metadata = ListEnvelope.fromJson(SOURCE.getJsonObject("metadata"));

        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _metadata;
    }

    public PayIncreases metadata(ListEnvelope metadata) throws ParseException {
        _metadata = metadata;
        SOURCE.put("metadata", metadata.getJson());
        return this;
    }

    public void setResults(PayIncrease[] results) throws ParseException {
        _results = results;
        SOURCE.put("results", PayIncrease.toJsonArray(results));
    }

    public PayIncrease[] getResults() {
        try {
            if (_results != null)
                return _results;

            if (SOURCE.has("results") && SOURCE.get("results") != null) {
                _results = PayIncrease.fromJsonArray(SOURCE.getJsonArray("results"));
            }

        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _results;
    }

    public PayIncreases results(PayIncrease[] results) throws ParseException {
        _results = results;
        SOURCE.put("results", PayIncrease.toJsonArray(results), true);
        return this;
    }

    public void setSum(PayIncreasesSum sum) throws ParseException {
        _sum = sum;
        SOURCE.put("sum", sum.getJson());
    }

    public PayIncreasesSum getSum() {
        try {
            if (_sum != null)
                return _sum;

            if (SOURCE.has("sum") && SOURCE.get("sum") != null)
                _sum = PayIncreasesSum.fromJson(SOURCE.getJsonObject("sum"));

        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _sum;
    }

    public PayIncreases sum(PayIncreasesSum sum) throws ParseException {
        _sum = sum;
        SOURCE.put("sum", sum.getJson());
        return this;
    }

    /*-*****************************-*/
    /*-             Json            -*/
    /*-*****************************-*/
    public static JsonArray toJsonArray(PayIncreases[] array) {
        JsonArray list = new JsonArray();
        for (PayIncreases item : array) {
            list.add(item.getJson());
        }
        return list;
    }

    public static PayIncreases[] fromJsonArray(JsonArray array) {
        PayIncreases[] list = new PayIncreases[array.size()];
        for (int i = 0; i < array.size(); i++) {
            list[i] = fromJson(array.getJsonObject(i));
        }
        return list;
    }

    public static PayIncreases fromJson(JsonObject obj) {
        try {
            return new PayIncreases(obj);
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
    public static final Parcelable.Creator<PayIncreases> CREATOR = new Parcelable.Creator<PayIncreases>() {

        @Override
        public PayIncreases createFromParcel(Parcel source) {
            try {
                return PayIncreases.fromJson((JsonObject) source.readParcelable(JsonObject.class.getClassLoader()));
            } catch (Exception ex) {
                Log.v(TAG, ex);
                return null;
            }
        }

        @Override
        public PayIncreases[] newArray(int size) {
            return new PayIncreases[size];
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
