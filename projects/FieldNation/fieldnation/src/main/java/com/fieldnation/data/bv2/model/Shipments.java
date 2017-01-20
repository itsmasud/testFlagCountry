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
import io.swagger.client.model.ListEnvelope;
import io.swagger.client.model.Shipment;
import java.util.ArrayList;
import java.util.List;

/**
 * Shipments
 */
@javax.annotation.Generated(value = "io.swagger.codegen.languages.JavaClientCodegen", date = "2017-01-20T21:04:59.764Z")
public class Shipments {
  @Json(name="metadata")
  private ListEnvelope metadata = null;

  @Json(name="results")
  private List<Shipment> results = new ArrayList<Shipment>();

  @Json(name="actions")
  private List<String> actions = new ArrayList<String>();

  public Shipments metadata(ListEnvelope metadata) {
    this.metadata = metadata;
    return this;
  }

   /**
   * Get metadata
   * @return metadata
  **/
  @ApiModelProperty(example = "null", value = "")
  public ListEnvelope getMetadata() {
    return metadata;
  }

  public void setMetadata(ListEnvelope metadata) {
    this.metadata = metadata;
  }

  public Shipments results(List<Shipment> results) {
    this.results = results;
    return this;
  }

  public Shipments addResultsItem(Shipment resultsItem) {
    this.results.add(resultsItem);
    return this;
  }

   /**
   * Get results
   * @return results
  **/
  @ApiModelProperty(example = "null", value = "")
  public List<Shipment> getResults() {
    return results;
  }

  public void setResults(List<Shipment> results) {
    this.results = results;
  }

  public Shipments actions(List<String> actions) {
    this.actions = actions;
    return this;
  }

  public Shipments addActionsItem(String actionsItem) {
    this.actions.add(actionsItem);
    return this;
  }

   /**
   * Get actions
   * @return actions
  **/
  @ApiModelProperty(example = "null", value = "")
  public List<String> getActions() {
    return actions;
  }

  public void setActions(List<String> actions) {
    this.actions = actions;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Shipments shipments = (Shipments) o;
    return Objects.equals(this.metadata, shipments.metadata) &&
        Objects.equals(this.results, shipments.results) &&
        Objects.equals(this.actions, shipments.actions);
  }

  @Override
  public int hashCode() {
    return Objects.hash(metadata, results, actions);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Shipments {\n");
    
    sb.append("    metadata: ").append(toIndentedString(metadata)).append("\n");
    sb.append("    results: ").append(toIndentedString(results)).append("\n");
    sb.append("    actions: ").append(toIndentedString(actions)).append("\n");
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

