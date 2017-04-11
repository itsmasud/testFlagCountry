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

public class ETA implements Parcelable {
    private static final String TAG = "ETA";

    @Json(name = "actions")
    private ActionsEnum[] _actions;

    @Json(name = "end")
    private Date _end;

    @Json(name = "hour_estimate")
    private Double _hourEstimate;

    @Json(name = "mode")
    private Boolean _mode;

    @Json(name = "start")
    private Date _start;

    @Json(name = "status")
    private ETAStatus _status;

    @Json(name = "user")
    private User _user;

    @Source
    private JsonObject SOURCE;

    public ETA() {
        SOURCE = new JsonObject();
    }

    public ETA(JsonObject obj) {
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

    public ETA actions(ActionsEnum[] actions) throws ParseException {
        _actions = actions;
        JsonArray ja = new JsonArray();
        for (ActionsEnum item : actions) {
            ja.add(item.toString());
        }
        SOURCE.put("actions", ja, true);
        return this;
    }

    public void setEnd(Date end) throws ParseException {
        _end = end;
        SOURCE.put("end", end.getJson());
    }

    public Date getEnd() {
        try {
            if (_end == null && SOURCE.has("end") && SOURCE.get("end") != null)
                _end = Date.fromJson(SOURCE.getJsonObject("end"));
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        if (_end != null && _end.isSet())
            return _end;

        return null;
    }

    public ETA end(Date end) throws ParseException {
        _end = end;
        SOURCE.put("end", end.getJson());
        return this;
    }

    public void setHourEstimate(Double hourEstimate) throws ParseException {
        _hourEstimate = hourEstimate;
        SOURCE.put("hour_estimate", hourEstimate);
    }

    public Double getHourEstimate() {
        try {
            if (_hourEstimate == null && SOURCE.has("hour_estimate") && SOURCE.get("hour_estimate") != null)
                _hourEstimate = SOURCE.getDouble("hour_estimate");
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _hourEstimate;
    }

    public ETA hourEstimate(Double hourEstimate) throws ParseException {
        _hourEstimate = hourEstimate;
        SOURCE.put("hour_estimate", hourEstimate);
        return this;
    }

    public void setMode(Boolean mode) throws ParseException {
        _mode = mode;
        SOURCE.put("mode", mode);
    }

    public Boolean getMode() {
        try {
            if (_mode == null && SOURCE.has("mode") && SOURCE.get("mode") != null)
                _mode = SOURCE.getBoolean("mode");
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _mode;
    }

    public ETA mode(Boolean mode) throws ParseException {
        _mode = mode;
        SOURCE.put("mode", mode);
        return this;
    }

    public void setStart(Date start) throws ParseException {
        _start = start;
        SOURCE.put("start", start.getJson());
    }

    public Date getStart() {
        try {
            if (_start == null && SOURCE.has("start") && SOURCE.get("start") != null)
                _start = Date.fromJson(SOURCE.getJsonObject("start"));
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        if (_start != null && _start.isSet())
            return _start;

        return null;
    }

    public ETA start(Date start) throws ParseException {
        _start = start;
        SOURCE.put("start", start.getJson());
        return this;
    }

    public void setStatus(ETAStatus status) throws ParseException {
        _status = status;
        SOURCE.put("status", status.getJson());
    }

    public ETAStatus getStatus() {
        try {
            if (_status == null && SOURCE.has("status") && SOURCE.get("status") != null)
                _status = ETAStatus.fromJson(SOURCE.getJsonObject("status"));
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        if (_status != null && _status.isSet())
            return _status;

        return null;
    }

    public ETA status(ETAStatus status) throws ParseException {
        _status = status;
        SOURCE.put("status", status.getJson());
        return this;
    }

    public void setUser(User user) throws ParseException {
        _user = user;
        SOURCE.put("user", user.getJson());
    }

    public User getUser() {
        try {
            if (_user == null && SOURCE.has("user") && SOURCE.get("user") != null)
                _user = User.fromJson(SOURCE.getJsonObject("user"));
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        if (_user != null && _user.isSet())
            return _user;

        return null;
    }

    public ETA user(User user) throws ParseException {
        _user = user;
        SOURCE.put("user", user.getJson());
        return this;
    }

    /*-******************************-*/
    /*-             Enums            -*/
    /*-******************************-*/
    public enum ActionsEnum {
        @Json(name = "add")
        ADD("add"),
        @Json(name = "confirm")
        CONFIRM("confirm"),
        @Json(name = "edit")
        EDIT("edit"),
        @Json(name = "mark_ready_to_go")
        MARK_READY_TO_GO("mark_ready_to_go"),
        @Json(name = "on_my_way")
        ON_MY_WAY("on_my_way"),
        @Json(name = "running_late")
        RUNNING_LATE("running_late");

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
    public static JsonArray toJsonArray(ETA[] array) {
        JsonArray list = new JsonArray();
        for (ETA item : array) {
            list.add(item.getJson());
        }
        return list;
    }

    public static ETA[] fromJsonArray(JsonArray array) {
        ETA[] list = new ETA[array.size()];
        for (int i = 0; i < array.size(); i++) {
            list[i] = fromJson(array.getJsonObject(i));
        }
        return list;
    }

    public static ETA fromJson(JsonObject obj) {
        try {
            return new ETA(obj);
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
    public static final Parcelable.Creator<ETA> CREATOR = new Parcelable.Creator<ETA>() {

        @Override
        public ETA createFromParcel(Parcel source) {
            try {
                return ETA.fromJson((JsonObject) source.readParcelable(JsonObject.class.getClassLoader()));
            } catch (Exception ex) {
                Log.v(TAG, ex);
                return null;
            }
        }

        @Override
        public ETA[] newArray(int size) {
            return new ETA[size];
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
        return getMode() != null;
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
