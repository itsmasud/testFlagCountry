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

import java.text.ParseException;

/**
 * Created by dmgen from swagger.
 */

public class UserTaxInfoUpdate implements Parcelable {
    private static final String TAG = "UserTaxInfoUpdate";

    @Json(name = "address1")
    private String _address1;

    @Json(name = "address2")
    private String _address2;

    @Json(name = "business_name")
    private String _businessName;

    @Json(name = "buyer_company_id")
    private Integer _buyerCompanyId;

    @Json(name = "city")
    private String _city;

    @Json(name = "company_id")
    private Integer _companyId;

    @Json(name = "electronic_consent")
    private Integer _electronicConsent;

    @Json(name = "entity_type")
    private String _entityType;

    @Json(name = "first_name")
    private String _firstName;

    @Json(name = "from_vendor_buyer_invite")
    private Integer _fromVendorBuyerInvite;

    @Json(name = "has_ein")
    private Integer _hasEin;

    @Json(name = "has_separate_tax")
    private Integer _hasSeparateTax;

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

    @Json(name = "technician_w9_status_id")
    private Integer _technicianW9StatusId;

    @Json(name = "tin")
    private String _tin;

    @Json(name = "vendor_company_id")
    private Integer _vendorCompanyId;

    @Json(name = "zip")
    private Integer _zip;

    @Source
    private JsonObject SOURCE = new JsonObject();

    public UserTaxInfoUpdate() {
    }

    public void setAddress1(String address1) throws ParseException {
        _address1 = address1;
        SOURCE.put("address1", address1);
    }

    public String getAddress1() {
        return _address1;
    }

    public UserTaxInfoUpdate address1(String address1) throws ParseException {
        _address1 = address1;
        SOURCE.put("address1", address1);
        return this;
    }

    public void setAddress2(String address2) throws ParseException {
        _address2 = address2;
        SOURCE.put("address2", address2);
    }

    public String getAddress2() {
        return _address2;
    }

    public UserTaxInfoUpdate address2(String address2) throws ParseException {
        _address2 = address2;
        SOURCE.put("address2", address2);
        return this;
    }

    public void setBusinessName(String businessName) throws ParseException {
        _businessName = businessName;
        SOURCE.put("business_name", businessName);
    }

    public String getBusinessName() {
        return _businessName;
    }

    public UserTaxInfoUpdate businessName(String businessName) throws ParseException {
        _businessName = businessName;
        SOURCE.put("business_name", businessName);
        return this;
    }

    public void setBuyerCompanyId(Integer buyerCompanyId) throws ParseException {
        _buyerCompanyId = buyerCompanyId;
        SOURCE.put("buyer_company_id", buyerCompanyId);
    }

    public Integer getBuyerCompanyId() {
        return _buyerCompanyId;
    }

    public UserTaxInfoUpdate buyerCompanyId(Integer buyerCompanyId) throws ParseException {
        _buyerCompanyId = buyerCompanyId;
        SOURCE.put("buyer_company_id", buyerCompanyId);
        return this;
    }

    public void setCity(String city) throws ParseException {
        _city = city;
        SOURCE.put("city", city);
    }

    public String getCity() {
        return _city;
    }

    public UserTaxInfoUpdate city(String city) throws ParseException {
        _city = city;
        SOURCE.put("city", city);
        return this;
    }

    public void setCompanyId(Integer companyId) throws ParseException {
        _companyId = companyId;
        SOURCE.put("company_id", companyId);
    }

    public Integer getCompanyId() {
        return _companyId;
    }

    public UserTaxInfoUpdate companyId(Integer companyId) throws ParseException {
        _companyId = companyId;
        SOURCE.put("company_id", companyId);
        return this;
    }

    public void setElectronicConsent(Integer electronicConsent) throws ParseException {
        _electronicConsent = electronicConsent;
        SOURCE.put("electronic_consent", electronicConsent);
    }

    public Integer getElectronicConsent() {
        return _electronicConsent;
    }

    public UserTaxInfoUpdate electronicConsent(Integer electronicConsent) throws ParseException {
        _electronicConsent = electronicConsent;
        SOURCE.put("electronic_consent", electronicConsent);
        return this;
    }

    public void setEntityType(String entityType) throws ParseException {
        _entityType = entityType;
        SOURCE.put("entity_type", entityType);
    }

    public String getEntityType() {
        return _entityType;
    }

    public UserTaxInfoUpdate entityType(String entityType) throws ParseException {
        _entityType = entityType;
        SOURCE.put("entity_type", entityType);
        return this;
    }

    public void setFirstName(String firstName) throws ParseException {
        _firstName = firstName;
        SOURCE.put("first_name", firstName);
    }

    public String getFirstName() {
        return _firstName;
    }

    public UserTaxInfoUpdate firstName(String firstName) throws ParseException {
        _firstName = firstName;
        SOURCE.put("first_name", firstName);
        return this;
    }

    public void setFromVendorBuyerInvite(Integer fromVendorBuyerInvite) throws ParseException {
        _fromVendorBuyerInvite = fromVendorBuyerInvite;
        SOURCE.put("from_vendor_buyer_invite", fromVendorBuyerInvite);
    }

