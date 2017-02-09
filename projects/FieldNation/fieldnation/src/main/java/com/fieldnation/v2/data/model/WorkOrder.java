package com.fieldnation.v2.data.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.fieldnation.fnjson.JsonArray;
import com.fieldnation.fnjson.JsonObject;
import com.fieldnation.fnjson.Serializer;
import com.fieldnation.fnjson.Unserializer;
import com.fieldnation.fnjson.annotations.Json;
import com.fieldnation.fnlog.Log;

import java.util.Arrays;
import java.util.Comparator;

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
    private Holds _holds;

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

    @Json(name = "role")
    private String _role;

    @Json(name = "schedule")
    private Schedule _schedule;

    @Json(name = "selection_rule")
    private SelectionRule _selectionRule;

    @Json(name = "shipments")
    private Shipments _shipments;

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

    public WorkOrder() {
    }

    public void setActions(ActionsEnum[] actions) {
        _actions = actions;
    }

    public ActionsEnum[] getActions() {
        return _actions;
    }

    public WorkOrder actions(ActionsEnum[] actions) {
        _actions = actions;
        return this;
    }

    public void setAssignee(Assignee assignee) {
        _assignee = assignee;
    }

    public Assignee getAssignee() {
        return _assignee;
    }

    public WorkOrder assignee(Assignee assignee) {
        _assignee = assignee;
        return this;
    }

    public void setAttachments(AttachmentFolders attachments) {
        _attachments = attachments;
    }

    public AttachmentFolders getAttachments() {
        return _attachments;
    }

    public WorkOrder attachments(AttachmentFolders attachments) {
        _attachments = attachments;
        return this;
    }

    public void setBundle(Bundle bundle) {
        _bundle = bundle;
    }

    public Bundle getBundle() {
        return _bundle;
    }

    public WorkOrder bundle(Bundle bundle) {
        _bundle = bundle;
        return this;
    }

    public void setClient(Company client) {
        _client = client;
    }

    public Company getClient() {
        return _client;
    }

    public WorkOrder client(Company client) {
        _client = client;
        return this;
    }

    public void setCompany(Company company) {
        _company = company;
    }

    public Company getCompany() {
        return _company;
    }

    public WorkOrder company(Company company) {
        _company = company;
        return this;
    }

    public void setConfidential(RichText confidential) {
        _confidential = confidential;
    }

    public RichText getConfidential() {
        return _confidential;
    }

    public WorkOrder confidential(RichText confidential) {
        _confidential = confidential;
        return this;
    }

    public void setContacts(Contacts contacts) {
        _contacts = contacts;
    }

    public Contacts getContacts() {
        return _contacts;
    }

    public WorkOrder contacts(Contacts contacts) {
        _contacts = contacts;
        return this;
    }

    public void setCorrelationId(String correlationId) {
        _correlationId = correlationId;
    }

    public String getCorrelationId() {
        return _correlationId;
    }

    public WorkOrder correlationId(String correlationId) {
        _correlationId = correlationId;
        return this;
    }

    public void setCustomFields(CustomFields customFields) {
        _customFields = customFields;
    }

    public CustomFields getCustomFields() {
        return _customFields;
    }

    public WorkOrder customFields(CustomFields customFields) {
        _customFields = customFields;
        return this;
    }

    public void setDescription(RichText description) {
        _description = description;
    }

    public RichText getDescription() {
        return _description;
    }

    public WorkOrder description(RichText description) {
        _description = description;
        return this;
    }

    public void setHolds(Holds holds) {
        _holds = holds;
    }

    public Holds getHolds() {
        return _holds;
    }

    public WorkOrder holds(Holds holds) {
        _holds = holds;
        return this;
    }

    public void setLocation(Location location) {
        _location = location;
    }

    public Location getLocation() {
        return _location;
    }

    public WorkOrder location(Location location) {
        _location = location;
        return this;
    }

    public void setManager(Manager manager) {
        _manager = manager;
    }

    public Manager getManager() {
        return _manager;
    }

    public WorkOrder manager(Manager manager) {
        _manager = manager;
        return this;
    }

    public void setMessages(Messages messages) {
        _messages = messages;
    }

    public Messages getMessages() {
        return _messages;
    }

    public WorkOrder messages(Messages messages) {
        _messages = messages;
        return this;
    }

    public void setMilestones(Milestones milestones) {
        _milestones = milestones;
    }

    public Milestones getMilestones() {
        return _milestones;
    }

    public WorkOrder milestones(Milestones milestones) {
        _milestones = milestones;
        return this;
    }

    public void setNetworks(String[] networks) {
        _networks = networks;
    }

    public String[] getNetworks() {
        return _networks;
    }

    public WorkOrder networks(String[] networks) {
        _networks = networks;
        return this;
    }

    public void setPay(Pay pay) {
        _pay = pay;
    }

    public Pay getPay() {
        return _pay;
    }

    public WorkOrder pay(Pay pay) {
        _pay = pay;
        return this;
    }

    public void setPolicyAndProcedures(RichText policyAndProcedures) {
        _policyAndProcedures = policyAndProcedures;
    }

    public RichText getPolicyAndProcedures() {
        return _policyAndProcedures;
    }

    public WorkOrder policyAndProcedures(RichText policyAndProcedures) {
        _policyAndProcedures = policyAndProcedures;
        return this;
    }

    public void setProject(Project project) {
        _project = project;
    }

    public Project getProject() {
        return _project;
    }

    public WorkOrder project(Project project) {
        _project = project;
        return this;
    }

    public void setPublishStats(WorkOrderPublishStats publishStats) {
        _publishStats = publishStats;
    }

    public WorkOrderPublishStats getPublishStats() {
        return _publishStats;
    }

    public WorkOrder publishStats(WorkOrderPublishStats publishStats) {
        _publishStats = publishStats;
        return this;
    }

    public void setQualifications(Qualifications qualifications) {
        _qualifications = qualifications;
    }

    public Qualifications getQualifications() {
        return _qualifications;
    }

    public WorkOrder qualifications(Qualifications qualifications) {
        _qualifications = qualifications;
        return this;
    }

    public void setRole(String role) {
        _role = role;
    }

    public String getRole() {
        return _role;
    }

    public WorkOrder role(String role) {
        _role = role;
        return this;
    }

    public void setSchedule(Schedule schedule) {
        _schedule = schedule;
    }

    public Schedule getSchedule() {
        return _schedule;
    }

    public WorkOrder schedule(Schedule schedule) {
        _schedule = schedule;
        return this;
    }

    public void setSelectionRule(SelectionRule selectionRule) {
        _selectionRule = selectionRule;
    }

    public SelectionRule getSelectionRule() {
        return _selectionRule;
    }

    public WorkOrder selectionRule(SelectionRule selectionRule) {
        _selectionRule = selectionRule;
        return this;
    }

    public void setShipments(Shipments shipments) {
        _shipments = shipments;
    }

    public Shipments getShipments() {
        return _shipments;
    }

    public WorkOrder shipments(Shipments shipments) {
        _shipments = shipments;
        return this;
    }

    public void setStandardInstructions(RichText standardInstructions) {
        _standardInstructions = standardInstructions;
    }

    public RichText getStandardInstructions() {
        return _standardInstructions;
    }

    public WorkOrder standardInstructions(RichText standardInstructions) {
        _standardInstructions = standardInstructions;
        return this;
    }

    public void setStatus(Status status) {
        _status = status;
    }

    public Status getStatus() {
        return _status;
    }

    public WorkOrder status(Status status) {
        _status = status;
        return this;
    }

    public void setTasks(Tasks tasks) {
        _tasks = tasks;
    }

    public Tasks getTasks() {
        return _tasks;
    }

    public WorkOrder tasks(Tasks tasks) {
        _tasks = tasks;
        return this;
    }

    public void setTimeLogs(TimeLogs timeLogs) {
        _timeLogs = timeLogs;
    }

    public TimeLogs getTimeLogs() {
        return _timeLogs;
    }

    public WorkOrder timeLogs(TimeLogs timeLogs) {
        _timeLogs = timeLogs;
        return this;
    }

    public void setTitle(String title) {
        _title = title;
    }

    public String getTitle() {
        return _title;
    }

    public WorkOrder title(String title) {
        _title = title;
        return this;
    }

    public void setTypeOfWork(TypeOfWork typeOfWork) {
        _typeOfWork = typeOfWork;
    }

    public TypeOfWork getTypeOfWork() {
        return _typeOfWork;
    }

    public WorkOrder typeOfWork(TypeOfWork typeOfWork) {
        _typeOfWork = typeOfWork;
        return this;
    }

    public void setW2(Boolean w2) {
        _w2 = w2;
    }

    public Boolean getW2() {
        return _w2;
    }

    public WorkOrder w2(Boolean w2) {
        _w2 = w2;
        return this;
    }

    public void setWorkOrderId(Integer workOrderId) {
        _workOrderId = workOrderId;
    }

    public Integer getWorkOrderId() {
        return _workOrderId;
    }

    public WorkOrder workOrderId(Integer workOrderId) {
        _workOrderId = workOrderId;
        return this;
    }

    /*-******************************-*/
    /*-             Enums            -*/
    /*-******************************-*/
    public enum ActionsEnum {
        @Json(name = "view_problem")
        VIEW_PROBLEM("view_problem"),
        @Json(name = "request")
        REQUEST("request"),
        @Json(name = "check_out")
        CHECK_OUT("check_out"),
        @Json(name = "report_a_problem")
        REPORT_A_PROBLEM("report_a_problem"),
        @Json(name = "eta")
        ETA("eta"),
        @Json(name = "edit")
        EDIT("edit"),
        @Json(name = "mark_complete")
        MARK_COMPLETE("mark_complete"),
        @Json(name = "check_in")
        CHECK_IN("check_in"),
        @Json(name = "messaging")
        MESSAGING("messaging");

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

    public JsonObject toJson() {
        return toJson(this);
    }

    public static JsonObject toJson(WorkOrder workOrder) {
        try {
            return Serializer.serializeObject(workOrder);
        } catch (Exception ex) {
            Log.v(TAG, TAG, ex);
            return null;
        }
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
        dest.writeParcelable(toJson(), flags);
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
}
