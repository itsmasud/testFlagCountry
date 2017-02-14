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

    public BundleWorkOrders() {
    }

    public void setBundleId(Integer bundleId) {
        _bundleId = bundleId;
    }

    public Integer getBundleId() {
        return _bundleId;
    }

    public BundleWorkOrders bundleId(Integer bundleId) {
        _bundleId = bundleId;
        return this;
    }

    public void setResults(WorkOrder[] results) {
        _results = results;
    }

    public WorkOrder[] getResults() {
        return _results;
    }

    public BundleWorkOrders results(WorkOrder[] results) {
        _results = results;
        return this;
    }

    public void setTotal(Integer total) {
        _total = total;
    }

    public Integer getTotal() {
        return _total;
    }

    public BundleWorkOrders total(Integer total) {
        _total = total;
        return this;
    }

    public void setWorkOrderId(Integer workOrderId) {
        _workOrderId = workOrderId;
    }

    public Integer getWorkOrderId() {
        return _workOrderId;
    }

    public BundleWorkOrders workOrderId(Integer workOrderId) {
        _workOrderId = workOrderId;
        return this;
    }

    /*-*****************************-*/
    /*-             Json            -*/
    /*-*****************************-*/
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

    public JsonObject toJson() {
        return toJson(this);
    }

    public static JsonObject toJson(BundleWorkOrders bundleWorkOrders) {
        try {
            return Serializer.serializeObject(bundleWorkOrders);
        } catch (Exception ex) {
            Log.v(TAG, TAG, ex);
            return null;
        }
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
        dest.writeParcelable(toJson(), flags);
    }
}
