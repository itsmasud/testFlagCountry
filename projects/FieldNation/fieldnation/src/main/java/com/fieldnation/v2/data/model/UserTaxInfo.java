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
 * Created by dmgen from swagger.
 */

public class UserTaxInfo implements Parcelable {
    private static final String TAG = "UserTaxInfo";

    @Json(name = "address1")
    private String _address1;

    @Json(name = "address2")
    private String _address2;

    @Json(name = "business_name")
    private String _businessName;

    @Json(name = "city")
    private String _city;

    @Json(name = "electronic_consent")
    private Integer _electronicConsent;

    @Json(name = "entity_type")
    private String _entityType;

    @Json(name = "files_separate_business_taxes")
    private Integer _filesSeparateBusinessTaxes;

    @Json(name = "first_name")
    private String _firstName;

    @Json(name = "has_ein")
    private Integer _hasEin;

    @Json(name = "last_name")
    private String _lastName;

    @Json(name = "name")
    private String _name;

    @Json(name = "other")
    private String _other;

    @Json(name = "phone")
    private String _phone;

    @Json(name = "state")
    private String _state;

    @Json(name = "tech_user_id")
    private Integer _techUserId;

    @Json(name = "tech_w9_id")
    private Integer _techW9Id;

    @Json(name = "technician_w9_status")
    private technicianW9Status[] _technicianW9Status;

    @Json(name = "technician_w9_status_id")
    private Integer _technicianW9StatusId;

    @Json(name = "tin")
    private String _tin;

    @Json(name = "user_id")
    private Integer _userId;

    @Json(name = "zip")
    private Integer _zip;

    public UserTaxInfo() {
    }

    public void setAddress1(String address1) {
        _address1 = address1;
    }

    public String getAddress1() {
        return _address1;
    }

    public UserTaxInfo address1(String address1) {
        _address1 = address1;
        return this;
    }

    public void setAddress2(String address2) {
        _address2 = address2;
    }

    public String getAddress2() {
        return _address2;
    }

    public UserTaxInfo address2(String address2) {
        _address2 = address2;
        return this;
    }

    public void setBusinessName(String businessName) {
        _businessName = businessName;
    }

    public String getBusinessName() {
        return _businessName;
    }

    public UserTaxInfo businessName(String businessName) {
        _businessName = businessName;
        return this;
    }

    public void setCity(String city) {
        _city = city;
    }

    public String getCity() {
        return _city;
    }

    public UserTaxInfo city(String city) {
        _city = city;
        return this;
    }

    public void setElectronicConsent(Integer electronicConsent) {
        _electronicConsent = electronicConsent;
    }

    public Integer getElectronicConsent() {
        return _electronicConsent;
    }

    public UserTaxInfo electronicConsent(Integer electronicConsent) {
        _electronicConsent = electronicConsent;
        return this;
    }

    public void setEntityType(String entityType) {
        _entityType = entityType;
    }

    public String getEntityType() {
        return _entityType;
    }

    public UserTaxInfo entityType(String entityType) {
        _entityType = entityType;
        return this;
    }

    public void setFilesSeparateBusinessTaxes(Integer filesSeparateBusinessTaxes) {
        _filesSeparateBusinessTaxes = filesSeparateBusinessTaxes;
    }

    public Integer getFilesSeparateBusinessTaxes() {
        return _filesSeparateBusinessTaxes;
    }

    public UserTaxInfo filesSeparateBusinessTaxes(Integer filesSeparateBusinessTaxes) {
        _filesSeparateBusinessTaxes = filesSeparateBusinessTaxes;
        return this;
    }

    public void setFirstName(String firstName) {
        _firstName = firstName;
    }

    public String getFirstName() {
        return _firstName;
    }

    public UserTaxInfo firstName(String firstName) {
        _firstName = firstName;
        return this;
    }

    public void setHasEin(Integer hasEin) {
        _hasEin = hasEin;
    }

    public Integer getHasEin() {
        return _hasEin;
    }

    public UserTaxInfo hasEin(Integer hasEin) {
        _hasEin = hasEin;
        return this;
    }

