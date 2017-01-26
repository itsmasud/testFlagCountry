package com.fieldnation.data.bv2.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.fieldnation.fnjson.JsonObject;
import com.fieldnation.fnjson.Serializer;
import com.fieldnation.fnjson.Unserializer;
import com.fieldnation.fnjson.annotations.Json;
import com.fieldnation.fnlog.Log;

/**
 * Created by dmgen from swagger on 1/26/17.
 */

public class StoredLocation implements Parcelable {
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

    public void setZip(String zip) {
        _zip = zip;
    }

    public String getZip() {
        return _zip;
    }

    public StoredLocation zip(String zip) {
        _zip = zip;
        return this;
    }

    public void setCountry(String country) {
        _country = country;
    }

    public String getCountry() {
        return _country;
    }

    public StoredLocation country(String country) {
        _country = country;
        return this;
    }

    public void setNotes(LocationNote[] notes) {
        _notes = notes;
    }

    public LocationNote[] getNotes() {
        return _notes;
    }

    public StoredLocation notes(LocationNote[] notes) {
        _notes = notes;
        return this;
    }

    public void setAddress2(String address2) {
        _address2 = address2;
    }

    public String getAddress2() {
        return _address2;
    }

    public StoredLocation address2(String address2) {
        _address2 = address2;
        return this;
    }

    public void setCity(String city) {
        _city = city;
    }

    public String getCity() {
        return _city;
    }

    public StoredLocation city(String city) {
        _city = city;
        return this;
    }

    public void setAddress1(String address1) {
        _address1 = address1;
    }

    public String getAddress1() {
        return _address1;
    }

    public StoredLocation address1(String address1) {
        _address1 = address1;
        return this;
    }

    public void setActive(Boolean active) {
        _active = active;
    }

    public Boolean getActive() {
        return _active;
    }

    public StoredLocation active(Boolean active) {
        _active = active;
        return this;
    }

    public void setType(LocationType type) {
        _type = type;
    }

    public LocationType getType() {
        return _type;
    }

    public StoredLocation type(LocationType type) {
        _type = type;
        return this;
    }

    public void setTimeZone(TimeZone timeZone) {
        _timeZone = timeZone;
    }

    public TimeZone getTimeZone() {
        return _timeZone;
    }

    public StoredLocation timeZone(TimeZone timeZone) {
        _timeZone = timeZone;
        return this;
    }

    public void setGeo(Coords geo) {
        _geo = geo;
    }

    public Coords getGeo() {
        return _geo;
    }

    public StoredLocation geo(Coords geo) {
        _geo = geo;
        return this;
    }

    public void setContact(Contact contact) {
        _contact = contact;
    }

    public Contact getContact() {
        return _contact;
    }

    public StoredLocation contact(Contact contact) {
        _contact = contact;
        return this;
    }

    public void setName(String name) {
        _name = name;
    }

    public String getName() {
        return _name;
    }

    public StoredLocation name(String name) {
        _name = name;
        return this;
    }

    public void setClient(Company client) {
        _client = client;
    }

    public Company getClient() {
        return _client;
    }

    public StoredLocation client(Company client) {
        _client = client;
        return this;
    }

    public void setCompany(Company company) {
        _company = company;
    }

    public Company getCompany() {
        return _company;
    }

    public StoredLocation company(Company company) {
        _company = company;
        return this;
    }

    public void setAttributes(LocationAttribute[] attributes) {
        _attributes = attributes;
    }

    public LocationAttribute[] getAttributes() {
        return _attributes;
    }

    public StoredLocation attributes(LocationAttribute[] attributes) {
        _attributes = attributes;
        return this;
    }

    public void setId(Integer id) {
        _id = id;
    }

    public Integer getId() {
        return _id;
    }

    public StoredLocation id(Integer id) {
        _id = id;
        return this;
    }

    public void setState(String state) {
        _state = state;
    }

    public String getState() {
        return _state;
    }

    public StoredLocation state(String state) {
        _state = state;
        return this;
    }

    public void setActions(ActionsEnum[] actions) {
        _actions = actions;
    }

    public ActionsEnum[] getActions() {
        return _actions;
    }

    public StoredLocation actions(ActionsEnum[] actions) {
        _actions = actions;
        return this;
    }

    public void setGroup(LocationGroup group) {
        _group = group;
    }

    public LocationGroup getGroup() {
        return _group;
    }

    public StoredLocation group(LocationGroup group) {
        _group = group;
        return this;
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

    /*-*********************************************-*/
    /*-			Parcelable Implementation           -*/
    /*-*********************************************-*/
    public static final Parcelable.Creator<StoredLocation> CREATOR = new Parcelable.Creator<StoredLocation>() {

        @Override
        public StoredLocation createFromParcel(Parcel source) {
            try {
                return StoredLocation.fromJson((JsonObject) source.readParcelable(JsonObject.class.getClassLoader()));
            } catch (Exception ex) {
                Log.v(TAG, ex);
                return null;
            }
        }

        @Override
        public StoredLocation[] newArray(int size) {
            return new StoredLocation[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(toJson(), flags);
    }
}
