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

public class PricingInsightsRegion implements Parcelable {
    private static final String TAG = "PricingInsightsRegion";

    @Json(name = "average_rate")
    private PricingInsightsRegionAverageRate _averageRate;

    @Json(name = "distance")
    private Double _distance;

    @Json(name = "id")
    private Integer _id;

    @Json(name = "name")
    private String _name;

    @Json(name = "providers")
    private PricingInsightsRegionProviders _providers;

    @Source
    private JsonObject SOURCE = new JsonObject();

    public PricingInsightsRegion() {
    }

    public void setAverageRate(PricingInsightsRegionAverageRate averageRate) throws ParseException {
        _averageRate = averageRate;
        SOURCE.put("average_rate", averageRate.getJson());
    }

    public PricingInsightsRegionAverageRate getAverageRate() {
        return _averageRate;
    }

    public PricingInsightsRegion averageRate(PricingInsightsRegionAverageRate averageRate) throws ParseException {
        _averageRate = averageRate;
        SOURCE.put("average_rate", averageRate.getJson());
        return this;
    }

    public void setDistance(Double distance) throws ParseException {
        _distance = distance;
        SOURCE.put("distance", distance);
    }

    public Double getDistance() {
        return _distance;
    }

    public PricingInsightsRegion distance(Double distance) throws ParseException {
        _distance = distance;
        SOURCE.put("distance", distance);
        return this;
    }

    public void setId(Integer id) throws ParseException {
        _id = id;
        SOURCE.put("id", id);
    }

    public Integer getId() {
        return _id;
    }

    public PricingInsightsRegion id(Integer id) throws ParseException {
        _id = id;
        SOURCE.put("id", id);
        return this;
    }

    public void setName(String name) throws ParseException {
        _name = name;
        SOURCE.put("name", name);
    }

    public String getName() {
        return _name;
    }

    public PricingInsightsRegion name(String name) throws ParseException {
        _name = name;
        SOURCE.put("name", name);
        return this;
    }

    public void setProviders(PricingInsightsRegionProviders providers) throws ParseException {
        _providers = providers;
        SOURCE.put("providers", providers.getJson());
    }

    public PricingInsightsRegionProviders getProviders() {
        return _providers;
    }

    public PricingInsightsRegion providers(PricingInsightsRegionProviders providers) throws ParseException {
        _providers = providers;
        SOURCE.put("providers", providers.getJson());
        return this;
    }

    /*-*****************************-*/
    /*-             Json            -*/
    /*-*****************************-*/
    public static JsonArray toJsonArray(PricingInsightsRegion[] array) {
        JsonArray list = new JsonArray();
        for (PricingInsightsRegion item : array) {
            list.add(item.getJson());
        }
        return list;
    }

    public static PricingInsightsRegion[] fromJsonArray(JsonArray array) {
        PricingInsightsRegion[] list = new PricingInsightsRegion[array.size()];
        for (int i = 0; i < array.size(); i++) {
            list[i] = fromJson(array.getJsonObject(i));
        }
        return list;
    }

    public static PricingInsightsRegion fromJson(JsonObject obj) {
        try {
            return Unserializer.unserializeObject(PricingInsightsRegion.class, obj);
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
    public static final Parcelable.Creator<PricingInsightsRegion> CREATOR = new Parcelable.Creator<PricingInsightsRegion>() {

        @Override
        public PricingInsightsRegion createFromParcel(Parcel source) {
            try {
                return PricingInsightsRegion.fromJson((JsonObject) source.readParcelable(JsonObject.class.getClassLoader()));
            } catch (Exception ex) {
                Log.v(TAG, ex);
                return null;
            }
        }

        @Override
        public PricingInsightsRegion[] newArray(int size) {
            return new PricingInsightsRegion[size];
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
