package com.fieldnation.data.bv2.model;

import com.fieldnation.fnjson.JsonObject;
import com.fieldnation.fnjson.Serializer;
import com.fieldnation.fnjson.Unserializer;
import com.fieldnation.fnjson.annotations.Json;
import com.fieldnation.fnlog.Log;

public class User {
    private static final String TAG = "User";

    @Json(name = "country")
    private String country;

    @Json(name = "request")
    private Request request;

    @Json(name = "notes")
    private String notes;

    @Json(name = "role")
    private String role;

    @Json(name = "city")
    private String city;

    @Json(name = "rating")
    private UserRating rating;

    @Json(name = "background_checks")
    private BackgroundCheck[] backgroundChecks;

    @Json(name = "preferred_groups")
    private UserPreferredGroups[] preferredGroups;

    @Json(name = "blocked")
    private UserBlocked[] blocked;

    @Json(name = "managed")
    private Boolean managed;

    @Json(name = "client")
    private Company client;

    @Json(name = "company")
    private UserCompany company;

    @Json(name = "id")
    private Integer id;

    @Json(name = "state")
    private String state;

    @Json(name = "protec")
    private Boolean protec;

    @Json(name = "first_name")
    private String firstName;

    @Json(name = "worker_compensation")
    private Boolean workerCompensation;

    @Json(name = "coords")
    private Coords coords;

    @Json(name = "email")
    private String email;

    @Json(name = "worked_with")
    private Boolean workedWith;

    @Json(name = "zip")
    private String zip;

    @Json(name = "drug_tests")
    private DrugTest[] drugTests;

    @Json(name = "upcoming_schedule")
    private UserUpcomingSchedule[] upcomingSchedule;

    @Json(name = "thumbnail")
    private String thumbnail;

    @Json(name = "website")
    private String website;

    @Json(name = "address")
    private String address;

    @Json(name = "jobs")
    private UserJobs jobs;

    @Json(name = "last_name")
    private String lastName;

    @Json(name = "time_zone")
    private TimeZone timeZone;

    @Json(name = "route")
    private Route route;

    @Json(name = "phone")
    private String phone;

    @Json(name = "last_active")
    private Date lastActive;

    public User() {
    }

    public String getCountry() {
        return country;
    }

    public Request getRequest() {
        return request;
    }

    public String getNotes() {
        return notes;
    }

    public String getRole() {
        return role;
    }

    public String getCity() {
        return city;
    }

    public UserRating getRating() {
        return rating;
    }

    public BackgroundCheck[] getBackgroundChecks() {
        return backgroundChecks;
    }

    public UserPreferredGroups[] getPreferredGroups() {
        return preferredGroups;
    }

    public UserBlocked[] getBlocked() {
        return blocked;
    }

    public Boolean getManaged() {
        return managed;
    }

    public Company getClient() {
        return client;
    }

    public UserCompany getCompany() {
        return company;
    }

    public Integer getId() {
        return id;
    }

    public String getState() {
        return state;
    }

    public Boolean getProtec() {
        return protec;
    }

    public String getFirstName() {
        return firstName;
    }

    public Boolean getWorkerCompensation() {
        return workerCompensation;
    }

    public Coords getCoords() {
        return coords;
    }

    public String getEmail() {
        return email;
    }

    public Boolean getWorkedWith() {
        return workedWith;
    }

    public String getZip() {
        return zip;
    }

    public DrugTest[] getDrugTests() {
        return drugTests;
    }

    public UserUpcomingSchedule[] getUpcomingSchedule() {
        return upcomingSchedule;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public String getWebsite() {
        return website;
    }

    public String getAddress() {
        return address;
    }

    public UserJobs getJobs() {
        return jobs;
    }

    public String getLastName() {
        return lastName;
    }

    public TimeZone getTimeZone() {
        return timeZone;
    }

    public Route getRoute() {
        return route;
    }

    public String getPhone() {
        return phone;
    }

    public Date getLastActive() {
        return lastActive;
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
