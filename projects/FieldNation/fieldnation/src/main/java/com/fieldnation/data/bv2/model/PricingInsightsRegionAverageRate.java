package com.fieldnation.data.bv2.model;
import android.os.Parcel;
import android.os.Parcelable;

import com.fieldnation.fnjson.JsonObject;
import com.fieldnation.fnjson.Serializer;
import com.fieldnation.fnjson.Unserializer;
import com.fieldnation.fnjson.annotations.Json;
import com.fieldnation.fnlog.Log;

/**
 * Created by dmgen from swagger on 1/26/17.
 */

public class PricingInsightsRegionAverageRate implements Parcelable {
    private static final String TAG = "PricingInsightsRegionAverageRate";

    @Json(name = "first_quartile")
    private Double _firstQuartile;

    @Json(name = "median")
    private Double _median;

    @Json(name = "third_quartile")
    private Double _thirdQuartile;

    public PricingInsightsRegionAverageRate() {
    }

    public void setFirstQuartile(Double firstQuartile) {
        _firstQuartile = firstQuartile;
    }

    public Double getFirstQuartile() {
        return _firstQuartile;
    }

    public PricingInsightsRegionAverageRate firstQuartile(Double firstQuartile) {
        _firstQuartile = firstQuartile;
        return this;
    }

    public void setMedian(Double median) {
        _median = median;
    }

    public Double getMedian() {
        return _median;
    }

    public PricingInsightsRegionAverageRate median(Double median) {
        _median = median;
        return this;
    }

    public void setThirdQuartile(Double thirdQuartile) {
        _thirdQuartile = thirdQuartile;
    }

    public Double getThirdQuartile() {
        return _thirdQuartile;
    }

    public PricingInsightsRegionAverageRate thirdQuartile(Double thirdQuartile) {
        _thirdQuartile = thirdQuartile;
        return this;
    }

    /*-*****************************-*/
    /*-             Json            -*/
    /*-*****************************-*/
    public static PricingInsightsRegionAverageRate fromJson(JsonObject obj) {
        try {
            return Unserializer.unserializeObject(PricingInsightsRegionAverageRate.class, obj);
        } catch (Exception ex) {
            Log.v(TAG, TAG, ex);
            return null;
        }
    }

    public JsonObject toJson() {
        return toJson(this);
    }

    public static JsonObject toJson(PricingInsightsRegionAverageRate pricingInsightsRegionAverageRate) {
        try {
            return Serializer.serializeObject(pricingInsightsRegionAverageRate);
        } catch (Exception ex) {
            Log.v(TAG, TAG, ex);
            return null;
        }
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
        dest.writeParcelable(toJson(), flags);
    }
}
