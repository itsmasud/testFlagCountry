package com.fieldnation.v2.data.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.fieldnation.fnjson.JsonArray;
import com.fieldnation.fnjson.JsonObject;
import com.fieldnation.fnjson.Serializer;
import com.fieldnation.fnjson.Unserializer;
import com.fieldnation.fnjson.annotations.Json;
import com.fieldnation.fnlog.Log;

/**
 * Created by dmgen from swagger.
 */

public class EtaWithLocation implements Parcelable {
    private static final String TAG = "EtaWithLocation";

    @Json(name = "schedule")
    private Schedule _schedule;

    @Json(name = "work_order_id")
    private Integer _workOrderId;

    @Json(name = "location")
    private Location _location;

    @Json(name = "title")
    private String _title;

    @Json(name = "bundle")
    private Bundle _bundle;

    public EtaWithLocation() {
    }

    public void setSchedule(Schedule schedule) {
        _schedule = schedule;
    }

    public Schedule getSchedule() {
        return _schedule;
    }

    public EtaWithLocation schedule(Schedule schedule) {
        _schedule = schedule;
        return this;
    }

    public void setWorkOrderId(Integer workOrderId) {
        _workOrderId = workOrderId;
    }

    public Integer getWorkOrderId() {
        return _workOrderId;
    }

    public EtaWithLocation workOrderId(Integer workOrderId) {
        _workOrderId = workOrderId;
        return this;
    }

    public void setLocation(Location location) {
        _location = location;
    }

    public Location getLocation() {
        return _location;
    }

    public EtaWithLocation location(Location location) {
        _location = location;
        return this;
    }

    public void setTitle(String title) {
        _title = title;
    }

    public String getTitle() {
        return _title;
    }

    public EtaWithLocation title(String title) {
        _title = title;
        return this;
    }

    public void setBundle(Bundle bundle) {
        _bundle = bundle;
    }

    public Bundle getBundle() {
        return _bundle;
    }

    public EtaWithLocation bundle(Bundle bundle) {
        _bundle = bundle;
        return this;
    }

    /*-*****************************-*/
    /*-             Json            -*/
    /*-*****************************-*/
    public static EtaWithLocation[] fromJsonArray(JsonArray array) {
        EtaWithLocation[] list = new EtaWithLocation[array.size()];
        for (int i = 0; i < array.size(); i++) {
            list[i] = fromJson(array.getJsonObject(i));
        }
        return list;
    }

    public static EtaWithLocation fromJson(JsonObject obj) {
        try {
            return Unserializer.unserializeObject(EtaWithLocation.class, obj);
        } catch (Exception ex) {
            Log.v(TAG, TAG, ex);
            return null;
        }
    }

    public JsonObject toJson() {
        return toJson(this);
    }

    public static JsonObject toJson(EtaWithLocation etaWithLocation) {
        try {
            return Serializer.serializeObject(etaWithLocation);
        } catch (Exception ex) {
            Log.v(TAG, TAG, ex);
            return null;
        }
    }

    /*-*********************************************-*/
    /*-			Parcelable Implementation           -*/
    /*-*********************************************-*/
    public static final Parcelable.Creator<EtaWithLocation> CREATOR = new Parcelable.Creator<EtaWithLocation>() {

        @Override
        public EtaWithLocation createFromParcel(Parcel source) {
            try {
                return EtaWithLocation.fromJson((JsonObject) source.readParcelable(JsonObject.class.getClassLoader()));
            } catch (Exception ex) {
                Log.v(TAG, ex);
                return null;
            }
        }

        @Override
        public EtaWithLocation[] newArray(int size) {
            return new EtaWithLocation[size];
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
