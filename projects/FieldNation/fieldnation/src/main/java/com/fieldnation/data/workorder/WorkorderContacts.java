package com.fieldnation.data.workorder;

import android.os.Parcel;
import android.os.Parcelable;

import com.fieldnation.Log;
import com.fieldnation.json.JsonObject;
import com.fieldnation.json.Serializer;
import com.fieldnation.json.Unserializer;
import com.fieldnation.json.annotations.Json;

public class WorkorderContacts implements Parcelable {
    private static final String TAG = "WorkorderContacts";

    @Json(name = "name")
    private String _name;
    @Json(name = "phoneNumber")
    private String _phoneNumber;
    @Json(name = "role")
    private String _role;
    @Json(name = "email")
    private String _email;
    @Json(name = "phoneExt")
    private String _phoneExt;
    @Json(name = "note")
    private String _note;

    public WorkorderContacts() {
    }

    public String getName() {
        return _name;
    }

    public String getPhoneNumber() {
        return _phoneNumber;
    }

    public String getRole() {
        return _role;
    }

    public String getEmail() {
        return _email;
    }

    public String getPhoneExt() {
        return _phoneExt;
    }

    public String getNote() {
        return _note;
    }

    public JsonObject toJson() {
        return toJson(this);
    }

    public static JsonObject toJson(WorkorderContacts additionalExpense) {
        try {
            return Serializer.serializeObject(additionalExpense);
        } catch (Exception ex) {
            Log.v(TAG, ex);
            return null;
        }
    }

    public static WorkorderContacts fromJson(JsonObject json) {
        try {
            return Unserializer.unserializeObject(WorkorderContacts.class, json);
        } catch (Exception ex) {
            Log.v(TAG, ex);
            return null;
        }
    }


	/*-*********************************************-*/
    /*-			Parcelable Implementation			-*/
    /*-*********************************************-*/

    public static final Creator<WorkorderContacts> CREATOR = new Creator<WorkorderContacts>() {

        @Override
        public WorkorderContacts createFromParcel(Parcel source) {
            try {
                return WorkorderContacts.fromJson((JsonObject) (source.readParcelable(JsonObject.class.getClassLoader())));
            } catch (Exception e) {
                Log.v(TAG, e);
            }
            return null;
        }

        @Override
        public WorkorderContacts[] newArray(int size) {
            return new WorkorderContacts[size];
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
