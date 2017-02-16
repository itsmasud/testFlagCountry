package com.fieldnation.v2.data.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.fieldnation.fnjson.JsonArray;
import com.fieldnation.fnjson.JsonObject;
import com.fieldnation.fnjson.Unserializer;
import com.fieldnation.fnjson.annotations.Json;
import com.fieldnation.fnjson.annotations.Source;
import com.fieldnation.fnlog.Log;

import java.text.ParseException;

/**
 * Created by dmgen from swagger.
 */

public class StoredLocation implements Parcelable {
    private static final String TAG = "StoredLocation";

    @Json(name = "actions")
    private ActionsEnum[] _actions;

    @Json(name = "active")
    private Boolean _active;

    @Json(name = "address1")
    private String _address1;

    @Json(name = "address2")
    private String _address2;

    @Json(name = "attributes")
    private LocationAttribute[] _attributes;

    @Json(name = "city")
    private String _city;

    @Json(name = "client")
    private Company _client;

    @Json(name = "company")
    private Company _company;

    @Json(name = "contact")
    private Contact _contact;

    @Json(name = "country")
    private String _country;

    @Json(name = "geo")
    private Coords _geo;

    @Json(name = "group")
    private LocationGroup _group;

    @Json(name = "id")
    private Integer _id;

    @Json(name = "name")
    private String _name;

    @Json(name = "notes")
    private LocationNote[] _notes;

    @Json(name = "state")
    private String _state;

    @Json(name = "time_zone")
    private TimeZone _timeZone;

    @Json(name = "type")
    private LocationType _type;

    @Json(name = "zip")
    private String _zip;

    @Source
    private JsonObject SOURCE = new JsonObject();

    public StoredLocation() {
    }

    public void setActions(ActionsEnum[] actions) throws ParseException {
        _actions = actions;
        JsonArray ja = new JsonArray();
        for (ActionsEnum item : actions) {
            ja.add(item.toString());
        }
        SOURCE.put("actions", ja);
    }

    public ActionsEnum[] getActions() {
        return _actions;
    }

    public StoredLocation actions(ActionsEnum[] actions) throws ParseException {
        _actions = actions;
        JsonArray ja = new JsonArray();
        for (ActionsEnum item : actions) {
            ja.add(item.toString());
        }
        SOURCE.put("actions", ja, true);
        return this;
    }

    public void setActive(Boolean active) throws ParseException {
        _active = active;
        SOURCE.put("active", active);
    }

    public Boolean getActive() {
        return _active;
    }

    public StoredLocation active(Boolean active) throws ParseException {
        _active = active;
        SOURCE.put("active", active);
        return this;
    }

    public void setAddress1(String address1) throws ParseException {
        _address1 = address1;
        SOURCE.put("address1", address1);
    }

    public String getAddress1() {
        return _address1;
    }

    public StoredLocation address1(String address1) throws ParseException {
        _address1 = address1;
        SOURCE.put("address1", address1);
        return this;
    }

    public void setAddress2(String address2) throws ParseException {
        _address2 = address2;
        SOURCE.put("address2", address2);
    }

    public String getAddress2() {
        return _address2;
    }

    public StoredLocation address2(String address2) throws ParseException {
        _address2 = address2;
        SOURCE.put("address2", address2);
        return this;
    }

    public void setAttributes(LocationAttribute[] attributes) throws ParseException {
        _attributes = attributes;
        SOURCE.put("attributes", LocationAttribute.toJsonArray(attributes));
    }

    public LocationAttribute[] getAttributes() {
        return _attributes;
    }

    public StoredLocation attributes(LocationAttribute[] attributes) throws ParseException {
        _attributes = attributes;
        SOURCE.put("attributes", LocationAttribute.toJsonArray(attributes), true);
        return this;
    }

    public void setCity(String city) throws ParseException {
        _city = city;
        SOURCE.put("city", city);
    }

    public String getCity() {
        return _city;
    }

    public StoredLocation city(String city) throws ParseException {
        _city = city;
        SOURCE.put("city", city);
        return this;
    }

    public void setClient(Company client) throws ParseException {
        _client = client;
        SOURCE.put("client", client.getJson());
    }

    public Company getClient() {
        return _client;
    }

    public StoredLocation client(Company client) throws ParseException {
        _client = client;
        SOURCE.put("client", client.getJson());
        return this;
    }

    public void setCompany(Company company) throws ParseException {
        _company = company;
        SOURCE.put("company", company.getJson());
    }

    public Company getCompany() {
        return _company;
    }

    public StoredLocation company(Company company) throws ParseException {
        _company = company;
        SOURCE.put("company", company.getJson());
        return this;
    }

