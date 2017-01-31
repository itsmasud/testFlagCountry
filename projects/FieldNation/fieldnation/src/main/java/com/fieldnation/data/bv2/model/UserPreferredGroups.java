package com.fieldnation.data.bv2.model;
import android.os.Parcel;
import android.os.Parcelable;

import com.fieldnation.fnjson.JsonArray;
import com.fieldnation.fnjson.JsonObject;
import com.fieldnation.fnjson.Serializer;
import com.fieldnation.fnjson.Unserializer;
import com.fieldnation.fnjson.annotations.Json;
import com.fieldnation.fnlog.Log;

/**
 * Created by dmgen from swagger on 1/31/17.
 */

public class UserPreferredGroups implements Parcelable {
    private static final String TAG = "UserPreferredGroups";

    @Json(name = "notes")
    private String _notes;

    @Json(name = "created")
    private Date _created;

    @Json(name = "name")
    private String _name;

    @Json(name = "id")
    private Integer _id;

    public UserPreferredGroups() {
    }

    public void setNotes(String notes) {
        _notes = notes;
    }

    public String getNotes() {
        return _notes;
    }

    public UserPreferredGroups notes(String notes) {
        _notes = notes;
        return this;
    }

    public void setCreated(Date created) {
        _created = created;
    }

    public Date getCreated() {
        return _created;
    }

    public UserPreferredGroups created(Date created) {
        _created = created;
        return this;
    }

    public void setName(String name) {
        _name = name;
    }

    public String getName() {
        return _name;
    }

    public UserPreferredGroups name(String name) {
        _name = name;
        return this;
    }

    public void setId(Integer id) {
        _id = id;
    }

    public Integer getId() {
        return _id;
    }

    public UserPreferredGroups id(Integer id) {
        _id = id;
        return this;
    }

    /*-*****************************-*/
    /*-             Json            -*/
    /*-*****************************-*/
    public static UserPreferredGroups[] fromJsonArray(JsonArray array) {
        UserPreferredGroups[] list = new UserPreferredGroups[array.size()];
        for (int i = 0; i < array.size(); i++) {
            list[i] = fromJson(array.getJsonObject(i));
        }
        return list;
    }

    public static UserPreferredGroups fromJson(JsonObject obj) {
        try {
            return Unserializer.unserializeObject(UserPreferredGroups.class, obj);
        } catch (Exception ex) {
            Log.v(TAG, TAG, ex);
            return null;
        }
    }

    public JsonObject toJson() {
        return toJson(this);
    }

    public static JsonObject toJson(UserPreferredGroups userPreferredGroups) {
        try {
            return Serializer.serializeObject(userPreferredGroups);
        } catch (Exception ex) {
            Log.v(TAG, TAG, ex);
            return null;
        }
    }

    /*-*********************************************-*/
    /*-			Parcelable Implementation           -*/
    /*-*********************************************-*/
    public static final Parcelable.Creator<UserPreferredGroups> CREATOR = new Parcelable.Creator<UserPreferredGroups>() {

        @Override
        public UserPreferredGroups createFromParcel(Parcel source) {
            try {
                return UserPreferredGroups.fromJson((JsonObject) source.readParcelable(JsonObject.class.getClassLoader()));
            } catch (Exception ex) {
                Log.v(TAG, ex);
                return null;
            }
        }

        @Override
        public UserPreferredGroups[] newArray(int size) {
            return new UserPreferredGroups[size];
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
