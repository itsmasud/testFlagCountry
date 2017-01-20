/*
 * RESTful API V2
 * No description provided (generated by Swagger Codegen https://github.com/swagger-api/swagger-codegen)
 *
 * OpenAPI spec version: 2.0.0
 * 
 *
 * NOTE: This class is auto generated by the swagger code generator program.
 * https://github.com/swagger-api/swagger-codegen.git
 * Do not edit the class manually.
 */


package com.fieldnation.data.bv2.model;

import java.util.Objects;
import com.google.gson.annotations.SerializedName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.client.model.Contact;
import io.swagger.client.model.Coords;
import io.swagger.client.model.LocationValidation;
import io.swagger.client.model.StoredLocation;
import io.swagger.client.model.TimeZone;
import io.swagger.client.model.UserBy;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Location
 */
@javax.annotation.Generated(value = "io.swagger.codegen.languages.JavaClientCodegen", date = "2017-01-20T21:04:59.764Z")
public class Location {
  @Json(name="work_order_id")
  private Integer workOrderId = null;

  /**
   * Gets or Sets mode
   */
  public enum ModeEnum {
    @Json(name="custom")
    CUSTOM("custom"),
    
    @Json(name="remote")
    REMOTE("remote"),
    
    @Json(name="location")
    LOCATION("location");

    private String value;

    ModeEnum(String value) {
      this.value = value;
    }

    @Override
    public String toString() {
      return String.valueOf(value);
    }
  }

  @Json(name="mode")
  private ModeEnum mode = null;

  @Json(name="role")
  private String role = null;

  @Json(name="actions")
  private String actions = null;

  @Json(name="correlation_id")
  private String correlationId = null;

  @Json(name="status_id")
  private Integer statusId = null;

  @Json(name="address1")
  private String address1 = null;

  @Json(name="address2")
  private String address2 = null;

  @Json(name="city")
  private String city = null;

  @Json(name="state")
  private String state = null;

  @Json(name="zip")
  private String zip = null;

  @Json(name="country")
  private String country = null;

  @Json(name="coordinates")
  private Coords coordinates = null;

  @Json(name="type")
  private UserBy type = null;

  @Json(name="save_location")
  private String saveLocation = null;

  @Json(name="save_location_group")
  private Integer saveLocationGroup = null;

  @Json(name="saved_location")
  private StoredLocation savedLocation = null;

  @Json(name="time_zone")
  private TimeZone timeZone = null;

  @Json(name="contacts")
  private List<Contact> contacts = new ArrayList<Contact>();

  @Json(name="map")
  private Map map = null;

  @Json(name="validation")
  private LocationValidation validation = null;

  public Location workOrderId(Integer workOrderId) {
    this.workOrderId = workOrderId;
    return this;
  }

   /**
   * Get workOrderId
   * @return workOrderId
  **/
  @ApiModelProperty(example = "null", value = "")
  public Integer getWorkOrderId() {
    return workOrderId;
  }

  public void setWorkOrderId(Integer workOrderId) {
    this.workOrderId = workOrderId;
  }

  public Location mode(ModeEnum mode) {
    this.mode = mode;
    return this;
  }

   /**
   * Get mode
   * @return mode
  **/
  @ApiModelProperty(example = "null", value = "")
  public ModeEnum getMode() {
    return mode;
  }

  public void setMode(ModeEnum mode) {
    this.mode = mode;
  }

  public Location role(String role) {
    this.role = role;
    return this;
  }

   /**
   * Get role
   * @return role
  **/
  @ApiModelProperty(example = "null", value = "")
  public String getRole() {
    return role;
  }

  public void setRole(String role) {
    this.role = role;
  }

  public Location actions(String actions) {
    this.actions = actions;
    return this;
  }

   /**
   * Get actions
   * @return actions
  **/
  @ApiModelProperty(example = "null", value = "")
  public String getActions() {
    return actions;
  }

  public void setActions(String actions) {
    this.actions = actions;
  }

  public Location correlationId(String correlationId) {
    this.correlationId = correlationId;
    return this;
  }

   /**
   * Get correlationId
   * @return correlationId
  **/
  @ApiModelProperty(example = "null", value = "")
  public String getCorrelationId() {
    return correlationId;
  }

  public void setCorrelationId(String correlationId) {
    this.correlationId = correlationId;
  }

  public Location statusId(Integer statusId) {
    this.statusId = statusId;
    return this;
  }

