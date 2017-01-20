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
 * RichText
 */
@javax.annotation.Generated(value = "io.swagger.codegen.languages.JavaClientCodegen", date = "2017-01-20T21:04:59.764Z")
public class RichText {
  @Json(name="html")
  private String html = null;

  @Json(name="markdown")
  private String markdown = null;

  public RichText html(String html) {
    this.html = html;
    return this;
  }

   /**
   * Get html
   * @return html
  **/
  @ApiModelProperty(example = "null", value = "")
  public String getHtml() {
    return html;
  }

  public void setHtml(String html) {
    this.html = html;
  }

  public RichText markdown(String markdown) {
    this.markdown = markdown;
    return this;
  }

   /**
   * Get markdown
   * @return markdown
  **/
  @ApiModelProperty(example = "null", value = "")
  public String getMarkdown() {
    return markdown;
  }

  public void setMarkdown(String markdown) {
    this.markdown = markdown;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    RichText richText = (RichText) o;
    return Objects.equals(this.html, richText.html) &&
        Objects.equals(this.markdown, richText.markdown);
  }

  @Override
  public int hashCode() {
    return Objects.hash(html, markdown);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class RichText {\n");
    
    sb.append("    html: ").append(toIndentedString(html)).append("\n");
    sb.append("    markdown: ").append(toIndentedString(markdown)).append("\n");
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

