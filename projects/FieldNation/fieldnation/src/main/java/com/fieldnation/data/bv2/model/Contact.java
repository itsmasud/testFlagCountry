package com.fieldnation.data.bv2.model;

import com.fieldnation.fnjson.JsonObject;
import com.fieldnation.fnjson.Serializer;
import com.fieldnation.fnjson.Unserializer;
import com.fieldnation.fnjson.annotations.Json;
import com.fieldnation.fnlog.Log;

public class Contact {
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

    public String getExt() {
        return _ext;
    }

    public String getRole() {
        return _role;
    }

    public String getNotes() {
        return _notes;
    }

    public String getPhone() {
        return _phone;
    }

    public String getName() {
        return _name;
    }

    public String getEmail() {
        return _email;
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
}
