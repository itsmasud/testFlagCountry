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
import io.swagger.client.model.Message;
import io.swagger.client.model.MessageFrom;
import io.swagger.client.model.MessageProblem;
import io.swagger.client.model.MessageTo;
import io.swagger.client.model.TimeLogsConfirmed;
import java.util.ArrayList;
import java.util.List;

/**
 * Message
 */
@javax.annotation.Generated(value = "io.swagger.codegen.languages.JavaClientCodegen", date = "2017-01-20T21:04:59.764Z")
public class Message {
  @Json(name="from")
  private MessageFrom from = null;

  @Json(name="to")
  private MessageTo to = null;

  @Json(name="role")
  private String role = null;

  @Json(name="msg_id")
  private Integer msgId = null;

  @Json(name="parent_id")
  private Boolean parentId = null;

  @Json(name="read")
  private Boolean read = null;

  @Json(name="created")
  private TimeLogsConfirmed created = null;

  @Json(name="message")
  private String message = null;

  @Json(name="problem")
  private MessageProblem problem = null;

  @Json(name="actions")
  private List<String> actions = new ArrayList<String>();

  @Json(name="replies")
  private Message replies = null;

  public Message from(MessageFrom from) {
    this.from = from;
    return this;
  }

   /**
   * Get from
   * @return from
  **/
  @ApiModelProperty(example = "null", value = "")
  public MessageFrom getFrom() {
    return from;
  }

  public void setFrom(MessageFrom from) {
    this.from = from;
  }

  public Message to(MessageTo to) {
    this.to = to;
    return this;
  }

   /**
   * Get to
   * @return to
  **/
  @ApiModelProperty(example = "null", value = "")
  public MessageTo getTo() {
    return to;
  }

  public void setTo(MessageTo to) {
    this.to = to;
  }

  public Message role(String role) {
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

  public Message msgId(Integer msgId) {
    this.msgId = msgId;
    return this;
  }

   /**
   * Get msgId
   * @return msgId
  **/
  @ApiModelProperty(example = "null", value = "")
  public Integer getMsgId() {
    return msgId;
  }

  public void setMsgId(Integer msgId) {
    this.msgId = msgId;
  }

  public Message parentId(Boolean parentId) {
    this.parentId = parentId;
    return this;
  }

   /**
   * Get parentId
   * @return parentId
  **/
  @ApiModelProperty(example = "null", value = "")
  public Boolean getParentId() {
    return parentId;
  }

  public void setParentId(Boolean parentId) {
    this.parentId = parentId;
  }

  public Message read(Boolean read) {
    this.read = read;
    return this;
  }

   /**
   * Get read
   * @return read
  **/
  @ApiModelProperty(example = "null", value = "")
  public Boolean getRead() {
    return read;
  }

  public void setRead(Boolean read) {
    this.read = read;
  }

  public Message created(TimeLogsConfirmed created) {
    this.created = created;
    return this;
  }

   /**
   * Get created
   * @return created
  **/
  @ApiModelProperty(example = "null", value = "")
  public TimeLogsConfirmed getCreated() {
    return created;
  }

  public void setCreated(TimeLogsConfirmed created) {
    this.created = created;
  }

  public Message message(String message) {
    this.message = message;
    return this;
  }

   /**
   * Get message
   * @return message
  **/
  @ApiModelProperty(example = "null", value = "")
  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  public Message problem(MessageProblem problem) {
    this.problem = problem;
    return this;
  }

   /**
   * Get problem
   * @return problem
  **/
  @ApiModelProperty(example = "null", value = "")
  public MessageProblem getProblem() {
    return problem;
  }

  public void setProblem(MessageProblem problem) {
    this.problem = problem;
  }

  public Message actions(List<String> actions) {
    this.actions = actions;
    return this;
  }

  public Message addActionsItem(String actionsItem) {
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

  public Message replies(Message replies) {
    this.replies = replies;
    return this;
  }

   /**
   * Get replies
   * @return replies
  **/
  @ApiModelProperty(example = "null", value = "")
  public Message getReplies() {
    return replies;
  }

  public void setReplies(Message replies) {
    this.replies = replies;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Message message = (Message) o;
    return Objects.equals(this.from, message.from) &&
        Objects.equals(this.to, message.to) &&
        Objects.equals(this.role, message.role) &&
        Objects.equals(this.msgId, message.msgId) &&
        Objects.equals(this.parentId, message.parentId) &&
        Objects.equals(this.read, message.read) &&
        Objects.equals(this.created, message.created) &&
        Objects.equals(this.message, message.message) &&
        Objects.equals(this.problem, message.problem) &&
        Objects.equals(this.actions, message.actions) &&
        Objects.equals(this.replies, message.replies);
  }

  @Override
  public int hashCode() {
    return Objects.hash(from, to, role, msgId, parentId, read, created, message, problem, actions, replies);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Message {\n");
    
    sb.append("    from: ").append(toIndentedString(from)).append("\n");
    sb.append("    to: ").append(toIndentedString(to)).append("\n");
    sb.append("    role: ").append(toIndentedString(role)).append("\n");
    sb.append("    msgId: ").append(toIndentedString(msgId)).append("\n");
    sb.append("    parentId: ").append(toIndentedString(parentId)).append("\n");
    sb.append("    read: ").append(toIndentedString(read)).append("\n");
    sb.append("    created: ").append(toIndentedString(created)).append("\n");
    sb.append("    message: ").append(toIndentedString(message)).append("\n");
    sb.append("    problem: ").append(toIndentedString(problem)).append("\n");
    sb.append("    actions: ").append(toIndentedString(actions)).append("\n");
    sb.append("    replies: ").append(toIndentedString(replies)).append("\n");
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

