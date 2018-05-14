package com.fieldnation.v2.data.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.fieldnation.fnjson.JsonArray;
import com.fieldnation.fnjson.JsonObject;
import com.fieldnation.fnjson.annotations.Json;
import com.fieldnation.fnjson.annotations.Source;
import com.fieldnation.fnlog.Log;

import java.text.ParseException;

/**
 * Created by dmgen from swagger.
 */

public class UserPreferencesResults implements Parcelable {
    private static final String TAG = "UserPreferencesResults";

    @Json(name = "actions")
    private String[] _actions;

    @Json(name = "id")
    private Double _id;

    @Json(name = "name")
    private String _name;

    @Json(name = "value")
    private String _value;

    @Source
    private JsonObject SOURCE;

    public UserPreferencesResults() {
        SOURCE = new JsonObject();
    }

    public UserPreferencesResults(JsonObject obj) {
        SOURCE = obj;
    }

    public void setActions(String[] actions) throws ParseException {
        _actions = actions;
        JsonArray ja = new JsonArray();
        for (String item : actions) {
            ja.add(item);
        }
        SOURCE.put("actions", ja);
    }

    public String[] getActions() {
        try {
            if (_actions != null)
                return _actions;

            if (SOURCE.has("actions") && SOURCE.get("actions") != null) {
                JsonArray ja = SOURCE.getJsonArray("actions");
                _actions = ja.toArray(new String[ja.size()]);
            }

        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        if (_actions == null)
            _actions = new String[0];

        return _actions;
    }

    public UserPreferencesResults actions(String[] actions) throws ParseException {
        _actions = actions;
        JsonArray ja = new JsonArray();
        for (String item : actions) {
            ja.add(item);
        }
        SOURCE.put("actions", ja, true);
        return this;
    }

    public void setId(Double id) throws ParseException {
        _id = id;
        SOURCE.put("id", id);
    }

    public Double getId() {
        try {
            if (_id == null && SOURCE.has("id") && SOURCE.get("id") != null)
                _id = SOURCE.getDouble("id");
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _id;
    }

    public UserPreferencesResults id(Double id) throws ParseException {
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

    public UserPreferencesResults name(String name) throws ParseException {
        _name = name;
        SOURCE.put("name", name);
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

    public UserPreferencesResults value(String value) throws ParseException {
        _value = value;
        SOURCE.put("value", value);
        return this;
    }

    /*-*****************************-*/
    /*-             Json            -*/
    /*-*****************************-*/
    public static JsonArray toJsonArray(UserPreferencesResults[] array) {
        JsonArray list = new JsonArray();
        for (UserPreferencesResults item : array) {
            list.add(item.getJson());
        }
        return list;
    }

    public static UserPreferencesResults[] fromJsonArray(JsonArray array) {
        UserPreferencesResults[] list = new UserPreferencesResults[array.size()];
        for (int i = 0; i < array.size(); i++) {
            list[i] = fromJson(array.getJsonObject(i));
        }
        return list;
    }

    public static UserPreferencesResults fromJson(JsonObject obj) {
        try {
            return new UserPreferencesResults(obj);
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
    public static final Creator<UserPreferencesResults> CREATOR = new Creator<UserPreferencesResults>() {

        @Override
        public UserPreferencesResults createFromParcel(Parcel source) {
            try {
                return UserPreferencesResults.fromJson((JsonObject) source.readParcelable(JsonObject.class.getClassLoader()));
            } catch (Exception ex) {
                Log.v(TAG, ex);
                return null;
            }
        }

        @Override
        public UserPreferencesResults[] newArray(int size) {
            return new UserPreferencesResults[size];
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
