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

public class UserBlockedBy implements Parcelable {
    private static final String TAG = "UserBlockedBy";

    @Json(name = "id")
    private Integer _id;

    @Json(name = "name")
    private String _name;

    @Source
    private JsonObject SOURCE = new JsonObject();

    public UserBlockedBy() {
    }

    public void setId(Integer id) throws ParseException {
        _id = id;
        SOURCE.put("id", id);
    }

    public Integer getId() {
        return _id;
    }

    public UserBlockedBy id(Integer id) throws ParseException {
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

    public UserBlockedBy name(String name) throws ParseException {
        _name = name;
        SOURCE.put("name", name);
        return this;
    }

    /*-*****************************-*/
    /*-             Json            -*/
    /*-*****************************-*/
    public static JsonArray toJsonArray(UserBlockedBy[] array) {
        JsonArray list = new JsonArray();
        for (UserBlockedBy item : array) {
            list.add(item.getJson());
        }
        return list;
    }

    public static UserBlockedBy[] fromJsonArray(JsonArray array) {
        UserBlockedBy[] list = new UserBlockedBy[array.size()];
        for (int i = 0; i < array.size(); i++) {
            list[i] = fromJson(array.getJsonObject(i));
        }
        return list;
    }

    public static UserBlockedBy fromJson(JsonObject obj) {
        try {
            return Unserializer.unserializeObject(UserBlockedBy.class, obj);
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
    public static final Parcelable.Creator<UserBlockedBy> CREATOR = new Parcelable.Creator<UserBlockedBy>() {

        @Override
        public UserBlockedBy createFromParcel(Parcel source) {
            try {
                return UserBlockedBy.fromJson((JsonObject) source.readParcelable(JsonObject.class.getClassLoader()));
            } catch (Exception ex) {
                Log.v(TAG, ex);
                return null;
            }
        }

        @Override
        public UserBlockedBy[] newArray(int size) {
            return new UserBlockedBy[size];
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
