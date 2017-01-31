package com.fieldnation.data.bv2.model;
import android.os.Parcel;
import android.os.Parcelable;

import com.fieldnation.fnjson.JsonObject;
import com.fieldnation.fnjson.Serializer;
import com.fieldnation.fnjson.Unserializer;
import com.fieldnation.fnjson.annotations.Json;
import com.fieldnation.fnlog.Log;

/**
 * Created by dmgen from swagger on 1/30/17.
 */

public class Cancellation implements Parcelable {
    private static final String TAG = "Cancellation";

    @Json(name = "cancel_reason")
    private Integer _cancelReason;

    @Json(name = "notes")
    private String _notes;

    public Cancellation() {
    }

    public void setCancelReason(Integer cancelReason) {
        _cancelReason = cancelReason;
    }

    public Integer getCancelReason() {
        return _cancelReason;
    }

    public Cancellation cancelReason(Integer cancelReason) {
        _cancelReason = cancelReason;
        return this;
    }

    public void setNotes(String notes) {
        _notes = notes;
    }

    public String getNotes() {
        return _notes;
    }

    public Cancellation notes(String notes) {
        _notes = notes;
        return this;
    }

    /*-*****************************-*/
    /*-             Json            -*/
    /*-*****************************-*/
    public static Cancellation fromJson(JsonObject obj) {
        try {
            return Unserializer.unserializeObject(Cancellation.class, obj);
        } catch (Exception ex) {
            Log.v(TAG, TAG, ex);
            return null;
        }
    }

    public JsonObject toJson() {
        return toJson(this);
    }

    public static JsonObject toJson(Cancellation cancellation) {
        try {
            return Serializer.serializeObject(cancellation);
        } catch (Exception ex) {
            Log.v(TAG, TAG, ex);
            return null;
        }
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
        dest.writeParcelable(toJson(), flags);
    }
}
