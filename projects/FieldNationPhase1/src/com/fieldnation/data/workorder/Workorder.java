package com.fieldnation.data.workorder;

import com.fieldnation.json.JsonObject;
import com.fieldnation.json.Serializer;
import com.fieldnation.json.annotations.Json;

public class Workorder{
	@Json(name="workorder_id")
	private int _workorderId;
	@Json(name="location")
	private Location _location;
	@Json(name="statusId")
	private int _statusId;
	@Json(name="identifier")
	private String _identifier;
	@Json(name="distance")
	private double _distance;
	@Json(name="customFields")
	private CustomFields[] _customFields;
	@Json(name="fullWorkDescription")
	private String _fullWorkDescription;
	@Json(name="bundleId")
	private String _bundleId;
	@Json(name="industry")
	private String _industry;
	@Json(name="closing_notes")
	private String _closingNotes;
	@Json(name="label")
	private Label[] _label;
	@Json(name="estimatedSchedule")
	private EstimatedSchedule _estimatedSchedule;
	@Json(name="skillsets")
	private Skillsets[] _skillsets;
	@Json(name="isCounter")
	private boolean _isCounter;
	@Json(name="pay")
	private Pay _pay;
	@Json(name="standard_instructions")
	private String _standardInstructions;
	@Json(name="discounts")
	private Discounts[] _discounts;
	@Json(name="customerPoliciesProcedures")
	private String _customerPoliciesProcedures;
	@Json(name="scheduledTimeStart")
	private int _scheduledTimeStart;
	@Json(name="tasks")
	private Tasks[] _tasks;
	@Json(name="scheduledTimeEnd")
	private String _scheduledTimeEnd;
	@Json(name="company_name")
	private String _companyName;
	@Json(name="additionalExpenses")
	private AdditionalExpenses[] _additionalExpenses;
	@Json(name="documents")
	private Documents[] _documents;
	@Json(name="status")
	private String _status;
	@Json(name="declinedWo")
	private int _declinedWo;
	@Json(name="schedule")
	private Schedule _schedule;
	@Json(name="deliverables")
	private Deliverables[] _deliverables;
	@Json(name="messages")
	private int _messages;
	@Json(name="provider")
	private Provider[] _provider;
	@Json(name="isRemoteWork")
	private int _isRemoteWork;
	@Json(name="typeOfWork")
	private String _typeOfWork;
	@Json(name="title")
	private String _title;

	public Workorder(){
	}
	public int getWorkorderId(){
		return _workorderId;
	}

	public Location getLocation(){
		return _location;
	}

	public int getStatusId(){
		return _statusId;
	}

	public String getIdentifier(){
		return _identifier;
	}

	public double getDistance(){
		return _distance;
	}

	public CustomFields[] getCustomFields(){
		return _customFields;
	}

	public String getFullWorkDescription(){
		return _fullWorkDescription;
	}

	public String getBundleId(){
		return _bundleId;
	}

	public String getIndustry(){
		return _industry;
	}

	public String getClosingNotes(){
		return _closingNotes;
	}

	public Label[] getLabel(){
		return _label;
	}

	public EstimatedSchedule getEstimatedSchedule(){
		return _estimatedSchedule;
	}

	public Skillsets[] getSkillsets(){
		return _skillsets;
	}

	public boolean getIsCounter(){
		return _isCounter;
	}

	public Pay getPay(){
		return _pay;
	}

	public String getStandardInstructions(){
		return _standardInstructions;
	}

	public Discounts[] getDiscounts(){
		return _discounts;
	}

	public String getCustomerPoliciesProcedures(){
		return _customerPoliciesProcedures;
	}

	public int getScheduledTimeStart(){
		return _scheduledTimeStart;
	}

	public Tasks[] getTasks(){
		return _tasks;
	}

	public String getScheduledTimeEnd(){
		return _scheduledTimeEnd;
	}

	public String getCompanyName(){
		return _companyName;
	}

	public AdditionalExpenses[] getAdditionalExpenses(){
		return _additionalExpenses;
	}

	public Documents[] getDocuments(){
		return _documents;
	}

	public String getStatus(){
		return _status;
	}

	public int getDeclinedWo(){
		return _declinedWo;
	}

	public Schedule getSchedule(){
		return _schedule;
	}

	public Deliverables[] getDeliverables(){
		return _deliverables;
	}

	public int getMessages(){
		return _messages;
	}

	public Provider[] getProvider(){
		return _provider;
	}

	public int getIsRemoteWork(){
		return _isRemoteWork;
	}

	public String getTypeOfWork(){
		return _typeOfWork;
	}

	public String getTitle(){
		return _title;
	}

	public JsonObject toJson(){
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
