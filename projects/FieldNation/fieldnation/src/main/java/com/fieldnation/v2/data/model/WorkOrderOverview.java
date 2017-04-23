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

public class WorkOrderOverview implements Parcelable {
    private static final String TAG = "WorkOrderOverview";

    @Json(name = "company")
    private WorkOrderOverviewCompany _company;

    @Json(name = "type_of_work")
    private WorkOrderOverviewTypeOfWork _typeOfWork;

    @Source
    private JsonObject SOURCE;

    public WorkOrderOverview() {
        SOURCE = new JsonObject();
    }

    public WorkOrderOverview(JsonObject obj) {
        SOURCE = obj;
    }

    public void setCompany(WorkOrderOverviewCompany company) throws ParseException {
        _company = company;
        SOURCE.put("company", company.getJson());
    }

    public WorkOrderOverviewCompany getCompany() {
        try {
            if (_company == null && SOURCE.has("company") && SOURCE.get("company") != null)
                _company = WorkOrderOverviewCompany.fromJson(SOURCE.getJsonObject("company"));
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        if (_company != null && _company.isSet())
            return _company;

        return null;
    }

    public WorkOrderOverview company(WorkOrderOverviewCompany company) throws ParseException {
        _company = company;
        SOURCE.put("company", company.getJson());
        return this;
    }

    public void setTypeOfWork(WorkOrderOverviewTypeOfWork typeOfWork) throws ParseException {
        _typeOfWork = typeOfWork;
        SOURCE.put("type_of_work", typeOfWork.getJson());
    }

    public WorkOrderOverviewTypeOfWork getTypeOfWork() {
        try {
            if (_typeOfWork == null && SOURCE.has("type_of_work") && SOURCE.get("type_of_work") != null)
                _typeOfWork = WorkOrderOverviewTypeOfWork.fromJson(SOURCE.getJsonObject("type_of_work"));
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        if (_typeOfWork != null && _typeOfWork.isSet())
            return _typeOfWork;

        return null;
    }

    public WorkOrderOverview typeOfWork(WorkOrderOverviewTypeOfWork typeOfWork) throws ParseException {
        _typeOfWork = typeOfWork;
        SOURCE.put("type_of_work", typeOfWork.getJson());
        return this;
    }

    /*-*****************************-*/
    /*-             Json            -*/
    /*-*****************************-*/
    public static JsonArray toJsonArray(WorkOrderOverview[] array) {
        JsonArray list = new JsonArray();
        for (WorkOrderOverview item : array) {
            list.add(item.getJson());
        }
        return list;
    }

    public static WorkOrderOverview[] fromJsonArray(JsonArray array) {
        WorkOrderOverview[] list = new WorkOrderOverview[array.size()];
        for (int i = 0; i < array.size(); i++) {
            list[i] = fromJson(array.getJsonObject(i));
        }
        return list;
    }

    public static WorkOrderOverview fromJson(JsonObject obj) {
        try {
            return new WorkOrderOverview(obj);
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
    public static final Parcelable.Creator<WorkOrderOverview> CREATOR = new Parcelable.Creator<WorkOrderOverview>() {

        @Override
        public WorkOrderOverview createFromParcel(Parcel source) {
            try {
                return WorkOrderOverview.fromJson((JsonObject) source.readParcelable(JsonObject.class.getClassLoader()));
            } catch (Exception ex) {
                Log.v(TAG, ex);
                return null;
            }
        }

        @Override
        public WorkOrderOverview[] newArray(int size) {
            return new WorkOrderOverview[size];
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
