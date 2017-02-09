package com.fieldnation.v2.data.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.fieldnation.fnjson.JsonArray;
import com.fieldnation.fnjson.JsonObject;
import com.fieldnation.fnjson.Serializer;
import com.fieldnation.fnjson.Unserializer;
import com.fieldnation.fnjson.annotations.Json;
import com.fieldnation.fnlog.Log;

/**
 * Created by dmgen from swagger.
 */

public class CustomField implements Parcelable {
    private static final String TAG = "CustomField";

    @Json(name = "actions")
    private ActionsEnum[] _actions;

    @Json(name = "category")
    private String _category;

    @Json(name = "dependency")
    private CustomFieldDependency _dependency;

    @Json(name = "flags")
    private FlagsEnum[] _flags;

    @Json(name = "id")
    private Integer _id;

    @Json(name = "name")
    private String _name;

    @Json(name = "options")
    private String[] _options;

    @Json(name = "role")
    private RoleEnum _role;

    @Json(name = "tip")
    private String _tip;

    @Json(name = "type")
    private TypeEnum _type;

    @Json(name = "value")
    private String _value;

    public CustomField() {
    }

    public void setActions(ActionsEnum[] actions) {
        _actions = actions;
    }

    public ActionsEnum[] getActions() {
        return _actions;
    }

    public CustomField actions(ActionsEnum[] actions) {
        _actions = actions;
        return this;
    }

    public void setCategory(String category) {
        _category = category;
    }

    public String getCategory() {
        return _category;
    }

    public CustomField category(String category) {
        _category = category;
        return this;
    }

    public void setDependency(CustomFieldDependency dependency) {
        _dependency = dependency;
    }

    public CustomFieldDependency getDependency() {
        return _dependency;
    }

    public CustomField dependency(CustomFieldDependency dependency) {
        _dependency = dependency;
        return this;
    }

    public void setFlags(FlagsEnum[] flags) {
        _flags = flags;
    }

    public FlagsEnum[] getFlags() {
        return _flags;
    }

    public CustomField flags(FlagsEnum[] flags) {
        _flags = flags;
        return this;
    }

    public void setId(Integer id) {
        _id = id;
    }

    public Integer getId() {
        return _id;
    }

    public CustomField id(Integer id) {
        _id = id;
        return this;
    }

    public void setName(String name) {
        _name = name;
    }

    public String getName() {
        return _name;
    }

    public CustomField name(String name) {
        _name = name;
        return this;
    }

    public void setOptions(String[] options) {
        _options = options;
    }

    public String[] getOptions() {
        return _options;
    }

    public CustomField options(String[] options) {
        _options = options;
        return this;
    }

    public void setRole(RoleEnum role) {
        _role = role;
    }

    public RoleEnum getRole() {
        return _role;
    }

    public CustomField role(RoleEnum role) {
        _role = role;
        return this;
    }

    public void setTip(String tip) {
        _tip = tip;
    }

    public String getTip() {
        return _tip;
    }

    public CustomField tip(String tip) {
        _tip = tip;
        return this;
    }

    public void setType(TypeEnum type) {
        _type = type;
    }

    public TypeEnum getType() {
        return _type;
    }

    public CustomField type(TypeEnum type) {
        _type = type;
        return this;
    }

    public void setValue(String value) {
        _value = value;
    }

    public String getValue() {
        return _value;
    }

    public CustomField value(String value) {
        _value = value;
        return this;
    }

    /*-******************************-*/
    /*-             Enums            -*/
    /*-******************************-*/
    public enum RoleEnum {
        @Json(name = "assigned_provider")
        ASSIGNED_PROVIDER("assigned_provider"),
        @Json(name = "buyer")
        BUYER("buyer");

        private String value;

        RoleEnum(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return String.valueOf(value);
        }
    }

    public enum TypeEnum {
        @Json(name = "date")
        DATE("date"),
        @Json(name = "datetime")
        DATETIME("datetime"),
        @Json(name = "numeric")
        NUMERIC("numeric"),
        @Json(name = "phone")
        PHONE("phone"),
        @Json(name = "predefined")
        PREDEFINED("predefined"),
        @Json(name = "text")
        TEXT("text"),
        @Json(name = "time")
        TIME("time");

        private String value;

        TypeEnum(String value) {
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

    public enum FlagsEnum {
        @Json(name = "included_in_alerts")
        INCLUDED_IN_ALERTS("included_in_alerts"),
        @Json(name = "visible_to_clients")
        VISIBLE_TO_CLIENTS("visible_to_clients"),
        @Json(name = "shown_in_header")
        SHOWN_IN_HEADER("shown_in_header"),
        @Json(name = "required_during_checkin")
        REQUIRED_DURING_CHECKIN("required_during_checkin"),
        @Json(name = "seen_by_clients")
        SEEN_BY_CLIENTS("seen_by_clients"),
        @Json(name = "internal_id")
        INTERNAL_ID("internal_id"),
        @Json(name = "client_request_required")
        CLIENT_REQUEST_REQUIRED("client_request_required"),
        @Json(name = "unique")
        UNIQUE("unique"),
        @Json(name = "client_request_use_for")
        CLIENT_REQUEST_USE_FOR("client_request_use_for"),
        @Json(name = "seen_by_provider")
        SEEN_BY_PROVIDER("seen_by_provider"),
        @Json(name = "required")
        REQUIRED("required");

        private String value;

        FlagsEnum(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return String.valueOf(value);
        }
    }

    /*-*****************************-*/
    /*-             Json            -*/
    /*-*****************************-*/
    public static CustomField[] fromJsonArray(JsonArray array) {
        CustomField[] list = new CustomField[array.size()];
        for (int i = 0; i < array.size(); i++) {
            list[i] = fromJson(array.getJsonObject(i));
        }
        return list;
    }

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

    /*-*********************************************-*/
    /*-			Parcelable Implementation           -*/
    /*-*********************************************-*/
    public static final Parcelable.Creator<CustomField> CREATOR = new Parcelable.Creator<CustomField>() {

        @Override
        public CustomField createFromParcel(Parcel source) {
            try {
                return CustomField.fromJson((JsonObject) source.readParcelable(JsonObject.class.getClassLoader()));
            } catch (Exception ex) {
                Log.v(TAG, ex);
                return null;
            }
        }

        @Override
        public CustomField[] newArray(int size) {
            return new CustomField[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(toJson(), flags);
    }
}
