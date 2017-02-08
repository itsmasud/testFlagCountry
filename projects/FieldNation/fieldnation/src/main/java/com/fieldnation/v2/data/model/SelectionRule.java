package com.fieldnation.v2.data.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.fieldnation.fnjson.JsonArray;
import com.fieldnation.fnjson.JsonObject;
import com.fieldnation.fnjson.Serializer;
import com.fieldnation.fnjson.Unserializer;
import com.fieldnation.fnjson.annotations.Json;
import com.fieldnation.fnlog.Log;

/**
 * Created by dmgen from swagger.
 */

public class SelectionRule implements Parcelable {
    private static final String TAG = "SelectionRule";

    @Json(name = "metadata")
    private ListEnvelope _metadata;

    @Json(name = "name")
    private String _name;

    @Json(name = "sum")
    private SelectionRuleSum _sum;

    @Json(name = "id")
    private Integer _id;

    @Json(name = "actions")
    private ActionsEnum _actions;

    @Json(name = "results")
    private SelectionRuleCriteria[] _results;

    @Json(name = "status")
    private StatusEnum _status;

    public SelectionRule() {
    }

    public void setMetadata(ListEnvelope metadata) {
        _metadata = metadata;
    }

    public ListEnvelope getMetadata() {
        return _metadata;
    }

    public SelectionRule metadata(ListEnvelope metadata) {
        _metadata = metadata;
        return this;
    }

    public void setName(String name) {
        _name = name;
    }

    public String getName() {
        return _name;
    }

    public SelectionRule name(String name) {
        _name = name;
        return this;
    }

    public void setSum(SelectionRuleSum sum) {
        _sum = sum;
    }

    public SelectionRuleSum getSum() {
        return _sum;
    }

    public SelectionRule sum(SelectionRuleSum sum) {
        _sum = sum;
        return this;
    }

    public void setId(Integer id) {
        _id = id;
    }

    public Integer getId() {
        return _id;
    }

    public SelectionRule id(Integer id) {
        _id = id;
        return this;
    }

    public void setActions(ActionsEnum actions) {
        _actions = actions;
    }

    public ActionsEnum getActions() {
        return _actions;
    }

    public SelectionRule actions(ActionsEnum actions) {
        _actions = actions;
        return this;
    }

    public void setResults(SelectionRuleCriteria[] results) {
        _results = results;
    }

    public SelectionRuleCriteria[] getResults() {
        return _results;
    }

    public SelectionRule results(SelectionRuleCriteria[] results) {
        _results = results;
        return this;
    }

    public void setStatus(StatusEnum status) {
        _status = status;
    }

    public StatusEnum getStatus() {
        return _status;
    }

    public SelectionRule status(StatusEnum status) {
        _status = status;
        return this;
    }

    /*-******************************-*/
    /*-             Enums            -*/
    /*-******************************-*/
    public enum ActionsEnum {
        @Json(name = "delete")
        DELETE("delete");

        private String value;

        ActionsEnum(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return String.valueOf(value);
        }
    }

    public enum StatusEnum {
        @Json(name = "match")
        MATCH("match"),
        @Json(name = "no_match_optional")
        NO_MATCH_OPTIONAL("no_match_optional"),
        @Json(name = "no_match_required")
        NO_MATCH_REQUIRED("no_match_required");

        private String value;

        StatusEnum(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return String.valueOf(value);
        }
    }

    /*-*****************************-*/
    /*-             Json            -*/
    /*-*****************************-*/
    public static SelectionRule[] fromJsonArray(JsonArray array) {
        SelectionRule[] list = new SelectionRule[array.size()];
        for (int i = 0; i < array.size(); i++) {
            list[i] = fromJson(array.getJsonObject(i));
        }
        return list;
    }

    public static SelectionRule fromJson(JsonObject obj) {
        try {
            return Unserializer.unserializeObject(SelectionRule.class, obj);
        } catch (Exception ex) {
            Log.v(TAG, TAG, ex);
            return null;
        }
    }

    public JsonObject toJson() {
        return toJson(this);
    }

    public static JsonObject toJson(SelectionRule selectionRule) {
        try {
            return Serializer.serializeObject(selectionRule);
        } catch (Exception ex) {
            Log.v(TAG, TAG, ex);
            return null;
        }
    }

    /*-*********************************************-*/
    /*-			Parcelable Implementation           -*/
    /*-*********************************************-*/
    public static final Parcelable.Creator<SelectionRule> CREATOR = new Parcelable.Creator<SelectionRule>() {

        @Override
        public SelectionRule createFromParcel(Parcel source) {
            try {
                return SelectionRule.fromJson((JsonObject) source.readParcelable(JsonObject.class.getClassLoader()));
            } catch (Exception ex) {
                Log.v(TAG, ex);
                return null;
            }
        }

        @Override
        public SelectionRule[] newArray(int size) {
            return new SelectionRule[size];
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
