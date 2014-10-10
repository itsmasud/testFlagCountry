package com.fieldnation.data.profile;

import com.fieldnation.data.workorder.Status;
import com.fieldnation.json.JsonObject;
import com.fieldnation.json.Serializer;
import com.fieldnation.json.annotations.Json;

public class Workorder {
	@Json(name = "status")
	private Status _status;
	@Json(name = "workorderId")
	private Long _workorderId;
	@Json(name = "title")
	private String _title;

	public Workorder() {
	}

	public Status getStatus() {
		return _status;
	}

	public Long getWorkorderId() {
		return _workorderId;
	}

	public String getTitle() {
		return _title;
	}

	public JsonObject toJson() {
		return toJson(this);
	}

	public static JsonObject toJson(Workorder workorder) {
		try {
			return Serializer.serializeObject(workorder);
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}

	public static Workorder fromJson(JsonObject json) {
		try {
			return Serializer.unserializeObject(Workorder.class, json);
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}
}
