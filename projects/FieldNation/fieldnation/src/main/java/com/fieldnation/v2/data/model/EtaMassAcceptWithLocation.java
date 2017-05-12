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

public class EtaMassAcceptWithLocation implements Parcelable {
    private static final String TAG = "EtaMassAcceptWithLocation";

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
    private JsonObject SOURCE;

    public EtaMassAcceptWithLocation() {
        SOURCE = new JsonObject();
    }

    public EtaMassAcceptWithLocation(JsonObject obj) {
        SOURCE = obj;
    }

    public void setBundle(Bundle bundle) throws ParseException {
        _bundle = bundle;
        SOURCE.put("bundle", bundle.getJson());
    }

    public Bundle getBundle() {
        try {
            if (_bundle == null && SOURCE.has("bundle") && SOURCE.get("bundle") != null)
                _bundle = Bundle.fromJson(SOURCE.getJsonObject("bundle"));
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        if (_bundle != null && _bundle.isSet())
            return _bundle;

        return null;
    }

    public EtaMassAcceptWithLocation bundle(Bundle bundle) throws ParseException {
        _bundle = bundle;
        SOURCE.put("bundle", bundle.getJson());
        return this;
    }

    public void setLocation(Location location) throws ParseException {
        _location = location;
        SOURCE.put("location", location.getJson());
    }

    public Location getLocation() {
        try {
            if (_location == null && SOURCE.has("location") && SOURCE.get("location") != null)
                _location = Location.fromJson(SOURCE.getJsonObject("location"));
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        if (_location != null && _location.isSet())
            return _location;

        return null;
    }

    public EtaMassAcceptWithLocation location(Location location) throws ParseException {
        _location = location;
        SOURCE.put("location", location.getJson());
        return this;
    }

    public void setSchedule(Schedule schedule) throws ParseException {
        _schedule = schedule;
        SOURCE.put("schedule", schedule.getJson());
    }

    public Schedule getSchedule() {
        try {
            if (_schedule == null && SOURCE.has("schedule") && SOURCE.get("schedule") != null)
                _schedule = Schedule.fromJson(SOURCE.getJsonObject("schedule"));
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        if (_schedule != null && _schedule.isSet())
            return _schedule;

        return null;
    }

    public EtaMassAcceptWithLocation schedule(Schedule schedule) throws ParseException {
        _schedule = schedule;
        SOURCE.put("schedule", schedule.getJson());
        return this;
    }

    public void setTitle(String title) throws ParseException {
        _title = title;
        SOURCE.put("title", title);
    }

    public String getTitle() {
        try {
            if (_title == null && SOURCE.has("title") && SOURCE.get("title") != null)
                _title = SOURCE.getString("title");
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _title;
    }

    public EtaMassAcceptWithLocation title(String title) throws ParseException {
        _title = title;
        SOURCE.put("title", title);
        return this;
    }

    public void setWorkOrderId(Integer workOrderId) throws ParseException {
        _workOrderId = workOrderId;
        SOURCE.put("work_order_id", workOrderId);
    }

    public Integer getWorkOrderId() {
        try {
            if (_workOrderId == null && SOURCE.has("work_order_id") && SOURCE.get("work_order_id") != null)
                _workOrderId = SOURCE.getInt("work_order_id");
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _workOrderId;
    }

    public EtaMassAcceptWithLocation workOrderId(Integer workOrderId) throws ParseException {
        _workOrderId = workOrderId;
        SOURCE.put("work_order_id", workOrderId);
        return this;
    }

    /*-*****************************-*/
    /*-             Json            -*/
    /*-*****************************-*/
    public static JsonArray toJsonArray(EtaMassAcceptWithLocation[] array) {
        JsonArray list = new JsonArray();
        for (EtaMassAcceptWithLocation item : array) {
            list.add(item.getJson());
        }
        return list;
    }

    public static EtaMassAcceptWithLocation[] fromJsonArray(JsonArray array) {
        EtaMassAcceptWithLocation[] list = new EtaMassAcceptWithLocation[array.size()];
        for (int i = 0; i < array.size(); i++) {
            list[i] = fromJson(array.getJsonObject(i));
        }
        return list;
    }

    public static EtaMassAcceptWithLocation fromJson(JsonObject obj) {
        try {
            return new EtaMassAcceptWithLocation(obj);
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
    public static final Parcelable.Creator<EtaMassAcceptWithLocation> CREATOR = new Parcelable.Creator<EtaMassAcceptWithLocation>() {

        @Override
        public EtaMassAcceptWithLocation createFromParcel(Parcel source) {
            try {
                return EtaMassAcceptWithLocation.fromJson((JsonObject) source.readParcelable(JsonObject.class.getClassLoader()));
            } catch (Exception ex) {
                Log.v(TAG, ex);
                return null;
            }
        }

        @Override
        public EtaMassAcceptWithLocation[] newArray(int size) {
            return new EtaMassAcceptWithLocation[size];
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
