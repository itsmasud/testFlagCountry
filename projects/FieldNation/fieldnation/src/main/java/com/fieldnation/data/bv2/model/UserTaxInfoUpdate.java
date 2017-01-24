package com.fieldnation.data.bv2.model;

import com.fieldnation.fnjson.JsonObject;
import com.fieldnation.fnjson.Serializer;
import com.fieldnation.fnjson.Unserializer;
import com.fieldnation.fnjson.annotations.Json;
import com.fieldnation.fnlog.Log;

public class UserTaxInfoUpdate {
    private static final String TAG = "UserTaxInfoUpdate";

    @Json(name = "zip")
    private Integer zip;

    @Json(name = "business_name")
    private String businessName;

    @Json(name = "other")
    private String other;

    @Json(name = "company_id")
    private Integer companyId;

    @Json(name = "address2")
    private String address2;

    @Json(name = "city")
    private String city;

    @Json(name = "address1")
    private String address1;

    @Json(name = "last_name")
    private String lastName;

    @Json(name = "electronic_consent")
    private Integer electronicConsent;

    @Json(name = "has_ein")
    private Integer hasEin;

    @Json(name = "from_vendor_buyer_invite")
    private Integer fromVendorBuyerInvite;

    @Json(name = "vendor_company_id")
    private Integer vendorCompanyId;

    @Json(name = "entity_type")
    private String entityType;

    @Json(name = "phone")
    private String phone;

    @Json(name = "name")
    private String name;

    @Json(name = "tin")
    private String tin;

    @Json(name = "has_separate_tax")
    private Integer hasSeparateTax;

    @Json(name = "state")
    private String state;

    @Json(name = "technician_w9_status_id")
    private Integer technicianW9StatusId;

    @Json(name = "first_name")
    private String firstName;

    @Json(name = "buyer_company_id")
    private Integer buyerCompanyId;

    public UserTaxInfoUpdate() {
    }

    public Integer getZip() {
        return zip;
    }

    public String getBusinessName() {
        return businessName;
    }

    public String getOther() {
        return other;
    }

    public Integer getCompanyId() {
        return companyId;
    }

    public String getAddress2() {
        return address2;
    }

    public String getCity() {
        return city;
    }

    public String getAddress1() {
        return address1;
    }

    public String getLastName() {
        return lastName;
    }

    public Integer getElectronicConsent() {
        return electronicConsent;
    }

    public Integer getHasEin() {
        return hasEin;
    }

    public Integer getFromVendorBuyerInvite() {
        return fromVendorBuyerInvite;
    }

    public Integer getVendorCompanyId() {
        return vendorCompanyId;
    }

    public String getEntityType() {
        return entityType;
    }

    public String getPhone() {
        return phone;
    }

    public String getName() {
        return name;
    }

    public String getTin() {
        return tin;
    }

    public Integer getHasSeparateTax() {
        return hasSeparateTax;
    }

    public String getState() {
        return state;
    }

    public Integer getTechnicianW9StatusId() {
        return technicianW9StatusId;
    }

    public String getFirstName() {
        return firstName;
    }

    public Integer getBuyerCompanyId() {
        return buyerCompanyId;
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
