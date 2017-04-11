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

public class SelectionRuleCriteria implements Parcelable {
    private static final String TAG = "SelectionRuleCriteria";

    @Json(name = "custom_field")
    private CustomField _customField;

    @Json(name = "description")
    private String _description;

    @Json(name = "extra")
    private String _extra;

    @Json(name = "id")
    private Integer _id;

    @Json(name = "name")
    private NameEnum _name;

    @Json(name = "operation")
    private OperationEnum _operation;

    @Json(name = "order")
    private Integer _order;

    @Json(name = "required")
    private Boolean _required;

    @Json(name = "service")
    private String _service;

    @Json(name = "status")
    private StatusEnum _status;

    @Json(name = "value")
    private String _value;

    @Json(name = "weight")
    private Integer _weight;

    @Source
    private JsonObject SOURCE;

    public SelectionRuleCriteria() {
        SOURCE = new JsonObject();
    }

    public SelectionRuleCriteria(JsonObject obj) {
        SOURCE = obj;
    }

    public void setCustomField(CustomField customField) throws ParseException {
        _customField = customField;
        SOURCE.put("custom_field", customField.getJson());
    }

    public CustomField getCustomField() {
        try {
            if (_customField == null && SOURCE.has("custom_field") && SOURCE.get("custom_field") != null)
                _customField = CustomField.fromJson(SOURCE.getJsonObject("custom_field"));
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        if (_customField != null && _customField.isSet())
        return _customField;

        return null;
    }

    public SelectionRuleCriteria customField(CustomField customField) throws ParseException {
        _customField = customField;
        SOURCE.put("custom_field", customField.getJson());
        return this;
    }

    public void setDescription(String description) throws ParseException {
        _description = description;
        SOURCE.put("description", description);
    }

    public String getDescription() {
        try {
            if (_description == null && SOURCE.has("description") && SOURCE.get("description") != null)
                _description = SOURCE.getString("description");
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _description;
    }

    public SelectionRuleCriteria description(String description) throws ParseException {
        _description = description;
        SOURCE.put("description", description);
        return this;
    }

    public void setExtra(String extra) throws ParseException {
        _extra = extra;
        SOURCE.put("extra", extra);
    }

    public String getExtra() {
        try {
            if (_extra == null && SOURCE.has("extra") && SOURCE.get("extra") != null)
                _extra = SOURCE.getString("extra");
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _extra;
    }

    public SelectionRuleCriteria extra(String extra) throws ParseException {
        _extra = extra;
        SOURCE.put("extra", extra);
        return this;
    }

    public void setId(Integer id) throws ParseException {
        _id = id;
        SOURCE.put("id", id);
    }

    public Integer getId() {
        try {
            if (_id == null && SOURCE.has("id") && SOURCE.get("id") != null)
                _id = SOURCE.getInt("id");
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _id;
    }

    public SelectionRuleCriteria id(Integer id) throws ParseException {
        _id = id;
        SOURCE.put("id", id);
        return this;
    }

    public void setName(NameEnum name) throws ParseException {
        _name = name;
        SOURCE.put("name", name.toString());
    }

    public NameEnum getName() {
        try {
            if (_name == null && SOURCE.has("name") && SOURCE.get("name") != null)
                _name = NameEnum.fromString(SOURCE.getString("name"));
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _name;
    }

    public SelectionRuleCriteria name(NameEnum name) throws ParseException {
        _name = name;
        SOURCE.put("name", name.toString());
        return this;
    }

    public void setOperation(OperationEnum operation) throws ParseException {
        _operation = operation;
        SOURCE.put("operation", operation.toString());
    }

    public OperationEnum getOperation() {
        try {
            if (_operation == null && SOURCE.has("operation") && SOURCE.get("operation") != null)
                _operation = OperationEnum.fromString(SOURCE.getString("operation"));
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _operation;
    }

    public SelectionRuleCriteria operation(OperationEnum operation) throws ParseException {
        _operation = operation;
        SOURCE.put("operation", operation.toString());
        return this;
    }

    public void setOrder(Integer order) throws ParseException {
        _order = order;
        SOURCE.put("order", order);
    }

    public Integer getOrder() {
        try {
            if (_order == null && SOURCE.has("order") && SOURCE.get("order") != null)
                _order = SOURCE.getInt("order");
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _order;
    }

    public SelectionRuleCriteria order(Integer order) throws ParseException {
        _order = order;
        SOURCE.put("order", order);
        return this;
    }

    public void setRequired(Boolean required) throws ParseException {
        _required = required;
        SOURCE.put("required", required);
    }

    public Boolean getRequired() {
        try {
            if (_required == null && SOURCE.has("required") && SOURCE.get("required") != null)
                _required = SOURCE.getBoolean("required");
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _required;
    }

    public SelectionRuleCriteria required(Boolean required) throws ParseException {
        _required = required;
        SOURCE.put("required", required);
        return this;
    }

    public void setService(String service) throws ParseException {
        _service = service;
        SOURCE.put("service", service);
    }

    public String getService() {
        try {
            if (_service == null && SOURCE.has("service") && SOURCE.get("service") != null)
                _service = SOURCE.getString("service");
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _service;
    }

    public SelectionRuleCriteria service(String service) throws ParseException {
        _service = service;
        SOURCE.put("service", service);
        return this;
    }

    public void setStatus(StatusEnum status) throws ParseException {
        _status = status;
        SOURCE.put("status", status.toString());
    }

    public StatusEnum getStatus() {
        try {
            if (_status == null && SOURCE.has("status") && SOURCE.get("status") != null)
                _status = StatusEnum.fromString(SOURCE.getString("status"));
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _status;
    }

    public SelectionRuleCriteria status(StatusEnum status) throws ParseException {
        _status = status;
        SOURCE.put("status", status.toString());
        return this;
    }

    public void setValue(String value) throws ParseException {
        _value = value;
        SOURCE.put("value", value);
    }

    public String getValue() {
        try {
            if (_value == null && SOURCE.has("value") && SOURCE.get("value") != null)
                _value = SOURCE.getString("value");
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _value;
    }

    public SelectionRuleCriteria value(String value) throws ParseException {
        _value = value;
        SOURCE.put("value", value);
        return this;
    }

    public void setWeight(Integer weight) throws ParseException {
        _weight = weight;
        SOURCE.put("weight", weight);
    }

    public Integer getWeight() {
        try {
            if (_weight == null && SOURCE.has("weight") && SOURCE.get("weight") != null)
                _weight = SOURCE.getInt("weight");
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _weight;
    }

    public SelectionRuleCriteria weight(Integer weight) throws ParseException {
        _weight = weight;
        SOURCE.put("weight", weight);
        return this;
    }

    /*-******************************-*/
    /*-             Enums            -*/
    /*-******************************-*/
    public enum NameEnum {
        @Json(name = "assignment_history")
        ASSIGNMENT_HISTORY("assignment_history"),
        @Json(name = "assignment_less_then")
        ASSIGNMENT_LESS_THEN("assignment_less_then"),
        @Json(name = "assignment_nearby")
        ASSIGNMENT_NEARBY("assignment_nearby"),
        @Json(name = "background_check")
        BACKGROUND_CHECK("background_check"),
        @Json(name = "block")
        BLOCK("block"),
        @Json(name = "cancel_rate")
        CANCEL_RATE("cancel_rate"),
        @Json(name = "completed_wo_company")
        COMPLETED_WO_COMPANY("completed_wo_company"),
        @Json(name = "completed_wo_marketplace")
        COMPLETED_WO_MARKETPLACE("completed_wo_marketplace"),
        @Json(name = "custom_buyer_field")
        CUSTOM_BUYER_FIELD("custom_buyer_field"),
        @Json(name = "custom_field_match")
        CUSTOM_FIELD_MATCH("custom_field_match"),
        @Json(name = "custom_provider_field")
        CUSTOM_PROVIDER_FIELD("custom_provider_field"),
        @Json(name = "distance")
        DISTANCE("distance"),
        @Json(name = "drug_test")
        DRUG_TEST("drug_test"),
        @Json(name = "has_verified")
        HAS_VERIFIED("has_verified"),
        @Json(name = "insurance")
        INSURANCE("insurance"),
        @Json(name = "preferred_provider")
        PREFERRED_PROVIDER("preferred_provider"),
        @Json(name = "protec")
        PROTEC("protec"),
        @Json(name = "ratings_all")
        RATINGS_ALL("ratings_all"),
        @Json(name = "ratings_positive")
        RATINGS_POSITIVE("ratings_positive"),
        @Json(name = "requests")
        REQUESTS("requests"),
        @Json(name = "skillset")
        SKILLSET("skillset");

        private String value;

        NameEnum(String value) {
            this.value = value;
        }

        public static NameEnum fromString(String value) {
            NameEnum[] values = values();
            for (NameEnum v : values) {
                if (v.value.equals(value))
                    return v;
            }
            return null;
        }

        public static NameEnum[] fromJsonArray(JsonArray jsonArray) {
            NameEnum[] list = new NameEnum[jsonArray.size()];
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

        public static StatusEnum fromString(String value) {
            StatusEnum[] values = values();
            for (StatusEnum v : values) {
                if (v.value.equals(value))
                    return v;
            }
            return null;
        }

        public static StatusEnum[] fromJsonArray(JsonArray jsonArray) {
            StatusEnum[] list = new StatusEnum[jsonArray.size()];
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

    public enum OperationEnum {
        @Json(name = "equal_to")
        EQUAL_TO("equal_to"),
        @Json(name = "greater_than")
        GREATER_THAN("greater_than"),
        @Json(name = "less_than")
        LESS_THAN("less_than"),
        @Json(name = "not_equal_to")
        NOT_EQUAL_TO("not_equal_to");

        private String value;

        OperationEnum(String value) {
            this.value = value;
        }

        public static OperationEnum fromString(String value) {
            OperationEnum[] values = values();
            for (OperationEnum v : values) {
                if (v.value.equals(value))
                    return v;
            }
            return null;
        }

        public static OperationEnum[] fromJsonArray(JsonArray jsonArray) {
            OperationEnum[] list = new OperationEnum[jsonArray.size()];
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
    public static JsonArray toJsonArray(SelectionRuleCriteria[] array) {
        JsonArray list = new JsonArray();
        for (SelectionRuleCriteria item : array) {
            list.add(item.getJson());
        }
        return list;
    }

    public static SelectionRuleCriteria[] fromJsonArray(JsonArray array) {
        SelectionRuleCriteria[] list = new SelectionRuleCriteria[array.size()];
        for (int i = 0; i < array.size(); i++) {
            list[i] = fromJson(array.getJsonObject(i));
        }
        return list;
    }

    public static SelectionRuleCriteria fromJson(JsonObject obj) {
        try {
            return new SelectionRuleCriteria(obj);
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
        dest.writeParcelable(getJson(), flags);
    }

    /*-*****************************-*/
    /*-         Human Code          -*/
    /*-*****************************-*/

    public boolean isSet() {
        return getId() != null && getId() != 0;
    }
}
