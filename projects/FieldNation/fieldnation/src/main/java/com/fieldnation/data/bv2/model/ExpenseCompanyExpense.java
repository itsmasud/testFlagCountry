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
import java.math.BigDecimal;

/**
 * ExpenseCompanyExpense
 */
@javax.annotation.Generated(value = "io.swagger.codegen.languages.JavaClientCodegen", date = "2017-01-20T21:04:59.764Z")
public class ExpenseCompanyExpense {
  @Json(name="id")
  private Integer id = null;

  @Json(name="expense_amount")
  private BigDecimal expenseAmount = null;

  @Json(name="hidden_tags")
  private String hiddenTags = null;

  @Json(name="api_code")
  private String apiCode = null;

  public ExpenseCompanyExpense id(Integer id) {
    this.id = id;
    return this;
  }

   /**
   * Get id
   * @return id
  **/
  @ApiModelProperty(example = "null", value = "")
  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public ExpenseCompanyExpense expenseAmount(BigDecimal expenseAmount) {
    this.expenseAmount = expenseAmount;
    return this;
  }

   /**
   * Get expenseAmount
   * @return expenseAmount
  **/
  @ApiModelProperty(example = "null", value = "")
  public BigDecimal getExpenseAmount() {
    return expenseAmount;
  }

  public void setExpenseAmount(BigDecimal expenseAmount) {
    this.expenseAmount = expenseAmount;
  }

  public ExpenseCompanyExpense hiddenTags(String hiddenTags) {
    this.hiddenTags = hiddenTags;
    return this;
  }

   /**
   * Get hiddenTags
   * @return hiddenTags
  **/
  @ApiModelProperty(example = "null", value = "")
  public String getHiddenTags() {
    return hiddenTags;
  }

  public void setHiddenTags(String hiddenTags) {
    this.hiddenTags = hiddenTags;
  }

  public ExpenseCompanyExpense apiCode(String apiCode) {
    this.apiCode = apiCode;
    return this;
  }

   /**
   * Get apiCode
   * @return apiCode
  **/
  @ApiModelProperty(example = "null", value = "")
  public String getApiCode() {
    return apiCode;
  }

  public void setApiCode(String apiCode) {
    this.apiCode = apiCode;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ExpenseCompanyExpense expenseCompanyExpense = (ExpenseCompanyExpense) o;
    return Objects.equals(this.id, expenseCompanyExpense.id) &&
        Objects.equals(this.expenseAmount, expenseCompanyExpense.expenseAmount) &&
        Objects.equals(this.hiddenTags, expenseCompanyExpense.hiddenTags) &&
        Objects.equals(this.apiCode, expenseCompanyExpense.apiCode);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, expenseAmount, hiddenTags, apiCode);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ExpenseCompanyExpense {\n");
    
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    expenseAmount: ").append(toIndentedString(expenseAmount)).append("\n");
    sb.append("    hiddenTags: ").append(toIndentedString(hiddenTags)).append("\n");
    sb.append("    apiCode: ").append(toIndentedString(apiCode)).append("\n");
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

