package com.fieldnation.data.bv2.model;

import com.fieldnation.fnjson.JsonObject;
import com.fieldnation.fnjson.Serializer;
import com.fieldnation.fnjson.Unserializer;
import com.fieldnation.fnjson.annotations.Json;
import com.fieldnation.fnlog.Log;

public class CustomFieldCategory {
    private static final String TAG = "CustomFieldCategory";

    @Json(name = "metadata")
    private ListEnvelope _metadata;

    @Json(name = "role")
    private String _role;

    @Json(name = "name")
    private String _name;

    @Json(name = "id")
    private Integer _id;

    @Json(name = "results")
    private CustomField[] _results;

    public CustomFieldCategory() {
    }

    public ListEnvelope getMetadata() {
        return _metadata;
    }

    public String getRole() {
        return _role;
    }

    public String getName() {
        return _name;
    }

    public Integer getId() {
        return _id;
    }

    public CustomField[] getResults() {
        return _results;
    }

    /*-*****************************-*/
    /*-             Json            -*/
    /*-*****************************-*/
    public static CustomFieldCategory fromJson(JsonObject obj) {
        try {
            return Unserializer.unserializeObject(CustomFieldCategory.class, obj);
        } catch (Exception ex) {
            Log.v(TAG, TAG, ex);
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
            Log.v(TAG, TAG, ex);
            return null;
        }
    }
}
