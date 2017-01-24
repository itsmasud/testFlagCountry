package com.fieldnation.data.bv2.model;

import com.fieldnation.fnjson.JsonObject;
import com.fieldnation.fnjson.Serializer;
import com.fieldnation.fnjson.Unserializer;
import com.fieldnation.fnjson.annotations.Json;
import com.fieldnation.fnlog.Log;

public class StoredLocation {
    private static final String TAG = "StoredLocation";

    @Json(name = "zip")
    private String zip;

    @Json(name = "country")
    private String country;

    @Json(name = "notes")
    private LocationNote[] notes;

    @Json(name = "address2")
    private String address2;

    @Json(name = "city")
    private String city;

    @Json(name = "address1")
    private String address1;

    @Json(name = "active")
    private Boolean active;

    @Json(name = "type")
    private LocationType type;

    @Json(name = "time_zone")
    private TimeZone timeZone;

    @Json(name = "geo")
    private Coords geo;

    @Json(name = "contact")
    private Contact contact;

    @Json(name = "name")
    private String name;

    @Json(name = "client")
    private Company client;

    @Json(name = "company")
    private Company company;

    @Json(name = "attributes")
    private LocationAttribute[] attributes;

    @Json(name = "id")
    private Integer id;

    @Json(name = "state")
    private String state;

    @Json(name = "actions")
    private ActionsEnum[] actions;

    @Json(name = "group")
    private LocationGroup group;

    public StoredLocation() {
    }

    public String getZip() {
        return zip;
    }

    public String getCountry() {
        return country;
    }

    public LocationNote[] getNotes() {
        return notes;
    }

    public String getAddress2() {
        return address2;
    }

    public String getCity() {
        return city;
    }

    public String getAddress1() {
        return address1;
    }

    public Boolean getActive() {
        return active;
    }

    public LocationType getType() {
        return type;
    }

    public TimeZone getTimeZone() {
        return timeZone;
    }

    public Coords getGeo() {
        return geo;
    }

    public Contact getContact() {
        return contact;
    }

    public String getName() {
        return name;
    }

    public Company getClient() {
        return client;
    }

    public Company getCompany() {
        return company;
    }

    public LocationAttribute[] getAttributes() {
        return attributes;
    }

    public Integer getId() {
        return id;
    }

    public String getState() {
        return state;
    }

    public ActionsEnum[] getActions() {
        return actions;
    }

    public LocationGroup getGroup() {
        return group;
    }

    /*-*****************************-*/
    /*-             Json            -*/
    /*-*****************************-*/
    public static StoredLocation fromJson(JsonObject obj) {
        try {
            return Unserializer.unserializeObject(StoredLocation.class, obj);
        } catch (Exception ex) {
            Log.v(TAG, ex);
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
            Log.v(TAG, ex);
            return null;
        }
    }
}
