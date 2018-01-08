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

public class PayCalculatedTotal implements Parcelable {
    private static final String TAG = "PayCalculatedTotal";

    @Json(name = "total")
    private PayCalculatedTotalTotal _total;

    @Json(name = "total_with_pending_expenses")
    private PayCalculatedTotalTotalWithPendingExpenses _totalWithPendingExpenses;

    @Source
    private JsonObject SOURCE;

    public PayCalculatedTotal() {
        SOURCE = new JsonObject();
    }

    public PayCalculatedTotal(JsonObject obj) {
        SOURCE = obj;
    }

    public void setTotal(PayCalculatedTotalTotal total) throws ParseException {
        _total = total;
        SOURCE.put("total", total.getJson());
    }

    public PayCalculatedTotalTotal getTotal() {
        try {
            if (_total == null && SOURCE.has("total") && SOURCE.get("total") != null)
                _total = PayCalculatedTotalTotal.fromJson(SOURCE.getJsonObject("total"));
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        if (_total == null)
            _total = new PayCalculatedTotalTotal();

        return _total;
    }

    public PayCalculatedTotal total(PayCalculatedTotalTotal total) throws ParseException {
        _total = total;
        SOURCE.put("total", total.getJson());
        return this;
    }

    public void setTotalWithPendingExpenses(PayCalculatedTotalTotalWithPendingExpenses totalWithPendingExpenses) throws ParseException {
        _totalWithPendingExpenses = totalWithPendingExpenses;
        SOURCE.put("total_with_pending_expenses", totalWithPendingExpenses.getJson());
    }

    public PayCalculatedTotalTotalWithPendingExpenses getTotalWithPendingExpenses() {
        try {
            if (_totalWithPendingExpenses == null && SOURCE.has("total_with_pending_expenses") && SOURCE.get("total_with_pending_expenses") != null)
                _totalWithPendingExpenses = PayCalculatedTotalTotalWithPendingExpenses.fromJson(SOURCE.getJsonObject("total_with_pending_expenses"));
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        if (_totalWithPendingExpenses == null)
            _totalWithPendingExpenses = new PayCalculatedTotalTotalWithPendingExpenses();

        return _totalWithPendingExpenses;
    }

    public PayCalculatedTotal totalWithPendingExpenses(PayCalculatedTotalTotalWithPendingExpenses totalWithPendingExpenses) throws ParseException {
        _totalWithPendingExpenses = totalWithPendingExpenses;
        SOURCE.put("total_with_pending_expenses", totalWithPendingExpenses.getJson());
        return this;
    }

    /*-*****************************-*/
    /*-             Json            -*/
    /*-*****************************-*/
    public static JsonArray toJsonArray(PayCalculatedTotal[] array) {
        JsonArray list = new JsonArray();
        for (PayCalculatedTotal item : array) {
            list.add(item.getJson());
        }
        return list;
    }

    public static PayCalculatedTotal[] fromJsonArray(JsonArray array) {
        PayCalculatedTotal[] list = new PayCalculatedTotal[array.size()];
        for (int i = 0; i < array.size(); i++) {
            list[i] = fromJson(array.getJsonObject(i));
        }
        return list;
    }

    public static PayCalculatedTotal fromJson(JsonObject obj) {
        try {
            return new PayCalculatedTotal(obj);
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
    public static final Parcelable.Creator<PayCalculatedTotal> CREATOR = new Parcelable.Creator<PayCalculatedTotal>() {

        @Override
        public PayCalculatedTotal createFromParcel(Parcel source) {
            try {
                return PayCalculatedTotal.fromJson((JsonObject) source.readParcelable(JsonObject.class.getClassLoader()));
            } catch (Exception ex) {
                Log.v(TAG, ex);
                return null;
            }
        }

        @Override
        public PayCalculatedTotal[] newArray(int size) {
            return new PayCalculatedTotal[size];
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
