package com.fieldnation.data.bv2.model;

import com.fieldnation.fnjson.JsonObject;
import com.fieldnation.fnjson.Serializer;
import com.fieldnation.fnjson.Unserializer;
import com.fieldnation.fnjson.annotations.Json;
import com.fieldnation.fnlog.Log;

public class Location {
    private static final String TAG = "Location";

    @Json(name = "zip")
    private String zip;

    @Json(name = "save_location_group")
    private Integer saveLocationGroup;

    @Json(name = "country")
    private String country;

    @Json(name = "role")
    private String role;

    @Json(name = "address2")
    private String address2;

    @Json(name = "city")
    private String city;

    @Json(name = "saved_location")
    private StoredLocation savedLocation;

    @Json(name = "address1")
    private String address1;

    @Json(name = "coordinates")
    private Coords coordinates;

    @Json(name = "type")
    private LocationType type;

    @Json(name = "time_zone")
    private TimeZone timeZone;

    @Json(name = "mode")
    private ModeEnum mode;

    @Json(name = "status_id")
    private Integer statusId;

    @Json(name = "save_location")
    private String saveLocation;

    @Json(name = "work_order_id")
    private Integer workOrderId;

    @Json(name = "correlation_id")
    private String correlationId;

    @Json(name = "state")
    private String state;

    @Json(name = "actions")
    private String actions;

    @Json(name = "map")
    private Map map;

    @Json(name = "contacts")
    private Contact[] contacts;

    @Json(name = "validation")
    private LocationValidation validation;

    public Location() {
    }

    public String getZip() {
        return zip;
    }

    public Integer getSaveLocationGroup() {
        return saveLocationGroup;
    }

    public String getCountry() {
        return country;
    }

    public String getRole() {
        return role;
    }

    public String getAddress2() {
        return address2;
    }

    public String getCity() {
        return city;
    }

    public StoredLocation getSavedLocation() {
        return savedLocation;
    }

    public String getAddress1() {
        return address1;
    }

    public Coords getCoordinates() {
        return coordinates;
    }

    public LocationType getType() {
        return type;
    }

    public TimeZone getTimeZone() {
        return timeZone;
    }

    public ModeEnum getMode() {
        return mode;
    }

    public Integer getStatusId() {
        return statusId;
    }

    public String getSaveLocation() {
        return saveLocation;
    }

    public Integer getWorkOrderId() {
        return workOrderId;
    }

    public String getCorrelationId() {
        return correlationId;
    }

    public String getState() {
        return state;
    }

    public String getActions() {
        return actions;
    }

    public Map getMap() {
        return map;
    }

    public Contact[] getContacts() {
        return contacts;
    }

    public LocationValidation getValidation() {
        return validation;
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
