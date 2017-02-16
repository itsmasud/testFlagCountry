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

public class Shipment implements Parcelable {
    private static final String TAG = "Shipment";

    @Json(name = "carrier")
    private ShipmentCarrier _carrier;

    @Json(name = "created")
    private Date _created;

    @Json(name = "direction")
    private DirectionEnum _direction;

    @Json(name = "id")
    private Integer _id;

    @Json(name = "name")
    private String _name;

    @Json(name = "status")
    private StatusEnum _status;

    @Json(name = "task")
    private ShipmentTask _task;

    @Json(name = "user")
    private User _user;

    @Source
    private JsonObject SOURCE = new JsonObject();

    public Shipment() {
    }

    public void setCarrier(ShipmentCarrier carrier) throws ParseException {
        _carrier = carrier;
        SOURCE.put("carrier", carrier.getJson());
    }

    public ShipmentCarrier getCarrier() {
        return _carrier;
    }

    public Shipment carrier(ShipmentCarrier carrier) throws ParseException {
        _carrier = carrier;
        SOURCE.put("carrier", carrier.getJson());
        return this;
    }

    public void setCreated(Date created) throws ParseException {
        _created = created;
        SOURCE.put("created", created.getJson());
    }

    public Date getCreated() {
        return _created;
    }

    public Shipment created(Date created) throws ParseException {
        _created = created;
        SOURCE.put("created", created.getJson());
        return this;
    }

    public void setDirection(DirectionEnum direction) throws ParseException {
        _direction = direction;
        SOURCE.put("direction", direction.toString());
    }

    public DirectionEnum getDirection() {
        return _direction;
    }

    public Shipment direction(DirectionEnum direction) throws ParseException {
        _direction = direction;
        SOURCE.put("direction", direction.toString());
        return this;
    }

    public void setId(Integer id) throws ParseException {
        _id = id;
        SOURCE.put("id", id);
    }

    public Integer getId() {
        return _id;
    }

    public Shipment id(Integer id) throws ParseException {
        _id = id;
        SOURCE.put("id", id);
        return this;
    }

    public void setName(String name) throws ParseException {
        _name = name;
        SOURCE.put("name", name);
    }

    public String getName() {
        return _name;
    }

    public Shipment name(String name) throws ParseException {
        _name = name;
        SOURCE.put("name", name);
        return this;
    }

    public void setStatus(StatusEnum status) throws ParseException {
        _status = status;
        SOURCE.put("status", status.toString());
    }

    public StatusEnum getStatus() {
        return _status;
    }

    public Shipment status(StatusEnum status) throws ParseException {
        _status = status;
        SOURCE.put("status", status.toString());
        return this;
    }

    public void setTask(ShipmentTask task) throws ParseException {
        _task = task;
        SOURCE.put("task", task.getJson());
    }

    public ShipmentTask getTask() {
        return _task;
    }

    public Shipment task(ShipmentTask task) throws ParseException {
        _task = task;
        SOURCE.put("task", task.getJson());
        return this;
    }

    public void setUser(User user) throws ParseException {
        _user = user;
        SOURCE.put("user", user.getJson());
    }

    public User getUser() {
        return _user;
    }

    public Shipment user(User user) throws ParseException {
        _user = user;
        SOURCE.put("user", user.getJson());
        return this;
    }

    /*-******************************-*/
    /*-             Enums            -*/
    /*-******************************-*/
    public enum StatusEnum {
        @Json(name = "arrived")
        ARRIVED("arrived"),
        @Json(name = "en_route")
        EN_ROUTE("en_route"),
        @Json(name = "error")
        ERROR("error"),
        @Json(name = "lost")
        LOST("lost"),
        @Json(name = "new")
        NEW("new");

        private String value;

        StatusEnum(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return String.valueOf(value);
        }
    }

    public enum DirectionEnum {
        @Json(name = "from_site")
        FROM_SITE("from_site"),
        @Json(name = "to_site")
        TO_SITE("to_site");

        private String value;

        DirectionEnum(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return String.valueOf(value);
        }
    }

    /*-*****************************-*/
    /*-             Json            -*/
    /*-*****************************-*/
    public static JsonArray toJsonArray(Shipment[] array) {
        JsonArray list = new JsonArray();
        for (Shipment item : array) {
            list.add(item.getJson());
        }
        return list;
    }

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

    public JsonObject getJson() {
        return SOURCE;
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
        dest.writeParcelable(getJson(), flags);
    }
}
