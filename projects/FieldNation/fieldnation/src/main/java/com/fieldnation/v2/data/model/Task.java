package com.fieldnation.v2.data.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.fieldnation.fnjson.JsonArray;
import com.fieldnation.fnjson.JsonObject;
import com.fieldnation.fnjson.Serializer;
import com.fieldnation.fnjson.Unserializer;
import com.fieldnation.fnjson.annotations.Json;
import com.fieldnation.fnjson.annotations.Source;
import com.fieldnation.fnlog.Log;

import java.text.ParseException;

/**
 * Created by dmgen from swagger.
 */

public class Task implements Parcelable {
    private static final String TAG = "Task";

    @Json(name = "actions")
    private ActionsEnum[] _actions;

    @Json(name = "alerts")
    private TaskAlert[] _alerts;

    @Json(name = "attachment")
    private Attachment _attachment;

    @Json(name = "attachments")
    private Attachment[] _attachments;

    @Json(name = "author")
    private User _author;

    @Json(name = "check_in")
    private CheckInOut _checkIn;

    @Json(name = "check_out")
    private CheckInOut _checkOut;

    @Json(name = "closing_notes")
    private String _closingNotes;

    @Json(name = "completed")
    private Boolean _completed;

    @Json(name = "confirmed")
    private Boolean _confirmed;

    @Json(name = "created")
    private Date _created;

    @Json(name = "custom_field")
    private CustomField _customField;

    @Json(name = "email")
    private String _email;

    @Json(name = "group")
    private TaskGroup _group;

    @Json(name = "id")
    private Integer _id;

    @Json(name = "label")
    private String _label;

    @Json(name = "on_my_way")
    private OnMyWay _onMyWay;

    @Json(name = "phone")
    private String _phone;

    @Json(name = "ready_to_go")
    private Boolean _readyToGo;

    @Json(name = "shipment")
    private Shipment _shipment;

    @Json(name = "signature")
    private Signature _signature;

    @Json(name = "time_zone")
    private TimeZone _timeZone;

    @Json(name = "type")
    private TaskType _type;

    @Json(name = "value")
    private String _value;

    @Source
    private JsonObject SOURCE = new JsonObject();

    public Task() {
    }

    public void setActions(ActionsEnum[] actions) throws ParseException {
        _actions = actions;
        JsonArray ja = new JsonArray();
        for (ActionsEnum item : actions) {
            ja.add(item.toString());
        }
        SOURCE.put("actions", ja);
    }

    public ActionsEnum[] getActions() {
        return _actions;
    }

    public Task actions(ActionsEnum[] actions) throws ParseException {
        _actions = actions;
        JsonArray ja = new JsonArray();
        for (ActionsEnum item : actions) {
            ja.add(item.toString());
        }
        SOURCE.put("actions", ja, true);
        return this;
    }

    public void setAlerts(TaskAlert[] alerts) throws ParseException {
        _alerts = alerts;
        SOURCE.put("alerts", TaskAlert.toJsonArray(alerts));
    }

    public TaskAlert[] getAlerts() {
        return _alerts;
    }

    public Task alerts(TaskAlert[] alerts) throws ParseException {
        _alerts = alerts;
        SOURCE.put("alerts", TaskAlert.toJsonArray(alerts), true);
        return this;
    }

    public void setAttachment(Attachment attachment) throws ParseException {
        _attachment = attachment;
        SOURCE.put("attachment", attachment.getJson());
    }

    public Attachment getAttachment() {
        return _attachment;
    }

    public Task attachment(Attachment attachment) throws ParseException {
        _attachment = attachment;
        SOURCE.put("attachment", attachment.getJson());
        return this;
    }

    public void setAttachments(Attachment[] attachments) throws ParseException {
        _attachments = attachments;
        SOURCE.put("attachments", Attachment.toJsonArray(attachments));
    }

    public Attachment[] getAttachments() {
        return _attachments;
    }

