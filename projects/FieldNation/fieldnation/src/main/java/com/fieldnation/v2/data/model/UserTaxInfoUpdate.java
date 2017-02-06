package com.fieldnation.v2.data.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.fieldnation.fnjson.JsonArray;
import com.fieldnation.fnjson.JsonObject;
import com.fieldnation.fnjson.Serializer;
import com.fieldnation.fnjson.Unserializer;
import com.fieldnation.fnjson.annotations.Json;
import com.fieldnation.fnlog.Log;

/**
 * Created by dmgen from swagger on 1/31/17.
 */

public class UserTaxInfoUpdate implements Parcelable {
    private static final String TAG = "UserTaxInfoUpdate";

    @Json(name = "zip")
    private Integer _zip;

    @Json(name = "business_name")
    private String _businessName;

    @Json(name = "other")
    private String _other;

    @Json(name = "company_id")
    private Integer _companyId;

    @Json(name = "address2")
    private String _address2;

    @Json(name = "city")
    private String _city;

    @Json(name = "address1")
    private String _address1;

    @Json(name = "last_name")
    private String _lastName;

    @Json(name = "electronic_consent")
    private Integer _electronicConsent;

    @Json(name = "has_ein")
    private Integer _hasEin;

    @Json(name = "from_vendor_buyer_invite")
    private Integer _fromVendorBuyerInvite;

    @Json(name = "vendor_company_id")
    private Integer _vendorCompanyId;

    @Json(name = "entity_type")
    private String _entityType;

    @Json(name = "phone")
    private String _phone;

    @Json(name = "name")
    private String _name;

    @Json(name = "tin")
    private String _tin;

    @Json(name = "has_separate_tax")
    private Integer _hasSeparateTax;

    @Json(name = "state")
    private String _state;

    @Json(name = "technician_w9_status_id")
    private Integer _technicianW9StatusId;

    @Json(name = "first_name")
    private String _firstName;

    @Json(name = "buyer_company_id")
    private Integer _buyerCompanyId;

    public UserTaxInfoUpdate() {
    }

    public void setZip(Integer zip) {
        _zip = zip;
    }

    public Integer getZip() {
        return _zip;
    }

    public UserTaxInfoUpdate zip(Integer zip) {
        _zip = zip;
        return this;
    }

    public void setBusinessName(String businessName) {
        _businessName = businessName;
    }

    public String getBusinessName() {
        return _businessName;
    }

    public UserTaxInfoUpdate businessName(String businessName) {
        _businessName = businessName;
        return this;
    }

    public void setOther(String other) {
        _other = other;
    }

    public String getOther() {
        return _other;
    }

    public UserTaxInfoUpdate other(String other) {
        _other = other;
        return this;
    }

    public void setCompanyId(Integer companyId) {
        _companyId = companyId;
    }

    public Integer getCompanyId() {
        return _companyId;
    }

    public UserTaxInfoUpdate companyId(Integer companyId) {
        _companyId = companyId;
        return this;
    }

    public void setAddress2(String address2) {
        _address2 = address2;
    }

    public String getAddress2() {
        return _address2;
    }

    public UserTaxInfoUpdate address2(String address2) {
        _address2 = address2;
        return this;
    }

    public void setCity(String city) {
        _city = city;
    }

    public String getCity() {
        return _city;
    }

    public UserTaxInfoUpdate city(String city) {
        _city = city;
        return this;
    }

    public void setAddress1(String address1) {
        _address1 = address1;
    }

    public String getAddress1() {
        return _address1;
    }

    public UserTaxInfoUpdate address1(String address1) {
        _address1 = address1;
        return this;
    }

    public void setLastName(String lastName) {
        _lastName = lastName;
    }

    public String getLastName() {
        return _lastName;
    }

    public UserTaxInfoUpdate lastName(String lastName) {
        _lastName = lastName;
        return this;
    }

    public void setElectronicConsent(Integer electronicConsent) {
        _electronicConsent = electronicConsent;
    }

    public Integer getElectronicConsent() {
        return _electronicConsent;
    }

    public UserTaxInfoUpdate electronicConsent(Integer electronicConsent) {
        _electronicConsent = electronicConsent;
        return this;
    }

    public void setHasEin(Integer hasEin) {
        _hasEin = hasEin;
    }

    public Integer getHasEin() {
        return _hasEin;
    }

    public UserTaxInfoUpdate hasEin(Integer hasEin) {
        _hasEin = hasEin;
        return this;
    }

    public void setFromVendorBuyerInvite(Integer fromVendorBuyerInvite) {
        _fromVendorBuyerInvite = fromVendorBuyerInvite;
    }

    public Integer getFromVendorBuyerInvite() {
        return _fromVendorBuyerInvite;
    }

