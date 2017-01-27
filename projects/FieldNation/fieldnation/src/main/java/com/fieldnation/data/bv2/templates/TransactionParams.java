package com.fieldnation.data.bv2.templates;

import com.fieldnation.fnjson.JsonObject;
import com.fieldnation.fnjson.Serializer;
import com.fieldnation.fnjson.Unserializer;
import com.fieldnation.fnjson.annotations.Json;
import com.fieldnation.fnlog.Log;

/**
 * Created by mc on 1/27/17.
 */

public class TransactionParams {
    private static final String TAG = "TransactionParams";

    @Json(name = "apiClass")
    String apiClassName;

    @Json(name = "apiFunction")
    String apiFunction;

    @Json(name = "successClass")
    String successClassName = null;

    @Json(name = "errorClass")
    String errorClassName = null;

    public TransactionParams() {
    }

    /*-*****************************-*/
    /*-             Json            -*/
    /*-*****************************-*/
    public static TransactionParams fromJson(JsonObject obj) {
        try {
            return Unserializer.unserializeObject(TransactionParams.class, obj);
        } catch (Exception ex) {
            Log.v(TAG, TAG, ex);
            return null;
        }
    }

    public JsonObject toJson() {
        return toJson(this);
    }

    public static JsonObject toJson(TransactionParams transactionParams) {
        try {
            return Serializer.serializeObject(transactionParams);
        } catch (Exception ex) {
            Log.v(TAG, TAG, ex);
            return null;
        }
    }


}
