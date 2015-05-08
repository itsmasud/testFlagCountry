package com.fieldnation.data.workorder;

import android.os.Parcel;
import android.os.Parcelable;

import com.fieldnation.json.JsonObject;
import com.fieldnation.json.Serializer;
import com.fieldnation.json.annotations.Json;
import com.fieldnation.utils.misc;

import java.text.ParseException;

public class Pay implements Parcelable {

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
    private Integer _maxDevice;
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

    public Pay() {
    }

    public Double getBlendedAdditionalHours() {
        return _blendedAdditionalHours;
    }

    public Double getBlendedAdditionalRate() {
        return _blendedAdditionalRate;
    }

    public Double getBlendedFirstHours() {
        return _blendedFirstHours;
    }

    public Double getBlendedStartRate() {
        return _blendedStartRate;
    }

    public Double getBonuses() {
        return _bonuses;
    }

    public String getDescription() {
        return _description;
    }

    public String getExpenses() {
        return _expenses;
    }

    public Double getFixedAmount() {
        return _fixedAmount;
    }

    public Boolean hidePay() {
        if (_hidePay != null)
            return _hidePay;

        return true;
    }

    public Integer getMaxDevice() {
        return _maxDevice;
    }

    public Double getMaxHour() {
        return _maxHour;
    }

    public Double getMaximumAmount() {
        return _maximumAmount;
    }

    public String getPayRateBasis() {
        return _payRateBasis;
    }

    public Double getPenaltyFees() {
        return _penaltyFees;
    }

    public Double getPerDevice() {
        return _perDevice;
    }

    public Double getPerHour() {
        return _perHour;
    }

    public JsonObject toJson() {
        return toJson(this);
    }

    public static JsonObject toJson(Pay pay) {
        try {
            return Serializer.serializeObject(pay);
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public static Pay fromJson(JsonObject json) {
        try {
            return Serializer.unserializeObject(Pay.class, json);
        } catch (Exception ex) {
            ex.printStackTrace();
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
        _maxDevice = maxDevices;
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
            line1 = "Fixed " + misc.toCurrency(getFixedAmount());
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
                return Pay.fromJson(new JsonObject(source.readString()));
            } catch (ParseException e) {
                e.printStackTrace();
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
        dest.writeString(toJson().toString());
    }

}
