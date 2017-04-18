package com.fieldnation.v2.data.client;

import android.os.Parcel;
import android.os.Parcelable;

import com.fieldnation.v2.data.model.*;
import com.fieldnation.fnjson.JsonArray;
import com.fieldnation.fnjson.JsonObject;
import com.fieldnation.fnjson.Serializer;
import com.fieldnation.fnjson.Unserializer;
import com.fieldnation.fnjson.annotations.Json;
import com.fieldnation.fnlog.Log;

/**
 * Created by dmgen from swagger.
 */

public class GetOverviewValuesOptions implements Parcelable {
    private static final String TAG = "GetOverviewValuesOptions";

    @Json(name = "clientId")
    private Integer _clientId;

    @Json(name = "projectId")
    private Integer _projectId;

    @Json(name = "serviceContractId")
    private Integer _serviceContractId;

    @Json(name = "teamId")
    private Integer _teamId;

    public GetOverviewValuesOptions() {
    }

    public void setClientId(Integer clientId) {
        _clientId = clientId;
    }

    public Integer getClientId() {
        return _clientId;
    }

    public GetOverviewValuesOptions clientId(Integer clientId) {
        _clientId = clientId;
        return this;
    }

    public boolean isClientIdSet() {
        return _clientId != null;
    }

    public String getClientIdUrlParam() {
        return "client_id=" + _clientId;
    }

    public void setProjectId(Integer projectId) {
        _projectId = projectId;
    }

    public Integer getProjectId() {
        return _projectId;
    }

    public GetOverviewValuesOptions projectId(Integer projectId) {
        _projectId = projectId;
        return this;
    }

    public boolean isProjectIdSet() {
        return _projectId != null;
    }

    public String getProjectIdUrlParam() {
        return "project_id=" + _projectId;
    }

    public void setServiceContractId(Integer serviceContractId) {
        _serviceContractId = serviceContractId;
    }

    public Integer getServiceContractId() {
        return _serviceContractId;
    }

    public GetOverviewValuesOptions serviceContractId(Integer serviceContractId) {
        _serviceContractId = serviceContractId;
        return this;
    }

    public boolean isServiceContractIdSet() {
        return _serviceContractId != null;
    }

    public String getServiceContractIdUrlParam() {
        return "service_contract_id=" + _serviceContractId;
    }

    public void setTeamId(Integer teamId) {
        _teamId = teamId;
    }

    public Integer getTeamId() {
        return _teamId;
    }

    public GetOverviewValuesOptions teamId(Integer teamId) {
        _teamId = teamId;
        return this;
    }

    public boolean isTeamIdSet() {
        return _teamId != null;
    }

    public String getTeamIdUrlParam() {
        return "team_id=" + _teamId;
    }

    /*-*****************************-*/
    /*-             Json            -*/
    /*-*****************************-*/
    public static GetOverviewValuesOptions fromJson(JsonObject obj) {
        try {
            return Unserializer.unserializeObject(GetOverviewValuesOptions.class, obj);
        } catch (Exception ex) {
            Log.v(TAG, ex);
            return null;
        }
    }

    public JsonObject toJson() {
        return toJson(this);
    }

    public static JsonObject toJson(GetOverviewValuesOptions getOverviewValuesOptions) {
        try {
            return Serializer.serializeObject(getOverviewValuesOptions);
        } catch (Exception ex) {
            Log.v(TAG, ex);
            return null;
        }
    }

    /*-*********************************************-*/
    /*-			Parcelable Implementation           -*/
    /*-*********************************************-*/
    public static final Parcelable.Creator<GetOverviewValuesOptions> CREATOR = new Parcelable.Creator<GetOverviewValuesOptions>() {

        @Override
        public GetOverviewValuesOptions createFromParcel(Parcel source) {
            try {
                return GetOverviewValuesOptions.fromJson((JsonObject) source.readParcelable(JsonObject.class.getClassLoader()));
            } catch (Exception ex) {
                Log.v(TAG, ex);
                return null;
            }
        }

        @Override
        public GetOverviewValuesOptions[] newArray(int size) {
            return new GetOverviewValuesOptions[size];
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
