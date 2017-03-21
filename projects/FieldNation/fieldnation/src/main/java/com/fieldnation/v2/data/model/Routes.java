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

public class Routes implements Parcelable {
    private static final String TAG = "Routes";

    @Json(name = "actions")
    private ActionsEnum[] _actions;

    @Json(name = "metadata")
    private ListEnvelope _metadata;

    @Json(name = "open_route")
    private Route _openRoute;

    @Json(name = "results")
    private Route[] _results;

    @Source
    private JsonObject SOURCE;

    public Routes() {
        SOURCE = new JsonObject();
    }

    public Routes(JsonObject obj) {
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

    public Routes actions(ActionsEnum[] actions) throws ParseException {
        _actions = actions;
        JsonArray ja = new JsonArray();
        for (ActionsEnum item : actions) {
            ja.add(item.toString());
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

        if (_metadata != null && _metadata.isSet())
        return _metadata;

        return null;
    }

    public Routes metadata(ListEnvelope metadata) throws ParseException {
        _metadata = metadata;
        SOURCE.put("metadata", metadata.getJson());
        return this;
    }

    public void setOpenRoute(Route openRoute) throws ParseException {
        _openRoute = openRoute;
        SOURCE.put("open_route", openRoute.getJson());
    }

    public Route getOpenRoute() {
        try {
            if (_openRoute == null && SOURCE.has("open_route") && SOURCE.get("open_route") != null)
                _openRoute = Route.fromJson(SOURCE.getJsonObject("open_route"));
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        if (_openRoute != null && _openRoute.isSet())
            return _openRoute;

        return null;
    }

    public Routes openRoute(Route openRoute) throws ParseException {
        _openRoute = openRoute;
        SOURCE.put("open_route", openRoute.getJson());
        return this;
    }

    public void setResults(Route[] results) throws ParseException {
        _results = results;
        SOURCE.put("results", Route.toJsonArray(results));
    }

    public Route[] getResults() {
        try {
            if (_results != null)
                return _results;

            if (SOURCE.has("results") && SOURCE.get("results") != null) {
                _results = Route.fromJsonArray(SOURCE.getJsonArray("results"));
            }

        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _results;
    }

    public Routes results(Route[] results) throws ParseException {
        _results = results;
        SOURCE.put("results", Route.toJsonArray(results), true);
        return this;
    }

    /*-******************************-*/
    /*-             Enums            -*/
    /*-******************************-*/
    public enum ActionsEnum {
        @Json(name = "add")
        ADD("add");

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
    public static JsonArray toJsonArray(Routes[] array) {
        JsonArray list = new JsonArray();
        for (Routes item : array) {
            list.add(item.getJson());
        }
        return list;
    }

    public static Routes[] fromJsonArray(JsonArray array) {
        Routes[] list = new Routes[array.size()];
        for (int i = 0; i < array.size(); i++) {
            list[i] = fromJson(array.getJsonObject(i));
        }
        return list;
    }

    public static Routes fromJson(JsonObject obj) {
        try {
            return new Routes(obj);
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
    public static final Parcelable.Creator<Routes> CREATOR = new Parcelable.Creator<Routes>() {

        @Override
        public Routes createFromParcel(Parcel source) {
            try {
                return Routes.fromJson((JsonObject) source.readParcelable(JsonObject.class.getClassLoader()));
            } catch (Exception ex) {
                Log.v(TAG, ex);
                return null;
            }
        }

        @Override
        public Routes[] newArray(int size) {
            return new Routes[size];
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
