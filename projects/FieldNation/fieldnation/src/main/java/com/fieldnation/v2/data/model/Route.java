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

public class Route implements Parcelable {
    private static final String TAG = "Route";

    @Json(name = "counter")
    private Boolean _counter;

    @Json(name = "created")
    private Date _created;

    @Json(name = "id")
    private Integer _id;

    @Json(name = "notes")
    private String _notes;

    @Json(name = "schedule")
    private Schedule _schedule;

    @Json(name = "user")
    private User _user;

    @Source
    private JsonObject SOURCE = new JsonObject();

    public Route() {
    }

    public void setCounter(Boolean counter) throws ParseException {
        _counter = counter;
        SOURCE.put("counter", counter);
    }

    public Boolean getCounter() {
        return _counter;
    }

    public Route counter(Boolean counter) throws ParseException {
        _counter = counter;
        SOURCE.put("counter", counter);
        return this;
    }

    public void setCreated(Date created) throws ParseException {
        _created = created;
        SOURCE.put("created", created.getJson());
    }

    public Date getCreated() {
        return _created;
    }

    public Route created(Date created) throws ParseException {
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

    public Route id(Integer id) throws ParseException {
        _id = id;
        SOURCE.put("id", id);
        return this;
    }

    public void setNotes(String notes) throws ParseException {
        _notes = notes;
        SOURCE.put("notes", notes);
    }

    public String getNotes() {
        return _notes;
    }

    public Route notes(String notes) throws ParseException {
        _notes = notes;
        SOURCE.put("notes", notes);
        return this;
    }

    public void setSchedule(Schedule schedule) throws ParseException {
        _schedule = schedule;
        SOURCE.put("schedule", schedule.getJson());
    }

    public Schedule getSchedule() {
        return _schedule;
    }

    public Route schedule(Schedule schedule) throws ParseException {
        _schedule = schedule;
        SOURCE.put("schedule", schedule.getJson());
        return this;
    }

    public void setUser(User user) throws ParseException {
        _user = user;
        SOURCE.put("user", user.getJson());
    }

    public User getUser() {
        return _user;
    }

    public Route user(User user) throws ParseException {
        _user = user;
        SOURCE.put("user", user.getJson());
        return this;
    }

    /*-*****************************-*/
    /*-             Json            -*/
    /*-*****************************-*/
    public static JsonArray toJsonArray(Route[] array) {
        JsonArray list = new JsonArray();
        for (Route item : array) {
            list.add(item.getJson());
        }
        return list;
    }

    public static Route[] fromJsonArray(JsonArray array) {
        Route[] list = new Route[array.size()];
        for (int i = 0; i < array.size(); i++) {
            list[i] = fromJson(array.getJsonObject(i));
        }
        return list;
    }

    public static Route fromJson(JsonObject obj) {
        try {
            return Unserializer.unserializeObject(Route.class, obj);
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
        dest.writeParcelable(getJson(), flags);
    }
}
