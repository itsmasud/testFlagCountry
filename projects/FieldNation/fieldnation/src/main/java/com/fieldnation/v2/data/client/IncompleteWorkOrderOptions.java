package com.fieldnation.v2.data.client;

import android.os.Parcel;
import android.os.Parcelable;

import com.fieldnation.v2.data.model.*;
import com.fieldnation.fnjson.JsonArray;
import com.fieldnation.fnjson.JsonObject;
import com.fieldnation.fnjson.Serializer;
import com.fieldnation.fnjson.Unserializer;
import com.fieldnation.fnjson.annotations.Json;
import com.fieldnation.fnlog.Log;

/**
 * Created by dmgen from swagger.
 */

public class IncompleteWorkOrderOptions implements Parcelable {
    private static final String TAG = "IncompleteWorkOrderOptions";

    @Json(name = "reason")
    private String _reason;

    @Json(name = "async")
    private Boolean _async;

    public IncompleteWorkOrderOptions() {
    }

    public void setReason(String reason) {
        _reason = reason;
    }

    public String getReason() {
        return _reason;
    }

    public IncompleteWorkOrderOptions reason(String reason) {
        _reason = reason;
        return this;
    }

    public void setAsync(Boolean async) {
        _async = async;
    }

    public Boolean getAsync() {
        return _async;
    }

    public IncompleteWorkOrderOptions async(Boolean async) {
        _async = async;
        return this;
    }

    /*-*****************************-*/
    /*-             Json            -*/
    /*-*****************************-*/
    public static IncompleteWorkOrderOptions fromJson(JsonObject obj) {
        try {
            return Unserializer.unserializeObject(IncompleteWorkOrderOptions.class, obj);
        } catch (Exception ex) {
            Log.v(TAG, TAG, ex);
            return null;
        }
    }

    public JsonObject toJson() {
        return toJson(this);
    }

    public static JsonObject toJson(IncompleteWorkOrderOptions incompleteWorkOrderOptions) {
        try {
            return Serializer.serializeObject(incompleteWorkOrderOptions);
        } catch (Exception ex) {
            Log.v(TAG, TAG, ex);
            return null;
        }
    }

    /*-*********************************************-*/
    /*-			Parcelable Implementation           -*/
    /*-*********************************************-*/
    public static final Parcelable.Creator<IncompleteWorkOrderOptions> CREATOR = new Parcelable.Creator<IncompleteWorkOrderOptions>() {

        @Override
        public IncompleteWorkOrderOptions createFromParcel(Parcel source) {
            try {
                return IncompleteWorkOrderOptions.fromJson((JsonObject) source.readParcelable(JsonObject.class.getClassLoader()));
            } catch (Exception ex) {
                Log.v(TAG, ex);
                return null;
            }
        }

        @Override
        public IncompleteWorkOrderOptions[] newArray(int size) {
            return new IncompleteWorkOrderOptions[size];
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
