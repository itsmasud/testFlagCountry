package com.fieldnation.data.accounting;

import android.os.Parcel;
import android.os.Parcelable;

import com.fieldnation.Log;
import com.fieldnation.json.JsonObject;
import com.fieldnation.json.Serializer;
import com.fieldnation.json.annotations.Json;
import com.fieldnation.utils.ISO8601;

import java.util.Calendar;

public class Payment implements Parcelable {
    private static final String TAG = "Payment";

    @Json(name = "amount")
    private Double _amount;
    @Json(name = "datePaid")
    private String _datePaid;
    @Json(name = "fees")
    private Fee[] _fees;
    @Json(name = "payMethod")
    private String _payMethod;
    @Json(name = "paymentId")
    private Long _paymentId;
    @Json(name = "status")
    private String _status;
    @Json(name = "workorders")
    private Workorder[] _workorders;

    public Payment() {
    }

    public Double getAmount() {
        return _amount;
    }

    public String getDatePaid() {
        return _datePaid;
    }

    public Calendar getDatePaidCalendar() {
        try {
            return ISO8601.toCalendar(_datePaid);
        } catch (Exception ex) {
            Log.v(TAG, ex);
            return null;
        }
    }

    public Fee[] getFees() {
        return _fees;
    }

    public String getPayMethod() {
        return _payMethod;
    }

    public Long getPaymentId() {
        return _paymentId;
    }

    public String getStatus() {
        return _status;
    }

    public Workorder[] getWorkorders() {
        return _workorders;
    }

    public JsonObject toJson() {
        return toJson(this);
    }

    public static JsonObject toJson(Payment payment) {
        try {
            return Serializer.serializeObject(payment);
        } catch (Exception ex) {
            Log.v(TAG, ex);
            return null;
        }
    }

    public static Payment fromJson(JsonObject json) {
        try {
            return Serializer.unserializeObject(Payment.class, json);
        } catch (Exception ex) {
            Log.v(TAG, ex);
            return null;
        }
    }


    /*-*********************************************-*/
    /*-			Parcelable Implementation			-*/
    /*-*********************************************-*/
    public static final Creator<Payment> CREATOR = new Creator<Payment>() {
        @Override
        public Payment createFromParcel(Parcel source) {
            try {
                return Payment.fromJson((JsonObject) (source.readParcelable(JsonObject.class.getClassLoader())));
            } catch (Exception ex) {
                Log.v(TAG, ex);
            }
            return null;
        }

        @Override
        public Payment[] newArray(int size) {
            return new Payment[size];
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
