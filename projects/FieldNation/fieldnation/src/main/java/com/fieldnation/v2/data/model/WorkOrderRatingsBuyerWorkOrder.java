package com.fieldnation.v2.data.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.fieldnation.fnjson.JsonArray;
import com.fieldnation.fnjson.JsonObject;
import com.fieldnation.fnjson.Serializer;
import com.fieldnation.fnjson.Unserializer;
import com.fieldnation.fnjson.annotations.Json;
import com.fieldnation.fnjson.annotations.Source;
import com.fieldnation.fnlog.Log;
import com.fieldnation.fntools.misc;

import java.text.ParseException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by dmgen from swagger.
 */

public class WorkOrderRatingsBuyerWorkOrder implements Parcelable {
    private static final String TAG = "WorkOrderRatingsBuyerWorkOrder";

    @Json(name = "categories")
    private WorkOrderRatingsBuyerWorkOrderCategories[] _categories;

    @Json(name = "comment")
    private String _comment;

    @Json(name = "created")
    private Date _created;

    @Json(name = "flagged")
    private Boolean _flagged;

    @Json(name = "form")
    private FormEnum _form;

    @Json(name = "hidden")
    private Boolean _hidden;

    @Json(name = "recommend")
    private Boolean _recommend;

    @Json(name = "stars")
    private Integer _stars;

    @Source
    private JsonObject SOURCE;

    public WorkOrderRatingsBuyerWorkOrder() {
        SOURCE = new JsonObject();
    }

    public WorkOrderRatingsBuyerWorkOrder(JsonObject obj) {
        SOURCE = obj;
    }

    public void setCategories(WorkOrderRatingsBuyerWorkOrderCategories[] categories) throws ParseException {
        _categories = categories;
        SOURCE.put("categories", WorkOrderRatingsBuyerWorkOrderCategories.toJsonArray(categories));
    }

