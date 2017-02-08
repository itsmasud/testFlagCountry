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

public class SelectionRuleCriteria implements Parcelable {
    private static final String TAG = "SelectionRuleCriteria";

    @Json(name = "custom_field")
    private CustomField _customField;

    @Json(name = "extra")
    private String _extra;

    @Json(name = "name")
    private NameEnum _name;

    @Json(name = "description")
    private String _description;

    @Json(name = "weight")
    private Integer _weight;

    @Json(name = "id")
    private Integer _id;

    @Json(name = "operation")
    private OperationEnum _operation;

    @Json(name = "value")
    private String _value;

    @Json(name = "required")
    private Boolean _required;

    @Json(name = "status")
    private StatusEnum _status;

    @Json(name = "points")
    private Integer _points;

    @Json(name = "order")
    private Integer _order;

    public SelectionRuleCriteria() {
    }

    public void setCustomField(CustomField customField) {
        _customField = customField;
    }

    public CustomField getCustomField() {
        return _customField;
    }

    public SelectionRuleCriteria customField(CustomField customField) {
        _customField = customField;
        return this;
    }

    public void setExtra(String extra) {
        _extra = extra;
    }

    public String getExtra() {
        return _extra;
    }

    public SelectionRuleCriteria extra(String extra) {
        _extra = extra;
        return this;
    }

    public void setName(NameEnum name) {
        _name = name;
    }

    public NameEnum getName() {
        return _name;
    }

    public SelectionRuleCriteria name(NameEnum name) {
        _name = name;
        return this;
    }

    public void setDescription(String description) {
        _description = description;
    }

    public String getDescription() {
        return _description;
    }

    public SelectionRuleCriteria description(String description) {
        _description = description;
        return this;
    }

    public void setWeight(Integer weight) {
        _weight = weight;
    }

    public Integer getWeight() {
        return _weight;
    }

    public SelectionRuleCriteria weight(Integer weight) {
        _weight = weight;
        return this;
    }

    public void setId(Integer id) {
        _id = id;
    }

    public Integer getId() {
        return _id;
    }

    public SelectionRuleCriteria id(Integer id) {
        _id = id;
        return this;
    }

    public void setOperation(OperationEnum operation) {
        _operation = operation;
    }

    public OperationEnum getOperation() {
        return _operation;
    }

    public SelectionRuleCriteria operation(OperationEnum operation) {
        _operation = operation;
        return this;
    }

    public void setValue(String value) {
        _value = value;
    }

    public String getValue() {
        return _value;
    }

    public SelectionRuleCriteria value(String value) {
        _value = value;
        return this;
    }

    public void setRequired(Boolean required) {
        _required = required;
    }

    public Boolean getRequired() {
        return _required;
    }

    public SelectionRuleCriteria required(Boolean required) {
        _required = required;
        return this;
    }

    public void setStatus(StatusEnum status) {
        _status = status;
    }

    public StatusEnum getStatus() {
        return _status;
    }

    public SelectionRuleCriteria status(StatusEnum status) {
        _status = status;
        return this;
    }

    public void setPoints(Integer points) {
        _points = points;
    }

    public Integer getPoints() {
        return _points;
    }

    public SelectionRuleCriteria points(Integer points) {
        _points = points;
        return this;
    }

    public void setOrder(Integer order) {
        _order = order;
    }

    public Integer getOrder() {
        return _order;
    }

    public SelectionRuleCriteria order(Integer order) {
        _order = order;
        return this;
    }

    /*-*****************************-*/
    /*-             Json            -*/
    /*-*****************************-*/
    public static SelectionRuleCriteria[] fromJsonArray(JsonArray array) {
        SelectionRuleCriteria[] list = new SelectionRuleCriteria[array.size()];
        for (int i = 0; i < array.size(); i++) {
            list[i] = fromJson(array.getJsonObject(i));
        }
        return list;
    }

    public static SelectionRuleCriteria fromJson(JsonObject obj) {
        try {
            return Unserializer.unserializeObject(SelectionRuleCriteria.class, obj);
        } catch (Exception ex) {
            Log.v(TAG, TAG, ex);
            return null;
        }
    }

    public JsonObject toJson() {
        return toJson(this);
    }

    public static JsonObject toJson(SelectionRuleCriteria selectionRuleCriteria) {
        try {
            return Serializer.serializeObject(selectionRuleCriteria);
        } catch (Exception ex) {
            Log.v(TAG, TAG, ex);
            return null;
        }
    }

    /*-*********************************************-*/
    /*-			Parcelable Implementation           -*/
    /*-*********************************************-*/
    public static final Parcelable.Creator<SelectionRuleCriteria> CREATOR = new Parcelable.Creator<SelectionRuleCriteria>() {

        @Override
        public SelectionRuleCriteria createFromParcel(Parcel source) {
            try {
                return SelectionRuleCriteria.fromJson((JsonObject) source.readParcelable(JsonObject.class.getClassLoader()));
            } catch (Exception ex) {
                Log.v(TAG, ex);
                return null;
            }
        }

        @Override
        public SelectionRuleCriteria[] newArray(int size) {
            return new SelectionRuleCriteria[size];
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
