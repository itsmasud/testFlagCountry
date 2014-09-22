package com.fieldnation.data.workorder;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import com.fieldnation.R;
import com.fieldnation.json.JsonObject;
import com.fieldnation.json.Serializer;
import com.fieldnation.json.annotations.Json;

public class Workorder {
	@Json(name = "isAssignedToWorkorder")
	private Boolean _isAssignedToWorkorder;
	@Json(name = "discounts")
	private Discount[] _discounts;
	@Json(name = "typeOfWork")
	private String _typeOfWork;
	@Json(name = "fullWorkDescription")
	private String _fullWorkDescription;
	@Json(name = "workorderId")
	private Long _workorderId;
	@Json(name = "hasFund")
	private Boolean _hasFund;
	@Json(name = "pay")
	private Pay _pay;
	@Json(name = "bundleId")
	private Long _bundleId;
	@Json(name = "customFields")
	private CustomField[] _customFields;
	@Json(name = "declinedWo")
	private Integer _declinedWo;
	@Json(name = "timezone")
	private String _timezone;
	@Json(name = "skillsets")
	private Skillset[] _skillsets;
	@Json(name = "messageCount")
	private Integer _messageCount;
	@Json(name = "identifier")
	private String _identifier;
	@Json(name = "checkInOutInfo")
	private CheckInOutInfo _checkInOutInfo;
	@Json(name = "loggedWork")
	private LoggedWork[] _loggedWork;
	@Json(name = "alertCount ")
	private Integer _alertCount;
	@Json(name = "customerPoliciesProcedures")
	private String _customerPoliciesProcedures;
	@Json(name = "location")
	private Location _location;
	@Json(name = "additionalExpenses")
	private AdditionalExpense[] _additionalExpenses;
	@Json(name = "messages")
	private Integer _messages;
	@Json(name = "industry")
	private String _industry;
	@Json(name = "standardInstructions")
	private String _standardInstructions;
	@Json(name = "provider")
	private Provider _provider;
	@Json(name = "canViewServicePayRateInfo")
	private Boolean _canViewServicePayRateInfo;
	@Json(name = "isRemoteWork")
	private Integer _isRemoteWork;
	@Json(name = "distance")
	private Double _distance;
	@Json(name = "statusId")
	private Integer _statusId;
	@Json(name = "schedule")
	private Schedule _schedule;
	@Json(name = "deliverables")
	private Deliverable[] _deliverables;
	@Json(name = "expectedPayment")
	private ExpectedPayment _expectedPayment;
	@Json(name = "confidentialInformation")
	private String _confidentialInformation;
	@Json(name = "companyName")
	private String _companyName;
	@Json(name = "estimatedSchedule")
	private EstimatedSchedule _estimatedSchedule;
	@Json(name = "status")
	private Status _status;
	@Json(name = "isWorkPerformed")
	private Boolean _isWorkPerformed;
	@Json(name = "uploadSlots")
	private UploadSlot[] _uploadSlots;
	@Json(name = "title")
	private String _title;
	@Json(name = "tasks")
	private Task[] _tasks;
	@Json(name = "w2")
	private Integer _w2;
	@Json(name = "companyId")
	private Integer _companyId;
	@Json(name = "isDeliverablesUploaded")
	private Boolean _isDeliverablesUploaded;
	@Json(name = "alerts")
	private Integer _alerts;
	@Json(name = "cancelFee")
	private Double _cancelFee;
	@Json(name = "bundleCount")
	private Integer _bundleCount;
	@Json(name = "isRequest")
	private Boolean _isRequest;
	@Json(name = "isCounter")
	private Boolean _isCounter;
	@Json(name = "documents")
	private Document[] _documents;
	@Json(name = "displayCounterOffer")
	private Integer _displayCounterOffer;
	@Json(name = "closingNotes")
	private String _closingNotes;
	// @Json(name = "label")
	// private Label[] _label;
	@Json(name = "shipmentTracking")
	private ShipmentTracking[] _shipmentTracking;
	@Json(name = "isWoOnHold")
	private Boolean _isWoOnHold;

	public Workorder() {
	}

	public Boolean getIsAssignedToWorkorder() {
		return _isAssignedToWorkorder;
	}

	public Discount[] getDiscounts() {
		return _discounts;
	}

	public String getTypeOfWork() {
		return _typeOfWork;
	}

	public String getFullWorkDescription() {
		return _fullWorkDescription;
	}

	public Long getWorkorderId() {
		return _workorderId;
	}

	public Boolean getHasFund() {
		return _hasFund;
	}

	public Pay getPay() {
		return _pay;
	}

	public Long getBundleId() {
		return _bundleId;
	}

	public CustomField[] getCustomFields() {
		return _customFields;
	}

	public Integer getDeclinedWo() {
		return _declinedWo;
	}

	public String getTimezone() {
		return _timezone;
	}