    public void setContact(Contact contact) throws ParseException {
        _contact = contact;
        SOURCE.put("contact", contact.getJson());
    }

    public Contact getContact() {
        return _contact;
    }

    public StoredLocation contact(Contact contact) throws ParseException {
        _contact = contact;
        SOURCE.put("contact", contact.getJson());
        return this;
    }

    public void setCountry(String country) throws ParseException {
        _country = country;
        SOURCE.put("country", country);
    }

    public String getCountry() {
        return _country;
    }

    public StoredLocation country(String country) throws ParseException {
        _country = country;
        SOURCE.put("country", country);
        return this;
    }

    public void setGeo(Coords geo) throws ParseException {
        _geo = geo;
        SOURCE.put("geo", geo.getJson());
    }

    public Coords getGeo() {
        return _geo;
    }

    public StoredLocation geo(Coords geo) throws ParseException {
        _geo = geo;
        SOURCE.put("geo", geo.getJson());
        return this;
    }

    public void setGroup(LocationGroup group) throws ParseException {
        _group = group;
        SOURCE.put("group", group.getJson());
    }

    public LocationGroup getGroup() {
        return _group;
    }

    public StoredLocation group(LocationGroup group) throws ParseException {
        _group = group;
        SOURCE.put("group", group.getJson());
        return this;
    }

    public void setId(Integer id) throws ParseException {
        _id = id;
        SOURCE.put("id", id);
    }

    public Integer getId() {
        return _id;
    }

    public StoredLocation id(Integer id) throws ParseException {
        _id = id;
        SOURCE.put("id", id);
        return this;
    }

    public void setName(String name) throws ParseException {
        _name = name;
        SOURCE.put("name", name);
    }

    public String getName() {
        return _name;
    }

    public StoredLocation name(String name) throws ParseException {
        _name = name;
        SOURCE.put("name", name);
        return this;
    }

    public void setNotes(LocationNote[] notes) throws ParseException {
        _notes = notes;
        SOURCE.put("notes", LocationNote.toJsonArray(notes));
    }

    public LocationNote[] getNotes() {
        return _notes;
    }

    public StoredLocation notes(LocationNote[] notes) throws ParseException {
        _notes = notes;
        SOURCE.put("notes", LocationNote.toJsonArray(notes));
        return this;
    }

    public void setState(String state) throws ParseException {
        _state = state;
        SOURCE.put("state", state);
    }

    public String getState() {
        return _state;
    }

    public StoredLocation state(String state) throws ParseException {
        _state = state;
        SOURCE.put("state", state);
        return this;
    }

    public void setTimeZone(TimeZone timeZone) throws ParseException {
        _timeZone = timeZone;
        SOURCE.put("time_zone", timeZone.getJson());
    }

    public TimeZone getTimeZone() {
        return _timeZone;
    }

    public StoredLocation timeZone(TimeZone timeZone) throws ParseException {
        _timeZone = timeZone;
        SOURCE.put("time_zone", timeZone.getJson());
        return this;
    }

    public void setType(LocationType type) throws ParseException {
        _type = type;
        SOURCE.put("type", type.getJson());
    }

    public LocationType getType() {
        return _type;
    }

    public StoredLocation type(LocationType type) throws ParseException {
        _type = type;
        SOURCE.put("type", type.getJson());
        return this;
    }

    public void setZip(String zip) throws ParseException {
        _zip = zip;
        SOURCE.put("zip", zip);
    }

    public String getZip() {
        return _zip;
    }

    public StoredLocation zip(String zip) throws ParseException {
        _zip = zip;
        SOURCE.put("zip", zip);
        return this;
    }

    /*-******************************-*/
    /*-             Enums            -*/
    /*-******************************-*/
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

    /*-*****************************-*/
    /*-             Json            -*/
    /*-*****************************-*/
    public static JsonArray toJsonArray(StoredLocation[] array) {
        JsonArray list = new JsonArray();
        for (StoredLocation item : array) {
            list.add(item.getJson());
        }
        return list;
    }

    public static StoredLocation[] fromJsonArray(JsonArray array) {
        StoredLocation[] list = new StoredLocation[array.size()];
        for (int i = 0; i < array.size(); i++) {
            list[i] = fromJson(array.getJsonObject(i));
        }
        return list;
    }

    public static StoredLocation fromJson(JsonObject obj) {
        try {
            return Unserializer.unserializeObject(StoredLocation.class, obj);
        } catch (Exception ex) {
            Log.v(TAG, TAG, ex);
            return null;
        }
    }

    public JsonObject getJson() {
        return SOURCE;
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
        dest.writeParcelable(getJson(), flags);
    }
}
