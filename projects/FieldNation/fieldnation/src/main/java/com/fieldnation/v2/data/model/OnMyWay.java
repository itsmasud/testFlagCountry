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

public class OnMyWay implements Parcelable {
    private static final String TAG = "OnMyWay";

    @Json(name = "active")
    private Boolean _active;

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
    private String _status;

    @Json(name = "substatus")
    private String _substatus;

    @Source
    private JsonObject SOURCE;

    public OnMyWay() {
        SOURCE = new JsonObject();
    }

    public OnMyWay(JsonObject obj) {
        SOURCE = obj;
    }

    public void setActive(Boolean active) throws ParseException {
        _active = active;
        SOURCE.put("active", active);
    }

    public Boolean getActive() {
        try {
            if (_active != null)
                return _active;

            if (SOURCE.has("active") && SOURCE.get("active") != null)
                _active = SOURCE.getBoolean("active");

        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _active;
    }

    public OnMyWay active(Boolean active) throws ParseException {
        _active = active;
        SOURCE.put("active", active);
        return this;
    }

    public void setCoords(Coords coords) throws ParseException {
        _coords = coords;
        SOURCE.put("coords", coords.getJson());
    }

    public Coords getCoords() {
        try {
            if (_coords != null)
                return _coords;

            if (SOURCE.has("coords") && SOURCE.get("coords") != null)
                _coords = Coords.fromJson(SOURCE.getJsonObject("coords"));

        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _coords;
    }

    public OnMyWay coords(Coords coords) throws ParseException {
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
            if (_created != null)
                return _created;

            if (SOURCE.has("created") && SOURCE.get("created") != null)
                _created = Date.fromJson(SOURCE.getJsonObject("created"));

        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _created;
    }

    public OnMyWay created(Date created) throws ParseException {
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
            if (_distance != null)
                return _distance;

            if (SOURCE.has("distance") && SOURCE.get("distance") != null)
                _distance = SOURCE.getDouble("distance");

        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _distance;
    }

    public OnMyWay distance(Double distance) throws ParseException {
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
            if (_driveTime != null)
                return _driveTime;

            if (SOURCE.has("drive_time") && SOURCE.get("drive_time") != null)
                _driveTime = SOURCE.getInt("drive_time");

        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _driveTime;
    }

    public OnMyWay driveTime(Integer driveTime) throws ParseException {
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
            if (_estimatedDelay != null)
                return _estimatedDelay;

            if (SOURCE.has("estimated_delay") && SOURCE.get("estimated_delay") != null)
                _estimatedDelay = SOURCE.getInt("estimated_delay");

        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _estimatedDelay;
    }

    public OnMyWay estimatedDelay(Integer estimatedDelay) throws ParseException {
        _estimatedDelay = estimatedDelay;
        SOURCE.put("estimated_delay", estimatedDelay);
        return this;
    }

    public void setStatus(String status) throws ParseException {
        _status = status;
        SOURCE.put("status", status);
    }

    public String getStatus() {
        try {
            if (_status != null)
                return _status;

            if (SOURCE.has("status") && SOURCE.get("status") != null)
                _status = SOURCE.getString("status");

        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _status;
    }

    public OnMyWay status(String status) throws ParseException {
        _status = status;
        SOURCE.put("status", status);
        return this;
    }

    public void setSubstatus(String substatus) throws ParseException {
        _substatus = substatus;
        SOURCE.put("substatus", substatus);
    }

    public String getSubstatus() {
        try {
            if (_substatus != null)
                return _substatus;

            if (SOURCE.has("substatus") && SOURCE.get("substatus") != null)
                _substatus = SOURCE.getString("substatus");

        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _substatus;
    }

    public OnMyWay substatus(String substatus) throws ParseException {
        _substatus = substatus;
        SOURCE.put("substatus", substatus);
        return this;
    }

    /*-*****************************-*/
    /*-             Json            -*/
    /*-*****************************-*/
    public static JsonArray toJsonArray(OnMyWay[] array) {
        JsonArray list = new JsonArray();
        for (OnMyWay item : array) {
            list.add(item.getJson());
        }
        return list;
    }

    public static OnMyWay[] fromJsonArray(JsonArray array) {
        OnMyWay[] list = new OnMyWay[array.size()];
        for (int i = 0; i < array.size(); i++) {
            list[i] = fromJson(array.getJsonObject(i));
        }
        return list;
    }

    public static OnMyWay fromJson(JsonObject obj) {
        try {
            return new OnMyWay(obj);
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
    public static final Parcelable.Creator<OnMyWay> CREATOR = new Parcelable.Creator<OnMyWay>() {

        @Override
        public OnMyWay createFromParcel(Parcel source) {
            try {
                return OnMyWay.fromJson((JsonObject) source.readParcelable(JsonObject.class.getClassLoader()));
            } catch (Exception ex) {
                Log.v(TAG, ex);
                return null;
            }
        }

        @Override
        public OnMyWay[] newArray(int size) {
            return new OnMyWay[size];
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
