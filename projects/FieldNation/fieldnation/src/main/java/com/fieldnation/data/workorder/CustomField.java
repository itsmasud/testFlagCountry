package com.fieldnation.data.workorder;

import android.os.Parcel;
import android.os.Parcelable;

import com.fieldnation.Log;
import com.fieldnation.json.JsonObject;
import com.fieldnation.json.Serializer;
import com.fieldnation.json.annotations.Json;

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
    @Json(name = "predefinedValues")
    private String[] _predefinedValues;
    @Json(name = "required")
    private Integer _required;
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

    public String[] getPredefinedValues() {
        return _predefinedValues;
    }

    public Boolean getRequired() {
        return _required == 1;
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
            ex.printStackTrace();
            return null;
        }
    }

    public static CustomField fromJson(JsonObject json) {
        try {
            return Serializer.unserializeObject(CustomField.class, json);
        } catch (Exception ex) {
            ex.printStackTrace();
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

        private String _value;

        private FieldType(String value) {
            _value = value;
        }

        public static FieldType fromString(String type) {
            FieldType[] vs = values();

            if (type == null) {
                return TEXT;
            }

            for (int i = 0; i < vs.length; i++) {
                if (type.equals(vs[i]._value))
                    return vs[i];
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
                e.printStackTrace();
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
