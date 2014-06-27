package com.fieldnation.data.workorder;

import java.text.ParseException;

import com.fieldnation.json.JsonObject;

public class Location {
	private String _state;
	private String _address2;
	private String _address1;
	private String _contactPhoneExt;
	private int _zip;
	private String _country;
	private String _city;
	private String _contactName;
	private String _contactPhone;
	private String _contactEmail;

	public Location(JsonObject json) throws ParseException {
		if (json.has("state")) {
			_state = json.getString("state");
		}
		if (json.has("address2")) {
			_address2 = json.getString("address2");
		}
		if (json.has("address1")) {
			_address1 = json.getString("address1");
		}
		if (json.has("contact_phone_ext")) {
			_contactPhoneExt = json.getString("contact_phone_ext");
		}
		if (json.has("zip")) {
			_zip = json.getInt("zip");
		}
		if (json.has("state")) {
			_state = json.getString("state");
		}

	}
}
