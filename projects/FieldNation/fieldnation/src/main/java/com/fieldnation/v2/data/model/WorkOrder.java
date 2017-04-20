package com.fieldnation.v2.data.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.fieldnation.fnjson.JsonArray;
import com.fieldnation.fnjson.JsonObject;
import com.fieldnation.fnjson.annotations.Json;
import com.fieldnation.fnjson.annotations.Source;
import com.fieldnation.fnlog.Log;

import java.text.ParseException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by dmgen from swagger.
 */

public class WorkOrder implements Parcelable {
    private static final String TAG = "WorkOrder";

    @Json(name = "actions")
    private ActionsEnum[] _actions;

    @Json(name = "assignee")
    private Assignee _assignee;

    @Json(name = "attachments")
    private AttachmentFolders _attachments;

    @Json(name = "bundle")
    private Bundle _bundle;

    @Json(name = "client")
    private Company _client;

    @Json(name = "client_request")
    private ClientRequest _clientRequest;

    @Json(name = "closing_notes")
    private String _closingNotes;

    @Json(name = "company")
    private Company _company;

    @Json(name = "confidential")
    private RichText _confidential;

    @Json(name = "contacts")
    private Contacts _contacts;

    @Json(name = "correlation_id")
    private String _correlationId;

    @Json(name = "custom_fields")
    private CustomFields _customFields;

    @Json(name = "description")
    private RichText _description;

    @Json(name = "eta")
    private ETA _eta;

    @Json(name = "holds")
    private Holds _holds;

    @Json(name = "id")
    private Integer _id;

    @Json(name = "integration")
    private Integration _integration;

    @Json(name = "location")
    private Location _location;

    @Json(name = "manager")
    private Manager _manager;

    @Json(name = "messages")
    private Messages _messages;

    @Json(name = "milestones")
    private Milestones _milestones;

    @Json(name = "networks")
    private Network[] _networks;

    @Json(name = "pay")
    private Pay _pay;

    @Json(name = "policy_and_procedures")
    private RichText _policyAndProcedures;

    @Json(name = "project")
    private Project _project;

    @Json(name = "publish_stats")
    private PublishStats _publishStats;

    @Json(name = "qualifications")
    private Qualifications _qualifications;

    @Json(name = "ratings")
    private WorkOrderRatings _ratings;

    @Json(name = "requests")
    private Requests _requests;

    @Json(name = "role")
    private WorkOrderRole _role;

    @Json(name = "routes")
    private Routes _routes;

    @Json(name = "schedule")
    private Schedule _schedule;

    @Json(name = "selection_rule")
    private SelectionRule _selectionRule;

    @Json(name = "service_contract")
    private ServiceContract _serviceContract;

    @Json(name = "shipments")
    private Shipments _shipments;

    @Json(name = "signatures")
    private Signatures _signatures;

    @Json(name = "standard_instructions")
    private RichText _standardInstructions;

    @Json(name = "status")
    private Status _status;

    @Json(name = "tasks")
    private Tasks _tasks;

    @Json(name = "team")
    private Team _team;

    @Json(name = "template")
    private Template _template;

    @Json(name = "time_logs")
    private TimeLogs _timeLogs;

    @Json(name = "title")
    private String _title;

    @Json(name = "type_of_work")
    private TypeOfWork _typeOfWork;

    @Json(name = "w2")
    private Boolean _w2;

    @Json(name = "workflow_completion")
    private WorkflowCompletion _workflowCompletion;

    @Source
    private JsonObject SOURCE;

    public WorkOrder() {
        SOURCE = new JsonObject();
    }

    public WorkOrder(JsonObject obj) {
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

        return _actions;
    }

    public WorkOrder actions(ActionsEnum[] actions) throws ParseException {
        _actions = actions;
        JsonArray ja = new JsonArray();
        for (ActionsEnum item : actions) {
            ja.add(item.toString());
        }
        SOURCE.put("actions", ja, true);
        return this;
    }

    public void setAssignee(Assignee assignee) throws ParseException {
        _assignee = assignee;
        SOURCE.put("assignee", assignee.getJson());
    }

