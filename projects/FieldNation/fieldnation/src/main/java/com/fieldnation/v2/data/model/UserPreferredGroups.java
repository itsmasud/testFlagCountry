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

public class UserPreferredGroups implements Parcelable {
    private static final String TAG = "UserPreferredGroups";

    @Json(name = "created")
    private Date _created;

    @Json(name = "id")
    private Integer _id;

    @Json(name = "name")
    private String _name;

    @Json(name = "notes")
    private String _notes;

    @Source
    private JsonObject SOURCE = new JsonObject();

    public UserPreferredGroups() {
    }

    public void setCreated(Date created) throws ParseException {
        _created = created;
        SOURCE.put("created", created.getJson());
    }

    public Date getCreated() {
        return _created;
    }

    public UserPreferredGroups created(Date created) throws ParseException {
        _created = created;
        SOURCE.put("created", created.getJson());
        return this;
    }

    public void setId(Integer id) throws ParseException {
        _id = id;
        SOURCE.put("id", id);
    }

    public Integer getId() {
        return _id;
    }

    public UserPreferredGroups id(Integer id) throws ParseException {
        _id = id;
        SOURCE.put("id", id);
        return this;
    }

    public void setName(String name) throws ParseException {
        _name = name;
        SOURCE.put("name", name);
    }

    public String getName() {
        return _name;
    }

    public UserPreferredGroups name(String name) throws ParseException {
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

    public UserPreferredGroups notes(String notes) throws ParseException {
        _notes = notes;
        SOURCE.put("notes", notes);
        return this;
    }

    /*-*****************************-*/
    /*-             Json            -*/
    /*-*****************************-*/
    public static JsonArray toJsonArray(UserPreferredGroups[] array) {
        JsonArray list = new JsonArray();
        for (UserPreferredGroups item : array) {
            list.add(item.getJson());
        }
        return list;
    }

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

    public JsonObject getJson() {
        return SOURCE;
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
        dest.writeParcelable(getJson(), flags);
    }
}
