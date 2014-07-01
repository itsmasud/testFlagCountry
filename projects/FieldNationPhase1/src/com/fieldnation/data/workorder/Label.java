package com.fieldnation.data.workorder;

import com.fieldnation.json.JsonObject;
import com.fieldnation.json.Serializer;
import com.fieldnation.json.annotations.Json;

public class Label{
	@Json(name="name")
	private String _name;
	@Json(name="type")
	private String _type;
	@Json(name="description")
	private String _description;
	@Json(name="label_id")
	private int _labelId;
	@Json(name="action")
	private String _action;

	public Label(){
	}
	public String getName(){
		return _name;
	}

	public String getType(){
		return _type;
	}

	public String getDescription(){
		return _description;
	}

	public int getLabelId(){
		return _labelId;
	}

	public String getAction(){
		return _action;
	}

	public JsonObject toJson(){
		return toJson(this);
	}

	public static JsonObject toJson(Label label) {
		try {
			return Serializer.serializeObject(label);
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}

	public static Label fromJson(JsonObject json) {
		try {
			return Serializer.unserializeObject(Label.class, json);
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}

}
