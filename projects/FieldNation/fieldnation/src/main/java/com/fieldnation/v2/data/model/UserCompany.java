package com.fieldnation.v2.data.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.fieldnation.fnjson.JsonArray;
import com.fieldnation.fnjson.JsonObject;
import com.fieldnation.fnjson.Serializer;
import com.fieldnation.fnjson.Unserializer;
import com.fieldnation.fnjson.annotations.Json;
import com.fieldnation.fnjson.annotations.Source;
import com.fieldnation.fnlog.Log;
import com.fieldnation.fntools.misc;

import java.text.ParseException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by dmgen from swagger.
 */

public class UserCompany implements Parcelable {
    private static final String TAG = "UserCompany";

    @Json(name = "features")
    private String[] _features;

    @Json(name = "id")
    private Integer _id;

    @Json(name = "name")
    private String _name;

    @Json(name = "technicians")
    private Integer _technicians;

    @Json(name = "vendors")
    private Company[] _vendors;

    @Source
    private JsonObject SOURCE;

    public UserCompany() {
        SOURCE = new JsonObject();
    }

    public UserCompany(JsonObject obj) {
        SOURCE = obj;
    }

    public void setFeatures(String[] features) throws ParseException {
        _features = features;
        JsonArray ja = new JsonArray();
        for (String item : features) {
            ja.add(item);
        }
        SOURCE.put("features", ja);
    }

    public String[] getFeatures() {
        try {
            if (_features != null)
                return _features;

            if (SOURCE.has("features") && SOURCE.get("features") != null) {
                JsonArray ja = SOURCE.getJsonArray("features");
                _features = ja.toArray(new String[ja.size()]);
            }

        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _features;
    }

    public UserCompany features(String[] features) throws ParseException {
        _features = features;
        JsonArray ja = new JsonArray();
        for (String item : features) {
            ja.add(item);
        }
        SOURCE.put("features", ja, true);
        return this;
    }

    public void setId(Integer id) throws ParseException {
        _id = id;
        SOURCE.put("id", id);
    }

    public Integer getId() {
        try {
            if (_id == null && SOURCE.has("id") && SOURCE.get("id") != null)
                _id = SOURCE.getInt("id");
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _id;
    }

    public UserCompany id(Integer id) throws ParseException {
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
            if (_name == null && SOURCE.has("name") && SOURCE.get("name") != null)
                _name = SOURCE.getString("name");
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _name;
    }

    public UserCompany name(String name) throws ParseException {
        _name = name;
        SOURCE.put("name", name);
        return this;
    }

    public void setTechnicians(Integer technicians) throws ParseException {
        _technicians = technicians;
        SOURCE.put("technicians", technicians);
    }

    public Integer getTechnicians() {
        try {
            if (_technicians == null && SOURCE.has("technicians") && SOURCE.get("technicians") != null)
                _technicians = SOURCE.getInt("technicians");
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _technicians;
    }

    public UserCompany technicians(Integer technicians) throws ParseException {
        _technicians = technicians;
        SOURCE.put("technicians", technicians);
        return this;
    }

    public void setVendors(Company[] vendors) throws ParseException {
        _vendors = vendors;
        SOURCE.put("vendors", Company.toJsonArray(vendors));
    }

    public Company[] getVendors() {
        try {
            if (_vendors != null)
                return _vendors;

            if (SOURCE.has("vendors") && SOURCE.get("vendors") != null) {
                _vendors = Company.fromJsonArray(SOURCE.getJsonArray("vendors"));
            }

        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _vendors;
    }

    public UserCompany vendors(Company[] vendors) throws ParseException {
        _vendors = vendors;
        SOURCE.put("vendors", Company.toJsonArray(vendors), true);
        return this;
    }

    /*-*****************************-*/
    /*-             Json            -*/
    /*-*****************************-*/
    public static JsonArray toJsonArray(UserCompany[] array) {
        JsonArray list = new JsonArray();
        for (UserCompany item : array) {
            list.add(item.getJson());
        }
        return list;
    }

    public static UserCompany[] fromJsonArray(JsonArray array) {
        UserCompany[] list = new UserCompany[array.size()];
        for (int i = 0; i < array.size(); i++) {
            list[i] = fromJson(array.getJsonObject(i));
        }
        return list;
    }

    public static UserCompany fromJson(JsonObject obj) {
        try {
            return new UserCompany(obj);
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
    public static final Parcelable.Creator<UserCompany> CREATOR = new Parcelable.Creator<UserCompany>() {

        @Override
        public UserCompany createFromParcel(Parcel source) {
            try {
                return UserCompany.fromJson((JsonObject) source.readParcelable(JsonObject.class.getClassLoader()));
            } catch (Exception ex) {
                Log.v(TAG, ex);
                return null;
            }
        }

        @Override
        public UserCompany[] newArray(int size) {
            return new UserCompany[size];
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

    public boolean isSet() {
        return getId() != null && getId() != 0;
    }
}