	public Skillset[] getSkillsets() {
		return _skillsets;
	}

	public Integer getMessageCount() {
		return _messageCount;
	}

	public String getIdentifier() {
		return _identifier;
	}

	public CheckInOutInfo getCheckInOutInfo() {
		return _checkInOutInfo;
	}

	public LoggedWork[] getLoggedWork() {
		return _loggedWork;
	}

	public Integer getAlertCount() {
		return _alertCount;
	}

	public String getCustomerPoliciesProcedures() {
		return _customerPoliciesProcedures;
	}

	public Location getLocation() {
		return _location;
	}

	public AdditionalExpense[] getAdditionalExpenses() {
		return _additionalExpenses;
	}

	public Integer getMessages() {
		return _messages;
	}

	public String getIndustry() {
		return _industry;
	}

	public String getStandardInstructions() {
		return _standardInstructions;
	}

	public Provider getProvider() {
		return _provider;
	}

	public Boolean getCanViewServicePayRateInfo() {
		return _canViewServicePayRateInfo;
	}

	public Integer getIsRemoteWork() {
		return _isRemoteWork;
	}

	public Double getDistance() {
		return _distance;
	}

	public Integer getStatusId() {
		return _statusId;
	}

	public Schedule getSchedule() {
		return _schedule;
	}

	public Deliverable[] getDeliverables() {
		return _deliverables;
	}

	public ExpectedPayment getExpectedPayment() {
		return _expectedPayment;
	}

	public String getConfidentialInformation() {
		return _confidentialInformation;
	}

	public String getCompanyName() {
		return _companyName;
	}

	public EstimatedSchedule getEstimatedSchedule() {
		return _estimatedSchedule;
	}

	public Status getStatus() {
		if (_status == null) {
			String data = "Status is null: " + _workorderId;
			System.out.println(data);
		} else if (_status.getWorkorderStatus() == null) {
			String data = "Could not get status: " + _workorderId + "\r\n";
			data += _status.toJson().display();

			System.out.println(data);
		} else if (_status.getWorkorderSubstatus() == null) {
			String data = "Could not get substatus: " + _workorderId + "\r\n";
			data += _status.toJson().display();

			System.out.println(data);
		}

		return _status;
	}

	public Boolean getIsWorkPerformed() {
		return _isWorkPerformed;
	}

	public UploadSlot[] getUploadSlots() {
		return _uploadSlots;
	}

	public String getTitle() {
		return _title;
	}

	public Task[] getTasks() {
		return _tasks;
	}

	public Integer getW2() {
		return _w2;
	}

	public Integer getCompanyId() {
		return _companyId;
	}

	public Boolean getIsDeliverablesUploaded() {
		return _isDeliverablesUploaded;
	}

	public Integer getAlerts() {
		return _alerts;
	}

	public Double getCancelFee() {
		return _cancelFee;
	}

	public Integer getBundleCount() {
		return _bundleCount;
	}

	public Boolean getIsRequest() {
		return _isRequest;
	}

	public Boolean getIsCounter() {
		return _isCounter;
	}

	public Document[] getDocuments() {
		return _documents;
	}

	public Integer getDisplayCounterOffer() {
		return _displayCounterOffer;
	}

	public String getClosingNotes() {
		return _closingNotes;
	}

	// public Label[] getLabels() {
	// return _label;
	// }

	public ShipmentTracking[] getShipmentTracking() {
		return _shipmentTracking;
	}

