package com.fieldnation.data.bv2.model;

import com.fieldnation.fnjson.JsonObject;
import com.fieldnation.fnjson.Serializer;
import com.fieldnation.fnjson.Unserializer;
import com.fieldnation.fnjson.annotations.Json;
import com.fieldnation.fnlog.Log;

public class CustomField {
    private static final String TAG = "CustomField";

    @Json(name = "id")
    private Integer id = null;

    @Json(name = "name")
    private String name = null;

    @Json(name = "tip")
    private String tip = null;

    @Json(name = "type")
    private String type = null;

    @Json(name = "options")
    private String[] options;

    @Json(name = "role")
    private String role = null;

    @Json(name = "value")
    private String value = null;

    @Json(name = "dependency")
    private CustomFieldDependency dependency = null;

    @Json(name = "flags")
    private String[] flags;

    @Json(name = "actions")
    private String actions = null;

    @Json(name = "category")
    private String category = null;


    public enum TypeEnum {
        TEXT("text"),
        NUMERIC("numeric"),
        DATE("date"),
        DATETIME("datetime"),
        TIME("time"),
        PREDEFINED("predefined"),
        PHONE("phone");

        private String value;

        TypeEnum(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return String.valueOf(value);
        }

        public static TypeEnum fromValue(String value) {
            TypeEnum[] values = values();
            for (TypeEnum e : values) {
                if (e.value.equals(value))
                    return e;
            }
            return null;
        }
    }

    public enum RoleEnum {
        BUYER("buyer"),
        ASSIGNED_PROVIDER("assigned_provider");

        private String value;

        RoleEnum(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return String.valueOf(value);
        }

        public static RoleEnum fromValue(String value) {
            RoleEnum[] values = values();
            for (RoleEnum e : values) {
                if (e.value.equals(value))
                    return e;
            }
            return null;
        }
    }

    public enum FlagsEnum {
        UNIQUE("unique"),
        REQUIRED("required"),
        INTERNAL_ID("internal_id"),
        REQUIRED_DURING_CHECKIN("required_during_checkin"),
        INCLUDED_IN_ALERTS("included_in_alerts"),
        SHOWN_IN_HEADER("shown_in_header"),
        SEEN_BY_PROVIDER("seen_by_provider"),
        SEEN_BY_CLIENTS("seen_by_clients"),
        VISIBLE_TO_CLIENTS("visible_to_clients"),
        CLIENT_REQUEST_REQUIRED("client_request_required"),
        CLIENT_REQUEST_USE_FOR("client_request_use_for");

        private String value;

        FlagsEnum(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return String.valueOf(value);
        }

        public static FlagsEnum fromValues(String value) {
            FlagsEnum[] values = values();
            for (FlagsEnum e : values) {
                if (e.value.equals(value))
                    return e;
            }
            return null;
        }
    }

    public enum ActionsEnum {
        EDIT("edit");

        private String value;

        ActionsEnum(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return String.valueOf(value);
        }

        public static ActionsEnum fromValue(String value) {
            ActionsEnum[] values = values();
            for (ActionsEnum e : values) {
                if (e.value.equals(value))
                    return e;
            }
            return null;
        }
    }

    public CustomField() {
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getTip() {
        return tip;
    }

    public TypeEnum getType() {
        return TypeEnum.fromValue(type);
    }

    public String[] getOptions() {
        return options;
    }

    public RoleEnum getRole() {
        return RoleEnum.fromValue(role);
    }

    public String getValue() {
        return value;
    }

    public CustomFieldDependency getDependency() {
        return dependency;
    }

    private FlagsEnum[] flagsEnums = null;

    public FlagsEnum[] getFlags() {
        if (flagsEnums == null) {
            flagsEnums = new FlagsEnum[flags.length];
            for (int i = 0; i < flags.length; i++) {
                flagsEnums[i] = FlagsEnum.fromValues(flags[i]);
            }
        }

        return flagsEnums;
    }

    public ActionsEnum getActions() {
        return ActionsEnum.fromValue(actions);
    }

    public String getCategory() {
        return category;
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