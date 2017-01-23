package com.fieldnation.data.bv2.model;

import com.fieldnation.fnjson.JsonObject;
import com.fieldnation.fnjson.Serializer;
import com.fieldnation.fnjson.Unserializer;
import com.fieldnation.fnjson.annotations.Json;
import com.fieldnation.fnlog.Log;

public class DrugTest {
    private static final String TAG = "DrugTest";

    @Json(name = "id")
    private Integer id = null;

    @Json(name = "name")
    private String name = null;

    @Json(name = "expires")
    private String expires = null;

    public DrugTest() {
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getExpires() {
        return expires;
    }

    /*-*****************************-*/
    /*-             Json            -*/
    /*-*****************************-*/
    public static DrugTest fromJson(JsonObject obj) {
        try {
            return Unserializer.unserializeObject(DrugTest.class, obj);
        } catch (Exception ex) {
            Log.v(TAG, ex);
            return null;
        }
    }

    public JsonObject toJson() {
        return toJson(this);
    }

    public static JsonObject toJson(DrugTest drugTest) {
        try {
            return Serializer.serializeObject(drugTest);
        } catch (Exception ex) {
            Log.v(TAG, ex);
            return null;
        }
    }
}

