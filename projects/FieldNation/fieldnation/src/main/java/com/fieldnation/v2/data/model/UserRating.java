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

public class UserRating implements Parcelable {
    private static final String TAG = "UserRating";

    @Json(name = "marketplace")
    private Rating _marketplace;

    @Json(name = "my_company")
    private Rating _myCompany;

    @Source
    private JsonObject SOURCE;

    public UserRating() {
        SOURCE = new JsonObject();
    }

    public UserRating(JsonObject obj) {
        SOURCE = obj;
    }

    public void setMarketplace(Rating marketplace) throws ParseException {
        _marketplace = marketplace;
        SOURCE.put("marketplace", marketplace.getJson());
    }

    public Rating getMarketplace() {
        try {
            if (_marketplace == null && SOURCE.has("marketplace") && SOURCE.get("marketplace") != null)
                _marketplace = Rating.fromJson(SOURCE.getJsonObject("marketplace"));
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        if (_marketplace == null)
            _marketplace = new Rating();

        return _marketplace;
    }

    public UserRating marketplace(Rating marketplace) throws ParseException {
        _marketplace = marketplace;
        SOURCE.put("marketplace", marketplace.getJson());
        return this;
    }

    public void setMyCompany(Rating myCompany) throws ParseException {
        _myCompany = myCompany;
        SOURCE.put("my_company", myCompany.getJson());
    }

    public Rating getMyCompany() {
        try {
            if (_myCompany == null && SOURCE.has("my_company") && SOURCE.get("my_company") != null)
                _myCompany = Rating.fromJson(SOURCE.getJsonObject("my_company"));
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        if (_myCompany == null)
            _myCompany = new Rating();

        return _myCompany;
    }

    public UserRating myCompany(Rating myCompany) throws ParseException {
        _myCompany = myCompany;
        SOURCE.put("my_company", myCompany.getJson());
        return this;
    }

    /*-*****************************-*/
    /*-             Json            -*/
    /*-*****************************-*/
    public static JsonArray toJsonArray(UserRating[] array) {
        JsonArray list = new JsonArray();
        for (UserRating item : array) {
            list.add(item.getJson());
        }
        return list;
    }

    public static UserRating[] fromJsonArray(JsonArray array) {
        UserRating[] list = new UserRating[array.size()];
        for (int i = 0; i < array.size(); i++) {
            list[i] = fromJson(array.getJsonObject(i));
        }
        return list;
    }

    public static UserRating fromJson(JsonObject obj) {
        try {
            return new UserRating(obj);
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
        dest.writeParcelable(getJson(), flags);
    }

    /*-*****************************-*/
    /*-         Human Code          -*/
    /*-*****************************-*/

}
