package com.fieldnation.data.v2;

import android.os.Parcel;
import android.os.Parcelable;

import com.fieldnation.fnjson.JsonArray;
import com.fieldnation.fnjson.JsonObject;
import com.fieldnation.fnjson.Serializer;
import com.fieldnation.fnjson.Unserializer;
import com.fieldnation.fnjson.annotations.Json;
import com.fieldnation.fnlog.Log;
import com.fieldnation.service.data.v2.workorder.WorkOrderListType;

/**
 * Created by Michael on 7/21/2016.
 */
public class SavedSearchParams implements Parcelable {
    private static final String TAG = "SavedSearchParams";

    public enum ListType {
        UNAVAILABLE("unavailable"),
        AVAILABLE("available"),
        ASSIGNED("assigned"),
        COMPLETED("completed");

        private final String _callString;

        ListType(String callString) {
            _callString = callString;
        }

        public String getCallString() {
            return _callString;
        }
    }

    public enum Status {
        DRAFT("draft"),
        DELETED("deleted"),
        PUBLISHED("published"),
        ROUTED("routed"),
        REQUESTED("requested"),
        DECLINED("declined"),
        COUNTER_OFFER("counter_offer"),
        CANCELED("canceled"),
        IN_PROGRESS("in_progress"),
        ON_HOLD("on_hold"),
        ON_HOLD_ACK("on_hold_ack"),
        NEEDS_READY_TO_GO("needs_ready_to_go"),
        CONFIRMED("confirmed"),
        UNCONFIRMED("unconfirmed"),
        CONFIRMED_ETA("confirmed_eta"),
        READY_TO_GO("ready_to_go"),
        CHECKED_IN("checked_in"),
        CHECKED_OUT("checked_out"),
        PENDING_REVIEW("pending_review"),
        APPROVED("approved"),
        PAID("paid"),
        TODAY("today"),
        TOMORROW("tomorrow");

        private final String _callString;

        Status(String callString) {
            _callString = callString;
        }

        public String getCallString() {
            return _callString;
        }
    }

    @Json
    public int id;
    @Json
    public Double latitude = null;
    @Json
    public Double longitude = null;
    @Json
    public Double radius = null;
    @Json
    public String sort = "time";
    @Json
    public String order = "asc";
    @Json
    public Boolean remoteWork = null;
    @Json
    public String title;
    @Json
    public Boolean canEdit = false;


    // Ui stuff for SearchEditScreen
    @Json
    public Integer uiLocationSpinner = 1;
    @Json
    public String uiLocationText = "";
    @Json
    public Integer uiDistanceSpinner = 3;

    // These three are encoded manually in toJson and fromJson
    public WorkOrderListType woList = WorkOrderListType.AVAILABLE;
    public ListType type = ListType.AVAILABLE;
    public Status status[] = new Status[]{Status.PUBLISHED};

    public SavedSearchParams() {
    }

    public SavedSearchParams(int id) {
        this.id = id;
    }

    public SavedSearchParams type(ListType listType) {
        type = listType;
        return this;
    }

    public SavedSearchParams canEdit(boolean canEdit) {
        this.canEdit = canEdit;
        return this;
    }

    public SavedSearchParams status(Status[] status) {
        this.status = status;
        return this;
    }

    public SavedSearchParams location(Double latitude, Double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
        return this;
    }

    public SavedSearchParams radius(Double radius) {
        this.radius = radius;
        return this;
    }

    public SavedSearchParams title(String title) {
        this.title = title;
        return this;
    }

    public SavedSearchParams order(String order) {
        this.order = order;
        return this;
    }

    public String toUrlParams() {
        String params = "?type=" + type.getCallString();

        if (status != null && status.length > 0) {
            params += "&status=";
            for (int i = 0; i < status.length; i++) {
                params += status[i].getCallString();
                if (i < status.length - 1)
                    params += ",";
            }
        }

        if (remoteWork != null)
            params += "&remote_work=" + (remoteWork ? 1 : 0);

        if (remoteWork == null || !remoteWork) {

            if (latitude != null && longitude != null)
                params += "&lat=" + latitude + "&lng=" + longitude;

            if (radius != null)
                params += "&radius=" + radius;
        }

        if (sort != null && order != null)
            params += "&sort=" + sort + "&order=" + order;


        return params;
    }

    public String toKey() {
        String key = type.getCallString();

        if (status != null && status.length > 0) {
            key += ":";

            for (int i = 0; i < status.length; i++) {
                Status s = status[i];
                key += s.getCallString();
                if (i < status.length - 1)
                    key += ",";
            }
        }

        if (latitude != null && longitude != null)
            key += ":" + ((int) (latitude * 1000)) + ":" + ((int) (longitude * 1000));

        if (radius != null)
            key += ":" + ((int) (radius * 1000));

        if (sort != null && order != null)
            key += ":" + sort + " " + order;

        if (remoteWork != null)
            key += ":remote_work=" + (remoteWork ? 1 : 0);

        return key;
    }

    // TODO make this better
    @Override
    public boolean equals(Object o) {
        if (!(o instanceof SavedSearchParams))
            return false;

        return toKey().equals(((SavedSearchParams) o).toKey());
    }

    /*-*************************************-*/
    /*-			JSON Implementation			-*/
    /*-*************************************-*/
    public JsonObject toJson() {
        return toJson(this);
    }

    public static JsonObject toJson(SavedSearchParams searchParams) {
        try {
            JsonObject obj = Serializer.serializeObject(searchParams);

            obj.put("woList", searchParams.woList.ordinal());

            obj.put("listType", searchParams.type.ordinal());

            if (searchParams.status != null && searchParams.status.length > 0) {
                JsonArray ja = new JsonArray();

                for (Status s : searchParams.status) {
                    ja.add(s.ordinal());
                }
                obj.put("status", ja);
            }

            return obj;
        } catch (Exception ex) {
            Log.v(TAG, ex);
            return null;
        }
    }

    public static SavedSearchParams fromJson(JsonObject json) {
        try {
            SavedSearchParams searchParams = Unserializer.unserializeObject(SavedSearchParams.class, json);

            searchParams.type = ListType.values()[json.getInt("listType")];
            searchParams.woList = WorkOrderListType.values()[json.getInt("woList")];

            if (json.has("status")) {
                JsonArray ja = json.getJsonArray("status");
                Status[] status = new Status[ja.size()];
                for (int i = 0; i < ja.size(); i++) {
                    status[i] = Status.values()[ja.getInt(i)];
                }
                searchParams.status = status;
            }
            return searchParams;

        } catch (Exception ex) {
            Log.v(TAG, ex);
            return null;
        }
    }

    /*-*********************************************-*/
    /*-			Parcelable Implementation			-*/
    /*-*********************************************-*/
    public static final Parcelable.Creator<SavedSearchParams> CREATOR = new Parcelable.Creator<SavedSearchParams>() {

        @Override
        public SavedSearchParams createFromParcel(Parcel source) {
            try {
                return SavedSearchParams.fromJson((JsonObject) source.readParcelable(JsonObject.class.getClassLoader()));
            } catch (Exception ex) {
                Log.v(TAG, ex);
                return null;
            }
        }

        @Override
        public SavedSearchParams[] newArray(int size) {
            return new SavedSearchParams[size];
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
