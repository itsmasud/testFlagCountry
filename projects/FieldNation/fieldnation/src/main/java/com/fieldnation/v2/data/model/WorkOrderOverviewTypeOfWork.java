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

public class WorkOrderOverviewTypeOfWork implements Parcelable {
    private static final String TAG = "WorkOrderOverviewTypeOfWork";

    @Json(name = "company")
    private WorkOrderOverviewTypeOfWorkCompany _company;

    @Json(name = "marketplace")
    private WorkOrderOverviewTypeOfWorkMarketplace _marketplace;

    @Source
    private JsonObject SOURCE;

    public WorkOrderOverviewTypeOfWork() {
        SOURCE = new JsonObject();
    }

    public WorkOrderOverviewTypeOfWork(JsonObject obj) {
        SOURCE = obj;
    }

    public void setCompany(WorkOrderOverviewTypeOfWorkCompany company) throws ParseException {
        _company = company;
        SOURCE.put("company", company.getJson());
    }

    public WorkOrderOverviewTypeOfWorkCompany getCompany() {
        try {
            if (_company == null && SOURCE.has("company") && SOURCE.get("company") != null)
                _company = WorkOrderOverviewTypeOfWorkCompany.fromJson(SOURCE.getJsonObject("company"));
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        if (_company == null)
            _company = new WorkOrderOverviewTypeOfWorkCompany();

            return _company;
    }

    public WorkOrderOverviewTypeOfWork company(WorkOrderOverviewTypeOfWorkCompany company) throws ParseException {
        _company = company;
        SOURCE.put("company", company.getJson());
        return this;
    }

    public void setMarketplace(WorkOrderOverviewTypeOfWorkMarketplace marketplace) throws ParseException {
        _marketplace = marketplace;
        SOURCE.put("marketplace", marketplace.getJson());
    }

    public WorkOrderOverviewTypeOfWorkMarketplace getMarketplace() {
        try {
            if (_marketplace == null && SOURCE.has("marketplace") && SOURCE.get("marketplace") != null)
                _marketplace = WorkOrderOverviewTypeOfWorkMarketplace.fromJson(SOURCE.getJsonObject("marketplace"));
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        if (_marketplace == null)
            _marketplace = new WorkOrderOverviewTypeOfWorkMarketplace();

            return _marketplace;
    }

    public WorkOrderOverviewTypeOfWork marketplace(WorkOrderOverviewTypeOfWorkMarketplace marketplace) throws ParseException {
        _marketplace = marketplace;
        SOURCE.put("marketplace", marketplace.getJson());
        return this;
    }

    /*-*****************************-*/
    /*-             Json            -*/
    /*-*****************************-*/
    public static JsonArray toJsonArray(WorkOrderOverviewTypeOfWork[] array) {
        JsonArray list = new JsonArray();
        for (WorkOrderOverviewTypeOfWork item : array) {
            list.add(item.getJson());
        }
        return list;
    }

    public static WorkOrderOverviewTypeOfWork[] fromJsonArray(JsonArray array) {
        WorkOrderOverviewTypeOfWork[] list = new WorkOrderOverviewTypeOfWork[array.size()];
        for (int i = 0; i < array.size(); i++) {
            list[i] = fromJson(array.getJsonObject(i));
        }
        return list;
    }

    public static WorkOrderOverviewTypeOfWork fromJson(JsonObject obj) {
        try {
            return new WorkOrderOverviewTypeOfWork(obj);
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
    public static final Parcelable.Creator<WorkOrderOverviewTypeOfWork> CREATOR = new Parcelable.Creator<WorkOrderOverviewTypeOfWork>() {

        @Override
        public WorkOrderOverviewTypeOfWork createFromParcel(Parcel source) {
            try {
                return WorkOrderOverviewTypeOfWork.fromJson((JsonObject) source.readParcelable(JsonObject.class.getClassLoader()));
            } catch (Exception ex) {
                Log.v(TAG, ex);
                return null;
            }
        }

        @Override
        public WorkOrderOverviewTypeOfWork[] newArray(int size) {
            return new WorkOrderOverviewTypeOfWork[size];
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
