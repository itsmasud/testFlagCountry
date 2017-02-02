package com.fieldnation.v2.data.model;
import android.os.Parcel;
import android.os.Parcelable;

import com.fieldnation.fnjson.JsonArray;
import com.fieldnation.fnjson.JsonObject;
import com.fieldnation.fnjson.Serializer;
import com.fieldnation.fnjson.Unserializer;
import com.fieldnation.fnjson.annotations.Json;
import com.fieldnation.fnlog.Log;

/**
 * Created by dmgen from swagger on 1/31/17.
 */

public class Contacts implements Parcelable {
    private static final String TAG = "Contacts";

    @Json(name = "metadata")
    private ListEnvelope _metadata;

    @Json(name = "correlation_id")
    private String _correlationId;

    @Json(name = "results")
    private Contact[] _results;

    @Json(name = "actions")
    private ActionsEnum[] _actions;

    public Contacts() {
    }

    public void setMetadata(ListEnvelope metadata) {
        _metadata = metadata;
    }

    public ListEnvelope getMetadata() {
        return _metadata;
    }

    public Contacts metadata(ListEnvelope metadata) {
        _metadata = metadata;
        return this;
    }

    public void setCorrelationId(String correlationId) {
        _correlationId = correlationId;
    }

    public String getCorrelationId() {
        return _correlationId;
    }

    public Contacts correlationId(String correlationId) {
        _correlationId = correlationId;
        return this;
    }

    public void setResults(Contact[] results) {
        _results = results;
    }

    public Contact[] getResults() {
        return _results;
    }

    public Contacts results(Contact[] results) {
        _results = results;
        return this;
    }

    public void setActions(ActionsEnum[] actions) {
        _actions = actions;
    }

    public ActionsEnum[] getActions() {
        return _actions;
    }

    public Contacts actions(ActionsEnum[] actions) {
        _actions = actions;
        return this;
    }

    /*-*****************************-*/
    /*-             Json            -*/
    /*-*****************************-*/
    public static Contacts[] fromJsonArray(JsonArray array) {
        Contacts[] list = new Contacts[array.size()];
        for (int i = 0; i < array.size(); i++) {
            list[i] = fromJson(array.getJsonObject(i));
        }
        return list;
    }

    public static Contacts fromJson(JsonObject obj) {
        try {
            return Unserializer.unserializeObject(Contacts.class, obj);
        } catch (Exception ex) {
            Log.v(TAG, TAG, ex);
            return null;
        }
    }

    public JsonObject toJson() {
        return toJson(this);
    }

    public static JsonObject toJson(Contacts contacts) {
        try {
            return Serializer.serializeObject(contacts);
        } catch (Exception ex) {
            Log.v(TAG, TAG, ex);
            return null;
        }
    }

    /*-*********************************************-*/
    /*-			Parcelable Implementation           -*/
    /*-*********************************************-*/
    public static final Parcelable.Creator<Contacts> CREATOR = new Parcelable.Creator<Contacts>() {

        @Override
        public Contacts createFromParcel(Parcel source) {
            try {
                return Contacts.fromJson((JsonObject) source.readParcelable(JsonObject.class.getClassLoader()));
            } catch (Exception ex) {
                Log.v(TAG, ex);
                return null;
            }
        }

        @Override
        public Contacts[] newArray(int size) {
            return new Contacts[size];
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
