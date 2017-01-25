package com.fieldnation.data.bv2.model;

import com.fieldnation.fnjson.JsonObject;
import com.fieldnation.fnjson.Serializer;
import com.fieldnation.fnjson.Unserializer;
import com.fieldnation.fnjson.annotations.Json;
import com.fieldnation.fnlog.Log;

public class PricingInsights {
    private static final String TAG = "PricingInsights";

    @Json(name = "region")
    private PricingInsightsRegion _region;

    public PricingInsights() {
    }

    public PricingInsightsRegion getRegion() {
        return _region;
    }

    /*-*****************************-*/
    /*-             Json            -*/
    /*-*****************************-*/
    public static PricingInsights fromJson(JsonObject obj) {
        try {
            return Unserializer.unserializeObject(PricingInsights.class, obj);
        } catch (Exception ex) {
            Log.v(TAG, TAG, ex);
            return null;
        }
    }

    public JsonObject toJson() {
        return toJson(this);
    }

    public static JsonObject toJson(PricingInsights pricingInsights) {
        try {
            return Serializer.serializeObject(pricingInsights);
        } catch (Exception ex) {
            Log.v(TAG, TAG, ex);
            return null;
        }
    }
}
