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

public class EtaWithLocation implements Parcelable {
    private static final String TAG = "EtaWithLocation";

    @Json(name = "bundle")
    private Bundle _bundle;

    @Json(name = "location")
    private Location _location;

    @Json(name = "schedule")
    private Schedule _schedule;

    @Json(name = "title")
    private String _title;

    @Json(name = "work_order_id")
    private Integer _workOrderId;

    @Source
    private JsonObject SOURCE = new JsonObject();

    public EtaWithLocation() {
    }

    public void setBundle(Bundle bundle) throws ParseException {
        _bundle = bundle;
        SOURCE.put("bundle", bundle.getJson());
    }

    public Bundle getBundle() {
        return _bundle;
    }

    public EtaWithLocation bundle(Bundle bundle) throws ParseException {
        _bundle = bundle;
        SOURCE.put("bundle", bundle.getJson());
        return this;
    }

    public void setLocation(Location location) throws ParseException {
        _location = location;
        SOURCE.put("location", location.getJson());
    }

    public Location getLocation() {
        return _location;
    }

    public EtaWithLocation location(Location location) throws ParseException {
        _location = location;
        SOURCE.put("location", location.getJson());
        return this;
    }

    public void setSchedule(Schedule schedule) throws ParseException {
        _schedule = schedule;
        SOURCE.put("schedule", schedule.getJson());
    }

    public Schedule getSchedule() {
        return _schedule;
    }

    public EtaWithLocation schedule(Schedule schedule) throws ParseException {
        _schedule = schedule;
        SOURCE.put("schedule", schedule.getJson());
        return this;
    }

    public void setTitle(String title) throws ParseException {
        _title = title;
        SOURCE.put("title", title);
    }

    public String getTitle() {
        return _title;
    }

    public EtaWithLocation title(String title) throws ParseException {
        _title = title;
        SOURCE.put("title", title);
        return this;
    }

    public void setWorkOrderId(Integer workOrderId) throws ParseException {
        _workOrderId = workOrderId;
        SOURCE.put("work_order_id", workOrderId);
    }

    public Integer getWorkOrderId() {
        return _workOrderId;
    }

    public EtaWithLocation workOrderId(Integer workOrderId) throws ParseException {
        _workOrderId = workOrderId;
        SOURCE.put("work_order_id", workOrderId);
        return this;
    }

    /*-*****************************-*/
    /*-             Json            -*/
    /*-*****************************-*/
    public static JsonArray toJsonArray(EtaWithLocation[] array) {
        JsonArray list = new JsonArray();
        for (EtaWithLocation item : array) {
            list.add(item.getJson());
        }
        return list;
    }

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

    public JsonObject getJson() {
        return SOURCE;
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
        dest.writeParcelable(getJson(), flags);
    }
}
