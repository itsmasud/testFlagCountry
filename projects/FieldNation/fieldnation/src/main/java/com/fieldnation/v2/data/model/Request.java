package com.fieldnation.v2.data.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.fieldnation.fnjson.JsonArray;
import com.fieldnation.fnjson.JsonObject;
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

public class Request implements Parcelable {
    private static final String TAG = "Request";

    @Json(name = "actions")
    private ActionsEnum[] _actions;

    @Json(name = "active")
    private Boolean _active;

    @Json(name = "counter")
    private Boolean _counter;

    @Json(name = "counter_notes")
    private String _counterNotes;

    @Json(name = "created")
    private Date _created;

    @Json(name = "expenses")
    private Expense[] _expenses;

    @Json(name = "expires")
    private Date _expires;

    @Json(name = "hour_estimate")
    private Double _hourEstimate;

    @Json(name = "id")
    private Integer _id;

    @Json(name = "notes")
    private String _notes;

    @Json(name = "pay")
    private Pay _pay;

    @Json(name = "schedule")
    private Schedule _schedule;

    @Json(name = "user")
    private User _user;

    @Json(name = "work_order")
    private WorkOrder _workOrder;

    @Source
    private JsonObject SOURCE;

    public Request() {
        SOURCE = new JsonObject();
    }

