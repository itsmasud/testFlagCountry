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

public class StoredLocations implements Parcelable {
    private static final String TAG = "StoredLocations";

    @Json(name = "actions")
    private ActionsEnum[] _actions;

    @Json(name = "mode")
    private ModeEnum _mode;

    @Json(name = "results")
    private Location[] _results;

    @Json(name = "role")
    private String _role;

    @Json(name = "work_order_id")
    private Integer _workOrderId;

    @Source
    private JsonObject SOURCE;

    public StoredLocations() {
        SOURCE = new JsonObject();
    }

    public StoredLocations(JsonObject obj) {
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

    public StoredLocations actions(ActionsEnum[] actions) throws ParseException {
        _actions = actions;
        JsonArray ja = new JsonArray();
        for (ActionsEnum item : actions) {
            ja.add(item.toString());
        }
        SOURCE.put("actions", ja, true);
        return this;
    }

    public void setMode(ModeEnum mode) throws ParseException {
        _mode = mode;
        SOURCE.put("mode", mode.toString());
    }

    public ModeEnum getMode() {
        try {
            if (_mode != null)
                return _mode;

            if (SOURCE.has("mode") && SOURCE.get("mode") != null)
                _mode = ModeEnum.fromString(SOURCE.getString("mode"));

        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _mode;
    }

    public StoredLocations mode(ModeEnum mode) throws ParseException {
        _mode = mode;
        SOURCE.put("mode", mode.toString());
        return this;
    }

    public void setResults(Location[] results) throws ParseException {
        _results = results;
        SOURCE.put("results", Location.toJsonArray(results));
    }

    public Location[] getResults() {
        try {
            if (_results != null)
                return _results;

            if (SOURCE.has("results") && SOURCE.get("results") != null) {
                _results = Location.fromJsonArray(SOURCE.getJsonArray("results"));
            }

        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _results;
    }

    public StoredLocations results(Location[] results) throws ParseException {
        _results = results;
        SOURCE.put("results", Location.toJsonArray(results), true);
        return this;
    }

    public void setRole(String role) throws ParseException {
        _role = role;
        SOURCE.put("role", role);
    }

    public String getRole() {
        try {
            if (_role != null)
                return _role;

            if (SOURCE.has("role") && SOURCE.get("role") != null)
                _role = SOURCE.getString("role");

        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _role;
    }

    public StoredLocations role(String role) throws ParseException {
        _role = role;
        SOURCE.put("role", role);
        return this;
    }

    public void setWorkOrderId(Integer workOrderId) throws ParseException {
        _workOrderId = workOrderId;
        SOURCE.put("work_order_id", workOrderId);
    }

    public Integer getWorkOrderId() {
        try {
            if (_workOrderId != null)
                return _workOrderId;

            if (SOURCE.has("work_order_id") && SOURCE.get("work_order_id") != null)
                _workOrderId = SOURCE.getInt("work_order_id");

        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _workOrderId;
    }

    public StoredLocations workOrderId(Integer workOrderId) throws ParseException {
        _workOrderId = workOrderId;
        SOURCE.put("work_order_id", workOrderId);
        return this;
    }

    /*-******************************-*/
    /*-             Enums            -*/
    /*-******************************-*/
    public enum ModeEnum {
        @Json(name = "custom")
        CUSTOM("custom"),
        @Json(name = "location")
        LOCATION("location"),
        @Json(name = "remote")
        REMOTE("remote");

        private String value;

        ModeEnum(String value) {
            this.value = value;
        }

        public static ModeEnum fromString(String value) {
            ModeEnum[] values = values();
            for (ModeEnum v : values) {
                if (v.value.equals(value))
                    return v;
            }
            return null;
        }

        public static ModeEnum[] fromJsonArray(JsonArray jsonArray) {
            ModeEnum[] list = new ModeEnum[jsonArray.size()];
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

    /*-*****************************-*/
    /*-             Json            -*/
    /*-*****************************-*/
    public static JsonArray toJsonArray(StoredLocations[] array) {
        JsonArray list = new JsonArray();
        for (StoredLocations item : array) {
            list.add(item.getJson());
        }
        return list;
    }

    public static StoredLocations[] fromJsonArray(JsonArray array) {
        StoredLocations[] list = new StoredLocations[array.size()];
        for (int i = 0; i < array.size(); i++) {
            list[i] = fromJson(array.getJsonObject(i));
        }
        return list;
    }

    public static StoredLocations fromJson(JsonObject obj) {
        try {
            return new StoredLocations(obj);
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
    public static final Parcelable.Creator<StoredLocations> CREATOR = new Parcelable.Creator<StoredLocations>() {

        @Override
        public StoredLocations createFromParcel(Parcel source) {
            try {
                return StoredLocations.fromJson((JsonObject) source.readParcelable(JsonObject.class.getClassLoader()));
            } catch (Exception ex) {
                Log.v(TAG, ex);
                return null;
            }
        }

        @Override
        public StoredLocations[] newArray(int size) {
            return new StoredLocations[size];
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
