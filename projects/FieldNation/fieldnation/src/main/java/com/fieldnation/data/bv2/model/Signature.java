package com.fieldnation.data.bv2.model;

import com.fieldnation.fnjson.JsonObject;
import com.fieldnation.fnjson.Serializer;
import com.fieldnation.fnjson.Unserializer;
import com.fieldnation.fnjson.annotations.Json;
import com.fieldnation.fnlog.Log;

public class Signature {
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

    public String getClosingNotes() {
        return _closingNotes;
    }

    public Task getTask() {
        return _task;
    }

    public String getData() {
        return _data;
    }

    public Date getCreated() {
        return _created;
    }

    public String getName() {
        return _name;
    }

    public String getFormat() {
        return _format;
    }

    public Integer getId() {
        return _id;
    }

    public String getWorklog() {
        return _worklog;
    }

    public TimeZone getTimeZone() {
        return _timeZone;
    }

    public String[] getActions() {
        return _actions;
    }

    public String getHash() {
        return _hash;
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
}
