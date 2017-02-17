package com.fieldnation.v2.data.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.fieldnation.fnjson.JsonArray;
import com.fieldnation.fnjson.JsonObject;
import com.fieldnation.fnjson.Serializer;
import com.fieldnation.fnjson.Unserializer;
import com.fieldnation.fnjson.annotations.Json;
import com.fieldnation.fnjson.annotations.Source;
import com.fieldnation.fnlog.Log;

import java.text.ParseException;

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

    @Source
    private JsonObject SOURCE = new JsonObject();

    public CustomField() {
    }

    public void setActions(ActionsEnum[] actions) throws ParseException {
        _actions = actions;
        JsonArray ja = new JsonArray();
        for (ActionsEnum item : actions) {
            ja.add(item.toString());
        }
        SOURCE.put("actions", ja);
    }

    public ActionsEnum[] getActions() {
        return _actions;
    }

    public CustomField actions(ActionsEnum[] actions) throws ParseException {
        _actions = actions;
        JsonArray ja = new JsonArray();
        for (ActionsEnum item : actions) {
            ja.add(item.toString());
        }
        SOURCE.put("actions", ja, true);
        return this;
    }

    public void setCategory(String category) throws ParseException {
        _category = category;
        SOURCE.put("category", category);
    }

    public String getCategory() {
        return _category;
    }

    public CustomField category(String category) throws ParseException {
        _category = category;
        SOURCE.put("category", category);
        return this;
    }

    public void setDependency(CustomFieldDependency dependency) throws ParseException {
        _dependency = dependency;
        SOURCE.put("dependency", dependency.getJson());
    }

    public CustomFieldDependency getDependency() {
        return _dependency;
    }

    public CustomField dependency(CustomFieldDependency dependency) throws ParseException {
        _dependency = dependency;
        SOURCE.put("dependency", dependency.getJson());
        return this;
    }

    public void setFlags(FlagsEnum[] flags) throws ParseException {
        _flags = flags;
        JsonArray ja = new JsonArray();
        for (FlagsEnum item : flags) {
            ja.add(item.toString());
        }
        SOURCE.put("flags", ja);
    }

    public FlagsEnum[] getFlags() {
        return _flags;
    }

    public CustomField flags(FlagsEnum[] flags) throws ParseException {
        _flags = flags;
        JsonArray ja = new JsonArray();
        for (FlagsEnum item : flags) {
            ja.add(item.toString());
        }
        SOURCE.put("flags", ja, true);
        return this;
    }

    public void setId(Integer id) throws ParseException {
        _id = id;
        SOURCE.put("id", id);
    }

    public Integer getId() {
        return _id;
    }

    public CustomField id(Integer id) throws ParseException {
        _id = id;
        SOURCE.put("id", id);
        return this;
    }

    public void setName(String name) throws ParseException {
        _name = name;
        SOURCE.put("name", name);
    }

    public String getName() {
        return _name;
    }

    public CustomField name(String name) throws ParseException {
        _name = name;
        SOURCE.put("name", name);
        return this;
    }

    public void setOptions(String[] options) throws ParseException {
        _options = options;
        JsonArray ja = new JsonArray();
        for (String item : options) {
            ja.add(item);
        }
        SOURCE.put("options", ja);
    }

    public String[] getOptions() {
        return _options;
    }

    public CustomField options(String[] options) throws ParseException {
        _options = options;
        JsonArray ja = new JsonArray();
        for (String item : options) {
            ja.add(item);
        }
        SOURCE.put("options", ja, true);
        return this;
    }

    public void setRole(RoleEnum role) throws ParseException {
        _role = role;
        SOURCE.put("role", role.toString());
    }

    public RoleEnum getRole() {
        return _role;
    }

    public CustomField role(RoleEnum role) throws ParseException {
        _role = role;
        SOURCE.put("role", role.toString());
        return this;
    }

    public void setTip(String tip) throws ParseException {
        _tip = tip;
        SOURCE.put("tip", tip);
    }

    public String getTip() {
        return _tip;
    }

    public CustomField tip(String tip) throws ParseException {
        _tip = tip;
        SOURCE.put("tip", tip);
        return this;
    }

    public void setType(TypeEnum type) throws ParseException {
        _type = type;
        SOURCE.put("type", type.toString());
    }

    public TypeEnum getType() {
        return _type;
    }

    public CustomField type(TypeEnum type) throws ParseException {
        _type = type;
        SOURCE.put("type", type.toString());
        return this;
    }

    public void setValue(String value) throws ParseException {
        _value = value;
        SOURCE.put("value", value);
    }

    public String getValue() {
        return _value;
    }

    public CustomField value(String value) throws ParseException {
        _value = value;
        SOURCE.put("value", value);
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
        @Json(name = "date_time")
        DATE_TIME("date_time"),
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
        @Json(name = "client_request_required")
        CLIENT_REQUEST_REQUIRED("client_request_required"),
        @Json(name = "client_request_use_for")
        CLIENT_REQUEST_USE_FOR("client_request_use_for"),
        @Json(name = "included_in_alerts")
        INCLUDED_IN_ALERTS("included_in_alerts"),
        @Json(name = "internal_id")
        INTERNAL_ID("internal_id"),
        @Json(name = "required")
        REQUIRED("required"),
        @Json(name = "required_during_checkin")
        REQUIRED_DURING_CHECKIN("required_during_checkin"),
        @Json(name = "seen_by_clients")
        SEEN_BY_CLIENTS("seen_by_clients"),
        @Json(name = "seen_by_provider")
        SEEN_BY_PROVIDER("seen_by_provider"),
        @Json(name = "shown_in_header")
        SHOWN_IN_HEADER("shown_in_header"),
        @Json(name = "unique")
        UNIQUE("unique"),
        @Json(name = "visible_to_clients")
        VISIBLE_TO_CLIENTS("visible_to_clients");

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
    public static JsonArray toJsonArray(CustomField[] array) {
        JsonArray list = new JsonArray();
        for (CustomField item : array) {
            list.add(item.getJson());
        }
        return list;
    }

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

    public JsonObject getJson() {
        return SOURCE;
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
        dest.writeParcelable(getJson(), flags);
    }
}
