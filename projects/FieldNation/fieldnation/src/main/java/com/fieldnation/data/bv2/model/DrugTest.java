package com.fieldnation.data.bv2.model;

import com.fieldnation.fnjson.JsonObject;
import com.fieldnation.fnjson.Serializer;
import com.fieldnation.fnjson.Unserializer;
import com.fieldnation.fnjson.annotations.Json;
import com.fieldnation.fnlog.Log;

public class DrugTest {
    private static final String TAG = "DrugTest";

    @Json(name = "expires")
    private String _expires;

    @Json(name = "name")
    private String _name;

    @Json(name = "id")
    private Integer _id;

    public DrugTest() {
    }

    public String getExpires() {
        return _expires;
    }

    public String getName() {
        return _name;
    }

    public Integer getId() {
        return _id;
    }

    /*-*****************************-*/
    /*-             Json            -*/
    /*-*****************************-*/
    public static DrugTest fromJson(JsonObject obj) {
        try {
            return Unserializer.unserializeObject(DrugTest.class, obj);
        } catch (Exception ex) {
            Log.v(TAG, TAG, ex);
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
            Log.v(TAG, TAG, ex);
            return null;
        }
    }
}
