package com.fieldnation.data.v2;

import android.os.Parcel;
import android.os.Parcelable;

import com.fieldnation.data.v2.actions.Action;
import com.fieldnation.data.v2.actions.ActionContainer;
import com.fieldnation.fnjson.JsonObject;
import com.fieldnation.fnjson.Serializer;
import com.fieldnation.fnjson.Unserializer;
import com.fieldnation.fnjson.annotations.Json;
import com.fieldnation.fnlog.Log;

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
    private String type;
    @Json
    private Org org;
    @Json
    private Bundle bundle;
    @Json
    private Location location;
    @Json
    private Schedule schedule;
    @Json
    private Pay pay;
    @Json(name = "actions")
    private ActionContainer actions;
    @Json(name = "contacts")
    private Contact[] contacts;


    public WorkOrder() {
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getType() {
        return type;
    }

    public Org getOrg() {
        return org;
    }

    public Bundle getBundle() {
        return bundle;
    }

    public boolean isBundle() {
        return bundle != null;
    }

    public Location getLocation() {
        return location;
    }

    public Schedule getSchedule() {
        return schedule;
    }

    public Pay getPay() {
        return pay;
    }

    public Action[] getPrimaryActions() {
        if (actions == null)
            return null;

        return actions.getPrimary();
    }

    public Action[] getSecondaryActions() {
        if (actions == null)
            return null;

        return actions.getSecondary();
    }

    public Contact[] getContacts() {
        return contacts;
    }

    @Override
    public int hashCode() {
        return (int) (long) id;
    }

    /*-*************************************-*/
    /*-			JSON Implementation			-*/
    /*-*************************************-*/
    public JsonObject toJson() {
        return toJson(this);
    }

    public static JsonObject toJson(WorkOrder workOrder) {
        JsonObject obj = null;
        try {
            obj = Serializer.serializeObject(workOrder);
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }
        return obj;
    }

    public static WorkOrder fromJson(JsonObject json) {
        WorkOrder wo = null;
        try {
            wo = Unserializer.unserializeObject(WorkOrder.class, json);
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }
        return wo;
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
}