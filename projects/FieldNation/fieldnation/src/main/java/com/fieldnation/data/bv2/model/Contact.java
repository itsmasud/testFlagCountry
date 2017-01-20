package com.fieldnation.data.bv2.model;

import com.fieldnation.fnjson.JsonObject;
import com.fieldnation.fnjson.Serializer;
import com.fieldnation.fnjson.Unserializer;
import com.fieldnation.fnjson.annotations.Json;
import com.fieldnation.fnlog.Log;

public class Contact {
    private static final String TAG = "Contact";

    @Json(name = "name")
    private String name = null;

    @Json(name = "role")
    private String role = null;

    @Json(name = "phone")
    private String phone = null;

    @Json(name = "ext")
    private String ext = null;

    @Json(name = "email")
    private String email = null;

    @Json(name = "notes")
    private String notes = null;

    public Contact() {
    }

    public String getName() {
        return name;
    }

    public String getRole() {
        return role;
    }

    public String getPhone() {
        return phone;
    }

    public String getExt() {
        return ext;
    }

    public String getEmail() {
        return email;
    }

    public String getNotes() {
        return notes;
    }

    /*-*****************************-*/
    /*-             Json            -*/
    /*-*****************************-*/
    public static Contact fromJson(JsonObject obj) {
        try {
            return Unserializer.unserializeObject(Contact.class, obj);
        } catch (Exception ex) {
            Log.v(TAG, ex);
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
            Log.v(TAG, ex);
            return null;
        }
    }
}

