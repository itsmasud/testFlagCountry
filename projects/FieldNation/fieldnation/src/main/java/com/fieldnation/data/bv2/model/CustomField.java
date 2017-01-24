package com.fieldnation.data.bv2.model;

import com.fieldnation.fnjson.JsonObject;
import com.fieldnation.fnjson.Serializer;
import com.fieldnation.fnjson.Unserializer;
import com.fieldnation.fnjson.annotations.Json;
import com.fieldnation.fnlog.Log;

public class CustomField {
    private static final String TAG = "CustomField";

    @Json(name = "role")
    private RoleEnum role;

    @Json(name = "dependency")
    private CustomFieldDependency dependency;

    @Json(name = "name")
    private String name;

    @Json(name = "options")
    private String[] options;

    @Json(name = "flags")
    private FlagsEnum[] flags;

    @Json(name = "tip")
    private String tip;

    @Json(name = "id")
    private Integer id;

    @Json(name = "type")
    private TypeEnum type;

    @Json(name = "category")
    private String category;

    @Json(name = "value")
    private String value;

    @Json(name = "actions")
    private ActionsEnum actions;

    public CustomField() {
    }

    public RoleEnum getRole() {
        return role;
    }

    public CustomFieldDependency getDependency() {
        return dependency;
    }

    public String getName() {
        return name;
    }

    public String[] getOptions() {
        return options;
    }

    public FlagsEnum[] getFlags() {
        return flags;
    }

    public String getTip() {
        return tip;
    }

    public Integer getId() {
        return id;
    }

    public TypeEnum getType() {
        return type;
    }

    public String getCategory() {
        return category;
    }

    public String getValue() {
        return value;
    }

    public ActionsEnum getActions() {
        return actions;
    }

    /*-*****************************-*/
    /*-             Json            -*/
    /*-*****************************-*/
    public static CustomField fromJson(JsonObject obj) {
        try {
            return Unserializer.unserializeObject(CustomField.class, obj);
        } catch (Exception ex) {
            Log.v(TAG, ex);
            return null;
        }
    }

    public JsonObject toJson() {
        return toJson(this);
    }

    public static JsonObject toJson(CustomField customField) {
        try {
            return Serializer.serializeObject(customField);
        } catch (Exception ex) {
            Log.v(TAG, ex);
            return null;
        }
    }
}
