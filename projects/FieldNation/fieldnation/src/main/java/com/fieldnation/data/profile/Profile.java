package com.fieldnation.data.profile;

import android.os.Parcel;
import android.os.Parcelable;

import com.fieldnation.fnjson.JsonObject;
import com.fieldnation.fnjson.Serializer;
import com.fieldnation.fnjson.Unserializer;
import com.fieldnation.fnjson.annotations.Json;
import com.fieldnation.fnlog.Log;
import com.fieldnation.fntools.ISO8601;

public class Profile implements Parcelable {
    private static final String TAG = "Profile";

    @Json(name = "acceptedTos")
    private Boolean _acceptedTos;
    @Json(name = "canViewPayments")
    private Boolean _canViewPayments;
    @Json(name = "city")
    private String _city;
    @Json(name = "companyId")
    private Integer _companyId;
    @Json(name = "companyName")
    private String _companyName;
    @Json(name = "description")
    private String _description;
    @Json(name = "email")
    private String _email;
    @Json(name = "firstname")
    private String _firstname;
    @Json(name = "iCalUrl")
    private String _iCalUrl;
    @Json(name = "hasValidCOI")
    private Boolean _hasValidCoi;
    @Json(name = "insurancePercent")
    private Float _insurancePercent;
    @Json(name = "isProvider")
    private Boolean _isProvider;
    @Json(name = "lastname")
    private String _lastname;
    @Json(name = "managedProviders")
    private Profile[] _managedProviders;
    @Json(name = "marketplaceStatusExplanation")
    private String _marketplaceStatusExplanation;
    @Json(name = "marketplaceStatusOn")
    private Boolean _marketplaceStatusOn;
    @Json(name = "marketplaceStatusReason")
    private String _marketplaceStatusReason;
    @Json(name = "newNotificationCount")
    private Integer _newNotificationCount;
    @Json(name = "phone")
    private String _phone;
    @Json(name = "phoneExt")
    private String _phoneExt;
    @Json(name = "photo")
    private Photo _photo;
    @Json(name = "rating")
    private Double _rating;
    @Json(name = "ratingsTotal")
    private Integer _ratingsTotal;
    @Json(name = "address1")
    private String _address1;
    @Json(name = "address2")
    private String _address2;
    @Json(name = "state")
    private String _state;
    @Json(name = "zip")
    private String _zip;
    @Json(name = "tagline")
    private String _tagline;
    @Json(name = "tosRequiredBy")
    private String _tosRequiredBy;
    @Json(name = "unreadMessageCount")
    private Integer _unreadMessageCount;
    @Json(name = "userId")
    private Long _userId;
    @Json(name = "webCalUrl")
    private String _webCalUrl;
    @Json(name = "workordersTotal")
    private Integer _workordersTotal;
    @Json(name = "canRequestWorkOnMarketplace")
    private Boolean _canRequestWorkOnMarketplace;


    public Profile() {
    }

    public Boolean getCanViewPayments() {
        if (_canViewPayments == null)
            return true;

        return _canViewPayments;
    }

    public String getCity() {
        return _city;
    }

    public Integer getCompanyId() {
        return _companyId;
    }

    public String getCompanyName() {
        return _companyName;
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

    public String getICalUrl() {
        return _iCalUrl;
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

    public Profile[] getManagedProviders() {
        return _managedProviders;
    }

    public String getMarketplaceStatusExplanation() {
        return _marketplaceStatusExplanation;
    }

    public Boolean getMarketplaceStatusOn() {
        if (_marketplaceStatusOn == null)
            return true;

        return _marketplaceStatusOn;
    }

    public String getMarketplaceStatusReason() {
        return _marketplaceStatusReason;
    }

    public Integer getNewNotificationCount() {
        return _newNotificationCount;
    }

    public String getPhone() {
        return _phone;
    }

    public String getPhoneExt() {
        return _phoneExt;
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

    public String getAddress1() {
        return _address1;
    }

    public String getAddress2() {
        return _address2;
    }

    public String getState() {
        return _state;
    }

    public String getZip() {
        return _zip;
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

    public Long getUserId() {
        return _userId;
    }

    public String getWebCalUrl() {
        return _webCalUrl;
    }

    public Integer getWorkordersTotal() {
        return _workordersTotal;
    }

    public Boolean canRequestWorkOnMarketplace() {
        if (_canRequestWorkOnMarketplace != null) {
            return _canRequestWorkOnMarketplace;
        }
        return true;
    }

    public JsonObject toJson() {
        return toJson(this);
    }

    public static JsonObject toJson(Profile profile) {
        try {
            return Serializer.serializeObject(profile);
        } catch (Exception ex) {
            Log.v(TAG, ex);
            return null;
        }
    }

    public static Profile fromJson(JsonObject json) {
        try {
            return Unserializer.unserializeObject(Profile.class, json);
        } catch (Exception ex) {
            Log.v(TAG, ex);
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
            Log.v(TAG, ex);
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
            Log.v(TAG, ex);
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
                return Profile.fromJson((JsonObject) (source.readParcelable(JsonObject.class.getClassLoader())));
            } catch (Exception ex) {
                Log.v(TAG, ex);
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
        dest.writeParcelable(toJson(), flags);
    }
}

