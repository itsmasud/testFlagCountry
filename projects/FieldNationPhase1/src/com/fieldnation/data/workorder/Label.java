package com.fieldnation.data.workorder;

import java.text.ParseException;

import com.fieldnation.json.JsonObject;

public class Label {
	private String _name;
	private int _labelId;
	private String _description;
	private String _action;

	public Label(JsonObject label) throws ParseException {
		_name = label.getString("name");
		_labelId = label.getInt("label_id");

		if (label.has("description")) {
			_description = label.getString("description");
		}

		if (label.has("action")) {
			_action = label.getString("action");
		}
	}

	public String getAction() {
		return _action;
	}

	public String getDescription() {
		return _description;
	}

	public int getLabelId() {
		return _labelId;
	}

	public String getName() {
		return _name;
	}

}
