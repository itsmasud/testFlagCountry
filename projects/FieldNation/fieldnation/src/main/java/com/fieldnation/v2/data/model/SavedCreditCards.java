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

public class SavedCreditCards implements Parcelable {
    private static final String TAG = "SavedCreditCards";

    @Json(name = "metadata")
    private ListEnvelope _metadata;

    @Json(name = "results")
    private SavedCreditCard[] _results;

    @Source
    private JsonObject SOURCE;

    public SavedCreditCards() {
        SOURCE = new JsonObject();
    }

    public SavedCreditCards(JsonObject obj) {
        SOURCE = obj;
    }

    public void setMetadata(ListEnvelope metadata) throws ParseException {
        _metadata = metadata;
        SOURCE.put("metadata", metadata.getJson());
    }

    public ListEnvelope getMetadata() {
        try {
            if (_metadata == null && SOURCE.has("metadata") && SOURCE.get("metadata") != null)
                _metadata = ListEnvelope.fromJson(SOURCE.getJsonObject("metadata"));
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        if (_metadata == null)
            _metadata = new ListEnvelope();

        return _metadata;
    }

    public SavedCreditCards metadata(ListEnvelope metadata) throws ParseException {
        _metadata = metadata;
        SOURCE.put("metadata", metadata.getJson());
        return this;
    }

    public void setResults(SavedCreditCard[] results) throws ParseException {
        _results = results;
        SOURCE.put("results", SavedCreditCard.toJsonArray(results));
    }

    public SavedCreditCard[] getResults() {
        try {
            if (_results != null)
                return _results;

            if (SOURCE.has("results") && SOURCE.get("results") != null) {
                _results = SavedCreditCard.fromJsonArray(SOURCE.getJsonArray("results"));
            }

        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        if (_results == null)
            _results = new SavedCreditCard[0];

        return _results;
    }

    public SavedCreditCards results(SavedCreditCard[] results) throws ParseException {
        _results = results;
        SOURCE.put("results", SavedCreditCard.toJsonArray(results), true);
        return this;
    }

    /*-*****************************-*/
    /*-             Json            -*/
    /*-*****************************-*/
    public static JsonArray toJsonArray(SavedCreditCards[] array) {
        JsonArray list = new JsonArray();
        for (SavedCreditCards item : array) {
            list.add(item.getJson());
        }
        return list;
    }

    public static SavedCreditCards[] fromJsonArray(JsonArray array) {
        SavedCreditCards[] list = new SavedCreditCards[array.size()];
        for (int i = 0; i < array.size(); i++) {
            list[i] = fromJson(array.getJsonObject(i));
        }
        return list;
    }

    public static SavedCreditCards fromJson(JsonObject obj) {
        try {
            return new SavedCreditCards(obj);
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
    public static final Parcelable.Creator<SavedCreditCards> CREATOR = new Parcelable.Creator<SavedCreditCards>() {

        @Override
        public SavedCreditCards createFromParcel(Parcel source) {
            try {
                return SavedCreditCards.fromJson((JsonObject) source.readParcelable(JsonObject.class.getClassLoader()));
            } catch (Exception ex) {
                Log.v(TAG, ex);
                return null;
            }
        }

        @Override
        public SavedCreditCards[] newArray(int size) {
            return new SavedCreditCards[size];
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
