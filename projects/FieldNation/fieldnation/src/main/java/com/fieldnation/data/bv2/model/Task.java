package com.fieldnation.data.bv2.model;

import com.fieldnation.fnjson.JsonObject;
import com.fieldnation.fnjson.Serializer;
import com.fieldnation.fnjson.Unserializer;
import com.fieldnation.fnjson.annotations.Json;
import com.fieldnation.fnlog.Log;

public class Task {
    private static final String TAG = "Task";

    @Json(name = "closing_notes")
    private String _closingNotes;

    @Json(name = "custom_field")
    private CustomField _customField;

    @Json(name = "attachments")
    private Attachment[] _attachments;

    @Json(name = "shipment")
    private Shipment _shipment;

    @Json(name = "signature")
    private Signature _signature;

    @Json(name = "created")
    private Date _created;

    @Json(name = "author")
    private User _author;

    @Json(name = "check_in")
    private CheckInOut _checkIn;

    @Json(name = "ready_to_go")
    private Boolean _readyToGo;

    @Json(name = "label")
    private String _label;

    @Json(name = "completed")
    private Boolean _completed;

    @Json(name = "time_zone")
    private TimeZone _timeZone;

    @Json(name = "type")
    private TaskType _type;

    @Json(name = "confirmed")
    private Boolean _confirmed;

    @Json(name = "on_my_way")
    private OnMyWay _onMyWay;

    @Json(name = "alerts")
    private TaskAlert[] _alerts;

    @Json(name = "check_out")
    private CheckInOut _checkOut;

    @Json(name = "attachment")
    private Attachment _attachment;

    @Json(name = "phone")
    private String _phone;

    @Json(name = "id")
    private Integer _id;

    @Json(name = "actions")
    private String[] _actions;

    @Json(name = "value")
    private String _value;

    @Json(name = "email")
    private String _email;

    @Json(name = "group")
    private TaskGroup _group;

    public Task() {
    }

    public String getClosingNotes() {
        return _closingNotes;
    }

    public CustomField getCustomField() {
        return _customField;
    }

    public Attachment[] getAttachments() {
        return _attachments;
    }

    public Shipment getShipment() {
        return _shipment;
    }

    public Signature getSignature() {
        return _signature;
    }

    public Date getCreated() {
        return _created;
    }

    public User getAuthor() {
        return _author;
    }

    public CheckInOut getCheckIn() {
        return _checkIn;
    }

    public Boolean getReadyToGo() {
        return _readyToGo;
    }

    public String getLabel() {
        return _label;
    }

    public Boolean getCompleted() {
        return _completed;
    }

    public TimeZone getTimeZone() {
        return _timeZone;
    }

    public TaskType getType() {
        return _type;
    }

    public Boolean getConfirmed() {
        return _confirmed;
    }

    public OnMyWay getOnMyWay() {
        return _onMyWay;
    }

    public TaskAlert[] getAlerts() {
        return _alerts;
    }

    public CheckInOut getCheckOut() {
        return _checkOut;
    }

    public Attachment getAttachment() {
        return _attachment;
    }

    public String getPhone() {
        return _phone;
    }

    public Integer getId() {
        return _id;
    }

    public String[] getActions() {
        return _actions;
    }

    public String getValue() {
        return _value;
    }

    public String getEmail() {
        return _email;
    }

    public TaskGroup getGroup() {
        return _group;
    }

    /*-*****************************-*/
    /*-             Json            -*/
    /*-*****************************-*/
    public static Task fromJson(JsonObject obj) {
        try {
            return Unserializer.unserializeObject(Task.class, obj);
        } catch (Exception ex) {
            Log.v(TAG, TAG, ex);
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
            Log.v(TAG, TAG, ex);
            return null;
        }
    }
}
