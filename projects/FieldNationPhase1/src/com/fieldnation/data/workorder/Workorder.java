package com.fieldnation.data.workorder;

import com.fieldnation.json.JsonObject;
import com.fieldnation.json.Serializer;
import com.fieldnation.json.annotations.Json;

public class Workorder {
	@Json(name = "workorder_id")
	private Integer _workorderId;
	@Json(name = "location")
	private Location _location;
	@Json(name = "statusId")
	private Integer _statusId;
	@Json(name = "identifier")
	private String _identifier;
	@Json(name="isRequest")
	private Boolean _isRequest;
	@Json(name = "distance")
	private Double _distance;
	@Json(name = "customFields")
	private CustomFields[] _customFields;
	@Json(name = "fullWorkDescription")
	private String _fullWorkDescription;
	@Json(name = "bundleId")
	private Integer _bundleId;
	@Json(name = "industry")
	private String _industry;
	@Json(name = "closing_notes")
	private String _closingNotes;
	@Json(name = "label")
	private Label[] _label;
	@Json(name = "estimatedSchedule")
	private EstimatedSchedule _estimatedSchedule;
	@Json(name = "skillsets")
	private Skillsets[] _skillsets;
	@Json(name = "isCounter")
	private Boolean _isCounter;
	@Json(name = "pay")
	private Pay _pay;
	@Json(name = "standard_instructions")
	private String _standardInstructions;
	@Json(name = "discounts")
	private Discounts[] _discounts;
	@Json(name = "customerPoliciesProcedures")
	private String _customerPoliciesProcedures;
	@Json(name = "scheduledTimeStart")
	private String _scheduledTimeStart;
	@Json(name = "tasks")
	private Tasks[] _tasks;
	@Json(name = "scheduledTimeEnd")
	private String _scheduledTimeEnd;
	@Json(name = "company_name")
	private String _companyName;
	@Json(name = "additionalExpenses")
	private AdditionalExpenses[] _additionalExpenses;
	@Json(name = "documents")
	private Documents[] _documents;
	@Json(name = "status")
	private String _status;
	@Json(name = "declinedWo")
	private Integer _declinedWo;
	@Json(name = "schedule")
	private Schedule _schedule;
	@Json(name = "deliverables")
	private Deliverables[] _deliverables;
	@Json(name = "messages")
	private Integer _messages;
	@Json(name="isRemoteWork")
	private Integer _isRemoteWork;
	@Json(name = "provider")
	private Provider[] _provider;
	@Json(name = "typeOfWork")
	private String _typeOfWork;
	@Json(name = "title")
	private String _title;

	public Workorder() {
	}
	public Integer getWorkorderId(){
		return _workorderId;
	}

	public Location getLocation() {
		return _location;
	}

	public Integer getStatusId() {
		return _statusId;
	}

	public String getIdentifier() {
		return _identifier;
	}

	public Boolean getIsRequest(){
		return _isRequest;
	}

	public Double getDistance() {
		return _distance;
	}

	public CustomFields[] getCustomFields() {
		return _customFields;
	}

	public String getFullWorkDescription() {
		return _fullWorkDescription;
	}

	public Integer getBundleId(){
		return _bundleId;
	}

	public String getIndustry() {
		return _industry;
	}

	public String getClosingNotes() {
		return _closingNotes;
	}

	public Label[] getLabel(){
		return _label;
	}

	public EstimatedSchedule getEstimatedSchedule() {
		return _estimatedSchedule;
	}

	public Skillsets[] getSkillsets() {
		return _skillsets;
	}

	public Boolean getIsCounter() {
		return _isCounter;
	}

	public Pay getPay() {
		return _pay;
	}

	public String getStandardInstructions() {
		return _standardInstructions;
	}

	public Discounts[] getDiscounts() {
		return _discounts;
	}

	public String getCustomerPoliciesProcedures() {
		return _customerPoliciesProcedures;
	}

	public String getScheduledTimeStart() {
		return _scheduledTimeStart;
	}

	public Tasks[] getTasks() {
		return _tasks;
	}

	public String getScheduledTimeEnd() {
		return _scheduledTimeEnd;
	}

	public String getCompanyName() {
		return _companyName;
	}

	public AdditionalExpenses[] getAdditionalExpenses() {
		return _additionalExpenses;
	}

	public Documents[] getDocuments() {
		return _documents;
	}

	public String getStatus() {
		return _status;
	}

	public Integer getDeclinedWo() {
		return _declinedWo;
	}

	public Schedule getSchedule() {
		return _schedule;
	}

	public Deliverables[] getDeliverables() {
		return _deliverables;
	}

	public Integer getMessages() {
		return _messages;
	}

	public Integer getIsRemoteWork(){
		return _isRemoteWork;
	}

	public Provider[] getProvider() {
		return _provider;
	}

	public String getTypeOfWork() {
		return _typeOfWork;
	}

	public String getTitle() {
		return _title;
	}

	public JsonObject toJson() {
		return toJson(this);
	}

	public static JsonObject toJson(Workorder workorder) {
		try {
			return Serializer.serializeObject(workorder);
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}

	public static Workorder fromJson(JsonObject json) {
		try {
			return Serializer.unserializeObject(Workorder.class, json);
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}

}
