package com.fieldnation.v2.data.model;
import android.os.Parcel;
import android.os.Parcelable;

import com.fieldnation.fnjson.JsonArray;
import com.fieldnation.fnjson.JsonObject;
import com.fieldnation.fnjson.Serializer;
import com.fieldnation.fnjson.Unserializer;
import com.fieldnation.fnjson.annotations.Json;
import com.fieldnation.fnlog.Log;

/**
 * Created by dmgen from swagger on 1/31/17.
 */

public class ShipmentCarrier implements Parcelable {
    private static final String TAG = "ShipmentCarrier";

    @Json(name = "arrived")
    private Date _arrived;

    @Json(name = "other")
    private String _other;

    @Json(name = "arrival")
    private Date _arrival;

    @Json(name = "name")
    private String _name;

    @Json(name = "tracking")
    private String _tracking;

    public ShipmentCarrier() {
    }

    public void setArrived(Date arrived) {
        _arrived = arrived;
    }

    public Date getArrived() {
        return _arrived;
    }

    public ShipmentCarrier arrived(Date arrived) {
        _arrived = arrived;
        return this;
    }

    public void setOther(String other) {
        _other = other;
    }

    public String getOther() {
        return _other;
    }

    public ShipmentCarrier other(String other) {
        _other = other;
        return this;
    }

    public void setArrival(Date arrival) {
        _arrival = arrival;
    }

    public Date getArrival() {
        return _arrival;
    }

    public ShipmentCarrier arrival(Date arrival) {
        _arrival = arrival;
        return this;
    }

    public void setName(String name) {
        _name = name;
    }

    public String getName() {
        return _name;
    }

    public ShipmentCarrier name(String name) {
        _name = name;
        return this;
    }

    public void setTracking(String tracking) {
        _tracking = tracking;
    }

    public String getTracking() {
        return _tracking;
    }

    public ShipmentCarrier tracking(String tracking) {
        _tracking = tracking;
        return this;
    }

    /*-*****************************-*/
    /*-             Json            -*/
    /*-*****************************-*/
    public static ShipmentCarrier[] fromJsonArray(JsonArray array) {
        ShipmentCarrier[] list = new ShipmentCarrier[array.size()];
        for (int i = 0; i < array.size(); i++) {
            list[i] = fromJson(array.getJsonObject(i));
        }
        return list;
    }

    public static ShipmentCarrier fromJson(JsonObject obj) {
        try {
            return Unserializer.unserializeObject(ShipmentCarrier.class, obj);
        } catch (Exception ex) {
            Log.v(TAG, TAG, ex);
            return null;
        }
    }

    public JsonObject toJson() {
        return toJson(this);
    }

    public static JsonObject toJson(ShipmentCarrier shipmentCarrier) {
        try {
            return Serializer.serializeObject(shipmentCarrier);
        } catch (Exception ex) {
            Log.v(TAG, TAG, ex);
            return null;
        }
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
        dest.writeParcelable(toJson(), flags);
    }
}
