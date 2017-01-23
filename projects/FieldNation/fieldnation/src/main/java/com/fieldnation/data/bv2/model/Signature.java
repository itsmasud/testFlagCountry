package com.fieldnation.data.bv2.model;

import com.fieldnation.fnjson.JsonObject;
import com.fieldnation.fnjson.Serializer;
import com.fieldnation.fnjson.Unserializer;
import com.fieldnation.fnjson.annotations.Json;
import com.fieldnation.fnlog.Log;

public class Signature {
    private static final String TAG = "Signature";

    @Json(name = "name")
    private String name = null;

    @Json(name = "format")
    private String format = null;

    @Json(name = "closing_notes")
    private String closingNotes = null;

    @Json(name = "data")
    private String data = null;

    @Json(name = "id")
    private Integer id = null;

    @Json(name = "hash")
    private String hash = null;

    @Json(name = "created")
    private String created = null;

    @Json(name = "worklog")
    private String worklog = null;

    @Json(name = "time_zone")
    private TimeZone timeZone = null;

    @Json(name = "actions")
    private String[] actions;

    @Json(name = "task")
    private Task task = null;

    public Signature() {
    }

    public String getName() {
        return name;
    }

    public String getFormat() {
        return format;
    }

    public String getClosingNotes() {
        return closingNotes;
    }

    public String getData() {
        return data;
    }

    public Integer getId() {
        return id;
    }

    public String getHash() {
        return hash;
    }

    public String getCreated() {
        return created;
    }

    public String getWorklog() {
        return worklog;
    }

    public TimeZone getTimeZone() {
        return timeZone;
    }

    public String[] getActions() {
        return actions;
    }

    public Task getTask() {
        return task;
    }

    /*-*****************************-*/
    /*-             Json            -*/
    /*-*****************************-*/
    public static Signature fromJson(JsonObject obj) {
        try {
            return Unserializer.unserializeObject(Signature.class, obj);
        } catch (Exception ex) {
            Log.v(TAG, ex);
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
            Log.v(TAG, ex);
            return null;
        }
    }
}