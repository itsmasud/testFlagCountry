package com.fieldnation.data.bv2.model;

import com.fieldnation.fnjson.JsonObject;
import com.fieldnation.fnjson.Serializer;
import com.fieldnation.fnjson.Unserializer;
import com.fieldnation.fnjson.annotations.Json;
import com.fieldnation.fnlog.Log;

public class PayIncrease {
    private static final String TAG = "PayIncrease";

    @Json(name = "status_description")
    private String _statusDescription;

    @Json(name = "author")
    private User _author;

    @Json(name = "created")
    private Date _created;

    @Json(name = "description")
    private String _description;

    @Json(name = "pay")
    private Pay _pay;

    @Json(name = "id")
    private Integer _id;

    @Json(name = "actions")
    private ActionsEnum[] _actions;

    @Json(name = "status")
    private StatusEnum _status;

    public PayIncrease() {
    }

    public String getStatusDescription() {
        return _statusDescription;
    }

    public User getAuthor() {
        return _author;
    }

    public Date getCreated() {
        return _created;
    }

    public String getDescription() {
        return _description;
    }

    public Pay getPay() {
        return _pay;
    }

    public Integer getId() {
        return _id;
    }

    public ActionsEnum[] getActions() {
        return _actions;
    }

    public StatusEnum getStatus() {
        return _status;
    }

    /*-*****************************-*/
    /*-             Json            -*/
    /*-*****************************-*/
    public static PayIncrease fromJson(JsonObject obj) {
        try {
            return Unserializer.unserializeObject(PayIncrease.class, obj);
        } catch (Exception ex) {
            Log.v(TAG, TAG, ex);
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
            Log.v(TAG, TAG, ex);
            return null;
        }
    }
}
