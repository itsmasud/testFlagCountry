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
import com.fieldnation.fntools.misc;

import java.text.ParseException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

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
    private TechnicianW9Status[] _technicianW9Status;

    @Json(name = "technician_w9_status_id")
    private Integer _technicianW9StatusId;

    @Json(name = "tin")
    private String _tin;

    @Json(name = "user_id")
    private Integer _userId;

    @Json(name = "zip")
    private Integer _zip;

    @Source
    private JsonObject SOURCE;

    public UserTaxInfo() {
        SOURCE = new JsonObject();
    }

    public UserTaxInfo(JsonObject obj) {
        SOURCE = obj;
    }

    public void setAddress1(String address1) throws ParseException {
        _address1 = address1;
        SOURCE.put("address1", address1);
    }

    public String getAddress1() {
        try {
            if (_address1 == null && SOURCE.has("address1") && SOURCE.get("address1") != null)
                _address1 = SOURCE.getString("address1");
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _address1;
    }

    public UserTaxInfo address1(String address1) throws ParseException {
        _address1 = address1;
        SOURCE.put("address1", address1);
        return this;
    }

    public void setAddress2(String address2) throws ParseException {
        _address2 = address2;
        SOURCE.put("address2", address2);
    }

    public String getAddress2() {
        try {
            if (_address2 == null && SOURCE.has("address2") && SOURCE.get("address2") != null)
                _address2 = SOURCE.getString("address2");
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _address2;
    }

    public UserTaxInfo address2(String address2) throws ParseException {
        _address2 = address2;
        SOURCE.put("address2", address2);
        return this;
    }

    public void setBusinessName(String businessName) throws ParseException {
        _businessName = businessName;
        SOURCE.put("business_name", businessName);
    }

    public String getBusinessName() {
        try {
            if (_businessName == null && SOURCE.has("business_name") && SOURCE.get("business_name") != null)
                _businessName = SOURCE.getString("business_name");
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _businessName;
    }

    public UserTaxInfo businessName(String businessName) throws ParseException {
        _businessName = businessName;
        SOURCE.put("business_name", businessName);
        return this;
    }

    public void setCity(String city) throws ParseException {
        _city = city;
        SOURCE.put("city", city);
    }

    public String getCity() {
        try {
            if (_city == null && SOURCE.has("city") && SOURCE.get("city") != null)
                _city = SOURCE.getString("city");
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _city;
    }

    public UserTaxInfo city(String city) throws ParseException {
        _city = city;
        SOURCE.put("city", city);
        return this;
    }

    public void setElectronicConsent(Integer electronicConsent) throws ParseException {
        _electronicConsent = electronicConsent;
        SOURCE.put("electronic_consent", electronicConsent);
    }

    public Integer getElectronicConsent() {
        try {
            if (_electronicConsent == null && SOURCE.has("electronic_consent") && SOURCE.get("electronic_consent") != null)
                _electronicConsent = SOURCE.getInt("electronic_consent");
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _electronicConsent;
    }

    public UserTaxInfo electronicConsent(Integer electronicConsent) throws ParseException {
        _electronicConsent = electronicConsent;
        SOURCE.put("electronic_consent", electronicConsent);
        return this;
    }

    public void setEntityType(String entityType) throws ParseException {
        _entityType = entityType;
        SOURCE.put("entity_type", entityType);
    }

    public String getEntityType() {
        try {
            if (_entityType == null && SOURCE.has("entity_type") && SOURCE.get("entity_type") != null)
                _entityType = SOURCE.getString("entity_type");
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _entityType;
    }

    public UserTaxInfo entityType(String entityType) throws ParseException {
        _entityType = entityType;
        SOURCE.put("entity_type", entityType);
        return this;
    }

    public void setFilesSeparateBusinessTaxes(Integer filesSeparateBusinessTaxes) throws ParseException {
        _filesSeparateBusinessTaxes = filesSeparateBusinessTaxes;
        SOURCE.put("files_separate_business_taxes", filesSeparateBusinessTaxes);
    }

    public Integer getFilesSeparateBusinessTaxes() {
        try {
            if (_filesSeparateBusinessTaxes == null && SOURCE.has("files_separate_business_taxes") && SOURCE.get("files_separate_business_taxes") != null)
                _filesSeparateBusinessTaxes = SOURCE.getInt("files_separate_business_taxes");
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _filesSeparateBusinessTaxes;
    }

    public UserTaxInfo filesSeparateBusinessTaxes(Integer filesSeparateBusinessTaxes) throws ParseException {
        _filesSeparateBusinessTaxes = filesSeparateBusinessTaxes;
        SOURCE.put("files_separate_business_taxes", filesSeparateBusinessTaxes);
        return this;
    }

    public void setFirstName(String firstName) throws ParseException {
        _firstName = firstName;
        SOURCE.put("first_name", firstName);
    }

    public String getFirstName() {
        try {
            if (_firstName == null && SOURCE.has("first_name") && SOURCE.get("first_name") != null)
                _firstName = SOURCE.getString("first_name");
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _firstName;
    }

    public UserTaxInfo firstName(String firstName) throws ParseException {
        _firstName = firstName;
        SOURCE.put("first_name", firstName);
        return this;
    }

    public void setHasEin(Integer hasEin) throws ParseException {
        _hasEin = hasEin;
        SOURCE.put("has_ein", hasEin);
    }

    public Integer getHasEin() {
        try {
            if (_hasEin == null && SOURCE.has("has_ein") && SOURCE.get("has_ein") != null)
                _hasEin = SOURCE.getInt("has_ein");
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _hasEin;
    }

    public UserTaxInfo hasEin(Integer hasEin) throws ParseException {
        _hasEin = hasEin;
        SOURCE.put("has_ein", hasEin);
        return this;
    }

    public void setLastName(String lastName) throws ParseException {
        _lastName = lastName;
        SOURCE.put("last_name", lastName);
    }

    public String getLastName() {
        try {
            if (_lastName == null && SOURCE.has("last_name") && SOURCE.get("last_name") != null)
                _lastName = SOURCE.getString("last_name");
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _lastName;
    }

    public UserTaxInfo lastName(String lastName) throws ParseException {
        _lastName = lastName;
        SOURCE.put("last_name", lastName);
        return this;
    }

    public void setName(String name) throws ParseException {
        _name = name;
        SOURCE.put("name", name);
    }

    public String getName() {
        try {
            if (_name == null && SOURCE.has("name") && SOURCE.get("name") != null)
                _name = SOURCE.getString("name");
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _name;
    }

    public UserTaxInfo name(String name) throws ParseException {
        _name = name;
        SOURCE.put("name", name);
        return this;
    }

    public void setOther(String other) throws ParseException {
        _other = other;
        SOURCE.put("other", other);
    }

    public String getOther() {
        try {
            if (_other == null && SOURCE.has("other") && SOURCE.get("other") != null)
                _other = SOURCE.getString("other");
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _other;
    }

    public UserTaxInfo other(String other) throws ParseException {
        _other = other;
        SOURCE.put("other", other);
        return this;
    }

    public void setPhone(String phone) throws ParseException {
        _phone = phone;
        SOURCE.put("phone", phone);
    }

    public String getPhone() {
        try {
            if (_phone == null && SOURCE.has("phone") && SOURCE.get("phone") != null)
                _phone = SOURCE.getString("phone");
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _phone;
    }

    public UserTaxInfo phone(String phone) throws ParseException {
        _phone = phone;
        SOURCE.put("phone", phone);
        return this;
    }

    public void setState(String state) throws ParseException {
        _state = state;
        SOURCE.put("state", state);
    }

    public String getState() {
        try {
            if (_state == null && SOURCE.has("state") && SOURCE.get("state") != null)
                _state = SOURCE.getString("state");
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _state;
    }

    public UserTaxInfo state(String state) throws ParseException {
        _state = state;
        SOURCE.put("state", state);
        return this;
    }

    public void setTechUserId(Integer techUserId) throws ParseException {
        _techUserId = techUserId;
        SOURCE.put("tech_user_id", techUserId);
    }

    public Integer getTechUserId() {
        try {
            if (_techUserId == null && SOURCE.has("tech_user_id") && SOURCE.get("tech_user_id") != null)
                _techUserId = SOURCE.getInt("tech_user_id");
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _techUserId;
    }

    public UserTaxInfo techUserId(Integer techUserId) throws ParseException {
        _techUserId = techUserId;
        SOURCE.put("tech_user_id", techUserId);
        return this;
    }

    public void setTechW9Id(Integer techW9Id) throws ParseException {
        _techW9Id = techW9Id;
        SOURCE.put("tech_w9_id", techW9Id);
    }

    public Integer getTechW9Id() {
        try {
            if (_techW9Id == null && SOURCE.has("tech_w9_id") && SOURCE.get("tech_w9_id") != null)
                _techW9Id = SOURCE.getInt("tech_w9_id");
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _techW9Id;
    }

    public UserTaxInfo techW9Id(Integer techW9Id) throws ParseException {
        _techW9Id = techW9Id;
        SOURCE.put("tech_w9_id", techW9Id);
        return this;
    }

    public void setTechnicianW9Status(TechnicianW9Status[] technicianW9Status) throws ParseException {
        _technicianW9Status = technicianW9Status;
        SOURCE.put("technician_w9_status", TechnicianW9Status.toJsonArray(technicianW9Status));
    }

    public TechnicianW9Status[] getTechnicianW9Status() {
        try {
            if (_technicianW9Status != null)
                return _technicianW9Status;

            if (SOURCE.has("technician_w9_status") && SOURCE.get("technician_w9_status") != null) {
                _technicianW9Status = TechnicianW9Status.fromJsonArray(SOURCE.getJsonArray("technician_w9_status"));
            }

        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        if (_technicianW9Status == null)
            _technicianW9Status = new TechnicianW9Status[0];

        return _technicianW9Status;
    }

    public UserTaxInfo technicianW9Status(TechnicianW9Status[] technicianW9Status) throws ParseException {
        _technicianW9Status = technicianW9Status;
        SOURCE.put("technician_w9_status", TechnicianW9Status.toJsonArray(technicianW9Status), true);
        return this;
    }

    public void setTechnicianW9StatusId(Integer technicianW9StatusId) throws ParseException {
        _technicianW9StatusId = technicianW9StatusId;
        SOURCE.put("technician_w9_status_id", technicianW9StatusId);
    }

    public Integer getTechnicianW9StatusId() {
        try {
            if (_technicianW9StatusId == null && SOURCE.has("technician_w9_status_id") && SOURCE.get("technician_w9_status_id") != null)
                _technicianW9StatusId = SOURCE.getInt("technician_w9_status_id");
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _technicianW9StatusId;
    }

    public UserTaxInfo technicianW9StatusId(Integer technicianW9StatusId) throws ParseException {
        _technicianW9StatusId = technicianW9StatusId;
        SOURCE.put("technician_w9_status_id", technicianW9StatusId);
        return this;
    }

    public void setTin(String tin) throws ParseException {
        _tin = tin;
        SOURCE.put("tin", tin);
    }

    public String getTin() {
        try {
            if (_tin == null && SOURCE.has("tin") && SOURCE.get("tin") != null)
                _tin = SOURCE.getString("tin");
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _tin;
    }

    public UserTaxInfo tin(String tin) throws ParseException {
        _tin = tin;
        SOURCE.put("tin", tin);
        return this;
    }

    public void setUserId(Integer userId) throws ParseException {
        _userId = userId;
        SOURCE.put("user_id", userId);
    }

    public Integer getUserId() {
        try {
            if (_userId == null && SOURCE.has("user_id") && SOURCE.get("user_id") != null)
                _userId = SOURCE.getInt("user_id");
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _userId;
    }

    public UserTaxInfo userId(Integer userId) throws ParseException {
        _userId = userId;
        SOURCE.put("user_id", userId);
        return this;
    }

    public void setZip(Integer zip) throws ParseException {
        _zip = zip;
        SOURCE.put("zip", zip);
    }

    public Integer getZip() {
        try {
            if (_zip == null && SOURCE.has("zip") && SOURCE.get("zip") != null)
                _zip = SOURCE.getInt("zip");
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _zip;
    }

    public UserTaxInfo zip(Integer zip) throws ParseException {
        _zip = zip;
        SOURCE.put("zip", zip);
        return this;
    }

    /*-*****************************-*/
    /*-             Json            -*/
    /*-*****************************-*/
    public static JsonArray toJsonArray(UserTaxInfo[] array) {
        JsonArray list = new JsonArray();
        for (UserTaxInfo item : array) {
            list.add(item.getJson());
        }
        return list;
    }

    public static UserTaxInfo[] fromJsonArray(JsonArray array) {
        UserTaxInfo[] list = new UserTaxInfo[array.size()];
        for (int i = 0; i < array.size(); i++) {
            list[i] = fromJson(array.getJsonObject(i));
        }
        return list;
    }

    public static UserTaxInfo fromJson(JsonObject obj) {
        try {
            return new UserTaxInfo(obj);
        } catch (Exception ex) {
            Log.v(TAG, ex);
            return null;
        }
    }

    public JsonObject getJson() {
        return SOURCE;
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
        dest.writeParcelable(getJson(), flags);
    }

    /*-*****************************-*/
    /*-         Human Code          -*/
    /*-*****************************-*/

}
