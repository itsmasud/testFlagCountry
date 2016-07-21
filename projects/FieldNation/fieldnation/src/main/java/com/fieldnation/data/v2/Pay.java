package com.fieldnation.data.v2;

import android.os.Parcel;
import android.os.Parcelable;

import com.fieldnation.Log;
import com.fieldnation.json.JsonObject;
import com.fieldnation.json.Serializer;
import com.fieldnation.json.Unserializer;

/**
 * Created by Michael on 7/21/2016.
 */
public class Pay implements Parcelable {
    private static final String TAG = "Pay";

    private String type;
    private String currency;
    private Double units;
    private Double amount;
    private Double additional_units;
    private Double additional_amount;
    private Double total;

    private Pay() {
    }

    public String getType() {
        return type;
    }

    public String getCurrency() {
        return currency;
    }

    public Double getUnits() {
        return units;
    }

    public Double getAmount() {
        return amount;
    }

    public Double getAdditionalUnits() {
        return additional_units;
    }

    public Double getAdditionalAmount() {
        return additional_amount;
    }

    public Double getTotal() {
        return total;
    }

    /*-*************************************-*/
    /*-			JSON Implementation			-*/
    /*-*************************************-*/
    public JsonObject toJson() {
        return toJson(this);
    }

    public static JsonObject toJson(Pay pay) {
        try {
            return Serializer.serializeObject(pay);
        } catch (Exception ex) {
            Log.v(TAG, ex);
            return null;
        }
    }

    public static Pay fromJson(JsonObject json) {
        try {
            return Unserializer.unserializeObject(Pay.class, json);
        } catch (Exception ex) {
            Log.v(TAG, ex);
            return null;
        }
    }

    /*-*********************************************-*/
    /*-			Parcelable Implementation			-*/
    /*-*********************************************-*/
    public static final Parcelable.Creator<Pay> CREATOR = new Parcelable.Creator<Pay>() {

        @Override
        public Pay createFromParcel(Parcel source) {
            try {
                return Pay.fromJson((JsonObject) source.readParcelable(JsonObject.class.getClassLoader()));
            } catch (Exception ex) {
                Log.v(TAG, ex);
                return null;
            }
        }

        @Override
        public Pay[] newArray(int size) {
            return new Pay[size];
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
