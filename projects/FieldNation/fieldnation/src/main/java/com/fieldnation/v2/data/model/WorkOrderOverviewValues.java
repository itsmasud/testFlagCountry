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

public class WorkOrderOverviewValues implements Parcelable {
    private static final String TAG = "WorkOrderOverviewValues";

    @Json(name = "actions")
    private ActionsEnum[] _actions;

    @Json(name = "client_id")
    private Integer _clientId;

    @Json(name = "clients")
    private WorkOrderOverviewValuesClients[] _clients;

    @Json(name = "network_id")
    private Integer[] _networkId;

    @Json(name = "networks")
    private WorkOrderOverviewValuesNetworks[] _networks;

    @Json(name = "project_id")
    private Integer _projectId;

    @Json(name = "projects")
    private WorkOrderOverviewValuesProjects[] _projects;

    @Json(name = "service_contract_id")
    private Integer _serviceContractId;

    @Json(name = "service_contracts")
    private WorkOrderOverviewValuesServiceContracts[] _serviceContracts;

    @Json(name = "team_id")
    private Integer _teamId;

    @Json(name = "teams")
    private WorkOrderOverviewValuesTeams[] _teams;

    @Json(name = "type_of_work_id")
    private Integer _typeOfWorkId;

    @Json(name = "types_of_work")
    private WorkOrderOverviewValuesTypesOfWork[] _typesOfWork;

    @Source
    private JsonObject SOURCE;

    public WorkOrderOverviewValues() {
        SOURCE = new JsonObject();
    }

    public WorkOrderOverviewValues(JsonObject obj) {
        SOURCE = obj;
    }

    public void setActions(ActionsEnum[] actions) throws ParseException {
        _actions = actions;
        JsonArray ja = new JsonArray();
        for (ActionsEnum item : actions) {
            ja.add(item.toString());
        }
        SOURCE.put("actions", ja);
    }

