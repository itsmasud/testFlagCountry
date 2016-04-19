package com.fieldnation.data.workorder;

import android.os.Parcel;
import android.os.Parcelable;

import com.fieldnation.Log;
import com.fieldnation.json.JsonObject;
import com.fieldnation.json.Serializer;
import com.fieldnation.json.annotations.Json;
import com.fieldnation.utils.misc;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class Workorder implements Parcelable {

    @Json(name = "additionalExpenses")
    private Expense[] _additionalExpenses;
    @Json(name = "alertCount ")
    private Integer _alertCount;
    @Json(name = "bundleCount")
    private Integer _bundleCount;
    @Json(name = "bundleId")
    private Long _bundleId;
    @Json(name = "buyerRatingInfo")
    private BuyerRating _buyerRatingInfo;
    @Json(name = "keyEvents")
    private KeyEvents _keyEvents;
    @Json(name = "closingNotes")
    private String _closingNotes;
    @Json(name = "collectedSignature")
    private Boolean _collectedSignature;
    @Json(name = "companyId")
    private Integer _companyId;
    @Json(name = "companyName")
    private String _companyName;
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
    @Json(name = "discounts")
    private Discount[] _discounts;
    @Json(name = "documents")
    private Document[] _documents;
    @Json(name = "estimatedSchedule")
    private Schedule _estimatedSchedule;
    @Json(name = "expectedPayment")
    private ExpectedPayment _expectedPayment;
    @Json(name = "fullWorkDescription")
    private String _fullWorkDescription;
    @Json(name = "hasClosingNotes ")
    private Boolean _hasClosingNotes;
    @Json(name = "isCounter")
    private Boolean _isCounter;
    @Json(name = "isGpsRequired")
    private Boolean _isGpsRequired;
    @Json(name = "isRemoteWork")
    private Boolean _isRemoteWork;
    @Json(name = "isWorkPerformed")
    private Boolean _isWorkPerformed;
    @Json(name = "location")
    private Location _location;
    @Json(name = "loggedWork")
    private LoggedWork[] _loggedWork;
    @Json(name = "messageCount")
    private Integer _messageCount;
    @Json(name = "messages")
    private Integer _messages;
    @Json(name = "needsReadyToGo")
    private Boolean _needsReadyToGo;
    @Json(name = "pay")
    private Pay _pay;
    @Json(name = "paymentId")
    private Long _paymentId;
    @Json(name = "schedule")
    private Schedule _schedule;
    @Json(name = "shipmentTracking")
    private ShipmentTracking[] _shipmentTracking;
    @Json(name = "signatureList")
    private Signature[] _signatureList;
    @Json(name = "standardInstruction")
    private String _standardInstruction;
    @Json(name = "standardInstructions")
    private String _standardInstructions;
    @Json(name = "status")
    private Status _status;
    @Json(name = "tasks")
    private Task[] _tasks;
    @Json(name = "title")
    private String _title;
    @Json(name = "typeOfWork")
    private String _typeOfWork;
    @Json(name = "uploadSlots")
    private UploadSlot[] _uploadSlots;
    @Json(name = "w2")
    private Integer _w2;
    @Json(name = "workorderId")
    private Long _workorderId;
    @Json(name = "workorderManagerInfo")
    private User _workorderManagerInfo;

    public Workorder() {
    }

    public Expense[] getAdditionalExpenses() {
        return _additionalExpenses;
    }

    public Integer getAlertCount() {
        return _alertCount;
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

    public KeyEvents getKeyEvents() {
        return _keyEvents;
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

    public Discount[] getDiscounts() {
        return _discounts;
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

    public String getFullWorkDescription() {
        return _fullWorkDescription;
    }

    public Boolean hasClosingNotes() {
        return _hasClosingNotes;
    }

    public Boolean getIsCounter() {
        if (_isCounter != null)
            return _isCounter;

        return false;
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

    public Boolean getIsWorkPerformed() {
        return _isWorkPerformed;
    }

    public Location getLocation() {
        return _location;
    }

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

    public Pay getPay() {
        return _pay;
    }

    public Long getPaymentId() {
        return _paymentId;
    }

    public Schedule getSchedule() {
        return _schedule;
    }

    public ShipmentTracking[] getShipmentTracking() {
        return _shipmentTracking;
    }

    public Signature[] getSignatureList() {
        return _signatureList;
    }

    public String getStandardInstruction() {
        if (_standardInstructions == null)
            return _standardInstruction;

        return _standardInstructions;
    }

    public Task[] getTasks() {
        return _tasks;
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

    public Long getWorkorderId() {
        return _workorderId;
    }

    public User getWorkorderManagerInfo() {
        return _workorderManagerInfo;
    }

    public JsonObject toJson() {
        return toJson(this);
    }

    public static JsonObject toJson(Workorder workorder) {
        try {
            return Serializer.serializeObject(workorder);
        } catch (Exception ex) {
            Log.v(TAG, ex);
            return null;
        }
    }

    public static Workorder fromJson(JsonObject json) {
        try {
            Workorder wo = Serializer.unserializeObject(Workorder.class, json);
            wo.buildStatus();
            return wo;
        } catch (Exception ex) {
            Log.v(TAG, ex);
            return null;
        }
    }

    /*-*********************************************-*/
    /*-				Not Generated Code				-*/
    /*-*********************************************-*/
    private static final String TAG = "data.workorder.Workorder";
    public static final int BUTTON_ACTION_NONE = 0;
    public static final int BUTTON_ACTION_REQUEST = 1;
    public static final int BUTTON_ACTION_ACCEPT = 2;
    public static final int BUTTON_ACTION_CHECKIN = 3;
    public static final int BUTTON_ACTION_CHECKOUT = 4;
    public static final int BUTTON_ACTION_RECOGNIZE_HOLD = 5;
    public static final int BUTTON_ACTION_VIEW_COUNTER = 6;
    public static final int BUTTON_ACTION_VIEW_PAYMENT = 7;
    public static final int BUTTON_ACTION_WITHDRAW_REQUEST = 8;
    public static final int BUTTON_ACTION_READY_TO_GO = 9;
    public static final int BUTTON_ACTION_CONFIRM = 10;
    public static final int BUTTON_ACTION_MAP = 11;
    public static final int BUTTON_ACTION_NAVIGATE = 12;
    public static final int BUTTON_ACTION_REPORT_PROBLEM = 13;
    public static final int BUTTON_ACTION_CLOSING_NOTES = 14;
    public static final int BUTTON_ACTION_MARK_INCOMPLETE = 15;
    public static final int BUTTON_ACTION_REVIEW_UPDATE = 16;
    public static final int BUTTON_ACTION_REVIEW_IN = 17;
    public static final int BUTTON_ACTION_EDIT_REVIEW = 18;
    public static final int BUTTON_ACTION_VIEW_REVIEW = 19;

    private int _rightButtonAction = 0;
    private int _leftButtonAction = 0;

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
        WorkorderSubstatus substatus = getWorkorderSubstatus();
        return ((status == WorkorderStatus.ASSIGNED
                && substatus != WorkorderSubstatus.ONHOLD_ACKNOWLEDGED
                && substatus != WorkorderSubstatus.ONHOLD_UNACKNOWLEDGED)
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
            if (hasClosingNotes() != null && !hasClosingNotes()) {
                return false;
            }
            if (misc.isEmptyOrNull(getClosingNotes())) {
                return false;
            }
            if (getStatus().getWorkorderSubstatus() == WorkorderSubstatus.CHECKEDIN) {
                return false;
            }

            if (!areTasksComplete()) {
                return false;
            }

            return areCustomFieldsDone();

        }
        return false;
    }

    public boolean canIncomplete() {
        return getStatus().getWorkorderStatus() == WorkorderStatus.COMPLETED;
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

    public int getLeftButtonAction() {
        return _leftButtonAction;
    }

    public int getRightButtonAction() {
        return _rightButtonAction;
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
        _leftButtonAction = BUTTON_ACTION_NONE;
        _rightButtonAction = BUTTON_ACTION_NONE;

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
                _leftButtonAction = BUTTON_ACTION_MAP;
                _rightButtonAction = BUTTON_ACTION_REQUEST;
                break;
            case ROUTED:
                _leftButtonAction = BUTTON_ACTION_MAP;
                _rightButtonAction = BUTTON_ACTION_ACCEPT;
                break;
            case REQUESTED:
                _leftButtonAction = BUTTON_ACTION_MAP;
                _rightButtonAction = BUTTON_ACTION_WITHDRAW_REQUEST;
                break;
            case COUNTEROFFERED:
                _leftButtonAction = BUTTON_ACTION_MAP;
                _rightButtonAction = BUTTON_ACTION_WITHDRAW_REQUEST;
                break;
            default:
                _leftButtonAction = BUTTON_ACTION_NONE;
                _rightButtonAction = BUTTON_ACTION_NONE;
                break;
        }
    }

    private void buildStatusAssigned(Status status) {
        switch (status.getWorkorderSubstatus()) {
            case UNCONFIRMED: // green
                if (getNeedsReadyToGo()) {
                    _leftButtonAction = BUTTON_ACTION_REPORT_PROBLEM;
                    _rightButtonAction = BUTTON_ACTION_READY_TO_GO;
                } else {
                    _leftButtonAction = BUTTON_ACTION_MAP;
                    _rightButtonAction = BUTTON_ACTION_CONFIRM;
                }
                break;
            case CONFIRMED: // white
                if (getNeedsReadyToGo()) {
                    _leftButtonAction = BUTTON_ACTION_REPORT_PROBLEM;
                    _rightButtonAction = BUTTON_ACTION_READY_TO_GO;
                } else {
                    _leftButtonAction = BUTTON_ACTION_MAP;
                    _rightButtonAction = BUTTON_ACTION_CHECKIN;
                }
                break;
            case ONHOLD_UNACKNOWLEDGED: // orange
                _leftButtonAction = BUTTON_ACTION_MAP;
                _rightButtonAction = BUTTON_ACTION_RECOGNIZE_HOLD;
                break;
            case ONHOLD_ACKNOWLEDGED: // gray
                _leftButtonAction = BUTTON_ACTION_MAP;
                _rightButtonAction = BUTTON_ACTION_NONE;
                break;
            case CHECKEDIN:
                _leftButtonAction = BUTTON_ACTION_MAP;
                _rightButtonAction = BUTTON_ACTION_CHECKOUT;
                break;
            case CHECKEDOUT:
                _leftButtonAction = BUTTON_ACTION_MAP;
                _rightButtonAction = BUTTON_ACTION_CHECKIN;
                break;
            default:
                _leftButtonAction = BUTTON_ACTION_NONE;
                _rightButtonAction = BUTTON_ACTION_NONE;
                break;
        }
    }

    private void buildStatusInProgress(Status status) {
        switch (status.getWorkorderSubstatus()) {
            case CHECKEDIN: // green
                _leftButtonAction = BUTTON_ACTION_MAP;
                _rightButtonAction = BUTTON_ACTION_CHECKOUT;
                break;
            case CHECKEDOUT: // white
                _leftButtonAction = BUTTON_ACTION_MAP;
                _rightButtonAction = BUTTON_ACTION_CHECKIN;
                break;
            case ONHOLD_UNACKNOWLEDGED: // orange
                _leftButtonAction = BUTTON_ACTION_MAP;
                _rightButtonAction = BUTTON_ACTION_RECOGNIZE_HOLD;
                break;
            case ONHOLD_ACKNOWLEDGED: // gray
                _leftButtonAction = BUTTON_ACTION_MAP;
                _rightButtonAction = BUTTON_ACTION_NONE;
                break;
            default:
                _leftButtonAction = BUTTON_ACTION_NONE;
                _rightButtonAction = BUTTON_ACTION_NONE;
                break;
        }
    }

    private void buildStatusCompleted(Status status) {
        _leftButtonAction = BUTTON_ACTION_NONE;
        switch (status.getWorkorderSubstatus()) {
            case PENDINGREVIEW:
                _rightButtonAction = BUTTON_ACTION_MARK_INCOMPLETE;
                break;
            case INREVIEW:
                _rightButtonAction = BUTTON_ACTION_MARK_INCOMPLETE;
                break;
            case APPROVED_PROCESSINGPAYMENT:
                _rightButtonAction = BUTTON_ACTION_NONE;
                break;
            case PAID:
                _rightButtonAction = BUTTON_ACTION_VIEW_PAYMENT;
                break;
            default:
                _leftButtonAction = BUTTON_ACTION_NONE;
                _rightButtonAction = BUTTON_ACTION_NONE;
                break;
        }
    }

    private void buildStatusCanceled(Status status) {
        _leftButtonAction = BUTTON_ACTION_NONE;
        switch (status.getWorkorderSubstatus()) {
            case CANCELED:
                _rightButtonAction = BUTTON_ACTION_NONE;
                break;
            case CANCELED_LATEFEEPAID:
                _rightButtonAction = BUTTON_ACTION_VIEW_PAYMENT;
                break;
            case CANCELED_LATEFEEPROCESSING:
                _rightButtonAction = BUTTON_ACTION_VIEW_PAYMENT;
                break;
            default:
                _leftButtonAction = BUTTON_ACTION_NONE;
                _rightButtonAction = BUTTON_ACTION_NONE;
                break;
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
                Log.v(TAG, ex);
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
