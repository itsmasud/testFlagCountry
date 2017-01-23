package com.fieldnation.data.bv2.model;

import com.fieldnation.fnjson.JsonObject;
import com.fieldnation.fnjson.Serializer;
import com.fieldnation.fnjson.Unserializer;
import com.fieldnation.fnjson.annotations.Json;
import com.fieldnation.fnlog.Log;

public class Manager {
    private static final String TAG = "Manager";

    @Json(name = "id")
    private Integer id = null;

    @Json(name = "first_name")
    private String firstName = null;

    @Json(name = "last_name")
    private String lastName = null;

    @Json(name = "email")
    private String email = null;

    @Json(name = "phone")
    private String phone = null;

    @Json(name = "rating")
    private Double rating = null;

    @Json(name = "ratings")
    private Double ratings = null;

    @Json(name = "approval_days")
    private Integer approvalDays = null;

    @Json(name = "review_period_days")
    private Integer reviewPeriodDays = null;

    public Manager() {
    }

    public Integer getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public Double getRating() {
        return rating;
    }

    public Double getRatings() {
        return ratings;
    }

    public Integer getApprovalDays() {
        return approvalDays;
    }

    public Integer getReviewPeriodDays() {
        return reviewPeriodDays;
    }

    /*-*****************************-*/
    /*-             Json            -*/
    /*-*****************************-*/
    public static Manager fromJson(JsonObject obj) {
        try {
            return Unserializer.unserializeObject(Manager.class, obj);
        } catch (Exception ex) {
            Log.v(TAG, ex);
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
            Log.v(TAG, ex);
            return null;
        }
    }
}