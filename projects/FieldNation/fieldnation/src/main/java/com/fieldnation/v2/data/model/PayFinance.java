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
    private JsonObject SOURCE = new JsonObject();

    public PayFinance() {
    }

    public void setDescription(String description) throws ParseException {
        _description = description;
        SOURCE.put("description", description);
    }

    public String getDescription() {
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
            return Unserializer.unserializeObject(PayFinance.class, obj);
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
}
