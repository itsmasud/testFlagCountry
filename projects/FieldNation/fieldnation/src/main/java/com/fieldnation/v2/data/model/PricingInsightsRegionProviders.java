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

public class PricingInsightsRegionProviders implements Parcelable {
    private static final String TAG = "PricingInsightsRegionProviders";

    @Json(name = "marketplace")
    private Integer _marketplace;

    @Source
    private JsonObject SOURCE;

    public PricingInsightsRegionProviders() {
        SOURCE = new JsonObject();
    }

    public PricingInsightsRegionProviders(JsonObject obj) {
        SOURCE = obj;
    }

    public void setMarketplace(Integer marketplace) throws ParseException {
        _marketplace = marketplace;
        SOURCE.put("marketplace", marketplace);
    }

    public Integer getMarketplace() {
        try {
            if (_marketplace == null && SOURCE.has("marketplace") && SOURCE.get("marketplace") != null)
                _marketplace = SOURCE.getInt("marketplace");
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _marketplace;
    }

    public PricingInsightsRegionProviders marketplace(Integer marketplace) throws ParseException {
        _marketplace = marketplace;
        SOURCE.put("marketplace", marketplace);
        return this;
    }

    /*-*****************************-*/
    /*-             Json            -*/
    /*-*****************************-*/
    public static JsonArray toJsonArray(PricingInsightsRegionProviders[] array) {
        JsonArray list = new JsonArray();
        for (PricingInsightsRegionProviders item : array) {
            list.add(item.getJson());
        }
        return list;
    }

    public static PricingInsightsRegionProviders[] fromJsonArray(JsonArray array) {
        PricingInsightsRegionProviders[] list = new PricingInsightsRegionProviders[array.size()];
        for (int i = 0; i < array.size(); i++) {
            list[i] = fromJson(array.getJsonObject(i));
        }
        return list;
    }

    public static PricingInsightsRegionProviders fromJson(JsonObject obj) {
        try {
            return new PricingInsightsRegionProviders(obj);
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
    public static final Parcelable.Creator<PricingInsightsRegionProviders> CREATOR = new Parcelable.Creator<PricingInsightsRegionProviders>() {

        @Override
        public PricingInsightsRegionProviders createFromParcel(Parcel source) {
            try {
                return PricingInsightsRegionProviders.fromJson((JsonObject) source.readParcelable(JsonObject.class.getClassLoader()));
            } catch (Exception ex) {
                Log.v(TAG, ex);
                return null;
            }
        }

        @Override
        public PricingInsightsRegionProviders[] newArray(int size) {
            return new PricingInsightsRegionProviders[size];
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
