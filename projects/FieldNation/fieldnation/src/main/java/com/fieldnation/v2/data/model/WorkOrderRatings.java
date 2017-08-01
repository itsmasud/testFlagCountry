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

public class WorkOrderRatings implements Parcelable {
    private static final String TAG = "WorkOrderRatings";

    @Json(name = "assigned_provider")
    private WorkOrderRatingsAssignedProvider _assignedProvider;

    @Json(name = "buyer")
    private WorkOrderRatingsBuyer _buyer;

    @Json(name = "correlation_id")
    private String _correlationId;

    @Json(name = "service_company")
    private WorkOrderRatingsServiceCompany _serviceCompany;

    @Source
    private JsonObject SOURCE;

    public WorkOrderRatings() {
        SOURCE = new JsonObject();
    }

    public WorkOrderRatings(JsonObject obj) {
        SOURCE = obj;
    }

    public void setAssignedProvider(WorkOrderRatingsAssignedProvider assignedProvider) throws ParseException {
        _assignedProvider = assignedProvider;
        SOURCE.put("assigned_provider", assignedProvider.getJson());
    }

    public WorkOrderRatingsAssignedProvider getAssignedProvider() {
        try {
            if (_assignedProvider == null && SOURCE.has("assigned_provider") && SOURCE.get("assigned_provider") != null)
                _assignedProvider = WorkOrderRatingsAssignedProvider.fromJson(SOURCE.getJsonObject("assigned_provider"));
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        if (_assignedProvider == null)
            _assignedProvider = new WorkOrderRatingsAssignedProvider();

            return _assignedProvider;
    }

    public WorkOrderRatings assignedProvider(WorkOrderRatingsAssignedProvider assignedProvider) throws ParseException {
        _assignedProvider = assignedProvider;
        SOURCE.put("assigned_provider", assignedProvider.getJson());
        return this;
    }

    public void setBuyer(WorkOrderRatingsBuyer buyer) throws ParseException {
        _buyer = buyer;
        SOURCE.put("buyer", buyer.getJson());
    }

    public WorkOrderRatingsBuyer getBuyer() {
        try {
            if (_buyer == null && SOURCE.has("buyer") && SOURCE.get("buyer") != null)
                _buyer = WorkOrderRatingsBuyer.fromJson(SOURCE.getJsonObject("buyer"));
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        if (_buyer == null)
            _buyer = new WorkOrderRatingsBuyer();

            return _buyer;
    }

    public WorkOrderRatings buyer(WorkOrderRatingsBuyer buyer) throws ParseException {
        _buyer = buyer;
        SOURCE.put("buyer", buyer.getJson());
        return this;
    }

    public void setCorrelationId(String correlationId) throws ParseException {
        _correlationId = correlationId;
        SOURCE.put("correlation_id", correlationId);
    }

    public String getCorrelationId() {
        try {
            if (_correlationId == null && SOURCE.has("correlation_id") && SOURCE.get("correlation_id") != null)
                _correlationId = SOURCE.getString("correlation_id");
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _correlationId;
    }

    public WorkOrderRatings correlationId(String correlationId) throws ParseException {
        _correlationId = correlationId;
        SOURCE.put("correlation_id", correlationId);
        return this;
    }

    public void setServiceCompany(WorkOrderRatingsServiceCompany serviceCompany) throws ParseException {
        _serviceCompany = serviceCompany;
        SOURCE.put("service_company", serviceCompany.getJson());
    }

    public WorkOrderRatingsServiceCompany getServiceCompany() {
        try {
            if (_serviceCompany == null && SOURCE.has("service_company") && SOURCE.get("service_company") != null)
                _serviceCompany = WorkOrderRatingsServiceCompany.fromJson(SOURCE.getJsonObject("service_company"));
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        if (_serviceCompany == null)
            _serviceCompany = new WorkOrderRatingsServiceCompany();

            return _serviceCompany;
    }

    public WorkOrderRatings serviceCompany(WorkOrderRatingsServiceCompany serviceCompany) throws ParseException {
        _serviceCompany = serviceCompany;
        SOURCE.put("service_company", serviceCompany.getJson());
        return this;
    }

    /*-*****************************-*/
    /*-             Json            -*/
    /*-*****************************-*/
    public static JsonArray toJsonArray(WorkOrderRatings[] array) {
        JsonArray list = new JsonArray();
        for (WorkOrderRatings item : array) {
            list.add(item.getJson());
        }
        return list;
    }

    public static WorkOrderRatings[] fromJsonArray(JsonArray array) {
        WorkOrderRatings[] list = new WorkOrderRatings[array.size()];
        for (int i = 0; i < array.size(); i++) {
            list[i] = fromJson(array.getJsonObject(i));
        }
        return list;
    }

    public static WorkOrderRatings fromJson(JsonObject obj) {
        try {
            return new WorkOrderRatings(obj);
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
    public static final Parcelable.Creator<WorkOrderRatings> CREATOR = new Parcelable.Creator<WorkOrderRatings>() {

        @Override
        public WorkOrderRatings createFromParcel(Parcel source) {
            try {
                return WorkOrderRatings.fromJson((JsonObject) source.readParcelable(JsonObject.class.getClassLoader()));
            } catch (Exception ex) {
                Log.v(TAG, ex);
                return null;
            }
        }

        @Override
        public WorkOrderRatings[] newArray(int size) {
            return new WorkOrderRatings[size];
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

}
