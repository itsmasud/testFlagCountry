package com.fieldnation.data.bv2.model;

import com.fieldnation.fnjson.JsonObject;
import com.fieldnation.fnjson.Serializer;
import com.fieldnation.fnjson.Unserializer;
import com.fieldnation.fnjson.annotations.Json;
import com.fieldnation.fnlog.Log;

public class Manager {
    private static final String TAG = "Manager";

    @Json(name = "phone")
    private String _phone;

    @Json(name = "ratings")
    private Double _ratings;

    @Json(name = "review_period_days")
    private Integer _reviewPeriodDays;

    @Json(name = "rating")
    private Double _rating;

    @Json(name = "last_name")
    private String _lastName;

    @Json(name = "id")
    private Integer _id;

    @Json(name = "approval_days")
    private Integer _approvalDays;

    @Json(name = "first_name")
    private String _firstName;

    @Json(name = "email")
    private String _email;

    public Manager() {
    }

    public String getPhone() {
        return _phone;
    }

    public Double getRatings() {
        return _ratings;
    }

    public Integer getReviewPeriodDays() {
        return _reviewPeriodDays;
    }

    public Double getRating() {
        return _rating;
    }

    public String getLastName() {
        return _lastName;
    }

    public Integer getId() {
        return _id;
    }

    public Integer getApprovalDays() {
        return _approvalDays;
    }

    public String getFirstName() {
        return _firstName;
    }

    public String getEmail() {
        return _email;
    }

    /*-*****************************-*/
    /*-             Json            -*/
    /*-*****************************-*/
    public static Manager fromJson(JsonObject obj) {
        try {
            return Unserializer.unserializeObject(Manager.class, obj);
        } catch (Exception ex) {
            Log.v(TAG, TAG, ex);
            return null;
        }
    }

    public JsonObject toJson() {
        return toJson(this);
    }

    public static JsonObject toJson(Manager manager) {
        try {
            return Serializer.serializeObject(manager);
        } catch (Exception ex) {
            Log.v(TAG, TAG, ex);
            return null;
        }
    }
}
