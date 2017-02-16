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

public class BundleWorkOrders implements Parcelable {
    private static final String TAG = "BundleWorkOrders";

    @Json(name = "bundle_id")
    private Integer _bundleId;

    @Json(name = "results")
    private WorkOrder[] _results;

    @Json(name = "total")
    private Integer _total;

    @Json(name = "work_order_id")
    private Integer _workOrderId;

    @Source
    private JsonObject SOURCE = new JsonObject();

    public BundleWorkOrders() {
    }

    public void setBundleId(Integer bundleId) throws ParseException {
        _bundleId = bundleId;
        SOURCE.put("bundle_id", bundleId);
    }

    public Integer getBundleId() {
        return _bundleId;
    }

    public BundleWorkOrders bundleId(Integer bundleId) throws ParseException {
        _bundleId = bundleId;
        SOURCE.put("bundle_id", bundleId);
        return this;
    }

    public void setResults(WorkOrder[] results) throws ParseException {
        _results = results;
        SOURCE.put("results", WorkOrder.toJsonArray(results));
    }

    public WorkOrder[] getResults() {
        return _results;
    }

    public BundleWorkOrders results(WorkOrder[] results) throws ParseException {
        _results = results;
        SOURCE.put("results", WorkOrder.toJsonArray(results), true);
        return this;
    }

    public void setTotal(Integer total) throws ParseException {
        _total = total;
        SOURCE.put("total", total);
    }

    public Integer getTotal() {
        return _total;
    }

    public BundleWorkOrders total(Integer total) throws ParseException {
        _total = total;
        SOURCE.put("total", total);
        return this;
    }

    public void setWorkOrderId(Integer workOrderId) throws ParseException {
        _workOrderId = workOrderId;
        SOURCE.put("work_order_id", workOrderId);
    }

    public Integer getWorkOrderId() {
        return _workOrderId;
    }

    public BundleWorkOrders workOrderId(Integer workOrderId) throws ParseException {
        _workOrderId = workOrderId;
        SOURCE.put("work_order_id", workOrderId);
        return this;
    }

    /*-*****************************-*/
    /*-             Json            -*/
    /*-*****************************-*/
    public static JsonArray toJsonArray(BundleWorkOrders[] array) {
        JsonArray list = new JsonArray();
        for (BundleWorkOrders item : array) {
            list.add(item.getJson());
        }
        return list;
    }

    public static BundleWorkOrders[] fromJsonArray(JsonArray array) {
        BundleWorkOrders[] list = new BundleWorkOrders[array.size()];
        for (int i = 0; i < array.size(); i++) {
            list[i] = fromJson(array.getJsonObject(i));
        }
        return list;
    }

    public static BundleWorkOrders fromJson(JsonObject obj) {
        try {
            return Unserializer.unserializeObject(BundleWorkOrders.class, obj);
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
    public static final Parcelable.Creator<BundleWorkOrders> CREATOR = new Parcelable.Creator<BundleWorkOrders>() {

        @Override
        public BundleWorkOrders createFromParcel(Parcel source) {
            try {
                return BundleWorkOrders.fromJson((JsonObject) source.readParcelable(JsonObject.class.getClassLoader()));
            } catch (Exception ex) {
                Log.v(TAG, ex);
                return null;
            }
        }

        @Override
        public BundleWorkOrders[] newArray(int size) {
            return new BundleWorkOrders[size];
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