   /**
   * Get statusId
   * @return statusId
  **/
  @ApiModelProperty(example = "null", value = "")
  public Integer getStatusId() {
    return statusId;
  }

  public void setStatusId(Integer statusId) {
    this.statusId = statusId;
  }

  public Location address1(String address1) {
    this.address1 = address1;
    return this;
  }

   /**
   * Get address1
   * @return address1
  **/
  @ApiModelProperty(example = "null", value = "")
  public String getAddress1() {
    return address1;
  }

  public void setAddress1(String address1) {
    this.address1 = address1;
  }

  public Location address2(String address2) {
    this.address2 = address2;
    return this;
  }

   /**
   * Get address2
   * @return address2
  **/
  @ApiModelProperty(example = "null", value = "")
  public String getAddress2() {
    return address2;
  }

  public void setAddress2(String address2) {
    this.address2 = address2;
  }

  public Location city(String city) {
    this.city = city;
    return this;
  }

   /**
   * Get city
   * @return city
  **/
  @ApiModelProperty(example = "null", value = "")
  public String getCity() {
    return city;
  }

  public void setCity(String city) {
    this.city = city;
  }

  public Location state(String state) {
    this.state = state;
    return this;
  }

   /**
   * Get state
   * @return state
  **/
  @ApiModelProperty(example = "null", value = "")
  public String getState() {
    return state;
  }

  public void setState(String state) {
    this.state = state;
  }

  public Location zip(String zip) {
    this.zip = zip;
    return this;
  }

   /**
   * Get zip
   * @return zip
  **/
  @ApiModelProperty(example = "null", value = "")
  public String getZip() {
    return zip;
  }

  public void setZip(String zip) {
    this.zip = zip;
  }

  public Location country(String country) {
    this.country = country;
    return this;
  }

   /**
   * Get country
   * @return country
  **/
  @ApiModelProperty(example = "null", value = "")
  public String getCountry() {
    return country;
  }

  public void setCountry(String country) {
    this.country = country;
  }

  public Location coordinates(Coords coordinates) {
    this.coordinates = coordinates;
    return this;
  }

   /**
   * Get coordinates
   * @return coordinates
  **/
  @ApiModelProperty(example = "null", value = "")
  public Coords getCoordinates() {
    return coordinates;
  }

  public void setCoordinates(Coords coordinates) {
    this.coordinates = coordinates;
  }

  public Location type(UserBy type) {
    this.type = type;
    return this;
  }

   /**
   * Get type
   * @return type
  **/
  @ApiModelProperty(example = "null", value = "")
  public UserBy getType() {
    return type;
  }

  public void setType(UserBy type) {
    this.type = type;
  }

  public Location saveLocation(String saveLocation) {
    this.saveLocation = saveLocation;
    return this;
  }

   /**
   * Get saveLocation
   * @return saveLocation
  **/
  @ApiModelProperty(example = "null", value = "")
  public String getSaveLocation() {
    return saveLocation;
  }

  public void setSaveLocation(String saveLocation) {
    this.saveLocation = saveLocation;
  }

  public Location saveLocationGroup(Integer saveLocationGroup) {
    this.saveLocationGroup = saveLocationGroup;
    return this;
  }

   /**
   * Get saveLocationGroup
   * @return saveLocationGroup
  **/
  @ApiModelProperty(example = "null", value = "")
  public Integer getSaveLocationGroup() {
    return saveLocationGroup;
  }

  public void setSaveLocationGroup(Integer saveLocationGroup) {
    this.saveLocationGroup = saveLocationGroup;
  }

  public Location savedLocation(StoredLocation savedLocation) {
    this.savedLocation = savedLocation;
    return this;
  }

   /**
   * Get savedLocation
   * @return savedLocation
  **/
  @ApiModelProperty(example = "null", value = "")
  public StoredLocation getSavedLocation() {
    return savedLocation;
  }

  public void setSavedLocation(StoredLocation savedLocation) {
    this.savedLocation = savedLocation;
  }

  public Location timeZone(TimeZone timeZone) {
    this.timeZone = timeZone;
    return this;
  }

   /**
   * Get timeZone
   * @return timeZone
  **/
  @ApiModelProperty(example = "null", value = "")
  public TimeZone getTimeZone() {
    return timeZone;
  }

  public void setTimeZone(TimeZone timeZone) {
    this.timeZone = timeZone;
  }

