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

public class Cancellation implements Parcelable {
    private static final String TAG = "Cancellation";

    @Json(name = "cancel_reason")
    private Integer _cancelReason;

    @Json(name = "notes")
    private String _notes;

    @Source
    private JsonObject SOURCE = new JsonObject();

    public Cancellation() {
    }

    public void setCancelReason(Integer cancelReason) throws ParseException {
        _cancelReason = cancelReason;
        SOURCE.put("cancel_reason", cancelReason);
    }

    public Integer getCancelReason() {
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
            return Unserializer.unserializeObject(Cancellation.class, obj);
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
}
