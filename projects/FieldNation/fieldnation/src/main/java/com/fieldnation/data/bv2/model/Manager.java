package com.fieldnation.data.bv2.model;
import android.os.Parcel;
import android.os.Parcelable;

import com.fieldnation.fnjson.JsonObject;
import com.fieldnation.fnjson.Serializer;
import com.fieldnation.fnjson.Unserializer;
import com.fieldnation.fnjson.annotations.Json;
import com.fieldnation.fnlog.Log;

/**
 * Created by dmgen from swagger on 1/26/17.
 */

public class Manager implements Parcelable {
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

    public void setPhone(String phone) {
        _phone = phone;
    }

    public String getPhone() {
        return _phone;
    }

    public Manager phone(String phone) {
        _phone = phone;
        return this;
    }

    public void setRatings(Double ratings) {
        _ratings = ratings;
    }

    public Double getRatings() {
        return _ratings;
    }

    public Manager ratings(Double ratings) {
        _ratings = ratings;
        return this;
    }

    public void setReviewPeriodDays(Integer reviewPeriodDays) {
        _reviewPeriodDays = reviewPeriodDays;
    }

    public Integer getReviewPeriodDays() {
        return _reviewPeriodDays;
    }

    public Manager reviewPeriodDays(Integer reviewPeriodDays) {
        _reviewPeriodDays = reviewPeriodDays;
        return this;
    }

    public void setRating(Double rating) {
        _rating = rating;
    }

    public Double getRating() {
        return _rating;
    }

    public Manager rating(Double rating) {
        _rating = rating;
        return this;
    }

    public void setLastName(String lastName) {
        _lastName = lastName;
    }

    public String getLastName() {
        return _lastName;
    }

    public Manager lastName(String lastName) {
        _lastName = lastName;
        return this;
    }

    public void setId(Integer id) {
        _id = id;
    }

    public Integer getId() {
        return _id;
    }

    public Manager id(Integer id) {
        _id = id;
        return this;
    }

    public void setApprovalDays(Integer approvalDays) {
        _approvalDays = approvalDays;
    }

    public Integer getApprovalDays() {
        return _approvalDays;
    }

    public Manager approvalDays(Integer approvalDays) {
        _approvalDays = approvalDays;
        return this;
    }

    public void setFirstName(String firstName) {
        _firstName = firstName;
    }

    public String getFirstName() {
        return _firstName;
    }

    public Manager firstName(String firstName) {
        _firstName = firstName;
        return this;
    }

    public void setEmail(String email) {
        _email = email;
    }

    public String getEmail() {
        return _email;
    }

    public Manager email(String email) {
        _email = email;
        return this;
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

    /*-*********************************************-*/
    /*-			Parcelable Implementation           -*/
    /*-*********************************************-*/
    public static final Parcelable.Creator<Manager> CREATOR = new Parcelable.Creator<Manager>() {

        @Override
        public Manager createFromParcel(Parcel source) {
            try {
                return Manager.fromJson((JsonObject) source.readParcelable(JsonObject.class.getClassLoader()));
            } catch (Exception ex) {
                Log.v(TAG, ex);
                return null;
            }
        }

        @Override
        public Manager[] newArray(int size) {
            return new Manager[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(toJson(), flags);
    }
}
