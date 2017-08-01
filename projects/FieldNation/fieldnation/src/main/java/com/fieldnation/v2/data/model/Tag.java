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
import com.fieldnation.fntools.misc;

import java.text.ParseException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by dmgen from swagger.
 */

public class Tag implements Parcelable {
    private static final String TAG = "Tag";

    @Json(name = "actions")
    private ActionsEnum[] _actions;

    @Json(name = "author")
    private User _author;

    @Json(name = "created")
    private Date _created;

    @Json(name = "hex_color")
    private String _hexColor;

    @Json(name = "id")
    private Integer _id;

    @Json(name = "label")
    private String _label;

    @Json(name = "types")
    private TypesEnum[] _types;

    @Json(name = "valid_statuses")
    private ValidStatusesEnum[] _validStatuses;

    @Source
    private JsonObject SOURCE;

    public Tag() {
        SOURCE = new JsonObject();
    }

    public Tag(JsonObject obj) {
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

        if (_actions == null)
            _actions = new ActionsEnum[0];

        return _actions;
    }

    public Tag actions(ActionsEnum[] actions) throws ParseException {
        _actions = actions;
        JsonArray ja = new JsonArray();
        for (ActionsEnum item : actions) {
            ja.add(item.toString());
        }
        SOURCE.put("actions", ja, true);
        return this;
    }

    public void setAuthor(User author) throws ParseException {
        _author = author;
        SOURCE.put("author", author.getJson());
    }

