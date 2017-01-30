package com.fieldnation.data.bv2.model;
import android.os.Parcel;
import android.os.Parcelable;

import com.fieldnation.fnjson.JsonObject;
import com.fieldnation.fnjson.Serializer;
import com.fieldnation.fnjson.Unserializer;
import com.fieldnation.fnjson.annotations.Json;
import com.fieldnation.fnlog.Log;

/**
 * Created by dmgen from swagger on 1/30/17.
 */

public class CompanyIntegration implements Parcelable {
    private static final String TAG = "CompanyIntegration";

    @Json(name = "api_pass")
    private String _apiPass;

    @Json(name = "company_id")
    private String _companyId;

    @Json(name = "api_key")
    private String _apiKey;

    @Json(name = "external_api_integration_id")
    private String _externalApiIntegrationId;

    @Json(name = "disable_notifications")
    private Boolean _disableNotifications;

    @Json(name = "active")
    private Boolean _active;

    @Json(name = "api_domain_name")
    private String _apiDomainName;

    @Json(name = "company_integration_id")
    private String _companyIntegrationId;

    @Json(name = "integration_type")
    private String _integrationType;

    public CompanyIntegration() {
    }

    public void setApiPass(String apiPass) {
        _apiPass = apiPass;
    }

    public String getApiPass() {
        return _apiPass;
    }

    public CompanyIntegration apiPass(String apiPass) {
        _apiPass = apiPass;
        return this;
    }

    public void setCompanyId(String companyId) {
        _companyId = companyId;
    }

    public String getCompanyId() {
        return _companyId;
    }

    public CompanyIntegration companyId(String companyId) {
        _companyId = companyId;
        return this;
    }

    public void setApiKey(String apiKey) {
        _apiKey = apiKey;
    }

    public String getApiKey() {
        return _apiKey;
    }

    public CompanyIntegration apiKey(String apiKey) {
        _apiKey = apiKey;
        return this;
    }

    public void setExternalApiIntegrationId(String externalApiIntegrationId) {
        _externalApiIntegrationId = externalApiIntegrationId;
    }

    public String getExternalApiIntegrationId() {
        return _externalApiIntegrationId;
    }

    public CompanyIntegration externalApiIntegrationId(String externalApiIntegrationId) {
        _externalApiIntegrationId = externalApiIntegrationId;
        return this;
    }

    public void setDisableNotifications(Boolean disableNotifications) {
        _disableNotifications = disableNotifications;
    }

    public Boolean getDisableNotifications() {
        return _disableNotifications;
    }

    public CompanyIntegration disableNotifications(Boolean disableNotifications) {
        _disableNotifications = disableNotifications;
        return this;
    }

    public void setActive(Boolean active) {
        _active = active;
    }

    public Boolean getActive() {
        return _active;
    }

    public CompanyIntegration active(Boolean active) {
        _active = active;
        return this;
    }

    public void setApiDomainName(String apiDomainName) {
        _apiDomainName = apiDomainName;
    }

    public String getApiDomainName() {
        return _apiDomainName;
    }

    public CompanyIntegration apiDomainName(String apiDomainName) {
        _apiDomainName = apiDomainName;
        return this;
    }

    public void setCompanyIntegrationId(String companyIntegrationId) {
        _companyIntegrationId = companyIntegrationId;
    }

    public String getCompanyIntegrationId() {
        return _companyIntegrationId;
    }

    public CompanyIntegration companyIntegrationId(String companyIntegrationId) {
        _companyIntegrationId = companyIntegrationId;
        return this;
    }

    public void setIntegrationType(String integrationType) {
        _integrationType = integrationType;
    }

    public String getIntegrationType() {
        return _integrationType;
    }

    public CompanyIntegration integrationType(String integrationType) {
        _integrationType = integrationType;
        return this;
    }

    /*-*****************************-*/
    /*-             Json            -*/
    /*-*****************************-*/
    public static CompanyIntegration fromJson(JsonObject obj) {
        try {
            return Unserializer.unserializeObject(CompanyIntegration.class, obj);
        } catch (Exception ex) {
            Log.v(TAG, TAG, ex);
            return null;
        }
    }

    public JsonObject toJson() {
        return toJson(this);
    }

    public static JsonObject toJson(CompanyIntegration companyIntegration) {
        try {
            return Serializer.serializeObject(companyIntegration);
        } catch (Exception ex) {
            Log.v(TAG, TAG, ex);
            return null;
        }
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
        dest.writeParcelable(toJson(), flags);
    }
}
