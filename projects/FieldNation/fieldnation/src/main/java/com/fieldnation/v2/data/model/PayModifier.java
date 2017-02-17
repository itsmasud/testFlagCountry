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

public class PayModifier implements Parcelable {
    private static final String TAG = "PayModifier";

    @Json(name = "actions")
    private ActionsEnum[] _actions;

    @Json(name = "amount")
    private Double _amount;

    @Json(name = "calculation")
    private CalculationEnum _calculation;

    @Json(name = "charged")
    private Boolean _charged;

    @Json(name = "description")
    private String _description;

    @Json(name = "hours24_applicable")
    private Boolean _hours24Applicable;

    @Json(name = "id")
    private Integer _id;

    @Json(name = "modifier")
    private Double _modifier;

    @Json(name = "name")
    private String _name;

    @Source
    private JsonObject SOURCE = new JsonObject();

    public PayModifier() {
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

    public PayModifier actions(ActionsEnum[] actions) throws ParseException {
        _actions = actions;
        JsonArray ja = new JsonArray();
        for (ActionsEnum item : actions) {
            ja.add(item.toString());
        }
        SOURCE.put("actions", ja, true);
        return this;
    }

    public void setAmount(Double amount) throws ParseException {
        _amount = amount;
        SOURCE.put("amount", amount);
    }

    public Double getAmount() {
        return _amount;
    }

    public PayModifier amount(Double amount) throws ParseException {
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

    public PayModifier calculation(CalculationEnum calculation) throws ParseException {
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

    public PayModifier charged(Boolean charged) throws ParseException {
        _charged = charged;
        SOURCE.put("charged", charged);
        return this;
    }

    public void setDescription(String description) throws ParseException {
        _description = description;
        SOURCE.put("description", description);
    }

    public String getDescription() {
        return _description;
    }

    public PayModifier description(String description) throws ParseException {
        _description = description;
        SOURCE.put("description", description);
        return this;
    }

    public void setHours24Applicable(Boolean hours24Applicable) throws ParseException {
        _hours24Applicable = hours24Applicable;
        SOURCE.put("hours24_applicable", hours24Applicable);
    }

    public Boolean getHours24Applicable() {
        return _hours24Applicable;
    }

    public PayModifier hours24Applicable(Boolean hours24Applicable) throws ParseException {
        _hours24Applicable = hours24Applicable;
        SOURCE.put("hours24_applicable", hours24Applicable);
        return this;
    }

    public void setId(Integer id) throws ParseException {
        _id = id;
        SOURCE.put("id", id);
    }

    public Integer getId() {
        return _id;
    }

    public PayModifier id(Integer id) throws ParseException {
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

    public PayModifier modifier(Double modifier) throws ParseException {
        _modifier = modifier;
        SOURCE.put("modifier", modifier);
        return this;
    }

    public void setName(String name) throws ParseException {
        _name = name;
        SOURCE.put("name", name);
    }

    public String getName() {
        return _name;
    }

    public PayModifier name(String name) throws ParseException {
        _name = name;
        SOURCE.put("name", name);
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

    public enum ActionsEnum {
        @Json(name = "charge")
        CHARGE("charge"),
        @Json(name = "remove")
        REMOVE("remove");

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
    public static JsonArray toJsonArray(PayModifier[] array) {
        JsonArray list = new JsonArray();
        for (PayModifier item : array) {
            list.add(item.getJson());
        }
        return list;
    }

    public static PayModifier[] fromJsonArray(JsonArray array) {
        PayModifier[] list = new PayModifier[array.size()];
        for (int i = 0; i < array.size(); i++) {
            list[i] = fromJson(array.getJsonObject(i));
        }
        return list;
    }

    public static PayModifier fromJson(JsonObject obj) {
        try {
            return Unserializer.unserializeObject(PayModifier.class, obj);
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
        dest.writeParcelable(getJson(), flags);
    }
}
