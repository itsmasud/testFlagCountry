package com.fieldnation.data.workorder;

import com.fieldnation.fnjson.JsonObject;
import com.fieldnation.fnjson.Serializer;
import com.fieldnation.fnjson.Unserializer;
import com.fieldnation.fnjson.annotations.Json;
import com.fieldnation.fnlog.Log;

public class BonusInfo {
    private static final String TAG = "BonusInfo";

    @Json(name = "amount")
    private Integer _amount;
    @Json(name = "charge_type")
    private Integer _chargeType;
    @Json(name = "charged")
    private Integer _charged;
    @Json(name = "company_id")
    private Integer _companyId;
    @Json(name = "currencyString")
    private String _currencyString;
    @Json(name = "id")
    private Integer _id;
    @Json(name = "name")
    private String _name;
    @Json(name = "rule_explanation")
    private String _ruleExplanation;
    @Json(name = "status")
    private Integer _status;
    @Json(name = "wocpf_id")
    private Integer _wocpfId;

    public BonusInfo() {
    }

    public Integer getAmount() {
        return _amount;
    }

    public Integer getChargeType() {
        return _chargeType;
    }

    public boolean isCharged() {
        return _charged == 1;
    }

    public Integer getCompanyId() {
        return _companyId;
    }

    public String getCurrencyString() {
        return _currencyString;
    }

    public Integer getId() {
        return _id;
    }

    public String getName() {
        return _name;
    }

    public String getRuleExplanation() {
        return _ruleExplanation;
    }

    public Integer getStatus() {
        return _status;
    }

    public Integer getWocpfId() {
        return _wocpfId;
    }

    public JsonObject toJson() {
        return toJson(this);
    }

    public static JsonObject toJson(BonusInfo workorderBonusInfo) {
        try {
            return Serializer.serializeObject(workorderBonusInfo);
        } catch (Exception ex) {
            Log.v(TAG, ex);
            return null;
        }
    }

    public static BonusInfo fromJson(JsonObject json) {
        try {
            return Unserializer.unserializeObject(BonusInfo.class, json);
        } catch (Exception ex) {
            Log.v(TAG, ex);
            return null;
        }
    }
}
