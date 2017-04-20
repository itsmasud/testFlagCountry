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

public class Blocks implements Parcelable {
    private static final String TAG = "Blocks";

    @Json(name = "actions")
    private ActionsEnum[] _actions;

    @Json(name = "author")
    private User _author;

    @Json(name = "blocked")
    private Boolean _blocked;

    @Json(name = "category")
    private String _category;

    @Json(name = "created")
    private Date _created;

    @Json(name = "reason")
    private String _reason;

    @Json(name = "types")
    private TypesEnum[] _types;

    @Source
    private JsonObject SOURCE;

    public Blocks() {
        SOURCE = new JsonObject();
    }

    public Blocks(JsonObject obj) {
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

    public Blocks actions(ActionsEnum[] actions) throws ParseException {
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

        if (_author != null && _author.isSet())
            return _author;

        return null;
    }

    public Blocks author(User author) throws ParseException {
        _author = author;
        SOURCE.put("author", author.getJson());
        return this;
    }

    public void setBlocked(Boolean blocked) throws ParseException {
        _blocked = blocked;
        SOURCE.put("blocked", blocked);
    }

    public Boolean getBlocked() {
        try {
            if (_blocked == null && SOURCE.has("blocked") && SOURCE.get("blocked") != null)
                _blocked = SOURCE.getBoolean("blocked");
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _blocked;
    }

    public Blocks blocked(Boolean blocked) throws ParseException {
        _blocked = blocked;
        SOURCE.put("blocked", blocked);
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

    public Blocks category(String category) throws ParseException {
        _category = category;
        SOURCE.put("category", category);
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

        if (_created != null && _created.isSet())
            return _created;

        return null;
    }

    public Blocks created(Date created) throws ParseException {
        _created = created;
        SOURCE.put("created", created.getJson());
        return this;
    }

    public void setReason(String reason) throws ParseException {
        _reason = reason;
        SOURCE.put("reason", reason);
    }

    public String getReason() {
        try {
            if (_reason == null && SOURCE.has("reason") && SOURCE.get("reason") != null)
                _reason = SOURCE.getString("reason");
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _reason;
    }

    public Blocks reason(String reason) throws ParseException {
        _reason = reason;
        SOURCE.put("reason", reason);
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

        return _types;
    }

    public Blocks types(TypesEnum[] types) throws ParseException {
        _types = types;
        JsonArray ja = new JsonArray();
        for (TypesEnum item : types) {
            ja.add(item.toString());
        }
        SOURCE.put("types", ja, true);
        return this;
    }

    /*-******************************-*/
    /*-             Enums            -*/
    /*-******************************-*/
    public enum ActionsEnum {
        @Json(name = "add")
        ADD("add"),
        @Json(name = "delete")
        DELETE("delete");

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
        @Json(name = "client")
        CLIENT("client"),
        @Json(name = "company")
        COMPANY("company"),
        @Json(name = "location")
        LOCATION("location"),
        @Json(name = "project")
        PROJECT("project"),
        @Json(name = "service_company")
        SERVICE_COMPANY("service_company"),
        @Json(name = "work_order")
        WORK_ORDER("work_order");

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

    /*-*****************************-*/
    /*-             Json            -*/
    /*-*****************************-*/
    public static JsonArray toJsonArray(Blocks[] array) {
        JsonArray list = new JsonArray();
        for (Blocks item : array) {
            list.add(item.getJson());
        }
        return list;
    }

    public static Blocks[] fromJsonArray(JsonArray array) {
        Blocks[] list = new Blocks[array.size()];
        for (int i = 0; i < array.size(); i++) {
            list[i] = fromJson(array.getJsonObject(i));
        }
        return list;
    }

    public static Blocks fromJson(JsonObject obj) {
        try {
            return new Blocks(obj);
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
    public static final Parcelable.Creator<Blocks> CREATOR = new Parcelable.Creator<Blocks>() {

        @Override
        public Blocks createFromParcel(Parcel source) {
            try {
                return Blocks.fromJson((JsonObject) source.readParcelable(JsonObject.class.getClassLoader()));
            } catch (Exception ex) {
                Log.v(TAG, ex);
                return null;
            }
        }

        @Override
        public Blocks[] newArray(int size) {
            return new Blocks[size];
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
        return true;
    }
}
