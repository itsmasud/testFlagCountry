package com.fieldnation.data.workorder;

import android.os.Parcel;
import android.os.Parcelable;

import com.fieldnation.fnlog.Log;
import com.fieldnation.fnjson.JsonObject;
import com.fieldnation.fnjson.Serializer;
import com.fieldnation.fnjson.Unserializer;
import com.fieldnation.fnjson.annotations.Json;

public class CustomField implements Parcelable {
    @Json(name = "customFieldFormat")
    private String _customFieldFormat;
    @Json(name = "customLabelId")
    private Long _customLabelId;
    @Json(name = "data_field_type")
    private String _dataFieldType;
    @Json(name = "dateEntered")
    private String _dateEntered;
    @Json(name = "dependsOnCustomLabelId")
    private Integer _dependsOnCustomLabelId;
    @Json(name = "dependsOnCustomLabelOperator")
    private Integer _dependsOnCustomLabelOperator;
    @Json(name = "dependsOnCustomLabelValue")
    private String _dependsOnCustomLabelValue;
    @Json(name = "isMatched")
    private Boolean _isMatched;
    @Json(name = "label")
    private String _label;
    @Json(name = "order")
    private Integer _order;
    @Json(name = "predefinedValues")
    private String[] _predefinedValues;
    @Json(name = "required")
    private Integer _required;
    @Json(name = "section")
    private String _section;
    @Json(name = "tip")
    private String _tip;
    @Json(name = "value")
    private String _value;

    public CustomField() {
    }

    public String getCustomFieldFormat() {
        return _customFieldFormat;
    }

    public Long getCustomLabelId() {
        return _customLabelId;
    }

    public String getDataFieldType() {
        return _dataFieldType;
    }

    public String getDateEntered() {
        return _dateEntered;
    }

    public Integer getDependsOnCustomLabelId() {
        return _dependsOnCustomLabelId;
    }

    public Integer getDependsOnCustomLabelOperator() {
        return _dependsOnCustomLabelOperator;
    }

    public String getDependsOnCustomLabelValue() {
        return _dependsOnCustomLabelValue;
    }

    public Boolean getIsMatched() {
        return _isMatched;
    }

    public String getLabel() {
        return _label;
    }

    public Integer getOrder() {
        return _order == null ? 0 : _order;
    }

    public String[] getPredefinedValues() {
        return _predefinedValues;
    }

    public Boolean getRequired() {
        return _required == 1;
    }

    public String getSection() {
        return _section;
    }

    public String getTip() {
        return _tip;
    }

    public String getValue() {
        return _value;
    }

    public JsonObject toJson() {
        return toJson(this);
    }

    public static JsonObject toJson(CustomField customField) {
        try {
            return Serializer.serializeObject(customField);
        } catch (Exception ex) {
            Log.v(TAG, ex);
            return null;
        }
    }

    public static CustomField fromJson(JsonObject json) {
        try {
            return Unserializer.unserializeObject(CustomField.class, json);
        } catch (Exception ex) {
            Log.v(TAG, ex);
            return null;
        }
    }

    /*-*************************************-*/
    /*-             Human Code              -*/
    /*-*************************************-*/
    private static final String TAG = "data.workorder.CustomField";

    public enum FieldType {
        TEXT(null),
        DATE("date"),
        TIME("time"),
        DATETIME("date_time"),
        LIST("predefined"),
        NUMBER("numeric"),
        PHONE("phone");

        private final String _value;

        FieldType(String value) {
            _value = value;
        }

        public static FieldType fromString(String type) {
            FieldType[] vs = values();

            if (type == null) {
                return TEXT;
            }

            for (FieldType v : vs) {
                if (type.equals(v._value))
                    return v;
            }

            Log.w(TAG, "invalid FieldType of " + type + " found!!!");

            return TEXT;
        }
    }

    public FieldType getFieldType() {
        return FieldType.fromString(getDataFieldType());
    }

    /*-*********************************************-*/
    /*-			Parcelable Implementation			-*/
    /*-*********************************************-*/
    public static final Parcelable.Creator<CustomField> CREATOR = new Parcelable.Creator<CustomField>() {

        @Override
        public CustomField createFromParcel(Parcel source) {
            try {
                return CustomField.fromJson((JsonObject) (source.readParcelable(JsonObject.class.getClassLoader())));
            } catch (Exception e) {
                Log.v(TAG, e);
            }
            return null;
        }

        @Override
        public CustomField[] newArray(int size) {
            return new CustomField[size];
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
