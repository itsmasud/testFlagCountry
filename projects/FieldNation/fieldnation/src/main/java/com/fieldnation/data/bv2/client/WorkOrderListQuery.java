package com.fieldnation.data.bv2.client;

import android.os.Parcel;
import android.os.Parcelable;

import com.fieldnation.data.bv2.model.Assignee;
import com.fieldnation.data.v2.SavedSearchParams;
import com.fieldnation.fnjson.JsonObject;
import com.fieldnation.fnjson.Serializer;
import com.fieldnation.fnjson.Unserializer;
import com.fieldnation.fnjson.annotations.Json;
import com.fieldnation.fnlog.Log;

/**
 * Created by mc on 1/24/17.
 */

public class WorkOrderListQuery implements Parcelable {
    private static final String TAG = "WorkOrderListQuery";

    @Json(name = "list")
    private String list;

    public WorkOrderListQuery() {
    }

    public String toUrlParams() {
        StringBuilder sb = new StringBuilder();

        sb.append("&list=" + list);

        sb.deleteCharAt(0);
        sb.insert(0, "?");

        return sb.toString();
    }

    /**
     * Saved group which sandboxes filters, sorts and column preferences in addition to pre-applied settings (e.g.: tabs or saved searches)
     *
     * @param list
     * @return
     */
    public WorkOrderListQuery list(String list) {
        this.list = list;
        return this;
    }

    public String getList() {
        return list;
    }

    public void setList(String list) {
        this.list = list;
    }

    /*-*****************************-*/
    /*-             Json            -*/
    /*-*****************************-*/
    public static WorkOrderListQuery fromJson(JsonObject obj) {
        try {
            return Unserializer.unserializeObject(WorkOrderListQuery.class, obj);
        } catch (Exception ex) {
            Log.v(TAG, ex);
            return null;
        }
    }

    public JsonObject toJson() {
        return toJson(this);
    }

    public static JsonObject toJson(WorkOrderListQuery workOrderListQuery) {
        try {
            return Serializer.serializeObject(workOrderListQuery);
        } catch (Exception ex) {
            Log.v(TAG, ex);
            return null;
        }
    }

    /*-*********************************************-*/
    /*-			Parcelable Implementation			-*/
    /*-*********************************************-*/
    public static final Parcelable.Creator<WorkOrderListQuery> CREATOR = new Parcelable.Creator<WorkOrderListQuery>() {

        @Override
        public WorkOrderListQuery createFromParcel(Parcel source) {
            try {
                return WorkOrderListQuery.fromJson((JsonObject) source.readParcelable(JsonObject.class.getClassLoader()));
            } catch (Exception ex) {
                Log.v(TAG, ex);
                return null;
            }
        }

        @Override
        public WorkOrderListQuery[] newArray(int size) {
            return new WorkOrderListQuery[size];
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
