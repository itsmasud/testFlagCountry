package com.fieldnation.data.bv2.model;

import com.fieldnation.fnjson.JsonObject;
import com.fieldnation.fnjson.Serializer;
import com.fieldnation.fnjson.Unserializer;
import com.fieldnation.fnjson.annotations.Json;
import com.fieldnation.fnlog.Log;

public class UserTaxInfoUpdate {
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

    public Integer getZip() {
        return _zip;
    }

    public String getBusinessName() {
        return _businessName;
    }

    public String getOther() {
        return _other;
    }

    public Integer getCompanyId() {
        return _companyId;
    }

    public String getAddress2() {
        return _address2;
    }

    public String getCity() {
        return _city;
    }

    public String getAddress1() {
        return _address1;
    }

    public String getLastName() {
        return _lastName;
    }

    public Integer getElectronicConsent() {
        return _electronicConsent;
    }

    public Integer getHasEin() {
        return _hasEin;
    }

    public Integer getFromVendorBuyerInvite() {
        return _fromVendorBuyerInvite;
    }

    public Integer getVendorCompanyId() {
        return _vendorCompanyId;
    }

    public String getEntityType() {
        return _entityType;
    }

    public String getPhone() {
        return _phone;
    }

    public String getName() {
        return _name;
    }

    public String getTin() {
        return _tin;
    }

    public Integer getHasSeparateTax() {
        return _hasSeparateTax;
    }

    public String getState() {
        return _state;
    }

    public Integer getTechnicianW9StatusId() {
        return _technicianW9StatusId;
    }

    public String getFirstName() {
        return _firstName;
    }

    public Integer getBuyerCompanyId() {
        return _buyerCompanyId;
    }

    /*-*****************************-*/
    /*-             Json            -*/
    /*-*****************************-*/
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
}
