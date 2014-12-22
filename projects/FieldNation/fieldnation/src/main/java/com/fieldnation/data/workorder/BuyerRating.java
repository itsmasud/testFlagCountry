package com.fieldnation.data.workorder;

import com.fieldnation.json.JsonObject;
import com.fieldnation.json.Serializer;
import com.fieldnation.json.annotations.Json;

public class BuyerRating {
    @Json(name = "buyerRateComments")
    private String _buyerRateComments;
    @Json(name = "companyLogo")
    private String _companyLogo;
    @Json(name = "dayLeftToGivingBuyerRating")
    private Boolean _dayLeftToGivingBuyerRating;
    @Json(name = "ratingId")
    private Integer _ratingId;
    @Json(name = "respectful")
    private Integer _respectful;
    @Json(name = "scopeRating")
    private Integer _scopeRating;
    @Json(name = "starRate")
    private Integer _starRate;
    @Json(name = "workorderManagerId")
    private Integer _workorderManagerId;
    @Json(name = "workorderManagerName")
    private String _workorderManagerName;

    public BuyerRating() {
    }

    public String getBuyerRateComments() {
        return _buyerRateComments;
    }

    public String getCompanyLogo() {
        return _companyLogo;
    }

    public Boolean getDayLeftToGivingBuyerRating() {
        return _dayLeftToGivingBuyerRating;
    }

    public Integer getRatingId() {
        return _ratingId;
    }

    public Integer getRespectful() {
        return _respectful;
    }

    public Integer getScopeRating() {
        return _scopeRating;
    }

    public Integer getStarRate() {
        return _starRate;
    }

    public Integer getWorkorderManagerId() {
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
