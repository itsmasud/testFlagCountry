package com.fieldnation.data.bv2.model;

import com.fieldnation.fnjson.JsonObject;
import com.fieldnation.fnjson.Serializer;
import com.fieldnation.fnjson.Unserializer;
import com.fieldnation.fnjson.annotations.Json;
import com.fieldnation.fnlog.Log;

public class PricingInsightsRegionProviders {
    private static final String TAG = "PricingInsightsRegionProviders";

    @Json(name = "marketplace")
    private Integer marketplace;

    public PricingInsightsRegionProviders() {
    }

    public Integer getMarketplace() {
        return marketplace;
    }

    /*-*****************************-*/
    /*-             Json            -*/
    /*-*****************************-*/
    public static PricingInsightsRegionProviders fromJson(JsonObject obj) {
        try {
            return Unserializer.unserializeObject(PricingInsightsRegionProviders.class, obj);
        } catch (Exception ex) {
            Log.v(TAG, ex);
            return null;
        }
    }

    public JsonObject toJson() {
        return toJson(this);
    }

    public static JsonObject toJson(PricingInsightsRegionProviders pricingInsightsRegionProviders) {
        try {
            return Serializer.serializeObject(pricingInsightsRegionProviders);
        } catch (Exception ex) {
            Log.v(TAG, ex);
            return null;
        }
    }
}
