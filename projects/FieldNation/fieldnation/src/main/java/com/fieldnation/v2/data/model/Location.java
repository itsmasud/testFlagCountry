package com.fieldnation.v2.data.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.fieldnation.fnjson.JsonArray;
import com.fieldnation.fnjson.JsonObject;
import com.fieldnation.fnjson.Serializer;
import com.fieldnation.fnjson.Unserializer;
import com.fieldnation.fnjson.annotations.Json;
import com.fieldnation.fnlog.Log;
import com.fieldnation.fntools.misc;

/**
 * Created by dmgen from swagger.
 */

public class Location implements Parcelable {
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
    private ActionsEnum[] _actions;

    @Json(name = "map")
    private Map _map;

    @Json(name = "contacts")
    private Contact[] _contacts;

    @Json(name = "validation")
    private LocationValidation _validation;

    public Location() {
    }

    public void setZip(String zip) {
        _zip = zip;
    }

    public String getZip() {
        return _zip;
    }

    public Location zip(String zip) {
        _zip = zip;
        return this;
    }

    public void setSaveLocationGroup(Integer saveLocationGroup) {
        _saveLocationGroup = saveLocationGroup;
    }

    public Integer getSaveLocationGroup() {
        return _saveLocationGroup;
    }

    public Location saveLocationGroup(Integer saveLocationGroup) {
        _saveLocationGroup = saveLocationGroup;
        return this;
    }

    public void setCountry(String country) {
        _country = country;
    }

    public String getCountry() {
        return _country;
    }

    public Location country(String country) {
        _country = country;
        return this;
    }

    public void setRole(String role) {
        _role = role;
    }

    public String getRole() {
        return _role;
    }

    public Location role(String role) {
        _role = role;
        return this;
    }

    public void setAddress2(String address2) {
        _address2 = address2;
    }

    public String getAddress2() {
        return _address2;
    }

    public Location address2(String address2) {
        _address2 = address2;
        return this;
    }

    public void setCity(String city) {
        _city = city;
    }

    public String getCity() {
        return _city;
    }

    public Location city(String city) {
        _city = city;
        return this;
    }

    public void setSavedLocation(StoredLocation savedLocation) {
        _savedLocation = savedLocation;
    }

    public StoredLocation getSavedLocation() {
        return _savedLocation;
    }

    public Location savedLocation(StoredLocation savedLocation) {
        _savedLocation = savedLocation;
        return this;
    }

    public void setAddress1(String address1) {
        _address1 = address1;
    }

    public String getAddress1() {
        return _address1;
    }

    public Location address1(String address1) {
        _address1 = address1;
        return this;
    }

    public void setCoordinates(Coords coordinates) {
        _coordinates = coordinates;
    }

    public Coords getCoordinates() {
        return _coordinates;
    }

    public Location coordinates(Coords coordinates) {
        _coordinates = coordinates;
        return this;
    }

    public void setType(LocationType type) {
        _type = type;
    }

    public LocationType getType() {
        return _type;
    }

    public Location type(LocationType type) {
        _type = type;
        return this;
    }

    public void setTimeZone(TimeZone timeZone) {
        _timeZone = timeZone;
    }

    public TimeZone getTimeZone() {
        return _timeZone;
    }

    public Location timeZone(TimeZone timeZone) {
        _timeZone = timeZone;
        return this;
    }

    public void setMode(ModeEnum mode) {
        _mode = mode;
    }

    public ModeEnum getMode() {
        return _mode;
    }

    public Location mode(ModeEnum mode) {
        _mode = mode;
        return this;
    }

    public void setStatusId(Integer statusId) {
        _statusId = statusId;
    }

    public Integer getStatusId() {
        return _statusId;
    }

    public Location statusId(Integer statusId) {
        _statusId = statusId;
        return this;
    }

    public void setSaveLocation(String saveLocation) {
        _saveLocation = saveLocation;
    }

    public String getSaveLocation() {
        return _saveLocation;
    }

    public Location saveLocation(String saveLocation) {
        _saveLocation = saveLocation;
        return this;
    }

    public void setWorkOrderId(Integer workOrderId) {
        _workOrderId = workOrderId;
    }

    public Integer getWorkOrderId() {
        return _workOrderId;
    }

    public Location workOrderId(Integer workOrderId) {
        _workOrderId = workOrderId;
        return this;
    }

    public void setCorrelationId(String correlationId) {
        _correlationId = correlationId;
    }

    public String getCorrelationId() {
        return _correlationId;
    }

    public Location correlationId(String correlationId) {
        _correlationId = correlationId;
        return this;
    }

    public void setState(String state) {
        _state = state;
    }

    public String getState() {
        return _state;
    }

    public Location state(String state) {
        _state = state;
        return this;
    }

    public void setActions(ActionsEnum[] actions) {
        _actions = actions;
    }

    public ActionsEnum[] getActions() {
        return _actions;
    }

    public Location actions(ActionsEnum[] actions) {
        _actions = actions;
        return this;
    }

    public void setMap(Map map) {
        _map = map;
    }

    public Map getMap() {
        return _map;
    }

    public Location map(Map map) {
        _map = map;
        return this;
    }

    public void setContacts(Contact[] contacts) {
        _contacts = contacts;
    }

    public Contact[] getContacts() {
        return _contacts;
    }

    public Location contacts(Contact[] contacts) {
        _contacts = contacts;
        return this;
    }

    public void setValidation(LocationValidation validation) {
        _validation = validation;
    }

    public LocationValidation getValidation() {
        return _validation;
    }

    public Location validation(LocationValidation validation) {
        _validation = validation;
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

        @Override
        public String toString() {
            return String.valueOf(value);
        }
    }

    public enum ActionsEnum {
        @Json(name = "unknown")
        UNKNOWN("unknown");

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
    public static Location[] fromJsonArray(JsonArray array) {
        Location[] list = new Location[array.size()];
        for (int i = 0; i < array.size(); i++) {
            list[i] = fromJson(array.getJsonObject(i));
        }
        return list;
    }

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
        dest.writeParcelable(toJson(), flags);
    }

   	/*-*************************************************-*/
    /*-				Human Generated Code				-*/
    /*-*************************************************-*/

    public String getCityState() {
        if (misc.isEmptyOrNull(_city) && misc.isEmptyOrNull(_state))
            // both empty
            return "";
        else if (misc.isEmptyOrNull(_city) && !misc.isEmptyOrNull(_state))
            // have state
            return _state;
        else if (!misc.isEmptyOrNull(_city) && misc.isEmptyOrNull(_state))
            return _city;
        else
            return _city + ", " + _state;
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

        if (!misc.isEmptyOrNull(_city)
                && !misc.isEmptyOrNull(_state)
                && !misc.isEmptyOrNull(_zip)
                && !misc.isEmptyOrNull(_country)) {
            address += _city + ", " + _state;
        }

        if (!misc.isEmptyOrNull(_zip)) {
            address += " " + _zip;
        }

        return address.trim();
    }

}