    public ActionsEnum[] getActions() {
        try {
            if (_actions != null)
                return _actions;

            if (SOURCE.has("actions") && SOURCE.get("actions") != null) {
                _actions = ActionsEnum.fromJsonArray(SOURCE.getJsonArray("actions"));
            }

        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        if (_actions == null)
            _actions = new ActionsEnum[0];

        return _actions;
    }

    public WorkOrderOverviewValues actions(ActionsEnum[] actions) throws ParseException {
        _actions = actions;
        JsonArray ja = new JsonArray();
        for (ActionsEnum item : actions) {
            ja.add(item.toString());
        }
        SOURCE.put("actions", ja, true);
        return this;
    }

    public void setClientId(Integer clientId) throws ParseException {
        _clientId = clientId;
        SOURCE.put("client_id", clientId);
    }

    public Integer getClientId() {
        try {
            if (_clientId == null && SOURCE.has("client_id") && SOURCE.get("client_id") != null)
                _clientId = SOURCE.getInt("client_id");
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _clientId;
    }

    public WorkOrderOverviewValues clientId(Integer clientId) throws ParseException {
        _clientId = clientId;
        SOURCE.put("client_id", clientId);
        return this;
    }

    public void setClients(WorkOrderOverviewValuesClients[] clients) throws ParseException {
        _clients = clients;
        SOURCE.put("clients", WorkOrderOverviewValuesClients.toJsonArray(clients));
    }

    public WorkOrderOverviewValuesClients[] getClients() {
        try {
            if (_clients != null)
                return _clients;

            if (SOURCE.has("clients") && SOURCE.get("clients") != null) {
                _clients = WorkOrderOverviewValuesClients.fromJsonArray(SOURCE.getJsonArray("clients"));
            }

        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        if (_clients == null)
            _clients = new WorkOrderOverviewValuesClients[0];

        return _clients;
    }

    public WorkOrderOverviewValues clients(WorkOrderOverviewValuesClients[] clients) throws ParseException {
        _clients = clients;
        SOURCE.put("clients", WorkOrderOverviewValuesClients.toJsonArray(clients), true);
        return this;
    }

    public void setNetworkId(Integer[] networkId) throws ParseException {
        _networkId = networkId;
        JsonArray ja = new JsonArray();
        for (Integer item : networkId) {
            ja.add(item);
        }
        SOURCE.put("network_id", ja);
    }

    public Integer[] getNetworkId() {
        try {
            if (_networkId != null)
                return _networkId;

            if (SOURCE.has("network_id") && SOURCE.get("network_id") != null) {
                JsonArray ja = SOURCE.getJsonArray("network_id");
                _networkId = ja.toArray(new Integer[ja.size()]);
            }

        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        if (_networkId == null)
            _networkId = new Integer[0];

        return _networkId;
    }

    public WorkOrderOverviewValues networkId(Integer[] networkId) throws ParseException {
        _networkId = networkId;
        JsonArray ja = new JsonArray();
        for (Integer item : networkId) {
            ja.add(item);
        }
        SOURCE.put("network_id", ja, true);
        return this;
    }

    public void setNetworks(WorkOrderOverviewValuesNetworks[] networks) throws ParseException {
        _networks = networks;
        SOURCE.put("networks", WorkOrderOverviewValuesNetworks.toJsonArray(networks));
    }

    public WorkOrderOverviewValuesNetworks[] getNetworks() {
        try {
            if (_networks != null)
                return _networks;

            if (SOURCE.has("networks") && SOURCE.get("networks") != null) {
                _networks = WorkOrderOverviewValuesNetworks.fromJsonArray(SOURCE.getJsonArray("networks"));
            }

        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        if (_networks == null)
            _networks = new WorkOrderOverviewValuesNetworks[0];

        return _networks;
    }

    public WorkOrderOverviewValues networks(WorkOrderOverviewValuesNetworks[] networks) throws ParseException {
        _networks = networks;
        SOURCE.put("networks", WorkOrderOverviewValuesNetworks.toJsonArray(networks), true);
        return this;
    }

    public void setProjectId(Integer projectId) throws ParseException {
        _projectId = projectId;
        SOURCE.put("project_id", projectId);
    }

    public Integer getProjectId() {
        try {
            if (_projectId == null && SOURCE.has("project_id") && SOURCE.get("project_id") != null)
                _projectId = SOURCE.getInt("project_id");
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _projectId;
    }

    public WorkOrderOverviewValues projectId(Integer projectId) throws ParseException {
        _projectId = projectId;
        SOURCE.put("project_id", projectId);
        return this;
    }

    public void setProjects(WorkOrderOverviewValuesProjects[] projects) throws ParseException {
        _projects = projects;
        SOURCE.put("projects", WorkOrderOverviewValuesProjects.toJsonArray(projects));
    }

    public WorkOrderOverviewValuesProjects[] getProjects() {
        try {
            if (_projects != null)
                return _projects;

            if (SOURCE.has("projects") && SOURCE.get("projects") != null) {
                _projects = WorkOrderOverviewValuesProjects.fromJsonArray(SOURCE.getJsonArray("projects"));
            }

        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        if (_projects == null)
            _projects = new WorkOrderOverviewValuesProjects[0];

        return _projects;
    }

    public WorkOrderOverviewValues projects(WorkOrderOverviewValuesProjects[] projects) throws ParseException {
        _projects = projects;
        SOURCE.put("projects", WorkOrderOverviewValuesProjects.toJsonArray(projects), true);
        return this;
    }

    public void setServiceContractId(Integer serviceContractId) throws ParseException {
        _serviceContractId = serviceContractId;
        SOURCE.put("service_contract_id", serviceContractId);
    }

    public Integer getServiceContractId() {
        try {
            if (_serviceContractId == null && SOURCE.has("service_contract_id") && SOURCE.get("service_contract_id") != null)
                _serviceContractId = SOURCE.getInt("service_contract_id");
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _serviceContractId;
    }

    public WorkOrderOverviewValues serviceContractId(Integer serviceContractId) throws ParseException {
        _serviceContractId = serviceContractId;
        SOURCE.put("service_contract_id", serviceContractId);
        return this;
    }

    public void setServiceContracts(WorkOrderOverviewValuesServiceContracts[] serviceContracts) throws ParseException {
        _serviceContracts = serviceContracts;
        SOURCE.put("service_contracts", WorkOrderOverviewValuesServiceContracts.toJsonArray(serviceContracts));
    }

    public WorkOrderOverviewValuesServiceContracts[] getServiceContracts() {
        try {
            if (_serviceContracts != null)
                return _serviceContracts;

            if (SOURCE.has("service_contracts") && SOURCE.get("service_contracts") != null) {
                _serviceContracts = WorkOrderOverviewValuesServiceContracts.fromJsonArray(SOURCE.getJsonArray("service_contracts"));
            }

        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        if (_serviceContracts == null)
            _serviceContracts = new WorkOrderOverviewValuesServiceContracts[0];

        return _serviceContracts;
    }

    public WorkOrderOverviewValues serviceContracts(WorkOrderOverviewValuesServiceContracts[] serviceContracts) throws ParseException {
        _serviceContracts = serviceContracts;
        SOURCE.put("service_contracts", WorkOrderOverviewValuesServiceContracts.toJsonArray(serviceContracts), true);
        return this;
    }

    public void setTeamId(Integer teamId) throws ParseException {
        _teamId = teamId;
        SOURCE.put("team_id", teamId);
    }

    public Integer getTeamId() {
        try {
            if (_teamId == null && SOURCE.has("team_id") && SOURCE.get("team_id") != null)
                _teamId = SOURCE.getInt("team_id");
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _teamId;
    }

    public WorkOrderOverviewValues teamId(Integer teamId) throws ParseException {
        _teamId = teamId;
        SOURCE.put("team_id", teamId);
        return this;
    }

    public void setTeams(WorkOrderOverviewValuesTeams[] teams) throws ParseException {
        _teams = teams;
        SOURCE.put("teams", WorkOrderOverviewValuesTeams.toJsonArray(teams));
    }

    public WorkOrderOverviewValuesTeams[] getTeams() {
        try {
            if (_teams != null)
                return _teams;

            if (SOURCE.has("teams") && SOURCE.get("teams") != null) {
                _teams = WorkOrderOverviewValuesTeams.fromJsonArray(SOURCE.getJsonArray("teams"));
            }

        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        if (_teams == null)
            _teams = new WorkOrderOverviewValuesTeams[0];

        return _teams;
    }

    public WorkOrderOverviewValues teams(WorkOrderOverviewValuesTeams[] teams) throws ParseException {
        _teams = teams;
        SOURCE.put("teams", WorkOrderOverviewValuesTeams.toJsonArray(teams), true);
        return this;
    }

    public void setTypeOfWorkId(Integer typeOfWorkId) throws ParseException {
        _typeOfWorkId = typeOfWorkId;
        SOURCE.put("type_of_work_id", typeOfWorkId);
    }

    public Integer getTypeOfWorkId() {
        try {
            if (_typeOfWorkId == null && SOURCE.has("type_of_work_id") && SOURCE.get("type_of_work_id") != null)
                _typeOfWorkId = SOURCE.getInt("type_of_work_id");
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _typeOfWorkId;
    }

    public WorkOrderOverviewValues typeOfWorkId(Integer typeOfWorkId) throws ParseException {
        _typeOfWorkId = typeOfWorkId;
        SOURCE.put("type_of_work_id", typeOfWorkId);
        return this;
    }

    public void setTypesOfWork(WorkOrderOverviewValuesTypesOfWork[] typesOfWork) throws ParseException {
        _typesOfWork = typesOfWork;
        SOURCE.put("types_of_work", WorkOrderOverviewValuesTypesOfWork.toJsonArray(typesOfWork));
    }

    public WorkOrderOverviewValuesTypesOfWork[] getTypesOfWork() {
        try {
            if (_typesOfWork != null)
                return _typesOfWork;

            if (SOURCE.has("types_of_work") && SOURCE.get("types_of_work") != null) {
                _typesOfWork = WorkOrderOverviewValuesTypesOfWork.fromJsonArray(SOURCE.getJsonArray("types_of_work"));
            }

        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        if (_typesOfWork == null)
            _typesOfWork = new WorkOrderOverviewValuesTypesOfWork[0];

        return _typesOfWork;
    }

    public WorkOrderOverviewValues typesOfWork(WorkOrderOverviewValuesTypesOfWork[] typesOfWork) throws ParseException {
        _typesOfWork = typesOfWork;
        SOURCE.put("types_of_work", WorkOrderOverviewValuesTypesOfWork.toJsonArray(typesOfWork), true);
        return this;
    }

    /*-******************************-*/
    /*-             Enums            -*/
    /*-******************************-*/
    public enum ActionsEnum {
        @Json(name = "edit")
        EDIT("edit"),
        @Json(name = "networks")
        NETWORKS("networks"),
        @Json(name = "service_contracts")
        SERVICE_CONTRACTS("service_contracts"),
        @Json(name = "teams")
        TEAMS("teams");

        private String value;

        ActionsEnum(String value) {
            this.value = value;
        }

        public static ActionsEnum fromString(String value) {
            ActionsEnum[] values = values();
            for (ActionsEnum v : values) {
                if (v.value.equals(value))
                    return v;
            }
            return null;
        }

        public static ActionsEnum[] fromJsonArray(JsonArray jsonArray) {
            ActionsEnum[] list = new ActionsEnum[jsonArray.size()];
            for (int i = 0; i < list.length; i++) {
                list[i] = fromString(jsonArray.getString(i));
            }
            return list;
        }

        @Override
        public String toString() {
            return String.valueOf(value);
        }
    }

    /*-*****************************-*/
    /*-             Json            -*/
    /*-*****************************-*/
    public static JsonArray toJsonArray(WorkOrderOverviewValues[] array) {
        JsonArray list = new JsonArray();
        for (WorkOrderOverviewValues item : array) {
            list.add(item.getJson());
        }
        return list;
    }

    public static WorkOrderOverviewValues[] fromJsonArray(JsonArray array) {
        WorkOrderOverviewValues[] list = new WorkOrderOverviewValues[array.size()];
        for (int i = 0; i < array.size(); i++) {
            list[i] = fromJson(array.getJsonObject(i));
        }
        return list;
    }

    public static WorkOrderOverviewValues fromJson(JsonObject obj) {
        try {
            return new WorkOrderOverviewValues(obj);
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
    public static final Parcelable.Creator<WorkOrderOverviewValues> CREATOR = new Parcelable.Creator<WorkOrderOverviewValues>() {

        @Override
        public WorkOrderOverviewValues createFromParcel(Parcel source) {
            try {
                return WorkOrderOverviewValues.fromJson((JsonObject) source.readParcelable(JsonObject.class.getClassLoader()));
            } catch (Exception ex) {
                Log.v(TAG, ex);
                return null;
            }
        }

        @Override
        public WorkOrderOverviewValues[] newArray(int size) {
            return new WorkOrderOverviewValues[size];
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
