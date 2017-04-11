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

public class Signature implements Parcelable {
    private static final String TAG = "Signature";

    @Json(name = "actions")
    private ActionsEnum[] _actions;

    @Json(name = "closing_notes")
    private String _closingNotes;

    @Json(name = "created")
    private Date _created;

    @Json(name = "data")
    private String _data;

    @Json(name = "format")
    private String _format;

    @Json(name = "hash")
    private String _hash;

    @Json(name = "id")
    private Integer _id;

    @Json(name = "name")
    private String _name;

    @Json(name = "task")
    private Task _task;

    @Json(name = "time_zone")
    private TimeZone _timeZone;

    @Json(name = "worklog")
    private String _worklog;

    @Source
    private JsonObject SOURCE;

    public Signature() {
        SOURCE = new JsonObject();
    }

    public Signature(JsonObject obj) {
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

    public Signature actions(ActionsEnum[] actions) throws ParseException {
        _actions = actions;
        JsonArray ja = new JsonArray();
        for (ActionsEnum item : actions) {
            ja.add(item.toString());
        }
        SOURCE.put("actions", ja, true);
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

    public Signature closingNotes(String closingNotes) throws ParseException {
        _closingNotes = closingNotes;
        SOURCE.put("closing_notes", closingNotes);
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

    public Signature created(Date created) throws ParseException {
        _created = created;
        SOURCE.put("created", created.getJson());
        return this;
    }

    public void setData(String data) throws ParseException {
        _data = data;
        SOURCE.put("data", data);
    }

    public String getData() {
        try {
            if (_data == null && SOURCE.has("data") && SOURCE.get("data") != null)
                _data = SOURCE.getString("data");
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _data;
    }

    public Signature data(String data) throws ParseException {
        _data = data;
        SOURCE.put("data", data);
        return this;
    }

    public void setFormat(String format) throws ParseException {
        _format = format;
        SOURCE.put("format", format);
    }

    public String getFormat() {
        try {
            if (_format == null && SOURCE.has("format") && SOURCE.get("format") != null)
                _format = SOURCE.getString("format");
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _format;
    }

    public Signature format(String format) throws ParseException {
        _format = format;
        SOURCE.put("format", format);
        return this;
    }

    public void setHash(String hash) throws ParseException {
        _hash = hash;
        SOURCE.put("hash", hash);
    }

    public String getHash() {
        try {
            if (_hash == null && SOURCE.has("hash") && SOURCE.get("hash") != null)
                _hash = SOURCE.getString("hash");
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _hash;
    }

    public Signature hash(String hash) throws ParseException {
        _hash = hash;
        SOURCE.put("hash", hash);
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

    public Signature id(Integer id) throws ParseException {
        _id = id;
        SOURCE.put("id", id);
        return this;
    }

    public void setName(String name) throws ParseException {
        _name = name;
        SOURCE.put("name", name);
    }

    public String getName() {
        try {
            if (_name == null && SOURCE.has("name") && SOURCE.get("name") != null)
                _name = SOURCE.getString("name");
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _name;
    }

    public Signature name(String name) throws ParseException {
        _name = name;
        SOURCE.put("name", name);
        return this;
    }

    public void setTask(Task task) throws ParseException {
        _task = task;
        SOURCE.put("task", task.getJson());
    }

    public Task getTask() {
        try {
            if (_task == null && SOURCE.has("task") && SOURCE.get("task") != null)
                _task = Task.fromJson(SOURCE.getJsonObject("task"));
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        if (_task != null && _task.isSet())
            return _task;

        return null;
    }

    public Signature task(Task task) throws ParseException {
        _task = task;
        SOURCE.put("task", task.getJson());
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

    public Signature timeZone(TimeZone timeZone) throws ParseException {
        _timeZone = timeZone;
        SOURCE.put("time_zone", timeZone.getJson());
        return this;
    }

    public void setWorklog(String worklog) throws ParseException {
        _worklog = worklog;
        SOURCE.put("worklog", worklog);
    }

    public String getWorklog() {
        try {
            if (_worklog == null && SOURCE.has("worklog") && SOURCE.get("worklog") != null)
                _worklog = SOURCE.getString("worklog");
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _worklog;
    }

    public Signature worklog(String worklog) throws ParseException {
        _worklog = worklog;
        SOURCE.put("worklog", worklog);
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
    public static JsonArray toJsonArray(Signature[] array) {
        JsonArray list = new JsonArray();
        for (Signature item : array) {
            list.add(item.getJson());
        }
        return list;
    }

    public static Signature[] fromJsonArray(JsonArray array) {
        Signature[] list = new Signature[array.size()];
        for (int i = 0; i < array.size(); i++) {
            list[i] = fromJson(array.getJsonObject(i));
        }
        return list;
    }

    public static Signature fromJson(JsonObject obj) {
        try {
            return new Signature(obj);
        } catch (Exception ex) {
            Log.v(TAG, ex);
            return null;
        }
    }

    public JsonObject getJson() {
        return SOURCE;
    }

    /*-*********************************************-*/
    /*-			Parcelable Implementation           -*/
    /*-*********************************************-*/
    public static final Parcelable.Creator<Signature> CREATOR = new Parcelable.Creator<Signature>() {

        @Override
        public Signature createFromParcel(Parcel source) {
            try {
                return Signature.fromJson((JsonObject) source.readParcelable(JsonObject.class.getClassLoader()));
            } catch (Exception ex) {
                Log.v(TAG, ex);
                return null;
            }
        }

        @Override
        public Signature[] newArray(int size) {
            return new Signature[size];
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
