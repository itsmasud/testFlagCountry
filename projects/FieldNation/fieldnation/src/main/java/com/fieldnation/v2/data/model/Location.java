package com.fieldnation.v2.data.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.fieldnation.fnjson.JsonArray;
import com.fieldnation.fnjson.JsonObject;
import com.fieldnation.fnjson.annotations.Json;
import com.fieldnation.fnjson.annotations.Source;
import com.fieldnation.fnlog.Log;
import com.fieldnation.fntools.misc;

import java.text.ParseException;

/**
 * Created by dmgen from swagger.
 */

public class Location implements Parcelable {
    private static final String TAG = "Location";

    @Json(name = "actions")
    private ActionsEnum[] _actions;

    @Json(name = "address1")
    private String _address1;

    @Json(name = "address2")
    private String _address2;

    @Json(name = "city")
    private String _city;

    @Json(name = "contacts")
    private Contact[] _contacts;

    @Json(name = "coordinates")
    private Coords _coordinates;

    @Json(name = "correlation_id")
    private String _correlationId;

    @Json(name = "country")
    private String _country;

    @Json(name = "map")
    private Map _map;

    @Json(name = "mode")
    private ModeEnum _mode;

    @Json(name = "role")
    private String _role;

    @Json(name = "save_location")
    private String _saveLocation;

    @Json(name = "save_location_group")
    private Integer _saveLocationGroup;

    @Json(name = "saved_location")
    private StoredLocation _savedLocation;

    @Json(name = "state")
    private String _state;

    @Json(name = "status_id")
    private Integer _statusId;

    @Json(name = "time_zone")
    private TimeZone _timeZone;

    @Json(name = "type")
    private LocationType _type;

    @Json(name = "validation")
    private LocationValidation _validation;

    @Json(name = "work_order_id")
    private Integer _workOrderId;

    @Json(name = "zip")
    private String _zip;

    @Source
    private JsonObject SOURCE;

    public Location() {
        SOURCE = new JsonObject();
    }

