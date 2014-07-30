package com.fieldnation.data.workorder;

import java.text.ParseException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import com.fieldnation.R;
import com.fieldnation.json.JsonObject;
import com.fieldnation.json.Serializer;
import com.fieldnation.json.annotations.Json;
import com.fieldnation.ui.workorder.WorkorderDataSelector;

public class Workorder {
	@Json(name = "location")
	private Location _location;
	@Json(name = "statusId")
	private Integer _statusId;
	@Json(name = "identifier")
	private String _identifier;
	@Json(name = "isRequest")
	private Boolean _isRequest;
	@Json(name = "distance")
	private Double _distance;
	@Json(name = "customFields")
	private CustomField[] _customFields;
	@Json(name = "fullWorkDescription")
	private String _fullWorkDescription;
	@Json(name = "bundleId")
	private Integer _bundleId;
	@Json(name = "industry")
	private String _industry;
	@Json(name = "workorderId")
	private Long _workorderId;
	@Json(name = "closing_notes")
	private String _closingNotes;
	@Json(name = "label")
	private Label[] _label;
	@Json(name = "estimatedSchedule")
	private EstimatedSchedule _estimatedSchedule;
	@Json(name = "skillsets")
	private Skillset[] _skillsets;
	@Json(name = "isCounter")
	private Boolean _isCounter;
	@Json(name = "pay")
	private Pay _pay;
	@Json(name = "standard_instructions")
	private String _standardInstructions;
	@Json(name = "discounts")
	private Discount[] _discounts;
	@Json(name = "customerPoliciesProcedures")
	private String _customerPoliciesProcedures;
	@Json(name = "tasks")
	private Task[] _tasks;
	@Json(name = "shipmentTracking")
	private ShipmentTracking[] _shipmentTracking;
	@Json(name = "company_name")
	private String _companyName;
	@Json(name = "additionalExpenses")
	private AdditionalExpense[] _additionalExpenses;
	@Json(name = "documents")
	private Documents[] _documents;
	@Json(name = "status")
	private String _status;
	@Json(name = "loggedWork")
	private LoggedWork[] _loggedWork;
	@Json(name = "declinedWo")
	private Integer _declinedWo;
	@Json(name = "schedule")
	private Schedule _schedule;
	@Json(name = "deliverables")
	private Deliverable[] _deliverables;
	@Json(name = "messages")
	private Integer _messages;
	@Json(name = "isRemoteWork")
	private Integer _isRemoteWork;
	@Json(name = "provider")
	private Provider[] _provider;
	@Json(name = "typeOfWork")
	private String _typeOfWork;
	@Json(name = "title")
	private String _title;

