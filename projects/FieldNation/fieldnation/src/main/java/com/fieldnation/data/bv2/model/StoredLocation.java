package com.fieldnation.data.bv2.model;

import com.fieldnation.fnjson.JsonObject;
import com.fieldnation.fnjson.Serializer;
import com.fieldnation.fnjson.Unserializer;
import com.fieldnation.fnjson.annotations.Json;
import com.fieldnation.fnlog.Log;

public class StoredLocation {
    private static final String TAG = "StoredLocation";

    @Json(name = "id")
    private Integer id = null;

    @Json(name = "name")
    private String name = null;

    @Json(name = "company")
    private Company company = null;

    @Json(name = "group")
    private LocationGroup group = null;

    @Json(name = "type")
    private LocationType type = null;

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

    @Json(name = "time_zone")
    private TimeZone timeZone = null;

    @Json(name = "contact")
    private Contact contact = null;

    @Json(name = "notes")
    private LocationNote[] notes = null;

    @Json(name = "active")
    private Boolean active = null;

    @Json(name = "client")
    private Company client = null;

    @Json(name = "geo")
    private Coords geo = null;

    @Json(name = "attributes")
    private LocationAttribute[] attributes;

    @Json(name = "actions")
    private ActionsEnum[] actions;

    public enum ActionsEnum {
        @Json(name = "edit")
        EDIT("edit");

        private String value;

        ActionsEnum(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return String.valueOf(value);
        }
    }

    public StoredLocation() {
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Company getCompany() {
        return company;
    }

    public LocationGroup getGroup() {
        return group;
    }

    public LocationType getType() {
        return type;
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

    public TimeZone getTimeZone() {
        return timeZone;
    }

    public Contact getContact() {
        return contact;
    }

    public LocationNote[] getNotes() {
        return notes;
    }

    public Boolean getActive() {
        return active;
    }

    public Company getClient() {
        return client;
    }

    public Coords getGeo() {
        return geo;
    }

    public LocationAttribute[] getAttributes() {
        return attributes;
    }

    public ActionsEnum[] getActions() {
        return actions;
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