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

public class EtaMassAccept implements Parcelable {
    private static final String TAG = "EtaMassAccept";

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

    public EtaMassAccept() {
        SOURCE = new JsonObject();
    }

    public EtaMassAccept(JsonObject obj) {
        SOURCE = obj;
    }

    public void setBundleId(Integer bundleId) throws ParseException {
        _bundleId = bundleId;
        SOURCE.put("bundle_id", bundleId);
    }

    public Integer getBundleId() {
        try {
            if (_bundleId == null && SOURCE.has("bundle_id") && SOURCE.get("bundle_id") != null)
                _bundleId = SOURCE.getInt("bundle_id");
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _bundleId;
    }

    public EtaMassAccept bundleId(Integer bundleId) throws ParseException {
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
            if (_hourEstimate == null && SOURCE.has("hour_estimate") && SOURCE.get("hour_estimate") != null)
                _hourEstimate = SOURCE.getDouble("hour_estimate");
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _hourEstimate;
    }

    public EtaMassAccept hourEstimate(Double hourEstimate) throws ParseException {
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
            if (_notes == null && SOURCE.has("notes") && SOURCE.get("notes") != null)
                _notes = SOURCE.getString("notes");
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _notes;
    }

    public EtaMassAccept notes(String notes) throws ParseException {
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
            if (_start == null && SOURCE.has("start") && SOURCE.get("start") != null)
                _start = Date.fromJson(SOURCE.getJsonObject("start"));
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        if (_start != null && _start.isSet())
            return _start;

        return null;
    }

    public EtaMassAccept start(Date start) throws ParseException {
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
            if (_workOrderId == null && SOURCE.has("work_order_id") && SOURCE.get("work_order_id") != null)
                _workOrderId = SOURCE.getInt("work_order_id");
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _workOrderId;
    }

    public EtaMassAccept workOrderId(Integer workOrderId) throws ParseException {
        _workOrderId = workOrderId;
        SOURCE.put("work_order_id", workOrderId);
        return this;
    }

    /*-*****************************-*/
    /*-             Json            -*/
    /*-*****************************-*/
    public static JsonArray toJsonArray(EtaMassAccept[] array) {
        JsonArray list = new JsonArray();
        for (EtaMassAccept item : array) {
            list.add(item.getJson());
        }
        return list;
    }

    public static EtaMassAccept[] fromJsonArray(JsonArray array) {
        EtaMassAccept[] list = new EtaMassAccept[array.size()];
        for (int i = 0; i < array.size(); i++) {
            list[i] = fromJson(array.getJsonObject(i));
        }
        return list;
    }

    public static EtaMassAccept fromJson(JsonObject obj) {
        try {
            return new EtaMassAccept(obj);
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
    public static final Parcelable.Creator<EtaMassAccept> CREATOR = new Parcelable.Creator<EtaMassAccept>() {

        @Override
        public EtaMassAccept createFromParcel(Parcel source) {
            try {
                return EtaMassAccept.fromJson((JsonObject) source.readParcelable(JsonObject.class.getClassLoader()));
            } catch (Exception ex) {
                Log.v(TAG, ex);
                return null;
            }
        }

        @Override
        public EtaMassAccept[] newArray(int size) {
            return new EtaMassAccept[size];
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
