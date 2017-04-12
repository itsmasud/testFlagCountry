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

public class PublishStats implements Parcelable {
    private static final String TAG = "PublishStats";

    @Json(name = "all_requests")
    private Integer _allRequests;

    @Json(name = "correlation_id")
    private String _correlationId;

    @Json(name = "counter_offers")
    private Integer _counterOffers;

    @Json(name = "declines")
    private Integer _declines;

    @Json(name = "requests")
    private Integer _requests;

    @Json(name = "routes")
    private Integer _routes;

    @Source
    private JsonObject SOURCE;

    public PublishStats() {
        SOURCE = new JsonObject();
    }

    public PublishStats(JsonObject obj) {
        SOURCE = obj;
    }

    public void setAllRequests(Integer allRequests) throws ParseException {
        _allRequests = allRequests;
        SOURCE.put("all_requests", allRequests);
    }

    public Integer getAllRequests() {
        try {
            if (_allRequests == null && SOURCE.has("all_requests") && SOURCE.get("all_requests") != null)
                _allRequests = SOURCE.getInt("all_requests");
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _allRequests;
    }

    public PublishStats allRequests(Integer allRequests) throws ParseException {
        _allRequests = allRequests;
        SOURCE.put("all_requests", allRequests);
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

    public PublishStats correlationId(String correlationId) throws ParseException {
        _correlationId = correlationId;
        SOURCE.put("correlation_id", correlationId);
        return this;
    }

    public void setCounterOffers(Integer counterOffers) throws ParseException {
        _counterOffers = counterOffers;
        SOURCE.put("counter_offers", counterOffers);
    }

    public Integer getCounterOffers() {
        try {
            if (_counterOffers == null && SOURCE.has("counter_offers") && SOURCE.get("counter_offers") != null)
                _counterOffers = SOURCE.getInt("counter_offers");
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _counterOffers;
    }

    public PublishStats counterOffers(Integer counterOffers) throws ParseException {
        _counterOffers = counterOffers;
        SOURCE.put("counter_offers", counterOffers);
        return this;
    }

    public void setDeclines(Integer declines) throws ParseException {
        _declines = declines;
        SOURCE.put("declines", declines);
    }

    public Integer getDeclines() {
        try {
            if (_declines == null && SOURCE.has("declines") && SOURCE.get("declines") != null)
                _declines = SOURCE.getInt("declines");
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _declines;
    }

    public PublishStats declines(Integer declines) throws ParseException {
        _declines = declines;
        SOURCE.put("declines", declines);
        return this;
    }

    public void setRequests(Integer requests) throws ParseException {
        _requests = requests;
        SOURCE.put("requests", requests);
    }

    public Integer getRequests() {
        try {
            if (_requests == null && SOURCE.has("requests") && SOURCE.get("requests") != null)
                _requests = SOURCE.getInt("requests");
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _requests;
    }

    public PublishStats requests(Integer requests) throws ParseException {
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
            if (_routes == null && SOURCE.has("routes") && SOURCE.get("routes") != null)
                _routes = SOURCE.getInt("routes");
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _routes;
    }

    public PublishStats routes(Integer routes) throws ParseException {
        _routes = routes;
        SOURCE.put("routes", routes);
        return this;
    }

    /*-*****************************-*/
    /*-             Json            -*/
    /*-*****************************-*/
    public static JsonArray toJsonArray(PublishStats[] array) {
        JsonArray list = new JsonArray();
        for (PublishStats item : array) {
            list.add(item.getJson());
        }
        return list;
    }

    public static PublishStats[] fromJsonArray(JsonArray array) {
        PublishStats[] list = new PublishStats[array.size()];
        for (int i = 0; i < array.size(); i++) {
            list[i] = fromJson(array.getJsonObject(i));
        }
        return list;
    }

    public static PublishStats fromJson(JsonObject obj) {
        try {
            return new PublishStats(obj);
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
    public static final Parcelable.Creator<PublishStats> CREATOR = new Parcelable.Creator<PublishStats>() {

        @Override
        public PublishStats createFromParcel(Parcel source) {
            try {
                return PublishStats.fromJson((JsonObject) source.readParcelable(JsonObject.class.getClassLoader()));
            } catch (Exception ex) {
                Log.v(TAG, ex);
                return null;
            }
        }

        @Override
        public PublishStats[] newArray(int size) {
            return new PublishStats[size];
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
