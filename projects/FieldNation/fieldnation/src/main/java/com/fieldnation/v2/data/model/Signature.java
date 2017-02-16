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
    private JsonObject SOURCE = new JsonObject();

    public Signature() {
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
        return _created;
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
        return _task;
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
        return _timeZone;
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
            return Unserializer.unserializeObject(Signature.class, obj);
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
}
