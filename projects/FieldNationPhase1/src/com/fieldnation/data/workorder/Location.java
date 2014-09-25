package com.fieldnation.data.workorder;

import com.fieldnation.json.JsonObject;
import com.fieldnation.json.Serializer;
import com.fieldnation.json.annotations.Json;
import com.fieldnation.utils.misc;

public class Location {
	@Json(name = "name")
	private String _name;
	@Json(name = "distance")
	private Double _distance;
	@Json(name = "state")
	private String _state;
	@Json(name = "address2")
	private String _address2;
	@Json(name = "address1")
	private String _address1;
	@Json(name = "zip")
	private String _zip;
	@Json(name = "contactPhone")
	private String _contactPhone;
	@Json(name = "country")
	private String _country;
	@Json(name = "contactEmail")
	private String _contactEmail;
	@Json(name = "city")
	private String _city;
	@Json(name = "distanceMapUrl")
	private String _distanceMapUrl;
	@Json(name = "notes")
	private String _notes;
	@Json(name = "contactPhoneExt")
	private Integer _contactPhoneExt;
	@Json(name = "type")
	private String _type;
	@Json(name = "checkInCheckOutMapUrl")
	private String _checkInCheckOutMapUrl;
	@Json(name = "contactName")
	private String _contactName;

	public Location() {
	}

	public String getName() {
		return _name;
	}

	public Double getDistance() {
		return _distance;
	}

	public String getState() {
		return _state;
	}

	public String getAddress2() {
		return _address2;
	}

	public String getAddress1() {
		return _address1;
	}

	public String getZip() {
		return _zip;
	}

	public String getContactPhone() {
		return _contactPhone;
	}

	public String getCountry() {
		return _country;
	}

	public String getContactEmail() {
		return _contactEmail;
	}

	public String getCity() {
		return _city;
	}

	public String getDistanceMapUrl() {
		return _distanceMapUrl;
	}

	public String getNotes() {
		return _notes;
	}

	public Integer getContactPhoneExt() {
		return _contactPhoneExt;
	}

	public String getType() {
		return _type;
	}

	public String getCheckInCheckOutMapUrl() {
		return _checkInCheckOutMapUrl;
	}

	public String getContactName() {
		return _contactName;
	}

	public JsonObject toJson() {
		return toJson(this);
	}

	public static JsonObject toJson(Location location) {
		try {
			return Serializer.serializeObject(location);
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}

	public static Location fromJson(JsonObject json) {
		try {
			return Serializer.unserializeObject(Location.class, json);
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}

	/*-*************************************************-*/
	/*-				Human Generated Code				-*/
	/*-*************************************************-*/

	public String getTopAddressLine() {
		if (getAddress1() != null || getAddress2() != null) {
			String address1 = null;
			String address2 = null;

			if (getAddress1() != null)
				address1 = getAddress1();
			if (getAddress2() != null)
				address2 = getAddress2();

			if (misc.isEmptyOrNull(address1))
				address1 = null;
			if (misc.isEmptyOrNull(address2))
				address2 = null;

			if (address1 == null)
				address1 = address2;
			else if (address2 != null) {
				address1 = (address1 + "\n" + address2).trim();
			}

			if (address1 != null) {
				return address1;
			} else {
				return "";
			}
		} else {
			return "";
		}
	}

	public String getFullAddress() {
		String address = "";

		if (misc.isEmptyOrNull(getTopAddressLine())) {
			address = getTopAddressLine() + "\n";
		}

		if (!misc.isEmptyOrNull(_city) && !misc.isEmptyOrNull(_state) && !misc.isEmptyOrNull(_zip) && !misc.isEmptyOrNull(_country)) {
			address += _city + ", " + _state + " " + _zip + "\n";
			address += _country;
		}

		return address.trim();
	}
}
