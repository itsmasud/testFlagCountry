package com.fieldnation.data.bv2.model;

import com.fieldnation.fnjson.JsonObject;
import com.fieldnation.fnjson.Serializer;
import com.fieldnation.fnjson.Unserializer;
import com.fieldnation.fnjson.annotations.Json;
import com.fieldnation.fnlog.Log;

public class CustomFieldCategory {
    private static final String TAG = "CustomFieldCategory";

    @Json(name = "metadata")
    private ListEnvelope metadata = null;

    @Json(name = "id")
    private Integer id = null;

    @Json(name = "name")
    private String name = null;

    @Json(name = "role")
    private String role = null;

    @Json(name = "results")
    private CustomField[] results;

    public CustomFieldCategory() {
    }

    public ListEnvelope getMetadata() {
        return metadata;
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getRole() {
        return role;
    }

    public CustomField[] getResults() {
        return results;
    }

    /*-*****************************-*/
    /*-             Json            -*/
    /*-*****************************-*/
    public static CustomFieldCategory fromJson(JsonObject obj) {
        try {
            return Unserializer.unserializeObject(CustomFieldCategory.class, obj);
        } catch (Exception ex) {
            Log.v(TAG, ex);
            return null;
        }
    }

    public JsonObject toJson() {
        return toJson(this);
    }

    public static JsonObject toJson(CustomFieldCategory customFieldCategory) {
        try {
            return Serializer.serializeObject(customFieldCategory);
        } catch (Exception ex) {
            Log.v(TAG, ex);
            return null;
        }
    }
}

