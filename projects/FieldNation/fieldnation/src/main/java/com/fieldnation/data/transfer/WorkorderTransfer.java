package com.fieldnation.data.transfer;

import android.os.Parcel;
import android.os.Parcelable;

import com.fieldnation.json.JsonObject;
import com.fieldnation.json.Serializer;
import com.fieldnation.json.annotations.Json;

/**
 * Created by Michael on 4/6/2015.
 */
public class WorkorderTransfer implements Parcelable {

    // bools
    @Json(name = "checkin")
    private Boolean _checkin;
    @Json(name = "checkout")
    private Boolean _checkout;
    @Json(name = "complete")
    private Boolean _complete;
    @Json(name = "ackHold")
    private Boolean _ackHold;
    @Json(name = "counterOffer")
    private Boolean _counterOffer;
    @Json(name = "request")
    private Boolean _request;
    @Json(name = "confirmAssignment")
    private Boolean _confirmAssignment;

    // objects
    @Json(name = "addSignatureJson")
    private String[] _addSignatureJson;
    @Json(name = "completeTask")
    private String[] _completeTask;
    @Json(name = "deleteDeliverable")
    private String[] _deleteDeliverable;
    @Json(name = "uploadDeliverable")
    private String[] _uploadDeliverable;
    @Json(name = "closingNotes")
    private String _closingNotes;

    public WorkorderTransfer() {
    }

    public boolean isCheckingIn() {
        if (_checkin != null)
            return _checkin;

        return false;
    }

    public static String makeCheckinTransfer() {
        return "{_transfer:{\"checkin\":true}}";
    }

    public boolean isCheckingOut() {
        if (_checkout != null)
            return _checkout;

        return false;
    }

    public static String makeCheckoutTransfer() {
        return "{_transfer:{\"checkout\":true}}";
    }

    public boolean isMarkingComplete() {
        if (_complete != null)
            return _complete;
        return false;
    }

    public static String makeMarkCompleteTransfer() {
        return "{_transfer:{\"complete\":true}}";
    }

    public boolean isAcknowledgingHold() {
        if (_ackHold != null)
            return _ackHold;
        return false;
    }

    public static String makeAckHoldTransfer() {
        return "{_transfer:{\"ackHold\":true}}";
    }

    public boolean isCounterOffering() {
        if (_counterOffer != null)
            return _counterOffer;
        return false;
    }

    public static String makeCounterOfferTransfer() {
        return "{_transfer:{\"counterOffer\":true}}";
    }

    public boolean isRequesting() {
        if (_request != null)
            return _request;
        return false;
    }

    public static String makeRequestTransfer() {
        return "{_transfer:{\"request\":true}}";
    }

    public boolean isConfirmingAssignment() {
        if (_confirmAssignment != null)
            return _confirmAssignment;
        return false;
    }

    public static String makeConfirmAssignment() {
        return "{_transfer:{\"confirmAssignment\":true}}";
    }


    // objects
    public boolean isAddingSignature() {
        return _addSignatureJson != null && _addSignatureJson.length > 0;
    }

    public String[] getAddSignatureJson() {
        return _addSignatureJson;
    }

    public static String makeAddSignatureTransfer(String name) {
        try {
            return new JsonObject("_transfer.addSignature[0].name", name).toString();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public boolean isCompletingTask() {
        return _completeTask != null && _completeTask.length > 0;
    }

    public String[] getCompletingTask() {
        return _completeTask;
    }

    public static String makeCompletingTaskTransfer(String type, long taskId, String data) {
        try {
            return new JsonObject()
                    .put("_transfer.completeTask[0].type", type)
                    .put("_transfer.completeTask[0].taskId", taskId)
                    .put("_transfer.completeTask[0].data", data)
                    .toString();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public boolean isDeletingDeliverable() {
        return _deleteDeliverable != null && _deleteDeliverable.length > 0;
    }

    public String[] getDeleteDeliverable() {
        return _deleteDeliverable;
    }

    public static String makeDeleteDeliverable(long workorderUploadId, String filename) {
        try {
            return new JsonObject()
                    .put("_transfer.deleteDeliverable[0].workorderUploadId", workorderUploadId)
                    .put("_transfer.deleteDeliverable[0].filename", filename)
                    .toString();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public boolean isUploadingDeliverable() {
        return _uploadDeliverable != null && _uploadDeliverable.length > 0;
    }

    public String[] getUploadingDeliverables() {
        return _uploadDeliverable;
    }

    public static String makeUploadDeliverable(long slotId, String filename) {
        try {
            return new JsonObject()
                    .put("_transfer.uploadDeliverable[0].slotId", slotId)
                    .put("_transfer.uploadDeliverable[0].filename", filename)
                    .toString();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public String getClosingNotes() {
        return _closingNotes;
    }

    public static String makeClosingNotesTransfer(String closingNotes) {
        try {
            return new JsonObject()
                    .put("_transfer.closingNotes", closingNotes)
                    .toString();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    // JSON
    public JsonObject toJson() {
        return toJson(this);
    }

    public static JsonObject toJson(WorkorderTransfer transfer) {
        try {
            return Serializer.serializeObject(transfer);
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public static WorkorderTransfer fromJson(JsonObject json) {
        try {
            return Serializer.unserializeObject(WorkorderTransfer.class, json);
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    /*-*********************************************-*/
    /*-			Parcelable Implementation			-*/
    /*-*********************************************-*/
    public static final Creator<WorkorderTransfer> CREATOR = new Creator<WorkorderTransfer>() {
        @Override
        public WorkorderTransfer createFromParcel(Parcel source) {
            try {
                return WorkorderTransfer.fromJson(new JsonObject(source.readString()));
            } catch (Exception ex) {
                ex.printStackTrace();
                return null;
            }
        }

        @Override
        public WorkorderTransfer[] newArray(int size) {
            return new WorkorderTransfer[0];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(toJson().toString());
    }
}
