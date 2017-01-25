package com.fieldnation.data.bv2.model;

import com.fieldnation.fnjson.JsonObject;
import com.fieldnation.fnjson.Serializer;
import com.fieldnation.fnjson.Unserializer;
import com.fieldnation.fnjson.annotations.Json;
import com.fieldnation.fnlog.Log;

public class UserTaxInfo {
    private static final String TAG = "UserTaxInfo";

    @Json(name = "zip")
    private Integer _zip;

    @Json(name = "business_name")
    private String _businessName;

    @Json(name = "other")
    private String _other;

    @Json(name = "address2")
    private String _address2;

    @Json(name = "city")
    private String _city;

    @Json(name = "address1")
    private String _address1;

    @Json(name = "tech_w9_id")
    private Integer _techW9Id;

    @Json(name = "last_name")
    private String _lastName;

    @Json(name = "electronic_consent")
    private Integer _electronicConsent;

    @Json(name = "has_ein")
    private Integer _hasEin;

    @Json(name = "tech_user_id")
    private Integer _techUserId;

    @Json(name = "entity_type")
    private String _entityType;

    @Json(name = "user_id")
    private Integer _userId;

    @Json(name = "phone")
    private String _phone;

    @Json(name = "files_separate_business_taxes")
    private Integer _filesSeparateBusinessTaxes;

    @Json(name = "name")
    private String _name;

    @Json(name = "tin")
    private String _tin;

    @Json(name = "technician_w9_status")
    private technicianW9Status[] _technicianW9Status;

    @Json(name = "state")
    private String _state;

    @Json(name = "technician_w9_status_id")
    private Integer _technicianW9StatusId;

    @Json(name = "first_name")
    private String _firstName;

    public UserTaxInfo() {
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

    public String getAddress2() {
        return _address2;
    }

    public String getCity() {
        return _city;
    }

    public String getAddress1() {
        return _address1;
    }

    public Integer getTechW9Id() {
        return _techW9Id;
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

    public Integer getTechUserId() {
        return _techUserId;
    }

    public String getEntityType() {
        return _entityType;
    }

    public Integer getUserId() {
        return _userId;
    }

    public String getPhone() {
        return _phone;
    }

    public Integer getFilesSeparateBusinessTaxes() {
        return _filesSeparateBusinessTaxes;
    }

    public String getName() {
        return _name;
    }

    public String getTin() {
        return _tin;
    }

    public technicianW9Status[] getTechnicianW9Status() {
        return _technicianW9Status;
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

    /*-*****************************-*/
    /*-             Json            -*/
    /*-*****************************-*/
    public static UserTaxInfo fromJson(JsonObject obj) {
        try {
            return Unserializer.unserializeObject(UserTaxInfo.class, obj);
        } catch (Exception ex) {
            Log.v(TAG, TAG, ex);
            return null;
        }
    }

    public JsonObject toJson() {
        return toJson(this);
    }

    public static JsonObject toJson(UserTaxInfo userTaxInfo) {
        try {
            return Serializer.serializeObject(userTaxInfo);
        } catch (Exception ex) {
            Log.v(TAG, TAG, ex);
            return null;
        }
    }
}
