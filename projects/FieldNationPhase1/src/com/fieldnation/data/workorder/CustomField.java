package com.fieldnation.data.workorder;

import com.fieldnation.json.JsonObject;
import com.fieldnation.json.Serializer;
import com.fieldnation.json.annotations.Json;

public class CustomField {
	@Json(name="customLabelId")
	private Integer _customLabelId;
	@Json(name="dependsOnCustomLabelOperator")
	private Integer _dependsOnCustomLabelOperator;
	@Json(name="dependsOnCustomLabelId")
	private Integer _dependsOnCustomLabelId;
	@Json(name="isMatched")
	private Boolean _isMatched;
	@Json(name="required")
	private Integer _required;
	@Json(name = "dateEntered")
	private String _dateEntered;
	@Json(name="customFieldFormat")
	private String _customFieldFormat;
	@Json(name = "label")
	private String _label;
	@Json(name="tip")
	private String _tip;
	@Json(name = "value")
	private String _value;
	@Json(name="dependsOnCustomLabelValue")
	private String _dependsOnCustomLabelValue;
	@Json(name="predefinedValues")
	private String[] _predefinedValues;
	@Json(name="data_field_type")
	private String _dataFieldType;

	public CustomField() {
	}
	public Integer getCustomLabelId(){
		return _customLabelId;
	}

	public Integer getDependsOnCustomLabelOperator(){
		return _dependsOnCustomLabelOperator;
	}

	public Integer getDependsOnCustomLabelId(){
		return _dependsOnCustomLabelId;
	}

	public Boolean getIsMatched(){
		return _isMatched;
	}

	public Integer getRequired(){
		return _required;
	}

	public Object getDateEntered(){
		return _dateEntered;
	}

	public String getCustomFieldFormat(){
		return _customFieldFormat;
	}

	public String getLabel() {
		return _label;
	}

	public String getTip(){
		return _tip;
	}

	public String getValue() {
		return _value;
	}

	public String getDependsOnCustomLabelValue(){
		return _dependsOnCustomLabelValue;
	}

	public String[] getPredefinedValues(){
		return _predefinedValues;
	}

	public String getDataFieldType(){
		return _dataFieldType;
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