    public User getAuthor() {
        try {
            if (_author == null && SOURCE.has("author") && SOURCE.get("author") != null)
                _author = User.fromJson(SOURCE.getJsonObject("author"));
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        if (_author == null)
            _author = new User();

            return _author;
    }

    public Tag author(User author) throws ParseException {
        _author = author;
        SOURCE.put("author", author.getJson());
        return this;
    }

    public void setCreated(Date created) throws ParseException {
        _created = created;
        SOURCE.put("created", created.getJson());
    }

    public Date getCreated() {
        try {
            if (_created == null && SOURCE.has("created") && SOURCE.get("created") != null)
                _created = Date.fromJson(SOURCE.getJsonObject("created"));
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        if (_created == null)
            _created = new Date();

            return _created;
    }

    public Tag created(Date created) throws ParseException {
        _created = created;
        SOURCE.put("created", created.getJson());
        return this;
    }

    public void setHexColor(String hexColor) throws ParseException {
        _hexColor = hexColor;
        SOURCE.put("hex_color", hexColor);
    }

    public String getHexColor() {
        try {
            if (_hexColor == null && SOURCE.has("hex_color") && SOURCE.get("hex_color") != null)
                _hexColor = SOURCE.getString("hex_color");
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _hexColor;
    }

    public Tag hexColor(String hexColor) throws ParseException {
        _hexColor = hexColor;
        SOURCE.put("hex_color", hexColor);
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

    public Tag id(Integer id) throws ParseException {
        _id = id;
        SOURCE.put("id", id);
        return this;
    }

    public void setLabel(String label) throws ParseException {
        _label = label;
        SOURCE.put("label", label);
    }

    public String getLabel() {
        try {
            if (_label == null && SOURCE.has("label") && SOURCE.get("label") != null)
                _label = SOURCE.getString("label");
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _label;
    }

    public Tag label(String label) throws ParseException {
        _label = label;
        SOURCE.put("label", label);
        return this;
    }

    public void setTypes(TypesEnum[] types) throws ParseException {
        _types = types;
        JsonArray ja = new JsonArray();
        for (TypesEnum item : types) {
            ja.add(item.toString());
        }
        SOURCE.put("types", ja);
    }

    public TypesEnum[] getTypes() {
        try {
            if (_types != null)
                return _types;

            if (SOURCE.has("types") && SOURCE.get("types") != null) {
                _types = TypesEnum.fromJsonArray(SOURCE.getJsonArray("types"));
            }

        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        if (_types == null)
            _types = new TypesEnum[0];

        return _types;
    }

    public Tag types(TypesEnum[] types) throws ParseException {
        _types = types;
        JsonArray ja = new JsonArray();
        for (TypesEnum item : types) {
            ja.add(item.toString());
        }
        SOURCE.put("types", ja, true);
        return this;
    }

    public void setValidStatuses(ValidStatusesEnum[] validStatuses) throws ParseException {
        _validStatuses = validStatuses;
        JsonArray ja = new JsonArray();
        for (ValidStatusesEnum item : validStatuses) {
            ja.add(item.toString());
        }
        SOURCE.put("valid_statuses", ja);
    }

    public ValidStatusesEnum[] getValidStatuses() {
        try {
            if (_validStatuses != null)
                return _validStatuses;

            if (SOURCE.has("valid_statuses") && SOURCE.get("valid_statuses") != null) {
                _validStatuses = ValidStatusesEnum.fromJsonArray(SOURCE.getJsonArray("valid_statuses"));
            }

        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        if (_validStatuses == null)
            _validStatuses = new ValidStatusesEnum[0];

        return _validStatuses;
    }

    public Tag validStatuses(ValidStatusesEnum[] validStatuses) throws ParseException {
        _validStatuses = validStatuses;
        JsonArray ja = new JsonArray();
        for (ValidStatusesEnum item : validStatuses) {
            ja.add(item.toString());
        }
        SOURCE.put("valid_statuses", ja, true);
        return this;
    }

    /*-******************************-*/
    /*-             Enums            -*/
    /*-******************************-*/
    public enum ActionsEnum {
        @Json(name = "delete")
        DELETE("delete"),
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

    public enum TypesEnum {
        @Json(name = "custom_company")
        CUSTOM_COMPANY("custom_company"),
        @Json(name = "hide_from_provider")
        HIDE_FROM_PROVIDER("hide_from_provider"),
        @Json(name = "hold")
        HOLD("hold"),
        @Json(name = "problem")
        PROBLEM("problem"),
        @Json(name = "provider_can_edit")
        PROVIDER_CAN_EDIT("provider_can_edit"),
        @Json(name = "show_in_dashboard")
        SHOW_IN_DASHBOARD("show_in_dashboard"),
        @Json(name = "substatus")
        SUBSTATUS("substatus");

        private String value;

        TypesEnum(String value) {
            this.value = value;
        }

        public static TypesEnum fromString(String value) {
            TypesEnum[] values = values();
            for (TypesEnum v : values) {
                if (v.value.equals(value))
                    return v;
            }
            return null;
        }

        public static TypesEnum[] fromJsonArray(JsonArray jsonArray) {
            TypesEnum[] list = new TypesEnum[jsonArray.size()];
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

    public enum ValidStatusesEnum {
        @Json(name = "approved")
        APPROVED("approved"),
        @Json(name = "assigned")
        ASSIGNED("assigned"),
        @Json(name = "canceled")
        CANCELED("canceled"),
        @Json(name = "created")
        CREATED("created"),
        @Json(name = "paid")
        PAID("paid"),
        @Json(name = "published")
        PUBLISHED("published"),
        @Json(name = "routed")
        ROUTED("routed"),
        @Json(name = "workdone")
        WORKDONE("workdone");

        private String value;

        ValidStatusesEnum(String value) {
            this.value = value;
        }

        public static ValidStatusesEnum fromString(String value) {
            ValidStatusesEnum[] values = values();
            for (ValidStatusesEnum v : values) {
                if (v.value.equals(value))
                    return v;
            }
            return null;
        }

        public static ValidStatusesEnum[] fromJsonArray(JsonArray jsonArray) {
            ValidStatusesEnum[] list = new ValidStatusesEnum[jsonArray.size()];
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
    public static JsonArray toJsonArray(Tag[] array) {
        JsonArray list = new JsonArray();
        for (Tag item : array) {
            list.add(item.getJson());
        }
        return list;
    }

    public static Tag[] fromJsonArray(JsonArray array) {
        Tag[] list = new Tag[array.size()];
        for (int i = 0; i < array.size(); i++) {
            list[i] = fromJson(array.getJsonObject(i));
        }
        return list;
    }

    public static Tag fromJson(JsonObject obj) {
        try {
            return new Tag(obj);
        } catch (Exception ex) {
            Log.v(TAG, ex);
            return null;
        }
    }

    public JsonObject getJson() {
        return SOURCE;
    }

    /*-*********************************************-*/
    /*-			Parcelable Implementation           -*/
    /*-*********************************************-*/
    public static final Parcelable.Creator<Tag> CREATOR = new Parcelable.Creator<Tag>() {

        @Override
        public Tag createFromParcel(Parcel source) {
            try {
                return Tag.fromJson((JsonObject) source.readParcelable(JsonObject.class.getClassLoader()));
            } catch (Exception ex) {
                Log.v(TAG, ex);
                return null;
            }
        }

        @Override
        public Tag[] newArray(int size) {
            return new Tag[size];
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

}
