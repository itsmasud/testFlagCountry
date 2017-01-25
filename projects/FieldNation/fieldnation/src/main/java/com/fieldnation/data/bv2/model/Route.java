package com.fieldnation.data.bv2.model;

import com.fieldnation.fnjson.JsonObject;
import com.fieldnation.fnjson.Serializer;
import com.fieldnation.fnjson.Unserializer;
import com.fieldnation.fnjson.annotations.Json;
import com.fieldnation.fnlog.Log;

public class Route {
    private static final String TAG = "Route";

    @Json(name = "schedule")
    private Schedule _schedule;

    @Json(name = "notes")
    private String _notes;

    @Json(name = "created")
    private Date _created;

    @Json(name = "id")
    private Integer _id;

    @Json(name = "counter")
    private Boolean _counter;

    @Json(name = "user")
    private User _user;

    public Route() {
    }

    public Schedule getSchedule() {
        return _schedule;
    }

    public String getNotes() {
        return _notes;
    }

    public Date getCreated() {
        return _created;
    }

    public Integer getId() {
        return _id;
    }

    public Boolean getCounter() {
        return _counter;
    }

    public User getUser() {
        return _user;
    }

    /*-*****************************-*/
    /*-             Json            -*/
    /*-*****************************-*/
    public static Route fromJson(JsonObject obj) {
        try {
            return Unserializer.unserializeObject(Route.class, obj);
        } catch (Exception ex) {
            Log.v(TAG, TAG, ex);
            return null;
        }
    }

    public JsonObject toJson() {
        return toJson(this);
    }

    public static JsonObject toJson(Route route) {
        try {
            return Serializer.serializeObject(route);
        } catch (Exception ex) {
            Log.v(TAG, TAG, ex);
            return null;
        }
    }
}
