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

public class Contact implements Parcelable {
    private static final String TAG = "Contact";

    @Json(name = "ext")
    private String _ext;

    @Json(name = "role")
    private String _role;

    @Json(name = "notes")
    private String _notes;

    @Json(name = "phone")
    private String _phone;

    @Json(name = "name")
    private String _name;

    @Json(name = "email")
    private String _email;

    public Contact() {
    }

    public void setExt(String ext) {
        _ext = ext;
    }

    public String getExt() {
        return _ext;
    }

    public Contact ext(String ext) {
        _ext = ext;
        return this;
    }

    public void setRole(String role) {
        _role = role;
    }

    public String getRole() {
        return _role;
    }

    public Contact role(String role) {
        _role = role;
        return this;
    }

    public void setNotes(String notes) {
        _notes = notes;
    }

    public String getNotes() {
        return _notes;
    }

    public Contact notes(String notes) {
        _notes = notes;
        return this;
    }

    public void setPhone(String phone) {
        _phone = phone;
    }

    public String getPhone() {
        return _phone;
    }

    public Contact phone(String phone) {
        _phone = phone;
        return this;
    }

    public void setName(String name) {
        _name = name;
    }

    public String getName() {
        return _name;
    }

    public Contact name(String name) {
        _name = name;
        return this;
    }

    public void setEmail(String email) {
        _email = email;
    }

    public String getEmail() {
        return _email;
    }

    public Contact email(String email) {
        _email = email;
        return this;
    }

    /*-*****************************-*/
    /*-             Json            -*/
    /*-*****************************-*/
    public static Contact fromJson(JsonObject obj) {
        try {
            return Unserializer.unserializeObject(Contact.class, obj);
        } catch (Exception ex) {
            Log.v(TAG, TAG, ex);
            return null;
        }
    }

    public JsonObject toJson() {
        return toJson(this);
    }

    public static JsonObject toJson(Contact contact) {
        try {
            return Serializer.serializeObject(contact);
        } catch (Exception ex) {
            Log.v(TAG, TAG, ex);
            return null;
        }
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
        dest.writeParcelable(toJson(), flags);
    }
}
