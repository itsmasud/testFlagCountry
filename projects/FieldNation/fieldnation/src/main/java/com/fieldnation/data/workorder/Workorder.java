package com.fieldnation.data.workorder;

import android.os.Parcel;
import android.os.Parcelable;

import com.fieldnation.Log;
import com.fieldnation.R;
import com.fieldnation.json.JsonObject;
import com.fieldnation.json.Serializer;
import com.fieldnation.json.annotations.Json;
import com.fieldnation.utils.misc;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class Workorder implements Parcelable {

    @Json(name = "acl")
    private String[] _acl;
    @Json(name = "additionalExpenses")
    private Expense[] _additionalExpenses;
    @Json(name = "alertCount ")
    private Integer _alertCount;
    @Json(name = "alerts")
    private Integer _alerts;
    @Json(name = "approximatePaymentDate")
    private String _approximatePaymentDate;
    @Json(name = "bundleCount")
    private Integer _bundleCount;
    @Json(name = "bundleId")
    private Long _bundleId;
    @Json(name = "buyerRatingInfo")
    private BuyerRating _buyerRatingInfo;
    @Json(name = "canEditFieldsForProviderToEnter")
    private Boolean _canEditFieldsForProviderToEnter;
    @Json(name = "canPerformPreassignmentAction")
    private Boolean _canPerformPreassignmentAction;
    @Json(name = "canRequestPayForThisWo")
    private Boolean _canRequestPayForThisWo;
    @Json(name = "canViewServicePayRateInfo")
    private Boolean _canViewServicePayRateInfo;
    @Json(name = "cancelFee")
    private Double _cancelFee;
    @Json(name = "checkInOutInfo")
    private CheckInOutInfo _checkInOutInfo;
    @Json(name = "closingNotes")
    private String _closingNotes;
    @Json(name = "collectedSignature")
    private Boolean _collectedSignature;
    @Json(name = "companyId")
    private Integer _companyId;
    @Json(name = "companyName")
    private String _companyName;
    @Json(name = "completionSteps")
    private CompletionStep[] _completionSteps;
    @Json(name = "confidentialInformation")
    private String _confidentialInformation;
    @Json(name = "counterOfferInfo")
    private CounterOfferInfo _counterOfferInfo;
    @Json(name = "customDisplayFields")
    private CustomDisplayFields[] _customDisplayFields;
    @Json(name = "customFields")
    private CustomField[] _customFields;
    @Json(name = "customerPoliciesProcedures")
    private String _customerPoliciesProcedures;
    @Json(name = "days_since_approved_or_cancelled")
    private Integer _daysSinceApprovedOrCanceled;
    @Json(name = "declinedWo")
    private Integer _declinedWo;
    //    @Json(name = "deliverables")
//    private Deliverable[] _deliverables;
    @Json(name = "discounts")
    private Discount[] _discounts;
    @Json(name = "displayCounterOffer")
    private Integer _displayCounterOffer;
    @Json(name = "distance")
    private Double _distance;
    @Json(name = "documents")
    private Document[] _documents;
    @Json(name = "estimatedSchedule")
    private Schedule _estimatedSchedule;
    @Json(name = "expectedPayment")
    private ExpectedPayment _expectedPayment;
    @Json(name = "financeId")
    private Integer _financeId;
    @Json(name = "fullWorkDescription")
    private String _fullWorkDescription;
    @Json(name = "hasFund")
    private Boolean _hasFund;
    @Json(name = "hasMarketplaceAccess")
    private Boolean _hasMarketplaceAccess;
    @Json(name = "identifier")
    private String _identifier;
    @Json(name = "increaseRequestInfo")
    private IncreaseRequestInfo _increaseRequestInfo;
    @Json(name = "industry")
    private String _industry;
    @Json(name = "isAssignedToWorkorder")
    private Boolean _isAssignedToWorkorder;
    @Json(name = "isCounter")
    private Boolean _isCounter;
    @Json(name = "isDeliverablesUploaded")
    private Boolean _isDeliverablesUploaded;
    @Json(name = "isExemptFromFee")
    private Boolean _isExemptFromFee;
    @Json(name = "isGpsRequired")
    private Boolean _isGpsRequired;
    @Json(name = "isRemoteWork")
    private Boolean _isRemoteWork;
    @Json(name = "isRequest")
    private Boolean _isRequest;
    @Json(name = "isRequestedByProvider")
    private Boolean _isRequestedByProvider;
    @Json(name = "isWoOnHold")
    private Boolean _isWoOnHold;
    @Json(name = "isWorkPerformed")
    private Boolean _isWorkPerformed;
    //	@Json(name="label")
//	private Label[] _label;
    @Json(name = "location")
    private Location _location;
    //	@Json(name="locationContacts")
//	private LocationContacts[] _locationContacts;
    @Json(name = "loggedWork")
    private LoggedWork[] _loggedWork;
    @Json(name = "messageCount")
    private Integer _messageCount;
    @Json(name = "messages")
    private Integer _messages;
    @Json(name = "needsReadyToGo")
    private Boolean _needsReadyToGo;
    @Json(name = "onlyApprovedAdditionalExpenses")
    private Expense[] _onlyApprovedAdditionalExpenses;
    @Json(name = "onlyDeniedAdditionalExpenses")
    private Expense[] _onlyDeniedAdditionalExpenses;
    @Json(name = "onlyNewAdditionalExpenses")
    private Expense[] _onlyNewAdditionalExpenses;
    @Json(name = "paidDate")
    private String _paidDate;
    @Json(name = "pay")
    private Pay _pay;
    @Json(name = "paymentId")
    private Long _paymentId;
    //    @Json(name = "provider")
//    private Provider _provider;
    @Json(name = "schedule")
    private Schedule _schedule;
    @Json(name = "shipmentTracking")
    private ShipmentTracking[] _shipmentTracking;
    @Json(name = "showProviderWasLatePrompt")
    private Boolean _showProviderWasLatePrompt;
    @Json(name = "signatureList")
    private Signature[] _signatureList;
    @Json(name = "skillsets")
    private Skillset[] _skillsets;
    @Json(name = "standardInstruction")
    private String _standardInstruction;
    @Json(name = "standardInstructions")
    private String _standardInstructions;
    @Json(name = "status")
    private Status _status;
    @Json(name = "statusId")
    private Integer _statusId;
    @Json(name = "taskEnabled")
    private Integer _taskEnabled;
    @Json(name = "tasks")
    private Task[] _tasks;
    @Json(name = "techCanPrint")
    private Boolean _techCanPrint;
    @Json(name = "time_since_published")
    private Integer _timeSincePublished;
    @Json(name = "timezone")
    private String _timezone;
    @Json(name = "timezoneIdentifier")
    private String _timezoneIdentifier;
    @Json(name = "title")
    private String _title;
    @Json(name = "typeOfWork")
    private String _typeOfWork;
    @Json(name = "uploadSlots")
    private UploadSlot[] _uploadSlots;
    @Json(name = "w2")
    private Integer _w2;
    @Json(name = "workorderBonusInfo")
    private WorkorderBonusInfo[] _workorderBonusInfo;
    //@Json(name="workorderContacts")
    //private Object[] _workorderContacts;
    @Json(name = "workorderId")
    private Long _workorderId;
    @Json(name = "workorderManagerInfo")
    private User _workorderManagerInfo;
//    @Json(name = "workorderPenaltyInfo")
//    private JsonObject[] _workorderPenaltyInfo;

    public Workorder() {
    }

    public Expense[] getAdditionalExpenses() {
        return _additionalExpenses;
    }

    public Integer getAlertCount() {
        return _alertCount;
    }

    public Integer getAlerts() {
        return _alerts;
    }

    public String getApproximatePaymentDate() {
        return _approximatePaymentDate;
    }

    public Integer getBundleCount() {
        return _bundleCount;
    }

    public Long getBundleId() {
        return _bundleId;
    }

    public BuyerRating getBuyerRatingInfo() {
        return _buyerRatingInfo;
    }

    public Boolean getCanEditFieldsForProviderToEnter() {
        return _canEditFieldsForProviderToEnter;
    }

    public Boolean getCanPerformPreassignmentAction() {
        return _canPerformPreassignmentAction;
    }

    public Boolean getCanRequestPayForThisWo() {
        return _canRequestPayForThisWo;
    }

    public Boolean getCanViewServicePayRateInfo() {
        if (_canViewServicePayRateInfo == null)
            return true;

        return _canViewServicePayRateInfo;
    }

    public Double getCancelFee() {
        return _cancelFee;
    }

    public CheckInOutInfo getCheckInOutInfo() {
        return _checkInOutInfo;
    }

    public String getClosingNotes() {
        return _closingNotes;
    }

    public Boolean isSignatureCollected() {
        if (_collectedSignature == null)
            return false;

        return _collectedSignature;
    }

    public Integer getCompanyId() {
        return _companyId;
    }

    public String getCompanyName() {
        return _companyName;
    }

    public CompletionStep[] getCompletionSteps() {
        return _completionSteps;
    }

    public String getConfidentialInformation() {
        return _confidentialInformation;
    }

    public CounterOfferInfo getCounterOfferInfo() {
        return _counterOfferInfo;
    }

    public CustomDisplayFields[] getCustomDisplayFields() {
        return _customDisplayFields;
    }

    public CustomField[] getCustomFields() {
        return _customFields;
    }

    public String getCustomerPoliciesProcedures() {
        return _customerPoliciesProcedures;
    }

    public Integer getDaysSinceApprovedOrCanceled() {
        return _daysSinceApprovedOrCanceled;
    }

    public Integer getDeclinedWo() {
        return _declinedWo;
    }

//    public Deliverable[] getDeliverables() {
//        return _deliverables;
//    }

    public Discount[] getDiscounts() {
        return _discounts;
    }

    public Integer getDisplayCounterOffer() {
        return _displayCounterOffer;
    }

    public Double getDistance() {
        return _distance;
    }

    public Document[] getDocuments() {
        return _documents;
    }

    public Schedule getEstimatedSchedule() {
        return _estimatedSchedule;
    }

    public ExpectedPayment getExpectedPayment() {
        return _expectedPayment;
    }

    public Integer getFinanceId() {
        return _financeId;
    }

    public String getFullWorkDescription() {
        return _fullWorkDescription;
    }

    public Boolean getHasFund() {
        return _hasFund;
    }

    public Boolean getHasMarketplaceAccess() {
        return _hasMarketplaceAccess;
    }

    public String getIdentifier() {
        return _identifier;
    }

    public IncreaseRequestInfo getIncreaseRequestInfo() {
        return _increaseRequestInfo;
    }

    public String getIndustry() {
        return _industry;
    }

    public Boolean getIsAssignedToWorkorder() {
        return _isAssignedToWorkorder;
    }

    public Boolean getIsCounter() {
        if (_isCounter != null)
            return _isCounter;

        return false;
    }

    public Boolean getIsDeliverablesUploaded() {
        return _isDeliverablesUploaded;
    }

    public Boolean getIsExemptFromFee() {
        return _isExemptFromFee;
    }

    public Boolean getIsGpsRequired() {
        if (_isGpsRequired != null)
            return _isGpsRequired;

        return false;
    }

    public Boolean getIsRemoteWork() {
        if (_isRemoteWork != null)
            return _isRemoteWork;

        return false;
    }

    public Boolean getIsRequest() {
        return _isRequest;
    }

    public Boolean getIsRequestedByProvider() {
        return _isRequestedByProvider;
    }

    public Boolean getIsWoOnHold() {
        return _isWoOnHold;
    }

    public Boolean getIsWorkPerformed() {
        return _isWorkPerformed;
    }

//	public Label[] getLabel(){
//		return _label;
//	}

    public Location getLocation() {
        return _location;
    }

//	public LocationContacts[] getLocationContacts(){
//		return _locationContacts;
//	}

    public LoggedWork[] getLoggedWork() {
        return _loggedWork;
    }

    public Integer getMessageCount() {
        return _messageCount;
    }

    public Integer getMessages() {
        return _messages;
    }

    public Boolean getNeedsReadyToGo() {
        if (_needsReadyToGo != null)
            return _needsReadyToGo;

        return false;
    }

    public Expense[] getOnlyApprovedAdditionalExpenses() {
        return _onlyApprovedAdditionalExpenses;
    }

    public Expense[] getOnlyDeniedAdditionalExpenses() {
        return _onlyDeniedAdditionalExpenses;
    }

    public Expense[] getOnlyNewAdditionalExpenses() {
        return _onlyNewAdditionalExpenses;
    }

    public String getPaidDate() {
        return _paidDate;
    }

    public Pay getPay() {
        return _pay;
//        return null;
    }

    public Long getPaymentId() {
        return _paymentId;
    }

//    public Provider getProvider() {
//        return _provider;
//    }

    public Schedule getSchedule() {
        return _schedule;
    }

    public ShipmentTracking[] getShipmentTracking() {
        return _shipmentTracking;
    }

    public Boolean getShowProviderWasLatePrompt() {
        return _showProviderWasLatePrompt;
    }

    public Signature[] getSignatureList() {
        return _signatureList;
    }

    public Skillset[] getSkillsets() {
        return _skillsets;
    }

    public String getStandardInstruction() {
        return _standardInstruction;
    }

    public String getStandardInstructions() {
        return _standardInstructions;
    }

//	public Status getStatus(){
//		return _status;
//	}

    public Integer getStatusId() {
        return _statusId;
    }

    public Integer getTaskEnabled() {
        return _taskEnabled;
    }

    public Task[] getTasks() {
        return _tasks;
    }

    public Boolean getTechCanPrint() {
        return _techCanPrint;
    }

    public Integer getTimeSincePublished() {
        return _timeSincePublished;
    }

    public String getTimezone() {
        return _timezone;
    }

    public String getTimezoneIdentifier() {
        return _timezoneIdentifier;
    }

    public String getTitle() {
        return _title;
    }

    public String getTypeOfWork() {
        return _typeOfWork;
    }

    public UploadSlot[] getUploadSlots() {
        return _uploadSlots;
    }

    public Integer getW2() {
        return _w2;
    }

    public WorkorderBonusInfo[] getWorkorderBonusInfo() {
        return _workorderBonusInfo;
    }

    public Long getWorkorderId() {
        return _workorderId;
    }

    public User getWorkorderManagerInfo() {
        return _workorderManagerInfo;
    }

//	public WorkorderPenaltyInfo[] getWorkorderPenaltyInfo(){
//        return _workorderPenaltyInfo;
//    }

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
    private static final String TAG = "data.workorder.Workorder";
    public static final int BUTTON_ACTION_NONE = 0;
    public static final int BUTTON_ACTION_REQUEST = 1;
    public static final int BUTTON_ACTION_ASSIGNMENT = 2;
    public static final int BUTTON_ACTION_CHECKIN = 3;
    public static final int BUTTON_ACTION_CHECKOUT = 4;
    public static final int BUTTON_ACTION_ACKNOWLEDGE_HOLD = 5;
    public static final int BUTTON_ACTION_VIEW_COUNTER = 6;
    public static final int BUTTON_ACTION_VIEW_PAYMENT = 7;
    public static final int BUTTON_ACTION_WITHDRAW_REQUEST = 8;
    public static final int BUTTON_ACTION_READY_TO_GO = 9;

    public static final int NOT_INTERESTED_ACTION_NONE = 0;
    public static final int NOT_INTERESTED_ACTION_DECLINE = 101;
    public static final int NOT_INTERESTED_ACTION_WITHDRAW_REQUEST = 102;
    public static final int NOT_INTERESTED_ACTION_CANCEL_ASSIGNMENT = 103;

    // status colors lookuptable
    private static final int[] _STATUS_LOOKUP_TABLE = {
            R.drawable.card_status_white, R.drawable.card_status_orange,
            R.drawable.card_status_green, R.drawable.card_status_gray};

    private static final int[] _STATUS_TEXT_TABLE = {
            R.color.fn_dark_text, R.color.fn_white_text,
            R.color.fn_white_text, R.color.fn_white_text};

    private static final int[] _STATUS_BUTTON_FG = {R.color.fn_dark_button_text,
            R.color.fn_white_text, R.color.fn_white_text, R.color.fn_white_text};

    private static final int[] _STATUS_BUTTON_BG = {R.drawable.btn_white,
            R.drawable.btn_orange, R.drawable.btn_green, R.drawable.btn_white};

    private int _buttonAction = 0;
    private int _notInterestedAction = 0;
    // private Set<Integer> _labelIds = new HashSet<Integer>();

    private final Set<Listener> _listeners = new HashSet<>();

    public void addListener(Listener listener) {
        _listeners.add(listener);
    }

    public boolean areTasksComplete() {
        Task[] tasks = getTasks();
        if (tasks == null || tasks.length == 0)
            return true;

        boolean workToDo = false;
        for (int i = 0; i < tasks.length; i++) {
            if (!tasks[i].getCompleted()) {
                workToDo = true;
                break;
            }
        }

        return !workToDo;
    }

    public boolean areCustomFieldsDone() {
        CustomField[] fields = getCustomFields();

        if (fields == null || fields.length == 0)
            return true;

        boolean fieldsToDo = false;
        for (int i = 0; i < fields.length; i++) {
            if (fields[i].getRequired() && misc.isEmptyOrNull(fields[i].getValue())) {
                fieldsToDo = true;
                break;
            }
        }

        return !fieldsToDo;
    }

    public boolean canRequestPayIncrease() {
        WorkorderStatus status = getStatus().getWorkorderStatus();
        return (status == WorkorderStatus.ASSIGNED
                || status == WorkorderStatus.INPROGRESS)
                && getPay() != null
                && !getPay().hidePay();
    }

    public boolean canCounterOffer() {
        return getStatus().getWorkorderStatus() == WorkorderStatus.AVAILABLE
                && getStatus().getWorkorderSubstatus() != WorkorderSubstatus.REQUESTED
                && !isBundle()
                && getPay() != null
                && !getPay().hidePay();
    }

    public boolean canComplete() {
        if (getStatus().getWorkorderStatus() == WorkorderStatus.AVAILABLE || getStatus().getWorkorderStatus() == WorkorderStatus.INPROGRESS) {
            if (misc.isEmptyOrNull(getClosingNotes())) {
                return false;
            }
            if (getStatus().getWorkorderSubstatus() == WorkorderSubstatus.CHECKEDIN) {
                return false;
            }

            if (!areTasksComplete()) {
                return false;
            }

            if (!areCustomFieldsDone()) {
                return false;
            }

            return true;
        }
        return false;
    }

    public boolean canChangeExpenses() {
        WorkorderStatus status = getWorkorderStatus();
        WorkorderSubstatus substatus = getWorkorderSubstatus();

        if (status == WorkorderStatus.ASSIGNED
                || status == WorkorderStatus.INPROGRESS) {
            if (substatus == WorkorderSubstatus.CHECKEDIN
                    || substatus == WorkorderSubstatus.CONFIRMED
                    || substatus == WorkorderSubstatus.CHECKEDOUT) {
                return true;
            }
        }
        return false;
    }

    public boolean canChangeDiscounts() {
        WorkorderStatus status = getWorkorderStatus();
        WorkorderSubstatus substatus = getWorkorderSubstatus();

        if (status == WorkorderStatus.ASSIGNED || status == WorkorderStatus.INPROGRESS) {
            if (substatus == WorkorderSubstatus.CHECKEDIN
                    || substatus == WorkorderSubstatus.CONFIRMED
                    || substatus == WorkorderSubstatus.CHECKEDOUT) {
                return true;
            }
        }
        return false;
    }

    public boolean canChangeShipments() {
        return canChangeExpenses();
    }


    public boolean canRequest() {
        if (getStatus().getWorkorderStatus() == WorkorderStatus.AVAILABLE) {
            WorkorderSubstatus substatus = getWorkorderSubstatus();

            return !canAcceptWork()
                    && substatus != WorkorderSubstatus.REQUESTED
                    && substatus != WorkorderSubstatus.COUNTEROFFERED;
        }
        return false;
    }

    public boolean canWithdrawRequest() {
        return getStatus().getWorkorderSubstatus() == WorkorderSubstatus.REQUESTED;
    }

    public boolean canAcceptWork() {
        return getWorkorderStatus() == WorkorderStatus.AVAILABLE
                && getWorkorderSubstatus() == WorkorderSubstatus.ROUTED;
    }

    public boolean canDeclineWork() {
        return getWorkorderStatus() == WorkorderStatus.AVAILABLE
                || getWorkorderSubstatus() == WorkorderSubstatus.ROUTED;
    }

    public boolean canChangeDeliverables() {
        return canViewDeliverables() && getUploadSlots() != null && getUploadSlots().length > 0;
    }

    public boolean canViewConfidentialInfo() {
        return getWorkorderStatus() == WorkorderStatus.ASSIGNED
                || getWorkorderStatus() == WorkorderStatus.INPROGRESS;
    }

    public boolean canChangeCustomFields() {
        return (getWorkorderStatus() == WorkorderStatus.ASSIGNED
                || getWorkorderStatus() == WorkorderStatus.INPROGRESS)
                && (getWorkorderSubstatus() == WorkorderSubstatus.CHECKEDIN
                || getWorkorderSubstatus() == WorkorderSubstatus.CHECKEDOUT
                || getWorkorderSubstatus() == WorkorderSubstatus.CONFIRMED);
    }

    public boolean canAcceptSignature() {
        WorkorderStatus status = getWorkorderStatus();
        WorkorderSubstatus substatus = getWorkorderSubstatus();
        return ((status == WorkorderStatus.ASSIGNED && getWorkorderSubstatus() != WorkorderSubstatus.UNCONFIRMED)
                || status == WorkorderStatus.INPROGRESS)
                && (substatus != WorkorderSubstatus.ONHOLD_ACKNOWLEDGED && substatus != WorkorderSubstatus.ONHOLD_UNACKNOWLEDGED);
    }

    public boolean canChangeClosingNotes() {
        return (getWorkorderStatus() == WorkorderStatus.ASSIGNED || getWorkorderStatus() == WorkorderStatus.INPROGRESS)
                && (getWorkorderSubstatus() == WorkorderSubstatus.CHECKEDIN || getWorkorderSubstatus() == WorkorderSubstatus.CHECKEDOUT
                || getWorkorderSubstatus() == WorkorderSubstatus.CONFIRMED);
    }

    public boolean canViewDeliverables() {
        return (getWorkorderStatus() == WorkorderStatus.ASSIGNED || getWorkorderStatus() == WorkorderStatus.INPROGRESS)
                && (getWorkorderSubstatus() == WorkorderSubstatus.CHECKEDIN || getWorkorderSubstatus() == WorkorderSubstatus.CHECKEDOUT
                || getWorkorderSubstatus() == WorkorderSubstatus.CONFIRMED);
    }

    public boolean canModifyTasks() {
        return (getWorkorderStatus() == WorkorderStatus.ASSIGNED || getWorkorderStatus() == WorkorderStatus.INPROGRESS)
                && (getWorkorderSubstatus() == WorkorderSubstatus.CHECKEDIN || getWorkorderSubstatus() == WorkorderSubstatus.CHECKEDOUT
                || getWorkorderSubstatus() == WorkorderSubstatus.CONFIRMED);
    }

    public boolean canModifyTimeLog() {
        return (getWorkorderStatus() == WorkorderStatus.ASSIGNED || getWorkorderStatus() == WorkorderStatus.INPROGRESS)
                && (getWorkorderSubstatus() == WorkorderSubstatus.CHECKEDIN || getWorkorderSubstatus() == WorkorderSubstatus.CHECKEDOUT
                || getWorkorderSubstatus() == WorkorderSubstatus.CONFIRMED);
    }

    public boolean canCheckout() {
        return getWorkorderSubstatus() == WorkorderSubstatus.CHECKEDIN;
    }

    public boolean canCheckin() {
        return getWorkorderSubstatus() == WorkorderSubstatus.CHECKEDOUT || getWorkorderSubstatus() == WorkorderSubstatus.CONFIRMED;
    }

    public boolean canAckHold() {
        return getWorkorderSubstatus() == WorkorderSubstatus.ONHOLD_UNACKNOWLEDGED;
    }

    public boolean canConfirm() {
        return getWorkorderSubstatus() == WorkorderSubstatus.UNCONFIRMED;
    }


    public void dispatchOnChange() {
        Iterator<Listener> iter = _listeners.iterator();
        while (iter.hasNext()) {
            iter.next().onChange(this);
        }
    }

    public WorkorderStatus getWorkorderStatus() {
        return getStatus().getWorkorderStatus();
    }

    public WorkorderSubstatus getWorkorderSubstatus() {
        return getStatus().getWorkorderSubstatus();
    }

    public int getButtonAction() {
        return _buttonAction;
    }

    public int getNotInterestedAction() {
        return _notInterestedAction;
    }

    public Status getStatus() {
        if (_status == null) {
            String data = "Status is null: " + _workorderId;
            Log.v(TAG, data);
        } else if (_status.getWorkorderStatus() == null) {
            String data = "Could not get status: " + _workorderId + "\r\n";
            data += _status.toJson().display();

            Log.v(TAG, data);
        } else if (_status.getWorkorderSubstatus() == null) {
            String data = "Could not get substatus: " + _workorderId + "\r\n";
            data += _status.toJson().display();
            Log.v(TAG, data);
        }

        return _status;
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

    public boolean isBundle() {
        return getBundleId() != null && getBundleId() > 0;
    }

    public void removeListener(Listener listener) {
        _listeners.remove(listener);
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
            case CANCELED:
                buildStatusCanceled(status);
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
                Log.v(TAG, "Unknown Status (" + _workorderId + "): "
                        + status.toJson().toString());
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
                _buttonAction = BUTTON_ACTION_WITHDRAW_REQUEST;
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
                if (getNeedsReadyToGo())
                    _buttonAction = BUTTON_ACTION_READY_TO_GO;
                else
                    _buttonAction = BUTTON_ACTION_ASSIGNMENT;

                _notInterestedAction = NOT_INTERESTED_ACTION_CANCEL_ASSIGNMENT;
                break;
            case CONFIRMED: // white
                if (getNeedsReadyToGo())
                    _buttonAction = BUTTON_ACTION_READY_TO_GO;
                else
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

    private void buildStatusCanceled(Status status) {
        if (status.getWorkorderSubstatus() == WorkorderSubstatus.CANCELED_LATEFEEPROCESSING) {
            _buttonAction = BUTTON_ACTION_VIEW_PAYMENT;
        }
    }

    public interface Listener {
        void onChange(Workorder workorder);
    }

    /*-*********************************************-*/
    /*-			Parcelable Implementation			-*/
    /*-*********************************************-*/
    public static final Parcelable.Creator<Workorder> CREATOR = new Parcelable.Creator<Workorder>() {

        @Override
        public Workorder createFromParcel(Parcel source) {
            try {
                return Workorder.fromJson((JsonObject) source.readParcelable(JsonObject.class.getClassLoader()));
            } catch (Exception ex) {
                ex.printStackTrace();
                return null;
            }
        }

        @Override
        public Workorder[] newArray(int size) {
            return new Workorder[size];
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

}
