package com.fieldnation.data.bv2.model;

import com.fieldnation.fnjson.JsonObject;
import com.fieldnation.fnjson.Serializer;
import com.fieldnation.fnjson.Unserializer;
import com.fieldnation.fnjson.annotations.Json;
import com.fieldnation.fnlog.Log;

public class Manager {
    private static final String TAG = "Manager";

    @Json(name = "phone")
    private String phone;

    @Json(name = "ratings")
    private Double ratings;

    @Json(name = "review_period_days")
    private Integer reviewPeriodDays;

    @Json(name = "rating")
    private Double rating;

    @Json(name = "last_name")
    private String lastName;

    @Json(name = "id")
    private Integer id;

    @Json(name = "approval_days")
    private Integer approvalDays;

    @Json(name = "first_name")
    private String firstName;

    @Json(name = "email")
    private String email;

    public Manager() {
    }

    public String getPhone() {
        return phone;
    }

    public Double getRatings() {
        return ratings;
    }

    public Integer getReviewPeriodDays() {
        return reviewPeriodDays;
    }

    public Double getRating() {
        return rating;
    }

    public String getLastName() {
        return lastName;
    }

    public Integer getId() {
        return id;
    }

    public Integer getApprovalDays() {
        return approvalDays;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getEmail() {
        return email;
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
