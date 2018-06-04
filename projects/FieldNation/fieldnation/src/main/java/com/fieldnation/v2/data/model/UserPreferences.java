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

public class UserPreferences implements Parcelable {
    private static final String TAG = "UserPreferences";

    @Json(name = "actions")
    private String[] _actions;

    @Json(name = "metadata")
    private ListEnvelope _metadata;

    @Json(name = "results")
    private UserPreferencesResults[] _results;

    @Source
    private JsonObject SOURCE;

    public UserPreferences() {
        SOURCE = new JsonObject();
    }

    public UserPreferences(JsonObject obj) {
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

    public UserPreferences actions(String[] actions) throws ParseException {
        _actions = actions;
        JsonArray ja = new JsonArray();
        for (String item : actions) {
            ja.add(item);
        }
        SOURCE.put("actions", ja, true);
        return this;
    }

    public void setMetadata(ListEnvelope metadata) throws ParseException {
        _metadata = metadata;
        SOURCE.put("metadata", metadata.getJson());
    }

    public ListEnvelope getMetadata() {
        try {
            if (_metadata == null && SOURCE.has("metadata") && SOURCE.get("metadata") != null)
                _metadata = ListEnvelope.fromJson(SOURCE.getJsonObject("metadata"));
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        if (_metadata == null)
            _metadata = new ListEnvelope();

        return _metadata;
    }

    public UserPreferences metadata(ListEnvelope metadata) throws ParseException {
        _metadata = metadata;
        SOURCE.put("metadata", metadata.getJson());
        return this;
    }

    public void setResults(UserPreferencesResults[] results) throws ParseException {
        _results = results;
        SOURCE.put("results", UserPreferencesResults.toJsonArray(results));
    }

    public UserPreferencesResults[] getResults() {
        try {
            if (_results != null)
                return _results;

            if (SOURCE.has("results") && SOURCE.get("results") != null) {
                _results = UserPreferencesResults.fromJsonArray(SOURCE.getJsonArray("results"));
            }

        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        if (_results == null)
            _results = new UserPreferencesResults[0];

        return _results;
    }

    public UserPreferences results(UserPreferencesResults[] results) throws ParseException {
        _results = results;
        SOURCE.put("results", UserPreferencesResults.toJsonArray(results), true);
        return this;
    }

    /*-*****************************-*/
    /*-             Json            -*/
    /*-*****************************-*/
    public static JsonArray toJsonArray(UserPreferences[] array) {
        JsonArray list = new JsonArray();
        for (UserPreferences item : array) {
            list.add(item.getJson());
        }
        return list;
    }

    public static UserPreferences[] fromJsonArray(JsonArray array) {
        UserPreferences[] list = new UserPreferences[array.size()];
        for (int i = 0; i < array.size(); i++) {
            list[i] = fromJson(array.getJsonObject(i));
        }
        return list;
    }

    public static UserPreferences fromJson(JsonObject obj) {
        try {
            return new UserPreferences(obj);
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
    public static final Creator<UserPreferences> CREATOR = new Creator<UserPreferences>() {

        @Override
        public UserPreferences createFromParcel(Parcel source) {
            try {
                return UserPreferences.fromJson((JsonObject) source.readParcelable(JsonObject.class.getClassLoader()));
            } catch (Exception ex) {
                Log.v(TAG, ex);
                return null;
            }
        }

        @Override
        public UserPreferences[] newArray(int size) {
            return new UserPreferences[size];
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
