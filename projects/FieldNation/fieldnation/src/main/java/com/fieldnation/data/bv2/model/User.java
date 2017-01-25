package com.fieldnation.data.bv2.model;

import com.fieldnation.fnjson.JsonObject;
import com.fieldnation.fnjson.Serializer;
import com.fieldnation.fnjson.Unserializer;
import com.fieldnation.fnjson.annotations.Json;
import com.fieldnation.fnlog.Log;

public class User {
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

    public String getCountry() {
        return _country;
    }

    public Request getRequest() {
        return _request;
    }

    public String getNotes() {
        return _notes;
    }

    public String getRole() {
        return _role;
    }

    public String getCity() {
        return _city;
    }

    public UserRating getRating() {
        return _rating;
    }

    public BackgroundCheck[] getBackgroundChecks() {
        return _backgroundChecks;
    }

    public UserPreferredGroups[] getPreferredGroups() {
        return _preferredGroups;
    }

    public UserBlocked[] getBlocked() {
        return _blocked;
    }

    public Boolean getManaged() {
        return _managed;
    }

    public Company getClient() {
        return _client;
    }

    public UserCompany getCompany() {
        return _company;
    }

    public Integer getId() {
        return _id;
    }

    public String getState() {
        return _state;
    }

    public Boolean getProtec() {
        return _protec;
    }

    public String getFirstName() {
        return _firstName;
    }

    public Boolean getWorkerCompensation() {
        return _workerCompensation;
    }

    public Coords getCoords() {
        return _coords;
    }

    public String getEmail() {
        return _email;
    }

    public Boolean getWorkedWith() {
        return _workedWith;
    }

    public String getZip() {
        return _zip;
    }

    public DrugTest[] getDrugTests() {
        return _drugTests;
    }

    public UserUpcomingSchedule[] getUpcomingSchedule() {
        return _upcomingSchedule;
    }

    public String getThumbnail() {
        return _thumbnail;
    }

    public String getWebsite() {
        return _website;
    }

    public String getAddress() {
        return _address;
    }

    public UserJobs getJobs() {
        return _jobs;
    }

    public String getLastName() {
        return _lastName;
    }

    public TimeZone getTimeZone() {
        return _timeZone;
    }

    public Route getRoute() {
        return _route;
    }

    public String getPhone() {
        return _phone;
    }

    public Date getLastActive() {
        return _lastActive;
    }

    /*-*****************************-*/
    /*-             Json            -*/
    /*-*****************************-*/
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
}
