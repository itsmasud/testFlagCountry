package com.fieldnation.data.bv2.model;

import com.fieldnation.fnjson.JsonObject;
import com.fieldnation.fnjson.Serializer;
import com.fieldnation.fnjson.Unserializer;
import com.fieldnation.fnjson.annotations.Json;
import com.fieldnation.fnlog.Log;

public class WorkOrder {
    private static final String TAG = "WorkOrder";

    @Json(name = "role")
    private String _role;

    @Json(name = "attachments")
    private AttachmentFolders _attachments;

    @Json(name = "project")
    private Project _project;

    @Json(name = "description")
    private RichText _description;

    @Json(name = "networks")
    private String[] _networks;

    @Json(name = "title")
    private String _title;

    @Json(name = "client")
    private Company _client;

    @Json(name = "company")
    private Company _company;

    @Json(name = "w2")
    private Boolean _w2;

    @Json(name = "policy_and_procedures")
    private RichText _policyAndProcedures;

    @Json(name = "bundle")
    private Bundle _bundle;

    @Json(name = "tasks")
    private Tasks _tasks;

    @Json(name = "confidential")
    private RichText _confidential;

    @Json(name = "standard_instructions")
    private RichText _standardInstructions;

    @Json(name = "manager")
    private Manager _manager;

    @Json(name = "custom_fields")
    private CustomFields _customFields;

    @Json(name = "type_of_work")
    private TypeOfWork _typeOfWork;

    @Json(name = "pay")
    private Pays _pay;

    @Json(name = "shipments")
    private Shipments _shipments;

    @Json(name = "schedule")
    private Schedule _schedule;

    @Json(name = "selection_rule")
    private SelectionRule _selectionRule;

    @Json(name = "work_order_id")
    private Integer _workOrderId;

    @Json(name = "correlation_id")
    private String _correlationId;

    @Json(name = "messages")
    private Messages _messages;

    @Json(name = "holds")
    private Holds _holds;

    @Json(name = "location")
    private Location _location;

    @Json(name = "assignee")
    private Assignee _assignee;

    @Json(name = "milestones")
    private Milestones _milestones;

    @Json(name = "contacts")
    private Contacts _contacts;

    @Json(name = "status")
    private Status _status;

    public WorkOrder() {
    }

    public String getRole() {
        return _role;
    }

    public AttachmentFolders getAttachments() {
        return _attachments;
    }

    public Project getProject() {
        return _project;
    }

    public RichText getDescription() {
        return _description;
    }

    public String[] getNetworks() {
        return _networks;
    }

    public String getTitle() {
        return _title;
    }

    public Company getClient() {
        return _client;
    }

    public Company getCompany() {
        return _company;
    }

    public Boolean getW2() {
        return _w2;
    }

    public RichText getPolicyAndProcedures() {
        return _policyAndProcedures;
    }

    public Bundle getBundle() {
        return _bundle;
    }

    public Tasks getTasks() {
        return _tasks;
    }

    public RichText getConfidential() {
        return _confidential;
    }

    public RichText getStandardInstructions() {
        return _standardInstructions;
    }

    public Manager getManager() {
        return _manager;
    }

    public CustomFields getCustomFields() {
        return _customFields;
    }

    public TypeOfWork getTypeOfWork() {
        return _typeOfWork;
    }

    public Pays getPay() {
        return _pay;
    }

    public Shipments getShipments() {
        return _shipments;
    }

    public Schedule getSchedule() {
        return _schedule;
    }

    public SelectionRule getSelectionRule() {
        return _selectionRule;
    }

    public Integer getWorkOrderId() {
        return _workOrderId;
    }

    public String getCorrelationId() {
        return _correlationId;
    }

    public Messages getMessages() {
        return _messages;
    }

    public Holds getHolds() {
        return _holds;
    }

    public Location getLocation() {
        return _location;
    }

    public Assignee getAssignee() {
        return _assignee;
    }

    public Milestones getMilestones() {
        return _milestones;
    }

    public Contacts getContacts() {
        return _contacts;
    }

    public Status getStatus() {
        return _status;
    }

    /*-*****************************-*/
    /*-             Json            -*/
    /*-*****************************-*/
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
}
