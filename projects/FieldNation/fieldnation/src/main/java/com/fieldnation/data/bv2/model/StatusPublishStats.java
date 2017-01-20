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

/**
 * StatusPublishStats
 */
@javax.annotation.Generated(value = "io.swagger.codegen.languages.JavaClientCodegen", date = "2017-01-20T21:04:59.764Z")
public class StatusPublishStats {
  @Json(name="routes")
  private Integer routes = null;

  @Json(name="requests")
  private Integer requests = null;

  @Json(name="counter_offers")
  private Integer counterOffers = null;

  public StatusPublishStats routes(Integer routes) {
    this.routes = routes;
    return this;
  }

   /**
   * Get routes
   * @return routes
  **/
  @ApiModelProperty(example = "null", value = "")
  public Integer getRoutes() {
    return routes;
  }

  public void setRoutes(Integer routes) {
    this.routes = routes;
  }

  public StatusPublishStats requests(Integer requests) {
    this.requests = requests;
    return this;
  }

   /**
   * Get requests
   * @return requests
  **/
  @ApiModelProperty(example = "null", value = "")
  public Integer getRequests() {
    return requests;
  }

  public void setRequests(Integer requests) {
    this.requests = requests;
  }

  public StatusPublishStats counterOffers(Integer counterOffers) {
    this.counterOffers = counterOffers;
    return this;
  }

   /**
   * Get counterOffers
   * @return counterOffers
  **/
  @ApiModelProperty(example = "null", value = "")
  public Integer getCounterOffers() {
    return counterOffers;
  }

  public void setCounterOffers(Integer counterOffers) {
    this.counterOffers = counterOffers;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    StatusPublishStats statusPublishStats = (StatusPublishStats) o;
    return Objects.equals(this.routes, statusPublishStats.routes) &&
        Objects.equals(this.requests, statusPublishStats.requests) &&
        Objects.equals(this.counterOffers, statusPublishStats.counterOffers);
  }

  @Override
  public int hashCode() {
    return Objects.hash(routes, requests, counterOffers);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class StatusPublishStats {\n");
    
    sb.append("    routes: ").append(toIndentedString(routes)).append("\n");
    sb.append("    requests: ").append(toIndentedString(requests)).append("\n");
    sb.append("    counterOffers: ").append(toIndentedString(counterOffers)).append("\n");
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