    public Task attachments(Attachment[] attachments) throws ParseException {
        _attachments = attachments;
        SOURCE.put("attachments", Attachment.toJsonArray(attachments), true);
        return this;
    }

    public void setAuthor(User author) throws ParseException {
        _author = author;
        SOURCE.put("author", author.getJson());
    }

    public User getAuthor() {
        return _author;
    }

    public Task author(User author) throws ParseException {
        _author = author;
        SOURCE.put("author", author.getJson());
        return this;
    }

    public void setCheckIn(CheckInOut checkIn) throws ParseException {
        _checkIn = checkIn;
        SOURCE.put("check_in", checkIn.getJson());
    }

    public CheckInOut getCheckIn() {
        return _checkIn;
    }

    public Task checkIn(CheckInOut checkIn) throws ParseException {
        _checkIn = checkIn;
        SOURCE.put("check_in", checkIn.getJson());
        return this;
    }

    public void setCheckOut(CheckInOut checkOut) throws ParseException {
        _checkOut = checkOut;
        SOURCE.put("check_out", checkOut.getJson());
    }

    public CheckInOut getCheckOut() {
        return _checkOut;
    }

    public Task checkOut(CheckInOut checkOut) throws ParseException {
        _checkOut = checkOut;
        SOURCE.put("check_out", checkOut.getJson());
        return this;
    }

    public void setClosingNotes(String closingNotes) throws ParseException {
        _closingNotes = closingNotes;
        SOURCE.put("closing_notes", closingNotes);
    }

    public String getClosingNotes() {
        return _closingNotes;
    }

    public Task closingNotes(String closingNotes) throws ParseException {
        _closingNotes = closingNotes;
        SOURCE.put("closing_notes", closingNotes);
        return this;
    }

    public void setCompleted(Boolean completed) throws ParseException {
        _completed = completed;
        SOURCE.put("completed", completed);
    }

    public Boolean getCompleted() {
        return _completed;
    }

    public Task completed(Boolean completed) throws ParseException {
        _completed = completed;
        SOURCE.put("completed", completed);
        return this;
    }

    public void setConfirmed(Boolean confirmed) throws ParseException {
        _confirmed = confirmed;
        SOURCE.put("confirmed", confirmed);
    }

    public Boolean getConfirmed() {
        return _confirmed;
    }

    public Task confirmed(Boolean confirmed) throws ParseException {
        _confirmed = confirmed;
        SOURCE.put("confirmed", confirmed);
        return this;
    }

    public void setCreated(Date created) throws ParseException {
        _created = created;
        SOURCE.put("created", created.getJson());
    }

    public Date getCreated() {
        return _created;
    }

    public Task created(Date created) throws ParseException {
        _created = created;
        SOURCE.put("created", created.getJson());
        return this;
    }

    public void setCustomField(CustomField customField) throws ParseException {
        _customField = customField;
        SOURCE.put("custom_field", customField.getJson());
    }

    public CustomField getCustomField() {
        return _customField;
    }

    public Task customField(CustomField customField) throws ParseException {
        _customField = customField;
        SOURCE.put("custom_field", customField.getJson());
        return this;
    }

    public void setEmail(String email) throws ParseException {
        _email = email;
        SOURCE.put("email", email);
    }

    public String getEmail() {
        return _email;
    }

    public Task email(String email) throws ParseException {
        _email = email;
        SOURCE.put("email", email);
        return this;
    }

    public void setGroup(TaskGroup group) throws ParseException {
        _group = group;
        SOURCE.put("group", group.getJson());
    }

    public TaskGroup getGroup() {
        return _group;
    }

    public Task group(TaskGroup group) throws ParseException {
        _group = group;
        SOURCE.put("group", group.getJson());
        return this;
    }

    public void setId(Integer id) throws ParseException {
        _id = id;
        SOURCE.put("id", id);
    }

    public Integer getId() {
        return _id;
    }

