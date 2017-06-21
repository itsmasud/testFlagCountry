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

public class PayFinance implements Parcelable {
    private static final String TAG = "PayFinance";

    @Json(name = "description")
    private String _description;

    @Json(name = "id")
    private Integer _id;

    @Json(name = "limit")
    private Double _limit;

    @Json(name = "terms")
    private String _terms;

    @Source
    private JsonObject SOURCE;

    public PayFinance() {
        SOURCE = new JsonObject();
    }

    public PayFinance(JsonObject obj) {
        SOURCE = obj;
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

    public PayFinance description(String description) throws ParseException {
        _description = description;
        SOURCE.put("description", description);
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

    public PayFinance id(Integer id) throws ParseException {
        _id = id;
        SOURCE.put("id", id);
        return this;
    }

    public void setLimit(Double limit) throws ParseException {
        _limit = limit;
        SOURCE.put("limit", limit);
    }

    public Double getLimit() {
        try {
            if (_limit == null && SOURCE.has("limit") && SOURCE.get("limit") != null)
                _limit = SOURCE.getDouble("limit");
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _limit;
    }

    public PayFinance limit(Double limit) throws ParseException {
        _limit = limit;
        SOURCE.put("limit", limit);
        return this;
    }

    public void setTerms(String terms) throws ParseException {
        _terms = terms;
        SOURCE.put("terms", terms);
    }

    public String getTerms() {
        try {
            if (_terms == null && SOURCE.has("terms") && SOURCE.get("terms") != null)
                _terms = SOURCE.getString("terms");
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _terms;
    }

    public PayFinance terms(String terms) throws ParseException {
        _terms = terms;
        SOURCE.put("terms", terms);
        return this;
    }

    /*-*****************************-*/
    /*-             Json            -*/
    /*-*****************************-*/
    public static JsonArray toJsonArray(PayFinance[] array) {
        JsonArray list = new JsonArray();
        for (PayFinance item : array) {
            list.add(item.getJson());
        }
        return list;
    }

    public static PayFinance[] fromJsonArray(JsonArray array) {
        PayFinance[] list = new PayFinance[array.size()];
        for (int i = 0; i < array.size(); i++) {
            list[i] = fromJson(array.getJsonObject(i));
        }
        return list;
    }

    public static PayFinance fromJson(JsonObject obj) {
        try {
            return new PayFinance(obj);
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
    public static final Parcelable.Creator<PayFinance> CREATOR = new Parcelable.Creator<PayFinance>() {

        @Override
        public PayFinance createFromParcel(Parcel source) {
            try {
                return PayFinance.fromJson((JsonObject) source.readParcelable(JsonObject.class.getClassLoader()));
            } catch (Exception ex) {
                Log.v(TAG, ex);
                return null;
            }
        }

        @Override
        public PayFinance[] newArray(int size) {
            return new PayFinance[size];
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
