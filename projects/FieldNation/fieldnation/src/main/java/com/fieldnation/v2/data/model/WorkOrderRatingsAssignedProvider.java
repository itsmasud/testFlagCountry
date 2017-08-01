package com.fieldnation.v2.data.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.fieldnation.fnjson.JsonArray;
import com.fieldnation.fnjson.JsonObject;
import com.fieldnation.fnjson.Serializer;
import com.fieldnation.fnjson.Unserializer;
import com.fieldnation.fnjson.annotations.Json;
import com.fieldnation.fnjson.annotations.Source;
import com.fieldnation.fnlog.Log;
import com.fieldnation.fntools.misc;

import java.text.ParseException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by dmgen from swagger.
 */

public class WorkOrderRatingsAssignedProvider implements Parcelable {
    private static final String TAG = "WorkOrderRatingsAssignedProvider";

    @Json(name = "actions")
    private ActionsEnum[] _actions;

    @Json(name = "blocks")
    private Blocks _blocks;

    @Json(name = "overall")
    private WorkOrderRatingsAssignedProviderOverall _overall;

    @Json(name = "user")
    private User _user;

    @Json(name = "work_order")
    private WorkOrderRatingsAssignedProviderWorkOrder _workOrder;

    @Source
    private JsonObject SOURCE;

    public WorkOrderRatingsAssignedProvider() {
        SOURCE = new JsonObject();
    }

    public WorkOrderRatingsAssignedProvider(JsonObject obj) {
        SOURCE = obj;
    }

    public void setActions(ActionsEnum[] actions) throws ParseException {
        _actions = actions;
        JsonArray ja = new JsonArray();
        for (ActionsEnum item : actions) {
            ja.add(item.toString());
        }
        SOURCE.put("actions", ja);
    }

