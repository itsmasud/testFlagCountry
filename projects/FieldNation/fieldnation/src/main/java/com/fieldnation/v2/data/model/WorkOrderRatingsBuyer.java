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

public class WorkOrderRatingsBuyer implements Parcelable {
    private static final String TAG = "WorkOrderRatingsBuyer";

    @Json(name = "actions")
    private ActionsEnum[] _actions;

    @Json(name = "blocks")
    private Blocks _blocks;

    @Json(name = "company")
    private WorkOrderRatingsBuyerCompany _company;

    @Json(name = "manager")
    private User _manager;

    @Json(name = "overall")
    private WorkOrderRatingsBuyerOverall _overall;

    @Json(name = "work_order")
    private WorkOrderRatingsBuyerWorkOrder _workOrder;

    @Source
    private JsonObject SOURCE;

    public WorkOrderRatingsBuyer() {
        SOURCE = new JsonObject();
    }

    public WorkOrderRatingsBuyer(JsonObject obj) {
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

    public WorkOrderRatingsBuyer actions(ActionsEnum[] actions) throws ParseException {
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

    public WorkOrderRatingsBuyer blocks(Blocks blocks) throws ParseException {
        _blocks = blocks;
        SOURCE.put("blocks", blocks.getJson());
        return this;
    }

    public void setCompany(WorkOrderRatingsBuyerCompany company) throws ParseException {
        _company = company;
        SOURCE.put("company", company.getJson());
    }

    public WorkOrderRatingsBuyerCompany getCompany() {
        try {
            if (_company == null && SOURCE.has("company") && SOURCE.get("company") != null)
                _company = WorkOrderRatingsBuyerCompany.fromJson(SOURCE.getJsonObject("company"));
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        if (_company == null)
            _company = new WorkOrderRatingsBuyerCompany();

            return _company;
    }

    public WorkOrderRatingsBuyer company(WorkOrderRatingsBuyerCompany company) throws ParseException {
        _company = company;
        SOURCE.put("company", company.getJson());
        return this;
    }

    public void setManager(User manager) throws ParseException {
        _manager = manager;
        SOURCE.put("manager", manager.getJson());
    }

    public User getManager() {
        try {
            if (_manager == null && SOURCE.has("manager") && SOURCE.get("manager") != null)
                _manager = User.fromJson(SOURCE.getJsonObject("manager"));
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        if (_manager == null)
            _manager = new User();

            return _manager;
    }

    public WorkOrderRatingsBuyer manager(User manager) throws ParseException {
        _manager = manager;
        SOURCE.put("manager", manager.getJson());
        return this;
    }

    public void setOverall(WorkOrderRatingsBuyerOverall overall) throws ParseException {
        _overall = overall;
        SOURCE.put("overall", overall.getJson());
    }

    public WorkOrderRatingsBuyerOverall getOverall() {
        try {
            if (_overall == null && SOURCE.has("overall") && SOURCE.get("overall") != null)
                _overall = WorkOrderRatingsBuyerOverall.fromJson(SOURCE.getJsonObject("overall"));
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        if (_overall == null)
            _overall = new WorkOrderRatingsBuyerOverall();

            return _overall;
    }

    public WorkOrderRatingsBuyer overall(WorkOrderRatingsBuyerOverall overall) throws ParseException {
        _overall = overall;
        SOURCE.put("overall", overall.getJson());
        return this;
    }

    public void setWorkOrder(WorkOrderRatingsBuyerWorkOrder workOrder) throws ParseException {
        _workOrder = workOrder;
        SOURCE.put("work_order", workOrder.getJson());
    }

    public WorkOrderRatingsBuyerWorkOrder getWorkOrder() {
        try {
            if (_workOrder == null && SOURCE.has("work_order") && SOURCE.get("work_order") != null)
                _workOrder = WorkOrderRatingsBuyerWorkOrder.fromJson(SOURCE.getJsonObject("work_order"));
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        if (_workOrder == null)
            _workOrder = new WorkOrderRatingsBuyerWorkOrder();

            return _workOrder;
    }

    public WorkOrderRatingsBuyer workOrder(WorkOrderRatingsBuyerWorkOrder workOrder) throws ParseException {
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
    public static JsonArray toJsonArray(WorkOrderRatingsBuyer[] array) {
        JsonArray list = new JsonArray();
        for (WorkOrderRatingsBuyer item : array) {
            list.add(item.getJson());
        }
        return list;
    }

    public static WorkOrderRatingsBuyer[] fromJsonArray(JsonArray array) {
        WorkOrderRatingsBuyer[] list = new WorkOrderRatingsBuyer[array.size()];
        for (int i = 0; i < array.size(); i++) {
            list[i] = fromJson(array.getJsonObject(i));
        }
        return list;
    }

    public static WorkOrderRatingsBuyer fromJson(JsonObject obj) {
        try {
            return new WorkOrderRatingsBuyer(obj);
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
    public static final Parcelable.Creator<WorkOrderRatingsBuyer> CREATOR = new Parcelable.Creator<WorkOrderRatingsBuyer>() {

        @Override
        public WorkOrderRatingsBuyer createFromParcel(Parcel source) {
            try {
                return WorkOrderRatingsBuyer.fromJson((JsonObject) source.readParcelable(JsonObject.class.getClassLoader()));
            } catch (Exception ex) {
                Log.v(TAG, ex);
                return null;
            }
        }

        @Override
        public WorkOrderRatingsBuyer[] newArray(int size) {
            return new WorkOrderRatingsBuyer[size];
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
