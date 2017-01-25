package com.fieldnation.data.bv2.model;

import com.fieldnation.fnjson.JsonObject;
import com.fieldnation.fnjson.Serializer;
import com.fieldnation.fnjson.Unserializer;
import com.fieldnation.fnjson.annotations.Json;
import com.fieldnation.fnlog.Log;

public class PayFinance {
    private static final String TAG = "PayFinance";

    @Json(name = "terms")
    private String _terms;

    @Json(name = "limit")
    private Double _limit;

    @Json(name = "description")
    private String _description;

    @Json(name = "id")
    private Integer _id;

    public PayFinance() {
    }

    public String getTerms() {
        return _terms;
    }

    public Double getLimit() {
        return _limit;
    }

    public String getDescription() {
        return _description;
    }

    public Integer getId() {
        return _id;
    }

    /*-*****************************-*/
    /*-             Json            -*/
    /*-*****************************-*/
    public static PayFinance fromJson(JsonObject obj) {
        try {
            return Unserializer.unserializeObject(PayFinance.class, obj);
        } catch (Exception ex) {
            Log.v(TAG, TAG, ex);
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
            Log.v(TAG, TAG, ex);
            return null;
        }
    }
}
