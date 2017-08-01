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

public class Manager implements Parcelable {
    private static final String TAG = "Manager";

    @Json(name = "email")
    private String _email;

    @Json(name = "first_name")
    private String _firstName;

    @Json(name = "id")
    private Integer _id;

    @Json(name = "last_name")
    private String _lastName;

    @Json(name = "phone")
    private String _phone;

    @Source
    private JsonObject SOURCE;

    public Manager() {
        SOURCE = new JsonObject();
    }

    public Manager(JsonObject obj) {
        SOURCE = obj;
    }

    public void setEmail(String email) throws ParseException {
        _email = email;
        SOURCE.put("email", email);
    }

    public String getEmail() {
        try {
            if (_email == null && SOURCE.has("email") && SOURCE.get("email") != null)
                _email = SOURCE.getString("email");
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _email;
    }

    public Manager email(String email) throws ParseException {
        _email = email;
        SOURCE.put("email", email);
        return this;
    }

    public void setFirstName(String firstName) throws ParseException {
        _firstName = firstName;
        SOURCE.put("first_name", firstName);
    }

    public String getFirstName() {
        try {
            if (_firstName == null && SOURCE.has("first_name") && SOURCE.get("first_name") != null)
                _firstName = SOURCE.getString("first_name");
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _firstName;
    }

    public Manager firstName(String firstName) throws ParseException {
        _firstName = firstName;
        SOURCE.put("first_name", firstName);
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

    public Manager id(Integer id) throws ParseException {
        _id = id;
        SOURCE.put("id", id);
        return this;
    }

    public void setLastName(String lastName) throws ParseException {
        _lastName = lastName;
        SOURCE.put("last_name", lastName);
    }

    public String getLastName() {
        try {
            if (_lastName == null && SOURCE.has("last_name") && SOURCE.get("last_name") != null)
                _lastName = SOURCE.getString("last_name");
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _lastName;
    }

    public Manager lastName(String lastName) throws ParseException {
        _lastName = lastName;
        SOURCE.put("last_name", lastName);
        return this;
    }

    public void setPhone(String phone) throws ParseException {
        _phone = phone;
        SOURCE.put("phone", phone);
    }

    public String getPhone() {
        try {
            if (_phone == null && SOURCE.has("phone") && SOURCE.get("phone") != null)
                _phone = SOURCE.getString("phone");
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _phone;
    }

    public Manager phone(String phone) throws ParseException {
        _phone = phone;
        SOURCE.put("phone", phone);
        return this;
    }

    /*-*****************************-*/
    /*-             Json            -*/
    /*-*****************************-*/
    public static JsonArray toJsonArray(Manager[] array) {
        JsonArray list = new JsonArray();
        for (Manager item : array) {
            list.add(item.getJson());
        }
        return list;
    }

    public static Manager[] fromJsonArray(JsonArray array) {
        Manager[] list = new Manager[array.size()];
        for (int i = 0; i < array.size(); i++) {
            list[i] = fromJson(array.getJsonObject(i));
        }
        return list;
    }

    public static Manager fromJson(JsonObject obj) {
        try {
            return new Manager(obj);
        } catch (Exception ex) {
            Log.v(TAG, ex);
            return null;
        }
    }

    public JsonObject getJson() {
        return SOURCE;
    }

    /*-*********************************************-*/
    /*-			Parcelable Implementation           -*/
    /*-*********************************************-*/
    public static final Parcelable.Creator<Manager> CREATOR = new Parcelable.Creator<Manager>() {

        @Override
        public Manager createFromParcel(Parcel source) {
            try {
                return Manager.fromJson((JsonObject) source.readParcelable(JsonObject.class.getClassLoader()));
            } catch (Exception ex) {
                Log.v(TAG, ex);
                return null;
            }
        }

        @Override
        public Manager[] newArray(int size) {
            return new Manager[size];
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

}
