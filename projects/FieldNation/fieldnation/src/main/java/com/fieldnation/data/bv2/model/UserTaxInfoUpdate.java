package com.fieldnation.data.bv2.model;

import com.fieldnation.fnjson.JsonObject;
import com.fieldnation.fnjson.Serializer;
import com.fieldnation.fnjson.Unserializer;
import com.fieldnation.fnjson.annotations.Json;
import com.fieldnation.fnlog.Log;

public class UserTaxInfoUpdate {
    private static final String TAG = "UserTaxInfoUpdate";

    @Json(name = "first_name")
    private String firstName = null;

    @Json(name = "last_name")
    private String lastName = null;

    @Json(name = "phone")
    private String phone = null;

    @Json(name = "name")
    private String name = null;

    @Json(name = "business_name")
    private String businessName = null;

    @Json(name = "entity_type")
    private String entityType = null;

    @Json(name = "other")
    private String other = null;

    @Json(name = "address1")
    private String address1 = null;

    @Json(name = "address2")
    private String address2 = null;

    @Json(name = "city")
    private String city = null;

    @Json(name = "state")
    private String state = null;

    @Json(name = "zip")
    private Integer zip = null;

    @Json(name = "has_ein")
    private Integer hasEin = null;

    @Json(name = "tin")
    private String tin = null;

    @Json(name = "electronic_consent")
    private Integer electronicConsent = null;

    @Json(name = "has_separate_tax")
    private Integer hasSeparateTax = null;

    @Json(name = "technician_w9_status_id")
    private Integer technicianW9StatusId = null;

    @Json(name = "company_id")
    private Integer companyId = null;

    @Json(name = "from_vendor_buyer_invite")
    private Integer fromVendorBuyerInvite = null;

    @Json(name = "buyer_company_id")
    private Integer buyerCompanyId = null;

    @Json(name = "vendor_company_id")
    private Integer vendorCompanyId = null;

    public UserTaxInfoUpdate() {
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getPhone() {
        return phone;
    }

    public String getName() {
        return name;
    }

    public String getBusinessName() {
        return businessName;
    }

    public String getEntityType() {
        return entityType;
    }

    public String getOther() {
        return other;
    }

    public String getAddress1() {
        return address1;
    }

    public String getAddress2() {
        return address2;
    }

    public String getCity() {
        return city;
    }

    public String getState() {
        return state;
    }

    public Integer getZip() {
        return zip;
    }

    public Integer getHasEin() {
        return hasEin;
    }

    public String getTin() {
        return tin;
    }

    public Integer getElectronicConsent() {
        return electronicConsent;
    }

    public Integer getHasSeparateTax() {
        return hasSeparateTax;
    }

    public Integer getTechnicianW9StatusId() {
        return technicianW9StatusId;
    }

    public Integer getCompanyId() {
        return companyId;
    }

    public Integer getFromVendorBuyerInvite() {
        return fromVendorBuyerInvite;
    }

    public Integer getBuyerCompanyId() {
        return buyerCompanyId;
    }

    public Integer getVendorCompanyId() {
        return vendorCompanyId;
    }

    /*-*****************************-*/
    /*-             Json            -*/
    /*-*****************************-*/
    public static UserTaxInfoUpdate fromJson(JsonObject obj) {
        try {
            return Unserializer.unserializeObject(UserTaxInfoUpdate.class, obj);
        } catch (Exception ex) {
            Log.v(TAG, ex);
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
            Log.v(TAG, ex);
            return null;
        }
    }
}