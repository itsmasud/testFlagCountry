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

public class Condition implements Parcelable {
    private static final String TAG = "Condition";

    @Json(name = "coords")
    private Coords _coords;

    @Json(name = "created")
    private Date _created;

    @Json(name = "distance")
    private Double _distance;

    @Json(name = "drive_time")
    private Integer _driveTime;

    @Json(name = "estimated_delay")
    private Integer _estimatedDelay;

    @Json(name = "status")
    private StatusEnum _status;

    @Json(name = "user")
    private User _user;

    @Source
    private JsonObject SOURCE;

    public Condition() {
        SOURCE = new JsonObject();
    }

    public Condition(JsonObject obj) {
        SOURCE = obj;
    }

    public void setCoords(Coords coords) throws ParseException {
        _coords = coords;
        SOURCE.put("coords", coords.getJson());
    }

    public Coords getCoords() {
        try {
            if (_coords == null && SOURCE.has("coords") && SOURCE.get("coords") != null)
                _coords = Coords.fromJson(SOURCE.getJsonObject("coords"));
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        if (_coords != null && _coords.isSet())
            return _coords;

        return null;
    }

    public Condition coords(Coords coords) throws ParseException {
        _coords = coords;
        SOURCE.put("coords", coords.getJson());
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

    public Condition created(Date created) throws ParseException {
        _created = created;
        SOURCE.put("created", created.getJson());
        return this;
    }

    public void setDistance(Double distance) throws ParseException {
        _distance = distance;
        SOURCE.put("distance", distance);
    }

    public Double getDistance() {
        try {
            if (_distance == null && SOURCE.has("distance") && SOURCE.get("distance") != null)
                _distance = SOURCE.getDouble("distance");
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _distance;
    }

    public Condition distance(Double distance) throws ParseException {
        _distance = distance;
        SOURCE.put("distance", distance);
        return this;
    }

    public void setDriveTime(Integer driveTime) throws ParseException {
        _driveTime = driveTime;
        SOURCE.put("drive_time", driveTime);
    }

    public Integer getDriveTime() {
        try {
            if (_driveTime == null && SOURCE.has("drive_time") && SOURCE.get("drive_time") != null)
                _driveTime = SOURCE.getInt("drive_time");
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _driveTime;
    }

    public Condition driveTime(Integer driveTime) throws ParseException {
        _driveTime = driveTime;
        SOURCE.put("drive_time", driveTime);
        return this;
    }

    public void setEstimatedDelay(Integer estimatedDelay) throws ParseException {
        _estimatedDelay = estimatedDelay;
        SOURCE.put("estimated_delay", estimatedDelay);
    }

    public Integer getEstimatedDelay() {
        try {
            if (_estimatedDelay == null && SOURCE.has("estimated_delay") && SOURCE.get("estimated_delay") != null)
                _estimatedDelay = SOURCE.getInt("estimated_delay");
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _estimatedDelay;
    }

    public Condition estimatedDelay(Integer estimatedDelay) throws ParseException {
        _estimatedDelay = estimatedDelay;
        SOURCE.put("estimated_delay", estimatedDelay);
        return this;
    }

    public void setStatus(StatusEnum status) throws ParseException {
        _status = status;
        SOURCE.put("status", status.toString());
    }

    public StatusEnum getStatus() {
        try {
            if (_status == null && SOURCE.has("status") && SOURCE.get("status") != null)
                _status = StatusEnum.fromString(SOURCE.getString("status"));
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _status;
    }

    public Condition status(StatusEnum status) throws ParseException {
        _status = status;
        SOURCE.put("status", status.toString());
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

    public Condition user(User user) throws ParseException {
        _user = user;
        SOURCE.put("user", user.getJson());
        return this;
    }

    /*-******************************-*/
    /*-             Enums            -*/
    /*-******************************-*/
    public enum StatusEnum {
        @Json(name = "delayed")
        DELAYED("delayed");

        private String value;

        StatusEnum(String value) {
            this.value = value;
        }

        public static StatusEnum fromString(String value) {
            StatusEnum[] values = values();
            for (StatusEnum v : values) {
                if (v.value.equals(value))
                    return v;
            }
            return null;
        }

        public static StatusEnum[] fromJsonArray(JsonArray jsonArray) {
            StatusEnum[] list = new StatusEnum[jsonArray.size()];
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
    public static JsonArray toJsonArray(Condition[] array) {
        JsonArray list = new JsonArray();
        for (Condition item : array) {
            list.add(item.getJson());
        }
        return list;
    }

    public static Condition[] fromJsonArray(JsonArray array) {
        Condition[] list = new Condition[array.size()];
        for (int i = 0; i < array.size(); i++) {
            list[i] = fromJson(array.getJsonObject(i));
        }
        return list;
    }

    public static Condition fromJson(JsonObject obj) {
        try {
            return new Condition(obj);
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
    public static final Parcelable.Creator<Condition> CREATOR = new Parcelable.Creator<Condition>() {

        @Override
        public Condition createFromParcel(Parcel source) {
            try {
                return Condition.fromJson((JsonObject) source.readParcelable(JsonObject.class.getClassLoader()));
            } catch (Exception ex) {
                Log.v(TAG, ex);
                return null;
            }
        }

        @Override
        public Condition[] newArray(int size) {
            return new Condition[size];
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
