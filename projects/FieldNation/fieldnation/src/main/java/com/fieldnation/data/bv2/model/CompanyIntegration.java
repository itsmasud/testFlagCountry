package com.fieldnation.data.bv2.model;

import com.fieldnation.fnjson.JsonObject;
import com.fieldnation.fnjson.Serializer;
import com.fieldnation.fnjson.Unserializer;
import com.fieldnation.fnjson.annotations.Json;
import com.fieldnation.fnlog.Log;

public class CompanyIntegration {
    private static final String TAG = "CompanyIntegration";

    @Json(name = "api_pass")
    private String apiPass;

    @Json(name = "company_id")
    private String companyId;

    @Json(name = "api_key")
    private String apiKey;

    @Json(name = "external_api_integration_id")
    private String externalApiIntegrationId;

    @Json(name = "disable_notifications")
    private Boolean disableNotifications;

    @Json(name = "active")
    private Boolean active;

    @Json(name = "api_domain_name")
    private String apiDomainName;

    @Json(name = "company_integration_id")
    private String companyIntegrationId;

    @Json(name = "integration_type")
    private String integrationType;

    public CompanyIntegration() {
    }

    public String getApiPass() {
        return apiPass;
    }

    public String getCompanyId() {
        return companyId;
    }

    public String getApiKey() {
        return apiKey;
    }

    public String getExternalApiIntegrationId() {
        return externalApiIntegrationId;
    }

    public Boolean getDisableNotifications() {
        return disableNotifications;
    }

    public Boolean getActive() {
        return active;
    }

    public String getApiDomainName() {
        return apiDomainName;
    }

    public String getCompanyIntegrationId() {
        return companyIntegrationId;
    }

    public String getIntegrationType() {
        return integrationType;
    }

    /*-*****************************-*/
    /*-             Json            -*/
    /*-*****************************-*/
    public static CompanyIntegration fromJson(JsonObject obj) {
        try {
            return Unserializer.unserializeObject(CompanyIntegration.class, obj);
        } catch (Exception ex) {
            Log.v(TAG, ex);
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
            Log.v(TAG, ex);
            return null;
        }
    }
}
