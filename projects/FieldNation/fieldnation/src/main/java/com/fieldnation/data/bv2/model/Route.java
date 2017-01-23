package com.fieldnation.data.bv2.model;

import com.fieldnation.fnjson.JsonObject;
import com.fieldnation.fnjson.Serializer;
import com.fieldnation.fnjson.Unserializer;
import com.fieldnation.fnjson.annotations.Json;
import com.fieldnation.fnlog.Log;

public class Route {
    private static final String TAG = "Route";

    @Json(name = "id")
    private Integer id = null;

    @Json(name = "user")
    private User user = null;

    @Json(name = "created")
    private String created = null;

    @Json(name = "schedule")
    private Schedule schedule = null;

    @Json(name = "notes")
    private String notes = null;

    @Json(name = "counter")
    private Boolean counter = null;

    public Route() {
    }

    public Integer getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public String getCreated() {
        return created;
    }

    public Schedule getSchedule() {
        return schedule;
    }

    public void setSchedule(Schedule schedule) {
        this.schedule = schedule;
    }

    public String getNotes() {
        return notes;
    }

    public Boolean getCounter() {
        return counter;
    }

    /*-*****************************-*/
    /*-             Json            -*/
    /*-*****************************-*/
    public static Route fromJson(JsonObject obj) {
        try {
            return Unserializer.unserializeObject(Route.class, obj);
        } catch (Exception ex) {
            Log.v(TAG, ex);
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
            Log.v(TAG, ex);
            return null;
        }
    }
}