    public Task id(Integer id) throws ParseException {
        _id = id;
        SOURCE.put("id", id);
        return this;
    }

    public void setLabel(String label) throws ParseException {
        _label = label;
        SOURCE.put("label", label);
    }

    public String getLabel() {
        return _label;
    }

    public Task label(String label) throws ParseException {
        _label = label;
        SOURCE.put("label", label);
        return this;
    }

    public void setOnMyWay(OnMyWay onMyWay) throws ParseException {
        _onMyWay = onMyWay;
        SOURCE.put("on_my_way", onMyWay.getJson());
    }

    public OnMyWay getOnMyWay() {
        return _onMyWay;
    }

    public Task onMyWay(OnMyWay onMyWay) throws ParseException {
        _onMyWay = onMyWay;
        SOURCE.put("on_my_way", onMyWay.getJson());
        return this;
    }

    public void setPhone(String phone) throws ParseException {
        _phone = phone;
        SOURCE.put("phone", phone);
    }

    public String getPhone() {
        return _phone;
    }

    public Task phone(String phone) throws ParseException {
        _phone = phone;
        SOURCE.put("phone", phone);
        return this;
    }

    public void setReadyToGo(Boolean readyToGo) throws ParseException {
        _readyToGo = readyToGo;
        SOURCE.put("ready_to_go", readyToGo);
    }

    public Boolean getReadyToGo() {
        return _readyToGo;
    }

    public Task readyToGo(Boolean readyToGo) throws ParseException {
        _readyToGo = readyToGo;
        SOURCE.put("ready_to_go", readyToGo);
        return this;
    }

    public void setShipment(Shipment shipment) throws ParseException {
        _shipment = shipment;
        SOURCE.put("shipment", shipment.getJson());
    }

    public Shipment getShipment() {
        return _shipment;
    }

    public Task shipment(Shipment shipment) throws ParseException {
        _shipment = shipment;
        SOURCE.put("shipment", shipment.getJson());
        return this;
    }

    public void setSignature(Signature signature) throws ParseException {
        _signature = signature;
        SOURCE.put("signature", signature.getJson());
    }

    public Signature getSignature() {
        return _signature;
    }

    public Task signature(Signature signature) throws ParseException {
        _signature = signature;
        SOURCE.put("signature", signature.getJson());
        return this;
    }

    public void setTimeZone(TimeZone timeZone) throws ParseException {
        _timeZone = timeZone;
        SOURCE.put("time_zone", timeZone.getJson());
    }

    public TimeZone getTimeZone() {
        return _timeZone;
    }

    public Task timeZone(TimeZone timeZone) throws ParseException {
        _timeZone = timeZone;
        SOURCE.put("time_zone", timeZone.getJson());
        return this;
    }

    public void setType(TaskType type) throws ParseException {
        _type = type;
        SOURCE.put("type", type.getJson());
    }

    public TaskType getType() {
        return _type;
    }

    public Task type(TaskType type) throws ParseException {
        _type = type;
        SOURCE.put("type", type.getJson());
        return this;
    }

    public void setValue(String value) throws ParseException {
        _value = value;
        SOURCE.put("value", value);
    }

    public String getValue() {
        return _value;
    }

    public Task value(String value) throws ParseException {
        _value = value;
        SOURCE.put("value", value);
        return this;
    }

    /*-******************************-*/
    /*-             Enums            -*/
    /*-******************************-*/
    public enum ActionsEnum {
        @Json(name = "unknown")
        UNKNOWN("unknown");

        private String value;

        ActionsEnum(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return String.valueOf(value);
        }
    }

    /*-*****************************-*/
    /*-             Json            -*/
    /*-*****************************-*/
    public static JsonArray toJsonArray(Task[] array) {
        JsonArray list = new JsonArray();
        for (Task item : array) {
            list.add(item.getJson());
        }
        return list;
    }

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

    public JsonObject getJson() {
        return SOURCE;
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
        dest.writeParcelable(getJson(), flags);
    }
}
