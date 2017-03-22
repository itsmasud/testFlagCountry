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

public class PricingInsights implements Parcelable {
    private static final String TAG = "PricingInsights";

    @Json(name = "region")
    private PricingInsightsRegion _region;

    @Source
    private JsonObject SOURCE;

    public PricingInsights() {
        SOURCE = new JsonObject();
    }

    public PricingInsights(JsonObject obj) {
        SOURCE = obj;
    }

    public void setRegion(PricingInsightsRegion region) throws ParseException {
        _region = region;
        SOURCE.put("region", region.getJson());
    }

    public PricingInsightsRegion getRegion() {
        try {
            if (_region == null && SOURCE.has("region") && SOURCE.get("region") != null)
                _region = PricingInsightsRegion.fromJson(SOURCE.getJsonObject("region"));
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        if (_region != null && _region.isSet())
        return _region;

        return null;
    }

    public PricingInsights region(PricingInsightsRegion region) throws ParseException {
        _region = region;
        SOURCE.put("region", region.getJson());
        return this;
    }

    /*-*****************************-*/
    /*-             Json            -*/
    /*-*****************************-*/
    public static JsonArray toJsonArray(PricingInsights[] array) {
        JsonArray list = new JsonArray();
        for (PricingInsights item : array) {
            list.add(item.getJson());
        }
        return list;
    }

    public static PricingInsights[] fromJsonArray(JsonArray array) {
        PricingInsights[] list = new PricingInsights[array.size()];
        for (int i = 0; i < array.size(); i++) {
            list[i] = fromJson(array.getJsonObject(i));
        }
        return list;
    }

    public static PricingInsights fromJson(JsonObject obj) {
        try {
            return new PricingInsights(obj);
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
    public static final Parcelable.Creator<PricingInsights> CREATOR = new Parcelable.Creator<PricingInsights>() {

        @Override
        public PricingInsights createFromParcel(Parcel source) {
            try {
                return PricingInsights.fromJson((JsonObject) source.readParcelable(JsonObject.class.getClassLoader()));
            } catch (Exception ex) {
                Log.v(TAG, ex);
                return null;
            }
        }

        @Override
        public PricingInsights[] newArray(int size) {
            return new PricingInsights[size];
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
