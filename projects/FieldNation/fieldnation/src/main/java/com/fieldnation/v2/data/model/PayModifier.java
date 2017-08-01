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
    private JsonObject SOURCE;

    public PayModifier() {
        SOURCE = new JsonObject();
    }

    public PayModifier(JsonObject obj) {
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
        try {
            if (_amount == null && SOURCE.has("amount") && SOURCE.get("amount") != null)
                _amount = SOURCE.getDouble("amount");
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

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
        try {
            if (_calculation == null && SOURCE.has("calculation") && SOURCE.get("calculation") != null)
                _calculation = CalculationEnum.fromString(SOURCE.getString("calculation"));
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

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
        try {
            if (_charged == null && SOURCE.has("charged") && SOURCE.get("charged") != null)
                _charged = SOURCE.getBoolean("charged");
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

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
        try {
            if (_description == null && SOURCE.has("description") && SOURCE.get("description") != null)
                _description = SOURCE.getString("description");
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

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
        try {
            if (_hours24Applicable == null && SOURCE.has("hours24_applicable") && SOURCE.get("hours24_applicable") != null)
                _hours24Applicable = SOURCE.getBoolean("hours24_applicable");
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

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
        try {
            if (_id == null && SOURCE.has("id") && SOURCE.get("id") != null)
                _id = SOURCE.getInt("id");
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

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
        try {
            if (_modifier == null && SOURCE.has("modifier") && SOURCE.get("modifier") != null)
                _modifier = SOURCE.getDouble("modifier");
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

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
        try {
            if (_name == null && SOURCE.has("name") && SOURCE.get("name") != null)
                _name = SOURCE.getString("name");
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

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

        public static CalculationEnum fromString(String value) {
            CalculationEnum[] values = values();
            for (CalculationEnum v : values) {
                if (v.value.equals(value))
                    return v;
            }
            return null;
        }

        public static CalculationEnum[] fromJsonArray(JsonArray jsonArray) {
            CalculationEnum[] list = new CalculationEnum[jsonArray.size()];
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

    public enum ActionsEnum {
        @Json(name = "charge")
        CHARGE("charge"),
        @Json(name = "delete")
        DELETE("delete");

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
            return new PayModifier(obj);
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

    /*-*****************************-*/
    /*-         Human Code          -*/
    /*-*****************************-*/

    private Set<ActionsEnum> _actionsSet = null;

    public Set<ActionsEnum> getActionsSet() {
        if (_actionsSet == null) {
            _actionsSet = new HashSet<>();
            if (getActions() != null) _actionsSet.addAll(Arrays.asList(getActions()));
        }
        return _actionsSet;
    }
}