    public Assignee getAssignee() {
        try {
            if (_assignee == null && SOURCE.has("assignee") && SOURCE.get("assignee") != null)
                _assignee = Assignee.fromJson(SOURCE.getJsonObject("assignee"));
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        if (_assignee != null && _assignee.isSet())
            return _assignee;

        return null;
    }

    public WorkOrder assignee(Assignee assignee) throws ParseException {
        _assignee = assignee;
        SOURCE.put("assignee", assignee.getJson());
        return this;
    }

    public void setAttachments(AttachmentFolders attachments) throws ParseException {
        _attachments = attachments;
        SOURCE.put("attachments", attachments.getJson());
    }

    public AttachmentFolders getAttachments() {
        try {
            if (_attachments == null && SOURCE.has("attachments") && SOURCE.get("attachments") != null)
                _attachments = AttachmentFolders.fromJson(SOURCE.getJsonObject("attachments"));
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        if (_attachments != null && _attachments.isSet())
            return _attachments;

        return null;
    }

    public WorkOrder attachments(AttachmentFolders attachments) throws ParseException {
        _attachments = attachments;
        SOURCE.put("attachments", attachments.getJson());
        return this;
    }

    public void setBundle(Bundle bundle) throws ParseException {
        _bundle = bundle;
        SOURCE.put("bundle", bundle.getJson());
    }

    public Bundle getBundle() {
        try {
            if (_bundle == null && SOURCE.has("bundle") && SOURCE.get("bundle") != null)
                _bundle = Bundle.fromJson(SOURCE.getJsonObject("bundle"));
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        if (_bundle != null && _bundle.isSet())
            return _bundle;

        return null;
    }

    public WorkOrder bundle(Bundle bundle) throws ParseException {
        _bundle = bundle;
        SOURCE.put("bundle", bundle.getJson());
        return this;
    }

    public void setClient(Company client) throws ParseException {
        _client = client;
        SOURCE.put("client", client.getJson());
    }

    public Company getClient() {
        try {
            if (_client == null && SOURCE.has("client") && SOURCE.get("client") != null)
                _client = Company.fromJson(SOURCE.getJsonObject("client"));
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        if (_client != null && _client.isSet())
            return _client;

        return null;
    }

    public WorkOrder client(Company client) throws ParseException {
        _client = client;
        SOURCE.put("client", client.getJson());
        return this;
    }

    public void setClientRequest(ClientRequest clientRequest) throws ParseException {
        _clientRequest = clientRequest;
        SOURCE.put("client_request", clientRequest.getJson());
    }

    public ClientRequest getClientRequest() {
        try {
            if (_clientRequest == null && SOURCE.has("client_request") && SOURCE.get("client_request") != null)
                _clientRequest = ClientRequest.fromJson(SOURCE.getJsonObject("client_request"));
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        if (_clientRequest != null && _clientRequest.isSet())
            return _clientRequest;

        return null;
    }

    public WorkOrder clientRequest(ClientRequest clientRequest) throws ParseException {
        _clientRequest = clientRequest;
        SOURCE.put("client_request", clientRequest.getJson());
        return this;
    }

    public void setClosingNotes(String closingNotes) throws ParseException {
        _closingNotes = closingNotes;
        SOURCE.put("closing_notes", closingNotes);
    }

    public String getClosingNotes() {
        try {
            if (_closingNotes == null && SOURCE.has("closing_notes") && SOURCE.get("closing_notes") != null)
                _closingNotes = SOURCE.getString("closing_notes");
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _closingNotes;
    }

    public WorkOrder closingNotes(String closingNotes) throws ParseException {
        _closingNotes = closingNotes;
        SOURCE.put("closing_notes", closingNotes);
        return this;
    }

    public void setCompany(Company company) throws ParseException {
        _company = company;
        SOURCE.put("company", company.getJson());
    }

    public Company getCompany() {
        try {
            if (_company == null && SOURCE.has("company") && SOURCE.get("company") != null)
                _company = Company.fromJson(SOURCE.getJsonObject("company"));
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        if (_company != null && _company.isSet())
            return _company;

        return null;
    }

    public WorkOrder company(Company company) throws ParseException {
        _company = company;
        SOURCE.put("company", company.getJson());
        return this;
    }

    public void setConfidential(RichText confidential) throws ParseException {
        _confidential = confidential;
        SOURCE.put("confidential", confidential.getJson());
    }

    public RichText getConfidential() {
        try {
            if (_confidential == null && SOURCE.has("confidential") && SOURCE.get("confidential") != null)
                _confidential = RichText.fromJson(SOURCE.getJsonObject("confidential"));
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        if (_confidential != null && _confidential.isSet())
            return _confidential;

        return null;
    }

    public WorkOrder confidential(RichText confidential) throws ParseException {
        _confidential = confidential;
        SOURCE.put("confidential", confidential.getJson());
        return this;
    }

    public void setContacts(Contacts contacts) throws ParseException {
        _contacts = contacts;
        SOURCE.put("contacts", contacts.getJson());
    }

    public Contacts getContacts() {
        try {
            if (_contacts == null && SOURCE.has("contacts") && SOURCE.get("contacts") != null)
                _contacts = Contacts.fromJson(SOURCE.getJsonObject("contacts"));
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        if (_contacts != null && _contacts.isSet())
            return _contacts;

        return null;
    }

    public WorkOrder contacts(Contacts contacts) throws ParseException {
        _contacts = contacts;
        SOURCE.put("contacts", contacts.getJson());
        return this;
    }

    public void setCorrelationId(String correlationId) throws ParseException {
        _correlationId = correlationId;
        SOURCE.put("correlation_id", correlationId);
    }

    public String getCorrelationId() {
        try {
            if (_correlationId == null && SOURCE.has("correlation_id") && SOURCE.get("correlation_id") != null)
                _correlationId = SOURCE.getString("correlation_id");
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _correlationId;
    }

    public WorkOrder correlationId(String correlationId) throws ParseException {
        _correlationId = correlationId;
        SOURCE.put("correlation_id", correlationId);
        return this;
    }

    public void setCustomFields(CustomFields customFields) throws ParseException {
        _customFields = customFields;
        SOURCE.put("custom_fields", customFields.getJson());
    }

    public CustomFields getCustomFields() {
        try {
            if (_customFields == null && SOURCE.has("custom_fields") && SOURCE.get("custom_fields") != null)
                _customFields = CustomFields.fromJson(SOURCE.getJsonObject("custom_fields"));
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        if (_customFields != null && _customFields.isSet())
            return _customFields;

        return null;
    }

    public WorkOrder customFields(CustomFields customFields) throws ParseException {
        _customFields = customFields;
        SOURCE.put("custom_fields", customFields.getJson());
        return this;
    }

    public void setDescription(RichText description) throws ParseException {
        _description = description;
        SOURCE.put("description", description.getJson());
    }

    public RichText getDescription() {
        try {
            if (_description == null && SOURCE.has("description") && SOURCE.get("description") != null)
                _description = RichText.fromJson(SOURCE.getJsonObject("description"));
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        if (_description != null && _description.isSet())
            return _description;

        return null;
    }

    public WorkOrder description(RichText description) throws ParseException {
        _description = description;
        SOURCE.put("description", description.getJson());
        return this;
    }

    public void setEta(ETA eta) throws ParseException {
        _eta = eta;
        SOURCE.put("eta", eta.getJson());
    }

    public ETA getEta() {
        try {
            if (_eta == null && SOURCE.has("eta") && SOURCE.get("eta") != null)
                _eta = ETA.fromJson(SOURCE.getJsonObject("eta"));
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        if (_eta != null && _eta.isSet())
            return _eta;

        return null;
    }

    public WorkOrder eta(ETA eta) throws ParseException {
        _eta = eta;
        SOURCE.put("eta", eta.getJson());
        return this;
    }

    public void setHolds(Holds holds) throws ParseException {
        _holds = holds;
        SOURCE.put("holds", holds.getJson());
    }

    public Holds getHolds() {
        try {
            if (_holds == null && SOURCE.has("holds") && SOURCE.get("holds") != null)
                _holds = Holds.fromJson(SOURCE.getJsonObject("holds"));
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        if (_holds != null && _holds.isSet())
            return _holds;

        return null;
    }

    public WorkOrder holds(Holds holds) throws ParseException {
        _holds = holds;
        SOURCE.put("holds", holds.getJson());
        return this;
    }

    public void setId(Integer id) throws ParseException {
        _id = id;
        SOURCE.put("id", id);
    }

    public Integer getId() {
        try {
            if (_id == null && SOURCE.has("id") && SOURCE.get("id") != null)
                _id = SOURCE.getInt("id");
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _id;
    }

    public WorkOrder id(Integer id) throws ParseException {
        _id = id;
        SOURCE.put("id", id);
        return this;
    }

    public void setIntegration(Integration integration) throws ParseException {
        _integration = integration;
        SOURCE.put("integration", integration.getJson());
    }

    public Integration getIntegration() {
        try {
            if (_integration == null && SOURCE.has("integration") && SOURCE.get("integration") != null)
                _integration = Integration.fromJson(SOURCE.getJsonObject("integration"));
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        if (_integration != null && _integration.isSet())
            return _integration;

        return null;
    }

    public WorkOrder integration(Integration integration) throws ParseException {
        _integration = integration;
        SOURCE.put("integration", integration.getJson());
        return this;
    }

    public void setLocation(Location location) throws ParseException {
        _location = location;
        SOURCE.put("location", location.getJson());
    }

    public Location getLocation() {
        try {
            if (_location == null && SOURCE.has("location") && SOURCE.get("location") != null)
                _location = Location.fromJson(SOURCE.getJsonObject("location"));
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        if (_location != null && _location.isSet())
            return _location;

        return null;
    }

    public WorkOrder location(Location location) throws ParseException {
        _location = location;
        SOURCE.put("location", location.getJson());
        return this;
    }

    public void setManager(Manager manager) throws ParseException {
        _manager = manager;
        SOURCE.put("manager", manager.getJson());
    }

    public Manager getManager() {
        try {
            if (_manager == null && SOURCE.has("manager") && SOURCE.get("manager") != null)
                _manager = Manager.fromJson(SOURCE.getJsonObject("manager"));
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        if (_manager != null && _manager.isSet())
            return _manager;

        return null;
    }

    public WorkOrder manager(Manager manager) throws ParseException {
        _manager = manager;
        SOURCE.put("manager", manager.getJson());
        return this;
    }

    public void setMessages(Messages messages) throws ParseException {
        _messages = messages;
        SOURCE.put("messages", messages.getJson());
    }

    public Messages getMessages() {
        try {
            if (_messages == null && SOURCE.has("messages") && SOURCE.get("messages") != null)
                _messages = Messages.fromJson(SOURCE.getJsonObject("messages"));
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        if (_messages != null && _messages.isSet())
            return _messages;

        return null;
    }

    public WorkOrder messages(Messages messages) throws ParseException {
        _messages = messages;
        SOURCE.put("messages", messages.getJson());
        return this;
    }

    public void setMilestones(Milestones milestones) throws ParseException {
        _milestones = milestones;
        SOURCE.put("milestones", milestones.getJson());
    }

    public Milestones getMilestones() {
        try {
            if (_milestones == null && SOURCE.has("milestones") && SOURCE.get("milestones") != null)
                _milestones = Milestones.fromJson(SOURCE.getJsonObject("milestones"));
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        if (_milestones != null && _milestones.isSet())
            return _milestones;

        return null;
    }

    public WorkOrder milestones(Milestones milestones) throws ParseException {
        _milestones = milestones;
        SOURCE.put("milestones", milestones.getJson());
        return this;
    }

    public void setNetworks(Network[] networks) throws ParseException {
        _networks = networks;
        SOURCE.put("networks", Network.toJsonArray(networks));
    }

    public Network[] getNetworks() {
        try {
            if (_networks != null)
                return _networks;

            if (SOURCE.has("networks") && SOURCE.get("networks") != null) {
                _networks = Network.fromJsonArray(SOURCE.getJsonArray("networks"));
            }

        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _networks;
    }

    public WorkOrder networks(Network[] networks) throws ParseException {
        _networks = networks;
        SOURCE.put("networks", Network.toJsonArray(networks), true);
        return this;
    }

    public void setPay(Pay pay) throws ParseException {
        _pay = pay;
        SOURCE.put("pay", pay.getJson());
    }

    public Pay getPay() {
        try {
            if (_pay == null && SOURCE.has("pay") && SOURCE.get("pay") != null)
                _pay = Pay.fromJson(SOURCE.getJsonObject("pay"));
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        if (_pay != null && _pay.isSet())
            return _pay;

        return null;
    }

    public WorkOrder pay(Pay pay) throws ParseException {
        _pay = pay;
        SOURCE.put("pay", pay.getJson());
        return this;
    }

    public void setPolicyAndProcedures(RichText policyAndProcedures) throws ParseException {
        _policyAndProcedures = policyAndProcedures;
        SOURCE.put("policy_and_procedures", policyAndProcedures.getJson());
    }

    public RichText getPolicyAndProcedures() {
        try {
            if (_policyAndProcedures == null && SOURCE.has("policy_and_procedures") && SOURCE.get("policy_and_procedures") != null)
                _policyAndProcedures = RichText.fromJson(SOURCE.getJsonObject("policy_and_procedures"));
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        if (_policyAndProcedures != null && _policyAndProcedures.isSet())
            return _policyAndProcedures;

        return null;
    }

    public WorkOrder policyAndProcedures(RichText policyAndProcedures) throws ParseException {
        _policyAndProcedures = policyAndProcedures;
        SOURCE.put("policy_and_procedures", policyAndProcedures.getJson());
        return this;
    }

    public void setProject(Project project) throws ParseException {
        _project = project;
        SOURCE.put("project", project.getJson());
    }

    public Project getProject() {
        try {
            if (_project == null && SOURCE.has("project") && SOURCE.get("project") != null)
                _project = Project.fromJson(SOURCE.getJsonObject("project"));
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        if (_project != null && _project.isSet())
            return _project;

        return null;
    }

    public WorkOrder project(Project project) throws ParseException {
        _project = project;
        SOURCE.put("project", project.getJson());
        return this;
    }

    public void setPublishStats(PublishStats publishStats) throws ParseException {
        _publishStats = publishStats;
        SOURCE.put("publish_stats", publishStats.getJson());
    }

    public PublishStats getPublishStats() {
        try {
            if (_publishStats == null && SOURCE.has("publish_stats") && SOURCE.get("publish_stats") != null)
                _publishStats = PublishStats.fromJson(SOURCE.getJsonObject("publish_stats"));
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        if (_publishStats != null && _publishStats.isSet())
            return _publishStats;

        return null;
    }

    public WorkOrder publishStats(PublishStats publishStats) throws ParseException {
        _publishStats = publishStats;
        SOURCE.put("publish_stats", publishStats.getJson());
        return this;
    }

    public void setQualifications(Qualifications qualifications) throws ParseException {
        _qualifications = qualifications;
        SOURCE.put("qualifications", qualifications.getJson());
    }

    public Qualifications getQualifications() {
        try {
            if (_qualifications == null && SOURCE.has("qualifications") && SOURCE.get("qualifications") != null)
                _qualifications = Qualifications.fromJson(SOURCE.getJsonObject("qualifications"));
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        if (_qualifications != null && _qualifications.isSet())
            return _qualifications;

        return null;
    }

    public WorkOrder qualifications(Qualifications qualifications) throws ParseException {
        _qualifications = qualifications;
        SOURCE.put("qualifications", qualifications.getJson());
        return this;
    }

    public void setRatings(WorkOrderRatings ratings) throws ParseException {
        _ratings = ratings;
        SOURCE.put("ratings", ratings.getJson());
    }

    public WorkOrderRatings getRatings() {
        try {
            if (_ratings == null && SOURCE.has("ratings") && SOURCE.get("ratings") != null)
                _ratings = WorkOrderRatings.fromJson(SOURCE.getJsonObject("ratings"));
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        if (_ratings != null && _ratings.isSet())
            return _ratings;

        return null;
    }

    public WorkOrder ratings(WorkOrderRatings ratings) throws ParseException {
        _ratings = ratings;
        SOURCE.put("ratings", ratings.getJson());
        return this;
    }

    public void setRequests(Requests requests) throws ParseException {
        _requests = requests;
        SOURCE.put("requests", requests.getJson());
    }

    public Requests getRequests() {
        try {
            if (_requests == null && SOURCE.has("requests") && SOURCE.get("requests") != null)
                _requests = Requests.fromJson(SOURCE.getJsonObject("requests"));
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        if (_requests != null && _requests.isSet())
            return _requests;

        return null;
    }

    public WorkOrder requests(Requests requests) throws ParseException {
        _requests = requests;
        SOURCE.put("requests", requests.getJson());
        return this;
    }

    public void setRole(WorkOrderRole role) throws ParseException {
        _role = role;
        SOURCE.put("role", role.getJson());
    }

    public WorkOrderRole getRole() {
        try {
            if (_role == null && SOURCE.has("role") && SOURCE.get("role") != null)
                _role = WorkOrderRole.fromJson(SOURCE.getJsonObject("role"));
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        if (_role != null && _role.isSet())
            return _role;

        return null;
    }

    public WorkOrder role(WorkOrderRole role) throws ParseException {
        _role = role;
        SOURCE.put("role", role.getJson());
        return this;
    }

    public void setRoutes(Routes routes) throws ParseException {
        _routes = routes;
        SOURCE.put("routes", routes.getJson());
    }

    public Routes getRoutes() {
        try {
            if (_routes == null && SOURCE.has("routes") && SOURCE.get("routes") != null)
                _routes = Routes.fromJson(SOURCE.getJsonObject("routes"));
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        if (_routes != null && _routes.isSet())
            return _routes;

        return null;
    }

    public WorkOrder routes(Routes routes) throws ParseException {
        _routes = routes;
        SOURCE.put("routes", routes.getJson());
        return this;
    }

    public void setSchedule(Schedule schedule) throws ParseException {
        _schedule = schedule;
        SOURCE.put("schedule", schedule.getJson());
    }

    public Schedule getSchedule() {
        try {
            if (_schedule == null && SOURCE.has("schedule") && SOURCE.get("schedule") != null)
                _schedule = Schedule.fromJson(SOURCE.getJsonObject("schedule"));
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        if (_schedule != null && _schedule.isSet())
            return _schedule;

        return null;
    }

    public WorkOrder schedule(Schedule schedule) throws ParseException {
        _schedule = schedule;
        SOURCE.put("schedule", schedule.getJson());
        return this;
    }

    public void setSelectionRule(SelectionRule selectionRule) throws ParseException {
        _selectionRule = selectionRule;
        SOURCE.put("selection_rule", selectionRule.getJson());
    }

    public SelectionRule getSelectionRule() {
        try {
            if (_selectionRule == null && SOURCE.has("selection_rule") && SOURCE.get("selection_rule") != null)
                _selectionRule = SelectionRule.fromJson(SOURCE.getJsonObject("selection_rule"));
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        if (_selectionRule != null && _selectionRule.isSet())
            return _selectionRule;

        return null;
    }

    public WorkOrder selectionRule(SelectionRule selectionRule) throws ParseException {
        _selectionRule = selectionRule;
        SOURCE.put("selection_rule", selectionRule.getJson());
        return this;
    }

    public void setServiceContract(ServiceContract serviceContract) throws ParseException {
        _serviceContract = serviceContract;
        SOURCE.put("service_contract", serviceContract.getJson());
    }

    public ServiceContract getServiceContract() {
        try {
            if (_serviceContract == null && SOURCE.has("service_contract") && SOURCE.get("service_contract") != null)
                _serviceContract = ServiceContract.fromJson(SOURCE.getJsonObject("service_contract"));
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        if (_serviceContract != null && _serviceContract.isSet())
            return _serviceContract;

        return null;
    }

    public WorkOrder serviceContract(ServiceContract serviceContract) throws ParseException {
        _serviceContract = serviceContract;
        SOURCE.put("service_contract", serviceContract.getJson());
        return this;
    }

    public void setShipments(Shipments shipments) throws ParseException {
        _shipments = shipments;
        SOURCE.put("shipments", shipments.getJson());
    }

    public Shipments getShipments() {
        try {
            if (_shipments == null && SOURCE.has("shipments") && SOURCE.get("shipments") != null)
                _shipments = Shipments.fromJson(SOURCE.getJsonObject("shipments"));
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        if (_shipments != null && _shipments.isSet())
            return _shipments;

        return null;
    }

    public WorkOrder shipments(Shipments shipments) throws ParseException {
        _shipments = shipments;
        SOURCE.put("shipments", shipments.getJson());
        return this;
    }

    public void setSignatures(Signatures signatures) throws ParseException {
        _signatures = signatures;
        SOURCE.put("signatures", signatures.getJson());
    }

    public Signatures getSignatures() {
        try {
            if (_signatures == null && SOURCE.has("signatures") && SOURCE.get("signatures") != null)
                _signatures = Signatures.fromJson(SOURCE.getJsonObject("signatures"));
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        if (_signatures != null && _signatures.isSet())
            return _signatures;

        return null;
    }

    public WorkOrder signatures(Signatures signatures) throws ParseException {
        _signatures = signatures;
        SOURCE.put("signatures", signatures.getJson());
        return this;
    }

    public void setStandardInstructions(RichText standardInstructions) throws ParseException {
        _standardInstructions = standardInstructions;
        SOURCE.put("standard_instructions", standardInstructions.getJson());
    }

    public RichText getStandardInstructions() {
        try {
            if (_standardInstructions == null && SOURCE.has("standard_instructions") && SOURCE.get("standard_instructions") != null)
                _standardInstructions = RichText.fromJson(SOURCE.getJsonObject("standard_instructions"));
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        if (_standardInstructions != null && _standardInstructions.isSet())
            return _standardInstructions;

        return null;
    }

    public WorkOrder standardInstructions(RichText standardInstructions) throws ParseException {
        _standardInstructions = standardInstructions;
        SOURCE.put("standard_instructions", standardInstructions.getJson());
        return this;
    }

    public void setStatus(Status status) throws ParseException {
        _status = status;
        SOURCE.put("status", status.getJson());
    }

    public Status getStatus() {
        try {
            if (_status == null && SOURCE.has("status") && SOURCE.get("status") != null)
                _status = Status.fromJson(SOURCE.getJsonObject("status"));
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        if (_status != null && _status.isSet())
            return _status;

        return null;
    }

    public WorkOrder status(Status status) throws ParseException {
        _status = status;
        SOURCE.put("status", status.getJson());
        return this;
    }

    public void setTasks(Tasks tasks) throws ParseException {
        _tasks = tasks;
        SOURCE.put("tasks", tasks.getJson());
    }

    public Tasks getTasks() {
        try {
            if (_tasks == null && SOURCE.has("tasks") && SOURCE.get("tasks") != null)
                _tasks = Tasks.fromJson(SOURCE.getJsonObject("tasks"));
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        if (_tasks != null && _tasks.isSet())
            return _tasks;

        return null;
    }

    public WorkOrder tasks(Tasks tasks) throws ParseException {
        _tasks = tasks;
        SOURCE.put("tasks", tasks.getJson());
        return this;
    }

    public void setTeam(Team team) throws ParseException {
        _team = team;
        SOURCE.put("team", team.getJson());
    }

    public Team getTeam() {
        try {
            if (_team == null && SOURCE.has("team") && SOURCE.get("team") != null)
                _team = Team.fromJson(SOURCE.getJsonObject("team"));
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        if (_team != null && _team.isSet())
            return _team;

        return null;
    }

    public WorkOrder team(Team team) throws ParseException {
        _team = team;
        SOURCE.put("team", team.getJson());
        return this;
    }

    public void setTemplate(Template template) throws ParseException {
        _template = template;
        SOURCE.put("template", template.getJson());
    }

    public Template getTemplate() {
        try {
            if (_template == null && SOURCE.has("template") && SOURCE.get("template") != null)
                _template = Template.fromJson(SOURCE.getJsonObject("template"));
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        if (_template != null && _template.isSet())
            return _template;

        return null;
    }

    public WorkOrder template(Template template) throws ParseException {
        _template = template;
        SOURCE.put("template", template.getJson());
        return this;
    }

    public void setTimeLogs(TimeLogs timeLogs) throws ParseException {
        _timeLogs = timeLogs;
        SOURCE.put("time_logs", timeLogs.getJson());
    }

    public TimeLogs getTimeLogs() {
        try {
            if (_timeLogs == null && SOURCE.has("time_logs") && SOURCE.get("time_logs") != null)
                _timeLogs = TimeLogs.fromJson(SOURCE.getJsonObject("time_logs"));
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        if (_timeLogs != null && _timeLogs.isSet())
            return _timeLogs;

        return null;
    }

    public WorkOrder timeLogs(TimeLogs timeLogs) throws ParseException {
        _timeLogs = timeLogs;
        SOURCE.put("time_logs", timeLogs.getJson());
        return this;
    }

    public void setTitle(String title) throws ParseException {
        _title = title;
        SOURCE.put("title", title);
    }

    public String getTitle() {
        try {
            if (_title == null && SOURCE.has("title") && SOURCE.get("title") != null)
                _title = SOURCE.getString("title");
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _title;
    }

    public WorkOrder title(String title) throws ParseException {
        _title = title;
        SOURCE.put("title", title);
        return this;
    }

    public void setTypeOfWork(TypeOfWork typeOfWork) throws ParseException {
        _typeOfWork = typeOfWork;
        SOURCE.put("type_of_work", typeOfWork.getJson());
    }

    public TypeOfWork getTypeOfWork() {
        try {
            if (_typeOfWork == null && SOURCE.has("type_of_work") && SOURCE.get("type_of_work") != null)
                _typeOfWork = TypeOfWork.fromJson(SOURCE.getJsonObject("type_of_work"));
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        if (_typeOfWork != null && _typeOfWork.isSet())
            return _typeOfWork;

        return null;
    }

    public WorkOrder typeOfWork(TypeOfWork typeOfWork) throws ParseException {
        _typeOfWork = typeOfWork;
        SOURCE.put("type_of_work", typeOfWork.getJson());
        return this;
    }

    public void setW2(Boolean w2) throws ParseException {
        _w2 = w2;
        SOURCE.put("w2", w2);
    }

    public Boolean getW2() {
        try {
            if (_w2 == null && SOURCE.has("w2") && SOURCE.get("w2") != null)
                _w2 = SOURCE.getBoolean("w2");
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _w2;
    }

    public WorkOrder w2(Boolean w2) throws ParseException {
        _w2 = w2;
        SOURCE.put("w2", w2);
        return this;
    }

    public void setWorkflowCompletion(WorkflowCompletion workflowCompletion) throws ParseException {
        _workflowCompletion = workflowCompletion;
        SOURCE.put("workflow_completion", workflowCompletion.getJson());
    }

    public WorkflowCompletion getWorkflowCompletion() {
        try {
            if (_workflowCompletion == null && SOURCE.has("workflow_completion") && SOURCE.get("workflow_completion") != null)
                _workflowCompletion = WorkflowCompletion.fromJson(SOURCE.getJsonObject("workflow_completion"));
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        if (_workflowCompletion != null && _workflowCompletion.isSet())
            return _workflowCompletion;

        return null;
    }

    public WorkOrder workflowCompletion(WorkflowCompletion workflowCompletion) throws ParseException {
        _workflowCompletion = workflowCompletion;
        SOURCE.put("workflow_completion", workflowCompletion.getJson());
        return this;
    }

    /*-******************************-*/
    /*-             Enums            -*/
    /*-******************************-*/
    public enum ActionsEnum {
        @Json(name = "apply_tags")
        APPLY_TAGS("apply_tags"),
        @Json(name = "approve")
        APPROVE("approve"),
        @Json(name = "cancel")
        CANCEL("cancel"),
        @Json(name = "closing_notes")
        CLOSING_NOTES("closing_notes"),
        @Json(name = "complete")
        COMPLETE("complete"),
        @Json(name = "confirm")
        CONFIRM("confirm"),
        @Json(name = "copy")
        COPY("copy"),
        @Json(name = "delete")
        DELETE("delete"),
        @Json(name = "edit")
        EDIT("edit"),
        @Json(name = "history")
        HISTORY("history"),
        @Json(name = "hold")
        HOLD("hold"),
        @Json(name = "incomplete")
        INCOMPLETE("incomplete"),
        @Json(name = "map")
        MAP("map"),
        @Json(name = "messaging")
        MESSAGING("messaging"),
        @Json(name = "overview")
        OVERVIEW("overview"),
        @Json(name = "print")
        PRINT("print"),
        @Json(name = "publish")
        PUBLISH("publish"),
        @Json(name = "report_a_problem")
        REPORT_A_PROBLEM("report_a_problem"),
        @Json(name = "request")
        REQUEST("request"),
        @Json(name = "revert")
        REVERT("revert"),
        @Json(name = "search-provider")
        SEARCH_PROVIDER("search-provider"),
        @Json(name = "tasks")
        TASKS("tasks"),
        @Json(name = "unapprove")
        UNAPPROVE("unapprove"),
        @Json(name = "unpublish")
        UNPUBLISH("unpublish"),
        @Json(name = "view_problem")
        VIEW_PROBLEM("view_problem");

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
    public static JsonArray toJsonArray(WorkOrder[] array) {
        JsonArray list = new JsonArray();
        for (WorkOrder item : array) {
            list.add(item.getJson());
        }
        return list;
    }

    public static WorkOrder[] fromJsonArray(JsonArray array) {
        WorkOrder[] list = new WorkOrder[array.size()];
        for (int i = 0; i < array.size(); i++) {
            list[i] = fromJson(array.getJsonObject(i));
        }
        return list;
    }

    public static WorkOrder fromJson(JsonObject obj) {
        try {
            return new WorkOrder(obj);
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
    public static final Parcelable.Creator<WorkOrder> CREATOR = new Parcelable.Creator<WorkOrder>() {

        @Override
        public WorkOrder createFromParcel(Parcel source) {
            try {
                return WorkOrder.fromJson((JsonObject) source.readParcelable(JsonObject.class.getClassLoader()));
            } catch (Exception ex) {
                Log.v(TAG, ex);
                return null;
            }
        }

        @Override
        public WorkOrder[] newArray(int size) {
            return new WorkOrder[size];
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

    public boolean isSet() {
        return true;
    }

    private Set<ActionsEnum> _actionsSet = null;

    public Set<ActionsEnum> getActionsSet() {
        if (_actionsSet == null && getActions() != null) {
            _actionsSet = new HashSet<>();
            _actionsSet.addAll(Arrays.asList(getActions()));
        }
        return _actionsSet;
    }

    public boolean isOnHold() {
        if (getHolds() == null)
            return false;

        if (getHolds().getResults().length == 0)
            return false;

        if (getHolds().getResults().length > 0)
            return true;
        return false;
    }

    public boolean areHoldsAcknowledged() {
        if (getHolds() == null)
            return true;

        if (getHolds().getResults().length == 0)
            return true;

        Hold[] holds = getHolds().getResults();
        for (Hold hold : holds) {
            if (hold.getAcknowledgment().getStatus() != Acknowledgment.StatusEnum.ACKNOWLEDGED)
                return false;
        }

        return true;
    }

    public Hold getUnAcknowledgedHold() {
        if (getHolds() == null || getHolds().getResults().length == 0)
            return null;

        Hold[] holds = getHolds().getResults();
        for (Hold hold : holds) {
            if (hold.getAcknowledgment().getStatus() != Acknowledgment.StatusEnum.ACKNOWLEDGED)
                return hold;
        }

        return null;
    }
}
