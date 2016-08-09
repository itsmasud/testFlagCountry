package com.fieldnation.data.workorder;

import com.fieldnation.fnlog.Log;
import com.fieldnation.fnjson.JsonObject;
import com.fieldnation.fnjson.Serializer;
import com.fieldnation.fnjson.Unserializer;
import com.fieldnation.fnjson.annotations.Json;

public class Skillset {
    private static final String TAG = "Skillset";

    @Json(name = "dynamicTermId")
    private Integer _dynamicTermId;
    @Json(name = "name")
    private String _name;

    public Skillset() {
    }

    public Integer getDynamicTermId() {
        return _dynamicTermId;
    }

    public String getName() {
        return _name;
    }

    public JsonObject toJson() {
        return toJson(this);
    }

    public static JsonObject toJson(Skillset skillset) {
        try {
            return Serializer.serializeObject(skillset);
        } catch (Exception ex) {
            Log.v(TAG, ex);
            return null;
        }
    }

    public static Skillset fromJson(JsonObject json) {
        try {
            return Unserializer.unserializeObject(Skillset.class, json);
        } catch (Exception ex) {
            Log.v(TAG, ex);
            return null;
        }
    }

}
