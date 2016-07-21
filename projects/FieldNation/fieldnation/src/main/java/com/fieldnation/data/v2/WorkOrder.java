package com.fieldnation.data.v2;

import android.os.Parcel;
import android.os.Parcelable;

import com.fieldnation.Log;
import com.fieldnation.json.JsonObject;
import com.fieldnation.json.Serializer;
import com.fieldnation.json.Unserializer;
import com.fieldnation.json.annotations.Json;

/**
 * Created by Michael on 7/21/2016.
 */
public class WorkOrder implements Parcelable {
    private static final String TAG = "WorkOrder";

    @Json
    private Long id;
    @Json
    private String title;
    @Json
    private Status status;
    @Json
    private Org org;
    @Json
    private Location location;
    @Json
    private Requirements requirements;
    @Json
    private Pay pay;

    private WorkOrder() {
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public Status getStatus() {
        return status;
    }

    public Status.Primary getPrimaryStatus() {
        return status.getPrimary();
    }

    public Status.Secondary getSecondaryStatus() {
        return status.getSecondary();
    }

    public Org getOrg() {
        return org;
    }

    public Location getLocation() {
        return location;
    }

    public Requirements getRequirements() {
        return requirements;
    }

    public Pay getPay() {
        return pay;
    }

    /*-*************************************-*/
    /*-			JSON Implementation			-*/
    /*-*************************************-*/
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

    public static WorkOrder fromJson(JsonObject json) {
        try {
            return Unserializer.unserializeObject(WorkOrder.class, json);
        } catch (Exception ex) {
            Log.v(TAG, ex);
            return null;
        }
    }

    /*-*********************************************-*/
    /*-			Parcelable Implementation			-*/
    /*-*********************************************-*/
    public static final Parcelable.Creator<WorkOrder> CREATOR = new Parcelable.Creator<WorkOrder>() {

        @Override
        public WorkOrder createFromParcel(Parcel source) {
            try {
                return WorkOrder.fromJson((JsonObject) source.readParcelable(JsonObject.class.getClassLoader()));
            } catch (Exception ex) {
                Log.v(TAG, ex);
                return null;
            }
        }

        @Override
        public WorkOrder[] newArray(int size) {
            return new WorkOrder[size];
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


    /*-*****************************************-*/
    /*-			Status Implementation			-*/
    /*-*****************************************-*/
    public static class Status implements Parcelable {
        private static final String TAG = "Status";

        @Json
        private String primary;
        @Json
        private String secondary;

        private Status() {
        }

        public Primary getPrimary() {
            return Primary.valueOf(primary);
        }

        public Secondary getSecondary() {
            return Secondary.fromValue(secondary);
        }

        public JsonObject toJson() {
            return toJson(this);
        }

        public static JsonObject toJson(Status status) {
            try {
                return Serializer.serializeObject(status);
            } catch (Exception ex) {
                Log.v(TAG, ex);
                return null;
            }
        }

        public static Status fromJson(JsonObject json) {
            try {
                return Unserializer.unserializeObject(Status.class, json);
            } catch (Exception ex) {
                Log.v(TAG, ex);
                return null;
            }
        }

        /*-*********************************************-*/
        /*-			Parcelable Implementation			-*/
        /*-*********************************************-*/
        public static final Parcelable.Creator<Status> CREATOR = new Parcelable.Creator<Status>() {

            @Override
            public Status createFromParcel(Parcel source) {
                try {
                    return Status.fromJson((JsonObject) source.readParcelable(JsonObject.class.getClassLoader()));
                } catch (Exception ex) {
                    Log.v(TAG, ex);
                    return null;
                }
            }

            @Override
            public Status[] newArray(int size) {
                return new Status[size];
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


        public enum Primary {
            AVAILABLE("available"), // 2 or 9
            ASSIGNED("assigned"), // 3
            INPROGRESS("inprogress"), // 3 + checkedin/out -- assigned
            COMPLETED("completed"), // 4 workdone
            APPROVED("approved"), // 5 == completed
            PAID("paid"), // 6 == completed
            CANCELED("canceled"), // 7
            NA("NA");

            private final String _value;

            Primary(String value) {
                _value = value;
            }

            public static Primary fromValue(String value) {
                if (value == null)
                    return NA;

                Primary[] stati = values();
                for (int i = 0; i < stati.length; i++) {
                    if (stati[i]._value.equals(value)) {
                        return stati[i];
                    }
                }
                return NA;
            }
        }

        public enum Secondary {
            AVAILABLE("available"), // 2
            ROUTED("routed"), // 2 + routed or 9 + routed
            REQUESTED("requested"), // not routed, not in available
            COUNTEROFFERED("counteroffered"), // SUBSTATUS_REQUESTED + countered
            UNCONFIRMED("unconfirmed"), // 3
            CONFIRMED("confirmed"), // 3 + confirmed
            ONHOLD_UNACKNOWLEDGED("onhold_unacknowledged"), // 3
            ONHOLD_ACKNOWLEDGED("onhold_acknowledged"), // 3
            CHECKEDIN("checkedin"), // 3 + checked in
            CHECKEDOUT("checkedout"), // 3 + checked out

            PENDINGREVIEW("pendingreview"), // 4
            INREVIEW("inreview"), // not a thing?
            APPROVED_PROCESSINGPAYMENT("approved_processingpayment"), // 5
            PAID("paid"), // 6
            CANCELED("cancelled"), // 7
            CANCELED_LATEFEEPROCESSING("cancelled_latefeeprocessing"), // 7 + has fees not paid
            CANCELED_LATEFEEPAID("cancelled_latefeepaid"), // 7 + has feed, paid
            NA("NA");

            private final String _value;

            Secondary(String value) {
                _value = value;

            }

            public static Secondary fromValue(String value) {
                if (value == null)
                    return NA;

                Secondary[] stati = values();
                for (int i = 0; i < stati.length; i++) {
                    if (stati[i]._value.equals(value)) {
                        return stati[i];
                    }
                }
                return NA;
            }
        }
    }

}