package com.fieldnation.data.workorder;

import com.fieldnation.json.JsonObject;
import com.fieldnation.json.Serializer;
import com.fieldnation.json.annotations.Json;

public class BuyerRating {
    @Json(name = "avgRating")
    private Double _avgRating;
    @Json(name = "clearExpectationRatingPercent")
    private Double _clearExpectationRatingPercent;
    @Json(name = "companyLogo")
    private String _companyLogo;
    @Json(name = "currentApprovalTimeGrade")
    private String _currentApprovalTimeGrade;
    @Json(name = "currentReviewPeriod")
    private Integer _currentReviewPeriod;
    @Json(name = "dayLeftToGivingBuyerRating")
    private Boolean _dayLeftToGivingBuyerRating;
    @Json(name = "professionalismRatingPercent")
    private Double _professionalismRatingPercent;
    @Json(name = "totalRating")
    private Integer _totalRating;
    @Json(name = "workorderManagerId")
    private Long _workorderManagerId;
    @Json(name = "workorderManagerName")
    private String _workorderManagerName;

    public BuyerRating() {
    }

    public Double getAvgRating() {
        return _avgRating;
    }

    public Double getClearExpectationRatingPercent() {
        return _clearExpectationRatingPercent;
    }

    public String getCompanyLogo() {
        return _companyLogo;
    }

    public String getCurrentApprovalTimeGrade() {
        return _currentApprovalTimeGrade;
    }

    public Integer getCurrentReviewPeriod() {
        return _currentReviewPeriod;
    }

    public Boolean getDayLeftToGivingBuyerRating() {
        return _dayLeftToGivingBuyerRating;
    }

    public Double getProfessionalismRatingPercent() {
        return _professionalismRatingPercent;
    }

    public Integer getTotalRating() {
        return _totalRating;
    }

    public Long getWorkorderManagerId() {
        return _workorderManagerId;
    }

    public String getWorkorderManagerName() {
        return _workorderManagerName;
    }

    public JsonObject toJson() {
        return toJson(this);
    }

    public static JsonObject toJson(BuyerRating buyerRatingInfo) {
        try {
            return Serializer.serializeObject(buyerRatingInfo);
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public static BuyerRating fromJson(JsonObject json) {
        try {
            return Serializer.unserializeObject(BuyerRating.class, json);
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

}
