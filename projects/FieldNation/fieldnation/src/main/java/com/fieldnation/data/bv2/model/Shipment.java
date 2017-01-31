package com.fieldnation.data.bv2.model;
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

public class Shipment implements Parcelable {
    private static final String TAG = "Shipment";

    @Json(name = "carrier")
    private ShipmentCarrier _carrier;

    @Json(name = "task")
    private ShipmentTask _task;

    @Json(name = "created")
    private Date _created;

    @Json(name = "name")
    private String _name;

    @Json(name = "id")
    private Integer _id;

    @Json(name = "user")
    private User _user;

    @Json(name = "direction")
    private DirectionEnum _direction;

    @Json(name = "status")
    private StatusEnum _status;

    public Shipment() {
    }

    public void setCarrier(ShipmentCarrier carrier) {
        _carrier = carrier;
    }

    public ShipmentCarrier getCarrier() {
        return _carrier;
    }

    public Shipment carrier(ShipmentCarrier carrier) {
        _carrier = carrier;
        return this;
    }

    public void setTask(ShipmentTask task) {
        _task = task;
    }

    public ShipmentTask getTask() {
        return _task;
    }

    public Shipment task(ShipmentTask task) {
        _task = task;
        return this;
    }

    public void setCreated(Date created) {
        _created = created;
    }

    public Date getCreated() {
        return _created;
    }

    public Shipment created(Date created) {
        _created = created;
        return this;
    }

    public void setName(String name) {
        _name = name;
    }

    public String getName() {
        return _name;
    }

    public Shipment name(String name) {
        _name = name;
        return this;
    }

    public void setId(Integer id) {
        _id = id;
    }

    public Integer getId() {
        return _id;
    }

    public Shipment id(Integer id) {
        _id = id;
        return this;
    }

    public void setUser(User user) {
        _user = user;
    }

    public User getUser() {
        return _user;
    }

    public Shipment user(User user) {
        _user = user;
        return this;
    }

    public void setDirection(DirectionEnum direction) {
        _direction = direction;
    }

    public DirectionEnum getDirection() {
        return _direction;
    }

    public Shipment direction(DirectionEnum direction) {
        _direction = direction;
        return this;
    }

    public void setStatus(StatusEnum status) {
        _status = status;
    }

    public StatusEnum getStatus() {
        return _status;
    }

    public Shipment status(StatusEnum status) {
        _status = status;
        return this;
    }

    /*-*****************************-*/
    /*-             Json            -*/
    /*-*****************************-*/
    public static Shipment[] fromJsonArray(JsonArray array) {
        Shipment[] list = new Shipment[array.size()];
        for (int i = 0; i < array.size(); i++) {
            list[i] = fromJson(array.getJsonObject(i));
        }
        return list;
    }

    public static Shipment fromJson(JsonObject obj) {
        try {
            return Unserializer.unserializeObject(Shipment.class, obj);
        } catch (Exception ex) {
            Log.v(TAG, TAG, ex);
            return null;
        }
    }

    public JsonObject toJson() {
        return toJson(this);
    }

    public static JsonObject toJson(Shipment shipment) {
        try {
            return Serializer.serializeObject(shipment);
        } catch (Exception ex) {
            Log.v(TAG, TAG, ex);
            return null;
        }
    }

    /*-*********************************************-*/
    /*-			Parcelable Implementation           -*/
    /*-*********************************************-*/
    public static final Parcelable.Creator<Shipment> CREATOR = new Parcelable.Creator<Shipment>() {

        @Override
        public Shipment createFromParcel(Parcel source) {
            try {
                return Shipment.fromJson((JsonObject) source.readParcelable(JsonObject.class.getClassLoader()));
            } catch (Exception ex) {
                Log.v(TAG, ex);
                return null;
            }
        }

        @Override
        public Shipment[] newArray(int size) {
            return new Shipment[size];
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
