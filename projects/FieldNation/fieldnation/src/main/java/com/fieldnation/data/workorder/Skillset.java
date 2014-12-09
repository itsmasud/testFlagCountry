package com.fieldnation.data.workorder;

import com.fieldnation.json.JsonObject;
import com.fieldnation.json.Serializer;
import com.fieldnation.json.annotations.Json;

public class Skillset {
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
            ex.printStackTrace();
            return null;
        }
    }

    public static Skillset fromJson(JsonObject json) {
        try {
            return Serializer.unserializeObject(Skillset.class, json);
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

}
