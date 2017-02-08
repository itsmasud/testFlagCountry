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

public class Fee implements Parcelable {
    private static final String TAG = "Fee";

    @Json(name = "calculation")
    private CalculationEnum _calculation;

    @Json(name = "amount")
    private Double _amount;

    @Json(name = "modifier")
    private Double _modifier;

    @Json(name = "id")
    private Integer _id;

    @Json(name = "charged")
    private Boolean _charged;

    public Fee() {
    }

    public void setCalculation(CalculationEnum calculation) {
        _calculation = calculation;
    }

    public CalculationEnum getCalculation() {
        return _calculation;
    }

    public Fee calculation(CalculationEnum calculation) {
        _calculation = calculation;
        return this;
    }

    public void setAmount(Double amount) {
        _amount = amount;
    }

    public Double getAmount() {
        return _amount;
    }

    public Fee amount(Double amount) {
        _amount = amount;
        return this;
    }

    public void setModifier(Double modifier) {
        _modifier = modifier;
    }

    public Double getModifier() {
        return _modifier;
    }

    public Fee modifier(Double modifier) {
        _modifier = modifier;
        return this;
    }

    public void setId(Integer id) {
        _id = id;
    }

    public Integer getId() {
        return _id;
    }

    public Fee id(Integer id) {
        _id = id;
        return this;
    }

    public void setCharged(Boolean charged) {
        _charged = charged;
    }

    public Boolean getCharged() {
        return _charged;
    }

    public Fee charged(Boolean charged) {
        _charged = charged;
        return this;
    }

    /*-******************************-*/
    /*-             Enums            -*/
    /*-******************************-*/
    public enum CalculationEnum {
        @Json(name = "fixed")
        FIXED("fixed"),
        @Json(name = "percent")
        PERCENT("percent");

        private String value;

        CalculationEnum(String value) {
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
    public static Fee[] fromJsonArray(JsonArray array) {
        Fee[] list = new Fee[array.size()];
        for (int i = 0; i < array.size(); i++) {
            list[i] = fromJson(array.getJsonObject(i));
        }
        return list;
    }

    public static Fee fromJson(JsonObject obj) {
        try {
            return Unserializer.unserializeObject(Fee.class, obj);
        } catch (Exception ex) {
            Log.v(TAG, TAG, ex);
            return null;
        }
    }

    public JsonObject toJson() {
        return toJson(this);
    }

    public static JsonObject toJson(Fee fee) {
        try {
            return Serializer.serializeObject(fee);
        } catch (Exception ex) {
            Log.v(TAG, TAG, ex);
            return null;
        }
    }

    /*-*********************************************-*/
    /*-			Parcelable Implementation           -*/
    /*-*********************************************-*/
    public static final Parcelable.Creator<Fee> CREATOR = new Parcelable.Creator<Fee>() {

        @Override
        public Fee createFromParcel(Parcel source) {
            try {
                return Fee.fromJson((JsonObject) source.readParcelable(JsonObject.class.getClassLoader()));
            } catch (Exception ex) {
                Log.v(TAG, ex);
                return null;
            }
        }

        @Override
        public Fee[] newArray(int size) {
            return new Fee[size];
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
