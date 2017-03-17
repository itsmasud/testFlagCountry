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

public class WorkOrderPublishStats implements Parcelable {
    private static final String TAG = "WorkOrderPublishStats";

    @Json(name = "counter_offers")
    private Integer _counterOffers;

    @Json(name = "requests")
    private Integer _requests;

    @Json(name = "routes")
    private Integer _routes;

    @Source
    private JsonObject SOURCE;

    public WorkOrderPublishStats() {
        SOURCE = new JsonObject();
    }

    public WorkOrderPublishStats(JsonObject obj) {
        SOURCE = obj;
    }

    public void setCounterOffers(Integer counterOffers) throws ParseException {
        _counterOffers = counterOffers;
        SOURCE.put("counter_offers", counterOffers);
    }

    public Integer getCounterOffers() {
        try {
            if (_counterOffers != null)
                return _counterOffers;

            if (SOURCE.has("counter_offers") && SOURCE.get("counter_offers") != null)
                _counterOffers = SOURCE.getInt("counter_offers");

        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _counterOffers;
    }

    public WorkOrderPublishStats counterOffers(Integer counterOffers) throws ParseException {
        _counterOffers = counterOffers;
        SOURCE.put("counter_offers", counterOffers);
        return this;
    }

    public void setRequests(Integer requests) throws ParseException {
        _requests = requests;
        SOURCE.put("requests", requests);
    }

    public Integer getRequests() {
        try {
            if (_requests != null)
                return _requests;

            if (SOURCE.has("requests") && SOURCE.get("requests") != null)
                _requests = SOURCE.getInt("requests");

        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _requests;
    }

    public WorkOrderPublishStats requests(Integer requests) throws ParseException {
        _requests = requests;
        SOURCE.put("requests", requests);
        return this;
    }

    public void setRoutes(Integer routes) throws ParseException {
        _routes = routes;
        SOURCE.put("routes", routes);
    }

    public Integer getRoutes() {
        try {
            if (_routes != null)
                return _routes;

            if (SOURCE.has("routes") && SOURCE.get("routes") != null)
                _routes = SOURCE.getInt("routes");

        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _routes;
    }

    public WorkOrderPublishStats routes(Integer routes) throws ParseException {
        _routes = routes;
        SOURCE.put("routes", routes);
        return this;
    }

    /*-*****************************-*/
    /*-             Json            -*/
    /*-*****************************-*/
    public static JsonArray toJsonArray(WorkOrderPublishStats[] array) {
        JsonArray list = new JsonArray();
        for (WorkOrderPublishStats item : array) {
            list.add(item.getJson());
        }
        return list;
    }

    public static WorkOrderPublishStats[] fromJsonArray(JsonArray array) {
        WorkOrderPublishStats[] list = new WorkOrderPublishStats[array.size()];
        for (int i = 0; i < array.size(); i++) {
            list[i] = fromJson(array.getJsonObject(i));
        }
        return list;
    }

    public static WorkOrderPublishStats fromJson(JsonObject obj) {
        try {
            return new WorkOrderPublishStats(obj);
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
    public static final Parcelable.Creator<WorkOrderPublishStats> CREATOR = new Parcelable.Creator<WorkOrderPublishStats>() {

        @Override
        public WorkOrderPublishStats createFromParcel(Parcel source) {
            try {
                return WorkOrderPublishStats.fromJson((JsonObject) source.readParcelable(JsonObject.class.getClassLoader()));
            } catch (Exception ex) {
                Log.v(TAG, ex);
                return null;
            }
        }

        @Override
        public WorkOrderPublishStats[] newArray(int size) {
            return new WorkOrderPublishStats[size];
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

    public boolean isSet() {
        return true;
    }
}
