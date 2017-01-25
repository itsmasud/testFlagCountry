package com.fieldnation.data.bv2.model;

import com.fieldnation.fnjson.JsonObject;
import com.fieldnation.fnjson.Serializer;
import com.fieldnation.fnjson.Unserializer;
import com.fieldnation.fnjson.annotations.Json;
import com.fieldnation.fnlog.Log;

public class PricingInsightsRegionAverageRate {
    private static final String TAG = "PricingInsightsRegionAverageRate";

    @Json(name = "first_quartile")
    private Double _firstQuartile;

    @Json(name = "median")
    private Double _median;

    @Json(name = "third_quartile")
    private Double _thirdQuartile;

    public PricingInsightsRegionAverageRate() {
    }

    public Double getFirstQuartile() {
        return _firstQuartile;
    }

    public Double getMedian() {
        return _median;
    }

    public Double getThirdQuartile() {
        return _thirdQuartile;
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
}
