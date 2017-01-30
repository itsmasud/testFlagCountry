package com.fieldnation.data.bv2.model;
import android.os.Parcel;
import android.os.Parcelable;

import com.fieldnation.fnjson.JsonObject;
import com.fieldnation.fnjson.Serializer;
import com.fieldnation.fnjson.Unserializer;
import com.fieldnation.fnjson.annotations.Json;
import com.fieldnation.fnlog.Log;

/**
 * Created by dmgen from swagger on 1/30/17.
 */

public class PayModifiers implements Parcelable {
    private static final String TAG = "PayModifiers";

    @Json(name = "metadata")
    private ListEnvelope _metadata;

    @Json(name = "sum")
    private PayModifiersSum _sum;

    @Json(name = "actions")
    private ActionsEnum[] _actions;

    @Json(name = "results")
    private PayModifier[] _results;

    public PayModifiers() {
    }

    public void setMetadata(ListEnvelope metadata) {
        _metadata = metadata;
    }

    public ListEnvelope getMetadata() {
        return _metadata;
    }

    public PayModifiers metadata(ListEnvelope metadata) {
        _metadata = metadata;
        return this;
    }

    public void setSum(PayModifiersSum sum) {
        _sum = sum;
    }

    public PayModifiersSum getSum() {
        return _sum;
    }

    public PayModifiers sum(PayModifiersSum sum) {
        _sum = sum;
        return this;
    }

    public void setActions(ActionsEnum[] actions) {
        _actions = actions;
    }

    public ActionsEnum[] getActions() {
        return _actions;
    }

    public PayModifiers actions(ActionsEnum[] actions) {
        _actions = actions;
        return this;
    }

    public void setResults(PayModifier[] results) {
        _results = results;
    }

    public PayModifier[] getResults() {
        return _results;
    }

    public PayModifiers results(PayModifier[] results) {
        _results = results;
        return this;
    }

    /*-*****************************-*/
    /*-             Json            -*/
    /*-*****************************-*/
    public static PayModifiers fromJson(JsonObject obj) {
        try {
            return Unserializer.unserializeObject(PayModifiers.class, obj);
        } catch (Exception ex) {
            Log.v(TAG, TAG, ex);
            return null;
        }
    }

    public JsonObject toJson() {
        return toJson(this);
    }

    public static JsonObject toJson(PayModifiers payModifiers) {
        try {
            return Serializer.serializeObject(payModifiers);
        } catch (Exception ex) {
            Log.v(TAG, TAG, ex);
            return null;
        }
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
        dest.writeParcelable(toJson(), flags);
    }
}