    public ActionsEnum[] getActions() {
        try {
            if (_actions != null)
                return _actions;

            if (SOURCE.has("actions") && SOURCE.get("actions") != null) {
                _actions = ActionsEnum.fromJsonArray(SOURCE.getJsonArray("actions"));
            }

        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        if (_actions == null)
            _actions = new ActionsEnum[0];

        return _actions;
    }

    public WorkOrderRatingsAssignedProvider actions(ActionsEnum[] actions) throws ParseException {
        _actions = actions;
        JsonArray ja = new JsonArray();
        for (ActionsEnum item : actions) {
            ja.add(item.toString());
        }
        SOURCE.put("actions", ja, true);
        return this;
    }

    public void setBlocks(Blocks blocks) throws ParseException {
        _blocks = blocks;
        SOURCE.put("blocks", blocks.getJson());
    }

    public Blocks getBlocks() {
        try {
            if (_blocks == null && SOURCE.has("blocks") && SOURCE.get("blocks") != null)
                _blocks = Blocks.fromJson(SOURCE.getJsonObject("blocks"));
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        if (_blocks == null)
            _blocks = new Blocks();

            return _blocks;
    }

    public WorkOrderRatingsAssignedProvider blocks(Blocks blocks) throws ParseException {
        _blocks = blocks;
        SOURCE.put("blocks", blocks.getJson());
        return this;
    }

    public void setOverall(WorkOrderRatingsAssignedProviderOverall overall) throws ParseException {
        _overall = overall;
        SOURCE.put("overall", overall.getJson());
    }

    public WorkOrderRatingsAssignedProviderOverall getOverall() {
        try {
            if (_overall == null && SOURCE.has("overall") && SOURCE.get("overall") != null)
                _overall = WorkOrderRatingsAssignedProviderOverall.fromJson(SOURCE.getJsonObject("overall"));
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        if (_overall == null)
            _overall = new WorkOrderRatingsAssignedProviderOverall();

            return _overall;
    }

    public WorkOrderRatingsAssignedProvider overall(WorkOrderRatingsAssignedProviderOverall overall) throws ParseException {
        _overall = overall;
        SOURCE.put("overall", overall.getJson());
        return this;
    }

    public void setUser(User user) throws ParseException {
        _user = user;
        SOURCE.put("user", user.getJson());
    }

    public User getUser() {
        try {
            if (_user == null && SOURCE.has("user") && SOURCE.get("user") != null)
                _user = User.fromJson(SOURCE.getJsonObject("user"));
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        if (_user == null)
            _user = new User();

            return _user;
    }

    public WorkOrderRatingsAssignedProvider user(User user) throws ParseException {
        _user = user;
        SOURCE.put("user", user.getJson());
        return this;
    }

    public void setWorkOrder(WorkOrderRatingsAssignedProviderWorkOrder workOrder) throws ParseException {
        _workOrder = workOrder;
        SOURCE.put("work_order", workOrder.getJson());
    }

    public WorkOrderRatingsAssignedProviderWorkOrder getWorkOrder() {
        try {
            if (_workOrder == null && SOURCE.has("work_order") && SOURCE.get("work_order") != null)
                _workOrder = WorkOrderRatingsAssignedProviderWorkOrder.fromJson(SOURCE.getJsonObject("work_order"));
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        if (_workOrder == null)
            _workOrder = new WorkOrderRatingsAssignedProviderWorkOrder();

            return _workOrder;
    }

    public WorkOrderRatingsAssignedProvider workOrder(WorkOrderRatingsAssignedProviderWorkOrder workOrder) throws ParseException {
        _workOrder = workOrder;
        SOURCE.put("work_order", workOrder.getJson());
        return this;
    }

    /*-******************************-*/
    /*-             Enums            -*/
    /*-******************************-*/
    public enum ActionsEnum {
        @Json(name = "add")
        ADD("add"),
        @Json(name = "delete")
        DELETE("delete"),
        @Json(name = "edit")
        EDIT("edit");

        private String value;

        ActionsEnum(String value) {
            this.value = value;
        }

        public static ActionsEnum fromString(String value) {
            ActionsEnum[] values = values();
            for (ActionsEnum v : values) {
                if (v.value.equals(value))
                    return v;
            }
            return null;
        }

        public static ActionsEnum[] fromJsonArray(JsonArray jsonArray) {
            ActionsEnum[] list = new ActionsEnum[jsonArray.size()];
            for (int i = 0; i < list.length; i++) {
                list[i] = fromString(jsonArray.getString(i));
            }
            return list;
        }

        @Override
        public String toString() {
            return String.valueOf(value);
        }
    }

    /*-*****************************-*/
    /*-             Json            -*/
    /*-*****************************-*/
    public static JsonArray toJsonArray(WorkOrderRatingsAssignedProvider[] array) {
        JsonArray list = new JsonArray();
        for (WorkOrderRatingsAssignedProvider item : array) {
            list.add(item.getJson());
        }
        return list;
    }

    public static WorkOrderRatingsAssignedProvider[] fromJsonArray(JsonArray array) {
        WorkOrderRatingsAssignedProvider[] list = new WorkOrderRatingsAssignedProvider[array.size()];
        for (int i = 0; i < array.size(); i++) {
            list[i] = fromJson(array.getJsonObject(i));
        }
        return list;
    }

    public static WorkOrderRatingsAssignedProvider fromJson(JsonObject obj) {
        try {
            return new WorkOrderRatingsAssignedProvider(obj);
        } catch (Exception ex) {
            Log.v(TAG, ex);
            return null;
        }
    }

    public JsonObject getJson() {
        return SOURCE;
    }

    /*-*********************************************-*/
    /*-			Parcelable Implementation           -*/
    /*-*********************************************-*/
    public static final Parcelable.Creator<WorkOrderRatingsAssignedProvider> CREATOR = new Parcelable.Creator<WorkOrderRatingsAssignedProvider>() {

        @Override
        public WorkOrderRatingsAssignedProvider createFromParcel(Parcel source) {
            try {
                return WorkOrderRatingsAssignedProvider.fromJson((JsonObject) source.readParcelable(JsonObject.class.getClassLoader()));
            } catch (Exception ex) {
                Log.v(TAG, ex);
                return null;
            }
        }

        @Override
        public WorkOrderRatingsAssignedProvider[] newArray(int size) {
            return new WorkOrderRatingsAssignedProvider[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(getJson(), flags);
    }

    /*-*****************************-*/
    /*-         Human Code          -*/
    /*-*****************************-*/

}
