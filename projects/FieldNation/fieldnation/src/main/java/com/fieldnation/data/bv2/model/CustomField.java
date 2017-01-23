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
    private TypeEnum type = null;

    @Json(name = "options")
    private String[] options;

    @Json(name = "role")
    private RoleEnum role = null;

    @Json(name = "value")
    private String value = null;

    @Json(name = "dependency")
    private CustomFieldDependency dependency = null;

    @Json(name = "flags")
    private FlagsEnum[] flags;

    @Json(name = "actions")
    private ActionsEnum actions = null;

    @Json(name = "category")
    private String category = null;


    public enum TypeEnum {
        @Json(name = "text")
        TEXT("text"),
        @Json(name = "numeric")
        NUMERIC("numeric"),
        @Json(name = "date")
        DATE("date"),
        @Json(name = "datetime")
        DATETIME("datetime"),
        @Json(name = "time")
        TIME("time"),
        @Json(name = "predefined")
        PREDEFINED("predefined"),
        @Json(name = "phone")
        PHONE("phone");

        private String value;

        TypeEnum(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return String.valueOf(value);
        }
    }

    public enum RoleEnum {
        @Json(name = "buyer")
        BUYER("buyer"),
        @Json(name = "assigned_provider")
        ASSIGNED_PROVIDER("assigned_provider");

        private String value;

        RoleEnum(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return String.valueOf(value);
        }
    }

    public enum FlagsEnum {
        @Json(name = "unique")
        UNIQUE("unique"),
        @Json(name = "required")
        REQUIRED("required"),
        @Json(name = "internal_id")
        INTERNAL_ID("internal_id"),
        @Json(name = "required_during_checkin")
        REQUIRED_DURING_CHECKIN("required_during_checkin"),
        @Json(name = "included_in_alerts")
        INCLUDED_IN_ALERTS("included_in_alerts"),
        @Json(name = "shown_in_header")
        SHOWN_IN_HEADER("shown_in_header"),
        @Json(name = "seen_by_provider")
        SEEN_BY_PROVIDER("seen_by_provider"),
        @Json(name = "seen_by_clients")
        SEEN_BY_CLIENTS("seen_by_clients"),
        @Json(name = "visible_to_clients")
        VISIBLE_TO_CLIENTS("visible_to_clients"),
        @Json(name = "client_request_required")
        CLIENT_REQUEST_REQUIRED("client_request_required"),
        @Json(name = "client_request_use_for")
        CLIENT_REQUEST_USE_FOR("client_request_use_for");

        private String value;

        FlagsEnum(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return String.valueOf(value);
        }
    }

    public enum ActionsEnum {
        @Json(name = "edit")
        EDIT("edit");

        private String value;

        ActionsEnum(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return String.valueOf(value);
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
        return type;
    }

    public String[] getOptions() {
        return options;
    }

    public RoleEnum getRole() {
        return role;
    }

    public String getValue() {
        return value;
    }

    public CustomFieldDependency getDependency() {
        return dependency;
    }

    private FlagsEnum[] flagsEnums = null;

    public FlagsEnum[] getFlags() {
        return flags;
    }

    public ActionsEnum getActions() {
        return actions;
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