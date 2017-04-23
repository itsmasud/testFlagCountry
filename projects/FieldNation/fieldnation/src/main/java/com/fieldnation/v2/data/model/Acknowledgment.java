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

public class Acknowledgment implements Parcelable {
    private static final String TAG = "Acknowledgment";

    @Json(name = "author")
    private User _author;

    @Json(name = "created")
    private Date _created;

    @Json(name = "status")
    private StatusEnum _status;

    @Source
    private JsonObject SOURCE;

    public Acknowledgment() {
        SOURCE = new JsonObject();
    }

    public Acknowledgment(JsonObject obj) {
        SOURCE = obj;
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

    public Acknowledgment author(User author) throws ParseException {
        _author = author;
        SOURCE.put("author", author.getJson());
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

    public Acknowledgment created(Date created) throws ParseException {
        _created = created;
        SOURCE.put("created", created.getJson());
        return this;
    }

    public void setStatus(StatusEnum status) throws ParseException {
        _status = status;
        SOURCE.put("status", status.toString());
    }

    public StatusEnum getStatus() {
        try {
            if (_status == null && SOURCE.has("status") && SOURCE.get("status") != null)
                _status = StatusEnum.fromString(SOURCE.getString("status"));
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _status;
    }

    public Acknowledgment status(StatusEnum status) throws ParseException {
        _status = status;
        SOURCE.put("status", status.toString());
        return this;
    }

    /*-******************************-*/
    /*-             Enums            -*/
    /*-******************************-*/
    public enum StatusEnum {
        @Json(name = "acknowledged")
        ACKNOWLEDGED("acknowledged"),
        @Json(name = "pending")
        PENDING("pending");

        private String value;

        StatusEnum(String value) {
            this.value = value;
        }

        public static StatusEnum fromString(String value) {
            StatusEnum[] values = values();
            for (StatusEnum v : values) {
                if (v.value.equals(value))
                    return v;
            }
            return null;
        }

        public static StatusEnum[] fromJsonArray(JsonArray jsonArray) {
            StatusEnum[] list = new StatusEnum[jsonArray.size()];
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
    public static JsonArray toJsonArray(Acknowledgment[] array) {
        JsonArray list = new JsonArray();
        for (Acknowledgment item : array) {
            list.add(item.getJson());
        }
        return list;
    }

    public static Acknowledgment[] fromJsonArray(JsonArray array) {
        Acknowledgment[] list = new Acknowledgment[array.size()];
        for (int i = 0; i < array.size(); i++) {
            list[i] = fromJson(array.getJsonObject(i));
        }
        return list;
    }

    public static Acknowledgment fromJson(JsonObject obj) {
        try {
            return new Acknowledgment(obj);
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
    public static final Parcelable.Creator<Acknowledgment> CREATOR = new Parcelable.Creator<Acknowledgment>() {

        @Override
        public Acknowledgment createFromParcel(Parcel source) {
            try {
                return Acknowledgment.fromJson((JsonObject) source.readParcelable(JsonObject.class.getClassLoader()));
            } catch (Exception ex) {
                Log.v(TAG, ex);
                return null;
            }
        }

        @Override
        public Acknowledgment[] newArray(int size) {
            return new Acknowledgment[size];
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
        return true;
    }
}
