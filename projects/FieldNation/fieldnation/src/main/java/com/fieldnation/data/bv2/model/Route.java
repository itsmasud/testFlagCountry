package com.fieldnation.data.bv2.model;
import android.os.Parcel;
import android.os.Parcelable;

import com.fieldnation.fnjson.JsonObject;
import com.fieldnation.fnjson.Serializer;
import com.fieldnation.fnjson.Unserializer;
import com.fieldnation.fnjson.annotations.Json;
import com.fieldnation.fnlog.Log;

/**
 * Created by dmgen from swagger on 1/30/17.
 */

public class Route implements Parcelable {
    private static final String TAG = "Route";

    @Json(name = "schedule")
    private Schedule _schedule;

    @Json(name = "notes")
    private String _notes;

    @Json(name = "created")
    private Date _created;

    @Json(name = "id")
    private Integer _id;

    @Json(name = "counter")
    private Boolean _counter;

    @Json(name = "user")
    private User _user;

    public Route() {
    }

    public void setSchedule(Schedule schedule) {
        _schedule = schedule;
    }

    public Schedule getSchedule() {
        return _schedule;
    }

    public Route schedule(Schedule schedule) {
        _schedule = schedule;
        return this;
    }

    public void setNotes(String notes) {
        _notes = notes;
    }

    public String getNotes() {
        return _notes;
    }

    public Route notes(String notes) {
        _notes = notes;
        return this;
    }

    public void setCreated(Date created) {
        _created = created;
    }

    public Date getCreated() {
        return _created;
    }

    public Route created(Date created) {
        _created = created;
        return this;
    }

    public void setId(Integer id) {
        _id = id;
    }

    public Integer getId() {
        return _id;
    }

    public Route id(Integer id) {
        _id = id;
        return this;
    }

    public void setCounter(Boolean counter) {
        _counter = counter;
    }

    public Boolean getCounter() {
        return _counter;
    }

    public Route counter(Boolean counter) {
        _counter = counter;
        return this;
    }

    public void setUser(User user) {
        _user = user;
    }

    public User getUser() {
        return _user;
    }

    public Route user(User user) {
        _user = user;
        return this;
    }

    /*-*****************************-*/
    /*-             Json            -*/
    /*-*****************************-*/
    public static Route fromJson(JsonObject obj) {
        try {
            return Unserializer.unserializeObject(Route.class, obj);
        } catch (Exception ex) {
            Log.v(TAG, TAG, ex);
            return null;
        }
    }

    public JsonObject toJson() {
        return toJson(this);
    }

    public static JsonObject toJson(Route route) {
        try {
            return Serializer.serializeObject(route);
        } catch (Exception ex) {
            Log.v(TAG, TAG, ex);
            return null;
        }
    }

    /*-*********************************************-*/
    /*-			Parcelable Implementation           -*/
    /*-*********************************************-*/
    public static final Parcelable.Creator<Route> CREATOR = new Parcelable.Creator<Route>() {

        @Override
        public Route createFromParcel(Parcel source) {
            try {
                return Route.fromJson((JsonObject) source.readParcelable(JsonObject.class.getClassLoader()));
            } catch (Exception ex) {
                Log.v(TAG, ex);
                return null;
            }
        }

        @Override
        public Route[] newArray(int size) {
            return new Route[size];
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