	public Workorder() {
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

	public Boolean getIsRequest() {
		return _isRequest;
	}

	public Double getDistance() {
		return _distance;
	}

	public CustomField[] getCustomFields() {
		return _customFields;
	}

	public String getFullWorkDescription() {
		return _fullWorkDescription;
	}

	public Integer getBundleId() {
		return _bundleId;
	}

	public String getIndustry() {
		return _industry;
	}

	public Long getWorkorderId(){
		return _workorderId;
	}

	public String getClosingNotes() {
		return _closingNotes;
	}

	public Label[] getLabels() {
		return _label;
	}

	public EstimatedSchedule getEstimatedSchedule() {
		return _estimatedSchedule;
	}

	public Skillset[] getSkillsets() {
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

	public Discount[] getDiscounts() {
		return _discounts;
	}

	public String getCustomerPoliciesProcedures() {
		return _customerPoliciesProcedures;
	}

	public Task[] getTasks() {
		return _tasks;
	}

	public ShipmentTracking[] getShipmentTracking() {
		return _shipmentTracking;
	}

	public String getCompanyName() {
		return _companyName;
	}

	public AdditionalExpense[] getAdditionalExpenses() {
		return _additionalExpenses;
	}

	public Documents[] getDocuments() {
		return _documents;
	}

	public String getStatus() {
		return _status;
	}

	public LoggedWork[] getLoggedWork() {
		return _loggedWork;
	}

	public Integer getDeclinedWo() {
		return _declinedWo;
	}

	public Schedule getSchedule() {
		return _schedule;
	}

	public Deliverable[] getDeliverables() {
		return _deliverables;
	}

	public Integer getMessages() {
		return _messages;
	}

	public Integer getIsRemoteWork() {
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

	public static Workorder fromJson(JsonObject json, WorkorderDataSelector selector) {
		try {
			Workorder wo = Serializer.unserializeObject(Workorder.class, json);
			wo._type = selector;
			wo.buildStatus();
			return wo;
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}

	/*-*********************************************-*/
	/*-				Not Generated Code				-*/
	/*-*********************************************-*/
	public static final int BUTTON_ACTION_NONE = 0;
	public static final int BUTTON_ACTION_REQUEST = 1;
	public static final int BUTTON_ACTION_ASSIGNMENT = 2;
	public static final int BUTTON_ACTION_CHECKIN = 3;
	public static final int BUTTON_ACTION_CHECKOUT = 4;
	public static final int BUTTON_ACTION_ACKNOWLEDGE_HOLD = 5;

	public static final int NOT_INTERESTED_ACTION_NONE = 0;
	public static final int NOT_INTERESTED_ACTION_DECLINE = 101;
	public static final int NOT_INTERESTED_ACTION_WITHDRAW_REQUEST = 102;
	public static final int NOT_INTERESTED_ACTION_CANCEL_ASSIGNMENT = 103;

	// status colors lookuptable
	private static final int[] _STATUS_LOOKUP_TABLE = {
			R.drawable.wo_card_status_white,
			R.drawable.wo_card_status_orange,
			R.drawable.wo_card_status_green,
			R.drawable.wo_card_status_gray };
	private static final int[] _STATUS_TEXT_TABLE = {
			R.color.woCardStatusLabel1,
			R.color.woCardStatusLabel2,
			R.color.woCardStatusLabel3,
			R.color.woCardStatusLabel4 };
	private static final int[] _STATUS_BUTTON_FG = {
			R.color.woCardButton1Foreground,
			R.color.woCardButton2Foreground,
			R.color.woCardButton3Foreground,
			R.color.woCardButton1Foreground };
	private static final int[] _STATUS_BUTTON_BG = {
			R.drawable.btn_white,
			R.drawable.btn_orange,
			R.drawable.btn_green,
			R.drawable.btn_white };

	private int _buttonAction = 0;
	private int _notInterestedAction = 0;
	private int _statusDisplayState = 0;
	private WorkorderDataSelector _type;
	private Set<Integer> _labelIds = new HashSet<Integer>();

	private Set<Listener> _listeners = new HashSet<Workorder.Listener>();

	public int getButtonAction() {
		return _buttonAction;
	}

	public int getNotInterestedAction() {
		return _notInterestedAction;
	}

	public WorkorderDataSelector getWorkorderDataSelector() {
		return _type;
	}

	public int getStatusBG() {
		return _STATUS_LOOKUP_TABLE[_statusDisplayState];
	}

	public int getStatusTextColor() {
		return _STATUS_TEXT_TABLE[_statusDisplayState];
	}

	public int getStatusButtonBG() {
		return _STATUS_BUTTON_BG[_statusDisplayState];
	}

	public int getStatusButtonFG() {
		return _STATUS_BUTTON_FG[_statusDisplayState];
	}

	public int getDisplayState() {
		return _statusDisplayState;
	}

	public void addListener(Listener listener) {
		_listeners.add(listener);
	}

	public void removeListener(Listener listener) {
		_listeners.remove(listener);
	}

	public void dispatchOnChange() {
		Iterator<Listener> iter = _listeners.iterator();
		while (iter.hasNext()) {
			iter.next().onChange(this);
		}
	}

	public boolean hasLabel(int labelId) {
		return _labelIds.contains(labelId);
	}

	public interface Listener {
		public void onChange(Workorder workorder);
	}

	private void buildStatus() throws ParseException {
		_notInterestedAction = NOT_INTERESTED_ACTION_NONE;
		_buttonAction = BUTTON_ACTION_NONE;

		_labelIds.clear();
		if (_label != null) {
			for (int i = 0; i < _label.length; i++) {
				_labelIds.add(_label[i].getLabelId());
			}
		}

		switch (_type) {
		case ASSIGNED:
			buildStatusAssigned();
			break;
		case AVAILABLE:
			buildStatusAvailable();
			break;
		case REQUESTED:
			buildStatusAvailable();
			break;
		case CANCELLED:
			buildStatusCancelled();
			break;
		case COMPLETED:
			buildStatusCompleted();
			break;
		case IN_PROGRESS:
			buildStatusInProgress();
			break;
		}
	}

	private void buildStatusAssigned() throws ParseException {
		// if on-hold, then only check for acknowledged
		boolean isOnHold = false;
		boolean isAcked = false;
		boolean has16 = false;

		// get on-hold value
		Label[] labels = getLabels();

		for (int i = 0; i < labels.length; i++) {
			Label label = labels[i];
			if (label.getType() != null) {
				if (label.getType().equals("on-hold"))
					isOnHold = true;

				if (label.getAction() == null || label.getAction().equals("acknowledge")) {
					isAcked = true;
				}
			}

			if (label.getLabelId() == 16) {
				has16 = true;
			}
		}

		if (isOnHold) {
			if (isAcked) {
				_statusDisplayState = 3;
				_buttonAction = BUTTON_ACTION_NONE;
			} else {
				_buttonAction = BUTTON_ACTION_ACKNOWLEDGE_HOLD;
				_statusDisplayState = 1;
			}
		} else if (has16) {
			_statusDisplayState = 2;
			_buttonAction = BUTTON_ACTION_ASSIGNMENT;
			_notInterestedAction = NOT_INTERESTED_ACTION_CANCEL_ASSIGNMENT;
		} else {
			_statusDisplayState = 0;
			_buttonAction = BUTTON_ACTION_CHECKIN;
		}
	}

	private void buildStatusAvailable() throws ParseException {
		long statusId = getStatusId();
		if (statusId == 9) {
			_statusDisplayState = 1;
			_buttonAction = BUTTON_ACTION_ASSIGNMENT;
			_notInterestedAction = NOT_INTERESTED_ACTION_DECLINE;
		} else {
			boolean has11 = false;
			boolean has12 = false;
			boolean has13 = false;

			Label[] labels = getLabels();

			for (int i = 0; i < labels.length; i++) {
				Label label = labels[i];
				if (label.getLabelId() == 11) {
					has11 = true;
				}
				if (label.getLabelId() == 12) {
					has12 = true;
				}
				if (label.getLabelId() == 13) {
					has13 = true;
				}
			}

			if (has11) {
				_statusDisplayState = 0;
				_buttonAction = BUTTON_ACTION_REQUEST;
				_notInterestedAction = NOT_INTERESTED_ACTION_DECLINE;

			} else if (has12) {
				_statusDisplayState = 2;
				_notInterestedAction = NOT_INTERESTED_ACTION_WITHDRAW_REQUEST;
			} else if (has13) {
				_statusDisplayState = 3;
				// TODO BUTTON_ACTION_VIEW_COUNTER!?
				// _buttonAction = BUTTON_ACTION_VIEW_COUNTER;
				_notInterestedAction = NOT_INTERESTED_ACTION_DECLINE;

			}
		}
	}

	private void buildStatusInProgress() throws ParseException {
		// if on-hold, then only check for acknowledged
		boolean isOnHold = false;
		boolean isAcked = false;
		boolean has1 = false;

		// get on-hold value
		Label[] labels = getLabels();

		for (int i = 0; i < labels.length; i++) {
			Label label = labels[i];
			if (label.getType() != null) {
				if (label.getType().equals("on-hold"))
					isOnHold = true;

				if (label.getAction() == null || label.getAction().equals("acknowledge")) {
					isAcked = true;
				}
			}

			if (label.getLabelId() == 1) {
				has1 = true;
			}
		}

		if (isOnHold) {
			if (isAcked) {
				_statusDisplayState = 3;
			} else {
				_statusDisplayState = 1;
				_buttonAction = BUTTON_ACTION_ACKNOWLEDGE_HOLD;
			}
		} else if (has1) {
			_statusDisplayState = 2;
			_buttonAction = BUTTON_ACTION_CHECKOUT;
		} else {
			_statusDisplayState = 0;
			_buttonAction = BUTTON_ACTION_CHECKIN;
		}
	}

	private void buildStatusCompleted() throws ParseException {
		Set<Integer> labels = new HashSet<Integer>();

		Label[] slabels = getLabels();
		for (int i = 0; i < slabels.length; i++) {
			int labelid = slabels[i].getLabelId();
			labels.add(labelid);
		}

		if (labels.contains(19)) {
			_statusDisplayState = 0;

		} else if (labels.contains(20)) {
			_statusDisplayState = 0;
		} else if (labels.contains(21)) {
			_statusDisplayState = 2;
		} else if (getStatus() != null && getStatus().equals("Paid")) {
			_statusDisplayState = 3;
		}
	}

	private void buildStatusCancelled() {
		// TODO METHOD STUB buildStatusCancelled!
	}

}
