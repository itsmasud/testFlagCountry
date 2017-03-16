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

public class BuyerRating implements Parcelable {
    private static final String TAG = "BuyerRating";

    @Json(name = "marketplace")
    private Double _marketplace;

    @Json(name = "user")
    private Double _user;

    @Source
    private JsonObject SOURCE;

    public BuyerRating() {
        SOURCE = new JsonObject();
    }

    public BuyerRating(JsonObject obj) {
        SOURCE = obj;
    }

    public void setMarketplace(Double marketplace) throws ParseException {
        _marketplace = marketplace;
        SOURCE.put("marketplace", marketplace);
    }

    public Double getMarketplace() {
        try {
            if (_marketplace == null && SOURCE.has("marketplace") && SOURCE.get("marketplace") != null)
                _marketplace = SOURCE.getDouble("marketplace");
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _marketplace;
    }

    public BuyerRating marketplace(Double marketplace) throws ParseException {
        _marketplace = marketplace;
        SOURCE.put("marketplace", marketplace);
        return this;
    }

    public void setUser(Double user) throws ParseException {
        _user = user;
        SOURCE.put("user", user);
    }

    public Double getUser() {
        try {
            if (_user == null && SOURCE.has("user") && SOURCE.get("user") != null)
                _user = SOURCE.getDouble("user");
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _user;
    }

    public BuyerRating user(Double user) throws ParseException {
        _user = user;
        SOURCE.put("user", user);
        return this;
    }

    /*-*****************************-*/
    /*-             Json            -*/
    /*-*****************************-*/
    public static JsonArray toJsonArray(BuyerRating[] array) {
        JsonArray list = new JsonArray();
        for (BuyerRating item : array) {
            list.add(item.getJson());
        }
        return list;
    }

    public static BuyerRating[] fromJsonArray(JsonArray array) {
        BuyerRating[] list = new BuyerRating[array.size()];
        for (int i = 0; i < array.size(); i++) {
            list[i] = fromJson(array.getJsonObject(i));
        }
        return list;
    }

    public static BuyerRating fromJson(JsonObject obj) {
        try {
            return new BuyerRating(obj);
        } catch (Exception ex) {
            Log.v(TAG, TAG, ex);
            return null;
        }
    }

    public JsonObject getJson() {
        return SOURCE;
    }

    /*-*********************************************-*/
    /*-			Parcelable Implementation           -*/
    /*-*********************************************-*/
    public static final Creator<BuyerRating> CREATOR = new Creator<BuyerRating>() {

        @Override
        public BuyerRating createFromParcel(Parcel source) {
            try {
                return BuyerRating.fromJson((JsonObject) source.readParcelable(JsonObject.class.getClassLoader()));
            } catch (Exception ex) {
                Log.v(TAG, ex);
                return null;
            }
        }

        @Override
        public BuyerRating[] newArray(int size) {
            return new BuyerRating[size];
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
