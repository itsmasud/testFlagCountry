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

public class TypesOfWork implements Parcelable {
    private static final String TAG = "TypesOfWork";

    @Json(name = "metadata")
    private ListEnvelope _metadata;

    @Json(name = "results")
    private TypeOfWork[] _results;

    @Source
    private JsonObject SOURCE;

    public TypesOfWork() {
        SOURCE = new JsonObject();
    }

    public TypesOfWork(JsonObject obj) {
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

    public TypesOfWork metadata(ListEnvelope metadata) throws ParseException {
        _metadata = metadata;
        SOURCE.put("metadata", metadata.getJson());
        return this;
    }

    public void setResults(TypeOfWork[] results) throws ParseException {
        _results = results;
        SOURCE.put("results", TypeOfWork.toJsonArray(results));
    }

    public TypeOfWork[] getResults() {
        try {
            if (_results != null)
                return _results;

            if (SOURCE.has("results") && SOURCE.get("results") != null) {
                _results = TypeOfWork.fromJsonArray(SOURCE.getJsonArray("results"));
            }

        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        if (_results == null)
            _results = new TypeOfWork[0];

        return _results;
    }

    public TypesOfWork results(TypeOfWork[] results) throws ParseException {
        _results = results;
        SOURCE.put("results", TypeOfWork.toJsonArray(results), true);
        return this;
    }

    /*-*****************************-*/
    /*-             Json            -*/
    /*-*****************************-*/
    public static JsonArray toJsonArray(TypesOfWork[] array) {
        JsonArray list = new JsonArray();
        for (TypesOfWork item : array) {
            list.add(item.getJson());
        }
        return list;
    }

    public static TypesOfWork[] fromJsonArray(JsonArray array) {
        TypesOfWork[] list = new TypesOfWork[array.size()];
        for (int i = 0; i < array.size(); i++) {
            list[i] = fromJson(array.getJsonObject(i));
        }
        return list;
    }

    public static TypesOfWork fromJson(JsonObject obj) {
        try {
            return new TypesOfWork(obj);
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
    public static final Parcelable.Creator<TypesOfWork> CREATOR = new Parcelable.Creator<TypesOfWork>() {

        @Override
        public TypesOfWork createFromParcel(Parcel source) {
            try {
                return TypesOfWork.fromJson((JsonObject) source.readParcelable(JsonObject.class.getClassLoader()));
            } catch (Exception ex) {
                Log.v(TAG, ex);
                return null;
            }
        }

        @Override
        public TypesOfWork[] newArray(int size) {
            return new TypesOfWork[size];
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
