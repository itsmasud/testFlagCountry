package com.fieldnation.data.v2;

import android.os.Parcel;
import android.os.Parcelable;

import com.fieldnation.data.v2.actions.Action;
import com.fieldnation.fnjson.JsonArray;
import com.fieldnation.fnjson.JsonObject;
import com.fieldnation.fnjson.Serializer;
import com.fieldnation.fnjson.Unserializer;
import com.fieldnation.fnjson.annotations.Json;
import com.fieldnation.fnlog.Log;
import com.fieldnation.fntools.ISO8601;

import java.util.Comparator;

/**
 * Created by Michael on 7/21/2016.
 */
public class WorkOrder implements Parcelable {
    private static final String TAG = "WorkOrder";

    private Action[] actions;
    @Json
    private Long id;
    @Json
    private String title;
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

    public WorkOrder() {
    }

    public Action[] getActions() {
        return actions;
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
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

    @Override
    public int hashCode() {
        return (int) (long) id;
    }

    public static Comparator<WorkOrder> getTimeComparator() {
        return _timeComparator;
    }

    private static final Comparator<WorkOrder> _timeComparator = new Comparator<WorkOrder>() {
        @Override
        public int compare(WorkOrder lhs, WorkOrder rhs) {
            try {
                long l = 0;
                if (lhs.getSchedule().getBegin() != null)
                    l = ISO8601.toUtc(lhs.getSchedule().getBegin());

                long r = 0;
                if (rhs.getSchedule().getBegin() != null)
                    r = ISO8601.toUtc(rhs.getSchedule().getBegin());

                if (l < r)
                    return -1;
                else if (l > r)
                    return 1;
                else
                    return 0;

            } catch (Exception ex) {
                ex.printStackTrace();
            }
            return 0;
        }
    };


    /*-*************************************-*/
    /*-			JSON Implementation			-*/
    /*-*************************************-*/
    public JsonObject toJson() {
        return toJson(this);
    }

    public static JsonObject toJson(WorkOrder workOrder) {
        try {
            JsonObject obj = Serializer.serializeObject(workOrder);
            if (workOrder.actions != null && workOrder.actions.length > 0) {
                JsonArray ja = Action.toJsonArray(workOrder.actions);
                obj.put("actions", ja);
            }
            return obj;
        } catch (Exception ex) {
            Log.v(TAG, ex);
            return null;
        }
    }

    public static WorkOrder fromJson(JsonObject json) {
        try {
            WorkOrder wo = Unserializer.unserializeObject(WorkOrder.class, json);

            if (json.has("actions"))
                wo.actions = Action.parseActions(json.getJsonArray("actions"));

            return wo;
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
}