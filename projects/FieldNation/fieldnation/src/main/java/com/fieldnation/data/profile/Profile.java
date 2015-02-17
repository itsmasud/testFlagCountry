package com.fieldnation.data.profile;

import android.os.Parcel;
import android.os.Parcelable;

import com.fieldnation.json.JsonObject;
import com.fieldnation.json.Serializer;
import com.fieldnation.json.annotations.Json;
import com.fieldnation.utils.ISO8601;

public class Profile implements Parcelable {
    @Json(name = "acceptedTos")
    private Boolean _acceptedTos;
    @Json(name = "city")
    private String _city;
    @Json(name = "description")
    private String _description;
    @Json(name = "email")
    private String _email;
    @Json(name = "firstname")
    private String _firstname;
    @Json(name = "hasValidCOI")
    private Boolean _hasValidCoi;
    @Json(name = "insurancePercent")
    private Float _insurancePercent;
    @Json(name = "isProvider")
    private Boolean _isProvider;
    @Json(name = "lastname")
    private String _lastname;
    @Json(name = "marketplaceStatusOn")
    private Boolean _marketplaceStatusOn;
    @Json(name = "newNotificationCount")
    private Integer _newNotificationCount;
    @Json(name = "phone")
    private String _phone;
    @Json(name = "photo")
    private Photo _photo;
    @Json(name = "rating")
    private Double _rating;
    @Json(name = "ratingsTotal")
    private Integer _ratingsTotal;
    @Json(name = "state")
    private String _state;
    @Json(name = "tagline")
    private String _tagline;
    @Json(name = "tosRequiredBy")
    private String _tosRequiredBy;
    @Json(name = "unreadMessageCount")
    private Integer _unreadMessageCount;
    @Json(name = "userId")
    private Integer _userId;
    @Json(name = "workordersTotal")
    private Integer _workordersTotal;

    public Profile() {
    }

    public Boolean getAcceptedTos() {
        return _acceptedTos;
    }

    public String getCity() {
        return _city;
    }

    public String getDescription() {
        return _description;
    }

    public String getEmail() {
        return _email;
    }

    public String getFirstname() {
        return _firstname;
    }

    public Boolean hasValidCoi() {
        if (_hasValidCoi != null)
            return _hasValidCoi;

        return false;
    }

    public Float insurancePercent() {
        if (_insurancePercent != null)
            return _insurancePercent;

        return 1.3F;
    }

    public Boolean isProvider() {
        return _isProvider;
    }

    public String getLastname() {
        return _lastname;
    }

    public Boolean getMarketplaceStatusOn() {
        return _marketplaceStatusOn;
    }

    public Integer getNewNotificationCount() {
        return _newNotificationCount;
    }

    public String getPhone() {
        return _phone;
    }

    public Photo getPhoto() {
        return _photo;
    }

    public Double getRating() {
        return _rating;
    }

    public Integer getRatingsTotal() {
        return _ratingsTotal;
    }

    public String getState() {
        return _state;
    }

    public String getTagline() {
        return _tagline;
    }

    public String getTosRequiredBy() {
        return _tosRequiredBy;
    }

    public Integer getUnreadMessageCount() {
        return _unreadMessageCount;
    }

    public Integer getUserId() {
        return _userId;
    }

    public Integer getWorkordersTotal() {
        return _workordersTotal;
    }

    public JsonObject toJson() {
        return toJson(this);
    }

    public static JsonObject toJson(Profile profile) {
        try {
            return Serializer.serializeObject(profile);
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public static Profile fromJson(JsonObject json) {
        try {
            return Serializer.unserializeObject(Profile.class, json);
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    /*-*****************************-*/
    /*-			Human Code			-*/
    /*-*****************************-*/
    public boolean isTosRequired() {
        if (_acceptedTos) {
            return false;
        }

        if (_tosRequiredBy == null) {
            return false;
        }

        if (daysUntilRequired() <= 0)
            return true;

        try {
            return System.currentTimeMillis() >= ISO8601.toUtc(_tosRequiredBy);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return false;
    }

    public int daysUntilRequired() {
        if (_acceptedTos) {
            return 0;
        }

        if (_tosRequiredBy == null) {
            return 0;
        }

        try {
            return (int) ((ISO8601.toUtc(_tosRequiredBy) - System.currentTimeMillis()) / 86400000);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return 0;
    }

    /*-*********************************************-*/
    /*-			Parcelable Implementation			-*/
    /*-*********************************************-*/

    public static final Parcelable.Creator<Profile> CREATOR = new Parcelable.Creator<Profile>() {

        @Override
        public Profile createFromParcel(Parcel source) {
            try {
                return Profile.fromJson(new JsonObject(source.readString()));
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            return null;
        }

        @Override
        public Profile[] newArray(int size) {
            return new Profile[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(toJson().toString());
    }
}

