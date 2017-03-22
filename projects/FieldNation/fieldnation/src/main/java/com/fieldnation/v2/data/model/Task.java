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
import com.fieldnation.fntools.misc;

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
    private AttachmentFolder _attachments;

    @Json(name = "author")
    private User _author;

    @Json(name = "check_in")
    private CheckInOut _checkIn;

    @Json(name = "check_out")
    private CheckInOut _checkOut;

    @Json(name = "closing_notes")
    private String _closingNotes;

    @Json(name = "completed")
    private Date _completed;

    @Json(name = "confirmed")
    private Boolean _confirmed;

    @Json(name = "created")
    private Date _created;

    @Json(name = "custom_field")
    private CustomField _customField;

    @Json(name = "description")
    private String _description;

    @Json(name = "document")
    private TaskDocument _document;

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
            if (_attachment == null && SOURCE.has("attachment") && SOURCE.get("attachment") != null)
                _attachment = Attachment.fromJson(SOURCE.getJsonObject("attachment"));
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        if (_attachment != null && _attachment.isSet())
            return _attachment;

        return null;
    }

    public Task attachment(Attachment attachment) throws ParseException {
        _attachment = attachment;
        SOURCE.put("attachment", attachment.getJson());
        return this;
    }

    public void setAttachments(AttachmentFolder attachments) throws ParseException {
        _attachments = attachments;
        SOURCE.put("attachments", attachments.getJson());
    }

    public AttachmentFolder getAttachments() {
        try {
            if (_attachments == null && SOURCE.has("attachments") && SOURCE.get("attachments") != null)
                _attachments = AttachmentFolder.fromJson(SOURCE.getJsonObject("attachments"));
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        if (_attachments != null && _attachments.isSet())
            return _attachments;

        return null;
    }

    public Task attachments(AttachmentFolder attachments) throws ParseException {
        _attachments = attachments;
        SOURCE.put("attachments", attachments.getJson());
        return this;
    }

    public void setAuthor(User author) throws ParseException {
        _author = author;
        SOURCE.put("author", author.getJson());
    }

    public User getAuthor() {
        try {
            if (_author == null && SOURCE.has("author") && SOURCE.get("author") != null)
                _author = User.fromJson(SOURCE.getJsonObject("author"));
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        if (_author != null && _author.isSet())
            return _author;

        return null;
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
            if (_checkIn == null && SOURCE.has("check_in") && SOURCE.get("check_in") != null)
                _checkIn = CheckInOut.fromJson(SOURCE.getJsonObject("check_in"));
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        if (_checkIn != null && _checkIn.isSet())
            return _checkIn;

        return null;
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
            if (_checkOut == null && SOURCE.has("check_out") && SOURCE.get("check_out") != null)
                _checkOut = CheckInOut.fromJson(SOURCE.getJsonObject("check_out"));
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        if (_checkOut != null && _checkOut.isSet())
            return _checkOut;

        return null;
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
            if (_closingNotes == null && SOURCE.has("closing_notes") && SOURCE.get("closing_notes") != null)
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

    public void setCompleted(Date completed) throws ParseException {
        _completed = completed;
        SOURCE.put("completed", completed.getJson());
    }

    public Date getCompleted() {
        try {
            if (_completed == null && SOURCE.has("completed") && SOURCE.get("completed") != null)
                _completed = Date.fromJson(SOURCE.getJsonObject("completed"));
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        if (_completed != null && _completed.isSet())
            return _completed;

        return null;
    }

    public Task completed(Date completed) throws ParseException {
        _completed = completed;
        SOURCE.put("completed", completed.getJson());
        return this;
    }

    public void setConfirmed(Boolean confirmed) throws ParseException {
        _confirmed = confirmed;
        SOURCE.put("confirmed", confirmed);
    }

    public Boolean getConfirmed() {
        try {
            if (_confirmed == null && SOURCE.has("confirmed") && SOURCE.get("confirmed") != null)
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
            if (_created == null && SOURCE.has("created") && SOURCE.get("created") != null)
                _created = Date.fromJson(SOURCE.getJsonObject("created"));
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        if (_created != null && _created.isSet())
            return _created;

        return null;
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
            if (_customField == null && SOURCE.has("custom_field") && SOURCE.get("custom_field") != null)
                _customField = CustomField.fromJson(SOURCE.getJsonObject("custom_field"));
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        if (_customField != null && _customField.isSet())
            return _customField;

        return null;
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
            if (_description == null && SOURCE.has("description") && SOURCE.get("description") != null)
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

    public void setDocument(TaskDocument document) throws ParseException {
        _document = document;
        SOURCE.put("document", document.getJson());
    }

    public TaskDocument getDocument() {
        try {
            if (_document == null && SOURCE.has("document") && SOURCE.get("document") != null)
                _document = TaskDocument.fromJson(SOURCE.getJsonObject("document"));
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        if (_document != null && _document.isSet())
            return _document;

        return null;
    }

    public Task document(TaskDocument document) throws ParseException {
        _document = document;
        SOURCE.put("document", document.getJson());
        return this;
    }

    public void setEmail(String email) throws ParseException {
        _email = email;
        SOURCE.put("email", email);
    }

    public String getEmail() {
        try {
            if (_email == null && SOURCE.has("email") && SOURCE.get("email") != null)
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
            if (_folderId == null && SOURCE.has("folder_id") && SOURCE.get("folder_id") != null)
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
            if (_group == null && SOURCE.has("group") && SOURCE.get("group") != null)
                _group = TaskGroup.fromJson(SOURCE.getJsonObject("group"));
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        if (_group != null && _group.isSet())
            return _group;

        return null;
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
            if (_id == null && SOURCE.has("id") && SOURCE.get("id") != null)
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
            if (_label == null && SOURCE.has("label") && SOURCE.get("label") != null)
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
            if (_onMyWay == null && SOURCE.has("on_my_way") && SOURCE.get("on_my_way") != null)
                _onMyWay = OnMyWay.fromJson(SOURCE.getJsonObject("on_my_way"));
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        if (_onMyWay != null && _onMyWay.isSet())
            return _onMyWay;

        return null;
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
            if (_phone == null && SOURCE.has("phone") && SOURCE.get("phone") != null)
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
            if (_readyToGo == null && SOURCE.has("ready_to_go") && SOURCE.get("ready_to_go") != null)
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
            if (_shipment == null && SOURCE.has("shipment") && SOURCE.get("shipment") != null)
                _shipment = Shipment.fromJson(SOURCE.getJsonObject("shipment"));
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        if (_shipment != null && _shipment.isSet())
            return _shipment;

        return null;
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
            if (_signature == null && SOURCE.has("signature") && SOURCE.get("signature") != null)
                _signature = Signature.fromJson(SOURCE.getJsonObject("signature"));
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        if (_signature != null && _signature.isSet())
            return _signature;

        return null;
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
            if (_timeZone == null && SOURCE.has("time_zone") && SOURCE.get("time_zone") != null)
                _timeZone = TimeZone.fromJson(SOURCE.getJsonObject("time_zone"));
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        if (_timeZone != null && _timeZone.isSet())
            return _timeZone;

        return null;
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
            if (_type == null && SOURCE.has("type") && SOURCE.get("type") != null)
                _type = TaskType.fromJson(SOURCE.getJsonObject("type"));
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        if (_type != null && _type.isSet())
            return _type;

        return null;
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
            if (_value == null && SOURCE.has("value") && SOURCE.get("value") != null)
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

    public boolean isSet() {
        return getId() != null && getId() != 0;
    }

    private Set<ActionsEnum> _actionsSet = null;

    public Set<ActionsEnum> getActionsSet() {
        if (_actionsSet == null) {
            _actionsSet = new HashSet<>();
            _actionsSet.addAll(Arrays.asList(getActions()));
        }
        return _actionsSet;
    }
}
