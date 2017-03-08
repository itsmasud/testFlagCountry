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

public class Hold implements Parcelable {
    private static final String TAG = "Hold";

    @Json(name = "acknowledged")
    private Boolean _acknowledged;

    @Json(name = "actions")
    private ActionsEnum[] _actions;

    @Json(name = "id")
    private Integer _id;

    @Json(name = "name")
    private String _name;

    @Json(name = "reason")
    private String _reason;

    @Source
    private JsonObject SOURCE;

    public Hold() {
        SOURCE = new JsonObject();
    }

    public Hold(JsonObject obj) {
        SOURCE = obj;
    }

    public void setAcknowledged(Boolean acknowledged) throws ParseException {
        _acknowledged = acknowledged;
        SOURCE.put("acknowledged", acknowledged);
    }

    public Boolean getAcknowledged() {
        try {
            if (_acknowledged != null)
                return _acknowledged;

            if (SOURCE.has("acknowledged") && SOURCE.get("acknowledged") != null)
                _acknowledged = SOURCE.getBoolean("acknowledged");

        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _acknowledged;
    }

    public Hold acknowledged(Boolean acknowledged) throws ParseException {
        _acknowledged = acknowledged;
        SOURCE.put("acknowledged", acknowledged);
        return this;
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

    public Hold actions(ActionsEnum[] actions) throws ParseException {
        _actions = actions;
        JsonArray ja = new JsonArray();
        for (ActionsEnum item : actions) {
            ja.add(item.toString());
        }
        SOURCE.put("actions", ja, true);
        return this;
    }

    public void setId(Integer id) throws ParseException {
        _id = id;
        SOURCE.put("id", id);
    }

    public Integer getId() {
        try {
            if (_id != null)
                return _id;

            if (SOURCE.has("id") && SOURCE.get("id") != null)
                _id = SOURCE.getInt("id");

        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _id;
    }

    public Hold id(Integer id) throws ParseException {
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
            if (_name != null)
                return _name;

            if (SOURCE.has("name") && SOURCE.get("name") != null)
                _name = SOURCE.getString("name");

        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _name;
    }

    public Hold name(String name) throws ParseException {
        _name = name;
        SOURCE.put("name", name);
        return this;
    }

    public void setReason(String reason) throws ParseException {
        _reason = reason;
        SOURCE.put("reason", reason);
    }

    public String getReason() {
        try {
            if (_reason != null)
                return _reason;

            if (SOURCE.has("reason") && SOURCE.get("reason") != null)
                _reason = SOURCE.getString("reason");

        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _reason;
    }

    public Hold reason(String reason) throws ParseException {
        _reason = reason;
        SOURCE.put("reason", reason);
        return this;
    }

    /*-******************************-*/
    /*-             Enums            -*/
    /*-******************************-*/
    public enum ActionsEnum {
        @Json(name = "unkown")
        UNKOWN("unkown");

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

    /*-*****************************-*/
    /*-             Json            -*/
    /*-*****************************-*/
    public static JsonArray toJsonArray(Hold[] array) {
        JsonArray list = new JsonArray();
        for (Hold item : array) {
            list.add(item.getJson());
        }
        return list;
    }

    public static Hold[] fromJsonArray(JsonArray array) {
        Hold[] list = new Hold[array.size()];
        for (int i = 0; i < array.size(); i++) {
            list[i] = fromJson(array.getJsonObject(i));
        }
        return list;
    }

    public static Hold fromJson(JsonObject obj) {
        try {
            return new Hold(obj);
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
    public static final Parcelable.Creator<Hold> CREATOR = new Parcelable.Creator<Hold>() {

        @Override
        public Hold createFromParcel(Parcel source) {
            try {
                return Hold.fromJson((JsonObject) source.readParcelable(JsonObject.class.getClassLoader()));
            } catch (Exception ex) {
                Log.v(TAG, ex);
                return null;
            }
        }

        @Override
        public Hold[] newArray(int size) {
            return new Hold[size];
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
