package com.fieldnation.data.bv2.model;

import com.fieldnation.fnjson.JsonObject;
import com.fieldnation.fnjson.Serializer;
import com.fieldnation.fnjson.Unserializer;
import com.fieldnation.fnjson.annotations.Json;
import com.fieldnation.fnlog.Log;

public class CustomField {
    private static final String TAG = "CustomField";

    @Json(name = "role")
    private RoleEnum _role;

    @Json(name = "dependency")
    private CustomFieldDependency _dependency;

    @Json(name = "name")
    private String _name;

    @Json(name = "options")
    private String[] _options;

    @Json(name = "flags")
    private FlagsEnum[] _flags;

    @Json(name = "tip")
    private String _tip;

    @Json(name = "id")
    private Integer _id;

    @Json(name = "type")
    private TypeEnum _type;

    @Json(name = "category")
    private String _category;

    @Json(name = "value")
    private String _value;

    @Json(name = "actions")
    private ActionsEnum _actions;

    public CustomField() {
    }

    public RoleEnum getRole() {
        return _role;
    }

    public CustomFieldDependency getDependency() {
        return _dependency;
    }

    public String getName() {
        return _name;
    }

    public String[] getOptions() {
        return _options;
    }

    public FlagsEnum[] getFlags() {
        return _flags;
    }

    public String getTip() {
        return _tip;
    }

    public Integer getId() {
        return _id;
    }

    public TypeEnum getType() {
        return _type;
    }

    public String getCategory() {
        return _category;
    }

    public String getValue() {
        return _value;
    }

    public ActionsEnum getActions() {
        return _actions;
    }

    /*-*****************************-*/
    /*-             Json            -*/
    /*-*****************************-*/
    public static CustomField fromJson(JsonObject obj) {
        try {
            return Unserializer.unserializeObject(CustomField.class, obj);
        } catch (Exception ex) {
            Log.v(TAG, TAG, ex);
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
            Log.v(TAG, TAG, ex);
            return null;
        }
    }
}
