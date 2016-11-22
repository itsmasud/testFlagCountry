package com.fieldnation.data.workorder;

import com.fieldnation.fnjson.JsonObject;
import com.fieldnation.fnjson.Serializer;
import com.fieldnation.fnjson.Unserializer;
import com.fieldnation.fnjson.annotations.Json;
import com.fieldnation.fnlog.Log;

public class BuyerRating {
    private static final String TAG = "BuyerRating";

    @Json(name = "avgRating")
    private Double _avgRating;
    @Json(name = "city")
    private String _city;
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
    @Json(name = "state")
    private String _state;
    @Json(name = "timeToApproval")
    private Integer _timeToApproval;
    @Json(name = "totalRating")
    private Integer _totalRating;
    @Json(name = "workorderManagerId")
    private Long _workorderManagerId;
    @Json(name = "workorderManagerName")
    private String _workorderManagerName;
    @Json(name = "ratingId")
    private Integer _ratingId ;


    public BuyerRating() {
    }

    public Double getAvgRating() {
        return _avgRating;
    }

    public String getCity() {
        return _city;
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

    public String getState() {
        return _state;
    }

    public Integer getTimeToApproval() {
        return _timeToApproval == null ? 0 : _timeToApproval;
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

    public Integer getRatingId() {
        return _ratingId;
    }

    public JsonObject toJson() {
        return toJson(this);
    }

    public static JsonObject toJson(BuyerRating buyerRatingInfo) {
        try {
            return Serializer.serializeObject(buyerRatingInfo);
        } catch (Exception ex) {
            Log.v(TAG, ex);
            return null;
        }
    }

    public static BuyerRating fromJson(JsonObject json) {
        try {
            return Unserializer.unserializeObject(BuyerRating.class, json);
        } catch (Exception ex) {
            Log.v(TAG, ex);
            return null;
        }
    }

}
