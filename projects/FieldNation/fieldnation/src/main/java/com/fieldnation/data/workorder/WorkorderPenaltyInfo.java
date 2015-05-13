package com.fieldnation.data.workorder;

import com.fieldnation.json.JsonObject;
import com.fieldnation.json.Serializer;
import com.fieldnation.json.annotations.Json;

public class WorkorderPenaltyInfo {

    @Json(name = "amount")
    private Double _amount;
    @Json(name = "charged")
    private Integer _charged;
    @Json(name = "chargeType")
    private Integer _chargeType;
    @Json(name = "name")
    private String _name;
    @Json(name = "penaltyFeeId")
    private Integer _penaltyFeeId;
    @Json(name = "ruleExplanation")
    private String _ruleExplanation;

    public WorkorderPenaltyInfo() {
    }

    public Double getAmount() {
        return _amount;
    }

    public Integer getCharged() {
        return _charged;
    }

    public Integer getChargeType() {
        return _chargeType;
    }

    public String getName() {
        return _name;
    }

    public Integer getPenaltyFeeId() {
        return _penaltyFeeId;
    }

    public String getRuleExplanation() {
        return _ruleExplanation;
    }

    public JsonObject toJson() {
        return toJson(this);
    }

    public static JsonObject toJson(WorkorderPenaltyInfo workorderPenaltyInfo) {
        try {
            return Serializer.serializeObject(workorderPenaltyInfo);
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public static WorkorderPenaltyInfo fromJson(JsonObject json) {
        try {
            return Serializer.unserializeObject(WorkorderPenaltyInfo.class, json);
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

}