    public void setLastName(String lastName) {
        _lastName = lastName;
    }

    public String getLastName() {
        return _lastName;
    }

    public UserTaxInfo lastName(String lastName) {
        _lastName = lastName;
        return this;
    }

    public void setName(String name) {
        _name = name;
    }

    public String getName() {
        return _name;
    }

    public UserTaxInfo name(String name) {
        _name = name;
        return this;
    }

    public void setOther(String other) {
        _other = other;
    }

    public String getOther() {
        return _other;
    }

    public UserTaxInfo other(String other) {
        _other = other;
        return this;
    }

    public void setPhone(String phone) {
        _phone = phone;
    }

    public String getPhone() {
        return _phone;
    }

    public UserTaxInfo phone(String phone) {
        _phone = phone;
        return this;
    }

    public void setState(String state) {
        _state = state;
    }

    public String getState() {
        return _state;
    }

    public UserTaxInfo state(String state) {
        _state = state;
        return this;
    }

    public void setTechUserId(Integer techUserId) {
        _techUserId = techUserId;
    }

    public Integer getTechUserId() {
        return _techUserId;
    }

    public UserTaxInfo techUserId(Integer techUserId) {
        _techUserId = techUserId;
        return this;
    }

    public void setTechW9Id(Integer techW9Id) {
        _techW9Id = techW9Id;
    }

    public Integer getTechW9Id() {
        return _techW9Id;
    }

    public UserTaxInfo techW9Id(Integer techW9Id) {
        _techW9Id = techW9Id;
        return this;
    }

    public void setTechnicianW9Status(technicianW9Status[] technicianW9Status) {
        _technicianW9Status = technicianW9Status;
    }

    public technicianW9Status[] getTechnicianW9Status() {
        return _technicianW9Status;
    }

    public UserTaxInfo technicianW9Status(technicianW9Status[] technicianW9Status) {
        _technicianW9Status = technicianW9Status;
        return this;
    }

    public void setTechnicianW9StatusId(Integer technicianW9StatusId) {
        _technicianW9StatusId = technicianW9StatusId;
    }

    public Integer getTechnicianW9StatusId() {
        return _technicianW9StatusId;
    }

    public UserTaxInfo technicianW9StatusId(Integer technicianW9StatusId) {
        _technicianW9StatusId = technicianW9StatusId;
        return this;
    }

    public void setTin(String tin) {
        _tin = tin;
    }

    public String getTin() {
        return _tin;
    }

    public UserTaxInfo tin(String tin) {
        _tin = tin;
        return this;
    }

    public void setUserId(Integer userId) {
        _userId = userId;
    }

    public Integer getUserId() {
        return _userId;
    }

    public UserTaxInfo userId(Integer userId) {
        _userId = userId;
        return this;
    }

    public void setZip(Integer zip) {
        _zip = zip;
    }

    public Integer getZip() {
        return _zip;
    }

    public UserTaxInfo zip(Integer zip) {
        _zip = zip;
        return this;
    }

    /*-*****************************-*/
    /*-             Json            -*/
    /*-*****************************-*/
    public static UserTaxInfo[] fromJsonArray(JsonArray array) {
        UserTaxInfo[] list = new UserTaxInfo[array.size()];
        for (int i = 0; i < array.size(); i++) {
            list[i] = fromJson(array.getJsonObject(i));
        }
        return list;
    }

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

    /*-*********************************************-*/
    /*-			Parcelable Implementation           -*/
    /*-*********************************************-*/
    public static final Parcelable.Creator<UserTaxInfo> CREATOR = new Parcelable.Creator<UserTaxInfo>() {

        @Override
        public UserTaxInfo createFromParcel(Parcel source) {
            try {
                return UserTaxInfo.fromJson((JsonObject) source.readParcelable(JsonObject.class.getClassLoader()));
            } catch (Exception ex) {
                Log.v(TAG, ex);
                return null;
            }
        }

        @Override
        public UserTaxInfo[] newArray(int size) {
            return new UserTaxInfo[size];
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
