package com.fieldnation.data.bv2.model;

import com.fieldnation.fnjson.JsonObject;
import com.fieldnation.fnjson.Serializer;
import com.fieldnation.fnjson.Unserializer;
import com.fieldnation.fnjson.annotations.Json;
import com.fieldnation.fnlog.Log;

public class PayIncrease {
    private static final String TAG = "PayIncrease";

    @Json(name = "id")
    private Integer id = null;

    @Json(name = "actions")
    private ActionsEnum[] actions;

    @Json(name = "description")
    private String description = null;

    @Json(name = "status")
    private StatusEnum status = null;

    @Json(name = "status_description")
    private String statusDescription = null;

    @Json(name = "pay")
    private Pay pay = null;

    @Json(name = "author")
    private User author = null;

    @Json(name = "created")
    private String created = null;

    public enum ActionsEnum {
        @Json(name = "accept")
        ACCEPT("accept"),
        @Json(name = "edit")
        EDIT("edit");
        private String value;

        ActionsEnum(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return String.valueOf(value);
        }
    }

    public enum StatusEnum {
        @Json(name = "declined")
        DECLINED("declined"),
        @Json(name = "accepted")
        ACCEPTED("accepted"),
        @Json(name = "pending")
        PENDING("pending");

        private String value;

        StatusEnum(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return String.valueOf(value);
        }
    }

    public PayIncrease() {
    }

    public Integer getId() {
        return id;
    }

    public ActionsEnum[] getActions() {
        return actions;
    }

    public String getDescription() {
        return description;
    }

    public StatusEnum getStatus() {
        return status;
    }

    public String getStatusDescription() {
        return statusDescription;
    }

    public Pay getPay() {
        return pay;
    }

    public User getAuthor() {
        return author;
    }

    public String getCreated() {
        return created;
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