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

import java.text.ParseException;

/**
 * Created by dmgen from swagger.
 */

public class Contact implements Parcelable {
    private static final String TAG = "Contact";

    @Json(name = "email")
    private String _email;

    @Json(name = "ext")
    private String _ext;

    @Json(name = "name")
    private String _name;

    @Json(name = "notes")
    private String _notes;

    @Json(name = "phone")
    private String _phone;

    @Json(name = "role")
    private String _role;

    @Source
    private JsonObject SOURCE = new JsonObject();

    public Contact() {
    }

    public void setEmail(String email) throws ParseException {
        _email = email;
        SOURCE.put("email", email);
    }

    public String getEmail() {
        return _email;
    }

    public Contact email(String email) throws ParseException {
        _email = email;
        SOURCE.put("email", email);
        return this;
    }

    public void setExt(String ext) throws ParseException {
        _ext = ext;
        SOURCE.put("ext", ext);
    }

    public String getExt() {
        return _ext;
    }

    public Contact ext(String ext) throws ParseException {
        _ext = ext;
        SOURCE.put("ext", ext);
        return this;
    }

    public void setName(String name) throws ParseException {
        _name = name;
        SOURCE.put("name", name);
    }

    public String getName() {
        return _name;
    }

    public Contact name(String name) throws ParseException {
        _name = name;
        SOURCE.put("name", name);
        return this;
    }

    public void setNotes(String notes) throws ParseException {
        _notes = notes;
        SOURCE.put("notes", notes);
    }

    public String getNotes() {
        return _notes;
    }

    public Contact notes(String notes) throws ParseException {
        _notes = notes;
        SOURCE.put("notes", notes);
        return this;
    }

    public void setPhone(String phone) throws ParseException {
        _phone = phone;
        SOURCE.put("phone", phone);
    }

    public String getPhone() {
        return _phone;
    }

    public Contact phone(String phone) throws ParseException {
        _phone = phone;
        SOURCE.put("phone", phone);
        return this;
    }

    public void setRole(String role) throws ParseException {
        _role = role;
        SOURCE.put("role", role);
    }

    public String getRole() {
        return _role;
    }

    public Contact role(String role) throws ParseException {
        _role = role;
        SOURCE.put("role", role);
        return this;
    }

    /*-*****************************-*/
    /*-             Json            -*/
    /*-*****************************-*/
    public static JsonArray toJsonArray(Contact[] array) {
        JsonArray list = new JsonArray();
        for (Contact item : array) {
            list.add(item.getJson());
        }
        return list;
    }

    public static Contact[] fromJsonArray(JsonArray array) {
        Contact[] list = new Contact[array.size()];
        for (int i = 0; i < array.size(); i++) {
            list[i] = fromJson(array.getJsonObject(i));
        }
        return list;
    }

    public static Contact fromJson(JsonObject obj) {
        try {
            return Unserializer.unserializeObject(Contact.class, obj);
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
    public static final Parcelable.Creator<Contact> CREATOR = new Parcelable.Creator<Contact>() {

        @Override
        public Contact createFromParcel(Parcel source) {
            try {
                return Contact.fromJson((JsonObject) source.readParcelable(JsonObject.class.getClassLoader()));
            } catch (Exception ex) {
                Log.v(TAG, ex);
                return null;
            }
        }

        @Override
        public Contact[] newArray(int size) {
            return new Contact[size];
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
