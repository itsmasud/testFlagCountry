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

public class Fee implements Parcelable {
    private static final String TAG = "Fee";

    @Json(name = "amount")
    private Double _amount;

    @Json(name = "calculation")
    private CalculationEnum _calculation;

    @Json(name = "charged")
    private Boolean _charged;

    @Json(name = "id")
    private Integer _id;

    @Json(name = "modifier")
    private Double _modifier;

    @Source
    private JsonObject SOURCE = new JsonObject();

    public Fee() {
    }

    public void setAmount(Double amount) throws ParseException {
        _amount = amount;
        SOURCE.put("amount", amount);
    }

    public Double getAmount() {
        return _amount;
    }

    public Fee amount(Double amount) throws ParseException {
        _amount = amount;
        SOURCE.put("amount", amount);
        return this;
    }

    public void setCalculation(CalculationEnum calculation) throws ParseException {
        _calculation = calculation;
        SOURCE.put("calculation", calculation.toString());
    }

    public CalculationEnum getCalculation() {
        return _calculation;
    }

    public Fee calculation(CalculationEnum calculation) throws ParseException {
        _calculation = calculation;
        SOURCE.put("calculation", calculation.toString());
        return this;
    }

    public void setCharged(Boolean charged) throws ParseException {
        _charged = charged;
        SOURCE.put("charged", charged);
    }

    public Boolean getCharged() {
        return _charged;
    }

    public Fee charged(Boolean charged) throws ParseException {
        _charged = charged;
        SOURCE.put("charged", charged);
        return this;
    }

    public void setId(Integer id) throws ParseException {
        _id = id;
        SOURCE.put("id", id);
    }

    public Integer getId() {
        return _id;
    }

    public Fee id(Integer id) throws ParseException {
        _id = id;
        SOURCE.put("id", id);
        return this;
    }

    public void setModifier(Double modifier) throws ParseException {
        _modifier = modifier;
        SOURCE.put("modifier", modifier);
    }

    public Double getModifier() {
        return _modifier;
    }

    public Fee modifier(Double modifier) throws ParseException {
        _modifier = modifier;
        SOURCE.put("modifier", modifier);
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
    public static JsonArray toJsonArray(Fee[] array) {
        JsonArray list = new JsonArray();
        for (Fee item : array) {
            list.add(item.getJson());
        }
        return list;
    }

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

    public JsonObject getJson() {
        return SOURCE;
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
        dest.writeParcelable(getJson(), flags);
    }
}
