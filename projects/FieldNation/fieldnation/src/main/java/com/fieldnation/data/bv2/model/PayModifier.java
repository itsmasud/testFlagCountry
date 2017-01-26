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

public class PayModifier implements Parcelable {
    private static final String TAG = "PayModifier";

    @Json(name = "calculation")
    private CalculationEnum _calculation;

    @Json(name = "amount")
    private Double _amount;

    @Json(name = "modifier")
    private Double _modifier;

    @Json(name = "name")
    private String _name;

    @Json(name = "description")
    private String _description;

    @Json(name = "id")
    private Integer _id;

    @Json(name = "charged")
    private Boolean _charged;

    @Json(name = "actions")
    private ActionsEnum[] _actions;

    public PayModifier() {
    }

    public void setCalculation(CalculationEnum calculation) {
        _calculation = calculation;
    }

    public CalculationEnum getCalculation() {
        return _calculation;
    }

    public PayModifier calculation(CalculationEnum calculation) {
        _calculation = calculation;
        return this;
    }

    public void setAmount(Double amount) {
        _amount = amount;
    }

    public Double getAmount() {
        return _amount;
    }

    public PayModifier amount(Double amount) {
        _amount = amount;
        return this;
    }

    public void setModifier(Double modifier) {
        _modifier = modifier;
    }

    public Double getModifier() {
        return _modifier;
    }

    public PayModifier modifier(Double modifier) {
        _modifier = modifier;
        return this;
    }

    public void setName(String name) {
        _name = name;
    }

    public String getName() {
        return _name;
    }

    public PayModifier name(String name) {
        _name = name;
        return this;
    }

    public void setDescription(String description) {
        _description = description;
    }

    public String getDescription() {
        return _description;
    }

    public PayModifier description(String description) {
        _description = description;
        return this;
    }

    public void setId(Integer id) {
        _id = id;
    }

    public Integer getId() {
        return _id;
    }

    public PayModifier id(Integer id) {
        _id = id;
        return this;
    }

    public void setCharged(Boolean charged) {
        _charged = charged;
    }

    public Boolean getCharged() {
        return _charged;
    }

    public PayModifier charged(Boolean charged) {
        _charged = charged;
        return this;
    }

    public void setActions(ActionsEnum[] actions) {
        _actions = actions;
    }

    public ActionsEnum[] getActions() {
        return _actions;
    }

    public PayModifier actions(ActionsEnum[] actions) {
        _actions = actions;
        return this;
    }

    /*-*****************************-*/
    /*-             Json            -*/
    /*-*****************************-*/
    public static PayModifier fromJson(JsonObject obj) {
        try {
            return Unserializer.unserializeObject(PayModifier.class, obj);
        } catch (Exception ex) {
            Log.v(TAG, TAG, ex);
            return null;
        }
    }

    public JsonObject toJson() {
        return toJson(this);
    }

    public static JsonObject toJson(PayModifier payModifier) {
        try {
            return Serializer.serializeObject(payModifier);
        } catch (Exception ex) {
            Log.v(TAG, TAG, ex);
            return null;
        }
    }

    /*-*********************************************-*/
    /*-			Parcelable Implementation           -*/
    /*-*********************************************-*/
    public static final Parcelable.Creator<PayModifier> CREATOR = new Parcelable.Creator<PayModifier>() {

        @Override
        public PayModifier createFromParcel(Parcel source) {
            try {
                return PayModifier.fromJson((JsonObject) source.readParcelable(JsonObject.class.getClassLoader()));
            } catch (Exception ex) {
                Log.v(TAG, ex);
                return null;
            }
        }

        @Override
        public PayModifier[] newArray(int size) {
            return new PayModifier[size];
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
