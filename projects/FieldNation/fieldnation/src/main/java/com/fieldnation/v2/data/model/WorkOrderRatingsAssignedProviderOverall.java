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

public class WorkOrderRatingsAssignedProviderOverall implements Parcelable {
    private static final String TAG = "WorkOrderRatingsAssignedProviderOverall";

    @Json(name = "company")
    private ProviderRatings _company;

    @Json(name = "marketplace")
    private ProviderRatings _marketplace;

    @Source
    private JsonObject SOURCE;

    public WorkOrderRatingsAssignedProviderOverall() {
        SOURCE = new JsonObject();
    }

    public WorkOrderRatingsAssignedProviderOverall(JsonObject obj) {
        SOURCE = obj;
    }

    public void setCompany(ProviderRatings company) throws ParseException {
        _company = company;
        SOURCE.put("company", company.getJson());
    }

    public ProviderRatings getCompany() {
        try {
            if (_company == null && SOURCE.has("company") && SOURCE.get("company") != null)
                _company = ProviderRatings.fromJson(SOURCE.getJsonObject("company"));
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        if (_company != null && _company.isSet())
            return _company;

        return null;
    }

    public WorkOrderRatingsAssignedProviderOverall company(ProviderRatings company) throws ParseException {
        _company = company;
        SOURCE.put("company", company.getJson());
        return this;
    }

    public void setMarketplace(ProviderRatings marketplace) throws ParseException {
        _marketplace = marketplace;
        SOURCE.put("marketplace", marketplace.getJson());
    }

    public ProviderRatings getMarketplace() {
        try {
            if (_marketplace == null && SOURCE.has("marketplace") && SOURCE.get("marketplace") != null)
                _marketplace = ProviderRatings.fromJson(SOURCE.getJsonObject("marketplace"));
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        if (_marketplace != null && _marketplace.isSet())
            return _marketplace;

        return null;
    }

    public WorkOrderRatingsAssignedProviderOverall marketplace(ProviderRatings marketplace) throws ParseException {
        _marketplace = marketplace;
        SOURCE.put("marketplace", marketplace.getJson());
        return this;
    }

    /*-*****************************-*/
    /*-             Json            -*/
    /*-*****************************-*/
    public static JsonArray toJsonArray(WorkOrderRatingsAssignedProviderOverall[] array) {
        JsonArray list = new JsonArray();
        for (WorkOrderRatingsAssignedProviderOverall item : array) {
            list.add(item.getJson());
        }
        return list;
    }

    public static WorkOrderRatingsAssignedProviderOverall[] fromJsonArray(JsonArray array) {
        WorkOrderRatingsAssignedProviderOverall[] list = new WorkOrderRatingsAssignedProviderOverall[array.size()];
        for (int i = 0; i < array.size(); i++) {
            list[i] = fromJson(array.getJsonObject(i));
        }
        return list;
    }

    public static WorkOrderRatingsAssignedProviderOverall fromJson(JsonObject obj) {
        try {
            return new WorkOrderRatingsAssignedProviderOverall(obj);
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
    public static final Parcelable.Creator<WorkOrderRatingsAssignedProviderOverall> CREATOR = new Parcelable.Creator<WorkOrderRatingsAssignedProviderOverall>() {

        @Override
        public WorkOrderRatingsAssignedProviderOverall createFromParcel(Parcel source) {
            try {
                return WorkOrderRatingsAssignedProviderOverall.fromJson((JsonObject) source.readParcelable(JsonObject.class.getClassLoader()));
            } catch (Exception ex) {
                Log.v(TAG, ex);
                return null;
            }
        }

        @Override
        public WorkOrderRatingsAssignedProviderOverall[] newArray(int size) {
            return new WorkOrderRatingsAssignedProviderOverall[size];
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
