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
    private JsonObject SOURCE;

    public StoredLocation() {
        SOURCE = new JsonObject();
    }

    public StoredLocation(JsonObject obj) {
        SOURCE = obj;
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
        try {
            if (_actions != null)
                return _actions;

            if (SOURCE.has("actions") && SOURCE.get("actions") != null) {
                _actions = ActionsEnum.fromJsonArray(SOURCE.getJsonArray("actions"));
            }

        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

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
        try {
            if (_active != null)
                return _active;

            if (SOURCE.has("active") && SOURCE.get("active") != null)
                _active = SOURCE.getBoolean("active");

        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

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
        try {
            if (_address1 != null)
                return _address1;

            if (SOURCE.has("address1") && SOURCE.get("address1") != null)
                _address1 = SOURCE.getString("address1");

        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

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
        try {
            if (_address2 != null)
                return _address2;

            if (SOURCE.has("address2") && SOURCE.get("address2") != null)
                _address2 = SOURCE.getString("address2");

        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

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
        try {
            if (_attributes != null)
                return _attributes;

            if (SOURCE.has("attributes") && SOURCE.get("attributes") != null) {
                _attributes = LocationAttribute.fromJsonArray(SOURCE.getJsonArray("attributes"));
            }

        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

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
        try {
            if (_city != null)
                return _city;

            if (SOURCE.has("city") && SOURCE.get("city") != null)
                _city = SOURCE.getString("city");

        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

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
        try {
            if (_client != null)
                return _client;

            if (SOURCE.has("client") && SOURCE.get("client") != null)
                _client = Company.fromJson(SOURCE.getJsonObject("client"));

        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

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
        try {
            if (_company != null)
                return _company;

            if (SOURCE.has("company") && SOURCE.get("company") != null)
                _company = Company.fromJson(SOURCE.getJsonObject("company"));

        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

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
        try {
            if (_contact != null)
                return _contact;

            if (SOURCE.has("contact") && SOURCE.get("contact") != null)
                _contact = Contact.fromJson(SOURCE.getJsonObject("contact"));

        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

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
        try {
            if (_country != null)
                return _country;

            if (SOURCE.has("country") && SOURCE.get("country") != null)
                _country = SOURCE.getString("country");

        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

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
        try {
            if (_geo != null)
                return _geo;

            if (SOURCE.has("geo") && SOURCE.get("geo") != null)
                _geo = Coords.fromJson(SOURCE.getJsonObject("geo"));

        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

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
        try {
            if (_group != null)
                return _group;

            if (SOURCE.has("group") && SOURCE.get("group") != null)
                _group = LocationGroup.fromJson(SOURCE.getJsonObject("group"));

        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

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
        try {
            if (_id != null)
                return _id;

            if (SOURCE.has("id") && SOURCE.get("id") != null)
                _id = SOURCE.getInt("id");

        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

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
        try {
            if (_name != null)
                return _name;

            if (SOURCE.has("name") && SOURCE.get("name") != null)
                _name = SOURCE.getString("name");

        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

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
        try {
            if (_notes != null)
                return _notes;

            if (SOURCE.has("notes") && SOURCE.get("notes") != null) {
                _notes = LocationNote.fromJsonArray(SOURCE.getJsonArray("notes"));
            }

        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _notes;
    }

    public StoredLocation notes(LocationNote[] notes) throws ParseException {
        _notes = notes;
        SOURCE.put("notes", LocationNote.toJsonArray(notes), true);
        return this;
    }

    public void setState(String state) throws ParseException {
        _state = state;
        SOURCE.put("state", state);
    }

    public String getState() {
        try {
            if (_state != null)
                return _state;

            if (SOURCE.has("state") && SOURCE.get("state") != null)
                _state = SOURCE.getString("state");

        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

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
        try {
            if (_timeZone != null)
                return _timeZone;

            if (SOURCE.has("time_zone") && SOURCE.get("time_zone") != null)
                _timeZone = TimeZone.fromJson(SOURCE.getJsonObject("time_zone"));

        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

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
        try {
            if (_type != null)
                return _type;

            if (SOURCE.has("type") && SOURCE.get("type") != null)
                _type = LocationType.fromJson(SOURCE.getJsonObject("type"));

        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

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
        try {
            if (_zip != null)
                return _zip;

            if (SOURCE.has("zip") && SOURCE.get("zip") != null)
                _zip = SOURCE.getString("zip");

        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

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

        public static ActionsEnum fromString(String value) {
            ActionsEnum[] values = values();
            for (ActionsEnum v : values) {
                if (v.value.equals(value))
                    return v;
            }
            return null;
        }

        public static ActionsEnum[] fromJsonArray(JsonArray jsonArray) {
            ActionsEnum[] list = new ActionsEnum[jsonArray.size()];
            for (int i = 0; i < list.length; i++) {
                list[i] = fromString(jsonArray.getString(i));
            }
            return list;
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
            return new StoredLocation(obj);
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
