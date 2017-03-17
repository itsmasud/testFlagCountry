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

public class Cancellation implements Parcelable {
    private static final String TAG = "Cancellation";

    @Json(name = "cancel_reason")
    private Integer _cancelReason;

    @Json(name = "notes")
    private String _notes;

    @Source
    private JsonObject SOURCE;

    public Cancellation() {
        SOURCE = new JsonObject();
    }

    public Cancellation(JsonObject obj) {
        SOURCE = obj;
    }

    public void setCancelReason(Integer cancelReason) throws ParseException {
        _cancelReason = cancelReason;
        SOURCE.put("cancel_reason", cancelReason);
    }

    public Integer getCancelReason() {
        try {
            if (_cancelReason == null && SOURCE.has("cancel_reason") && SOURCE.get("cancel_reason") != null)
                _cancelReason = SOURCE.getInt("cancel_reason");
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _cancelReason;
    }

    public Cancellation cancelReason(Integer cancelReason) throws ParseException {
        _cancelReason = cancelReason;
        SOURCE.put("cancel_reason", cancelReason);
        return this;
    }

    public void setNotes(String notes) throws ParseException {
        _notes = notes;
        SOURCE.put("notes", notes);
    }

    public String getNotes() {
        try {
            if (_notes == null && SOURCE.has("notes") && SOURCE.get("notes") != null)
                _notes = SOURCE.getString("notes");
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _notes;
    }

    public Cancellation notes(String notes) throws ParseException {
        _notes = notes;
        SOURCE.put("notes", notes);
        return this;
    }

    /*-*****************************-*/
    /*-             Json            -*/
    /*-*****************************-*/
    public static JsonArray toJsonArray(Cancellation[] array) {
        JsonArray list = new JsonArray();
        for (Cancellation item : array) {
            list.add(item.getJson());
        }
        return list;
    }

    public static Cancellation[] fromJsonArray(JsonArray array) {
        Cancellation[] list = new Cancellation[array.size()];
        for (int i = 0; i < array.size(); i++) {
            list[i] = fromJson(array.getJsonObject(i));
        }
        return list;
    }

    public static Cancellation fromJson(JsonObject obj) {
        try {
            return new Cancellation(obj);
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
    public static final Parcelable.Creator<Cancellation> CREATOR = new Parcelable.Creator<Cancellation>() {

        @Override
        public Cancellation createFromParcel(Parcel source) {
            try {
                return Cancellation.fromJson((JsonObject) source.readParcelable(JsonObject.class.getClassLoader()));
            } catch (Exception ex) {
                Log.v(TAG, ex);
                return null;
            }
        }

        @Override
        public Cancellation[] newArray(int size) {
            return new Cancellation[size];
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
