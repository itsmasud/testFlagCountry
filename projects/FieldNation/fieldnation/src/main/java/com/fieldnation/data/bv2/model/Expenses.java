package com.fieldnation.data.bv2.model;

import com.fieldnation.fnjson.JsonObject;
import com.fieldnation.fnjson.Serializer;
import com.fieldnation.fnjson.Unserializer;
import com.fieldnation.fnjson.annotations.Json;
import com.fieldnation.fnlog.Log;

public class Expenses {
    private static final String TAG = "Expenses";

    @Json(name = "metadata")
    private ListEnvelope _metadata;

    @Json(name = "results")
    private Expense[] _results;

    public Expenses() {
    }

    public ListEnvelope getMetadata() {
        return _metadata;
    }

    public Expense[] getResults() {
        return _results;
    }

    /*-*****************************-*/
    /*-             Json            -*/
    /*-*****************************-*/
    public static Expenses fromJson(JsonObject obj) {
        try {
            return Unserializer.unserializeObject(Expenses.class, obj);
        } catch (Exception ex) {
            Log.v(TAG, TAG, ex);
            return null;
        }
    }

    public JsonObject toJson() {
        return toJson(this);
    }

    public static JsonObject toJson(Expenses expenses) {
        try {
            return Serializer.serializeObject(expenses);
        } catch (Exception ex) {
            Log.v(TAG, TAG, ex);
            return null;
        }
    }
}
