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

public class Messages implements Parcelable {
    private static final String TAG = "Messages";

    @Json(name = "actions")
    private ActionsEnum[] _actions;

    @Json(name = "metadata")
    private ListEnvelope _metadata;

    @Json(name = "problem_reported")
    private Boolean _problemReported;

    @Json(name = "results")
    private Message _results;

    @Json(name = "sum")
    private Integer _sum;

    @Source
    private JsonObject SOURCE = new JsonObject();

    public Messages() {
    }

    public void setActions(ActionsEnum[] actions) throws ParseException {
        _actions = actions;
        JsonArray ja = new JsonArray();
        for (ActionsEnum item : actions) {
            ja.add(item.toString());
        }
        SOURCE.put("actions", ja);
    }

    public ActionsEnum[] getActions() {
        return _actions;
    }

    public Messages actions(ActionsEnum[] actions) throws ParseException {
        _actions = actions;
        JsonArray ja = new JsonArray();
        for (ActionsEnum item : actions) {
            ja.add(item.toString());
        }
        SOURCE.put("actions", ja, true);
        return this;
    }

    public void setMetadata(ListEnvelope metadata) throws ParseException {
        _metadata = metadata;
        SOURCE.put("metadata", metadata.getJson());
    }

    public ListEnvelope getMetadata() {
        return _metadata;
    }

    public Messages metadata(ListEnvelope metadata) throws ParseException {
        _metadata = metadata;
        SOURCE.put("metadata", metadata.getJson());
        return this;
    }

    public void setProblemReported(Boolean problemReported) throws ParseException {
        _problemReported = problemReported;
        SOURCE.put("problem_reported", problemReported);
    }

    public Boolean getProblemReported() {
        return _problemReported;
    }

    public Messages problemReported(Boolean problemReported) throws ParseException {
        _problemReported = problemReported;
        SOURCE.put("problem_reported", problemReported);
        return this;
    }

    public void setResults(Message results) throws ParseException {
        _results = results;
        SOURCE.put("results", results.getJson());
    }

    public Message getResults() {
        return _results;
    }

    public Messages results(Message results) throws ParseException {
        _results = results;
        SOURCE.put("results", results.getJson());
        return this;
    }

    public void setSum(Integer sum) throws ParseException {
        _sum = sum;
        SOURCE.put("sum", sum);
    }

    public Integer getSum() {
        return _sum;
    }

    public Messages sum(Integer sum) throws ParseException {
        _sum = sum;
        SOURCE.put("sum", sum);
        return this;
    }

    /*-******************************-*/
    /*-             Enums            -*/
    /*-******************************-*/
    public enum ActionsEnum {
        @Json(name = "add")
        ADD("add");

        private String value;

        ActionsEnum(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return String.valueOf(value);
        }
    }

    /*-*****************************-*/
    /*-             Json            -*/
    /*-*****************************-*/
    public static JsonArray toJsonArray(Messages[] array) {
        JsonArray list = new JsonArray();
        for (Messages item : array) {
            list.add(item.getJson());
        }
        return list;
    }

    public static Messages[] fromJsonArray(JsonArray array) {
        Messages[] list = new Messages[array.size()];
        for (int i = 0; i < array.size(); i++) {
            list[i] = fromJson(array.getJsonObject(i));
        }
        return list;
    }

    public static Messages fromJson(JsonObject obj) {
        try {
            return Unserializer.unserializeObject(Messages.class, obj);
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
    public static final Parcelable.Creator<Messages> CREATOR = new Parcelable.Creator<Messages>() {

        @Override
        public Messages createFromParcel(Parcel source) {
            try {
                return Messages.fromJson((JsonObject) source.readParcelable(JsonObject.class.getClassLoader()));
            } catch (Exception ex) {
                Log.v(TAG, ex);
                return null;
            }
        }

        @Override
        public Messages[] newArray(int size) {
            return new Messages[size];
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
