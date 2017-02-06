package com.fieldnation.v2.data.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.fieldnation.fnjson.JsonArray;
import com.fieldnation.fnjson.JsonObject;
import com.fieldnation.fnjson.Serializer;
import com.fieldnation.fnjson.Unserializer;
import com.fieldnation.fnjson.annotations.Json;
import com.fieldnation.fnlog.Log;

/**
 * Created by dmgen from swagger.
 */

public class User implements Parcelable {
    private static final String TAG = "User";

    @Json(name = "country")
    private String _country;

    @Json(name = "request")
    private Request _request;

    @Json(name = "notes")
    private String _notes;

    @Json(name = "role")
    private String _role;

    @Json(name = "city")
    private String _city;

    @Json(name = "rating")
    private UserRating _rating;

    @Json(name = "background_checks")
    private BackgroundCheck[] _backgroundChecks;

    @Json(name = "preferred_groups")
    private UserPreferredGroups[] _preferredGroups;

    @Json(name = "blocked")
    private UserBlocked[] _blocked;

    @Json(name = "managed")
    private Boolean _managed;

    @Json(name = "client")
    private Company _client;

    @Json(name = "company")
    private UserCompany _company;

    @Json(name = "id")
    private Integer _id;

    @Json(name = "state")
    private String _state;

    @Json(name = "protec")
    private Boolean _protec;

    @Json(name = "first_name")
    private String _firstName;

    @Json(name = "worker_compensation")
    private Boolean _workerCompensation;

    @Json(name = "coords")
    private Coords _coords;

    @Json(name = "email")
    private String _email;

    @Json(name = "worked_with")
    private Boolean _workedWith;

    @Json(name = "zip")
    private String _zip;

    @Json(name = "drug_tests")
    private DrugTest[] _drugTests;

    @Json(name = "upcoming_schedule")
    private UserUpcomingSchedule[] _upcomingSchedule;

    @Json(name = "thumbnail")
    private String _thumbnail;

    @Json(name = "website")
    private String _website;

    @Json(name = "address")
    private String _address;

    @Json(name = "jobs")
    private UserJobs _jobs;

    @Json(name = "last_name")
    private String _lastName;

    @Json(name = "time_zone")
    private TimeZone _timeZone;

    @Json(name = "route")
    private Route _route;

    @Json(name = "phone")
    private String _phone;

    @Json(name = "last_active")
    private Date _lastActive;

    public User() {
    }

    public void setCountry(String country) {
        _country = country;
    }

    public String getCountry() {
        return _country;
    }

    public User country(String country) {
        _country = country;
        return this;
    }

    public void setRequest(Request request) {
        _request = request;
    }

    public Request getRequest() {
        return _request;
    }

    public User request(Request request) {
        _request = request;
        return this;
    }

    public void setNotes(String notes) {
        _notes = notes;
    }

    public String getNotes() {
        return _notes;
    }

    public User notes(String notes) {
        _notes = notes;
        return this;
    }

    public void setRole(String role) {
        _role = role;
    }

    public String getRole() {
        return _role;
    }

    public User role(String role) {
        _role = role;
        return this;
    }

    public void setCity(String city) {
        _city = city;
    }

    public String getCity() {
        return _city;
    }

    public User city(String city) {
        _city = city;
        return this;
    }

    public void setRating(UserRating rating) {
        _rating = rating;
    }

    public UserRating getRating() {
        return _rating;
    }

    public User rating(UserRating rating) {
        _rating = rating;
        return this;
    }

    public void setBackgroundChecks(BackgroundCheck[] backgroundChecks) {
        _backgroundChecks = backgroundChecks;
    }

    public BackgroundCheck[] getBackgroundChecks() {
        return _backgroundChecks;
    }

    public User backgroundChecks(BackgroundCheck[] backgroundChecks) {
        _backgroundChecks = backgroundChecks;
        return this;
    }

    public void setPreferredGroups(UserPreferredGroups[] preferredGroups) {
        _preferredGroups = preferredGroups;
    }

    public UserPreferredGroups[] getPreferredGroups() {
        return _preferredGroups;
    }

    public User preferredGroups(UserPreferredGroups[] preferredGroups) {
        _preferredGroups = preferredGroups;
        return this;
    }

    public void setBlocked(UserBlocked[] blocked) {
        _blocked = blocked;
    }

    public UserBlocked[] getBlocked() {
        return _blocked;
    }

    public User blocked(UserBlocked[] blocked) {
        _blocked = blocked;
        return this;
    }

    public void setManaged(Boolean managed) {
        _managed = managed;
    }

    public Boolean getManaged() {
        return _managed;
    }

    public User managed(Boolean managed) {
        _managed = managed;
        return this;
    }

    public void setClient(Company client) {
        _client = client;
    }

    public Company getClient() {
        return _client;
    }

    public User client(Company client) {
        _client = client;
        return this;
    }

    public void setCompany(UserCompany company) {
        _company = company;
    }

    public UserCompany getCompany() {
        return _company;
    }

    public User company(UserCompany company) {
        _company = company;
        return this;
    }

    public void setId(Integer id) {
        _id = id;
    }

    public Integer getId() {
        return _id;
    }

    public User id(Integer id) {
        _id = id;
        return this;
    }

