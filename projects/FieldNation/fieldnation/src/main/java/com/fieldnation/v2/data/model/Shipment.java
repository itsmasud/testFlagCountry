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

    @Json(name = "actions")
    private ActionsEnum[] _actions;

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
    private JsonObject SOURCE;

    public Shipment() {
        SOURCE = new JsonObject();
    }

    public Shipment(JsonObject obj) {
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

    public Shipment actions(ActionsEnum[] actions) throws ParseException {
        _actions = actions;
        JsonArray ja = new JsonArray();
        for (ActionsEnum item : actions) {
            ja.add(item.toString());
        }
        SOURCE.put("actions", ja, true);
        return this;
    }

    public void setCarrier(ShipmentCarrier carrier) throws ParseException {
        _carrier = carrier;
        SOURCE.put("carrier", carrier.getJson());
    }

    public ShipmentCarrier getCarrier() {
        try {
            if (_carrier != null)
                return _carrier;

            if (SOURCE.has("carrier") && SOURCE.get("carrier") != null)
                _carrier = ShipmentCarrier.fromJson(SOURCE.getJsonObject("carrier"));

        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

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
        try {
            if (_direction != null)
                return _direction;

            if (SOURCE.has("direction") && SOURCE.get("direction") != null)
                _direction = DirectionEnum.fromString(SOURCE.getString("direction"));

        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

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
        try {
            if (_id != null)
                return _id;

            if (SOURCE.has("id") && SOURCE.get("id") != null)
                _id = SOURCE.getInt("id");

        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

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
        try {
            if (_status != null)
                return _status;

            if (SOURCE.has("status") && SOURCE.get("status") != null)
                _status = StatusEnum.fromString(SOURCE.getString("status"));

        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

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
        try {
            if (_task != null)
                return _task;

            if (SOURCE.has("task") && SOURCE.get("task") != null)
                _task = ShipmentTask.fromJson(SOURCE.getJsonObject("task"));

        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

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
        try {
            if (_user != null)
                return _user;

            if (SOURCE.has("user") && SOURCE.get("user") != null)
                _user = User.fromJson(SOURCE.getJsonObject("user"));

        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

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

    public enum DirectionEnum {
        @Json(name = "from_site")
        FROM_SITE("from_site"),
        @Json(name = "to_site")
        TO_SITE("to_site");

        private String value;

        DirectionEnum(String value) {
            this.value = value;
        }

        public static DirectionEnum fromString(String value) {
            DirectionEnum[] values = values();
            for (DirectionEnum v : values) {
                if (v.value.equals(value))
                    return v;
            }
            return null;
        }

        public static DirectionEnum[] fromJsonArray(JsonArray jsonArray) {
            DirectionEnum[] list = new DirectionEnum[jsonArray.size()];
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

    public enum ActionsEnum {
        @Json(name = "delete")
        DELETE("delete"),
        @Json(name = "edit")
        EDIT("edit");

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
            return new Shipment(obj);
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
