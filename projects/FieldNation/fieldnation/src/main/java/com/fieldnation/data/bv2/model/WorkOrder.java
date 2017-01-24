package com.fieldnation.data.bv2.model;

import com.fieldnation.fnjson.JsonObject;
import com.fieldnation.fnjson.Serializer;
import com.fieldnation.fnjson.Unserializer;
import com.fieldnation.fnjson.annotations.Json;
import com.fieldnation.fnlog.Log;

public class WorkOrder {
    private static final String TAG = "WorkOrder";

    @Json(name = "role")
    private String role;

    @Json(name = "attachments")
    private AttachmentFolders attachments;

    @Json(name = "project")
    private Project project;

    @Json(name = "description")
    private RichText description;

    @Json(name = "networks")
    private String[] networks;

    @Json(name = "title")
    private String title;

    @Json(name = "client")
    private Company client;

    @Json(name = "company")
    private Company company;

    @Json(name = "w2")
    private Boolean w2;

    @Json(name = "policy_and_procedures")
    private RichText policyAndProcedures;

    @Json(name = "bundle")
    private Bundle bundle;

    @Json(name = "tasks")
    private Tasks tasks;

    @Json(name = "confidential")
    private RichText confidential;

    @Json(name = "standard_instructions")
    private RichText standardInstructions;

    @Json(name = "manager")
    private Manager manager;

    @Json(name = "custom_fields")
    private CustomFields customFields;

    @Json(name = "type_of_work")
    private TypeOfWork typeOfWork;

    @Json(name = "pay")
    private Pays pay;

    @Json(name = "shipments")
    private Shipments shipments;

    @Json(name = "schedule")
    private Schedule schedule;

    @Json(name = "selection_rule")
    private SelectionRule selectionRule;

    @Json(name = "work_order_id")
    private Integer workOrderId;

    @Json(name = "correlation_id")
    private String correlationId;

    @Json(name = "messages")
    private Messages messages;

    @Json(name = "holds")
    private Holds holds;

    @Json(name = "location")
    private Location location;

    @Json(name = "assignee")
    private Assignee assignee;

    @Json(name = "milestones")
    private Milestones milestones;

    @Json(name = "contacts")
    private Contacts contacts;

    @Json(name = "status")
    private Status status;

    public WorkOrder() {
    }

    public String getRole() {
        return role;
    }

    public AttachmentFolders getAttachments() {
        return attachments;
    }

    public Project getProject() {
        return project;
    }

    public RichText getDescription() {
        return description;
    }

    public String[] getNetworks() {
        return networks;
    }

    public String getTitle() {
        return title;
    }

    public Company getClient() {
        return client;
    }

    public Company getCompany() {
        return company;
    }

    public Boolean getW2() {
        return w2;
    }

    public RichText getPolicyAndProcedures() {
        return policyAndProcedures;
    }

    public Bundle getBundle() {
        return bundle;
    }

    public Tasks getTasks() {
        return tasks;
    }

    public RichText getConfidential() {
        return confidential;
    }

    public RichText getStandardInstructions() {
        return standardInstructions;
    }

    public Manager getManager() {
        return manager;
    }

    public CustomFields getCustomFields() {
        return customFields;
    }

    public TypeOfWork getTypeOfWork() {
        return typeOfWork;
    }

    public Pays getPay() {
        return pay;
    }

    public Shipments getShipments() {
        return shipments;
    }

    public Schedule getSchedule() {
        return schedule;
    }

    public SelectionRule getSelectionRule() {
        return selectionRule;
    }

    public Integer getWorkOrderId() {
        return workOrderId;
    }

    public String getCorrelationId() {
        return correlationId;
    }

    public Messages getMessages() {
        return messages;
    }

    public Holds getHolds() {
        return holds;
    }

    public Location getLocation() {
        return location;
    }

    public Assignee getAssignee() {
        return assignee;
    }

    public Milestones getMilestones() {
        return milestones;
    }

    public Contacts getContacts() {
        return contacts;
    }

    public Status getStatus() {
        return status;
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