    public Location(JsonObject obj) {
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

    public Location actions(ActionsEnum[] actions) throws ParseException {
        _actions = actions;
        JsonArray ja = new JsonArray();
        for (ActionsEnum item : actions) {
            ja.add(item.toString());
        }
        SOURCE.put("actions", ja, true);
        return this;
    }

    public void setAddress1(String address1) throws ParseException {
        _address1 = address1;
        SOURCE.put("address1", address1);
    }

    public String getAddress1() {
        try {
            if (_address1 == null && SOURCE.has("address1") && SOURCE.get("address1") != null)
                _address1 = SOURCE.getString("address1");
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _address1;
    }

    public Location address1(String address1) throws ParseException {
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
            if (_address2 == null && SOURCE.has("address2") && SOURCE.get("address2") != null)
                _address2 = SOURCE.getString("address2");
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _address2;
    }

    public Location address2(String address2) throws ParseException {
        _address2 = address2;
        SOURCE.put("address2", address2);
        return this;
    }

    public void setCity(String city) throws ParseException {
        _city = city;
        SOURCE.put("city", city);
    }

    public String getCity() {
        try {
            if (_city == null && SOURCE.has("city") && SOURCE.get("city") != null)
                _city = SOURCE.getString("city");
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _city;
    }

    public Location city(String city) throws ParseException {
        _city = city;
        SOURCE.put("city", city);
        return this;
    }

    public void setContacts(Contact[] contacts) throws ParseException {
        _contacts = contacts;
        SOURCE.put("contacts", Contact.toJsonArray(contacts));
    }

    public Contact[] getContacts() {
        try {
            if (_contacts != null)
                return _contacts;

            if (SOURCE.has("contacts") && SOURCE.get("contacts") != null) {
                _contacts = Contact.fromJsonArray(SOURCE.getJsonArray("contacts"));
            }

        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _contacts;
    }

    public Location contacts(Contact[] contacts) throws ParseException {
        _contacts = contacts;
        SOURCE.put("contacts", Contact.toJsonArray(contacts), true);
        return this;
    }

    public void setCoordinates(Coords coordinates) throws ParseException {
        _coordinates = coordinates;
        SOURCE.put("coordinates", coordinates.getJson());
    }

    public Coords getCoordinates() {
        try {
            if (_coordinates == null && SOURCE.has("coordinates") && SOURCE.get("coordinates") != null)
                _coordinates = Coords.fromJson(SOURCE.getJsonObject("coordinates"));
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        if (_coordinates != null && _coordinates.isSet())
            return _coordinates;

        return null;
    }

    public Location coordinates(Coords coordinates) throws ParseException {
        _coordinates = coordinates;
        SOURCE.put("coordinates", coordinates.getJson());
        return this;
    }

    public void setCorrelationId(String correlationId) throws ParseException {
        _correlationId = correlationId;
        SOURCE.put("correlation_id", correlationId);
    }

    public String getCorrelationId() {
        try {
            if (_correlationId == null && SOURCE.has("correlation_id") && SOURCE.get("correlation_id") != null)
                _correlationId = SOURCE.getString("correlation_id");
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _correlationId;
    }

    public Location correlationId(String correlationId) throws ParseException {
        _correlationId = correlationId;
        SOURCE.put("correlation_id", correlationId);
        return this;
    }

    public void setCountry(String country) throws ParseException {
        _country = country;
        SOURCE.put("country", country);
    }

    public String getCountry() {
        try {
            if (_country == null && SOURCE.has("country") && SOURCE.get("country") != null)
                _country = SOURCE.getString("country");
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _country;
    }

    public Location country(String country) throws ParseException {
        _country = country;
        SOURCE.put("country", country);
        return this;
    }

    public void setMap(Map map) throws ParseException {
        _map = map;
        SOURCE.put("map", map.getJson());
    }

    public Map getMap() {
        try {
            if (_map == null && SOURCE.has("map") && SOURCE.get("map") != null)
                _map = Map.fromJson(SOURCE.getJsonObject("map"));
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        if (_map != null && _map.isSet())
            return _map;

        return null;
    }

    public Location map(Map map) throws ParseException {
        _map = map;
        SOURCE.put("map", map.getJson());
        return this;
    }

    public void setMode(ModeEnum mode) throws ParseException {
        _mode = mode;
        SOURCE.put("mode", mode.toString());
    }

    public ModeEnum getMode() {
        try {
            if (_mode == null && SOURCE.has("mode") && SOURCE.get("mode") != null)
                _mode = ModeEnum.fromString(SOURCE.getString("mode"));
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _mode;
    }

    public Location mode(ModeEnum mode) throws ParseException {
        _mode = mode;
        SOURCE.put("mode", mode.toString());
        return this;
    }

    public void setRole(String role) throws ParseException {
        _role = role;
        SOURCE.put("role", role);
    }

    public String getRole() {
        try {
            if (_role == null && SOURCE.has("role") && SOURCE.get("role") != null)
                _role = SOURCE.getString("role");
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _role;
    }

    public Location role(String role) throws ParseException {
        _role = role;
        SOURCE.put("role", role);
        return this;
    }

    public void setSaveLocation(String saveLocation) throws ParseException {
        _saveLocation = saveLocation;
        SOURCE.put("save_location", saveLocation);
    }

    public String getSaveLocation() {
        try {
            if (_saveLocation == null && SOURCE.has("save_location") && SOURCE.get("save_location") != null)
                _saveLocation = SOURCE.getString("save_location");
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _saveLocation;
    }

    public Location saveLocation(String saveLocation) throws ParseException {
        _saveLocation = saveLocation;
        SOURCE.put("save_location", saveLocation);
        return this;
    }

    public void setSaveLocationGroup(Integer saveLocationGroup) throws ParseException {
        _saveLocationGroup = saveLocationGroup;
        SOURCE.put("save_location_group", saveLocationGroup);
    }

    public Integer getSaveLocationGroup() {
        try {
            if (_saveLocationGroup == null && SOURCE.has("save_location_group") && SOURCE.get("save_location_group") != null)
                _saveLocationGroup = SOURCE.getInt("save_location_group");
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _saveLocationGroup;
    }

    public Location saveLocationGroup(Integer saveLocationGroup) throws ParseException {
        _saveLocationGroup = saveLocationGroup;
        SOURCE.put("save_location_group", saveLocationGroup);
        return this;
    }

    public void setSavedLocation(StoredLocation savedLocation) throws ParseException {
        _savedLocation = savedLocation;
        SOURCE.put("saved_location", savedLocation.getJson());
    }

    public StoredLocation getSavedLocation() {
        try {
            if (_savedLocation == null && SOURCE.has("saved_location") && SOURCE.get("saved_location") != null)
                _savedLocation = StoredLocation.fromJson(SOURCE.getJsonObject("saved_location"));
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        if (_savedLocation != null && _savedLocation.isSet())
            return _savedLocation;

        return null;
    }

    public Location savedLocation(StoredLocation savedLocation) throws ParseException {
        _savedLocation = savedLocation;
        SOURCE.put("saved_location", savedLocation.getJson());
        return this;
    }

    public void setState(String state) throws ParseException {
        _state = state;
        SOURCE.put("state", state);
    }

    public String getState() {
        try {
            if (_state == null && SOURCE.has("state") && SOURCE.get("state") != null)
                _state = SOURCE.getString("state");
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _state;
    }

    public Location state(String state) throws ParseException {
        _state = state;
        SOURCE.put("state", state);
        return this;
    }

    public void setStatusId(Integer statusId) throws ParseException {
        _statusId = statusId;
        SOURCE.put("status_id", statusId);
    }

    public Integer getStatusId() {
        try {
            if (_statusId == null && SOURCE.has("status_id") && SOURCE.get("status_id") != null)
                _statusId = SOURCE.getInt("status_id");
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _statusId;
    }

    public Location statusId(Integer statusId) throws ParseException {
        _statusId = statusId;
        SOURCE.put("status_id", statusId);
        return this;
    }

    public void setTimeZone(TimeZone timeZone) throws ParseException {
        _timeZone = timeZone;
        SOURCE.put("time_zone", timeZone.getJson());
    }

    public TimeZone getTimeZone() {
        try {
            if (_timeZone == null && SOURCE.has("time_zone") && SOURCE.get("time_zone") != null)
                _timeZone = TimeZone.fromJson(SOURCE.getJsonObject("time_zone"));
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        if (_timeZone != null && _timeZone.isSet())
            return _timeZone;

        return null;
    }

    public Location timeZone(TimeZone timeZone) throws ParseException {
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
            if (_type == null && SOURCE.has("type") && SOURCE.get("type") != null)
                _type = LocationType.fromJson(SOURCE.getJsonObject("type"));
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        if (_type != null && _type.isSet())
            return _type;

        return null;
    }

    public Location type(LocationType type) throws ParseException {
        _type = type;
        SOURCE.put("type", type.getJson());
        return this;
    }

    public void setValidation(LocationValidation validation) throws ParseException {
        _validation = validation;
        SOURCE.put("validation", validation.getJson());
    }

    public LocationValidation getValidation() {
        try {
            if (_validation == null && SOURCE.has("validation") && SOURCE.get("validation") != null)
                _validation = LocationValidation.fromJson(SOURCE.getJsonObject("validation"));
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        if (_validation != null && _validation.isSet())
            return _validation;

        return null;
    }

    public Location validation(LocationValidation validation) throws ParseException {
        _validation = validation;
        SOURCE.put("validation", validation.getJson());
        return this;
    }

    public void setWorkOrderId(Integer workOrderId) throws ParseException {
        _workOrderId = workOrderId;
        SOURCE.put("work_order_id", workOrderId);
    }

    public Integer getWorkOrderId() {
        try {
            if (_workOrderId == null && SOURCE.has("work_order_id") && SOURCE.get("work_order_id") != null)
                _workOrderId = SOURCE.getInt("work_order_id");
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _workOrderId;
    }

    public Location workOrderId(Integer workOrderId) throws ParseException {
        _workOrderId = workOrderId;
        SOURCE.put("work_order_id", workOrderId);
        return this;
    }

    public void setZip(String zip) throws ParseException {
        _zip = zip;
        SOURCE.put("zip", zip);
    }

    public String getZip() {
        try {
            if (_zip == null && SOURCE.has("zip") && SOURCE.get("zip") != null)
                _zip = SOURCE.getString("zip");
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _zip;
    }

    public Location zip(String zip) throws ParseException {
        _zip = zip;
        SOURCE.put("zip", zip);
        return this;
    }

    /*-******************************-*/
    /*-             Enums            -*/
    /*-******************************-*/
    public enum ModeEnum {
        @Json(name = "custom")
        CUSTOM("custom"),
        @Json(name = "location")
        LOCATION("location"),
        @Json(name = "remote")
        REMOTE("remote");

        private String value;

        ModeEnum(String value) {
            this.value = value;
        }

        public static ModeEnum fromString(String value) {
            ModeEnum[] values = values();
            for (ModeEnum v : values) {
                if (v.value.equals(value))
                    return v;
            }
            return null;
        }

        public static ModeEnum[] fromJsonArray(JsonArray jsonArray) {
            ModeEnum[] list = new ModeEnum[jsonArray.size()];
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

    public enum ActionsEnum {
        @Json(name = "edit")
        EDIT("edit"),
        @Json(name = "map")
        MAP("map");

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
    public static JsonArray toJsonArray(Location[] array) {
        JsonArray list = new JsonArray();
        for (Location item : array) {
            list.add(item.getJson());
        }
        return list;
    }

    public static Location[] fromJsonArray(JsonArray array) {
        Location[] list = new Location[array.size()];
        for (int i = 0; i < array.size(); i++) {
            list[i] = fromJson(array.getJsonObject(i));
        }
        return list;
    }

    public static Location fromJson(JsonObject obj) {
        try {
            return new Location(obj);
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
    public static final Parcelable.Creator<Location> CREATOR = new Parcelable.Creator<Location>() {

        @Override
        public Location createFromParcel(Parcel source) {
            try {
                return Location.fromJson((JsonObject) source.readParcelable(JsonObject.class.getClassLoader()));
            } catch (Exception ex) {
                Log.v(TAG, ex);
                return null;
            }
        }

        @Override
        public Location[] newArray(int size) {
            return new Location[size];
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

    /*-*****************************-*/
    /*-         Human Code          -*/
    /*-*****************************-*/

    public String getCityState() {
        if (misc.isEmptyOrNull(getCity()) && misc.isEmptyOrNull(getState()))
            // both empty
            return "";
        else if (misc.isEmptyOrNull(getCity()) && !misc.isEmptyOrNull(getState()))
            // have state
            return getState();
        else if (!misc.isEmptyOrNull(getCity()) && misc.isEmptyOrNull(getState()))
            return getCity();
        else
            return getCity() + ", " + getState();
    }

    public String getTopAddressLine() {
        if (getAddress1() != null || getAddress2() != null) {
            String address1 = null;
            String address2 = null;

            if (getAddress1() != null)
                address1 = getAddress1();
            if (getAddress2() != null)
                address2 = getAddress2();

            if (misc.isEmptyOrNull(address1))
                address1 = null;
            if (misc.isEmptyOrNull(address2))
                address2 = null;

            if (address1 == null)
                address1 = address2;
            else if (address2 != null) {
                address1 = (address1 + "\n" + address2).trim();
            }

            if (address1 != null) {
                return address1;
            } else {
                return "";
            }
        } else {
            return "";
        }
    }

    public String getFullAddressOneLine() {
        String address = "";

        String topAddr = getTopAddressLine();
        if (!misc.isEmptyOrNull(topAddr)) {
            address += topAddr + ", ";
        }

        if (!misc.isEmptyOrNull(getCity())
                && !misc.isEmptyOrNull(getState())
                && !misc.isEmptyOrNull(getZip())
                && !misc.isEmptyOrNull(getCountry())) {
            address += getCity() + ", " + getState();
        }

        if (!misc.isEmptyOrNull(getZip())) {
            address += " " + getZip();
        }

        return address.trim();
    }

    public boolean isSet() {
        return getMode() != null;
    }
}