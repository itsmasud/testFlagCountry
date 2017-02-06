package com.fieldnation.v2.data.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.fieldnation.fnjson.JsonArray;
import com.fieldnation.fnjson.JsonObject;
import com.fieldnation.fnjson.Serializer;
import com.fieldnation.fnjson.Unserializer;
import com.fieldnation.fnjson.annotations.Json;
import com.fieldnation.fnlog.Log;

/**
 * Created by dmgen from swagger on 1/31/17.
 */

public class Task implements Parcelable {
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
    private ActionsEnum[] _actions;

    @Json(name = "value")
    private String _value;

    @Json(name = "email")
    private String _email;

    @Json(name = "group")
    private TaskGroup _group;

    public Task() {
    }

    public void setClosingNotes(String closingNotes) {
        _closingNotes = closingNotes;
    }

    public String getClosingNotes() {
        return _closingNotes;
    }

    public Task closingNotes(String closingNotes) {
        _closingNotes = closingNotes;
        return this;
    }

    public void setCustomField(CustomField customField) {
        _customField = customField;
    }

    public CustomField getCustomField() {
        return _customField;
    }

    public Task customField(CustomField customField) {
        _customField = customField;
        return this;
    }

    public void setAttachments(Attachment[] attachments) {
        _attachments = attachments;
    }

    public Attachment[] getAttachments() {
        return _attachments;
    }

    public Task attachments(Attachment[] attachments) {
        _attachments = attachments;
        return this;
    }

    public void setShipment(Shipment shipment) {
        _shipment = shipment;
    }

    public Shipment getShipment() {
        return _shipment;
    }

    public Task shipment(Shipment shipment) {
        _shipment = shipment;
        return this;
    }

    public void setSignature(Signature signature) {
        _signature = signature;
    }

    public Signature getSignature() {
        return _signature;
    }

    public Task signature(Signature signature) {
        _signature = signature;
        return this;
    }

    public void setCreated(Date created) {
        _created = created;
    }

    public Date getCreated() {
        return _created;
    }

    public Task created(Date created) {
        _created = created;
        return this;
    }

    public void setAuthor(User author) {
        _author = author;
    }

    public User getAuthor() {
        return _author;
    }

    public Task author(User author) {
        _author = author;
        return this;
    }

    public void setCheckIn(CheckInOut checkIn) {
        _checkIn = checkIn;
    }

    public CheckInOut getCheckIn() {
        return _checkIn;
    }

    public Task checkIn(CheckInOut checkIn) {
        _checkIn = checkIn;
        return this;
    }

    public void setReadyToGo(Boolean readyToGo) {
        _readyToGo = readyToGo;
    }

    public Boolean getReadyToGo() {
        return _readyToGo;
    }

    public Task readyToGo(Boolean readyToGo) {
        _readyToGo = readyToGo;
        return this;
    }

    public void setLabel(String label) {
        _label = label;
    }

    public String getLabel() {
        return _label;
    }

    public Task label(String label) {
        _label = label;
        return this;
    }

    public void setCompleted(Boolean completed) {
        _completed = completed;
    }

    public Boolean getCompleted() {
        return _completed;
    }

    public Task completed(Boolean completed) {
        _completed = completed;
        return this;
    }

    public void setTimeZone(TimeZone timeZone) {
        _timeZone = timeZone;
    }

    public TimeZone getTimeZone() {
        return _timeZone;
    }

    public Task timeZone(TimeZone timeZone) {
        _timeZone = timeZone;
        return this;
    }

    public void setType(TaskType type) {
        _type = type;
    }

    public TaskType getType() {
        return _type;
    }

    public Task type(TaskType type) {
        _type = type;
        return this;
    }

    public void setConfirmed(Boolean confirmed) {
        _confirmed = confirmed;
    }

    public Boolean getConfirmed() {
        return _confirmed;
    }

    public Task confirmed(Boolean confirmed) {
        _confirmed = confirmed;
        return this;
    }

    public void setOnMyWay(OnMyWay onMyWay) {
        _onMyWay = onMyWay;
    }

    public OnMyWay getOnMyWay() {
        return _onMyWay;
    }

    public Task onMyWay(OnMyWay onMyWay) {
        _onMyWay = onMyWay;
        return this;
    }

    public void setAlerts(TaskAlert[] alerts) {
        _alerts = alerts;
    }

    public TaskAlert[] getAlerts() {
        return _alerts;
    }

    public Task alerts(TaskAlert[] alerts) {
        _alerts = alerts;
        return this;
    }

    public void setCheckOut(CheckInOut checkOut) {
        _checkOut = checkOut;
    }

    public CheckInOut getCheckOut() {
        return _checkOut;
    }

    public Task checkOut(CheckInOut checkOut) {
        _checkOut = checkOut;
        return this;
    }

    public void setAttachment(Attachment attachment) {
        _attachment = attachment;
    }

    public Attachment getAttachment() {
        return _attachment;
    }

    public Task attachment(Attachment attachment) {
        _attachment = attachment;
        return this;
    }

    public void setPhone(String phone) {
        _phone = phone;
    }

    public String getPhone() {
        return _phone;
    }

    public Task phone(String phone) {
        _phone = phone;
        return this;
    }

    public void setId(Integer id) {
        _id = id;
    }

    public Integer getId() {
        return _id;
    }

    public Task id(Integer id) {
        _id = id;
        return this;
    }

    public void setActions(ActionsEnum[] actions) {
        _actions = actions;
    }

    public ActionsEnum[] getActions() {
        return _actions;
    }

    public Task actions(ActionsEnum[] actions) {
        _actions = actions;
        return this;
    }

    public void setValue(String value) {
        _value = value;
    }

    public String getValue() {
        return _value;
    }

    public Task value(String value) {
        _value = value;
        return this;
    }

    public void setEmail(String email) {
        _email = email;
    }

    public String getEmail() {
        return _email;
    }

    public Task email(String email) {
        _email = email;
        return this;
    }

    public void setGroup(TaskGroup group) {
        _group = group;
    }

    public TaskGroup getGroup() {
        return _group;
    }

    public Task group(TaskGroup group) {
        _group = group;
        return this;
    }

    /*-*****************************-*/
    /*-             Json            -*/
    /*-*****************************-*/
    public static Task[] fromJsonArray(JsonArray array) {
        Task[] list = new Task[array.size()];
        for (int i = 0; i < array.size(); i++) {
            list[i] = fromJson(array.getJsonObject(i));
        }
        return list;
    }

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

    /*-*********************************************-*/
    /*-			Parcelable Implementation           -*/
    /*-*********************************************-*/
    public static final Parcelable.Creator<Task> CREATOR = new Parcelable.Creator<Task>() {

        @Override
        public Task createFromParcel(Parcel source) {
            try {
                return Task.fromJson((JsonObject) source.readParcelable(JsonObject.class.getClassLoader()));
            } catch (Exception ex) {
                Log.v(TAG, ex);
                return null;
            }
        }

        @Override
        public Task[] newArray(int size) {
            return new Task[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(toJson(), flags);
    }
}
