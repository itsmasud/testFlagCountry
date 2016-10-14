package com.fieldnation.data.v2;

import android.os.Parcel;
import android.os.Parcelable;

import com.fieldnation.fnjson.JsonObject;
import com.fieldnation.fnjson.Serializer;
import com.fieldnation.fnjson.Unserializer;
import com.fieldnation.fnjson.annotations.Json;
import com.fieldnation.fnlog.Log;

/**
 * Created by Michael on 10/13/2016.
 */

public class Contact implements Parcelable {
    private static final String TAG = "Contact";

    @Json(name = "email")
    private String email;
    @Json(name = "phone_number")
    private String phoneNumber;
    @Json(name = "note")
    private String note;
    @Json(name = "phone_ext")
    private String phoneExt;
    @Json(name = "role")
    private String role;

    public Contact() {
    }

    public String getEmail() {
        return email;
    }

    public String getNote() {
        return note;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getPhoneExt() {
        return phoneExt;
    }

    public String getRole() {
        return role;
    }

    /*-*************************************-*/
    /*-			JSON Implementation			-*/
    /*-*************************************-*/
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

    public static Contact fromJson(JsonObject json) {
        try {
            return Unserializer.unserializeObject(Contact.class, json);
        } catch (Exception ex) {
            Log.v(TAG, ex);
            return null;
        }
    }

    /*-*********************************************-*/
    /*-			Parcelable Implementation			-*/
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
