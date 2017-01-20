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
import io.swagger.client.model.ExpenseCompanyExpense;
import io.swagger.client.model.User;
import io.swagger.client.model.UserBy;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Expense
 */
@javax.annotation.Generated(value = "io.swagger.codegen.languages.JavaClientCodegen", date = "2017-01-20T21:04:59.764Z")
public class Expense {
  @Json(name="description")
  private String description = null;

  @Json(name="quantity")
  private Integer quantity = null;

  /**
   * Gets or Sets status
   */
  public enum StatusEnum {
    @Json(name="new")
    NEW("new"),
    
    @Json(name="approved")
    APPROVED("approved"),
    
    @Json(name="disapproved")
    DISAPPROVED("disapproved");

    private String value;

    StatusEnum(String value) {
      this.value = value;
    }

    @Override
    public String toString() {
      return String.valueOf(value);
    }
  }

  @Json(name="status")
  private StatusEnum status = null;

  @Json(name="status_description")
  private String statusDescription = null;

  @Json(name="category")
  private UserBy category = null;

  @Json(name="added")
  private Date added = null;

  @Json(name="author")
  private User author = null;

  @Json(name="company_expense")
  private ExpenseCompanyExpense companyExpense = null;

  @Json(name="amount")
  private BigDecimal amount = null;

  @Json(name="reason")
  private String reason = null;

  public Expense description(String description) {
    this.description = description;
    return this;
  }

   /**
   * Get description
   * @return description
  **/
  @ApiModelProperty(example = "null", value = "")
  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public Expense quantity(Integer quantity) {
    this.quantity = quantity;
    return this;
  }

   /**
   * Get quantity
   * @return quantity
  **/
  @ApiModelProperty(example = "null", value = "")
  public Integer getQuantity() {
    return quantity;
  }

  public void setQuantity(Integer quantity) {
    this.quantity = quantity;
  }

  public Expense status(StatusEnum status) {
    this.status = status;
    return this;
  }

   /**
   * Get status
   * @return status
  **/
  @ApiModelProperty(example = "null", value = "")
  public StatusEnum getStatus() {
    return status;
  }

  public void setStatus(StatusEnum status) {
    this.status = status;
  }

  public Expense statusDescription(String statusDescription) {
    this.statusDescription = statusDescription;
    return this;
  }

   /**
   * Get statusDescription
   * @return statusDescription
  **/
  @ApiModelProperty(example = "null", value = "")
  public String getStatusDescription() {
    return statusDescription;
  }

  public void setStatusDescription(String statusDescription) {
    this.statusDescription = statusDescription;
  }

  public Expense category(UserBy category) {
    this.category = category;
    return this;
  }

   /**
   * Get category
   * @return category
  **/
  @ApiModelProperty(example = "null", value = "")
  public UserBy getCategory() {
    return category;
  }

  public void setCategory(UserBy category) {
    this.category = category;
  }

  public Expense added(Date added) {
    this.added = added;
    return this;
  }

   /**
   * Get added
   * @return added
  **/
  @ApiModelProperty(example = "null", value = "")
  public Date getAdded() {
    return added;
  }

  public void setAdded(Date added) {
    this.added = added;
  }

  public Expense author(User author) {
    this.author = author;
    return this;
  }

   /**
   * Get author
   * @return author
  **/
  @ApiModelProperty(example = "null", value = "")
  public User getAuthor() {
    return author;
  }

  public void setAuthor(User author) {
    this.author = author;
  }

  public Expense companyExpense(ExpenseCompanyExpense companyExpense) {
    this.companyExpense = companyExpense;
    return this;
  }

   /**
   * Get companyExpense
   * @return companyExpense
  **/
  @ApiModelProperty(example = "null", value = "")
  public ExpenseCompanyExpense getCompanyExpense() {
    return companyExpense;
  }

  public void setCompanyExpense(ExpenseCompanyExpense companyExpense) {
    this.companyExpense = companyExpense;
  }

  public Expense amount(BigDecimal amount) {
    this.amount = amount;
    return this;
  }

   /**
   * Get amount
   * @return amount
  **/
  @ApiModelProperty(example = "null", value = "")
  public BigDecimal getAmount() {
    return amount;
  }

  public void setAmount(BigDecimal amount) {
    this.amount = amount;
  }

  public Expense reason(String reason) {
    this.reason = reason;
    return this;
  }

   /**
   * Get reason
   * @return reason
  **/
  @ApiModelProperty(example = "null", value = "")
  public String getReason() {
    return reason;
  }

  public void setReason(String reason) {
    this.reason = reason;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Expense expense = (Expense) o;
    return Objects.equals(this.description, expense.description) &&
        Objects.equals(this.quantity, expense.quantity) &&
        Objects.equals(this.status, expense.status) &&
        Objects.equals(this.statusDescription, expense.statusDescription) &&
        Objects.equals(this.category, expense.category) &&
        Objects.equals(this.added, expense.added) &&
        Objects.equals(this.author, expense.author) &&
        Objects.equals(this.companyExpense, expense.companyExpense) &&
        Objects.equals(this.amount, expense.amount) &&
        Objects.equals(this.reason, expense.reason);
  }

  @Override
  public int hashCode() {
    return Objects.hash(description, quantity, status, statusDescription, category, added, author, companyExpense, amount, reason);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Expense {\n");
    
    sb.append("    description: ").append(toIndentedString(description)).append("\n");
    sb.append("    quantity: ").append(toIndentedString(quantity)).append("\n");
    sb.append("    status: ").append(toIndentedString(status)).append("\n");
    sb.append("    statusDescription: ").append(toIndentedString(statusDescription)).append("\n");
    sb.append("    category: ").append(toIndentedString(category)).append("\n");
    sb.append("    added: ").append(toIndentedString(added)).append("\n");
    sb.append("    author: ").append(toIndentedString(author)).append("\n");
    sb.append("    companyExpense: ").append(toIndentedString(companyExpense)).append("\n");
    sb.append("    amount: ").append(toIndentedString(amount)).append("\n");
    sb.append("    reason: ").append(toIndentedString(reason)).append("\n");
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

