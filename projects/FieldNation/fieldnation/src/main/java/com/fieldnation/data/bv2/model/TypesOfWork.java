package com.fieldnation.data.bv2.model;
import android.os.Parcel;
import android.os.Parcelable;

import com.fieldnation.fnjson.JsonObject;
import com.fieldnation.fnjson.Serializer;
import com.fieldnation.fnjson.Unserializer;
import com.fieldnation.fnjson.annotations.Json;
import com.fieldnation.fnlog.Log;

/**
 * Created by dmgen from swagger on 1/26/17.
 */

public class TypesOfWork implements Parcelable {
    private static final String TAG = "TypesOfWork";

    @Json(name = "metadata")
    private ListEnvelope _metadata;

    @Json(name = "results")
    private TypeOfWork[] _results;

    public TypesOfWork() {
    }

    public void setMetadata(ListEnvelope metadata) {
        _metadata = metadata;
    }

    public ListEnvelope getMetadata() {
        return _metadata;
    }

    public TypesOfWork metadata(ListEnvelope metadata) {
        _metadata = metadata;
        return this;
    }

    public void setResults(TypeOfWork[] results) {
        _results = results;
    }

    public TypeOfWork[] getResults() {
        return _results;
    }

    public TypesOfWork results(TypeOfWork[] results) {
        _results = results;
        return this;
    }

    /*-*****************************-*/
    /*-             Json            -*/
    /*-*****************************-*/
    public static TypesOfWork fromJson(JsonObject obj) {
        try {
            return Unserializer.unserializeObject(TypesOfWork.class, obj);
        } catch (Exception ex) {
            Log.v(TAG, TAG, ex);
            return null;
        }
    }

    public JsonObject toJson() {
        return toJson(this);
    }

    public static JsonObject toJson(TypesOfWork typesOfWork) {
        try {
            return Serializer.serializeObject(typesOfWork);
        } catch (Exception ex) {
            Log.v(TAG, TAG, ex);
            return null;
        }
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
        dest.writeParcelable(toJson(), flags);
    }
}
