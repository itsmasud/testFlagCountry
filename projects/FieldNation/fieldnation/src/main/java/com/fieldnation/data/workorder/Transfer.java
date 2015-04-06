package com.fieldnation.data.workorder;

import android.os.Parcel;
import android.os.Parcelable;

import com.fieldnation.json.JsonObject;
import com.fieldnation.json.Serializer;
import com.fieldnation.json.annotations.Json;

/**
 * Created by Michael on 4/6/2015.
 */
public class Transfer implements Parcelable {
    @Json(name = "checkin")
    private String _checkin;
    @Json(name = "checkout")
    private String _checkout;
    @Json(name = "addSignatureJson")
    private String _addSignatureJson;
    @Json(name = "completeSignatureTaskJson")
    private JsonObject _completeSignatureTaskJson;
    @Json(name = "complete")
    private String _complete;
    @Json(name = "ackHold")
    private String _ackHold;
    @Json(name = "counterOffer")
    private String _counterOffer;
    @Json(name = "request")
    private String _request;
    @Json(name = "confirmAssignment")
    private String _confirmAssignment;
    @Json(name = "deleteDeliverable")
    private JsonObject _deleteDeliverable;


    public JsonObject toJson() {
        return toJson(this);
    }

    public static JsonObject toJson(Transfer transfer) {
        try {
            return Serializer.serializeObject(transfer);
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public static Transfer fromJson(JsonObject json) {
        try {
            return Serializer.unserializeObject(Transfer.class, json);
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    /*-*********************************************-*/
    /*-			Parcelable Implementation			-*/
    /*-*********************************************-*/
    public static final Creator<Transfer> CREATOR = new Creator<Transfer>() {
        @Override
        public Transfer createFromParcel(Parcel source) {
            try {
                return Transfer.fromJson(new JsonObject(source.readString()));
            } catch (Exception ex) {
                ex.printStackTrace();
                return null;
            }
        }

        @Override
        public Transfer[] newArray(int size) {
            return new Transfer[0];
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
