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

public class PredefinedExpense implements Parcelable {
    private static final String TAG = "PredefinedExpense";

    @Json(name = "amount")
    private Double _amount;

    @Json(name = "api_code")
    private String _apiCode;

    @Json(name = "description")
    private String _description;

    @Json(name = "hidden_tags")
    private String _hiddenTags;

    @Json(name = "id")
    private Integer _id;

    @Json(name = "label")
    private String _label;

    @Source
    private JsonObject SOURCE;

    public PredefinedExpense() {
        SOURCE = new JsonObject();
    }

    public PredefinedExpense(JsonObject obj) {
        SOURCE = obj;
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

    public PredefinedExpense amount(Double amount) throws ParseException {
        _amount = amount;
        SOURCE.put("amount", amount);
        return this;
    }

    public void setApiCode(String apiCode) throws ParseException {
        _apiCode = apiCode;
        SOURCE.put("api_code", apiCode);
    }

    public String getApiCode() {
        try {
            if (_apiCode == null && SOURCE.has("api_code") && SOURCE.get("api_code") != null)
                _apiCode = SOURCE.getString("api_code");
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _apiCode;
    }

    public PredefinedExpense apiCode(String apiCode) throws ParseException {
        _apiCode = apiCode;
        SOURCE.put("api_code", apiCode);
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

    public PredefinedExpense description(String description) throws ParseException {
        _description = description;
        SOURCE.put("description", description);
        return this;
    }

    public void setHiddenTags(String hiddenTags) throws ParseException {
        _hiddenTags = hiddenTags;
        SOURCE.put("hidden_tags", hiddenTags);
    }

    public String getHiddenTags() {
        try {
            if (_hiddenTags == null && SOURCE.has("hidden_tags") && SOURCE.get("hidden_tags") != null)
                _hiddenTags = SOURCE.getString("hidden_tags");
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _hiddenTags;
    }

    public PredefinedExpense hiddenTags(String hiddenTags) throws ParseException {
        _hiddenTags = hiddenTags;
        SOURCE.put("hidden_tags", hiddenTags);
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

    public PredefinedExpense id(Integer id) throws ParseException {
        _id = id;
        SOURCE.put("id", id);
        return this;
    }

    public void setLabel(String label) throws ParseException {
        _label = label;
        SOURCE.put("label", label);
    }

    public String getLabel() {
        try {
            if (_label == null && SOURCE.has("label") && SOURCE.get("label") != null)
                _label = SOURCE.getString("label");
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _label;
    }

    public PredefinedExpense label(String label) throws ParseException {
        _label = label;
        SOURCE.put("label", label);
        return this;
    }

    /*-*****************************-*/
    /*-             Json            -*/
    /*-*****************************-*/
    public static JsonArray toJsonArray(PredefinedExpense[] array) {
        JsonArray list = new JsonArray();
        for (PredefinedExpense item : array) {
            list.add(item.getJson());
        }
        return list;
    }

    public static PredefinedExpense[] fromJsonArray(JsonArray array) {
        PredefinedExpense[] list = new PredefinedExpense[array.size()];
        for (int i = 0; i < array.size(); i++) {
            list[i] = fromJson(array.getJsonObject(i));
        }
        return list;
    }

    public static PredefinedExpense fromJson(JsonObject obj) {
        try {
            return new PredefinedExpense(obj);
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
    public static final Parcelable.Creator<PredefinedExpense> CREATOR = new Parcelable.Creator<PredefinedExpense>() {

        @Override
        public PredefinedExpense createFromParcel(Parcel source) {
            try {
                return PredefinedExpense.fromJson((JsonObject) source.readParcelable(JsonObject.class.getClassLoader()));
            } catch (Exception ex) {
                Log.v(TAG, ex);
                return null;
            }
        }

        @Override
        public PredefinedExpense[] newArray(int size) {
            return new PredefinedExpense[size];
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
