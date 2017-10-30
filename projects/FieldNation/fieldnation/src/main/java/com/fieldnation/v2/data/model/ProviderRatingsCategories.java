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

public class ProviderRatingsCategories implements Parcelable {
    private static final String TAG = "ProviderRatingsCategories";

    @Json(name = "percent")
    private Integer _percent;

    @Json(name = "type")
    private String _type;

    @Source
    private JsonObject SOURCE;

    public ProviderRatingsCategories() {
        SOURCE = new JsonObject();
    }

    public ProviderRatingsCategories(JsonObject obj) {
        SOURCE = obj;
    }

    public void setPercent(Integer percent) throws ParseException {
        _percent = percent;
        SOURCE.put("percent", percent);
    }

    public Integer getPercent() {
        try {
            if (_percent == null && SOURCE.has("percent") && SOURCE.get("percent") != null)
                _percent = SOURCE.getInt("percent");
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _percent;
    }

    public ProviderRatingsCategories percent(Integer percent) throws ParseException {
        _percent = percent;
        SOURCE.put("percent", percent);
        return this;
    }

    public void setType(String type) throws ParseException {
        _type = type;
        SOURCE.put("type", type);
    }

    public String getType() {
        try {
            if (_type == null && SOURCE.has("type") && SOURCE.get("type") != null)
                _type = SOURCE.getString("type");
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _type;
    }

    public ProviderRatingsCategories type(String type) throws ParseException {
        _type = type;
        SOURCE.put("type", type);
        return this;
    }

    /*-*****************************-*/
    /*-             Json            -*/
    /*-*****************************-*/
    public static JsonArray toJsonArray(ProviderRatingsCategories[] array) {
        JsonArray list = new JsonArray();
        for (ProviderRatingsCategories item : array) {
            list.add(item.getJson());
        }
        return list;
    }

    public static ProviderRatingsCategories[] fromJsonArray(JsonArray array) {
        ProviderRatingsCategories[] list = new ProviderRatingsCategories[array.size()];
        for (int i = 0; i < array.size(); i++) {
            list[i] = fromJson(array.getJsonObject(i));
        }
        return list;
    }

    public static ProviderRatingsCategories fromJson(JsonObject obj) {
        try {
            return new ProviderRatingsCategories(obj);
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
    public static final Parcelable.Creator<ProviderRatingsCategories> CREATOR = new Parcelable.Creator<ProviderRatingsCategories>() {

        @Override
        public ProviderRatingsCategories createFromParcel(Parcel source) {
            try {
                return ProviderRatingsCategories.fromJson((JsonObject) source.readParcelable(JsonObject.class.getClassLoader()));
            } catch (Exception ex) {
                Log.v(TAG, ex);
                return null;
            }
        }

        @Override
        public ProviderRatingsCategories[] newArray(int size) {
            return new ProviderRatingsCategories[size];
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
