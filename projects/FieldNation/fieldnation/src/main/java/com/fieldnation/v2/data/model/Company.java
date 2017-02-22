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

public class Company implements Parcelable {
    private static final String TAG = "Company";

    @Json(name = "blocked")
    private Boolean _blocked;

    @Json(name = "features")
    private String[] _features;

    @Json(name = "id")
    private Integer _id;

    @Json(name = "name")
    private String _name;

    @Json(name = "rating")
    private Rating _rating;

    @Source
    private JsonObject SOURCE;

    public Company() {
        SOURCE = new JsonObject();
    }

    public Company(JsonObject obj) {
        SOURCE = obj;
    }

    public void setBlocked(Boolean blocked) throws ParseException {
        _blocked = blocked;
        SOURCE.put("blocked", blocked);
    }

    public Boolean getBlocked() {
        try {
            if (_blocked != null)
                return _blocked;

            if (SOURCE.has("blocked") && SOURCE.get("blocked") != null)
                _blocked = SOURCE.getBoolean("blocked");

        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _blocked;
    }

    public Company blocked(Boolean blocked) throws ParseException {
        _blocked = blocked;
        SOURCE.put("blocked", blocked);
        return this;
    }

    public void setFeatures(String[] features) throws ParseException {
        _features = features;
        JsonArray ja = new JsonArray();
        for (String item : features) {
            ja.add(item);
        }
        SOURCE.put("features", ja);
    }

    public String[] getFeatures() {
        try {
            if (_features != null)
                return _features;

            if (SOURCE.has("features") && SOURCE.get("features") != null) {
                JsonArray ja = SOURCE.getJsonArray("features");
                _features = ja.toArray(new String[ja.size()]);
            }

        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _features;
    }

    public Company features(String[] features) throws ParseException {
        _features = features;
        JsonArray ja = new JsonArray();
        for (String item : features) {
            ja.add(item);
        }
        SOURCE.put("features", ja, true);
        return this;
    }

    public void setId(Integer id) throws ParseException {
        _id = id;
        SOURCE.put("id", id);
    }

    public Integer getId() {
        try {
            if (_id != null)
                return _id;

            if (SOURCE.has("id") && SOURCE.get("id") != null)
                _id = SOURCE.getInt("id");

        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _id;
    }

    public Company id(Integer id) throws ParseException {
        _id = id;
        SOURCE.put("id", id);
        return this;
    }

    public void setName(String name) throws ParseException {
        _name = name;
        SOURCE.put("name", name);
    }

    public String getName() {
        try {
            if (_name != null)
                return _name;

            if (SOURCE.has("name") && SOURCE.get("name") != null)
                _name = SOURCE.getString("name");

        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _name;
    }

    public Company name(String name) throws ParseException {
        _name = name;
        SOURCE.put("name", name);
        return this;
    }

    public void setRating(Rating rating) throws ParseException {
        _rating = rating;
        SOURCE.put("rating", rating.getJson());
    }

    public Rating getRating() {
        try {
            if (_rating != null)
                return _rating;

            if (SOURCE.has("rating") && SOURCE.get("rating") != null)
                _rating = Rating.fromJson(SOURCE.getJsonObject("rating"));

        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _rating;
    }

    public Company rating(Rating rating) throws ParseException {
        _rating = rating;
        SOURCE.put("rating", rating.getJson());
        return this;
    }

    /*-*****************************-*/
    /*-             Json            -*/
    /*-*****************************-*/
    public static JsonArray toJsonArray(Company[] array) {
        JsonArray list = new JsonArray();
        for (Company item : array) {
            list.add(item.getJson());
        }
        return list;
    }

    public static Company[] fromJsonArray(JsonArray array) {
        Company[] list = new Company[array.size()];
        for (int i = 0; i < array.size(); i++) {
            list[i] = fromJson(array.getJsonObject(i));
        }
        return list;
    }

    public static Company fromJson(JsonObject obj) {
        try {
            return new Company(obj);
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
    public static final Parcelable.Creator<Company> CREATOR = new Parcelable.Creator<Company>() {

        @Override
        public Company createFromParcel(Parcel source) {
            try {
                return Company.fromJson((JsonObject) source.readParcelable(JsonObject.class.getClassLoader()));
            } catch (Exception ex) {
                Log.v(TAG, ex);
                return null;
            }
        }

        @Override
        public Company[] newArray(int size) {
            return new Company[size];
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
