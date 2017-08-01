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

public class Messages implements Parcelable {
    private static final String TAG = "Messages";

    @Json(name = "actions")
    private ActionsEnum[] _actions;

    @Json(name = "metadata")
    private ListEnvelope _metadata;

    @Json(name = "problem_reported")
    private Boolean _problemReported;

    @Json(name = "results")
    private Message[] _results;

    @Json(name = "sum")
    private Integer _sum;

    @Json(name = "unread")
    private Integer _unread;

    @Source
    private JsonObject SOURCE;

    public Messages() {
        SOURCE = new JsonObject();
    }

    public Messages(JsonObject obj) {
        SOURCE = obj;
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
        try {
            if (_actions != null)
                return _actions;

            if (SOURCE.has("actions") && SOURCE.get("actions") != null) {
                _actions = ActionsEnum.fromJsonArray(SOURCE.getJsonArray("actions"));
            }

        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        if (_actions == null)
            _actions = new ActionsEnum[0];

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
        try {
            if (_problemReported == null && SOURCE.has("problem_reported") && SOURCE.get("problem_reported") != null)
                _problemReported = SOURCE.getBoolean("problem_reported");
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _problemReported;
    }

    public Messages problemReported(Boolean problemReported) throws ParseException {
        _problemReported = problemReported;
        SOURCE.put("problem_reported", problemReported);
        return this;
    }

    public void setResults(Message[] results) throws ParseException {
        _results = results;
        SOURCE.put("results", Message.toJsonArray(results));
    }

    public Message[] getResults() {
        try {
            if (_results != null)
                return _results;

            if (SOURCE.has("results") && SOURCE.get("results") != null) {
                _results = Message.fromJsonArray(SOURCE.getJsonArray("results"));
            }

        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        if (_results == null)
            _results = new Message[0];

        return _results;
    }

    public Messages results(Message[] results) throws ParseException {
        _results = results;
        SOURCE.put("results", Message.toJsonArray(results), true);
        return this;
    }

    public void setSum(Integer sum) throws ParseException {
        _sum = sum;
        SOURCE.put("sum", sum);
    }

    public Integer getSum() {
        try {
            if (_sum == null && SOURCE.has("sum") && SOURCE.get("sum") != null)
                _sum = SOURCE.getInt("sum");
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _sum;
    }

    public Messages sum(Integer sum) throws ParseException {
        _sum = sum;
        SOURCE.put("sum", sum);
        return this;
    }

    public void setUnread(Integer unread) throws ParseException {
        _unread = unread;
        SOURCE.put("unread", unread);
    }

    public Integer getUnread() {
        try {
            if (_unread == null && SOURCE.has("unread") && SOURCE.get("unread") != null)
                _unread = SOURCE.getInt("unread");
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _unread;
    }

    public Messages unread(Integer unread) throws ParseException {
        _unread = unread;
        SOURCE.put("unread", unread);
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

        public static ActionsEnum fromString(String value) {
            ActionsEnum[] values = values();
            for (ActionsEnum v : values) {
                if (v.value.equals(value))
                    return v;
            }
            return null;
        }

        public static ActionsEnum[] fromJsonArray(JsonArray jsonArray) {
            ActionsEnum[] list = new ActionsEnum[jsonArray.size()];
            for (int i = 0; i < list.length; i++) {
                list[i] = fromString(jsonArray.getString(i));
            }
            return list;
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
            return new Messages(obj);
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

    /*-*****************************-*/
    /*-         Human Code          -*/
    /*-*****************************-*/

}
