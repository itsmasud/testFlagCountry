package com.fieldnation.data.bv2.model;

import com.fieldnation.fnjson.JsonObject;
import com.fieldnation.fnjson.Serializer;
import com.fieldnation.fnjson.Unserializer;
import com.fieldnation.fnjson.annotations.Json;
import com.fieldnation.fnlog.Log;

public class Task {
    private static final String TAG = "Task";

    @Json(name = "closing_notes")
    private String closingNotes;

    @Json(name = "custom_field")
    private CustomField customField;

    @Json(name = "attachments")
    private Attachment[] attachments;

    @Json(name = "shipment")
    private Shipment shipment;

    @Json(name = "signature")
    private Signature signature;

    @Json(name = "created")
    private Date created;

    @Json(name = "author")
    private User author;

    @Json(name = "check_in")
    private CheckInOut checkIn;

    @Json(name = "ready_to_go")
    private Boolean readyToGo;

    @Json(name = "label")
    private String label;

    @Json(name = "completed")
    private Boolean completed;

    @Json(name = "time_zone")
    private TimeZone timeZone;

    @Json(name = "type")
    private TaskType type;

    @Json(name = "confirmed")
    private Boolean confirmed;

    @Json(name = "on_my_way")
    private OnMyWay onMyWay;

    @Json(name = "alerts")
    private TaskAlert[] alerts;

    @Json(name = "check_out")
    private CheckInOut checkOut;

    @Json(name = "attachment")
    private Attachment attachment;

    @Json(name = "phone")
    private String phone;

    @Json(name = "id")
    private Integer id;

    @Json(name = "actions")
    private String[] actions;

    @Json(name = "value")
    private String value;

    @Json(name = "email")
    private String email;

    @Json(name = "group")
    private TaskGroup group;

    public Task() {
    }

    public String getClosingNotes() {
        return closingNotes;
    }

    public CustomField getCustomField() {
        return customField;
    }

    public Attachment[] getAttachments() {
        return attachments;
    }

    public Shipment getShipment() {
        return shipment;
    }

    public Signature getSignature() {
        return signature;
    }

    public Date getCreated() {
        return created;
    }

    public User getAuthor() {
        return author;
    }

    public CheckInOut getCheckIn() {
        return checkIn;
    }

    public Boolean getReadyToGo() {
        return readyToGo;
    }

    public String getLabel() {
        return label;
    }

    public Boolean getCompleted() {
        return completed;
    }

    public TimeZone getTimeZone() {
        return timeZone;
    }

    public TaskType getType() {
        return type;
    }

    public Boolean getConfirmed() {
        return confirmed;
    }

    public OnMyWay getOnMyWay() {
        return onMyWay;
    }

    public TaskAlert[] getAlerts() {
        return alerts;
    }

    public CheckInOut getCheckOut() {
        return checkOut;
    }

    public Attachment getAttachment() {
        return attachment;
    }

    public String getPhone() {
        return phone;
    }

    public Integer getId() {
        return id;
    }

    public String[] getActions() {
        return actions;
    }

    public String getValue() {
        return value;
    }

    public String getEmail() {
        return email;
    }

    public TaskGroup getGroup() {
        return group;
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
