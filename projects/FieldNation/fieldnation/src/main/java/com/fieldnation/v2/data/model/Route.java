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

public class Route implements Parcelable {
    private static final String TAG = "Route";

    @Json(name = "actions")
    private ActionsEnum[] _actions;

    @Json(name = "assigned")
    private Boolean _assigned;

    @Json(name = "created")
    private Date _created;

    @Json(name = "declined")
    private Boolean _declined;

    @Json(name = "id")
    private Integer _id;

    @Json(name = "routed")
    private User _routed;

    @Json(name = "tecnichian")
    private User _tecnichian;

    @Source
    private JsonObject SOURCE;

    public Route() {
        SOURCE = new JsonObject();
    }

    public Route(JsonObject obj) {
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

    public Route actions(ActionsEnum[] actions) throws ParseException {
        _actions = actions;
        JsonArray ja = new JsonArray();
        for (ActionsEnum item : actions) {
            ja.add(item.toString());
        }
        SOURCE.put("actions", ja, true);
        return this;
    }

    public void setAssigned(Boolean assigned) throws ParseException {
        _assigned = assigned;
        SOURCE.put("assigned", assigned);
    }

    public Boolean getAssigned() {
        try {
            if (_assigned == null && SOURCE.has("assigned") && SOURCE.get("assigned") != null)
                _assigned = SOURCE.getBoolean("assigned");
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _assigned;
    }

    public Route assigned(Boolean assigned) throws ParseException {
        _assigned = assigned;
        SOURCE.put("assigned", assigned);
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

    public Route created(Date created) throws ParseException {
        _created = created;
        SOURCE.put("created", created.getJson());
        return this;
    }

    public void setDeclined(Boolean declined) throws ParseException {
        _declined = declined;
        SOURCE.put("declined", declined);
    }

    public Boolean getDeclined() {
        try {
            if (_declined == null && SOURCE.has("declined") && SOURCE.get("declined") != null)
                _declined = SOURCE.getBoolean("declined");
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _declined;
    }

    public Route declined(Boolean declined) throws ParseException {
        _declined = declined;
        SOURCE.put("declined", declined);
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

    public Route id(Integer id) throws ParseException {
        _id = id;
        SOURCE.put("id", id);
        return this;
    }

    public void setRouted(User routed) throws ParseException {
        _routed = routed;
        SOURCE.put("routed", routed.getJson());
    }

    public User getRouted() {
        try {
            if (_routed == null && SOURCE.has("routed") && SOURCE.get("routed") != null)
                _routed = User.fromJson(SOURCE.getJsonObject("routed"));
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        if (_routed != null && _routed.isSet())
            return _routed;

        return null;
    }

    public Route routed(User routed) throws ParseException {
        _routed = routed;
        SOURCE.put("routed", routed.getJson());
        return this;
    }

    public void setTecnichian(User tecnichian) throws ParseException {
        _tecnichian = tecnichian;
        SOURCE.put("tecnichian", tecnichian.getJson());
    }

    public User getTecnichian() {
        try {
            if (_tecnichian == null && SOURCE.has("tecnichian") && SOURCE.get("tecnichian") != null)
                _tecnichian = User.fromJson(SOURCE.getJsonObject("tecnichian"));
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        if (_tecnichian != null && _tecnichian.isSet())
            return _tecnichian;

        return null;
    }

    public Route tecnichian(User tecnichian) throws ParseException {
        _tecnichian = tecnichian;
        SOURCE.put("tecnichian", tecnichian.getJson());
        return this;
    }

    /*-******************************-*/
    /*-             Enums            -*/
    /*-******************************-*/
    public enum ActionsEnum {
        @Json(name = "accept")
        ACCEPT("accept"),
        @Json(name = "decline")
        DECLINE("decline");

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
    public static JsonArray toJsonArray(Route[] array) {
        JsonArray list = new JsonArray();
        for (Route item : array) {
            list.add(item.getJson());
        }
        return list;
    }

    public static Route[] fromJsonArray(JsonArray array) {
        Route[] list = new Route[array.size()];
        for (int i = 0; i < array.size(); i++) {
            list[i] = fromJson(array.getJsonObject(i));
        }
        return list;
    }

    public static Route fromJson(JsonObject obj) {
        try {
            return new Route(obj);
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
    public static final Parcelable.Creator<Route> CREATOR = new Parcelable.Creator<Route>() {

        @Override
        public Route createFromParcel(Parcel source) {
            try {
                return Route.fromJson((JsonObject) source.readParcelable(JsonObject.class.getClassLoader()));
            } catch (Exception ex) {
                Log.v(TAG, ex);
                return null;
            }
        }

        @Override
        public Route[] newArray(int size) {
            return new Route[size];
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
}
