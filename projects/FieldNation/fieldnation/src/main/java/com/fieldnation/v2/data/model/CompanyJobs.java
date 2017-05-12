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

public class CompanyJobs implements Parcelable {
    private static final String TAG = "CompanyJobs";

    @Json(name = "marketplace")
    private Integer _marketplace;

    @Json(name = "my_company")
    private Integer _myCompany;

    @Source
    private JsonObject SOURCE;

    public CompanyJobs() {
        SOURCE = new JsonObject();
    }

    public CompanyJobs(JsonObject obj) {
        SOURCE = obj;
    }

    public void setMarketplace(Integer marketplace) throws ParseException {
        _marketplace = marketplace;
        SOURCE.put("marketplace", marketplace);
    }

    public Integer getMarketplace() {
        try {
            if (_marketplace == null && SOURCE.has("marketplace") && SOURCE.get("marketplace") != null)
                _marketplace = SOURCE.getInt("marketplace");
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _marketplace;
    }

    public CompanyJobs marketplace(Integer marketplace) throws ParseException {
        _marketplace = marketplace;
        SOURCE.put("marketplace", marketplace);
        return this;
    }

    public void setMyCompany(Integer myCompany) throws ParseException {
        _myCompany = myCompany;
        SOURCE.put("my_company", myCompany);
    }

    public Integer getMyCompany() {
        try {
            if (_myCompany == null && SOURCE.has("my_company") && SOURCE.get("my_company") != null)
                _myCompany = SOURCE.getInt("my_company");
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _myCompany;
    }

    public CompanyJobs myCompany(Integer myCompany) throws ParseException {
        _myCompany = myCompany;
        SOURCE.put("my_company", myCompany);
        return this;
    }

    /*-*****************************-*/
    /*-             Json            -*/
    /*-*****************************-*/
    public static JsonArray toJsonArray(CompanyJobs[] array) {
        JsonArray list = new JsonArray();
        for (CompanyJobs item : array) {
            list.add(item.getJson());
        }
        return list;
    }

    public static CompanyJobs[] fromJsonArray(JsonArray array) {
        CompanyJobs[] list = new CompanyJobs[array.size()];
        for (int i = 0; i < array.size(); i++) {
            list[i] = fromJson(array.getJsonObject(i));
        }
        return list;
    }

    public static CompanyJobs fromJson(JsonObject obj) {
        try {
            return new CompanyJobs(obj);
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
    public static final Parcelable.Creator<CompanyJobs> CREATOR = new Parcelable.Creator<CompanyJobs>() {

        @Override
        public CompanyJobs createFromParcel(Parcel source) {
            try {
                return CompanyJobs.fromJson((JsonObject) source.readParcelable(JsonObject.class.getClassLoader()));
            } catch (Exception ex) {
                Log.v(TAG, ex);
                return null;
            }
        }

        @Override
        public CompanyJobs[] newArray(int size) {
            return new CompanyJobs[size];
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
