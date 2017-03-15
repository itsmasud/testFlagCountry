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

public class ShipmentCarrier implements Parcelable {
    private static final String TAG = "ShipmentCarrier";

    @Json(name = "arrival")
    private Date _arrival;

    @Json(name = "arrived")
    private Date _arrived;

    @Json(name = "name")
    private NameEnum _name;

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
            if (_arrival == null && SOURCE.has("arrival") && SOURCE.get("arrival") != null)
                _arrival = Date.fromJson(SOURCE.getJsonObject("arrival"));
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        if (_arrival != null && _arrival.isSet())
            return _arrival;

        return null;
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
            if (_arrived == null && SOURCE.has("arrived") && SOURCE.get("arrived") != null)
                _arrived = Date.fromJson(SOURCE.getJsonObject("arrived"));
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        if (_arrived != null && _arrived.isSet())
            return _arrived;

        return null;
    }

    public ShipmentCarrier arrived(Date arrived) throws ParseException {
        _arrived = arrived;
        SOURCE.put("arrived", arrived.getJson());
        return this;
    }

    public void setName(NameEnum name) throws ParseException {
        _name = name;
        SOURCE.put("name", name.toString());
    }

    public NameEnum getName() {
        try {
            if (_name == null && SOURCE.has("name") && SOURCE.get("name") != null)
                _name = NameEnum.fromString(SOURCE.getString("name"));
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _name;
    }

    public ShipmentCarrier name(NameEnum name) throws ParseException {
        _name = name;
        SOURCE.put("name", name.toString());
        return this;
    }

    public void setOther(String other) throws ParseException {
        _other = other;
        SOURCE.put("other", other);
    }

    public String getOther() {
        try {
            if (_other == null && SOURCE.has("other") && SOURCE.get("other") != null)
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
            if (_tracking == null && SOURCE.has("tracking") && SOURCE.get("tracking") != null)
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

    /*-******************************-*/
    /*-             Enums            -*/
    /*-******************************-*/
    public enum NameEnum {
        @Json(name = "fedex")
        FEDEX("fedex"),
        @Json(name = "ups")
        UPS("ups"),
        @Json(name = "usps")
        USPS("usps"),
        @Json(name = "other")
        OTHER("other");

        private String value;

        NameEnum(String value) {
            this.value = value;
        }

        public static NameEnum fromString(String value) {
            NameEnum[] values = values();
            for (NameEnum v : values) {
                if (v.value.equals(value))
                    return v;
            }
            return null;
        }

        public static NameEnum[] fromJsonArray(JsonArray jsonArray) {
            NameEnum[] list = new NameEnum[jsonArray.size()];
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

    /*-*****************************-*/
    /*-         Human Code          -*/
    /*-*****************************-*/

    public boolean isSet() {
        return true;
    }
}
