package com.fieldnation.data.workorder;

import com.fieldnation.Log;
import com.fieldnation.json.JsonObject;
import com.fieldnation.json.Serializer;
import com.fieldnation.json.annotations.Json;

public class WorkorderBonusInfo {
    private static final String TAG = "WorkorderBonusInfo";

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

    public WorkorderBonusInfo() {
    }

    public Integer getAmount() {
        return _amount;
    }

    public Integer getChargeType() {
        return _chargeType;
    }

    public Integer getCharged() {
        return _charged;
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

    public static JsonObject toJson(WorkorderBonusInfo workorderBonusInfo) {
        try {
            return Serializer.serializeObject(workorderBonusInfo);
        } catch (Exception ex) {
            Log.v(TAG, ex);
            return null;
        }
    }

    public static WorkorderBonusInfo fromJson(JsonObject json) {
        try {
            return Serializer.unserializeObject(WorkorderBonusInfo.class, json);
        } catch (Exception ex) {
            Log.v(TAG, ex);
            return null;
        }
    }
}