    public Integer getFromVendorBuyerInvite() {
        return _fromVendorBuyerInvite;
    }

    public UserTaxInfoUpdate fromVendorBuyerInvite(Integer fromVendorBuyerInvite) throws ParseException {
        _fromVendorBuyerInvite = fromVendorBuyerInvite;
        SOURCE.put("from_vendor_buyer_invite", fromVendorBuyerInvite);
        return this;
    }

    public void setHasEin(Integer hasEin) throws ParseException {
        _hasEin = hasEin;
        SOURCE.put("has_ein", hasEin);
    }

    public Integer getHasEin() {
        return _hasEin;
    }

    public UserTaxInfoUpdate hasEin(Integer hasEin) throws ParseException {
        _hasEin = hasEin;
        SOURCE.put("has_ein", hasEin);
        return this;
    }

    public void setHasSeparateTax(Integer hasSeparateTax) throws ParseException {
        _hasSeparateTax = hasSeparateTax;
        SOURCE.put("has_separate_tax", hasSeparateTax);
    }

    public Integer getHasSeparateTax() {
        return _hasSeparateTax;
    }

    public UserTaxInfoUpdate hasSeparateTax(Integer hasSeparateTax) throws ParseException {
        _hasSeparateTax = hasSeparateTax;
        SOURCE.put("has_separate_tax", hasSeparateTax);
        return this;
    }

    public void setLastName(String lastName) throws ParseException {
        _lastName = lastName;
        SOURCE.put("last_name", lastName);
    }

    public String getLastName() {
        return _lastName;
    }

    public UserTaxInfoUpdate lastName(String lastName) throws ParseException {
        _lastName = lastName;
        SOURCE.put("last_name", lastName);
        return this;
    }

    public void setName(String name) throws ParseException {
        _name = name;
        SOURCE.put("name", name);
    }

    public String getName() {
        return _name;
    }

    public UserTaxInfoUpdate name(String name) throws ParseException {
        _name = name;
        SOURCE.put("name", name);
        return this;
    }

    public void setOther(String other) throws ParseException {
        _other = other;
        SOURCE.put("other", other);
    }

    public String getOther() {
        return _other;
    }

    public UserTaxInfoUpdate other(String other) throws ParseException {
        _other = other;
        SOURCE.put("other", other);
        return this;
    }

    public void setPhone(String phone) throws ParseException {
        _phone = phone;
        SOURCE.put("phone", phone);
    }

    public String getPhone() {
        return _phone;
    }

    public UserTaxInfoUpdate phone(String phone) throws ParseException {
        _phone = phone;
        SOURCE.put("phone", phone);
        return this;
    }

    public void setState(String state) throws ParseException {
        _state = state;
        SOURCE.put("state", state);
    }

    public String getState() {
        return _state;
    }

    public UserTaxInfoUpdate state(String state) throws ParseException {
        _state = state;
        SOURCE.put("state", state);
        return this;
    }

    public void setTechnicianW9StatusId(Integer technicianW9StatusId) throws ParseException {
        _technicianW9StatusId = technicianW9StatusId;
        SOURCE.put("technician_w9_status_id", technicianW9StatusId);
    }

    public Integer getTechnicianW9StatusId() {
        return _technicianW9StatusId;
    }

    public UserTaxInfoUpdate technicianW9StatusId(Integer technicianW9StatusId) throws ParseException {
        _technicianW9StatusId = technicianW9StatusId;
        SOURCE.put("technician_w9_status_id", technicianW9StatusId);
        return this;
    }

    public void setTin(String tin) throws ParseException {
        _tin = tin;
        SOURCE.put("tin", tin);
    }

    public String getTin() {
        return _tin;
    }

    public UserTaxInfoUpdate tin(String tin) throws ParseException {
        _tin = tin;
        SOURCE.put("tin", tin);
        return this;
    }

    public void setVendorCompanyId(Integer vendorCompanyId) throws ParseException {
        _vendorCompanyId = vendorCompanyId;
        SOURCE.put("vendor_company_id", vendorCompanyId);
    }

    public Integer getVendorCompanyId() {
        return _vendorCompanyId;
    }

    public UserTaxInfoUpdate vendorCompanyId(Integer vendorCompanyId) throws ParseException {
        _vendorCompanyId = vendorCompanyId;
        SOURCE.put("vendor_company_id", vendorCompanyId);
        return this;
    }

    public void setZip(Integer zip) throws ParseException {
        _zip = zip;
        SOURCE.put("zip", zip);
    }

    public Integer getZip() {
        return _zip;
    }

    public UserTaxInfoUpdate zip(Integer zip) throws ParseException {
        _zip = zip;
        SOURCE.put("zip", zip);
        return this;
    }

    /*-*****************************-*/
    /*-             Json            -*/
    /*-*****************************-*/
    public static JsonArray toJsonArray(UserTaxInfoUpdate[] array) {
        JsonArray list = new JsonArray();
        for (UserTaxInfoUpdate item : array) {
            list.add(item.getJson());
        }
        return list;
    }

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

    public JsonObject getJson() {
        return SOURCE;
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
        dest.writeParcelable(getJson(), flags);
    }
}
