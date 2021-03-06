package com.fieldnation.v2.data.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.fieldnation.fnjson.JsonArray;
import com.fieldnation.fnjson.JsonObject;
import com.fieldnation.fnjson.annotations.Json;
import com.fieldnation.fnjson.annotations.Source;
import com.fieldnation.fnlog.Log;

import java.text.ParseException;

/**
 * Created by dmgen from swagger.
 */

public class Holds implements Parcelable {
    private static final String TAG = "Holds";

    @Json(name = "actions")
    private String[] _actions;

    @Json(name = "metadata")
    private ListEnvelope _metadata;

    @Json(name = "results")
    private Hold[] _results;

    @Source
    private JsonObject SOURCE;

    public Holds() {
        SOURCE = new JsonObject();
    }

    public Holds(JsonObject obj) {
        SOURCE = obj;
    }

    public void setActions(String[] actions) throws ParseException {
        _actions = actions;
        JsonArray ja = new JsonArray();
        for (String item : actions) {
            ja.add(item);
        }
        SOURCE.put("actions", ja);
    }

    public String[] getActions() {
        try {
            if (_actions != null)
                return _actions;

            if (SOURCE.has("actions") && SOURCE.get("actions") != null) {
                JsonArray ja = SOURCE.getJsonArray("actions");
                _actions = ja.toArray(new String[ja.size()]);
            }

        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        if (_actions == null)
            _actions = new String[0];

        return _actions;
    }

    public Holds actions(String[] actions) throws ParseException {
        _actions = actions;
        JsonArray ja = new JsonArray();
        for (String item : actions) {
            ja.add(item);
        }
        SOURCE.put("actions", ja, true);
        return this;
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

    public Holds metadata(ListEnvelope metadata) throws ParseException {
        _metadata = metadata;
        SOURCE.put("metadata", metadata.getJson());
        return this;
    }

    public void setResults(Hold[] results) throws ParseException {
        _results = results;
        SOURCE.put("results", Hold.toJsonArray(results));
    }

    public Hold[] getResults() {
        try {
            if (_results != null)
                return _results;

            if (SOURCE.has("results") && SOURCE.get("results") != null) {
                _results = Hold.fromJsonArray(SOURCE.getJsonArray("results"));
            }

        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        if (_results == null)
            _results = new Hold[0];

        return _results;
    }

    public Holds results(Hold[] results) throws ParseException {
        _results = results;
        SOURCE.put("results", Hold.toJsonArray(results), true);
        return this;
    }

    /*-*****************************-*/
    /*-             Json            -*/
    /*-*****************************-*/
    public static JsonArray toJsonArray(Holds[] array) {
        JsonArray list = new JsonArray();
        for (Holds item : array) {
            list.add(item.getJson());
        }
        return list;
    }

    public static Holds[] fromJsonArray(JsonArray array) {
        Holds[] list = new Holds[array.size()];
        for (int i = 0; i < array.size(); i++) {
            list[i] = fromJson(array.getJsonObject(i));
        }
        return list;
    }

    public static Holds fromJson(JsonObject obj) {
        try {
            return new Holds(obj);
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
    public static final Parcelable.Creator<Holds> CREATOR = new Parcelable.Creator<Holds>() {

        @Override
        public Holds createFromParcel(Parcel source) {
            try {
                return Holds.fromJson((JsonObject) source.readParcelable(JsonObject.class.getClassLoader()));
            } catch (Exception ex) {
                Log.v(TAG, ex);
                return null;
            }
        }

        @Override
        public Holds[] newArray(int size) {
            return new Holds[size];
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

    public boolean isOnHold() {
        if (getResults().length == 0)
            return false;

        return getResults().length > 0;
    }

    public boolean areHoldsAcknowledged() {
        if (getResults().length == 0)
            return true;

        Hold[] holds = getResults();
        for (Hold hold : holds) {
            if (hold.getAcknowledgment().getStatus() != Acknowledgment.StatusEnum.ACKNOWLEDGED)
                return false;
        }

        return true;
    }

    public Hold getUnAcknowledgedHold() {
        if (getResults().length == 0)
            return null;

        Hold[] holds = getResults();
        for (Hold hold : holds) {
            if (hold.getAcknowledgment().getStatus() != Acknowledgment.StatusEnum.ACKNOWLEDGED)
                return hold;
        }

        return null;
    }
}
