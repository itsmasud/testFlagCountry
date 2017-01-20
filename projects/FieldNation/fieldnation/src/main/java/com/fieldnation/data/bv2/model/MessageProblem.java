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
import io.swagger.client.model.MessageProblemType;

/**
 * MessageProblem
 */
@javax.annotation.Generated(value = "io.swagger.codegen.languages.JavaClientCodegen", date = "2017-01-20T21:04:59.764Z")
public class MessageProblem {
  @Json(name="type")
  private MessageProblemType type = null;

  @Json(name="resolved")
  private Boolean resolved = null;

  @Json(name="flag_id")
  private Integer flagId = null;

  public MessageProblem type(MessageProblemType type) {
    this.type = type;
    return this;
  }

   /**
   * Get type
   * @return type
  **/
  @ApiModelProperty(example = "null", value = "")
  public MessageProblemType getType() {
    return type;
  }

  public void setType(MessageProblemType type) {
    this.type = type;
  }

  public MessageProblem resolved(Boolean resolved) {
    this.resolved = resolved;
    return this;
  }

   /**
   * Get resolved
   * @return resolved
  **/
  @ApiModelProperty(example = "null", value = "")
  public Boolean getResolved() {
    return resolved;
  }

  public void setResolved(Boolean resolved) {
    this.resolved = resolved;
  }

  public MessageProblem flagId(Integer flagId) {
    this.flagId = flagId;
    return this;
  }

   /**
   * Get flagId
   * @return flagId
  **/
  @ApiModelProperty(example = "null", value = "")
  public Integer getFlagId() {
    return flagId;
  }

  public void setFlagId(Integer flagId) {
    this.flagId = flagId;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    MessageProblem messageProblem = (MessageProblem) o;
    return Objects.equals(this.type, messageProblem.type) &&
        Objects.equals(this.resolved, messageProblem.resolved) &&
        Objects.equals(this.flagId, messageProblem.flagId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(type, resolved, flagId);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class MessageProblem {\n");
    
    sb.append("    type: ").append(toIndentedString(type)).append("\n");
    sb.append("    resolved: ").append(toIndentedString(resolved)).append("\n");
    sb.append("    flagId: ").append(toIndentedString(flagId)).append("\n");
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

