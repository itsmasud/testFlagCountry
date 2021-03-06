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

public class RatingCompany implements Parcelable {
    private static final String TAG = "RatingCompany";

    @Json(name = "blocked")
    private Boolean _blocked;

    @Json(name = "marketplace")
    private RatingCompanyMarketplace _marketplace;

    @Json(name = "mine")
    private RatingCompanyMine _mine;

    @Json(name = "work_completed")
    private Integer _workCompleted;

    @Source
    private JsonObject SOURCE;

    public RatingCompany() {
        SOURCE = new JsonObject();
    }

    public RatingCompany(JsonObject obj) {
        SOURCE = obj;
    }

    public void setBlocked(Boolean blocked) throws ParseException {
        _blocked = blocked;
        SOURCE.put("blocked", blocked);
    }

    public Boolean getBlocked() {
        try {
            if (_blocked == null && SOURCE.has("blocked") && SOURCE.get("blocked") != null)
                _blocked = SOURCE.getBoolean("blocked");
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _blocked;
    }

    public RatingCompany blocked(Boolean blocked) throws ParseException {
        _blocked = blocked;
        SOURCE.put("blocked", blocked);
        return this;
    }

    public void setMarketplace(RatingCompanyMarketplace marketplace) throws ParseException {
        _marketplace = marketplace;
        SOURCE.put("marketplace", marketplace.getJson());
    }

    public RatingCompanyMarketplace getMarketplace() {
        try {
            if (_marketplace == null && SOURCE.has("marketplace") && SOURCE.get("marketplace") != null)
                _marketplace = RatingCompanyMarketplace.fromJson(SOURCE.getJsonObject("marketplace"));
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        if (_marketplace == null)
            _marketplace = new RatingCompanyMarketplace();

        return _marketplace;
    }

    public RatingCompany marketplace(RatingCompanyMarketplace marketplace) throws ParseException {
        _marketplace = marketplace;
        SOURCE.put("marketplace", marketplace.getJson());
        return this;
    }

    public void setMine(RatingCompanyMine mine) throws ParseException {
        _mine = mine;
        SOURCE.put("mine", mine.getJson());
    }

    public RatingCompanyMine getMine() {
        try {
            if (_mine == null && SOURCE.has("mine") && SOURCE.get("mine") != null)
                _mine = RatingCompanyMine.fromJson(SOURCE.getJsonObject("mine"));
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        if (_mine == null)
            _mine = new RatingCompanyMine();

        return _mine;
    }

    public RatingCompany mine(RatingCompanyMine mine) throws ParseException {
        _mine = mine;
        SOURCE.put("mine", mine.getJson());
        return this;
    }

    public void setWorkCompleted(Integer workCompleted) throws ParseException {
        _workCompleted = workCompleted;
        SOURCE.put("work_completed", workCompleted);
    }

    public Integer getWorkCompleted() {
        try {
            if (_workCompleted == null && SOURCE.has("work_completed") && SOURCE.get("work_completed") != null)
                _workCompleted = SOURCE.getInt("work_completed");
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _workCompleted;
    }

    public RatingCompany workCompleted(Integer workCompleted) throws ParseException {
        _workCompleted = workCompleted;
        SOURCE.put("work_completed", workCompleted);
        return this;
    }

    /*-*****************************-*/
    /*-             Json            -*/
    /*-*****************************-*/
    public static JsonArray toJsonArray(RatingCompany[] array) {
        JsonArray list = new JsonArray();
        for (RatingCompany item : array) {
            list.add(item.getJson());
        }
        return list;
    }

    public static RatingCompany[] fromJsonArray(JsonArray array) {
        RatingCompany[] list = new RatingCompany[array.size()];
        for (int i = 0; i < array.size(); i++) {
            list[i] = fromJson(array.getJsonObject(i));
        }
        return list;
    }

    public static RatingCompany fromJson(JsonObject obj) {
        try {
            return new RatingCompany(obj);
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
    public static final Parcelable.Creator<RatingCompany> CREATOR = new Parcelable.Creator<RatingCompany>() {

        @Override
        public RatingCompany createFromParcel(Parcel source) {
            try {
                return RatingCompany.fromJson((JsonObject) source.readParcelable(JsonObject.class.getClassLoader()));
            } catch (Exception ex) {
                Log.v(TAG, ex);
                return null;
            }
        }

        @Override
        public RatingCompany[] newArray(int size) {
            return new RatingCompany[size];
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
