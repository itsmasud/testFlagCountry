package com.fieldnation.v2.data.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.fieldnation.fnjson.JsonArray;
import com.fieldnation.fnjson.JsonObject;
import com.fieldnation.fnjson.Serializer;
import com.fieldnation.fnjson.Unserializer;
import com.fieldnation.fnjson.annotations.Json;
import com.fieldnation.fnlog.Log;

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

    public RatingCompany() {
    }

    public void setBlocked(Boolean blocked) {
        _blocked = blocked;
    }

    public Boolean getBlocked() {
        return _blocked;
    }

    public RatingCompany blocked(Boolean blocked) {
        _blocked = blocked;
        return this;
    }

    public void setMarketplace(RatingCompanyMarketplace marketplace) {
        _marketplace = marketplace;
    }

    public RatingCompanyMarketplace getMarketplace() {
        return _marketplace;
    }

    public RatingCompany marketplace(RatingCompanyMarketplace marketplace) {
        _marketplace = marketplace;
        return this;
    }

    public void setMine(RatingCompanyMine mine) {
        _mine = mine;
    }

    public RatingCompanyMine getMine() {
        return _mine;
    }

    public RatingCompany mine(RatingCompanyMine mine) {
        _mine = mine;
        return this;
    }

    public void setWorkCompleted(Integer workCompleted) {
        _workCompleted = workCompleted;
    }

    public Integer getWorkCompleted() {
        return _workCompleted;
    }

    public RatingCompany workCompleted(Integer workCompleted) {
        _workCompleted = workCompleted;
        return this;
    }

    /*-*****************************-*/
    /*-             Json            -*/
    /*-*****************************-*/
    public static RatingCompany[] fromJsonArray(JsonArray array) {
        RatingCompany[] list = new RatingCompany[array.size()];
        for (int i = 0; i < array.size(); i++) {
            list[i] = fromJson(array.getJsonObject(i));
        }
        return list;
    }

    public static RatingCompany fromJson(JsonObject obj) {
        try {
            return Unserializer.unserializeObject(RatingCompany.class, obj);
        } catch (Exception ex) {
            Log.v(TAG, TAG, ex);
            return null;
        }
    }

    public JsonObject toJson() {
        return toJson(this);
    }

    public static JsonObject toJson(RatingCompany ratingCompany) {
        try {
            return Serializer.serializeObject(ratingCompany);
        } catch (Exception ex) {
            Log.v(TAG, TAG, ex);
            return null;
        }
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
        dest.writeParcelable(toJson(), flags);
    }
}
