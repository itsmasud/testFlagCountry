package com.fieldnation.data.bv2.model;

import com.fieldnation.fnjson.JsonObject;
import com.fieldnation.fnjson.Serializer;
import com.fieldnation.fnjson.Unserializer;
import com.fieldnation.fnjson.annotations.Json;
import com.fieldnation.fnlog.Log;

public class Location {
    private static final String TAG = "Location";

    @Json(name = "zip")
    private String _zip;

    @Json(name = "save_location_group")
    private Integer _saveLocationGroup;

    @Json(name = "country")
    private String _country;

    @Json(name = "role")
    private String _role;

    @Json(name = "address2")
    private String _address2;

    @Json(name = "city")
    private String _city;

    @Json(name = "saved_location")
    private StoredLocation _savedLocation;

    @Json(name = "address1")
    private String _address1;

    @Json(name = "coordinates")
    private Coords _coordinates;

    @Json(name = "type")
    private LocationType _type;

    @Json(name = "time_zone")
    private TimeZone _timeZone;

    @Json(name = "mode")
    private ModeEnum _mode;

    @Json(name = "status_id")
    private Integer _statusId;

    @Json(name = "save_location")
    private String _saveLocation;

    @Json(name = "work_order_id")
    private Integer _workOrderId;

    @Json(name = "correlation_id")
    private String _correlationId;

    @Json(name = "state")
    private String _state;

    @Json(name = "actions")
    private String _actions;

    @Json(name = "map")
    private Map _map;

    @Json(name = "contacts")
    private Contact[] _contacts;

    @Json(name = "validation")
    private LocationValidation _validation;

    public Location() {
    }

    public String getZip() {
        return _zip;
    }

    public Integer getSaveLocationGroup() {
        return _saveLocationGroup;
    }

    public String getCountry() {
        return _country;
    }

    public String getRole() {
        return _role;
    }

    public String getAddress2() {
        return _address2;
    }

    public String getCity() {
        return _city;
    }

    public StoredLocation getSavedLocation() {
        return _savedLocation;
    }

    public String getAddress1() {
        return _address1;
    }

    public Coords getCoordinates() {
        return _coordinates;
    }

    public LocationType getType() {
        return _type;
    }

    public TimeZone getTimeZone() {
        return _timeZone;
    }

    public ModeEnum getMode() {
        return _mode;
    }

    public Integer getStatusId() {
        return _statusId;
    }

    public String getSaveLocation() {
        return _saveLocation;
    }

    public Integer getWorkOrderId() {
        return _workOrderId;
    }

    public String getCorrelationId() {
        return _correlationId;
    }

    public String getState() {
        return _state;
    }

    public String getActions() {
        return _actions;
    }

    public Map getMap() {
        return _map;
    }

    public Contact[] getContacts() {
        return _contacts;
    }

    public LocationValidation getValidation() {
        return _validation;
    }

    /*-*****************************-*/
    /*-             Json            -*/
    /*-*****************************-*/
    public static Location fromJson(JsonObject obj) {
        try {
            return Unserializer.unserializeObject(Location.class, obj);
        } catch (Exception ex) {
            Log.v(TAG, TAG, ex);
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
            Log.v(TAG, TAG, ex);
            return null;
        }
    }
}
