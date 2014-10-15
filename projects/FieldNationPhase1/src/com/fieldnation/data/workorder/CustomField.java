package com.fieldnation.data.workorder;

import com.fieldnation.json.JsonObject;
import com.fieldnation.json.Serializer;
import com.fieldnation.json.annotations.Json;

public class CustomField {
	@Json(name="customFieldFormat")
	private String _customFieldFormat;
	@Json(name="customLabelId")
	private Integer _customLabelId;
	@Json(name="data_field_type")
	private String _dataFieldType;
	@Json(name="dateEntered")
	private String _dateEntered;
	@Json(name="dependsOnCustomLabelId")
	private Integer _dependsOnCustomLabelId;
	@Json(name="dependsOnCustomLabelOperator")
	private Integer _dependsOnCustomLabelOperator;
	@Json(name="dependsOnCustomLabelValue")
	private String _dependsOnCustomLabelValue;
	@Json(name="isMatched")
	private Boolean _isMatched;
	@Json(name = "label")
	private String _label;
	@Json(name="predefinedValues")
	private String[] _predefinedValues;
	@Json(name="required")
	private Integer _required;
	@Json(name="tip")
	private String _tip;
	@Json(name = "value")
	private String _value;

	public CustomField() {
	}
	public String getCustomFieldFormat(){
		return _customFieldFormat;
	}

	public Integer getCustomLabelId(){
		return _customLabelId;
	}

	public String getDataFieldType(){
		return _dataFieldType;
	}

	public String getDateEntered(){
		return _dateEntered;
	}

	public Integer getDependsOnCustomLabelId(){
		return _dependsOnCustomLabelId;
	}

	public Integer getDependsOnCustomLabelOperator(){
		return _dependsOnCustomLabelOperator;
	}

	public String getDependsOnCustomLabelValue(){
		return _dependsOnCustomLabelValue;
	}

	public Boolean getIsMatched(){
		return _isMatched;
	}

	public String getLabel() {
		return _label;
	}

	public String[] getPredefinedValues(){
		return _predefinedValues;
	}

	public Integer getRequired(){
		return _required;
	}

	public String getTip(){
		return _tip;
	}

	public String getValue(){
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

}
