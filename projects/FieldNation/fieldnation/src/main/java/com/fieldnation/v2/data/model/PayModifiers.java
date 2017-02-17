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

public class PayModifiers implements Parcelable {
    private static final String TAG = "PayModifiers";

    @Json(name = "actions")
    private ActionsEnum[] _actions;

    @Json(name = "metadata")
    private ListEnvelope _metadata;

    @Json(name = "results")
    private PayModifier[] _results;

    @Json(name = "sum")
    private PayModifiersSum _sum;

    @Source
    private JsonObject SOURCE = new JsonObject();

    public PayModifiers() {
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

    public PayModifiers actions(ActionsEnum[] actions) throws ParseException {
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

    public PayModifiers metadata(ListEnvelope metadata) throws ParseException {
        _metadata = metadata;
        SOURCE.put("metadata", metadata.getJson());
        return this;
    }

    public void setResults(PayModifier[] results) throws ParseException {
        _results = results;
        SOURCE.put("results", PayModifier.toJsonArray(results));
    }

    public PayModifier[] getResults() {
        return _results;
    }

    public PayModifiers results(PayModifier[] results) throws ParseException {
        _results = results;
        SOURCE.put("results", PayModifier.toJsonArray(results), true);
        return this;
    }

    public void setSum(PayModifiersSum sum) throws ParseException {
        _sum = sum;
        SOURCE.put("sum", sum.getJson());
    }

    public PayModifiersSum getSum() {
        return _sum;
    }

    public PayModifiers sum(PayModifiersSum sum) throws ParseException {
        _sum = sum;
        SOURCE.put("sum", sum.getJson());
        return this;
    }

    /*-******************************-*/
    /*-             Enums            -*/
    /*-******************************-*/
    public enum ActionsEnum {
        @Json(name = "edit")
        EDIT("edit");

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
    public static JsonArray toJsonArray(PayModifiers[] array) {
        JsonArray list = new JsonArray();
        for (PayModifiers item : array) {
            list.add(item.getJson());
        }
        return list;
    }

    public static PayModifiers[] fromJsonArray(JsonArray array) {
        PayModifiers[] list = new PayModifiers[array.size()];
        for (int i = 0; i < array.size(); i++) {
            list[i] = fromJson(array.getJsonObject(i));
        }
        return list;
    }

    public static PayModifiers fromJson(JsonObject obj) {
        try {
            return Unserializer.unserializeObject(PayModifiers.class, obj);
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
    public static final Parcelable.Creator<PayModifiers> CREATOR = new Parcelable.Creator<PayModifiers>() {

        @Override
        public PayModifiers createFromParcel(Parcel source) {
            try {
                return PayModifiers.fromJson((JsonObject) source.readParcelable(JsonObject.class.getClassLoader()));
            } catch (Exception ex) {
                Log.v(TAG, ex);
                return null;
            }
        }

        @Override
        public PayModifiers[] newArray(int size) {
            return new PayModifiers[size];
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
