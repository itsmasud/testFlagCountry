package com.fieldnation.data.bv2.model;
import android.os.Parcel;
import android.os.Parcelable;

import com.fieldnation.fnjson.JsonObject;
import com.fieldnation.fnjson.Serializer;
import com.fieldnation.fnjson.Unserializer;
import com.fieldnation.fnjson.annotations.Json;
import com.fieldnation.fnlog.Log;

/**
 * Created by dmgen from swagger on 1/26/17.
 */

public class Signature implements Parcelable {
    private static final String TAG = "Signature";

    @Json(name = "closing_notes")
    private String _closingNotes;

    @Json(name = "task")
    private Task _task;

    @Json(name = "data")
    private String _data;

    @Json(name = "created")
    private Date _created;

    @Json(name = "name")
    private String _name;

    @Json(name = "format")
    private String _format;

    @Json(name = "id")
    private Integer _id;

    @Json(name = "worklog")
    private String _worklog;

    @Json(name = "time_zone")
    private TimeZone _timeZone;

    @Json(name = "actions")
    private String[] _actions;

    @Json(name = "hash")
    private String _hash;

    public Signature() {
    }

    public void setClosingNotes(String closingNotes) {
        _closingNotes = closingNotes;
    }

    public String getClosingNotes() {
        return _closingNotes;
    }

    public Signature closingNotes(String closingNotes) {
        _closingNotes = closingNotes;
        return this;
    }

    public void setTask(Task task) {
        _task = task;
    }

    public Task getTask() {
        return _task;
    }

    public Signature task(Task task) {
        _task = task;
        return this;
    }

    public void setData(String data) {
        _data = data;
    }

    public String getData() {
        return _data;
    }

    public Signature data(String data) {
        _data = data;
        return this;
    }

    public void setCreated(Date created) {
        _created = created;
    }

    public Date getCreated() {
        return _created;
    }

    public Signature created(Date created) {
        _created = created;
        return this;
    }

    public void setName(String name) {
        _name = name;
    }

    public String getName() {
        return _name;
    }

    public Signature name(String name) {
        _name = name;
        return this;
    }

    public void setFormat(String format) {
        _format = format;
    }

    public String getFormat() {
        return _format;
    }

    public Signature format(String format) {
        _format = format;
        return this;
    }

    public void setId(Integer id) {
        _id = id;
    }

    public Integer getId() {
        return _id;
    }

    public Signature id(Integer id) {
        _id = id;
        return this;
    }

    public void setWorklog(String worklog) {
        _worklog = worklog;
    }

    public String getWorklog() {
        return _worklog;
    }

    public Signature worklog(String worklog) {
        _worklog = worklog;
        return this;
    }

    public void setTimeZone(TimeZone timeZone) {
        _timeZone = timeZone;
    }

    public TimeZone getTimeZone() {
        return _timeZone;
    }

    public Signature timeZone(TimeZone timeZone) {
        _timeZone = timeZone;
        return this;
    }

    public void setActions(String[] actions) {
        _actions = actions;
    }

    public String[] getActions() {
        return _actions;
    }

    public Signature actions(String[] actions) {
        _actions = actions;
        return this;
    }

    public void setHash(String hash) {
        _hash = hash;
    }

    public String getHash() {
        return _hash;
    }

    public Signature hash(String hash) {
        _hash = hash;
        return this;
    }

    /*-*****************************-*/
    /*-             Json            -*/
    /*-*****************************-*/
    public static Signature fromJson(JsonObject obj) {
        try {
            return Unserializer.unserializeObject(Signature.class, obj);
        } catch (Exception ex) {
            Log.v(TAG, TAG, ex);
            return null;
        }
    }

    public JsonObject toJson() {
        return toJson(this);
    }

    public static JsonObject toJson(Signature signature) {
        try {
            return Serializer.serializeObject(signature);
        } catch (Exception ex) {
            Log.v(TAG, TAG, ex);
            return null;
        }
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
        dest.writeParcelable(toJson(), flags);
    }
}
