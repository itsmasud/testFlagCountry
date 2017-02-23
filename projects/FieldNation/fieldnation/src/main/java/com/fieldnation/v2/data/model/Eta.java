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

public class Eta implements Parcelable {
    private static final String TAG = "Eta";

    @Json(name = "bundle_id")
    private Integer _bundleId;

    @Json(name = "hour_estimate")
    private Double _hourEstimate;

    @Json(name = "notes")
    private String _notes;

    @Json(name = "start")
    private Date _start;

    @Json(name = "work_order_id")
    private Integer _workOrderId;

    @Source
    private JsonObject SOURCE;

    public Eta() {
        SOURCE = new JsonObject();
    }

    public Eta(JsonObject obj) {
        SOURCE = obj;
    }

    public void setBundleId(Integer bundleId) throws ParseException {
        _bundleId = bundleId;
        SOURCE.put("bundle_id", bundleId);
    }

    public Integer getBundleId() {
        try {
            if (_bundleId != null)
                return _bundleId;

            if (SOURCE.has("bundle_id") && SOURCE.get("bundle_id") != null)
                _bundleId = SOURCE.getInt("bundle_id");

        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _bundleId;
    }

    public Eta bundleId(Integer bundleId) throws ParseException {
        _bundleId = bundleId;
        SOURCE.put("bundle_id", bundleId);
        return this;
    }

    public void setHourEstimate(Double hourEstimate) throws ParseException {
        _hourEstimate = hourEstimate;
        SOURCE.put("hour_estimate", hourEstimate);
    }

    public Double getHourEstimate() {
        try {
            if (_hourEstimate != null)
                return _hourEstimate;

            if (SOURCE.has("hour_estimate") && SOURCE.get("hour_estimate") != null)
                _hourEstimate = SOURCE.getDouble("hour_estimate");

        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _hourEstimate;
    }

    public Eta hourEstimate(Double hourEstimate) throws ParseException {
        _hourEstimate = hourEstimate;
        SOURCE.put("hour_estimate", hourEstimate);
        return this;
    }

    public void setNotes(String notes) throws ParseException {
        _notes = notes;
        SOURCE.put("notes", notes);
    }

    public String getNotes() {
        try {
            if (_notes != null)
                return _notes;

            if (SOURCE.has("notes") && SOURCE.get("notes") != null)
                _notes = SOURCE.getString("notes");

        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _notes;
    }

    public Eta notes(String notes) throws ParseException {
        _notes = notes;
        SOURCE.put("notes", notes);
        return this;
    }

    public void setStart(Date start) throws ParseException {
        _start = start;
        SOURCE.put("start", start.getJson());
    }

    public Date getStart() {
        try {
            if (_start != null)
                return _start;

            if (SOURCE.has("start") && SOURCE.get("start") != null)
                _start = Date.fromJson(SOURCE.getJsonObject("start"));

        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _start;
    }

    public Eta start(Date start) throws ParseException {
        _start = start;
        SOURCE.put("start", start.getJson());
        return this;
    }

    public void setWorkOrderId(Integer workOrderId) throws ParseException {
        _workOrderId = workOrderId;
        SOURCE.put("work_order_id", workOrderId);
    }

    public Integer getWorkOrderId() {
        try {
            if (_workOrderId != null)
                return _workOrderId;

            if (SOURCE.has("work_order_id") && SOURCE.get("work_order_id") != null)
                _workOrderId = SOURCE.getInt("work_order_id");

        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _workOrderId;
    }

    public Eta workOrderId(Integer workOrderId) throws ParseException {
        _workOrderId = workOrderId;
        SOURCE.put("work_order_id", workOrderId);
        return this;
    }

    /*-*****************************-*/
    /*-             Json            -*/
    /*-*****************************-*/
    public static JsonArray toJsonArray(Eta[] array) {
        JsonArray list = new JsonArray();
        for (Eta item : array) {
            list.add(item.getJson());
        }
        return list;
    }

    public static Eta[] fromJsonArray(JsonArray array) {
        Eta[] list = new Eta[array.size()];
        for (int i = 0; i < array.size(); i++) {
            list[i] = fromJson(array.getJsonObject(i));
        }
        return list;
    }

    public static Eta fromJson(JsonObject obj) {
        try {
            return new Eta(obj);
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
    public static final Parcelable.Creator<Eta> CREATOR = new Parcelable.Creator<Eta>() {

        @Override
        public Eta createFromParcel(Parcel source) {
            try {
                return Eta.fromJson((JsonObject) source.readParcelable(JsonObject.class.getClassLoader()));
            } catch (Exception ex) {
                Log.v(TAG, ex);
                return null;
            }
        }

        @Override
        public Eta[] newArray(int size) {
            return new Eta[size];
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
