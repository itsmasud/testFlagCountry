package com.fieldnation.data.bv2.model;

import com.fieldnation.fnjson.JsonObject;
import com.fieldnation.fnjson.Serializer;
import com.fieldnation.fnjson.Unserializer;
import com.fieldnation.fnjson.annotations.Json;
import com.fieldnation.fnlog.Log;

public class TypeOfWork {
    private static final String TAG = "TypeOfWork";

    @Json(name = "id")
    private Integer id = null;

    @Json(name = "industry")
    private String industry = null;

    @Json(name = "name")
    private String name = null;

    public TypeOfWork() {
    }

    public Integer getId() {
        return id;
    }

    public String getIndustry() {
        return industry;
    }

    public String getName() {
        return name;
    }

    /*-*****************************-*/
    /*-             Json            -*/
    /*-*****************************-*/
    public static TypeOfWork fromJson(JsonObject obj) {
        try {
            return Unserializer.unserializeObject(TypeOfWork.class, obj);
        } catch (Exception ex) {
            Log.v(TAG, ex);
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
            Log.v(TAG, ex);
            return null;
        }
    }
}