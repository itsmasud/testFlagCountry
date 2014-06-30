package com.fieldnation.data.workorder;

import com.fieldnation.json.annotations.Json;

public class Label{
	@Json(name="name")
	private String _name;
	@Json(name="type")
	private String _type;
	@Json(name="label_id")
	private int _labelId;
	@Json(name="description")
	private String _description;
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

	public int getLabelId(){
		return _labelId;
	}

	public String getDescription(){
		return _description;
	}

	public String getAction(){
		return _action;
	}

}
