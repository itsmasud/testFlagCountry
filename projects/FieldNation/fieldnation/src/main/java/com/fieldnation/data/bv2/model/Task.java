package com.fieldnation.data.bv2.model;

import com.fieldnation.fnjson.JsonObject;
import com.fieldnation.fnjson.Serializer;
import com.fieldnation.fnjson.Unserializer;
import com.fieldnation.fnjson.annotations.Json;
import com.fieldnation.fnlog.Log;

public class Task {
    private static final String TAG = "Task";

    @Json(name = "id")
    private Integer id = null;

    @Json(name = "alerts")
    private TaskAlert[] alerts;

    @Json(name = "label")
    private String label = null;

    @Json(name = "group")
    private TaskGroup group = null;

    @Json(name = "time_zone")
    private TimeZone timeZone = null;

    @Json(name = "actions")
    private String[] actions;

    @Json(name = "type")
    private TaskType type = null;

    @Json(name = "created")
    private String created = null;

    @Json(name = "author")
    private User author = null;

    @Json(name = "completed")
    private Boolean completed = null;

    @Json(name = "check_in")
    private CheckInOut checkIn = null;

    @Json(name = "check_out")
    private CheckInOut checkOut = null;

    @Json(name = "closing_notes")
    private String closingNotes = null;

    @Json(name = "confirmed")
    private Boolean confirmed = null;

    @Json(name = "ready_to_go")
    private Boolean readyToGo = null;

    @Json(name = "on_my_way")
    private OnMyWay onMyWay = null;

    @Json(name = "attachments")
    private Attachment[] attachments;

    @Json(name = "custom_field")
    private CustomField customField = null;

    @Json(name = "value")
    private String value = null;

    @Json(name = "phone")
    private String phone = null;

    @Json(name = "email")
    private String email = null;

    @Json(name = "signature")
    private Signature signature = null;

    @Json(name = "shipment")
    private Shipment shipment = null;

    @Json(name = "attachment")
    private Attachment attachment = null;

    public Task() {
    }

    public Integer getId() {
        return id;
    }

    public TaskAlert[] getAlerts() {
        return alerts;
    }

    public String getLabel() {
        return label;
    }

    public TaskGroup getGroup() {
        return group;
    }

    public TimeZone getTimeZone() {
        return timeZone;
    }

    public String[] getActions() {
        return actions;
    }

    public TaskType getType() {
        return type;
    }

    public String getCreated() {
        return created;
    }

    public User getAuthor() {
        return author;
    }

    public Boolean getCompleted() {
        return completed;
    }

    public CheckInOut getCheckIn() {
        return checkIn;
    }

    public CheckInOut getCheckOut() {
        return checkOut;
    }

    public String getClosingNotes() {
        return closingNotes;
    }

    public Boolean getConfirmed() {
        return confirmed;
    }

    public Boolean getReadyToGo() {
        return readyToGo;
    }

    public OnMyWay getOnMyWay() {
        return onMyWay;
    }

    public Attachment[] getAttachments() {
        return attachments;
    }

    public CustomField getCustomField() {
        return customField;
    }

    public String getValue() {
        return value;
    }

    public String getPhone() {
        return phone;
    }

    public String getEmail() {
        return email;
    }

    public Signature getSignature() {
        return signature;
    }

    public Shipment getShipment() {
        return shipment;
    }

    public Attachment getAttachment() {
        return attachment;
    }

    /*-*****************************-*/
    /*-             Json            -*/
    /*-*****************************-*/
    public static Task fromJson(JsonObject obj) {
        try {
            return Unserializer.unserializeObject(Task.class, obj);
        } catch (Exception ex) {
            Log.v(TAG, ex);
            return null;
        }
    }

    public JsonObject toJson() {
        return toJson(this);
    }

    public static JsonObject toJson(Task task) {
        try {
            return Serializer.serializeObject(task);
        } catch (Exception ex) {
            Log.v(TAG, ex);
            return null;
        }
    }
}