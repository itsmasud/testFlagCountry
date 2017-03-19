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

public class User implements Parcelable {
    private static final String TAG = "User";

    @Json(name = "address")
    private String _address;

    @Json(name = "background_checks")
    private BackgroundCheck[] _backgroundChecks;

    @Json(name = "blocked")
    private UserBlocked[] _blocked;

    @Json(name = "city")
    private String _city;

    @Json(name = "client")
    private Company _client;

    @Json(name = "company")
    private UserCompany _company;

    @Json(name = "coords")
    private Coords _coords;

    @Json(name = "country")
    private String _country;

    @Json(name = "drug_tests")
    private DrugTest[] _drugTests;

    @Json(name = "email")
    private String _email;

    @Json(name = "first_name")
    private String _firstName;

    @Json(name = "id")
    private Integer _id;

    @Json(name = "jobs")
    private UserJobs _jobs;

    @Json(name = "last_active")
    private Date _lastActive;

    @Json(name = "last_name")
    private String _lastName;

    @Json(name = "managed")
    private Boolean _managed;

    @Json(name = "notes")
    private String _notes;

    @Json(name = "phone")
    private String _phone;

    @Json(name = "preferred_groups")
    private UserPreferredGroups[] _preferredGroups;

    @Json(name = "protec")
    private Boolean _protec;

    @Json(name = "rating")
    private UserRating _rating;

    @Json(name = "request")
    private Request _request;

    @Json(name = "role")
    private String _role;

    @Json(name = "route")
    private Route _route;

    @Json(name = "state")
    private String _state;

    @Json(name = "thumbnail")
    private String _thumbnail;

    @Json(name = "time_zone")
    private TimeZone _timeZone;

    @Json(name = "upcoming_schedule")
    private UserUpcomingSchedule[] _upcomingSchedule;

    @Json(name = "website")
    private String _website;

    @Json(name = "worked_with")
    private Boolean _workedWith;

    @Json(name = "worker_compensation")
    private Boolean _workerCompensation;

    @Json(name = "zip")
    private String _zip;

    @Source
    private JsonObject SOURCE;

    public User() {
        SOURCE = new JsonObject();
    }

    public User(JsonObject obj) {
        SOURCE = obj;
    }

    public void setAddress(String address) throws ParseException {
        _address = address;
        SOURCE.put("address", address);
    }

