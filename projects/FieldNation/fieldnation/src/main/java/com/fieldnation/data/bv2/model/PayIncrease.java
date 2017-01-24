package com.fieldnation.data.bv2.model;

import com.fieldnation.fnjson.JsonObject;
import com.fieldnation.fnjson.Serializer;
import com.fieldnation.fnjson.Unserializer;
import com.fieldnation.fnjson.annotations.Json;
import com.fieldnation.fnlog.Log;

public class PayIncrease {
    private static final String TAG = "PayIncrease";

    @Json(name = "status_description")
    private String statusDescription;

    @Json(name = "author")
    private User author;

    @Json(name = "created")
    private Date created;

    @Json(name = "description")
    private String description;

    @Json(name = "pay")
    private Pay pay;

    @Json(name = "id")
    private Integer id;

    @Json(name = "actions")
    private ActionsEnum[] actions;

    @Json(name = "status")
    private StatusEnum status;

    public PayIncrease() {
    }

    public String getStatusDescription() {
        return statusDescription;
    }

    public User getAuthor() {
        return author;
    }

    public Date getCreated() {
        return created;
    }

    public String getDescription() {
        return description;
    }

    public Pay getPay() {
        return pay;
    }

    public Integer getId() {
        return id;
    }

    public ActionsEnum[] getActions() {
        return actions;
    }

    public StatusEnum getStatus() {
        return status;
    }

    /*-*****************************-*/
    /*-             Json            -*/
    /*-*****************************-*/
    public static PayIncrease fromJson(JsonObject obj) {
        try {
            return Unserializer.unserializeObject(PayIncrease.class, obj);
        } catch (Exception ex) {
            Log.v(TAG, ex);
            return null;
        }
    }

    public JsonObject toJson() {
        return toJson(this);
    }

    public static JsonObject toJson(PayIncrease payIncrease) {
        try {
            return Serializer.serializeObject(payIncrease);
        } catch (Exception ex) {
            Log.v(TAG, ex);
            return null;
        }
    }
}
