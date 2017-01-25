package com.fieldnation.data.bv2.model;

import com.fieldnation.fnjson.JsonObject;
import com.fieldnation.fnjson.Serializer;
import com.fieldnation.fnjson.Unserializer;
import com.fieldnation.fnjson.annotations.Json;
import com.fieldnation.fnlog.Log;

public class PricingInsightsRegion {
    private static final String TAG = "PricingInsightsRegion";

    @Json(name = "distance")
    private Double _distance;

    @Json(name = "name")
    private String _name;

    @Json(name = "average_rate")
    private PricingInsightsRegionAverageRate _averageRate;

    @Json(name = "id")
    private Integer _id;

    @Json(name = "providers")
    private PricingInsightsRegionProviders _providers;

    public PricingInsightsRegion() {
    }

    public Double getDistance() {
        return _distance;
    }

    public String getName() {
        return _name;
    }

    public PricingInsightsRegionAverageRate getAverageRate() {
        return _averageRate;
    }

    public Integer getId() {
        return _id;
    }

    public PricingInsightsRegionProviders getProviders() {
        return _providers;
    }

    /*-*****************************-*/
    /*-             Json            -*/
    /*-*****************************-*/
    public static PricingInsightsRegion fromJson(JsonObject obj) {
        try {
            return Unserializer.unserializeObject(PricingInsightsRegion.class, obj);
        } catch (Exception ex) {
            Log.v(TAG, TAG, ex);
            return null;
        }
    }

    public JsonObject toJson() {
        return toJson(this);
    }

    public static JsonObject toJson(PricingInsightsRegion pricingInsightsRegion) {
        try {
            return Serializer.serializeObject(pricingInsightsRegion);
        } catch (Exception ex) {
            Log.v(TAG, TAG, ex);
            return null;
        }
    }
}
