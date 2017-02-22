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

public class ShipmentCarrier implements Parcelable {
    private static final String TAG = "ShipmentCarrier";

    @Json(name = "arrival")
    private Date _arrival;

    @Json(name = "arrived")
    private Date _arrived;

    @Json(name = "name")
    private String _name;

    @Json(name = "other")
    private String _other;

    @Json(name = "tracking")
    private String _tracking;

    @Source
    private JsonObject SOURCE;

    public ShipmentCarrier() {
        SOURCE = new JsonObject();
    }

    public ShipmentCarrier(JsonObject obj) {
        SOURCE = obj;
    }

    public void setArrival(Date arrival) throws ParseException {
        _arrival = arrival;
        SOURCE.put("arrival", arrival.getJson());
    }

    public Date getArrival() {
        try {
            if (_arrival != null)
                return _arrival;

            if (SOURCE.has("arrival") && SOURCE.get("arrival") != null)
                _arrival = Date.fromJson(SOURCE.getJsonObject("arrival"));

        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _arrival;
    }

    public ShipmentCarrier arrival(Date arrival) throws ParseException {
        _arrival = arrival;
        SOURCE.put("arrival", arrival.getJson());
        return this;
    }

    public void setArrived(Date arrived) throws ParseException {
        _arrived = arrived;
        SOURCE.put("arrived", arrived.getJson());
    }

    public Date getArrived() {
        try {
            if (_arrived != null)
                return _arrived;

            if (SOURCE.has("arrived") && SOURCE.get("arrived") != null)
                _arrived = Date.fromJson(SOURCE.getJsonObject("arrived"));

        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _arrived;
    }

    public ShipmentCarrier arrived(Date arrived) throws ParseException {
        _arrived = arrived;
        SOURCE.put("arrived", arrived.getJson());
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

    public ShipmentCarrier name(String name) throws ParseException {
        _name = name;
        SOURCE.put("name", name);
        return this;
    }

    public void setOther(String other) throws ParseException {
        _other = other;
        SOURCE.put("other", other);
    }

    public String getOther() {
        try {
            if (_other != null)
                return _other;

            if (SOURCE.has("other") && SOURCE.get("other") != null)
                _other = SOURCE.getString("other");

        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _other;
    }

    public ShipmentCarrier other(String other) throws ParseException {
        _other = other;
        SOURCE.put("other", other);
        return this;
    }

    public void setTracking(String tracking) throws ParseException {
        _tracking = tracking;
        SOURCE.put("tracking", tracking);
    }

    public String getTracking() {
        try {
            if (_tracking != null)
                return _tracking;

            if (SOURCE.has("tracking") && SOURCE.get("tracking") != null)
                _tracking = SOURCE.getString("tracking");

        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _tracking;
    }

    public ShipmentCarrier tracking(String tracking) throws ParseException {
        _tracking = tracking;
        SOURCE.put("tracking", tracking);
        return this;
    }

    /*-*****************************-*/
    /*-             Json            -*/
    /*-*****************************-*/
    public static JsonArray toJsonArray(ShipmentCarrier[] array) {
        JsonArray list = new JsonArray();
        for (ShipmentCarrier item : array) {
            list.add(item.getJson());
        }
        return list;
    }

    public static ShipmentCarrier[] fromJsonArray(JsonArray array) {
        ShipmentCarrier[] list = new ShipmentCarrier[array.size()];
        for (int i = 0; i < array.size(); i++) {
            list[i] = fromJson(array.getJsonObject(i));
        }
        return list;
    }

    public static ShipmentCarrier fromJson(JsonObject obj) {
        try {
            return new ShipmentCarrier(obj);
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
    public static final Parcelable.Creator<ShipmentCarrier> CREATOR = new Parcelable.Creator<ShipmentCarrier>() {

        @Override
        public ShipmentCarrier createFromParcel(Parcel source) {
            try {
                return ShipmentCarrier.fromJson((JsonObject) source.readParcelable(JsonObject.class.getClassLoader()));
            } catch (Exception ex) {
                Log.v(TAG, ex);
                return null;
            }
        }

        @Override
        public ShipmentCarrier[] newArray(int size) {
            return new ShipmentCarrier[size];
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
