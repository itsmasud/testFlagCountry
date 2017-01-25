package com.fieldnation.data.bv2.model;

import com.fieldnation.fnjson.JsonObject;
import com.fieldnation.fnjson.Serializer;
import com.fieldnation.fnjson.Unserializer;
import com.fieldnation.fnjson.annotations.Json;
import com.fieldnation.fnlog.Log;

public class StoredLocation {
    private static final String TAG = "StoredLocation";

    @Json(name = "zip")
    private String _zip;

    @Json(name = "country")
    private String _country;

    @Json(name = "notes")
    private LocationNote[] _notes;

    @Json(name = "address2")
    private String _address2;

    @Json(name = "city")
    private String _city;

    @Json(name = "address1")
    private String _address1;

    @Json(name = "active")
    private Boolean _active;

    @Json(name = "type")
    private LocationType _type;

    @Json(name = "time_zone")
    private TimeZone _timeZone;

    @Json(name = "geo")
    private Coords _geo;

    @Json(name = "contact")
    private Contact _contact;

    @Json(name = "name")
    private String _name;

    @Json(name = "client")
    private Company _client;

    @Json(name = "company")
    private Company _company;

    @Json(name = "attributes")
    private LocationAttribute[] _attributes;

    @Json(name = "id")
    private Integer _id;

    @Json(name = "state")
    private String _state;

    @Json(name = "actions")
    private ActionsEnum[] _actions;

    @Json(name = "group")
    private LocationGroup _group;

    public StoredLocation() {
    }

    public String getZip() {
        return _zip;
    }

    public String getCountry() {
        return _country;
    }

    public LocationNote[] getNotes() {
        return _notes;
    }

    public String getAddress2() {
        return _address2;
    }

    public String getCity() {
        return _city;
    }

    public String getAddress1() {
        return _address1;
    }

    public Boolean getActive() {
        return _active;
    }

    public LocationType getType() {
        return _type;
    }

    public TimeZone getTimeZone() {
        return _timeZone;
    }

    public Coords getGeo() {
        return _geo;
    }

    public Contact getContact() {
        return _contact;
    }

    public String getName() {
        return _name;
    }

    public Company getClient() {
        return _client;
    }

    public Company getCompany() {
        return _company;
    }

    public LocationAttribute[] getAttributes() {
        return _attributes;
    }

    public Integer getId() {
        return _id;
    }

    public String getState() {
        return _state;
    }

    public ActionsEnum[] getActions() {
        return _actions;
    }

    public LocationGroup getGroup() {
        return _group;
    }

    /*-*****************************-*/
    /*-             Json            -*/
    /*-*****************************-*/
    public static StoredLocation fromJson(JsonObject obj) {
        try {
            return Unserializer.unserializeObject(StoredLocation.class, obj);
        } catch (Exception ex) {
            Log.v(TAG, TAG, ex);
            return null;
        }
    }

    public JsonObject toJson() {
        return toJson(this);
    }

    public static JsonObject toJson(StoredLocation storedLocation) {
        try {
            return Serializer.serializeObject(storedLocation);
        } catch (Exception ex) {
            Log.v(TAG, TAG, ex);
            return null;
        }
    }
}
