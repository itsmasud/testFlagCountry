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

public class Problems implements Parcelable {
    private static final String TAG = "Problems";

    @Json(name = "actions")
    private ActionsEnum[] _actions;

    @Json(name = "metadata")
    private ListEnvelope _metadata;

    @Json(name = "results")
    private Problem[] _results;

    @Json(name = "sum")
    private ProblemsSum _sum;

    @Json(name = "types")
    private ProblemType[] _types;

    @Source
    private JsonObject SOURCE;

    public Problems() {
        SOURCE = new JsonObject();
    }

    public Problems(JsonObject obj) {
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

        return _actions;
    }

    public Problems actions(ActionsEnum[] actions) throws ParseException {
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

        if (_metadata != null && _metadata.isSet())
        return _metadata;

        return null;
    }

    public Problems metadata(ListEnvelope metadata) throws ParseException {
        _metadata = metadata;
        SOURCE.put("metadata", metadata.getJson());
        return this;
    }

    public void setResults(Problem[] results) throws ParseException {
        _results = results;
        SOURCE.put("results", Problem.toJsonArray(results));
    }

    public Problem[] getResults() {
        try {
            if (_results != null)
                return _results;

            if (SOURCE.has("results") && SOURCE.get("results") != null) {
                _results = Problem.fromJsonArray(SOURCE.getJsonArray("results"));
            }

        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _results;
    }

    public Problems results(Problem[] results) throws ParseException {
        _results = results;
        SOURCE.put("results", Problem.toJsonArray(results), true);
        return this;
    }

    public void setSum(ProblemsSum sum) throws ParseException {
        _sum = sum;
        SOURCE.put("sum", sum.getJson());
    }

    public ProblemsSum getSum() {
        try {
            if (_sum == null && SOURCE.has("sum") && SOURCE.get("sum") != null)
                _sum = ProblemsSum.fromJson(SOURCE.getJsonObject("sum"));
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        if (_sum != null && _sum.isSet())
        return _sum;

        return null;
    }

    public Problems sum(ProblemsSum sum) throws ParseException {
        _sum = sum;
        SOURCE.put("sum", sum.getJson());
        return this;
    }

    public void setTypes(ProblemType[] types) throws ParseException {
        _types = types;
        SOURCE.put("types", ProblemType.toJsonArray(types));
    }

    public ProblemType[] getTypes() {
        try {
            if (_types != null)
                return _types;

            if (SOURCE.has("types") && SOURCE.get("types") != null) {
                _types = ProblemType.fromJsonArray(SOURCE.getJsonArray("types"));
            }

        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _types;
    }

    public Problems types(ProblemType[] types) throws ParseException {
        _types = types;
        SOURCE.put("types", ProblemType.toJsonArray(types), true);
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
    public static JsonArray toJsonArray(Problems[] array) {
        JsonArray list = new JsonArray();
        for (Problems item : array) {
            list.add(item.getJson());
        }
        return list;
    }

    public static Problems[] fromJsonArray(JsonArray array) {
        Problems[] list = new Problems[array.size()];
        for (int i = 0; i < array.size(); i++) {
            list[i] = fromJson(array.getJsonObject(i));
        }
        return list;
    }

    public static Problems fromJson(JsonObject obj) {
        try {
            return new Problems(obj);
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
    public static final Parcelable.Creator<Problems> CREATOR = new Parcelable.Creator<Problems>() {

        @Override
        public Problems createFromParcel(Parcel source) {
            try {
                return Problems.fromJson((JsonObject) source.readParcelable(JsonObject.class.getClassLoader()));
            } catch (Exception ex) {
                Log.v(TAG, ex);
                return null;
            }
        }

        @Override
        public Problems[] newArray(int size) {
            return new Problems[size];
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