  public Location contacts(List<Contact> contacts) {
    this.contacts = contacts;
    return this;
  }

  public Location addContactsItem(Contact contactsItem) {
    this.contacts.add(contactsItem);
    return this;
  }

   /**
   * Get contacts
   * @return contacts
  **/
  @ApiModelProperty(example = "null", value = "")
  public List<Contact> getContacts() {
    return contacts;
  }

  public void setContacts(List<Contact> contacts) {
    this.contacts = contacts;
  }

  public Location map(Map map) {
    this.map = map;
    return this;
  }

   /**
   * Get map
   * @return map
  **/
  @ApiModelProperty(example = "null", value = "")
  public Map getMap() {
    return map;
  }

  public void setMap(Map map) {
    this.map = map;
  }

  public Location validation(LocationValidation validation) {
    this.validation = validation;
    return this;
  }

   /**
   * Get validation
   * @return validation
  **/
  @ApiModelProperty(example = "null", value = "")
  public LocationValidation getValidation() {
    return validation;
  }

  public void setValidation(LocationValidation validation) {
    this.validation = validation;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Location location = (Location) o;
    return Objects.equals(this.workOrderId, location.workOrderId) &&
        Objects.equals(this.mode, location.mode) &&
        Objects.equals(this.role, location.role) &&
        Objects.equals(this.actions, location.actions) &&
        Objects.equals(this.correlationId, location.correlationId) &&
        Objects.equals(this.statusId, location.statusId) &&
        Objects.equals(this.address1, location.address1) &&
        Objects.equals(this.address2, location.address2) &&
        Objects.equals(this.city, location.city) &&
        Objects.equals(this.state, location.state) &&
        Objects.equals(this.zip, location.zip) &&
        Objects.equals(this.country, location.country) &&
        Objects.equals(this.coordinates, location.coordinates) &&
        Objects.equals(this.type, location.type) &&
        Objects.equals(this.saveLocation, location.saveLocation) &&
        Objects.equals(this.saveLocationGroup, location.saveLocationGroup) &&
        Objects.equals(this.savedLocation, location.savedLocation) &&
        Objects.equals(this.timeZone, location.timeZone) &&
        Objects.equals(this.contacts, location.contacts) &&
        Objects.equals(this.map, location.map) &&
        Objects.equals(this.validation, location.validation);
  }

  @Override
  public int hashCode() {
    return Objects.hash(workOrderId, mode, role, actions, correlationId, statusId, address1, address2, city, state, zip, country, coordinates, type, saveLocation, saveLocationGroup, savedLocation, timeZone, contacts, map, validation);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Location {\n");
    
    sb.append("    workOrderId: ").append(toIndentedString(workOrderId)).append("\n");
    sb.append("    mode: ").append(toIndentedString(mode)).append("\n");
    sb.append("    role: ").append(toIndentedString(role)).append("\n");
    sb.append("    actions: ").append(toIndentedString(actions)).append("\n");
    sb.append("    correlationId: ").append(toIndentedString(correlationId)).append("\n");
    sb.append("    statusId: ").append(toIndentedString(statusId)).append("\n");
    sb.append("    address1: ").append(toIndentedString(address1)).append("\n");
    sb.append("    address2: ").append(toIndentedString(address2)).append("\n");
    sb.append("    city: ").append(toIndentedString(city)).append("\n");
    sb.append("    state: ").append(toIndentedString(state)).append("\n");
    sb.append("    zip: ").append(toIndentedString(zip)).append("\n");
    sb.append("    country: ").append(toIndentedString(country)).append("\n");
    sb.append("    coordinates: ").append(toIndentedString(coordinates)).append("\n");
    sb.append("    type: ").append(toIndentedString(type)).append("\n");
    sb.append("    saveLocation: ").append(toIndentedString(saveLocation)).append("\n");
    sb.append("    saveLocationGroup: ").append(toIndentedString(saveLocationGroup)).append("\n");
    sb.append("    savedLocation: ").append(toIndentedString(savedLocation)).append("\n");
    sb.append("    timeZone: ").append(toIndentedString(timeZone)).append("\n");
    sb.append("    contacts: ").append(toIndentedString(contacts)).append("\n");
    sb.append("    map: ").append(toIndentedString(map)).append("\n");
    sb.append("    validation: ").append(toIndentedString(validation)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces
   * (except the first line).
   */
  private String toIndentedString(java.lang.Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }
  
}

