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

public class WorkOrderRatingsBuyerCompany implements Parcelable {
    private static final String TAG = "WorkOrderRatingsBuyerCompany";

    @Json(name = "id")
    private Integer _id;

    @Source
    private JsonObject SOURCE;

    public WorkOrderRatingsBuyerCompany() {
        SOURCE = new JsonObject();
    }

    public WorkOrderRatingsBuyerCompany(JsonObject obj) {
        SOURCE = obj;
    }

    public void setId(Integer id) throws ParseException {
        _id = id;
        SOURCE.put("id", id);
    }

    public Integer getId() {
        try {
            if (_id == null && SOURCE.has("id") && SOURCE.get("id") != null)
                _id = SOURCE.getInt("id");
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _id;
    }

    public WorkOrderRatingsBuyerCompany id(Integer id) throws ParseException {
        _id = id;
        SOURCE.put("id", id);
        return this;
    }

    /*-*****************************-*/
    /*-             Json            -*/
    /*-*****************************-*/
    public static JsonArray toJsonArray(WorkOrderRatingsBuyerCompany[] array) {
        JsonArray list = new JsonArray();
        for (WorkOrderRatingsBuyerCompany item : array) {
            list.add(item.getJson());
        }
        return list;
    }

    public static WorkOrderRatingsBuyerCompany[] fromJsonArray(JsonArray array) {
        WorkOrderRatingsBuyerCompany[] list = new WorkOrderRatingsBuyerCompany[array.size()];
        for (int i = 0; i < array.size(); i++) {
            list[i] = fromJson(array.getJsonObject(i));
        }
        return list;
    }

    public static WorkOrderRatingsBuyerCompany fromJson(JsonObject obj) {
        try {
            return new WorkOrderRatingsBuyerCompany(obj);
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
    public static final Parcelable.Creator<WorkOrderRatingsBuyerCompany> CREATOR = new Parcelable.Creator<WorkOrderRatingsBuyerCompany>() {

        @Override
        public WorkOrderRatingsBuyerCompany createFromParcel(Parcel source) {
            try {
                return WorkOrderRatingsBuyerCompany.fromJson((JsonObject) source.readParcelable(JsonObject.class.getClassLoader()));
            } catch (Exception ex) {
                Log.v(TAG, ex);
                return null;
            }
        }

        @Override
        public WorkOrderRatingsBuyerCompany[] newArray(int size) {
            return new WorkOrderRatingsBuyerCompany[size];
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
