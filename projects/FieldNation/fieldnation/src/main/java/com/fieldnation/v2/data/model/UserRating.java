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

public class UserRating implements Parcelable {
    private static final String TAG = "UserRating";

    @Json(name = "marketplace")
    private Rating _marketplace;

    @Json(name = "my_company")
    private Rating _myCompany;

    public UserRating() {
    }

    public void setMarketplace(Rating marketplace) {
        _marketplace = marketplace;
    }

    public Rating getMarketplace() {
        return _marketplace;
    }

    public UserRating marketplace(Rating marketplace) {
        _marketplace = marketplace;
        return this;
    }

    public void setMyCompany(Rating myCompany) {
        _myCompany = myCompany;
    }

    public Rating getMyCompany() {
        return _myCompany;
    }

    public UserRating myCompany(Rating myCompany) {
        _myCompany = myCompany;
        return this;
    }

    /*-*****************************-*/
    /*-             Json            -*/
    /*-*****************************-*/
    public static UserRating[] fromJsonArray(JsonArray array) {
        UserRating[] list = new UserRating[array.size()];
        for (int i = 0; i < array.size(); i++) {
            list[i] = fromJson(array.getJsonObject(i));
        }
        return list;
    }

    public static UserRating fromJson(JsonObject obj) {
        try {
            return Unserializer.unserializeObject(UserRating.class, obj);
        } catch (Exception ex) {
            Log.v(TAG, TAG, ex);
            return null;
        }
    }

    public JsonObject toJson() {
        return toJson(this);
    }

    public static JsonObject toJson(UserRating userRating) {
        try {
            return Serializer.serializeObject(userRating);
        } catch (Exception ex) {
            Log.v(TAG, TAG, ex);
            return null;
        }
    }

    /*-*********************************************-*/
    /*-			Parcelable Implementation           -*/
    /*-*********************************************-*/
    public static final Parcelable.Creator<UserRating> CREATOR = new Parcelable.Creator<UserRating>() {

        @Override
        public UserRating createFromParcel(Parcel source) {
            try {
                return UserRating.fromJson((JsonObject) source.readParcelable(JsonObject.class.getClassLoader()));
            } catch (Exception ex) {
                Log.v(TAG, ex);
                return null;
            }
        }

        @Override
        public UserRating[] newArray(int size) {
            return new UserRating[size];
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
