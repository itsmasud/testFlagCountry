package com.fieldnation.data.workorder;

import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;

import com.fieldnation.json.JsonObject;
import com.fieldnation.json.Serializer;
import com.fieldnation.json.UnsupportedDataTypeException;
import com.fieldnation.json.annotations.Json;

public class Workorder {
	@Json(name = "workorder_id")
	private int _workorderId;

	@Json(name = "location")
	private Location _location;

	@Json(name = "statusId")
	private int _statusId;

	@Json(name = "distance")
	private float _distance;

	@Json(name = "providersPhoto")
	private ProviderPhoto _providersPhoto;

	@Json(name = "label")
	private Label[] _labels;

	@Json(name = "pay")
	private Pay _pay;

	@Json(name = "provider")
	private Provider[] _providers;

	@Json(name = "title")
	private String _title;

	/**
	 * never call this, call fromJson instead
	 */
	public Workorder() {
	}

	public JsonObject toJson() {
		return toJson(this);
	}

	public static JsonObject toJson(Workorder wo) {
		try {
			return Serializer.serializeObject(wo);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static Workorder fromJson(JsonObject json) {
		try {
			return Serializer.unserializeObject(Workorder.class, json);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}
