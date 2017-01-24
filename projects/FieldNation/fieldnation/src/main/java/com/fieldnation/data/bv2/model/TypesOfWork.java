package com.fieldnation.data.bv2.model;

import com.fieldnation.fnjson.JsonObject;
import com.fieldnation.fnjson.Serializer;
import com.fieldnation.fnjson.Unserializer;
import com.fieldnation.fnjson.annotations.Json;
import com.fieldnation.fnlog.Log;

public class TypesOfWork {
    private static final String TAG = "TypesOfWork";

    @Json(name = "metadata")
    private ListEnvelope metadata;

    @Json(name = "results")
    private TypeOfWork[] results;

    public TypesOfWork() {
    }

    public ListEnvelope getMetadata() {
        return metadata;
    }

    public TypeOfWork[] getResults() {
        return results;
    }

    /*-*****************************-*/
    /*-             Json            -*/
    /*-*****************************-*/
    public static TypesOfWork fromJson(JsonObject obj) {
        try {
            return Unserializer.unserializeObject(TypesOfWork.class, obj);
        } catch (Exception ex) {
            Log.v(TAG, ex);
            return null;
        }
    }

    public JsonObject toJson() {
        return toJson(this);
    }

    public static JsonObject toJson(TypesOfWork typesOfWork) {
        try {
            return Serializer.serializeObject(typesOfWork);
        } catch (Exception ex) {
            Log.v(TAG, ex);
            return null;
        }
    }
}
