package com.fieldnation.data.bv2.model;

import com.fieldnation.fnjson.JsonObject;
import com.fieldnation.fnjson.Serializer;
import com.fieldnation.fnjson.Unserializer;
import com.fieldnation.fnjson.annotations.Json;
import com.fieldnation.fnlog.Log;

public class User {
    private static final String TAG = "User";

    @Json(name = "id")
    private Integer id = null;

    @Json(name = "first_name")
    private String firstName = null;

    @Json(name = "last_name")
    private String lastName = null;

    @Json(name = "address")
    private String address = null;

    @Json(name = "city")
    private String city = null;

    @Json(name = "state")
    private String state = null;

    @Json(name = "zip")
    private String zip = null;

    @Json(name = "country")
    private String country = null;

    @Json(name = "thumbnail")
    private String thumbnail = null;

    @Json(name = "worker_compensation")
    private Boolean workerCompensation = null;

    @Json(name = "time_zone")
    private TimeZone timeZone = null;

    @Json(name = "request")
    private Request request = null;

    @Json(name = "route")
    private Route route = null;

    @Json(name = "website")
    private String website = null;

    @Json(name = "coords")
    private Coords coords = null;

    @Json(name = "protec")
    private Boolean protec = null;

    @Json(name = "background_checks")
    private BackgroundCheck[] backgroundChecks;

    @Json(name = "drug_tests")
    private DrugTest[] drugTests;

    @Json(name = "notes")
    private String notes = null;

    @Json(name = "upcoming_schedule")
    private UserUpcomingSchedule[] upcomingSchedule;

    @Json(name = "worked_with")
    private Boolean workedWith = null;

    @Json(name = "jobs")
    private UserJobs jobs = null;

    @Json(name = "managed")
    private Boolean managed = null;

    @Json(name = "rating")
    private UserRating rating = null;

    @Json(name = "company")
    private UserCompany company = null;

    @Json(name = "client")
    private Company client = null;

    @Json(name = "role")
    private String role = null;

    @Json(name = "last_active")
    private String lastActive = null;

    @Json(name = "email")
    private String email = null;

    @Json(name = "phone")
    private String phone = null;

    @Json(name = "preferred_groups")
    private UserPreferredGroups[] preferredGroups;

    @Json(name = "blocked")
    private UserBlocked[] blocked;

    public User() {
    }

    public Integer getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getAddress() {
        return address;
    }

    public String getCity() {
        return city;
    }

    public String getState() {
        return state;
    }

    public String getZip() {
        return zip;
    }

    public String getCountry() {
        return country;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public Boolean getWorkerCompensation() {
        return workerCompensation;
    }

    public TimeZone getTimeZone() {
        return timeZone;
    }

    public Request getRequest() {
        return request;
    }

    public Route getRoute() {
        return route;
    }

    public String getWebsite() {
        return website;
    }

    public Coords getCoords() {
        return coords;
    }

    public Boolean getProtec() {
        return protec;
    }

    public BackgroundCheck[] getBackgroundChecks() {
        return backgroundChecks;
    }

    public DrugTest[] getDrugTests() {
        return drugTests;
    }

    public String getNotes() {
        return notes;
    }

    public UserUpcomingSchedule[] getUpcomingSchedule() {
        return upcomingSchedule;
    }

    public Boolean getWorkedWith() {
        return workedWith;
    }

    public UserJobs getJobs() {
        return jobs;
    }

    public Boolean getManaged() {
        return managed;
    }

    public UserRating getRating() {
        return rating;
    }

    public UserCompany getCompany() {
        return company;
    }

    public Company getClient() {
        return client;
    }

    public String getRole() {
        return role;
    }

    public String getLastActive() {
        return lastActive;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public UserPreferredGroups[] getPreferredGroups() {
        return preferredGroups;
    }

    public UserBlocked[] getBlocked() {
        return blocked;
    }

    /*-*****************************-*/
    /*-             Json            -*/
    /*-*****************************-*/
    public static User fromJson(JsonObject obj) {
        try {
            return Unserializer.unserializeObject(User.class, obj);
        } catch (Exception ex) {
            Log.v(TAG, ex);
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
            Log.v(TAG, ex);
            return null;
        }
    }
}