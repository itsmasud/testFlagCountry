package com.fieldnation.data.workorder;

import com.fieldnation.fnlog.Log;
import com.fieldnation.fnjson.JsonObject;
import com.fieldnation.fnjson.Serializer;
import com.fieldnation.fnjson.Unserializer;
import com.fieldnation.fnjson.annotations.Json;
import com.fieldnation.fntools.misc;

public class Location {
    private static final String TAG = "Location";

    @Json(name = "address1")
    private String _address1;
    @Json(name = "address2")
    private String _address2;
    @Json(name = "checkInCheckOutMapUrl")
    private String _checkInCheckOutMapUrl;
    @Json(name = "city")
    private String _city;
    @Json(name = "contactEmail")
    private String _contactEmail;
    @Json(name = "contactName")
    private String _contactName;
    @Json(name = "contactPhone")
    private String _contactPhone;
    @Json(name = "contactPhoneExt")
    private String _contactPhoneExt;
    @Json(name = "country")
    private String _country;
    @Json(name = "distance")
    private Double _distance;
    @Json(name = "distanceMapUrl")
    private String _distanceMapUrl;
    @Json(name = "geo")
    private Geo _geo;
    @Json(name = "groupName")
    private String _groupName;
    @Json(name = "mapUrl")
    private String _mapUrl;
    @Json(name = "name")
    private String _name;
    @Json(name = "notes")
    private String _notes;
    @Json(name = "state")
    private String _state;
    @Json(name = "type")
    private String _type;
    @Json(name = "zip")
    private String _zip;

    public Location() {
    }

    public String getAddress1() {
        return _address1;
    }

    public String getAddress2() {
        return _address2;
    }

    public String getCheckInCheckOutMapUrl() {
        return _checkInCheckOutMapUrl;
    }

    public String getCity() {
        return _city;
    }

    public String getContactEmail() {
        return _contactEmail;
    }

    public String getContactName() {
        return _contactName;
    }

    public String getContactPhone() {
        return _contactPhone;
    }

    public String getContactPhoneExt() {
        return _contactPhoneExt;
    }

    public String getCountry() {
        return _country;
    }

    public Double getDistance() {
        return _distance;
    }

    public String getDistanceMapUrl() {
        return _distanceMapUrl;
    }

    public Geo getGeo() {
        return _geo;
    }

    public String getGroupName() {
        return _groupName;
    }

    public String getMapUrl() {
        return _mapUrl;
    }

    public String getName() {
        return _name;
    }

    public String getNotes() {
        return _notes;
    }

    public String getState() {
        return _state;
    }

    public String getType() {
        return _type;
    }

    public String getZip() {
        return _zip;
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

    public static Location fromJson(JsonObject json) {
        try {
            return Unserializer.unserializeObject(Location.class, json);
        } catch (Exception ex) {
            Log.v(TAG, ex);
            return null;
        }
    }

	/*-*************************************************-*/
    /*-				Human Generated Code				-*/
    /*-*************************************************-*/

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

    public String getFullAddressAndContactName() {
        String address = "";

        if (!misc.isEmptyOrNull(_name)) {
            address += _name + "\n";
        }

        String topAddr = getTopAddressLine();
        if (!misc.isEmptyOrNull(topAddr)) {
            address += topAddr + "\n";
        }

        if (!misc.isEmptyOrNull(_city) && !misc.isEmptyOrNull(_state) && !misc.isEmptyOrNull(_zip) && !misc.isEmptyOrNull(_country)) {
            address += _city + ", " + _state + " " + _zip + "\n";
            address += _country;
        }

        return address.trim();
    }

    public String getFullAddress() {
        String address = "";

        String topAddr = getTopAddressLine();
        if (!misc.isEmptyOrNull(topAddr)) {
            address += topAddr + "\n";
        }

        if (!misc.isEmptyOrNull(_city) && !misc.isEmptyOrNull(_state) && !misc.isEmptyOrNull(_zip) && !misc.isEmptyOrNull(_country)) {
            address += _city + ", " + _state + " " + _zip + "\n";
            address += _country;
        }

        return address.trim();
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