    public UserTaxInfoUpdate fromVendorBuyerInvite(Integer fromVendorBuyerInvite) {
        _fromVendorBuyerInvite = fromVendorBuyerInvite;
        return this;
    }

    public void setVendorCompanyId(Integer vendorCompanyId) {
        _vendorCompanyId = vendorCompanyId;
    }

    public Integer getVendorCompanyId() {
        return _vendorCompanyId;
    }

    public UserTaxInfoUpdate vendorCompanyId(Integer vendorCompanyId) {
        _vendorCompanyId = vendorCompanyId;
        return this;
    }

    public void setEntityType(String entityType) {
        _entityType = entityType;
    }

    public String getEntityType() {
        return _entityType;
    }

    public UserTaxInfoUpdate entityType(String entityType) {
        _entityType = entityType;
        return this;
    }

    public void setPhone(String phone) {
        _phone = phone;
    }

    public String getPhone() {
        return _phone;
    }

    public UserTaxInfoUpdate phone(String phone) {
        _phone = phone;
        return this;
    }

    public void setName(String name) {
        _name = name;
    }

    public String getName() {
        return _name;
    }

    public UserTaxInfoUpdate name(String name) {
        _name = name;
        return this;
    }

    public void setTin(String tin) {
        _tin = tin;
    }

    public String getTin() {
        return _tin;
    }

    public UserTaxInfoUpdate tin(String tin) {
        _tin = tin;
        return this;
    }

    public void setHasSeparateTax(Integer hasSeparateTax) {
        _hasSeparateTax = hasSeparateTax;
    }

    public Integer getHasSeparateTax() {
        return _hasSeparateTax;
    }

    public UserTaxInfoUpdate hasSeparateTax(Integer hasSeparateTax) {
        _hasSeparateTax = hasSeparateTax;
        return this;
    }

    public void setState(String state) {
        _state = state;
    }

    public String getState() {
        return _state;
    }

    public UserTaxInfoUpdate state(String state) {
        _state = state;
        return this;
    }

    public void setTechnicianW9StatusId(Integer technicianW9StatusId) {
        _technicianW9StatusId = technicianW9StatusId;
    }

    public Integer getTechnicianW9StatusId() {
        return _technicianW9StatusId;
    }

    public UserTaxInfoUpdate technicianW9StatusId(Integer technicianW9StatusId) {
        _technicianW9StatusId = technicianW9StatusId;
        return this;
    }

    public void setFirstName(String firstName) {
        _firstName = firstName;
    }

    public String getFirstName() {
        return _firstName;
    }

    public UserTaxInfoUpdate firstName(String firstName) {
        _firstName = firstName;
        return this;
    }

    public void setBuyerCompanyId(Integer buyerCompanyId) {
        _buyerCompanyId = buyerCompanyId;
    }

    public Integer getBuyerCompanyId() {
        return _buyerCompanyId;
    }

    public UserTaxInfoUpdate buyerCompanyId(Integer buyerCompanyId) {
        _buyerCompanyId = buyerCompanyId;
        return this;
    }

    /*-*****************************-*/
    /*-             Json            -*/
    /*-*****************************-*/
    public static UserTaxInfoUpdate[] fromJsonArray(JsonArray array) {
        UserTaxInfoUpdate[] list = new UserTaxInfoUpdate[array.size()];
        for (int i = 0; i < array.size(); i++) {
            list[i] = fromJson(array.getJsonObject(i));
        }
        return list;
    }

    public static UserTaxInfoUpdate fromJson(JsonObject obj) {
        try {
            return Unserializer.unserializeObject(UserTaxInfoUpdate.class, obj);
        } catch (Exception ex) {
            Log.v(TAG, TAG, ex);
            return null;
        }
    }

    public JsonObject toJson() {
        return toJson(this);
    }

    public static JsonObject toJson(UserTaxInfoUpdate userTaxInfoUpdate) {
        try {
            return Serializer.serializeObject(userTaxInfoUpdate);
        } catch (Exception ex) {
            Log.v(TAG, TAG, ex);
            return null;
        }
    }

    /*-*********************************************-*/
    /*-			Parcelable Implementation           -*/
    /*-*********************************************-*/
    public static final Parcelable.Creator<UserTaxInfoUpdate> CREATOR = new Parcelable.Creator<UserTaxInfoUpdate>() {

        @Override
        public UserTaxInfoUpdate createFromParcel(Parcel source) {
            try {
                return UserTaxInfoUpdate.fromJson((JsonObject) source.readParcelable(JsonObject.class.getClassLoader()));
            } catch (Exception ex) {
                Log.v(TAG, ex);
                return null;
            }
        }

        @Override
        public UserTaxInfoUpdate[] newArray(int size) {
            return new UserTaxInfoUpdate[size];
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
