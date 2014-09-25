package com.fieldnation.data.profile;

import com.fieldnation.json.JsonObject;
import com.fieldnation.json.Serializer;
import com.fieldnation.json.annotations.Json;

public class Profile{
	@Json(name="state")
	private String _state;
	@Json(name="photo")
	private Photo _photo;
	@Json(name="workordersTotal")
	private Integer _workordersTotal;
	@Json(name="rating")
	private Double _rating;
	@Json(name="city")
	private String _city;
	@Json(name="unreadMessageCount")
	private Integer _unreadMessageCount;
	@Json(name="newNotificationCount")
	private Integer _newNotificationCount;
	@Json(name="marketplaceStatusOn")
	private Boolean _marketplaceStatusOn;
	@Json(name="firstname")
	private String _firstname;
	@Json(name="userId")
	private int _userId;
	@Json(name="lastname")
	private String _lastname;
	@Json(name="phone")
	private String _phone;
	@Json(name="email")
	private String _email;
	@Json(name="ratingsTotal")
	private Integer _ratingsTotal;
	@Json(name="tagline")
	private String _tagline;
	@Json(name="description")
	private String _description;

	public Profile(){
	}
	public String getState(){
		return _state;
	}

	public Photo getPhoto(){
		return _photo;
	}

	public Integer getWorkordersTotal(){
		return _workordersTotal;
	}

	public Double getRating(){
		return _rating;
	}

	public String getCity(){
		return _city;
	}

	public Integer getUnreadMessageCount(){
		return _unreadMessageCount;
	}

	public Integer getNewNotificationCount(){
		return _newNotificationCount;
	}

	public Boolean getMarketplaceStatusOn(){
		return _marketplaceStatusOn;
	}

	public String getFirstname(){
		return _firstname;
	}

	public int getUserId(){
		return _userId;
	}

	public String getLastname(){
		return _lastname;
	}

	public String getPhone(){
		return _phone;
	}

	public String getEmail(){
		return _email;
	}

	public Integer getRatingsTotal(){
		return _ratingsTotal;
	}

	public String getTagline(){
		return _tagline;
	}

	public String getDescription(){
		return _description;
	}

	public JsonObject toJson(){
		return toJson(this);
	}

	public static JsonObject toJson(Profile profile) {
		try {
			return Serializer.serializeObject(profile);
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}

	public static Profile fromJson(JsonObject json) {
		try {
			return Serializer.unserializeObject(Profile.class, json);
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}

}
