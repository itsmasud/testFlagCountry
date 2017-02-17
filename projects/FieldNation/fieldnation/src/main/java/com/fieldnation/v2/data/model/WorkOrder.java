package com.fieldnation.v2.data.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.fieldnation.fnjson.JsonArray;
import com.fieldnation.fnjson.JsonObject;
import com.fieldnation.fnjson.Unserializer;
import com.fieldnation.fnjson.annotations.Json;
import com.fieldnation.fnjson.annotations.Source;
import com.fieldnation.fnlog.Log;

import java.text.ParseException;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by dmgen from swagger.
 */

public class WorkOrder implements Parcelable {
    private static final String TAG = "WorkOrder";

    @Json(name = "actions")
    private ActionsEnum[] _actions;

    @Json(name = "allow_counter_offers")
    private Boolean _allowCounterOffers;

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
    private JsonObject SOURCE = new JsonObject();

    public WorkOrder() {
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

    public void setAllowCounterOffers(Boolean allowCounterOffers) throws ParseException {
        _allowCounterOffers = allowCounterOffers;
        SOURCE.put("allow_counter_offers", allowCounterOffers);
    }

    public Boolean getAllowCounterOffers() {
        return _allowCounterOffers;
    }

    public WorkOrder allowCounterOffers(Boolean allowCounterOffers) throws ParseException {
        _allowCounterOffers = allowCounterOffers;
        SOURCE.put("allow_counter_offers", allowCounterOffers);
        return this;
    }

    public void setAssignee(Assignee assignee) throws ParseException {
        _assignee = assignee;
        SOURCE.put("assignee", assignee.getJson());
    }

    public Assignee getAssignee() {
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
        return _description;
    }

    public WorkOrder description(RichText description) throws ParseException {
        _description = description;
        SOURCE.put("description", description.getJson());
        return this;
    }

    public void setHolds(Hold[] holds) throws ParseException {
        _holds = holds;
        SOURCE.put("holds", Hold.toJsonArray(holds));
    }

    public Hold[] getHolds() {
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
        return _qualifications;
    }

    public WorkOrder qualifications(Qualifications qualifications) throws ParseException {
        _qualifications = qualifications;
        SOURCE.put("qualifications", qualifications.getJson());
        return this;
    }

    public void setRequests(Requests requests) throws ParseException {
        _requests = requests;
        SOURCE.put("requests", requests.getJson());
    }

    public Requests getRequests() {
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
        @Json(name = "check_in")
        CHECK_IN("check_in"),
        @Json(name = "check_out")
        CHECK_OUT("check_out"),
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
        @Json(name = "request")
        REQUEST("request"),
        @Json(name = "view_problem")
        VIEW_PROBLEM("view_problem"),
        @Json(name = "withdraw_request")
        WITHDRAW_REQUEST("withdraw_request");

        private String value;

        ActionsEnum(String value) {
            this.value = value;
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
            return Unserializer.unserializeObject(WorkOrder.class, obj);
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

    public ActionsEnum[] getSortedActions() {
        Arrays.sort(_actions, new Comparator<ActionsEnum>() {
            @Override
            public int compare(ActionsEnum lhs, ActionsEnum rhs) {
                if (lhs == null || rhs == null)
                    return 0;

                if (lhs.ordinal() < rhs.ordinal())
                    return -1;
                else if (lhs.ordinal() > rhs.ordinal())
                    return 1;
                else
                    return 0;
            }
        });
        return _actions;
    }

    private Set<ActionsEnum> _actionsSet = null;

    public Set<ActionsEnum> getActionsSet() {
        if (_actionsSet == null) {
            _actionsSet = new HashSet<>();
            _actionsSet.addAll(Arrays.asList(_actions));
        }

        return _actionsSet;
    }
}
