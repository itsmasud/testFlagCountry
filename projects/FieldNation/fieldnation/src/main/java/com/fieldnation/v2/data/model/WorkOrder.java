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

    @Json(name = "discounts")
    private PayModifiers _discounts;

    @Json(name = "holds")
    private Hold[] _holds;

    @Json(name = "location")
    private Location _location;

    @Json(name = "manager")
    private Manager _manager;

    @Json(name = "messages")
    private Messages _messages;

    @Json(name = "milestones")
    private Milestones _milestones;

    @Json(name = "networks")
    private String[] _networks;

    @Json(name = "pay")
    private Pay _pay;

    @Json(name = "policy_and_procedures")
    private RichText _policyAndProcedures;

    @Json(name = "project")
    private Project _project;

    @Json(name = "publish_stats")
    private WorkOrderPublishStats _publishStats;

    @Json(name = "qualifications")
    private Qualifications _qualifications;

    @Json(name = "ratings")
    private Rating _ratings;

    @Json(name = "requests")
    private Requests _requests;

    @Json(name = "role")
    private String _role;

    @Json(name = "schedule")
    private Schedule _schedule;

    @Json(name = "selection_rule")
    private SelectionRule _selectionRule;

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

    @Json(name = "time_logs")
    private TimeLogs _timeLogs;

    @Json(name = "title")
    private String _title;

    @Json(name = "type_of_work")
    private TypeOfWork _typeOfWork;

    @Json(name = "w2")
    private Boolean _w2;

    @Json(name = "work_order_id")
    private Integer _workOrderId;

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
            if (_assignee != null)
                return _assignee;

            if (SOURCE.has("assignee") && SOURCE.get("assignee") != null)
                _assignee = Assignee.fromJson(SOURCE.getJsonObject("assignee"));

        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _assignee;
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
            if (_attachments != null)
                return _attachments;

            if (SOURCE.has("attachments") && SOURCE.get("attachments") != null)
                _attachments = AttachmentFolders.fromJson(SOURCE.getJsonObject("attachments"));

        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _attachments;
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
            if (_bundle != null)
                return _bundle;

            if (SOURCE.has("bundle") && SOURCE.get("bundle") != null)
                _bundle = Bundle.fromJson(SOURCE.getJsonObject("bundle"));

        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _bundle;
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
            if (_client != null)
                return _client;

            if (SOURCE.has("client") && SOURCE.get("client") != null)
                _client = Company.fromJson(SOURCE.getJsonObject("client"));

        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _client;
    }

    public WorkOrder client(Company client) throws ParseException {
        _client = client;
        SOURCE.put("client", client.getJson());
        return this;
    }

    public void setClosingNotes(String closingNotes) throws ParseException {
        _closingNotes = closingNotes;
        SOURCE.put("closing_notes", closingNotes);
    }

    public String getClosingNotes() {
        try {
            if (_closingNotes != null)
                return _closingNotes;

            if (SOURCE.has("closing_notes") && SOURCE.get("closing_notes") != null)
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
            if (_company != null)
                return _company;

            if (SOURCE.has("company") && SOURCE.get("company") != null)
                _company = Company.fromJson(SOURCE.getJsonObject("company"));

        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _company;
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
            if (_confidential != null)
                return _confidential;

            if (SOURCE.has("confidential") && SOURCE.get("confidential") != null)
                _confidential = RichText.fromJson(SOURCE.getJsonObject("confidential"));

        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _confidential;
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
            if (_contacts != null)
                return _contacts;

            if (SOURCE.has("contacts") && SOURCE.get("contacts") != null)
                _contacts = Contacts.fromJson(SOURCE.getJsonObject("contacts"));

        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _contacts;
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
            if (_correlationId != null)
                return _correlationId;

            if (SOURCE.has("correlation_id") && SOURCE.get("correlation_id") != null)
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
            if (_customFields != null)
                return _customFields;

            if (SOURCE.has("custom_fields") && SOURCE.get("custom_fields") != null)
                _customFields = CustomFields.fromJson(SOURCE.getJsonObject("custom_fields"));

        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _customFields;
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
            if (_description != null)
                return _description;

            if (SOURCE.has("description") && SOURCE.get("description") != null)
                _description = RichText.fromJson(SOURCE.getJsonObject("description"));

        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _description;
    }

    public WorkOrder description(RichText description) throws ParseException {
        _description = description;
        SOURCE.put("description", description.getJson());
        return this;
    }

    public void setDiscounts(PayModifiers discounts) throws ParseException {
        _discounts = discounts;
        SOURCE.put("discounts", discounts.getJson());
    }

    public PayModifiers getDiscounts() {
        try {
            if (_discounts != null)
                return _discounts;

            if (SOURCE.has("discounts") && SOURCE.get("discounts") != null)
                _discounts = PayModifiers.fromJson(SOURCE.getJsonObject("discounts"));

        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _discounts;
    }

    public WorkOrder discounts(PayModifiers discounts) throws ParseException {
        _discounts = discounts;
        SOURCE.put("discounts", discounts.getJson());
        return this;
    }

    public void setHolds(Hold[] holds) throws ParseException {
        _holds = holds;
        SOURCE.put("holds", Hold.toJsonArray(holds));
    }

    public Hold[] getHolds() {
        try {
            if (_holds != null)
                return _holds;

            if (SOURCE.has("holds") && SOURCE.get("holds") != null) {
                _holds = Hold.fromJsonArray(SOURCE.getJsonArray("holds"));
            }

        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _holds;
    }

    public WorkOrder holds(Hold[] holds) throws ParseException {
        _holds = holds;
        SOURCE.put("holds", Hold.toJsonArray(holds), true);
        return this;
    }

    public void setLocation(Location location) throws ParseException {
        _location = location;
        SOURCE.put("location", location.getJson());
    }

    public Location getLocation() {
        try {
            if (_location != null)
                return _location;

            if (SOURCE.has("location") && SOURCE.get("location") != null)
                _location = Location.fromJson(SOURCE.getJsonObject("location"));

        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _location;
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
            if (_manager != null)
                return _manager;

            if (SOURCE.has("manager") && SOURCE.get("manager") != null)
                _manager = Manager.fromJson(SOURCE.getJsonObject("manager"));

        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _manager;
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
            if (_messages != null)
                return _messages;

            if (SOURCE.has("messages") && SOURCE.get("messages") != null)
                _messages = Messages.fromJson(SOURCE.getJsonObject("messages"));

        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _messages;
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
            if (_milestones != null)
                return _milestones;

            if (SOURCE.has("milestones") && SOURCE.get("milestones") != null)
                _milestones = Milestones.fromJson(SOURCE.getJsonObject("milestones"));

        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _milestones;
    }

    public WorkOrder milestones(Milestones milestones) throws ParseException {
        _milestones = milestones;
        SOURCE.put("milestones", milestones.getJson());
        return this;
    }

    public void setNetworks(String[] networks) throws ParseException {
        _networks = networks;
        JsonArray ja = new JsonArray();
        for (String item : networks) {
            ja.add(item);
        }
        SOURCE.put("networks", ja);
    }

    public String[] getNetworks() {
        try {
            if (_networks != null)
                return _networks;

            if (SOURCE.has("networks") && SOURCE.get("networks") != null) {
                JsonArray ja = SOURCE.getJsonArray("networks");
                _networks = ja.toArray(new String[ja.size()]);
            }

        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _networks;
    }

    public WorkOrder networks(String[] networks) throws ParseException {
        _networks = networks;
        JsonArray ja = new JsonArray();
        for (String item : networks) {
            ja.add(item);
        }
        SOURCE.put("networks", ja, true);
        return this;
    }

    public void setPay(Pay pay) throws ParseException {
        _pay = pay;
        SOURCE.put("pay", pay.getJson());
    }

    public Pay getPay() {
        try {
            if (_pay != null)
                return _pay;

            if (SOURCE.has("pay") && SOURCE.get("pay") != null)
                _pay = Pay.fromJson(SOURCE.getJsonObject("pay"));

        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _pay;
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
            if (_policyAndProcedures != null)
                return _policyAndProcedures;

            if (SOURCE.has("policy_and_procedures") && SOURCE.get("policy_and_procedures") != null)
                _policyAndProcedures = RichText.fromJson(SOURCE.getJsonObject("policy_and_procedures"));

        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _policyAndProcedures;
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
            if (_project != null)
                return _project;

            if (SOURCE.has("project") && SOURCE.get("project") != null)
                _project = Project.fromJson(SOURCE.getJsonObject("project"));

        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _project;
    }

    public WorkOrder project(Project project) throws ParseException {
        _project = project;
        SOURCE.put("project", project.getJson());
        return this;
    }

    public void setPublishStats(WorkOrderPublishStats publishStats) throws ParseException {
        _publishStats = publishStats;
        SOURCE.put("publish_stats", publishStats.getJson());
    }

    public WorkOrderPublishStats getPublishStats() {
        try {
            if (_publishStats != null)
                return _publishStats;

            if (SOURCE.has("publish_stats") && SOURCE.get("publish_stats") != null)
                _publishStats = WorkOrderPublishStats.fromJson(SOURCE.getJsonObject("publish_stats"));

        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _publishStats;
    }

    public WorkOrder publishStats(WorkOrderPublishStats publishStats) throws ParseException {
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
            if (_qualifications != null)
                return _qualifications;

            if (SOURCE.has("qualifications") && SOURCE.get("qualifications") != null)
                _qualifications = Qualifications.fromJson(SOURCE.getJsonObject("qualifications"));

        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _qualifications;
    }

    public WorkOrder qualifications(Qualifications qualifications) throws ParseException {
        _qualifications = qualifications;
        SOURCE.put("qualifications", qualifications.getJson());
        return this;
    }

    public void setRatings(Rating ratings) throws ParseException {
        _ratings = ratings;
        SOURCE.put("ratings", ratings.getJson());
    }

    public Rating getRatings() {
        try {
            if (_ratings != null)
                return _ratings;

            if (SOURCE.has("ratings") && SOURCE.get("ratings") != null)
                _ratings = Rating.fromJson(SOURCE.getJsonObject("ratings"));

        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _ratings;
    }

    public WorkOrder ratings(Rating ratings) throws ParseException {
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
            if (_requests != null)
                return _requests;

            if (SOURCE.has("requests") && SOURCE.get("requests") != null)
                _requests = Requests.fromJson(SOURCE.getJsonObject("requests"));

        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _requests;
    }

    public WorkOrder requests(Requests requests) throws ParseException {
        _requests = requests;
        SOURCE.put("requests", requests.getJson());
        return this;
    }

    public void setRole(String role) throws ParseException {
        _role = role;
        SOURCE.put("role", role);
    }

    public String getRole() {
        try {
            if (_role != null)
                return _role;

            if (SOURCE.has("role") && SOURCE.get("role") != null)
                _role = SOURCE.getString("role");

        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _role;
    }

    public WorkOrder role(String role) throws ParseException {
        _role = role;
        SOURCE.put("role", role);
        return this;
    }

    public void setSchedule(Schedule schedule) throws ParseException {
        _schedule = schedule;
        SOURCE.put("schedule", schedule.getJson());
    }

    public Schedule getSchedule() {
        try {
            if (_schedule != null)
                return _schedule;

            if (SOURCE.has("schedule") && SOURCE.get("schedule") != null)
                _schedule = Schedule.fromJson(SOURCE.getJsonObject("schedule"));

        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _schedule;
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
            if (_selectionRule != null)
                return _selectionRule;

            if (SOURCE.has("selection_rule") && SOURCE.get("selection_rule") != null)
                _selectionRule = SelectionRule.fromJson(SOURCE.getJsonObject("selection_rule"));

        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _selectionRule;
    }

    public WorkOrder selectionRule(SelectionRule selectionRule) throws ParseException {
        _selectionRule = selectionRule;
        SOURCE.put("selection_rule", selectionRule.getJson());
        return this;
    }

    public void setShipments(Shipments shipments) throws ParseException {
        _shipments = shipments;
        SOURCE.put("shipments", shipments.getJson());
    }

    public Shipments getShipments() {
        try {
            if (_shipments != null)
                return _shipments;

            if (SOURCE.has("shipments") && SOURCE.get("shipments") != null)
                _shipments = Shipments.fromJson(SOURCE.getJsonObject("shipments"));

        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _shipments;
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
            if (_signatures != null)
                return _signatures;

            if (SOURCE.has("signatures") && SOURCE.get("signatures") != null)
                _signatures = Signatures.fromJson(SOURCE.getJsonObject("signatures"));

        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _signatures;
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
            if (_standardInstructions != null)
                return _standardInstructions;

            if (SOURCE.has("standard_instructions") && SOURCE.get("standard_instructions") != null)
                _standardInstructions = RichText.fromJson(SOURCE.getJsonObject("standard_instructions"));

        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _standardInstructions;
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
            if (_status != null)
                return _status;

            if (SOURCE.has("status") && SOURCE.get("status") != null)
                _status = Status.fromJson(SOURCE.getJsonObject("status"));

        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _status;
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
            if (_tasks != null)
                return _tasks;

            if (SOURCE.has("tasks") && SOURCE.get("tasks") != null)
                _tasks = Tasks.fromJson(SOURCE.getJsonObject("tasks"));

        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _tasks;
    }

    public WorkOrder tasks(Tasks tasks) throws ParseException {
        _tasks = tasks;
        SOURCE.put("tasks", tasks.getJson());
        return this;
    }

    public void setTimeLogs(TimeLogs timeLogs) throws ParseException {
        _timeLogs = timeLogs;
        SOURCE.put("time_logs", timeLogs.getJson());
    }

    public TimeLogs getTimeLogs() {
        try {
            if (_timeLogs != null)
                return _timeLogs;

            if (SOURCE.has("time_logs") && SOURCE.get("time_logs") != null)
                _timeLogs = TimeLogs.fromJson(SOURCE.getJsonObject("time_logs"));

        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _timeLogs;
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
            if (_title != null)
                return _title;

            if (SOURCE.has("title") && SOURCE.get("title") != null)
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
            if (_typeOfWork != null)
                return _typeOfWork;

            if (SOURCE.has("type_of_work") && SOURCE.get("type_of_work") != null)
                _typeOfWork = TypeOfWork.fromJson(SOURCE.getJsonObject("type_of_work"));

        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _typeOfWork;
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
            if (_w2 != null)
                return _w2;

            if (SOURCE.has("w2") && SOURCE.get("w2") != null)
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

    public void setWorkOrderId(Integer workOrderId) throws ParseException {
        _workOrderId = workOrderId;
        SOURCE.put("work_order_id", workOrderId);
    }

    public Integer getWorkOrderId() {
        try {
            if (_workOrderId != null)
                return _workOrderId;

            if (SOURCE.has("work_order_id") && SOURCE.get("work_order_id") != null)
                _workOrderId = SOURCE.getInt("work_order_id");

        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _workOrderId;
    }

    public WorkOrder workOrderId(Integer workOrderId) throws ParseException {
        _workOrderId = workOrderId;
        SOURCE.put("work_order_id", workOrderId);
        return this;
    }

    /*-******************************-*/
    /*-             Enums            -*/
    /*-******************************-*/
    public enum ActionsEnum {
        @Json(name = "closing_notes")
        CLOSING_NOTES("closing_notes"),
        @Json(name = "confirm")
        CONFIRM("confirm"),
        @Json(name = "map")
        MAP("map"),
        @Json(name = "mark_complete")
        MARK_COMPLETE("mark_complete"),
        @Json(name = "mark_incomplete")
        MARK_INCOMPLETE("mark_incomplete"),
        @Json(name = "messaging")
        MESSAGING("messaging"),
        @Json(name = "report_a_problem")
        REPORT_A_PROBLEM("report_a_problem"),
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
    private Set<ActionsEnum> _actionsSet = null;

    public Set<ActionsEnum> getActionsSet() {
        if (_actionsSet == null) {
            _actionsSet = new HashSet<>();
            _actionsSet.addAll(Arrays.asList(getActions()));
        }

        return _actionsSet;
    }
}
