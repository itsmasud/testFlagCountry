package com.fieldnation.data.bv2.model;

import com.fieldnation.fnjson.JsonObject;
import com.fieldnation.fnjson.Serializer;
import com.fieldnation.fnjson.Unserializer;
import com.fieldnation.fnjson.annotations.Json;
import com.fieldnation.fnlog.Log;

public class PricingInsightsRegionAverageRate {
    private static final String TAG = "PricingInsightsRegionAverageRate";

    @Json(name = "first_quartile")
    private Double firstQuartile = null;

    @Json(name = "median")
    private Double median = null;

    @Json(name = "third_quartile")
    private Double thirdQuartile = null;

    public PricingInsightsRegionAverageRate() {
    }

    public Double getFirstQuartile() {
        return firstQuartile;
    }

    public Double getMedian() {
        return median;
    }

    public Double getThirdQuartile() {
        return thirdQuartile;
    }

    /*-*****************************-*/
    /*-             Json            -*/
    /*-*****************************-*/
    public static PricingInsightsRegionAverageRate fromJson(JsonObject obj) {
        try {
            return Unserializer.unserializeObject(PricingInsightsRegionAverageRate.class, obj);
        } catch (Exception ex) {
            Log.v(TAG, ex);
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
            Log.v(TAG, ex);
            return null;
        }
    }
}