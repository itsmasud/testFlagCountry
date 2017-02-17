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

public class Users implements Parcelable {
    private static final String TAG = "Users";

    @Json(name = "metadata")
    private ListEnvelope _metadata;

    @Json(name = "results")
    private User[] _results;

    @Source
    private JsonObject SOURCE = new JsonObject();

    public Users() {
    }

    public void setMetadata(ListEnvelope metadata) throws ParseException {
        _metadata = metadata;
        SOURCE.put("metadata", metadata.getJson());
    }

    public ListEnvelope getMetadata() {
        return _metadata;
    }

    public Users metadata(ListEnvelope metadata) throws ParseException {
        _metadata = metadata;
        SOURCE.put("metadata", metadata.getJson());
        return this;
    }

    public void setResults(User[] results) throws ParseException {
        _results = results;
        SOURCE.put("results", User.toJsonArray(results));
    }

    public User[] getResults() {
        return _results;
    }

    public Users results(User[] results) throws ParseException {
        _results = results;
        SOURCE.put("results", User.toJsonArray(results), true);
        return this;
    }

    /*-*****************************-*/
    /*-             Json            -*/
    /*-*****************************-*/
    public static JsonArray toJsonArray(Users[] array) {
        JsonArray list = new JsonArray();
        for (Users item : array) {
            list.add(item.getJson());
        }
        return list;
    }

    public static Users[] fromJsonArray(JsonArray array) {
        Users[] list = new Users[array.size()];
        for (int i = 0; i < array.size(); i++) {
            list[i] = fromJson(array.getJsonObject(i));
        }
        return list;
    }

    public static Users fromJson(JsonObject obj) {
        try {
            return Unserializer.unserializeObject(Users.class, obj);
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
    public static final Parcelable.Creator<Users> CREATOR = new Parcelable.Creator<Users>() {

        @Override
        public Users createFromParcel(Parcel source) {
            try {
                return Users.fromJson((JsonObject) source.readParcelable(JsonObject.class.getClassLoader()));
            } catch (Exception ex) {
                Log.v(TAG, ex);
                return null;
            }
        }

        @Override
        public Users[] newArray(int size) {
            return new Users[size];
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
