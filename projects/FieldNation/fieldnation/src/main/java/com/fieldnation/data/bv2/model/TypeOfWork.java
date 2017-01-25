package com.fieldnation.data.bv2.model;

import com.fieldnation.fnjson.JsonObject;
import com.fieldnation.fnjson.Serializer;
import com.fieldnation.fnjson.Unserializer;
import com.fieldnation.fnjson.annotations.Json;
import com.fieldnation.fnlog.Log;

public class TypeOfWork {
    private static final String TAG = "TypeOfWork";

    @Json(name = "name")
    private String _name;

    @Json(name = "industry")
    private String _industry;

    @Json(name = "id")
    private Integer _id;

    public TypeOfWork() {
    }

    public String getName() {
        return _name;
    }

    public String getIndustry() {
        return _industry;
    }

    public Integer getId() {
        return _id;
    }

    /*-*****************************-*/
    /*-             Json            -*/
    /*-*****************************-*/
    public static TypeOfWork fromJson(JsonObject obj) {
        try {
            return Unserializer.unserializeObject(TypeOfWork.class, obj);
        } catch (Exception ex) {
            Log.v(TAG, TAG, ex);
            return null;
        }
    }

    public JsonObject toJson() {
        return toJson(this);
    }

    public static JsonObject toJson(TypeOfWork typeOfWork) {
        try {
            return Serializer.serializeObject(typeOfWork);
        } catch (Exception ex) {
            Log.v(TAG, TAG, ex);
            return null;
        }
    }
}
