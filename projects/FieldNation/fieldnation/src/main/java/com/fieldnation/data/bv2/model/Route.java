package com.fieldnation.data.bv2.model;

import com.fieldnation.fnjson.JsonObject;
import com.fieldnation.fnjson.Serializer;
import com.fieldnation.fnjson.Unserializer;
import com.fieldnation.fnjson.annotations.Json;
import com.fieldnation.fnlog.Log;

public class Route {
    private static final String TAG = "Route";

    @Json(name = "schedule")
    private Schedule schedule;

    @Json(name = "notes")
    private String notes;

    @Json(name = "created")
    private Date created;

    @Json(name = "id")
    private Integer id;

    @Json(name = "counter")
    private Boolean counter;

    @Json(name = "user")
    private User user;

    public Route() {
    }

    public Schedule getSchedule() {
        return schedule;
    }

    public String getNotes() {
        return notes;
    }

    public Date getCreated() {
        return created;
    }

    public Integer getId() {
        return id;
    }

    public Boolean getCounter() {
        return counter;
    }

    public User getUser() {
        return user;
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
