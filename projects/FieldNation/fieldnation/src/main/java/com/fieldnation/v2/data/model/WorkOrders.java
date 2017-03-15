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

public class WorkOrders implements Parcelable {
    private static final String TAG = "WorkOrders";

    @Json(name = "metadata")
    private ListEnvelope _metadata;

    @Json(name = "provider_first_tow_filter")
    private Boolean _providerFirstTowFilter;

    @Json(name = "results")
    private WorkOrder[] _results;

    @Json(name = "user_id")
    private Double _userId;

    @Source
    private JsonObject SOURCE;

    public WorkOrders() {
        SOURCE = new JsonObject();
    }

    public WorkOrders(JsonObject obj) {
        SOURCE = obj;
    }

    public void setMetadata(ListEnvelope metadata) throws ParseException {
        _metadata = metadata;
        SOURCE.put("metadata", metadata.getJson());
    }

    public ListEnvelope getMetadata() {
        try {
            if (_metadata == null && SOURCE.has("metadata") && SOURCE.get("metadata") != null)
                _metadata = ListEnvelope.fromJson(SOURCE.getJsonObject("metadata"));
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        if (_metadata != null && _metadata.isSet())
            return _metadata;

        return null;
    }

    public WorkOrders metadata(ListEnvelope metadata) throws ParseException {
        _metadata = metadata;
        SOURCE.put("metadata", metadata.getJson());
        return this;
    }

    public void setProviderFirstTowFilter(Boolean providerFirstTowFilter) throws ParseException {
        _providerFirstTowFilter = providerFirstTowFilter;
        SOURCE.put("provider_first_tow_filter", providerFirstTowFilter);
    }

    public Boolean getProviderFirstTowFilter() {
        try {
            if (_providerFirstTowFilter == null && SOURCE.has("provider_first_tow_filter") && SOURCE.get("provider_first_tow_filter") != null)
                _providerFirstTowFilter = SOURCE.getBoolean("provider_first_tow_filter");
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _providerFirstTowFilter;
    }

    public WorkOrders providerFirstTowFilter(Boolean providerFirstTowFilter) throws ParseException {
        _providerFirstTowFilter = providerFirstTowFilter;
        SOURCE.put("provider_first_tow_filter", providerFirstTowFilter);
        return this;
    }

    public void setResults(WorkOrder[] results) throws ParseException {
        _results = results;
        SOURCE.put("results", WorkOrder.toJsonArray(results));
    }

    public WorkOrder[] getResults() {
        try {
            if (_results != null)
                return _results;

            if (SOURCE.has("results") && SOURCE.get("results") != null) {
                _results = WorkOrder.fromJsonArray(SOURCE.getJsonArray("results"));
            }

        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _results;
    }

    public WorkOrders results(WorkOrder[] results) throws ParseException {
        _results = results;
        SOURCE.put("results", WorkOrder.toJsonArray(results), true);
        return this;
    }

    public void setUserId(Double userId) throws ParseException {
        _userId = userId;
        SOURCE.put("user_id", userId);
    }

    public Double getUserId() {
        try {
            if (_userId == null && SOURCE.has("user_id") && SOURCE.get("user_id") != null)
                _userId = SOURCE.getDouble("user_id");
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _userId;
    }

    public WorkOrders userId(Double userId) throws ParseException {
        _userId = userId;
        SOURCE.put("user_id", userId);
        return this;
    }

    /*-*****************************-*/
    /*-             Json            -*/
    /*-*****************************-*/
    public static JsonArray toJsonArray(WorkOrders[] array) {
        JsonArray list = new JsonArray();
        for (WorkOrders item : array) {
            list.add(item.getJson());
        }
        return list;
    }

    public static WorkOrders[] fromJsonArray(JsonArray array) {
        WorkOrders[] list = new WorkOrders[array.size()];
        for (int i = 0; i < array.size(); i++) {
            list[i] = fromJson(array.getJsonObject(i));
        }
        return list;
    }

    public static WorkOrders fromJson(JsonObject obj) {
        try {
            return new WorkOrders(obj);
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
    public static final Parcelable.Creator<WorkOrders> CREATOR = new Parcelable.Creator<WorkOrders>() {

        @Override
        public WorkOrders createFromParcel(Parcel source) {
            try {
                return WorkOrders.fromJson((JsonObject) source.readParcelable(JsonObject.class.getClassLoader()));
            } catch (Exception ex) {
                Log.v(TAG, ex);
                return null;
            }
        }

        @Override
        public WorkOrders[] newArray(int size) {
            return new WorkOrders[size];
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
