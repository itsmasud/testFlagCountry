package com.fieldnation.data.bv2.model;
import android.os.Parcel;
import android.os.Parcelable;

import com.fieldnation.fnjson.JsonArray;
import com.fieldnation.fnjson.JsonObject;
import com.fieldnation.fnjson.Serializer;
import com.fieldnation.fnjson.Unserializer;
import com.fieldnation.fnjson.annotations.Json;
import com.fieldnation.fnlog.Log;

/**
 * Created by dmgen from swagger on 1/31/17.
 */

public class ScheduleEtaStatus implements Parcelable {
    private static final String TAG = "ScheduleEtaStatus";

    @Json(name = "name")
    private NameEnum _name;

    @Json(name = "updated")
    private Date _updated;

    public ScheduleEtaStatus() {
    }

    public void setName(NameEnum name) {
        _name = name;
    }

    public NameEnum getName() {
        return _name;
    }

    public ScheduleEtaStatus name(NameEnum name) {
        _name = name;
        return this;
    }

    public void setUpdated(Date updated) {
        _updated = updated;
    }

    public Date getUpdated() {
        return _updated;
    }

    public ScheduleEtaStatus updated(Date updated) {
        _updated = updated;
        return this;
    }

    /*-*****************************-*/
    /*-             Json            -*/
    /*-*****************************-*/
    public static ScheduleEtaStatus[] fromJsonArray(JsonArray array) {
        ScheduleEtaStatus[] list = new ScheduleEtaStatus[array.size()];
        for (int i = 0; i < array.size(); i++) {
            list[i] = fromJson(array.getJsonObject(i));
        }
        return list;
    }

    public static ScheduleEtaStatus fromJson(JsonObject obj) {
        try {
            return Unserializer.unserializeObject(ScheduleEtaStatus.class, obj);
        } catch (Exception ex) {
            Log.v(TAG, TAG, ex);
            return null;
        }
    }

    public JsonObject toJson() {
        return toJson(this);
    }

    public static JsonObject toJson(ScheduleEtaStatus scheduleEtaStatus) {
        try {
            return Serializer.serializeObject(scheduleEtaStatus);
        } catch (Exception ex) {
            Log.v(TAG, TAG, ex);
            return null;
        }
    }

    /*-*********************************************-*/
    /*-			Parcelable Implementation           -*/
    /*-*********************************************-*/
    public static final Parcelable.Creator<ScheduleEtaStatus> CREATOR = new Parcelable.Creator<ScheduleEtaStatus>() {

        @Override
        public ScheduleEtaStatus createFromParcel(Parcel source) {
            try {
                return ScheduleEtaStatus.fromJson((JsonObject) source.readParcelable(JsonObject.class.getClassLoader()));
            } catch (Exception ex) {
                Log.v(TAG, ex);
                return null;
            }
        }

        @Override
        public ScheduleEtaStatus[] newArray(int size) {
            return new ScheduleEtaStatus[size];
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