	public Boolean getIsWoOnHold() {
		return _isWoOnHold;
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
			Workorder wo = Serializer.unserializeObject(Workorder.class, json);
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
	public static final int BUTTON_ACTION_VIEW_COUNTER = 6;
	public static final int BUTTON_ACTION_VIEW_PAYMENT = 7;

	public static final int NOT_INTERESTED_ACTION_NONE = 0;
	public static final int NOT_INTERESTED_ACTION_DECLINE = 101;
	public static final int NOT_INTERESTED_ACTION_WITHDRAW_REQUEST = 102;
	public static final int NOT_INTERESTED_ACTION_CANCEL_ASSIGNMENT = 103;

	// status colors lookuptable
	private static final int[] _STATUS_LOOKUP_TABLE = { R.drawable.card_status_white, R.drawable.card_status_orange,
			R.drawable.card_status_green, R.drawable.card_status_gray };
	private static final int[] _STATUS_TEXT_TABLE = { R.color.woCardStatusLabel1, R.color.woCardStatusLabel2,
			R.color.woCardStatusLabel3, R.color.woCardStatusLabel4 };
	private static final int[] _STATUS_BUTTON_FG = { R.color.btn_white_fg, R.color.btn_orange_fg, R.color.btn_green_fg,
			R.color.btn_gray_fg };
	private static final int[] _STATUS_BUTTON_BG = { R.drawable.btn_white, R.drawable.btn_orange, R.drawable.btn_green,
			R.drawable.btn_white };

	private int _buttonAction = 0;
	private int _notInterestedAction = 0;
	// private Set<Integer> _labelIds = new HashSet<Integer>();

	private Set<Listener> _listeners = new HashSet<Workorder.Listener>();

	public int getButtonAction() {
		return _buttonAction;
	}

	public int getNotInterestedAction() {
		return _notInterestedAction;
	}

	public int getStatusBG() {
		return _STATUS_LOOKUP_TABLE[getStatusIntent().ordinal()];
	}

	public int getStatusTextColor() {
		return _STATUS_TEXT_TABLE[getStatusIntent().ordinal()];
	}

	public int getStatusButtonBG() {
		return _STATUS_BUTTON_BG[getStatusIntent().ordinal()];
	}

	public int getStatusButtonFG() {
		return _STATUS_BUTTON_FG[getStatusIntent().ordinal()];
	}

	public StatusIntent getStatusIntent() {
		return getStatus().getStatusIntent();
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

	public interface Listener {
		public void onChange(Workorder workorder);
	}

	private void buildStatus() {
		_notInterestedAction = NOT_INTERESTED_ACTION_NONE;
		_buttonAction = BUTTON_ACTION_NONE;

		Status status = getStatus();

		switch (status.getWorkorderStatus()) {
		case ASSIGNED:
			buildStatusAssigned(status);
			break;
		case AVAILABLE:
			buildStatusAvailable(status);
			break;
		case CANCELLED:
			buildStatusCancelled(status);
			break;
		case COMPLETED:
		case APPROVED:
		case PAID:
			buildStatusCompleted(status);
			break;
		case INPROGRESS:
			buildStatusInProgress(status);
			break;
		default:
			System.out.println("Unknown Status (" + _workorderId + "): " + status.toJson().toString());
			break;

		}
	}

	private void buildStatusAvailable(Status status) {
		switch (status.getWorkorderSubstatus()) {
		case AVAILABLE:
			_buttonAction = BUTTON_ACTION_REQUEST;
			_notInterestedAction = NOT_INTERESTED_ACTION_DECLINE;
			break;
		case ROUTED:
			_buttonAction = BUTTON_ACTION_ASSIGNMENT;
			_notInterestedAction = NOT_INTERESTED_ACTION_DECLINE;
			break;
		case REQUESTED:
			_notInterestedAction = NOT_INTERESTED_ACTION_WITHDRAW_REQUEST;
			break;
		case COUNTEROFFERED:
			_buttonAction = BUTTON_ACTION_VIEW_COUNTER;
			_notInterestedAction = NOT_INTERESTED_ACTION_DECLINE;
			break;
		default:
			return;
		}
	}

	private void buildStatusAssigned(Status status) {
		switch (status.getWorkorderSubstatus()) {
		case UNCONFIRMED: // green
			_buttonAction = BUTTON_ACTION_ASSIGNMENT;
			_notInterestedAction = NOT_INTERESTED_ACTION_CANCEL_ASSIGNMENT;
			break;
		case CONFIRMED: // white
			_buttonAction = BUTTON_ACTION_CHECKIN;
			break;
		case ONHOLD_UNACKNOWLEDGED: // orange
			_buttonAction = BUTTON_ACTION_ACKNOWLEDGE_HOLD;
			break;
		case ONHOLD_ACKNOWLEDGED: // gray
			_buttonAction = BUTTON_ACTION_NONE;
			break;
		case CHECKEDIN:
			_buttonAction = BUTTON_ACTION_CHECKOUT;
			break;
		case CHECKEDOUT:
			_buttonAction = BUTTON_ACTION_CHECKIN;
		default:
			return;
		}
	}

	private void buildStatusInProgress(Status status) {
		switch (status.getWorkorderSubstatus()) {
		case CHECKEDIN: // green
			_buttonAction = BUTTON_ACTION_CHECKOUT;
			break;
		case CHECKEDOUT: // white
			_buttonAction = BUTTON_ACTION_CHECKIN;
			break;
		case ONHOLD_UNACKNOWLEDGED: // orange
			_buttonAction = BUTTON_ACTION_ACKNOWLEDGE_HOLD;
			break;
		case ONHOLD_ACKNOWLEDGED: // gray
			// no action
			break;
		default:
			return;
		}
	}

	private void buildStatusCompleted(Status status) {
		if (status.getWorkorderSubstatus() == WorkorderSubstatus.APPROVED_PROCESSINGPAYMENT) {
			_buttonAction = BUTTON_ACTION_VIEW_PAYMENT;
		}
	}

	private void buildStatusCancelled(Status status) {
		if (status.getWorkorderSubstatus() == WorkorderSubstatus.CANCELLED_LATEFEEPROCESSING) {
			_buttonAction = BUTTON_ACTION_VIEW_PAYMENT;
		}
	}
}
