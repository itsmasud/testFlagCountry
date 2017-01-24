package com.fieldnation.data.bv2.model;

import com.fieldnation.fnjson.JsonObject;
import com.fieldnation.fnjson.Serializer;
import com.fieldnation.fnjson.Unserializer;
import com.fieldnation.fnjson.annotations.Json;
import com.fieldnation.fnlog.Log;

public class UserTaxInfo {
    private static final String TAG = "UserTaxInfo";

    @Json(name = "zip")
    private Integer zip;

    @Json(name = "business_name")
    private String businessName;

    @Json(name = "other")
    private String other;

    @Json(name = "address2")
    private String address2;

    @Json(name = "city")
    private String city;

    @Json(name = "address1")
    private String address1;

    @Json(name = "tech_w9_id")
    private Integer techW9Id;

    @Json(name = "last_name")
    private String lastName;

    @Json(name = "electronic_consent")
    private Integer electronicConsent;

    @Json(name = "has_ein")
    private Integer hasEin;

    @Json(name = "tech_user_id")
    private Integer techUserId;

    @Json(name = "entity_type")
    private String entityType;

    @Json(name = "user_id")
    private Integer userId;

    @Json(name = "phone")
    private String phone;

    @Json(name = "files_separate_business_taxes")
    private Integer filesSeparateBusinessTaxes;

    @Json(name = "name")
    private String name;

    @Json(name = "tin")
    private String tin;

    @Json(name = "technician_w9_status")
    private TechnicianW9Status[] technicianW9Status;

    @Json(name = "state")
    private String state;

    @Json(name = "technician_w9_status_id")
    private Integer technicianW9StatusId;

    @Json(name = "first_name")
    private String firstName;

    public UserTaxInfo() {
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

    public String getAddress2() {
        return address2;
    }

    public String getCity() {
        return city;
    }

    public String getAddress1() {
        return address1;
    }

    public Integer getTechW9Id() {
        return techW9Id;
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

    public Integer getTechUserId() {
        return techUserId;
    }

    public String getEntityType() {
        return entityType;
    }

    public Integer getUserId() {
        return userId;
    }

    public String getPhone() {
        return phone;
    }

    public Integer getFilesSeparateBusinessTaxes() {
        return filesSeparateBusinessTaxes;
    }

    public String getName() {
        return name;
    }

    public String getTin() {
        return tin;
    }

    public TechnicianW9Status[] getTechnicianW9Status() {
        return technicianW9Status;
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

    /*-*****************************-*/
    /*-             Json            -*/
    /*-*****************************-*/
    public static UserTaxInfo fromJson(JsonObject obj) {
        try {
            return Unserializer.unserializeObject(UserTaxInfo.class, obj);
        } catch (Exception ex) {
            Log.v(TAG, ex);
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
            Log.v(TAG, ex);
            return null;
        }
    }
}
