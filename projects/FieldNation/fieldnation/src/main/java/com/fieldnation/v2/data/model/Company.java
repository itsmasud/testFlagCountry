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
import com.fieldnation.fntools.misc;

import java.text.ParseException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by dmgen from swagger.
 */

public class Company implements Parcelable {
    private static final String TAG = "Company";

    @Json(name = "about")
    private String _about;

    @Json(name = "blocked")
    private Boolean _blocked;

    @Json(name = "features")
    private String[] _features;

    @Json(name = "id")
    private Integer _id;

    @Json(name = "jobs")
    private CompanyJobs _jobs;

    @Json(name = "location")
    private Location _location;

    @Json(name = "name")
    private String _name;

    @Json(name = "photo")
    private String _photo;

    @Json(name = "provider_count")
    private Integer _providerCount;

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

    public void setAbout(String about) throws ParseException {
        _about = about;
        SOURCE.put("about", about);
    }

    public String getAbout() {
        try {
            if (_about == null && SOURCE.has("about") && SOURCE.get("about") != null)
                _about = SOURCE.getString("about");
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _about;
    }

    public Company about(String about) throws ParseException {
        _about = about;
        SOURCE.put("about", about);
        return this;
    }

    public void setBlocked(Boolean blocked) throws ParseException {
        _blocked = blocked;
        SOURCE.put("blocked", blocked);
    }

    public Boolean getBlocked() {
        try {
            if (_blocked == null && SOURCE.has("blocked") && SOURCE.get("blocked") != null)
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
            if (_id == null && SOURCE.has("id") && SOURCE.get("id") != null)
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

    public void setJobs(CompanyJobs jobs) throws ParseException {
        _jobs = jobs;
        SOURCE.put("jobs", jobs.getJson());
    }

    public CompanyJobs getJobs() {
        try {
            if (_jobs == null && SOURCE.has("jobs") && SOURCE.get("jobs") != null)
                _jobs = CompanyJobs.fromJson(SOURCE.getJsonObject("jobs"));
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        if (_jobs != null && _jobs.isSet())
            return _jobs;

        return null;
    }

    public Company jobs(CompanyJobs jobs) throws ParseException {
        _jobs = jobs;
        SOURCE.put("jobs", jobs.getJson());
        return this;
    }

    public void setLocation(Location location) throws ParseException {
        _location = location;
        SOURCE.put("location", location.getJson());
    }

    public Location getLocation() {
        try {
            if (_location == null && SOURCE.has("location") && SOURCE.get("location") != null)
                _location = Location.fromJson(SOURCE.getJsonObject("location"));
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        if (_location != null && _location.isSet())
            return _location;

        return null;
    }

    public Company location(Location location) throws ParseException {
        _location = location;
        SOURCE.put("location", location.getJson());
        return this;
    }

    public void setName(String name) throws ParseException {
        _name = name;
        SOURCE.put("name", name);
    }

    public String getName() {
        try {
            if (_name == null && SOURCE.has("name") && SOURCE.get("name") != null)
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

    public void setPhoto(String photo) throws ParseException {
        _photo = photo;
        SOURCE.put("photo", photo);
    }

    public String getPhoto() {
        try {
            if (_photo == null && SOURCE.has("photo") && SOURCE.get("photo") != null)
                _photo = SOURCE.getString("photo");
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _photo;
    }

    public Company photo(String photo) throws ParseException {
        _photo = photo;
        SOURCE.put("photo", photo);
        return this;
    }

    public void setProviderCount(Integer providerCount) throws ParseException {
        _providerCount = providerCount;
        SOURCE.put("provider_count", providerCount);
    }

    public Integer getProviderCount() {
        try {
            if (_providerCount == null && SOURCE.has("provider_count") && SOURCE.get("provider_count") != null)
                _providerCount = SOURCE.getInt("provider_count");
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _providerCount;
    }

    public Company providerCount(Integer providerCount) throws ParseException {
        _providerCount = providerCount;
        SOURCE.put("provider_count", providerCount);
        return this;
    }

    public void setRating(Rating rating) throws ParseException {
        _rating = rating;
        SOURCE.put("rating", rating.getJson());
    }

    public Rating getRating() {
        try {
            if (_rating == null && SOURCE.has("rating") && SOURCE.get("rating") != null)
                _rating = Rating.fromJson(SOURCE.getJsonObject("rating"));
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        if (_rating != null && _rating.isSet())
            return _rating;

        return null;
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
            Log.v(TAG, ex);
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

    /*-*****************************-*/
    /*-         Human Code          -*/
    /*-*****************************-*/

    public boolean isSet() {
        return getId() != null && getId() != 0;
    }
}
