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
 * Created by dmgen from swagger on 1/31/17.
 */

public class PayFinance implements Parcelable {
    private static final String TAG = "PayFinance";

    @Json(name = "terms")
    private String _terms;

    @Json(name = "limit")
    private Double _limit;

    @Json(name = "description")
    private String _description;

    @Json(name = "id")
    private Integer _id;

    public PayFinance() {
    }

    public void setTerms(String terms) {
        _terms = terms;
    }

    public String getTerms() {
        return _terms;
    }

    public PayFinance terms(String terms) {
        _terms = terms;
        return this;
    }

    public void setLimit(Double limit) {
        _limit = limit;
    }

    public Double getLimit() {
        return _limit;
    }

    public PayFinance limit(Double limit) {
        _limit = limit;
        return this;
    }

    public void setDescription(String description) {
        _description = description;
    }

    public String getDescription() {
        return _description;
    }

    public PayFinance description(String description) {
        _description = description;
        return this;
    }

    public void setId(Integer id) {
        _id = id;
    }

    public Integer getId() {
        return _id;
    }

    public PayFinance id(Integer id) {
        _id = id;
        return this;
    }

    /*-*****************************-*/
    /*-             Json            -*/
    /*-*****************************-*/
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

    public JsonObject toJson() {
        return toJson(this);
    }

    public static JsonObject toJson(PayFinance payFinance) {
        try {
            return Serializer.serializeObject(payFinance);
        } catch (Exception ex) {
            Log.v(TAG, TAG, ex);
            return null;
        }
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
        dest.writeParcelable(toJson(), flags);
    }
}