    public void setState(String state) {
        _state = state;
    }

    public String getState() {
        return _state;
    }

    public User state(String state) {
        _state = state;
        return this;
    }

    public void setProtec(Boolean protec) {
        _protec = protec;
    }

    public Boolean getProtec() {
        return _protec;
    }

    public User protec(Boolean protec) {
        _protec = protec;
        return this;
    }

    public void setFirstName(String firstName) {
        _firstName = firstName;
    }

    public String getFirstName() {
        return _firstName;
    }

    public User firstName(String firstName) {
        _firstName = firstName;
        return this;
    }

    public void setWorkerCompensation(Boolean workerCompensation) {
        _workerCompensation = workerCompensation;
    }

    public Boolean getWorkerCompensation() {
        return _workerCompensation;
    }

    public User workerCompensation(Boolean workerCompensation) {
        _workerCompensation = workerCompensation;
        return this;
    }

    public void setCoords(Coords coords) {
        _coords = coords;
    }

    public Coords getCoords() {
        return _coords;
    }

    public User coords(Coords coords) {
        _coords = coords;
        return this;
    }

    public void setEmail(String email) {
        _email = email;
    }

    public String getEmail() {
        return _email;
    }

    public User email(String email) {
        _email = email;
        return this;
    }

    public void setWorkedWith(Boolean workedWith) {
        _workedWith = workedWith;
    }

    public Boolean getWorkedWith() {
        return _workedWith;
    }

    public User workedWith(Boolean workedWith) {
        _workedWith = workedWith;
        return this;
    }

    public void setZip(String zip) {
        _zip = zip;
    }

    public String getZip() {
        return _zip;
    }

    public User zip(String zip) {
        _zip = zip;
        return this;
    }

    public void setDrugTests(DrugTest[] drugTests) {
        _drugTests = drugTests;
    }

    public DrugTest[] getDrugTests() {
        return _drugTests;
    }

    public User drugTests(DrugTest[] drugTests) {
        _drugTests = drugTests;
        return this;
    }

    public void setUpcomingSchedule(UserUpcomingSchedule[] upcomingSchedule) {
        _upcomingSchedule = upcomingSchedule;
    }

    public UserUpcomingSchedule[] getUpcomingSchedule() {
        return _upcomingSchedule;
    }

    public User upcomingSchedule(UserUpcomingSchedule[] upcomingSchedule) {
        _upcomingSchedule = upcomingSchedule;
        return this;
    }

    public void setThumbnail(String thumbnail) {
        _thumbnail = thumbnail;
    }

    public String getThumbnail() {
        return _thumbnail;
    }

    public User thumbnail(String thumbnail) {
        _thumbnail = thumbnail;
        return this;
    }

    public void setWebsite(String website) {
        _website = website;
    }

    public String getWebsite() {
        return _website;
    }

    public User website(String website) {
        _website = website;
        return this;
    }

    public void setAddress(String address) {
        _address = address;
    }

    public String getAddress() {
        return _address;
    }

    public User address(String address) {
        _address = address;
        return this;
    }

    public void setJobs(UserJobs jobs) {
        _jobs = jobs;
    }

    public UserJobs getJobs() {
        return _jobs;
    }

    public User jobs(UserJobs jobs) {
        _jobs = jobs;
        return this;
    }

    public void setLastName(String lastName) {
        _lastName = lastName;
    }

    public String getLastName() {
        return _lastName;
    }

    public User lastName(String lastName) {
        _lastName = lastName;
        return this;
    }

    public void setTimeZone(TimeZone timeZone) {
        _timeZone = timeZone;
    }

    public TimeZone getTimeZone() {
        return _timeZone;
    }

    public User timeZone(TimeZone timeZone) {
        _timeZone = timeZone;
        return this;
    }

    public void setRoute(Route route) {
        _route = route;
    }

    public Route getRoute() {
        return _route;
    }

    public User route(Route route) {
        _route = route;
        return this;
    }

    public void setPhone(String phone) {
        _phone = phone;
    }

    public String getPhone() {
        return _phone;
    }

    public User phone(String phone) {
        _phone = phone;
        return this;
    }

    public void setLastActive(Date lastActive) {
        _lastActive = lastActive;
    }

    public Date getLastActive() {
        return _lastActive;
    }

    public User lastActive(Date lastActive) {
        _lastActive = lastActive;
        return this;
    }

    /*-*****************************-*/
    /*-             Json            -*/
    /*-*****************************-*/
    public static User[] fromJsonArray(JsonArray array) {
        User[] list = new User[array.size()];
        for (int i = 0; i < array.size(); i++) {
            list[i] = fromJson(array.getJsonObject(i));
        }
        return list;
    }

    public static User fromJson(JsonObject obj) {
        try {
            return Unserializer.unserializeObject(User.class, obj);
        } catch (Exception ex) {
            Log.v(TAG, TAG, ex);
            return null;
        }
    }

    public JsonObject toJson() {
        return toJson(this);
    }

    public static JsonObject toJson(User user) {
        try {
            return Serializer.serializeObject(user);
        } catch (Exception ex) {
            Log.v(TAG, TAG, ex);
            return null;
        }
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
        dest.writeParcelable(toJson(), flags);
    }
}
