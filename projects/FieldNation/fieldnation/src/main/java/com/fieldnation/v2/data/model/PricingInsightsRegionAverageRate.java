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

public class PricingInsightsRegionAverageRate implements Parcelable {
    private static final String TAG = "PricingInsightsRegionAverageRate";

    @Json(name = "first_quartile")
    private Double _firstQuartile;

    @Json(name = "median")
    private Double _median;

    @Json(name = "third_quartile")
    private Double _thirdQuartile;

    @Source
    private JsonObject SOURCE = new JsonObject();

    public PricingInsightsRegionAverageRate() {
    }

    public void setFirstQuartile(Double firstQuartile) throws ParseException {
        _firstQuartile = firstQuartile;
        SOURCE.put("first_quartile", firstQuartile);
    }

    public Double getFirstQuartile() {
        return _firstQuartile;
    }

    public PricingInsightsRegionAverageRate firstQuartile(Double firstQuartile) throws ParseException {
        _firstQuartile = firstQuartile;
        SOURCE.put("first_quartile", firstQuartile);
        return this;
    }

    public void setMedian(Double median) throws ParseException {
        _median = median;
        SOURCE.put("median", median);
    }

    public Double getMedian() {
        return _median;
    }

    public PricingInsightsRegionAverageRate median(Double median) throws ParseException {
        _median = median;
        SOURCE.put("median", median);
        return this;
    }

    public void setThirdQuartile(Double thirdQuartile) throws ParseException {
        _thirdQuartile = thirdQuartile;
        SOURCE.put("third_quartile", thirdQuartile);
    }

    public Double getThirdQuartile() {
        return _thirdQuartile;
    }

    public PricingInsightsRegionAverageRate thirdQuartile(Double thirdQuartile) throws ParseException {
        _thirdQuartile = thirdQuartile;
        SOURCE.put("third_quartile", thirdQuartile);
        return this;
    }

    /*-*****************************-*/
    /*-             Json            -*/
    /*-*****************************-*/
    public static JsonArray toJsonArray(PricingInsightsRegionAverageRate[] array) {
        JsonArray list = new JsonArray();
        for (PricingInsightsRegionAverageRate item : array) {
            list.add(item.getJson());
        }
        return list;
    }

    public static PricingInsightsRegionAverageRate[] fromJsonArray(JsonArray array) {
        PricingInsightsRegionAverageRate[] list = new PricingInsightsRegionAverageRate[array.size()];
        for (int i = 0; i < array.size(); i++) {
            list[i] = fromJson(array.getJsonObject(i));
        }
        return list;
    }

    public static PricingInsightsRegionAverageRate fromJson(JsonObject obj) {
        try {
            return Unserializer.unserializeObject(PricingInsightsRegionAverageRate.class, obj);
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
    public static final Parcelable.Creator<PricingInsightsRegionAverageRate> CREATOR = new Parcelable.Creator<PricingInsightsRegionAverageRate>() {

        @Override
        public PricingInsightsRegionAverageRate createFromParcel(Parcel source) {
            try {
                return PricingInsightsRegionAverageRate.fromJson((JsonObject) source.readParcelable(JsonObject.class.getClassLoader()));
            } catch (Exception ex) {
                Log.v(TAG, ex);
                return null;
            }
        }

        @Override
        public PricingInsightsRegionAverageRate[] newArray(int size) {
            return new PricingInsightsRegionAverageRate[size];
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
