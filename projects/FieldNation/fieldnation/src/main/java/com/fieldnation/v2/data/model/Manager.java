package com.fieldnation.v2.data.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.fieldnation.fnjson.JsonArray;
import com.fieldnation.fnjson.JsonObject;
import com.fieldnation.fnjson.Serializer;
import com.fieldnation.fnjson.Unserializer;
import com.fieldnation.fnjson.annotations.Json;
import com.fieldnation.fnjson.annotations.Source;
import com.fieldnation.fnlog.Log;

import java.text.ParseException;

/**
 * Created by dmgen from swagger.
 */

public class Manager implements Parcelable {
    private static final String TAG = "Manager";

    @Json(name = "approval_days")
    private Integer _approvalDays;

    @Json(name = "email")
    private String _email;

    @Json(name = "first_name")
    private String _firstName;

    @Json(name = "id")
    private Integer _id;

    @Json(name = "last_name")
    private String _lastName;

    @Json(name = "phone")
    private String _phone;

    @Json(name = "rating")
    private Double _rating;

    @Json(name = "ratings")
    private Double _ratings;

    @Json(name = "review_period_days")
    private Integer _reviewPeriodDays;

    @Source
    private JsonObject SOURCE = new JsonObject();

    public Manager() {
    }

    public void setApprovalDays(Integer approvalDays) throws ParseException {
        _approvalDays = approvalDays;
        SOURCE.put("approval_days", approvalDays);
    }

    public Integer getApprovalDays() {
        return _approvalDays;
    }

    public Manager approvalDays(Integer approvalDays) throws ParseException {
        _approvalDays = approvalDays;
        SOURCE.put("approval_days", approvalDays);
        return this;
    }

    public void setEmail(String email) throws ParseException {
        _email = email;
        SOURCE.put("email", email);
    }

    public String getEmail() {
        return _email;
    }

    public Manager email(String email) throws ParseException {
        _email = email;
        SOURCE.put("email", email);
        return this;
    }

    public void setFirstName(String firstName) throws ParseException {
        _firstName = firstName;
        SOURCE.put("first_name", firstName);
    }

    public String getFirstName() {
        return _firstName;
    }

    public Manager firstName(String firstName) throws ParseException {
        _firstName = firstName;
        SOURCE.put("first_name", firstName);
        return this;
    }

    public void setId(Integer id) throws ParseException {
        _id = id;
        SOURCE.put("id", id);
    }

    public Integer getId() {
        return _id;
    }

    public Manager id(Integer id) throws ParseException {
        _id = id;
        SOURCE.put("id", id);
        return this;
    }

    public void setLastName(String lastName) throws ParseException {
        _lastName = lastName;
        SOURCE.put("last_name", lastName);
    }

    public String getLastName() {
        return _lastName;
    }

    public Manager lastName(String lastName) throws ParseException {
        _lastName = lastName;
        SOURCE.put("last_name", lastName);
        return this;
    }

    public void setPhone(String phone) throws ParseException {
        _phone = phone;
        SOURCE.put("phone", phone);
    }

    public String getPhone() {
        return _phone;
    }

    public Manager phone(String phone) throws ParseException {
        _phone = phone;
        SOURCE.put("phone", phone);
        return this;
    }

    public void setRating(Double rating) throws ParseException {
        _rating = rating;
        SOURCE.put("rating", rating);
    }

    public Double getRating() {
        return _rating;
    }

    public Manager rating(Double rating) throws ParseException {
        _rating = rating;
        SOURCE.put("rating", rating);
        return this;
    }

    public void setRatings(Double ratings) throws ParseException {
        _ratings = ratings;
        SOURCE.put("ratings", ratings);
    }

    public Double getRatings() {
        return _ratings;
    }

    public Manager ratings(Double ratings) throws ParseException {
        _ratings = ratings;
        SOURCE.put("ratings", ratings);
        return this;
    }

    public void setReviewPeriodDays(Integer reviewPeriodDays) throws ParseException {
        _reviewPeriodDays = reviewPeriodDays;
        SOURCE.put("review_period_days", reviewPeriodDays);
    }

    public Integer getReviewPeriodDays() {
        return _reviewPeriodDays;
    }

    public Manager reviewPeriodDays(Integer reviewPeriodDays) throws ParseException {
        _reviewPeriodDays = reviewPeriodDays;
        SOURCE.put("review_period_days", reviewPeriodDays);
        return this;
    }

    /*-*****************************-*/
    /*-             Json            -*/
    /*-*****************************-*/
    public static JsonArray toJsonArray(Manager[] array) {
        JsonArray list = new JsonArray();
        for (Manager item : array) {
            list.add(item.getJson());
        }
        return list;
    }

    public static Manager[] fromJsonArray(JsonArray array) {
        Manager[] list = new Manager[array.size()];
        for (int i = 0; i < array.size(); i++) {
            list[i] = fromJson(array.getJsonObject(i));
        }
        return list;
    }

    public static Manager fromJson(JsonObject obj) {
        try {
            return Unserializer.unserializeObject(Manager.class, obj);
        } catch (Exception ex) {
            Log.v(TAG, TAG, ex);
            return null;
        }
    }

    public JsonObject getJson() {
        return SOURCE;
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
        dest.writeParcelable(getJson(), flags);
    }
}
