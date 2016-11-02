package com.fieldnation.data.workorder;

import android.os.Parcel;
import android.os.Parcelable;

import com.fieldnation.fnjson.JsonObject;
import com.fieldnation.fnjson.Serializer;
import com.fieldnation.fnjson.Unserializer;
import com.fieldnation.fnjson.annotations.Json;
import com.fieldnation.fnlog.Log;
import com.fieldnation.fntools.misc;

public class Pay implements Parcelable {
    private static final String TAG = "Pay";

    @Json(name = "blendedAdditionalHours")
    private Double _blendedAdditionalHours;
    @Json(name = "blendedAdditionalRate")
    private Double _blendedAdditionalRate;
    @Json(name = "blendedFirstHours")
    private Double _blendedFirstHours;
    @Json(name = "blendedStartRate")
    private Double _blendedStartRate;
    @Json(name = "bonuses")
    private Double _bonuses;
    @Json(name = "description")
    private String _description;
    @Json(name = "expenses")
    private String _expenses;
    @Json(name = "fixedAmount")
    private Double _fixedAmount;
    @Json(name = "hidePay")
    private Boolean _hidePay;
    @Json(name = "maxDevice")
    private Double _maxDevice;
    @Json(name = "maxHour")
    private Double _maxHour;
    @Json(name = "maximumAmount")
    private Double _maximumAmount;
    @Json(name = "payRateBasis")
    private String _payRateBasis;
    @Json(name = "penaltyFees")
    private Double _penaltyFees;
    @Json(name = "perDevice")
    private Double _perDevice;
    @Json(name = "perHour")
    private Double _perHour;
    @Json(name = "totalPayment")
    private Double _totalPayment;

    public Pay() {
    }

    public Double getBlendedAdditionalHours() {
        return _blendedAdditionalHours == null ? 0.0 : _blendedAdditionalHours;
    }

    public Double getBlendedAdditionalRate() {
        return _blendedAdditionalRate == null ? 0.0 : _blendedAdditionalRate;
    }

    public Double getBlendedFirstHours() {
        return _blendedFirstHours == null ? 0.0 : _blendedFirstHours;
    }

    public Double getBlendedStartRate() {
        return _blendedStartRate == null ? 0.0 : _blendedStartRate;
    }

    public Double getBonuses() {
        return _bonuses == null ? 0.0 : _bonuses;
    }

    public String getDescription() {
        return _description;
    }

    public void setDescription(String description) {
        _description = description;
    }

    public String getExpenses() {
        return _expenses;
    }

    public Double getFixedAmount() {
        return _fixedAmount == null ? 0.0 : _fixedAmount;
    }

    public Boolean hidePay() {
        if (_hidePay != null)
            return _hidePay;

        return false;
    }

    public Integer getMaxDevice() {
        return (int) (double) (_maxDevice == null ? 0.0 : _maxDevice);
    }

    public Double getMaxHour() {
        return _maxHour == null ? 0.0 : _maxHour;
    }

    public Double getMaximumAmount() {
        return _maximumAmount == null ? 0.0 : _maximumAmount;
    }

    public String getPayRateBasis() {
        return _payRateBasis;
    }

    public Double getPenaltyFees() {
        return _penaltyFees == null ? 0.0 : _penaltyFees;
    }

    public Double getPerDevice() {
        return _perDevice == null ? 0.0 : _perDevice;
    }

    public Double getPerHour() {
        return _perHour == null ? 0.0 : _perHour;
    }

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

    /*-*************************************************-*/
    /*-				Human Generated Code				-*/
    /*-*************************************************-*/
    public Pay(double hourlyRate, double maxHours) {
        _payRateBasis = "Hourly";
        _perHour = hourlyRate;
        _maxHour = maxHours;
    }

    public Pay(double fixedRate) {
        _payRateBasis = "Fixed";
        _fixedAmount = fixedRate;
    }

    public Pay(double deviceRate, int maxDevices) {
        _payRateBasis = "Per Device";
        _perDevice = deviceRate;
        _maxDevice = (double) maxDevices;
    }

    public Pay(double hourlyRate, double maxHours, double extraRate, double extraMax) {
        _payRateBasis = "Blended";
        _blendedAdditionalHours = extraMax;
        _blendedAdditionalRate = extraRate;
        _blendedFirstHours = maxHours;
        _blendedStartRate = hourlyRate;
    }

    public String[] toDisplayStringLong() {
        String line1 = null;
        String line2 = null;

        String basis = getPayRateBasis();

        // Todo, need to localize this
        if ("Fixed".equals(basis)) {
            line1 = misc.toCurrency(getFixedAmount()) + " Fixed";
        } else if ("Hourly".equals(basis)) {
            line1 = misc.toCurrency(getPerHour()) + " per hr up to " + getMaxHour() + " hours.";
        } else if ("Blended".equals(basis)) {
            line1 = misc.toCurrency(getBlendedStartRate()) + " for the first " + getBlendedFirstHours() + " hours.";
            line2 = "Then " + misc.toCurrency(getBlendedAdditionalRate()) + " per hr up to " + getBlendedAdditionalHours() + " hours.";
        } else if ("Per Device".equals(basis)) {
            line1 = misc.toCurrency(getPerDevice()) + " per device up to " + getMaxDevice() + " devices.";
        }

        return new String[]{line1, line2};
    }

    public String toDisplayStringShort() {
        String basis = getPayRateBasis();

        try {
            // Todo, localize this
            if ("Fixed".equals(basis)) {
                return misc.toCurrency(getFixedAmount());
            } else if ("Hourly".equals(basis)) {
                return misc.toCurrency(getPerHour());
            } else if ("Blended".equals(basis)) {
                return misc.toCurrency(getBlendedStartRate());
                // + "\n + " + misc.toCurrency(getBlendedAdditionalRate()) + " X " +
                // getBlendedAdditionalHours();
            } else if ("Per Device".equals(basis)) {
                return misc.toCurrency(getPerDevice());
            }
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }
        return null;
    }

    public boolean isFixedRate() {
        return "Fixed".equals(getPayRateBasis());
    }

    public boolean isHourlyRate() {
        return "Hourly".equals(getPayRateBasis());
    }

    public boolean isBlendedRate() {
        return "Blended".equals(getPayRateBasis());
    }

    public boolean isPerDeviceRate() {
        return "Per Device".equals(getPayRateBasis());
    }

    public Double getTotalPayment() {
        return _totalPayment;
    }

    /*-*********************************************-*/
    /*-			Parcelable Implementation			-*/
    /*-*********************************************-*/
    public static final Parcelable.Creator<Pay> CREATOR = new Creator<Pay>() {

        @Override
        public Pay[] newArray(int size) {
            return new Pay[size];
        }

        @Override
        public Pay createFromParcel(Parcel source) {
            try {
                return Pay.fromJson((JsonObject) (source.readParcelable(JsonObject.class.getClassLoader())));
            } catch (Exception e) {
                Log.v(TAG, e);
            }
            return null;
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