    public Request(JsonObject obj) {
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

    public Request actions(ActionsEnum[] actions) throws ParseException {
        _actions = actions;
        JsonArray ja = new JsonArray();
        for (ActionsEnum item : actions) {
            ja.add(item.toString());
        }
        SOURCE.put("actions", ja, true);
        return this;
    }

    public void setActive(Boolean active) throws ParseException {
        _active = active;
        SOURCE.put("active", active);
    }

    public Boolean getActive() {
        try {
            if (_active != null)
                return _active;

            if (SOURCE.has("active") && SOURCE.get("active") != null)
                _active = SOURCE.getBoolean("active");

        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _active;
    }

    public Request active(Boolean active) throws ParseException {
        _active = active;
        SOURCE.put("active", active);
        return this;
    }

    public void setCounter(Boolean counter) throws ParseException {
        _counter = counter;
        SOURCE.put("counter", counter);
    }

    public Boolean getCounter() {
        try {
            if (_counter != null)
                return _counter;

            if (SOURCE.has("counter") && SOURCE.get("counter") != null)
                _counter = SOURCE.getBoolean("counter");

        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _counter;
    }

    public Request counter(Boolean counter) throws ParseException {
        _counter = counter;
        SOURCE.put("counter", counter);
        return this;
    }

    public void setCounterNotes(String counterNotes) throws ParseException {
        _counterNotes = counterNotes;
        SOURCE.put("counter_notes", counterNotes);
    }

    public String getCounterNotes() {
        try {
            if (_counterNotes != null)
                return _counterNotes;

            if (SOURCE.has("counter_notes") && SOURCE.get("counter_notes") != null)
                _counterNotes = SOURCE.getString("counter_notes");

        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _counterNotes;
    }

    public Request counterNotes(String counterNotes) throws ParseException {
        _counterNotes = counterNotes;
        SOURCE.put("counter_notes", counterNotes);
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

    public Request created(Date created) throws ParseException {
        _created = created;
        SOURCE.put("created", created.getJson());
        return this;
    }

    public void setExpenses(Expense[] expenses) throws ParseException {
        _expenses = expenses;
        SOURCE.put("expenses", Expense.toJsonArray(expenses));
    }

    public Expense[] getExpenses() {
        try {
            if (_expenses != null)
                return _expenses;

            if (SOURCE.has("expenses") && SOURCE.get("expenses") != null) {
                _expenses = Expense.fromJsonArray(SOURCE.getJsonArray("expenses"));
            }

        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _expenses;
    }

    public Request expenses(Expense[] expenses) throws ParseException {
        _expenses = expenses;
        SOURCE.put("expenses", Expense.toJsonArray(expenses), true);
        return this;
    }

    public void setExpires(Date expires) throws ParseException {
        _expires = expires;
        SOURCE.put("expires", expires.getJson());
    }

    public Date getExpires() {
        try {
            if (_expires != null)
                return _expires;

            if (SOURCE.has("expires") && SOURCE.get("expires") != null)
                _expires = Date.fromJson(SOURCE.getJsonObject("expires"));

        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _expires;
    }

    public Request expires(Date expires) throws ParseException {
        _expires = expires;
        SOURCE.put("expires", expires.getJson());
        return this;
    }

    public void setHourEstimate(Double hourEstimate) throws ParseException {
        _hourEstimate = hourEstimate;
        SOURCE.put("hour_estimate", hourEstimate);
    }

    public Double getHourEstimate() {
        try {
            if (_hourEstimate != null)
                return _hourEstimate;

            if (SOURCE.has("hour_estimate") && SOURCE.get("hour_estimate") != null)
                _hourEstimate = SOURCE.getDouble("hour_estimate");

        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _hourEstimate;
    }

    public Request hourEstimate(Double hourEstimate) throws ParseException {
        _hourEstimate = hourEstimate;
        SOURCE.put("hour_estimate", hourEstimate);
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

    public Request id(Integer id) throws ParseException {
        _id = id;
        SOURCE.put("id", id);
        return this;
    }

    public void setNotes(String notes) throws ParseException {
        _notes = notes;
        SOURCE.put("notes", notes);
    }

    public String getNotes() {
        try {
            if (_notes != null)
                return _notes;

            if (SOURCE.has("notes") && SOURCE.get("notes") != null)
                _notes = SOURCE.getString("notes");

        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _notes;
    }

    public Request notes(String notes) throws ParseException {
        _notes = notes;
        SOURCE.put("notes", notes);
        return this;
    }

    public void setPay(Pay pay) throws ParseException {
        _pay = pay;
        SOURCE.put("pay", pay.getJson());
    }

    public Pay getPay() {
        try {
            if (_pay != null)
                return _pay;

            if (SOURCE.has("pay") && SOURCE.get("pay") != null)
                _pay = Pay.fromJson(SOURCE.getJsonObject("pay"));

        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _pay;
    }

    public Request pay(Pay pay) throws ParseException {
        _pay = pay;
        SOURCE.put("pay", pay.getJson());
        return this;
    }

    public void setSchedule(Schedule schedule) throws ParseException {
        _schedule = schedule;
        SOURCE.put("schedule", schedule.getJson());
    }

    public Schedule getSchedule() {
        try {
            if (_schedule != null)
                return _schedule;

            if (SOURCE.has("schedule") && SOURCE.get("schedule") != null)
                _schedule = Schedule.fromJson(SOURCE.getJsonObject("schedule"));

        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _schedule;
    }

    public Request schedule(Schedule schedule) throws ParseException {
        _schedule = schedule;
        SOURCE.put("schedule", schedule.getJson());
        return this;
    }

    public void setUser(User user) throws ParseException {
        _user = user;
        SOURCE.put("user", user.getJson());
    }

    public User getUser() {
        try {
            if (_user != null)
                return _user;

            if (SOURCE.has("user") && SOURCE.get("user") != null)
                _user = User.fromJson(SOURCE.getJsonObject("user"));

        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _user;
    }

    public Request user(User user) throws ParseException {
        _user = user;
        SOURCE.put("user", user.getJson());
        return this;
    }

    public void setWorkOrder(WorkOrder workOrder) throws ParseException {
        _workOrder = workOrder;
        SOURCE.put("work_order", workOrder.getJson());
    }

    public WorkOrder getWorkOrder() {
        try {
            if (_workOrder != null)
                return _workOrder;

            if (SOURCE.has("work_order") && SOURCE.get("work_order") != null)
                _workOrder = WorkOrder.fromJson(SOURCE.getJsonObject("work_order"));

        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _workOrder;
    }

    public Request workOrder(WorkOrder workOrder) throws ParseException {
        _workOrder = workOrder;
        SOURCE.put("work_order", workOrder.getJson());
        return this;
    }

    /*-******************************-*/
    /*-             Enums            -*/
    /*-******************************-*/
    public enum ActionsEnum {
        @Json(name = "remove")
        REMOVE("remove");

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
    public static JsonArray toJsonArray(Request[] array) {
        JsonArray list = new JsonArray();
        for (Request item : array) {
            list.add(item.getJson());
        }
        return list;
    }

    public static Request[] fromJsonArray(JsonArray array) {
        Request[] list = new Request[array.size()];
        for (int i = 0; i < array.size(); i++) {
            list[i] = fromJson(array.getJsonObject(i));
        }
        return list;
    }

    public static Request fromJson(JsonObject obj) {
        try {
            return new Request(obj);
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
    public static final Parcelable.Creator<Request> CREATOR = new Parcelable.Creator<Request>() {

        @Override
        public Request createFromParcel(Parcel source) {
            try {
                return Request.fromJson((JsonObject) source.readParcelable(JsonObject.class.getClassLoader()));
            } catch (Exception ex) {
                Log.v(TAG, ex);
                return null;
            }
        }

        @Override
        public Request[] newArray(int size) {
            return new Request[size];
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

    private Set<ActionsEnum> _actionsSet = null;

    public Set<ActionsEnum> getActionsSet() {
        if (_actionsSet == null) {
            _actionsSet = new HashSet<>();
            _actionsSet.addAll(Arrays.asList(getActions()));
        }

        return _actionsSet;
    }
}
