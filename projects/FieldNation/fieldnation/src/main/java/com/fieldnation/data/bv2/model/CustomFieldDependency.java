package com.fieldnation.data.bv2.model;

import com.fieldnation.fnjson.annotations.Json;

public class CustomFieldDependency {
    private static final String TAG = "CustomFieldDependency";

    @Json(name = "id")
    private Integer id = null;

    @Json(name = "value")
    private String value = null;

    public enum OperatorEnum {
        LESS_THAN("less_than"),
        GREATER_THAN("greater_than"),
        EQUALS("equals"),
        LESS_THAN_EQUALS("less_than_equals"),
        GREATER_THAN_EQUALS("greater_than_equals");

        private String value;

        OperatorEnum(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return String.valueOf(value);
        }
    }

    @Json(name = "operator")
    private OperatorEnum operator = null;

    public CustomFieldDependency id(Integer id) {
        this.id = id;
        return this;
    }

    /**
     * Get id
     *
     * @return id
     **/
    @ApiModelProperty(example = "null", value = "")
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public CustomFieldDependency value(String value) {
        this.value = value;
        return this;
    }

    /**
     * Get value
     *
     * @return value
     **/
    @ApiModelProperty(example = "null", value = "")
    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public CustomFieldDependency operator(OperatorEnum operator) {
        this.operator = operator;
        return this;
    }

    /**
     * Get operator
     *
     * @return operator
     **/
    @ApiModelProperty(example = "null", value = "")
    public OperatorEnum getOperator() {
        return operator;
    }

    public void setOperator(OperatorEnum operator) {
        this.operator = operator;
    }


    @Override
    public boolean equals(java.lang.Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        CustomFieldDependency customFieldDependency = (CustomFieldDependency) o;
        return Objects.equals(this.id, customFieldDependency.id) &&
                Objects.equals(this.value, customFieldDependency.value) &&
                Objects.equals(this.operator, customFieldDependency.operator);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, value, operator);
    }


    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class CustomFieldDependency {\n");

        sb.append("    id: ").append(toIndentedString(id)).append("\n");
        sb.append("    value: ").append(toIndentedString(value)).append("\n");
        sb.append("    operator: ").append(toIndentedString(operator)).append("\n");
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

