package com.fieldnation.data.bv2.model;

import com.fieldnation.fnjson.JsonObject;
import com.fieldnation.fnjson.Serializer;
import com.fieldnation.fnjson.Unserializer;
import com.fieldnation.fnjson.annotations.Json;
import com.fieldnation.fnlog.Log;

public class Location {
    private static final String TAG = "Location";

    @Json(name = "work_order_id")
    private Integer workOrderId = null;

    @Json(name = "mode")
    private ModeEnum mode = null;

    @Json(name = "role")
    private String role = null;

    @Json(name = "actions")
    private String actions = null;

    @Json(name = "correlation_id")
    private String correlationId = null;

    @Json(name = "status_id")
    private Integer statusId = null;

    @Json(name = "address1")
    private String address1 = null;

    @Json(name = "address2")
    private String address2 = null;

    @Json(name = "city")
    private String city = null;

    @Json(name = "state")
    private String state = null;

    @Json(name = "zip")
    private String zip = null;

    @Json(name = "country")
    private String country = null;

    @Json(name = "coordinates")
    private Coords coordinates = null;

    @Json(name = "type")
    private UserBy type = null;

    @Json(name = "save_location")
    private String saveLocation = null;

    @Json(name = "save_location_group")
    private Integer saveLocationGroup = null;

    @Json(name = "saved_location")
    private StoredLocation savedLocation = null;

    @Json(name = "time_zone")
    private TimeZone timeZone = null;

    @Json(name = "contacts")
    private Contact[] contacts;

    // TODO not sure what this data type is
    @Json(name = "map")
    private String map = null;

    @Json(name = "validation")
    private LocationValidation validation = null;

    public enum ModeEnum {
        @Json(name = "custom")
        CUSTOM("custom"),
        @Json(name = "remote")
        REMOTE("remote"),
        @Json(name = "location")
        LOCATION("location");

        private String value;

        ModeEnum(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return String.valueOf(value);
        }
    }

    public Location() {
    }

    public Integer getWorkOrderId() {
        return workOrderId;
    }

    public ModeEnum getMode() {
        return mode;
    }

    public String getRole() {
        return role;
    }

    public String getActions() {
        return actions;
    }

    public String getCorrelationId() {
        return correlationId;
    }

    public Integer getStatusId() {
        return statusId;
    }

    public String getAddress1() {
        return address1;
    }

    public String getAddress2() {
        return address2;
    }

    public String getCity() {
        return city;
    }

    public String getState() {
        return state;
    }

    public String getZip() {
        return zip;
    }

    public String getCountry() {
        return country;
    }

    public Coords getCoordinates() {
        return coordinates;
    }

    public UserBy getType() {
        return type;
    }

    public String getSaveLocation() {
        return saveLocation;
    }

    public Integer getSaveLocationGroup() {
        return saveLocationGroup;
    }

    public StoredLocation getSavedLocation() {
        return savedLocation;
    }

    public TimeZone getTimeZone() {
        return timeZone;
    }

    public Contact[] getContacts() {
        return contacts;
    }

    public String getMap() {
        return map;
    }

    /*-*****************************-*/
    /*-             Json            -*/
    /*-*****************************-*/
    public static Location fromJson(JsonObject obj) {
        try {
            return Unserializer.unserializeObject(Location.class, obj);
        } catch (Exception ex) {
            Log.v(TAG, ex);
            return null;
        }
    }

    public JsonObject toJson() {
        return toJson(this);
    }

    public static JsonObject toJson(Location location) {
        try {
            return Serializer.serializeObject(location);
        } catch (Exception ex) {
            Log.v(TAG, ex);
            return null;
        }
    }
}