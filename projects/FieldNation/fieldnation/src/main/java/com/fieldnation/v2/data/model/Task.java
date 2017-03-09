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
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

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

    @Json(name = "description")
    private String _description;

    @Json(name = "email")
    private String _email;

    @Json(name = "folder_id")
    private Integer _folderId;

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
    private JsonObject SOURCE;

    public Task() {
        SOURCE = new JsonObject();
    }

    public Task(JsonObject obj) {
        SOURCE = obj;
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
        try {
            if (_actions != null)
                return _actions;

            if (SOURCE.has("actions") && SOURCE.get("actions") != null) {
                _actions = ActionsEnum.fromJsonArray(SOURCE.getJsonArray("actions"));
            }

        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

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
        try {
            if (_alerts != null)
                return _alerts;

            if (SOURCE.has("alerts") && SOURCE.get("alerts") != null) {
                _alerts = TaskAlert.fromJsonArray(SOURCE.getJsonArray("alerts"));
            }

        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

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
        try {
            if (_attachment != null)
                return _attachment;

            if (SOURCE.has("attachment") && SOURCE.get("attachment") != null)
                _attachment = Attachment.fromJson(SOURCE.getJsonObject("attachment"));

        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

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
        try {
            if (_attachments != null)
                return _attachments;

            if (SOURCE.has("attachments") && SOURCE.get("attachments") != null) {
                _attachments = Attachment.fromJsonArray(SOURCE.getJsonArray("attachments"));
            }

        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

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
        try {
            if (_author != null)
                return _author;

            if (SOURCE.has("author") && SOURCE.get("author") != null)
                _author = User.fromJson(SOURCE.getJsonObject("author"));

        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

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
        try {
            if (_checkIn != null)
                return _checkIn;

            if (SOURCE.has("check_in") && SOURCE.get("check_in") != null)
                _checkIn = CheckInOut.fromJson(SOURCE.getJsonObject("check_in"));

        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

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
        try {
            if (_checkOut != null)
                return _checkOut;

            if (SOURCE.has("check_out") && SOURCE.get("check_out") != null)
                _checkOut = CheckInOut.fromJson(SOURCE.getJsonObject("check_out"));

        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

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
        try {
            if (_closingNotes != null)
                return _closingNotes;

            if (SOURCE.has("closing_notes") && SOURCE.get("closing_notes") != null)
                _closingNotes = SOURCE.getString("closing_notes");

        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

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
        try {
            if (_completed != null)
                return _completed;

            if (SOURCE.has("completed") && SOURCE.get("completed") != null)
                _completed = SOURCE.getBoolean("completed");

        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

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
        try {
            if (_confirmed != null)
                return _confirmed;

            if (SOURCE.has("confirmed") && SOURCE.get("confirmed") != null)
                _confirmed = SOURCE.getBoolean("confirmed");

        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

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
        try {
            if (_created != null)
                return _created;

            if (SOURCE.has("created") && SOURCE.get("created") != null)
                _created = Date.fromJson(SOURCE.getJsonObject("created"));

        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

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
        try {
            if (_customField != null)
                return _customField;

            if (SOURCE.has("custom_field") && SOURCE.get("custom_field") != null)
                _customField = CustomField.fromJson(SOURCE.getJsonObject("custom_field"));

        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _customField;
    }

    public Task customField(CustomField customField) throws ParseException {
        _customField = customField;
        SOURCE.put("custom_field", customField.getJson());
        return this;
    }

    public void setDescription(String description) throws ParseException {
        _description = description;
        SOURCE.put("description", description);
    }

    public String getDescription() {
        try {
            if (_description != null)
                return _description;

            if (SOURCE.has("description") && SOURCE.get("description") != null)
                _description = SOURCE.getString("description");

        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _description;
    }

    public Task description(String description) throws ParseException {
        _description = description;
        SOURCE.put("description", description);
        return this;
    }

    public void setEmail(String email) throws ParseException {
        _email = email;
        SOURCE.put("email", email);
    }

    public String getEmail() {
        try {
            if (_email != null)
                return _email;

            if (SOURCE.has("email") && SOURCE.get("email") != null)
                _email = SOURCE.getString("email");

        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _email;
    }

    public Task email(String email) throws ParseException {
        _email = email;
        SOURCE.put("email", email);
        return this;
    }

    public void setFolderId(Integer folderId) throws ParseException {
        _folderId = folderId;
        SOURCE.put("folder_id", folderId);
    }

    public Integer getFolderId() {
        try {
            if (_folderId != null)
                return _folderId;

            if (SOURCE.has("folder_id") && SOURCE.get("folder_id") != null)
                _folderId = SOURCE.getInt("folder_id");

        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _folderId;
    }

    public Task folderId(Integer folderId) throws ParseException {
        _folderId = folderId;
        SOURCE.put("folder_id", folderId);
        return this;
    }

    public void setGroup(TaskGroup group) throws ParseException {
        _group = group;
        SOURCE.put("group", group.getJson());
    }

    public TaskGroup getGroup() {
        try {
            if (_group != null)
                return _group;

            if (SOURCE.has("group") && SOURCE.get("group") != null)
                _group = TaskGroup.fromJson(SOURCE.getJsonObject("group"));

        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

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
        try {
            if (_id != null)
                return _id;

            if (SOURCE.has("id") && SOURCE.get("id") != null)
                _id = SOURCE.getInt("id");

        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

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
        try {
            if (_label != null)
                return _label;

            if (SOURCE.has("label") && SOURCE.get("label") != null)
                _label = SOURCE.getString("label");

        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

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
        try {
            if (_onMyWay != null)
                return _onMyWay;

            if (SOURCE.has("on_my_way") && SOURCE.get("on_my_way") != null)
                _onMyWay = OnMyWay.fromJson(SOURCE.getJsonObject("on_my_way"));

        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

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
        try {
            if (_phone != null)
                return _phone;

            if (SOURCE.has("phone") && SOURCE.get("phone") != null)
                _phone = SOURCE.getString("phone");

        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

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
        try {
            if (_readyToGo != null)
                return _readyToGo;

            if (SOURCE.has("ready_to_go") && SOURCE.get("ready_to_go") != null)
                _readyToGo = SOURCE.getBoolean("ready_to_go");

        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

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
        try {
            if (_shipment != null)
                return _shipment;

            if (SOURCE.has("shipment") && SOURCE.get("shipment") != null)
                _shipment = Shipment.fromJson(SOURCE.getJsonObject("shipment"));

        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

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
        try {
            if (_signature != null)
                return _signature;

            if (SOURCE.has("signature") && SOURCE.get("signature") != null)
                _signature = Signature.fromJson(SOURCE.getJsonObject("signature"));

        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

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
        try {
            if (_timeZone != null)
                return _timeZone;

            if (SOURCE.has("time_zone") && SOURCE.get("time_zone") != null)
                _timeZone = TimeZone.fromJson(SOURCE.getJsonObject("time_zone"));

        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

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
        try {
            if (_type != null)
                return _type;

            if (SOURCE.has("type") && SOURCE.get("type") != null)
                _type = TaskType.fromJson(SOURCE.getJsonObject("type"));

        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

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
        try {
            if (_value != null)
                return _value;

            if (SOURCE.has("value") && SOURCE.get("value") != null)
                _value = SOURCE.getString("value");

        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

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
        @Json(name = "check_in")
        CHECK_IN("check_in"),
        @Json(name = "check_out")
        CHECK_OUT("check_out"),
        @Json(name = "closing_notes")
        CLOSING_NOTES("closing_notes"),
        @Json(name = "complete")
        COMPLETE("complete"),
        @Json(name = "create_shipment")
        CREATE_SHIPMENT("create_shipment"),
        @Json(name = "eta")
        ETA("eta"),
        @Json(name = "incomplete")
        INCOMPLETE("incomplete"),
        @Json(name = "signature")
        SIGNATURE("signature"),
        @Json(name = "upload")
        UPLOAD("upload");

        private String value;

        ActionsEnum(String value) {
            this.value = value;
        }

        public static ActionsEnum fromString(String value) {
            ActionsEnum[] values = values();
            for (ActionsEnum v : values) {
                if (v.value.equals(value))
                    return v;
            }
            return null;
        }

        public static ActionsEnum[] fromJsonArray(JsonArray jsonArray) {
            ActionsEnum[] list = new ActionsEnum[jsonArray.size()];
            for (int i = 0; i < list.length; i++) {
                list[i] = fromString(jsonArray.getString(i));
            }
            return list;
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
            return new Task(obj);
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

    /*-*****************************-*/
    /*-         Human Code          -*/
    /*-*****************************-*/
    private Set<Task.ActionsEnum> _actionsSet = null;

    public Set<Task.ActionsEnum> getActionsSet() {
        if (_actionsSet == null) {
            _actionsSet = new HashSet<>();
            _actionsSet.addAll(Arrays.asList(getActions()));
        }
        return _actionsSet;
    }
}
