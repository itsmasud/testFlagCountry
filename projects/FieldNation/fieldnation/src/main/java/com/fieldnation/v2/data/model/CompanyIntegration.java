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

public class CompanyIntegration implements Parcelable {
    private static final String TAG = "CompanyIntegration";

    @Json(name = "active")
    private Boolean _active;

    @Json(name = "api_domain_name")
    private String _apiDomainName;

    @Json(name = "api_key")
    private String _apiKey;

    @Json(name = "api_pass")
    private String _apiPass;

    @Json(name = "company_id")
    private String _companyId;

    @Json(name = "company_integration_id")
    private String _companyIntegrationId;

    @Json(name = "disable_notifications")
    private Boolean _disableNotifications;

    @Json(name = "external_api_integration_id")
    private String _externalApiIntegrationId;

    @Json(name = "integration_type")
    private String _integrationType;

    @Source
    private JsonObject SOURCE;

    public CompanyIntegration() {
        SOURCE = new JsonObject();
    }

    public CompanyIntegration(JsonObject obj) {
        SOURCE = obj;
    }

    public void setActive(Boolean active) throws ParseException {
        _active = active;
        SOURCE.put("active", active);
    }

    public Boolean getActive() {
        try {
            if (_active == null && SOURCE.has("active") && SOURCE.get("active") != null)
                _active = SOURCE.getBoolean("active");
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _active;
    }

    public CompanyIntegration active(Boolean active) throws ParseException {
        _active = active;
        SOURCE.put("active", active);
        return this;
    }

    public void setApiDomainName(String apiDomainName) throws ParseException {
        _apiDomainName = apiDomainName;
        SOURCE.put("api_domain_name", apiDomainName);
    }

    public String getApiDomainName() {
        try {
            if (_apiDomainName == null && SOURCE.has("api_domain_name") && SOURCE.get("api_domain_name") != null)
                _apiDomainName = SOURCE.getString("api_domain_name");
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _apiDomainName;
    }

    public CompanyIntegration apiDomainName(String apiDomainName) throws ParseException {
        _apiDomainName = apiDomainName;
        SOURCE.put("api_domain_name", apiDomainName);
        return this;
    }

    public void setApiKey(String apiKey) throws ParseException {
        _apiKey = apiKey;
        SOURCE.put("api_key", apiKey);
    }

    public String getApiKey() {
        try {
            if (_apiKey == null && SOURCE.has("api_key") && SOURCE.get("api_key") != null)
                _apiKey = SOURCE.getString("api_key");
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _apiKey;
    }

    public CompanyIntegration apiKey(String apiKey) throws ParseException {
        _apiKey = apiKey;
        SOURCE.put("api_key", apiKey);
        return this;
    }

    public void setApiPass(String apiPass) throws ParseException {
        _apiPass = apiPass;
        SOURCE.put("api_pass", apiPass);
    }

    public String getApiPass() {
        try {
            if (_apiPass == null && SOURCE.has("api_pass") && SOURCE.get("api_pass") != null)
                _apiPass = SOURCE.getString("api_pass");
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _apiPass;
    }

    public CompanyIntegration apiPass(String apiPass) throws ParseException {
        _apiPass = apiPass;
        SOURCE.put("api_pass", apiPass);
        return this;
    }

    public void setCompanyId(String companyId) throws ParseException {
        _companyId = companyId;
        SOURCE.put("company_id", companyId);
    }

    public String getCompanyId() {
        try {
            if (_companyId == null && SOURCE.has("company_id") && SOURCE.get("company_id") != null)
                _companyId = SOURCE.getString("company_id");
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _companyId;
    }

    public CompanyIntegration companyId(String companyId) throws ParseException {
        _companyId = companyId;
        SOURCE.put("company_id", companyId);
        return this;
    }

    public void setCompanyIntegrationId(String companyIntegrationId) throws ParseException {
        _companyIntegrationId = companyIntegrationId;
        SOURCE.put("company_integration_id", companyIntegrationId);
    }

    public String getCompanyIntegrationId() {
        try {
            if (_companyIntegrationId == null && SOURCE.has("company_integration_id") && SOURCE.get("company_integration_id") != null)
                _companyIntegrationId = SOURCE.getString("company_integration_id");
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _companyIntegrationId;
    }

    public CompanyIntegration companyIntegrationId(String companyIntegrationId) throws ParseException {
        _companyIntegrationId = companyIntegrationId;
        SOURCE.put("company_integration_id", companyIntegrationId);
        return this;
    }

    public void setDisableNotifications(Boolean disableNotifications) throws ParseException {
        _disableNotifications = disableNotifications;
        SOURCE.put("disable_notifications", disableNotifications);
    }

    public Boolean getDisableNotifications() {
        try {
            if (_disableNotifications == null && SOURCE.has("disable_notifications") && SOURCE.get("disable_notifications") != null)
                _disableNotifications = SOURCE.getBoolean("disable_notifications");
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _disableNotifications;
    }

    public CompanyIntegration disableNotifications(Boolean disableNotifications) throws ParseException {
        _disableNotifications = disableNotifications;
        SOURCE.put("disable_notifications", disableNotifications);
        return this;
    }

    public void setExternalApiIntegrationId(String externalApiIntegrationId) throws ParseException {
        _externalApiIntegrationId = externalApiIntegrationId;
        SOURCE.put("external_api_integration_id", externalApiIntegrationId);
    }

    public String getExternalApiIntegrationId() {
        try {
            if (_externalApiIntegrationId == null && SOURCE.has("external_api_integration_id") && SOURCE.get("external_api_integration_id") != null)
                _externalApiIntegrationId = SOURCE.getString("external_api_integration_id");
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _externalApiIntegrationId;
    }

    public CompanyIntegration externalApiIntegrationId(String externalApiIntegrationId) throws ParseException {
        _externalApiIntegrationId = externalApiIntegrationId;
        SOURCE.put("external_api_integration_id", externalApiIntegrationId);
        return this;
    }

    public void setIntegrationType(String integrationType) throws ParseException {
        _integrationType = integrationType;
        SOURCE.put("integration_type", integrationType);
    }

    public String getIntegrationType() {
        try {
            if (_integrationType == null && SOURCE.has("integration_type") && SOURCE.get("integration_type") != null)
                _integrationType = SOURCE.getString("integration_type");
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _integrationType;
    }

    public CompanyIntegration integrationType(String integrationType) throws ParseException {
        _integrationType = integrationType;
        SOURCE.put("integration_type", integrationType);
        return this;
    }

    /*-*****************************-*/
    /*-             Json            -*/
    /*-*****************************-*/
    public static JsonArray toJsonArray(CompanyIntegration[] array) {
        JsonArray list = new JsonArray();
        for (CompanyIntegration item : array) {
            list.add(item.getJson());
        }
        return list;
    }

    public static CompanyIntegration[] fromJsonArray(JsonArray array) {
        CompanyIntegration[] list = new CompanyIntegration[array.size()];
        for (int i = 0; i < array.size(); i++) {
            list[i] = fromJson(array.getJsonObject(i));
        }
        return list;
    }

    public static CompanyIntegration fromJson(JsonObject obj) {
        try {
            return new CompanyIntegration(obj);
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
    public static final Parcelable.Creator<CompanyIntegration> CREATOR = new Parcelable.Creator<CompanyIntegration>() {

        @Override
        public CompanyIntegration createFromParcel(Parcel source) {
            try {
                return CompanyIntegration.fromJson((JsonObject) source.readParcelable(JsonObject.class.getClassLoader()));
            } catch (Exception ex) {
                Log.v(TAG, ex);
                return null;
            }
        }

        @Override
        public CompanyIntegration[] newArray(int size) {
            return new CompanyIntegration[size];
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
