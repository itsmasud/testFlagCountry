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

public class UpdateScheduleOptions implements Parcelable {
    private static final String TAG = "UpdateScheduleOptions";

    @Json(name = "confirm")
    private Boolean _confirm;

    @Json(name = "updateFromIvr")
    private Boolean _updateFromIvr;

    @Json(name = "async")
    private Boolean _async;

    public UpdateScheduleOptions() {
    }

    public void setConfirm(Boolean confirm) {
        _confirm = confirm;
    }

    public Boolean getConfirm() {
        return _confirm;
    }

    public UpdateScheduleOptions confirm(Boolean confirm) {
        _confirm = confirm;
        return this;
    }

    public void setUpdateFromIvr(Boolean updateFromIvr) {
        _updateFromIvr = updateFromIvr;
    }

    public Boolean getUpdateFromIvr() {
        return _updateFromIvr;
    }

    public UpdateScheduleOptions updateFromIvr(Boolean updateFromIvr) {
        _updateFromIvr = updateFromIvr;
        return this;
    }

    public void setAsync(Boolean async) {
        _async = async;
    }

    public Boolean getAsync() {
        return _async;
    }

    public UpdateScheduleOptions async(Boolean async) {
        _async = async;
        return this;
    }

    /*-*****************************-*/
    /*-             Json            -*/
    /*-*****************************-*/
    public static UpdateScheduleOptions fromJson(JsonObject obj) {
        try {
            return Unserializer.unserializeObject(UpdateScheduleOptions.class, obj);
        } catch (Exception ex) {
            Log.v(TAG, TAG, ex);
            return null;
        }
    }

    public JsonObject toJson() {
        return toJson(this);
    }

    public static JsonObject toJson(UpdateScheduleOptions updateScheduleOptions) {
        try {
            return Serializer.serializeObject(updateScheduleOptions);
        } catch (Exception ex) {
            Log.v(TAG, TAG, ex);
            return null;
        }
    }

    /*-*********************************************-*/
    /*-			Parcelable Implementation           -*/
    /*-*********************************************-*/
    public static final Parcelable.Creator<UpdateScheduleOptions> CREATOR = new Parcelable.Creator<UpdateScheduleOptions>() {

        @Override
        public UpdateScheduleOptions createFromParcel(Parcel source) {
            try {
                return UpdateScheduleOptions.fromJson((JsonObject) source.readParcelable(JsonObject.class.getClassLoader()));
            } catch (Exception ex) {
                Log.v(TAG, ex);
                return null;
            }
        }

        @Override
        public UpdateScheduleOptions[] newArray(int size) {
            return new UpdateScheduleOptions[size];
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
