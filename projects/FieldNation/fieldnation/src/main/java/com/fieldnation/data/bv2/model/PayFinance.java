package com.fieldnation.data.bv2.model;

import com.fieldnation.fnjson.JsonObject;
import com.fieldnation.fnjson.Serializer;
import com.fieldnation.fnjson.Unserializer;
import com.fieldnation.fnjson.annotations.Json;
import com.fieldnation.fnlog.Log;

public class PayFinance {
    private static final String TAG = "PayFinance";

    @Json(name = "id")
    private Integer id = null;

    @Json(name = "terms")
    private String terms = null;

    @Json(name = "description")
    private String description = null;

    @Json(name = "limit")
    private Double limit = null;

    public PayFinance() {
    }

    public Integer getId() {
        return id;
    }

    public String getTerms() {
        return terms;
    }

    public String getDescription() {
        return description;
    }

    public Double getLimit() {
        return limit;
    }

    /*-*****************************-*/
    /*-             Json            -*/
    /*-*****************************-*/
    public static PayFinance fromJson(JsonObject obj) {
        try {
            return Unserializer.unserializeObject(PayFinance.class, obj);
        } catch (Exception ex) {
            Log.v(TAG, ex);
            return null;
        }
    }

    public JsonObject toJson() {
        return toJson(this);
    }

    public static JsonObject toJson(PayFinance payFinance) {
        try {
            return Serializer.serializeObject(payFinance);
        } catch (Exception ex) {
            Log.v(TAG, ex);
            return null;
        }
    }
}