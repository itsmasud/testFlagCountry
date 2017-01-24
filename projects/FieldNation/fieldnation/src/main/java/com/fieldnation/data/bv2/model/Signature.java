package com.fieldnation.data.bv2.model;

import com.fieldnation.fnjson.JsonObject;
import com.fieldnation.fnjson.Serializer;
import com.fieldnation.fnjson.Unserializer;
import com.fieldnation.fnjson.annotations.Json;
import com.fieldnation.fnlog.Log;

public class Signature {
    private static final String TAG = "Signature";

    @Json(name = "closing_notes")
    private String closingNotes;

    @Json(name = "task")
    private Task task;

    @Json(name = "data")
    private String data;

    @Json(name = "created")
    private Date created;

    @Json(name = "name")
    private String name;

    @Json(name = "format")
    private String format;

    @Json(name = "id")
    private Integer id;

    @Json(name = "worklog")
    private String worklog;

    @Json(name = "time_zone")
    private TimeZone timeZone;

    @Json(name = "actions")
    private String[] actions;

    @Json(name = "hash")
    private String hash;

    public Signature() {
    }

    public String getClosingNotes() {
        return closingNotes;
    }

    public Task getTask() {
        return task;
    }

    public String getData() {
        return data;
    }

    public Date getCreated() {
        return created;
    }

    public String getName() {
        return name;
    }

    public String getFormat() {
        return format;
    }

    public Integer getId() {
        return id;
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

    public String getHash() {
        return hash;
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
