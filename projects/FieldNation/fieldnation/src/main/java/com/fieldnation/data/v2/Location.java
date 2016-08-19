package com.fieldnation.data.v2;

import android.os.Parcel;
import android.os.Parcelable;

import com.fieldnation.fnlog.Log;
import com.fieldnation.fnjson.JsonObject;
import com.fieldnation.fnjson.Serializer;
import com.fieldnation.fnjson.Unserializer;
import com.fieldnation.fnjson.annotations.Json;
import com.fieldnation.fntools.misc;

/**
 * Created by Michael on 7/21/2016.
 */
public class Location implements Parcelable {
    private static final String TAG = "Location";

    @Json(name = "city")
    private String _city;
    @Json(name = "zip")
    private String _zip;
    @Json(name = "state")
    private String _state;
    @Json(name = "country")
    private String _country;
    @Json(name = "geo")
    private Geo _geo;
    @Json(name = "remote")
    private Boolean _remote = false;

    public Location() {
    }

    public String getCity() {
        return _city;
    }

    public String getZip() {
        return _zip;
    }

    public String getState() {
        return _state;
    }

    public String getCountry() {
        return _country;
    }

    public Geo getGeo() {
        return _geo;
    }

    public Boolean isRemote() {
        return _remote;
    }

    /*-*************************************-*/
    /*-			JSON Implementation			-*/
    /*-*************************************-*/

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
//        if (getAddress1() != null || getAddress2() != null) {
        String address1 = null;
        String address2 = null;

//            if (getAddress1() != null)
//                address1 = getAddress1();
//            if (getAddress2() != null)
//                address2 = getAddress2();

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
//        } else {
//            return "";
//        }
    }

    public String getFullAddressAndContactName() {
        String address = "";

//        if (!misc.isEmptyOrNull(_name)) {
//            address += _name + "\n";
//        }

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

    /*-*********************************************-*/
    /*-			Parcelable Implementation			-*/
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
}
