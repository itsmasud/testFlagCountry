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

public class CompanyIntegrations implements Parcelable {
    private static final String TAG = "CompanyIntegrations";

    @Json(name = "envelope")
    private ListEnvelope _envelope;

    @Json(name = "results")
    private CompanyIntegration[] _results;

    @Source
    private JsonObject SOURCE = new JsonObject();

    public CompanyIntegrations() {
    }

    public void setEnvelope(ListEnvelope envelope) throws ParseException {
        _envelope = envelope;
        SOURCE.put("envelope", envelope.getJson());
    }

    public ListEnvelope getEnvelope() {
        return _envelope;
    }

    public CompanyIntegrations envelope(ListEnvelope envelope) throws ParseException {
        _envelope = envelope;
        SOURCE.put("envelope", envelope.getJson());
        return this;
    }

    public void setResults(CompanyIntegration[] results) throws ParseException {
        _results = results;
        SOURCE.put("results", CompanyIntegration.toJsonArray(results));
    }

    public CompanyIntegration[] getResults() {
        return _results;
    }

    public CompanyIntegrations results(CompanyIntegration[] results) throws ParseException {
        _results = results;
        SOURCE.put("results", CompanyIntegration.toJsonArray(results), true);
        return this;
    }

    /*-*****************************-*/
    /*-             Json            -*/
    /*-*****************************-*/
    public static JsonArray toJsonArray(CompanyIntegrations[] array) {
        JsonArray list = new JsonArray();
        for (CompanyIntegrations item : array) {
            list.add(item.getJson());
        }
        return list;
    }

    public static CompanyIntegrations[] fromJsonArray(JsonArray array) {
        CompanyIntegrations[] list = new CompanyIntegrations[array.size()];
        for (int i = 0; i < array.size(); i++) {
            list[i] = fromJson(array.getJsonObject(i));
        }
        return list;
    }

    public static CompanyIntegrations fromJson(JsonObject obj) {
        try {
            return Unserializer.unserializeObject(CompanyIntegrations.class, obj);
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
    public static final Parcelable.Creator<CompanyIntegrations> CREATOR = new Parcelable.Creator<CompanyIntegrations>() {

        @Override
        public CompanyIntegrations createFromParcel(Parcel source) {
            try {
                return CompanyIntegrations.fromJson((JsonObject) source.readParcelable(JsonObject.class.getClassLoader()));
            } catch (Exception ex) {
                Log.v(TAG, ex);
                return null;
            }
        }

        @Override
        public CompanyIntegrations[] newArray(int size) {
            return new CompanyIntegrations[size];
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
