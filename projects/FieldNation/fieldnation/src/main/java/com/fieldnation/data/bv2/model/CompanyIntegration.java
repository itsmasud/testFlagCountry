package com.fieldnation.data.bv2.model;

import com.fieldnation.fnjson.JsonObject;
import com.fieldnation.fnjson.Serializer;
import com.fieldnation.fnjson.Unserializer;
import com.fieldnation.fnjson.annotations.Json;
import com.fieldnation.fnlog.Log;

public class CompanyIntegration {
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

    public String getApiPass() {
        return _apiPass;
    }

    public String getCompanyId() {
        return _companyId;
    }

    public String getApiKey() {
        return _apiKey;
    }

    public String getExternalApiIntegrationId() {
        return _externalApiIntegrationId;
    }

    public Boolean getDisableNotifications() {
        return _disableNotifications;
    }

    public Boolean getActive() {
        return _active;
    }

    public String getApiDomainName() {
        return _apiDomainName;
    }

    public String getCompanyIntegrationId() {
        return _companyIntegrationId;
    }

    public String getIntegrationType() {
        return _integrationType;
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
}
