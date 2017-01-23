package com.fieldnation.data.bv2.model;

import com.fieldnation.fnjson.JsonObject;
import com.fieldnation.fnjson.Serializer;
import com.fieldnation.fnjson.Unserializer;
import com.fieldnation.fnjson.annotations.Json;
import com.fieldnation.fnlog.Log;

public class Project {
    private static final String TAG = "Project";

    @Json(name = "id")
    private Integer id = null;

    @Json(name = "name")
    private String name = null;

    public Project() {
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    /*-*****************************-*/
    /*-             Json            -*/
    /*-*****************************-*/
    public static Project fromJson(JsonObject obj) {
        try {
            return Unserializer.unserializeObject(Project.class, obj);
        } catch (Exception ex) {
            Log.v(TAG, ex);
            return null;
        }
    }

    public JsonObject toJson() {
        return toJson(this);
    }

    public static JsonObject toJson(Project project) {
        try {
            return Serializer.serializeObject(project);
        } catch (Exception ex) {
            Log.v(TAG, ex);
            return null;
        }
    }
}