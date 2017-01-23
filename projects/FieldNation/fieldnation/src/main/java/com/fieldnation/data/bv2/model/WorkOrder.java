package com.fieldnation.data.bv2.model;

import com.fieldnation.fnjson.JsonObject;
import com.fieldnation.fnjson.Serializer;
import com.fieldnation.fnjson.Unserializer;
import com.fieldnation.fnjson.annotations.Json;
import com.fieldnation.fnlog.Log;

public class WorkOrder {
    private static final String TAG = "WorkOrder";

    @Json(name = "work_order_id")
    private Integer workOrderId = null;

    @Json(name = "networks")
    private String[] networks;

    @Json(name = "correlation_id")
    private String correlationId = null;

    @Json(name = "title")
    private String title = null;

    @Json(name = "assignee")
    private Assignee assignee = null;

    @Json(name = "type_of_work")
    private TypeOfWork typeOfWork = null;

    @Json(name = "manager")
    private Manager manager = null;

    @Json(name = "bundle")
    private Bundle bundle = null;

    @Json(name = "project")
    private Project project = null;

    @Json(name = "client")
    private Company client = null;

    @Json(name = "company")
    private Company company = null;

    @Json(name = "custom_fields")
    private CustomFields customFields = null;

    @Json(name = "w2")
    private Boolean w2 = null;

    @Json(name = "role")
    private String role = null;

    @Json(name = "location")
    private Location location = null;

    @Json(name = "schedule")
    private Schedule schedule = null;

    @Json(name = "pay")
    private Pays pay = null;

    @Json(name = "contacts")
    private Contacts contacts = null;

    @Json(name = "shipments")
    private Shipments shipments = null;

    @Json(name = "attachments")
    private AttachmentFolders attachments = null;

    @Json(name = "tasks")
    private Tasks tasks = null;

    @Json(name = "messages")
    private Messages messages = null;

    @Json(name = "status")
    private Status status = null;

    @Json(name = "holds")
    private Holds holds = null;

    @Json(name = "milestones")
    private Milestones milestones = null;

    @Json(name = "description")
    private RichText description = null;

    @Json(name = "confidential")
    private RichText confidential = null;

    @Json(name = "policy_and_procedures")
    private RichText policyAndProcedures = null;

    @Json(name = "standard_instructions")
    private RichText standardInstructions = null;

    @Json(name = "selection_rule")
    private SelectionRule selectionRule = null;

    public WorkOrder() {
    }

    public Integer getWorkOrderId() {
        return workOrderId;
    }

    public String[] getNetworks() {
        return networks;
    }

    public String getCorrelationId() {
        return correlationId;
    }

    public String getTitle() {
        return title;
    }

    public Assignee getAssignee() {
        return assignee;
    }

    public TypeOfWork getTypeOfWork() {
        return typeOfWork;
    }

    public Manager getManager() {
        return manager;
    }

    public Bundle getBundle() {
        return bundle;
    }

    public Project getProject() {
        return project;
    }

    public Company getClient() {
        return client;
    }

    public Company getCompany() {
        return company;
    }

    public CustomFields getCustomFields() {
        return customFields;
    }

    public Boolean getW2() {
        return w2;
    }

    public String getRole() {
        return role;
    }

    public Location getLocation() {
        return location;
    }

    public Schedule getSchedule() {
        return schedule;
    }

    public Pays getPay() {
        return pay;
    }

    public Contacts getContacts() {
        return contacts;
    }

    public Shipments getShipments() {
        return shipments;
    }

    public AttachmentFolders getAttachments() {
        return attachments;
    }

    public Tasks getTasks() {
        return tasks;
    }

    public Messages getMessages() {
        return messages;
    }

    public Status getStatus() {
        return status;
    }

    public Holds getHolds() {
        return holds;
    }

    public Milestones getMilestones() {
        return milestones;
    }

    public RichText getDescription() {
        return description;
    }

    public RichText getConfidential() {
        return confidential;
    }

    public RichText getPolicyAndProcedures() {
        return policyAndProcedures;
    }

    public RichText getStandardInstructions() {
        return standardInstructions;
    }

    public SelectionRule getSelectionRule() {
        return selectionRule;
    }

    /*-*****************************-*/
    /*-             Json            -*/
    /*-*****************************-*/
    public static WorkOrder fromJson(JsonObject obj) {
        try {
            return Unserializer.unserializeObject(WorkOrder.class, obj);
        } catch (Exception ex) {
            Log.v(TAG, ex);
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
            Log.v(TAG, ex);
            return null;
        }
    }
}