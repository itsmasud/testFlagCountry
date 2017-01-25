package com.fieldnation.data.bv2.model;

import com.fieldnation.fnjson.JsonObject;
import com.fieldnation.fnjson.Serializer;
import com.fieldnation.fnjson.Unserializer;
import com.fieldnation.fnjson.annotations.Json;
import com.fieldnation.fnlog.Log;

public class CompanyIntegrations {
    private static final String TAG = "CompanyIntegrations";

    @Json(name = "envelope")
    private ListEnvelope _envelope;

    @Json(name = "results")
    private CompanyIntegration[] _results;

    public CompanyIntegrations() {
    }

    public ListEnvelope getEnvelope() {
        return _envelope;
    }

    public CompanyIntegration[] getResults() {
        return _results;
    }

    /*-*****************************-*/
    /*-             Json            -*/
    /*-*****************************-*/
    public static CompanyIntegrations fromJson(JsonObject obj) {
        try {
            return Unserializer.unserializeObject(CompanyIntegrations.class, obj);
        } catch (Exception ex) {
            Log.v(TAG, TAG, ex);
            return null;
        }
    }

    public JsonObject toJson() {
        return toJson(this);
    }

    public static JsonObject toJson(CompanyIntegrations companyIntegrations) {
        try {
            return Serializer.serializeObject(companyIntegrations);
        } catch (Exception ex) {
            Log.v(TAG, TAG, ex);
            return null;
        }
    }
}
