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

public class WorkOrderRatingsAssignedProviderWorkOrder implements Parcelable {
    private static final String TAG = "WorkOrderRatingsAssignedProviderWorkOrder";

    @Json(name = "categories")
    private WorkOrderRatingsAssignedProviderWorkOrderCategories[] _categories;

    @Json(name = "comment")
    private WorkOrderRatingsAssignedProviderWorkOrderComment _comment;

    @Json(name = "created")
    private Date _created;

    @Json(name = "stars")
    private Integer _stars;

    @Source
    private JsonObject SOURCE;

    public WorkOrderRatingsAssignedProviderWorkOrder() {
        SOURCE = new JsonObject();
    }

    public WorkOrderRatingsAssignedProviderWorkOrder(JsonObject obj) {
        SOURCE = obj;
    }

    public void setCategories(WorkOrderRatingsAssignedProviderWorkOrderCategories[] categories) throws ParseException {
        _categories = categories;
        SOURCE.put("categories", WorkOrderRatingsAssignedProviderWorkOrderCategories.toJsonArray(categories));
    }

    public WorkOrderRatingsAssignedProviderWorkOrderCategories[] getCategories() {
        try {
            if (_categories != null)
                return _categories;

            if (SOURCE.has("categories") && SOURCE.get("categories") != null) {
                _categories = WorkOrderRatingsAssignedProviderWorkOrderCategories.fromJsonArray(SOURCE.getJsonArray("categories"));
            }

        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _categories;
    }

    public WorkOrderRatingsAssignedProviderWorkOrder categories(WorkOrderRatingsAssignedProviderWorkOrderCategories[] categories) throws ParseException {
        _categories = categories;
        SOURCE.put("categories", WorkOrderRatingsAssignedProviderWorkOrderCategories.toJsonArray(categories), true);
        return this;
    }

    public void setComment(WorkOrderRatingsAssignedProviderWorkOrderComment comment) throws ParseException {
        _comment = comment;
        SOURCE.put("comment", comment.getJson());
    }

    public WorkOrderRatingsAssignedProviderWorkOrderComment getComment() {
        try {
            if (_comment == null && SOURCE.has("comment") && SOURCE.get("comment") != null)
                _comment = WorkOrderRatingsAssignedProviderWorkOrderComment.fromJson(SOURCE.getJsonObject("comment"));
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        if (_comment != null && _comment.isSet())
            return _comment;

        return null;
    }

    public WorkOrderRatingsAssignedProviderWorkOrder comment(WorkOrderRatingsAssignedProviderWorkOrderComment comment) throws ParseException {
        _comment = comment;
        SOURCE.put("comment", comment.getJson());
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

        if (_created != null && _created.isSet())
            return _created;

        return null;
    }

    public WorkOrderRatingsAssignedProviderWorkOrder created(Date created) throws ParseException {
        _created = created;
        SOURCE.put("created", created.getJson());
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

    public WorkOrderRatingsAssignedProviderWorkOrder stars(Integer stars) throws ParseException {
        _stars = stars;
        SOURCE.put("stars", stars);
        return this;
    }

    /*-*****************************-*/
    /*-             Json            -*/
    /*-*****************************-*/
    public static JsonArray toJsonArray(WorkOrderRatingsAssignedProviderWorkOrder[] array) {
        JsonArray list = new JsonArray();
        for (WorkOrderRatingsAssignedProviderWorkOrder item : array) {
            list.add(item.getJson());
        }
        return list;
    }

    public static WorkOrderRatingsAssignedProviderWorkOrder[] fromJsonArray(JsonArray array) {
        WorkOrderRatingsAssignedProviderWorkOrder[] list = new WorkOrderRatingsAssignedProviderWorkOrder[array.size()];
        for (int i = 0; i < array.size(); i++) {
            list[i] = fromJson(array.getJsonObject(i));
        }
        return list;
    }

    public static WorkOrderRatingsAssignedProviderWorkOrder fromJson(JsonObject obj) {
        try {
            return new WorkOrderRatingsAssignedProviderWorkOrder(obj);
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
    public static final Parcelable.Creator<WorkOrderRatingsAssignedProviderWorkOrder> CREATOR = new Parcelable.Creator<WorkOrderRatingsAssignedProviderWorkOrder>() {

        @Override
        public WorkOrderRatingsAssignedProviderWorkOrder createFromParcel(Parcel source) {
            try {
                return WorkOrderRatingsAssignedProviderWorkOrder.fromJson((JsonObject) source.readParcelable(JsonObject.class.getClassLoader()));
            } catch (Exception ex) {
                Log.v(TAG, ex);
                return null;
            }
        }

        @Override
        public WorkOrderRatingsAssignedProviderWorkOrder[] newArray(int size) {
            return new WorkOrderRatingsAssignedProviderWorkOrder[size];
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

    public boolean isSet() {
        return true;
    }
}
