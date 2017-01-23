package com.fieldnation.data.bv2.model;

import com.fieldnation.fnjson.annotations.Json;

public class ListEnvelope {
    private static final String TAG = "ListEnvelope";

    @Json(name = "total")
    private Integer total = null;

    @Json(name = "page")
    private Integer page = null;

    @Json(name = "pages")
    private Integer pages = null;

    @Json(name = "per_page")
    private Integer perPage = null;

    @Json(name = "columns")
    private String columns = null;

    @Json(name = "available_columns")
    private AvailableColumn[] availableColumns;

    @Json(name = "list")
    private String list = null;

    @Json(name = "view")
    private String view = null;

    @Json(name = "sort")
    private String sort = null;

    @Json(name = "order")
    private String order = null;


    public enum ViewEnum {
        MAP("map"),
        LIST("list"),
        SCHEDULE("schedule"),
        CARD("card");

        private String value;

        ViewEnum(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return String.valueOf(value);
        }

        public static ViewEnum fromValue(String value) {
            ViewEnum[] values = values();
            for (ViewEnum e : values) {
                if (e.value.equals(value))
                    return e;
            }
            return null;
        }
    }

    public enum OrderEnum {
        ASC("asc"),
        DESC("desc");

        private String value;

        OrderEnum(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return String.valueOf(value);
        }
    }

    public ListEnvelope total(Integer total) {
        this.total = total;
        return this;
    }

    /**
     * Get total
     *
     * @return total
     **/
    @ApiModelProperty(example = "null", value = "")
    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public ListEnvelope page(Integer page) {
        this.page = page;
        return this;
    }

    /**
     * Get page
     *
     * @return page
     **/
    @ApiModelProperty(example = "null", value = "")
    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public ListEnvelope pages(Integer pages) {
        this.pages = pages;
        return this;
    }

    /**
     * Get pages
     *
     * @return pages
     **/
    @ApiModelProperty(example = "null", value = "")
    public Integer getPages() {
        return pages;
    }

    public void setPages(Integer pages) {
        this.pages = pages;
    }

    public ListEnvelope perPage(Integer perPage) {
        this.perPage = perPage;
        return this;
    }

    /**
     * Get perPage
     *
     * @return perPage
     **/
    @ApiModelProperty(example = "null", value = "")
    public Integer getPerPage() {
        return perPage;
    }

    public void setPerPage(Integer perPage) {
        this.perPage = perPage;
    }

    public ListEnvelope columns(String columns) {
        this.columns = columns;
        return this;
    }

    /**
     * Get columns
     *
     * @return columns
     **/
    @ApiModelProperty(example = "null", value = "")
    public String getColumns() {
        return columns;
    }

    public void setColumns(String columns) {
        this.columns = columns;
    }

    public ListEnvelope availableColumns(List<AvailableColumn> availableColumns) {
        this.availableColumns = availableColumns;
        return this;
    }

    public ListEnvelope addAvailableColumnsItem(AvailableColumn availableColumnsItem) {
        this.availableColumns.add(availableColumnsItem);
        return this;
    }

    /**
     * Get availableColumns
     *
     * @return availableColumns
     **/
    @ApiModelProperty(example = "null", value = "")
    public List<AvailableColumn> getAvailableColumns() {
        return availableColumns;
    }

    public void setAvailableColumns(List<AvailableColumn> availableColumns) {
        this.availableColumns = availableColumns;
    }

    public ListEnvelope list(String list) {
        this.list = list;
        return this;
    }

    /**
     * Get list
     *
     * @return list
     **/
    @ApiModelProperty(example = "null", value = "")
    public String getList() {
        return list;
    }

    public void setList(String list) {
        this.list = list;
    }

    public ListEnvelope view(ViewEnum view) {
        this.view = view;
        return this;
    }

    /**
     * Get view
     *
     * @return view
     **/
    @ApiModelProperty(example = "null", value = "")
    public ViewEnum getView() {
        return view;
    }

    public void setView(ViewEnum view) {
        this.view = view;
    }

    public ListEnvelope sort(String sort) {
        this.sort = sort;
        return this;
    }

    /**
     * Get sort
     *
     * @return sort
     **/
    @ApiModelProperty(example = "null", value = "")
    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public ListEnvelope order(OrderEnum order) {
        this.order = order;
        return this;
    }

    /**
     * Get order
     *
     * @return order
     **/
    @ApiModelProperty(example = "null", value = "")
    public OrderEnum getOrder() {
        return order;
    }

    public void setOrder(OrderEnum order) {
        this.order = order;
    }


    @Override
    public boolean equals(java.lang.Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ListEnvelope listEnvelope = (ListEnvelope) o;
        return Objects.equals(this.total, listEnvelope.total) &&
                Objects.equals(this.page, listEnvelope.page) &&
                Objects.equals(this.pages, listEnvelope.pages) &&
                Objects.equals(this.perPage, listEnvelope.perPage) &&
                Objects.equals(this.columns, listEnvelope.columns) &&
                Objects.equals(this.availableColumns, listEnvelope.availableColumns) &&
                Objects.equals(this.list, listEnvelope.list) &&
                Objects.equals(this.view, listEnvelope.view) &&
                Objects.equals(this.sort, listEnvelope.sort) &&
                Objects.equals(this.order, listEnvelope.order);
    }

    @Override
    public int hashCode() {
        return Objects.hash(total, page, pages, perPage, columns, availableColumns, list, view, sort, order);
    }


    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class ListEnvelope {\n");

        sb.append("    total: ").append(toIndentedString(total)).append("\n");
        sb.append("    page: ").append(toIndentedString(page)).append("\n");
        sb.append("    pages: ").append(toIndentedString(pages)).append("\n");
        sb.append("    perPage: ").append(toIndentedString(perPage)).append("\n");
        sb.append("    columns: ").append(toIndentedString(columns)).append("\n");
        sb.append("    availableColumns: ").append(toIndentedString(availableColumns)).append("\n");
        sb.append("    list: ").append(toIndentedString(list)).append("\n");
        sb.append("    view: ").append(toIndentedString(view)).append("\n");
        sb.append("    sort: ").append(toIndentedString(sort)).append("\n");
        sb.append("    order: ").append(toIndentedString(order)).append("\n");
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

