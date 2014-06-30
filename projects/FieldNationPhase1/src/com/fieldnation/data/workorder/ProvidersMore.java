package com.fieldnation.data.workorder;

import com.fieldnation.json.JsonObject;
import com.fieldnation.json.Serializer;

public class ProvidersMore {

	public ProvidersMore() {
	}

	public JsonObject toJson() {
		return toJson(this);
	}

	public static JsonObject toJson(ProvidersMore providersMore) {
		try {
			return Serializer.serializeObject(providersMore);
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}

	public static ProvidersMore fromJson(JsonObject json) {
		try {
			return Serializer.unserializeObject(ProvidersMore.class, json);
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}

}