    public WorkOrderRatingsBuyerWorkOrderCategories[] getCategories() {
        try {
            if (_categories != null)
                return _categories;

            if (SOURCE.has("categories") && SOURCE.get("categories") != null) {
                _categories = WorkOrderRatingsBuyerWorkOrderCategories.fromJsonArray(SOURCE.getJsonArray("categories"));
            }

        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        if (_categories == null)
            _categories = new WorkOrderRatingsBuyerWorkOrderCategories[0];

        return _categories;
    }

    public WorkOrderRatingsBuyerWorkOrder categories(WorkOrderRatingsBuyerWorkOrderCategories[] categories) throws ParseException {
        _categories = categories;
        SOURCE.put("categories", WorkOrderRatingsBuyerWorkOrderCategories.toJsonArray(categories), true);
        return this;
    }

    public void setComment(String comment) throws ParseException {
        _comment = comment;
        SOURCE.put("comment", comment);
    }

    public String getComment() {
        try {
            if (_comment == null && SOURCE.has("comment") && SOURCE.get("comment") != null)
                _comment = SOURCE.getString("comment");
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _comment;
    }

    public WorkOrderRatingsBuyerWorkOrder comment(String comment) throws ParseException {
        _comment = comment;
        SOURCE.put("comment", comment);
        return this;
    }

    public void setCreated(Date created) throws ParseException {
        _created = created;
        SOURCE.put("created", created.getJson());
    }

    public Date getCreated() {
        try {
            if (_created == null && SOURCE.has("created") && SOURCE.get("created") != null)
                _created = Date.fromJson(SOURCE.getJsonObject("created"));
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        if (_created == null)
            _created = new Date();

            return _created;
    }

    public WorkOrderRatingsBuyerWorkOrder created(Date created) throws ParseException {
        _created = created;
        SOURCE.put("created", created.getJson());
        return this;
    }

    public void setFlagged(Boolean flagged) throws ParseException {
        _flagged = flagged;
        SOURCE.put("flagged", flagged);
    }

    public Boolean getFlagged() {
        try {
            if (_flagged == null && SOURCE.has("flagged") && SOURCE.get("flagged") != null)
                _flagged = SOURCE.getBoolean("flagged");
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _flagged;
    }

    public WorkOrderRatingsBuyerWorkOrder flagged(Boolean flagged) throws ParseException {
        _flagged = flagged;
        SOURCE.put("flagged", flagged);
        return this;
    }

    public void setForm(FormEnum form) throws ParseException {
        _form = form;
        SOURCE.put("form", form.toString());
    }

    public FormEnum getForm() {
        try {
            if (_form == null && SOURCE.has("form") && SOURCE.get("form") != null)
                _form = FormEnum.fromString(SOURCE.getString("form"));
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _form;
    }

    public WorkOrderRatingsBuyerWorkOrder form(FormEnum form) throws ParseException {
        _form = form;
        SOURCE.put("form", form.toString());
        return this;
    }

    public void setHidden(Boolean hidden) throws ParseException {
        _hidden = hidden;
        SOURCE.put("hidden", hidden);
    }

    public Boolean getHidden() {
        try {
            if (_hidden == null && SOURCE.has("hidden") && SOURCE.get("hidden") != null)
                _hidden = SOURCE.getBoolean("hidden");
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _hidden;
    }

    public WorkOrderRatingsBuyerWorkOrder hidden(Boolean hidden) throws ParseException {
        _hidden = hidden;
        SOURCE.put("hidden", hidden);
        return this;
    }

    public void setRecommend(Boolean recommend) throws ParseException {
        _recommend = recommend;
        SOURCE.put("recommend", recommend);
    }

    public Boolean getRecommend() {
        try {
            if (_recommend == null && SOURCE.has("recommend") && SOURCE.get("recommend") != null)
                _recommend = SOURCE.getBoolean("recommend");
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _recommend;
    }

    public WorkOrderRatingsBuyerWorkOrder recommend(Boolean recommend) throws ParseException {
        _recommend = recommend;
        SOURCE.put("recommend", recommend);
        return this;
    }

    public void setStars(Integer stars) throws ParseException {
        _stars = stars;
        SOURCE.put("stars", stars);
    }

    public Integer getStars() {
        try {
            if (_stars == null && SOURCE.has("stars") && SOURCE.get("stars") != null)
                _stars = SOURCE.getInt("stars");
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _stars;
    }

    public WorkOrderRatingsBuyerWorkOrder stars(Integer stars) throws ParseException {
        _stars = stars;
        SOURCE.put("stars", stars);
        return this;
    }

    /*-******************************-*/
    /*-             Enums            -*/
    /*-******************************-*/
    public enum FormEnum {
        @Json(name = "new")
        NEW("new"),
        @Json(name = "old")
        OLD("old");

        private String value;

        FormEnum(String value) {
            this.value = value;
        }

        public static FormEnum fromString(String value) {
            FormEnum[] values = values();
            for (FormEnum v : values) {
                if (v.value.equals(value))
                    return v;
            }
            return null;
        }

        public static FormEnum[] fromJsonArray(JsonArray jsonArray) {
            FormEnum[] list = new FormEnum[jsonArray.size()];
            for (int i = 0; i < list.length; i++) {
                list[i] = fromString(jsonArray.getString(i));
            }
            return list;
        }

        @Override
        public String toString() {
            return String.valueOf(value);
        }
    }

    /*-*****************************-*/
    /*-             Json            -*/
    /*-*****************************-*/
    public static JsonArray toJsonArray(WorkOrderRatingsBuyerWorkOrder[] array) {
        JsonArray list = new JsonArray();
        for (WorkOrderRatingsBuyerWorkOrder item : array) {
            list.add(item.getJson());
        }
        return list;
    }

    public static WorkOrderRatingsBuyerWorkOrder[] fromJsonArray(JsonArray array) {
        WorkOrderRatingsBuyerWorkOrder[] list = new WorkOrderRatingsBuyerWorkOrder[array.size()];
        for (int i = 0; i < array.size(); i++) {
            list[i] = fromJson(array.getJsonObject(i));
        }
        return list;
    }

    public static WorkOrderRatingsBuyerWorkOrder fromJson(JsonObject obj) {
        try {
            return new WorkOrderRatingsBuyerWorkOrder(obj);
        } catch (Exception ex) {
            Log.v(TAG, ex);
            return null;
        }
    }

    public JsonObject getJson() {
        return SOURCE;
    }

    /*-*********************************************-*/
    /*-			Parcelable Implementation           -*/
    /*-*********************************************-*/
    public static final Parcelable.Creator<WorkOrderRatingsBuyerWorkOrder> CREATOR = new Parcelable.Creator<WorkOrderRatingsBuyerWorkOrder>() {

        @Override
        public WorkOrderRatingsBuyerWorkOrder createFromParcel(Parcel source) {
            try {
                return WorkOrderRatingsBuyerWorkOrder.fromJson((JsonObject) source.readParcelable(JsonObject.class.getClassLoader()));
            } catch (Exception ex) {
                Log.v(TAG, ex);
                return null;
            }
        }

        @Override
        public WorkOrderRatingsBuyerWorkOrder[] newArray(int size) {
            return new WorkOrderRatingsBuyerWorkOrder[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(getJson(), flags);
    }

    /*-*****************************-*/
    /*-         Human Code          -*/
    /*-*****************************-*/

}
