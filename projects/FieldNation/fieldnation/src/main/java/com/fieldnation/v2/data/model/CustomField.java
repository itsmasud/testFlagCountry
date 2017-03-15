package com.fieldnation.v2.data.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.fieldnation.fnjson.JsonArray;
import com.fieldnation.fnjson.JsonObject;
import com.fieldnation.fnjson.annotations.Json;
import com.fieldnation.fnjson.annotations.Source;
import com.fieldnation.fnlog.Log;

import java.text.ParseException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

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
    private JsonObject SOURCE;

    public CustomField() {
        SOURCE = new JsonObject();
    }

    public CustomField(JsonObject obj) {
        SOURCE = obj;
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
        try {
            if (_actions != null)
                return _actions;

            if (SOURCE.has("actions") && SOURCE.get("actions") != null) {
                _actions = ActionsEnum.fromJsonArray(SOURCE.getJsonArray("actions"));
            }

        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

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
        try {
            if (_category == null && SOURCE.has("category") && SOURCE.get("category") != null)
                _category = SOURCE.getString("category");
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

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
        try {
            if (_dependency == null && SOURCE.has("dependency") && SOURCE.get("dependency") != null)
                _dependency = CustomFieldDependency.fromJson(SOURCE.getJsonObject("dependency"));
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        if (_dependency != null && _dependency.isSet())
            return _dependency;

        return null;
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
        try {
            if (_flags != null)
                return _flags;

            if (SOURCE.has("flags") && SOURCE.get("flags") != null) {
                _flags = FlagsEnum.fromJsonArray(SOURCE.getJsonArray("flags"));
            }

        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

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
        try {
            if (_id == null && SOURCE.has("id") && SOURCE.get("id") != null)
                _id = SOURCE.getInt("id");
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

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
        try {
            if (_name == null && SOURCE.has("name") && SOURCE.get("name") != null)
                _name = SOURCE.getString("name");
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

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
        try {
            if (_options != null)
                return _options;

            if (SOURCE.has("options") && SOURCE.get("options") != null) {
                JsonArray ja = SOURCE.getJsonArray("options");
                _options = ja.toArray(new String[ja.size()]);
            }

        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

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
        try {
            if (_role == null && SOURCE.has("role") && SOURCE.get("role") != null)
                _role = RoleEnum.fromString(SOURCE.getString("role"));
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

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
        try {
            if (_tip == null && SOURCE.has("tip") && SOURCE.get("tip") != null)
                _tip = SOURCE.getString("tip");
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

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
        try {
            if (_type == null && SOURCE.has("type") && SOURCE.get("type") != null)
                _type = TypeEnum.fromString(SOURCE.getString("type"));
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

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
        try {
            if (_value == null && SOURCE.has("value") && SOURCE.get("value") != null)
                _value = SOURCE.getString("value");
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

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

        public static RoleEnum fromString(String value) {
            RoleEnum[] values = values();
            for (RoleEnum v : values) {
                if (v.value.equals(value))
                    return v;
            }
            return null;
        }

        public static RoleEnum[] fromJsonArray(JsonArray jsonArray) {
            RoleEnum[] list = new RoleEnum[jsonArray.size()];
            for (int i = 0; i < list.length; i++) {
                list[i] = fromString(jsonArray.getString(i));
            }
            return list;
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

        public static TypeEnum fromString(String value) {
            TypeEnum[] values = values();
            for (TypeEnum v : values) {
                if (v.value.equals(value))
                    return v;
            }
            return null;
        }

        public static TypeEnum[] fromJsonArray(JsonArray jsonArray) {
            TypeEnum[] list = new TypeEnum[jsonArray.size()];
            for (int i = 0; i < list.length; i++) {
                list[i] = fromString(jsonArray.getString(i));
            }
            return list;
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

        public static ActionsEnum fromString(String value) {
            ActionsEnum[] values = values();
            for (ActionsEnum v : values) {
                if (v.value.equals(value))
                    return v;
            }
            return null;
        }

        public static ActionsEnum[] fromJsonArray(JsonArray jsonArray) {
            ActionsEnum[] list = new ActionsEnum[jsonArray.size()];
            for (int i = 0; i < list.length; i++) {
                list[i] = fromString(jsonArray.getString(i));
            }
            return list;
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

        public static FlagsEnum fromString(String value) {
            FlagsEnum[] values = values();
            for (FlagsEnum v : values) {
                if (v.value.equals(value))
                    return v;
            }
            return null;
        }

        public static FlagsEnum[] fromJsonArray(JsonArray jsonArray) {
            FlagsEnum[] list = new FlagsEnum[jsonArray.size()];
            for (int i = 0; i < list.length; i++) {
                list[i] = fromString(jsonArray.getString(i));
            }
            return list;
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
            return new CustomField(obj);
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

    /*-*****************************-*/
    /*-         Human Code          -*/
    /*-*****************************-*/

    public boolean isSet() {
        return getId() != null && getId() != 0;
    }

    private Set<ActionsEnum> _actionsSet = null;

    public Set<ActionsEnum> getActionsSet() {
        if (_actionsSet == null) {
            _actionsSet = new HashSet<>();
            _actionsSet.addAll(Arrays.asList(getActions()));
        }
        return _actionsSet;
    }

    private Set<FlagsEnum> _flagsSet = null;

    public Set<FlagsEnum> getFlagsSet() {
        if (_flagsSet == null) {
            _flagsSet = new HashSet<>();
            _flagsSet.addAll(Arrays.asList(getFlags()));
        }
        return _flagsSet;
    }
}