    public String getAddress() {
        try {
            if (_address == null && SOURCE.has("address") && SOURCE.get("address") != null)
                _address = SOURCE.getString("address");
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _address;
    }

    public User address(String address) throws ParseException {
        _address = address;
        SOURCE.put("address", address);
        return this;
    }

    public void setBackgroundChecks(BackgroundCheck[] backgroundChecks) throws ParseException {
        _backgroundChecks = backgroundChecks;
        SOURCE.put("background_checks", BackgroundCheck.toJsonArray(backgroundChecks));
    }

    public BackgroundCheck[] getBackgroundChecks() {
        try {
            if (_backgroundChecks != null)
                return _backgroundChecks;

            if (SOURCE.has("background_checks") && SOURCE.get("background_checks") != null) {
                _backgroundChecks = BackgroundCheck.fromJsonArray(SOURCE.getJsonArray("background_checks"));
            }

        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _backgroundChecks;
    }

    public User backgroundChecks(BackgroundCheck[] backgroundChecks) throws ParseException {
        _backgroundChecks = backgroundChecks;
        SOURCE.put("background_checks", BackgroundCheck.toJsonArray(backgroundChecks), true);
        return this;
    }

    public void setBlocked(UserBlocked[] blocked) throws ParseException {
        _blocked = blocked;
        SOURCE.put("blocked", UserBlocked.toJsonArray(blocked));
    }

    public UserBlocked[] getBlocked() {
        try {
            if (_blocked != null)
                return _blocked;

            if (SOURCE.has("blocked") && SOURCE.get("blocked") != null) {
                _blocked = UserBlocked.fromJsonArray(SOURCE.getJsonArray("blocked"));
            }

        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _blocked;
    }

    public User blocked(UserBlocked[] blocked) throws ParseException {
        _blocked = blocked;
        SOURCE.put("blocked", UserBlocked.toJsonArray(blocked), true);
        return this;
    }

    public void setCity(String city) throws ParseException {
        _city = city;
        SOURCE.put("city", city);
    }

    public String getCity() {
        try {
            if (_city == null && SOURCE.has("city") && SOURCE.get("city") != null)
                _city = SOURCE.getString("city");
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _city;
    }

    public User city(String city) throws ParseException {
        _city = city;
        SOURCE.put("city", city);
        return this;
    }

    public void setClient(Company client) throws ParseException {
        _client = client;
        SOURCE.put("client", client.getJson());
    }

    public Company getClient() {
        try {
            if (_client == null && SOURCE.has("client") && SOURCE.get("client") != null)
                _client = Company.fromJson(SOURCE.getJsonObject("client"));
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        if (_client != null && _client.isSet())
            return _client;

        return null;
    }

    public User client(Company client) throws ParseException {
        _client = client;
        SOURCE.put("client", client.getJson());
        return this;
    }

    public void setCompany(UserCompany company) throws ParseException {
        _company = company;
        SOURCE.put("company", company.getJson());
    }

    public UserCompany getCompany() {
        try {
            if (_company == null && SOURCE.has("company") && SOURCE.get("company") != null)
                _company = UserCompany.fromJson(SOURCE.getJsonObject("company"));
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        if (_company != null && _company.isSet())
            return _company;

        return null;
    }

    public User company(UserCompany company) throws ParseException {
        _company = company;
        SOURCE.put("company", company.getJson());
        return this;
    }

    public void setCoords(Coords coords) throws ParseException {
        _coords = coords;
        SOURCE.put("coords", coords.getJson());
    }

    public Coords getCoords() {
        try {
            if (_coords == null && SOURCE.has("coords") && SOURCE.get("coords") != null)
                _coords = Coords.fromJson(SOURCE.getJsonObject("coords"));
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        if (_coords != null && _coords.isSet())
            return _coords;

        return null;
    }

    public User coords(Coords coords) throws ParseException {
        _coords = coords;
        SOURCE.put("coords", coords.getJson());
        return this;
    }

    public void setCountry(String country) throws ParseException {
        _country = country;
        SOURCE.put("country", country);
    }

    public String getCountry() {
        try {
            if (_country == null && SOURCE.has("country") && SOURCE.get("country") != null)
                _country = SOURCE.getString("country");
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _country;
    }

    public User country(String country) throws ParseException {
        _country = country;
        SOURCE.put("country", country);
        return this;
    }

    public void setDrugTests(DrugTest[] drugTests) throws ParseException {
        _drugTests = drugTests;
        SOURCE.put("drug_tests", DrugTest.toJsonArray(drugTests));
    }

    public DrugTest[] getDrugTests() {
        try {
            if (_drugTests != null)
                return _drugTests;

            if (SOURCE.has("drug_tests") && SOURCE.get("drug_tests") != null) {
                _drugTests = DrugTest.fromJsonArray(SOURCE.getJsonArray("drug_tests"));
            }

        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _drugTests;
    }

    public User drugTests(DrugTest[] drugTests) throws ParseException {
        _drugTests = drugTests;
        SOURCE.put("drug_tests", DrugTest.toJsonArray(drugTests), true);
        return this;
    }

    public void setEmail(String email) throws ParseException {
        _email = email;
        SOURCE.put("email", email);
    }

    public String getEmail() {
        try {
            if (_email == null && SOURCE.has("email") && SOURCE.get("email") != null)
                _email = SOURCE.getString("email");
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _email;
    }

    public User email(String email) throws ParseException {
        _email = email;
        SOURCE.put("email", email);
        return this;
    }

    public void setFirstName(String firstName) throws ParseException {
        _firstName = firstName;
        SOURCE.put("first_name", firstName);
    }

    public String getFirstName() {
        try {
            if (_firstName == null && SOURCE.has("first_name") && SOURCE.get("first_name") != null)
                _firstName = SOURCE.getString("first_name");
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _firstName;
    }

    public User firstName(String firstName) throws ParseException {
        _firstName = firstName;
        SOURCE.put("first_name", firstName);
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

    public User id(Integer id) throws ParseException {
        _id = id;
        SOURCE.put("id", id);
        return this;
    }

    public void setJobs(UserJobs jobs) throws ParseException {
        _jobs = jobs;
        SOURCE.put("jobs", jobs.getJson());
    }

    public UserJobs getJobs() {
        try {
            if (_jobs == null && SOURCE.has("jobs") && SOURCE.get("jobs") != null)
                _jobs = UserJobs.fromJson(SOURCE.getJsonObject("jobs"));
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        if (_jobs != null && _jobs.isSet())
            return _jobs;

        return null;
    }

    public User jobs(UserJobs jobs) throws ParseException {
        _jobs = jobs;
        SOURCE.put("jobs", jobs.getJson());
        return this;
    }

    public void setLastActive(Date lastActive) throws ParseException {
        _lastActive = lastActive;
        SOURCE.put("last_active", lastActive.getJson());
    }

    public Date getLastActive() {
        try {
            if (_lastActive == null && SOURCE.has("last_active") && SOURCE.get("last_active") != null)
                _lastActive = Date.fromJson(SOURCE.getJsonObject("last_active"));
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        if (_lastActive != null && _lastActive.isSet())
            return _lastActive;

        return null;
    }

    public User lastActive(Date lastActive) throws ParseException {
        _lastActive = lastActive;
        SOURCE.put("last_active", lastActive.getJson());
        return this;
    }

    public void setLastName(String lastName) throws ParseException {
        _lastName = lastName;
        SOURCE.put("last_name", lastName);
    }

    public String getLastName() {
        try {
            if (_lastName == null && SOURCE.has("last_name") && SOURCE.get("last_name") != null)
                _lastName = SOURCE.getString("last_name");
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _lastName;
    }

    public User lastName(String lastName) throws ParseException {
        _lastName = lastName;
        SOURCE.put("last_name", lastName);
        return this;
    }

    public void setManaged(Boolean managed) throws ParseException {
        _managed = managed;
        SOURCE.put("managed", managed);
    }

    public Boolean getManaged() {
        try {
            if (_managed == null && SOURCE.has("managed") && SOURCE.get("managed") != null)
                _managed = SOURCE.getBoolean("managed");
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _managed;
    }

    public User managed(Boolean managed) throws ParseException {
        _managed = managed;
        SOURCE.put("managed", managed);
        return this;
    }

    public void setNotes(String notes) throws ParseException {
        _notes = notes;
        SOURCE.put("notes", notes);
    }

    public String getNotes() {
        try {
            if (_notes == null && SOURCE.has("notes") && SOURCE.get("notes") != null)
                _notes = SOURCE.getString("notes");
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _notes;
    }

    public User notes(String notes) throws ParseException {
        _notes = notes;
        SOURCE.put("notes", notes);
        return this;
    }

    public void setPhone(String phone) throws ParseException {
        _phone = phone;
        SOURCE.put("phone", phone);
    }

    public String getPhone() {
        try {
            if (_phone == null && SOURCE.has("phone") && SOURCE.get("phone") != null)
                _phone = SOURCE.getString("phone");
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _phone;
    }

    public User phone(String phone) throws ParseException {
        _phone = phone;
        SOURCE.put("phone", phone);
        return this;
    }

    public void setPreferredGroups(UserPreferredGroups[] preferredGroups) throws ParseException {
        _preferredGroups = preferredGroups;
        SOURCE.put("preferred_groups", UserPreferredGroups.toJsonArray(preferredGroups));
    }

    public UserPreferredGroups[] getPreferredGroups() {
        try {
            if (_preferredGroups != null)
                return _preferredGroups;

            if (SOURCE.has("preferred_groups") && SOURCE.get("preferred_groups") != null) {
                _preferredGroups = UserPreferredGroups.fromJsonArray(SOURCE.getJsonArray("preferred_groups"));
            }

        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _preferredGroups;
    }

    public User preferredGroups(UserPreferredGroups[] preferredGroups) throws ParseException {
        _preferredGroups = preferredGroups;
        SOURCE.put("preferred_groups", UserPreferredGroups.toJsonArray(preferredGroups), true);
        return this;
    }

    public void setProtec(Boolean protec) throws ParseException {
        _protec = protec;
        SOURCE.put("protec", protec);
    }

    public Boolean getProtec() {
        try {
            if (_protec == null && SOURCE.has("protec") && SOURCE.get("protec") != null)
                _protec = SOURCE.getBoolean("protec");
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _protec;
    }

    public User protec(Boolean protec) throws ParseException {
        _protec = protec;
        SOURCE.put("protec", protec);
        return this;
    }

    public void setRating(UserRating rating) throws ParseException {
        _rating = rating;
        SOURCE.put("rating", rating.getJson());
    }

    public UserRating getRating() {
        try {
            if (_rating == null && SOURCE.has("rating") && SOURCE.get("rating") != null)
                _rating = UserRating.fromJson(SOURCE.getJsonObject("rating"));
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        if (_rating != null && _rating.isSet())
            return _rating;

        return null;
    }

    public User rating(UserRating rating) throws ParseException {
        _rating = rating;
        SOURCE.put("rating", rating.getJson());
        return this;
    }

    public void setRequest(Request request) throws ParseException {
        _request = request;
        SOURCE.put("request", request.getJson());
    }

    public Request getRequest() {
        try {
            if (_request == null && SOURCE.has("request") && SOURCE.get("request") != null)
                _request = Request.fromJson(SOURCE.getJsonObject("request"));
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        if (_request != null && _request.isSet())
            return _request;

        return null;
    }

    public User request(Request request) throws ParseException {
        _request = request;
        SOURCE.put("request", request.getJson());
        return this;
    }

    public void setRole(String role) throws ParseException {
        _role = role;
        SOURCE.put("role", role);
    }

    public String getRole() {
        try {
            if (_role == null && SOURCE.has("role") && SOURCE.get("role") != null)
                _role = SOURCE.getString("role");
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _role;
    }

    public User role(String role) throws ParseException {
        _role = role;
        SOURCE.put("role", role);
        return this;
    }

    public void setRoute(Route route) throws ParseException {
        _route = route;
        SOURCE.put("route", route.getJson());
    }

    public Route getRoute() {
        try {
            if (_route == null && SOURCE.has("route") && SOURCE.get("route") != null)
                _route = Route.fromJson(SOURCE.getJsonObject("route"));
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        if (_route != null && _route.isSet())
            return _route;

        return null;
    }

    public User route(Route route) throws ParseException {
        _route = route;
        SOURCE.put("route", route.getJson());
        return this;
    }

    public void setState(String state) throws ParseException {
        _state = state;
        SOURCE.put("state", state);
    }

    public String getState() {
        try {
            if (_state == null && SOURCE.has("state") && SOURCE.get("state") != null)
                _state = SOURCE.getString("state");
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _state;
    }

    public User state(String state) throws ParseException {
        _state = state;
        SOURCE.put("state", state);
        return this;
    }

    public void setThumbnail(String thumbnail) throws ParseException {
        _thumbnail = thumbnail;
        SOURCE.put("thumbnail", thumbnail);
    }

    public String getThumbnail() {
        try {
            if (_thumbnail == null && SOURCE.has("thumbnail") && SOURCE.get("thumbnail") != null)
                _thumbnail = SOURCE.getString("thumbnail");
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _thumbnail;
    }

    public User thumbnail(String thumbnail) throws ParseException {
        _thumbnail = thumbnail;
        SOURCE.put("thumbnail", thumbnail);
        return this;
    }

    public void setTimeZone(TimeZone timeZone) throws ParseException {
        _timeZone = timeZone;
        SOURCE.put("time_zone", timeZone.getJson());
    }

    public TimeZone getTimeZone() {
        try {
            if (_timeZone == null && SOURCE.has("time_zone") && SOURCE.get("time_zone") != null)
                _timeZone = TimeZone.fromJson(SOURCE.getJsonObject("time_zone"));
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        if (_timeZone != null && _timeZone.isSet())
            return _timeZone;

        return null;
    }

    public User timeZone(TimeZone timeZone) throws ParseException {
        _timeZone = timeZone;
        SOURCE.put("time_zone", timeZone.getJson());
        return this;
    }

    public void setUpcomingSchedule(UserUpcomingSchedule[] upcomingSchedule) throws ParseException {
        _upcomingSchedule = upcomingSchedule;
        SOURCE.put("upcoming_schedule", UserUpcomingSchedule.toJsonArray(upcomingSchedule));
    }

    public UserUpcomingSchedule[] getUpcomingSchedule() {
        try {
            if (_upcomingSchedule != null)
                return _upcomingSchedule;

            if (SOURCE.has("upcoming_schedule") && SOURCE.get("upcoming_schedule") != null) {
                _upcomingSchedule = UserUpcomingSchedule.fromJsonArray(SOURCE.getJsonArray("upcoming_schedule"));
            }

        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _upcomingSchedule;
    }

    public User upcomingSchedule(UserUpcomingSchedule[] upcomingSchedule) throws ParseException {
        _upcomingSchedule = upcomingSchedule;
        SOURCE.put("upcoming_schedule", UserUpcomingSchedule.toJsonArray(upcomingSchedule), true);
        return this;
    }

    public void setWebsite(String website) throws ParseException {
        _website = website;
        SOURCE.put("website", website);
    }

    public String getWebsite() {
        try {
            if (_website == null && SOURCE.has("website") && SOURCE.get("website") != null)
                _website = SOURCE.getString("website");
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _website;
    }

    public User website(String website) throws ParseException {
        _website = website;
        SOURCE.put("website", website);
        return this;
    }

    public void setWorkedWith(Boolean workedWith) throws ParseException {
        _workedWith = workedWith;
        SOURCE.put("worked_with", workedWith);
    }

    public Boolean getWorkedWith() {
        try {
            if (_workedWith == null && SOURCE.has("worked_with") && SOURCE.get("worked_with") != null)
                _workedWith = SOURCE.getBoolean("worked_with");
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _workedWith;
    }

    public User workedWith(Boolean workedWith) throws ParseException {
        _workedWith = workedWith;
        SOURCE.put("worked_with", workedWith);
        return this;
    }

    public void setWorkerCompensation(Boolean workerCompensation) throws ParseException {
        _workerCompensation = workerCompensation;
        SOURCE.put("worker_compensation", workerCompensation);
    }

    public Boolean getWorkerCompensation() {
        try {
            if (_workerCompensation == null && SOURCE.has("worker_compensation") && SOURCE.get("worker_compensation") != null)
                _workerCompensation = SOURCE.getBoolean("worker_compensation");
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _workerCompensation;
    }

    public User workerCompensation(Boolean workerCompensation) throws ParseException {
        _workerCompensation = workerCompensation;
        SOURCE.put("worker_compensation", workerCompensation);
        return this;
    }

    public void setZip(String zip) throws ParseException {
        _zip = zip;
        SOURCE.put("zip", zip);
    }

    public String getZip() {
        try {
            if (_zip == null && SOURCE.has("zip") && SOURCE.get("zip") != null)
                _zip = SOURCE.getString("zip");
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _zip;
    }

    public User zip(String zip) throws ParseException {
        _zip = zip;
        SOURCE.put("zip", zip);
        return this;
    }

    /*-*****************************-*/
    /*-             Json            -*/
    /*-*****************************-*/
    public static JsonArray toJsonArray(User[] array) {
        JsonArray list = new JsonArray();
        for (User item : array) {
            list.add(item.getJson());
        }
        return list;
    }

    public static User[] fromJsonArray(JsonArray array) {
        User[] list = new User[array.size()];
        for (int i = 0; i < array.size(); i++) {
            list[i] = fromJson(array.getJsonObject(i));
        }
        return list;
    }

    public static User fromJson(JsonObject obj) {
        try {
            return new User(obj);
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
    public static final Parcelable.Creator<User> CREATOR = new Parcelable.Creator<User>() {

        @Override
        public User createFromParcel(Parcel source) {
            try {
                return User.fromJson((JsonObject) source.readParcelable(JsonObject.class.getClassLoader()));
            } catch (Exception ex) {
                Log.v(TAG, ex);
                return null;
            }
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
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
