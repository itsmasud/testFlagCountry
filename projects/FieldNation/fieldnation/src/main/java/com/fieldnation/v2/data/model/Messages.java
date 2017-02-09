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

    public Messages() {
    }

    public void setActions(ActionsEnum[] actions) {
        _actions = actions;
    }

    public ActionsEnum[] getActions() {
        return _actions;
    }

    public Messages actions(ActionsEnum[] actions) {
        _actions = actions;
        return this;
    }

    public void setMetadata(ListEnvelope metadata) {
        _metadata = metadata;
    }

    public ListEnvelope getMetadata() {
        return _metadata;
    }

    public Messages metadata(ListEnvelope metadata) {
        _metadata = metadata;
        return this;
    }

    public void setProblemReported(Boolean problemReported) {
        _problemReported = problemReported;
    }

    public Boolean getProblemReported() {
        return _problemReported;
    }

    public Messages problemReported(Boolean problemReported) {
        _problemReported = problemReported;
        return this;
    }

    public void setResults(Message results) {
        _results = results;
    }

    public Message getResults() {
        return _results;
    }

    public Messages results(Message results) {
        _results = results;
        return this;
    }

    public void setSum(Integer sum) {
        _sum = sum;
    }

    public Integer getSum() {
        return _sum;
    }

    public Messages sum(Integer sum) {
        _sum = sum;
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

    public JsonObject toJson() {
        return toJson(this);
    }

    public static JsonObject toJson(Messages messages) {
        try {
            return Serializer.serializeObject(messages);
        } catch (Exception ex) {
            Log.v(TAG, TAG, ex);
            return null;
        }
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
        dest.writeParcelable(toJson(), flags);
    }
}
