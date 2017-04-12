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

public class CompanyFeature implements Parcelable {
    private static final String TAG = "CompanyFeature";

    @Json(name = "company_features_id")
    private Integer _companyFeaturesId;

    @Json(name = "company_id")
    private Integer _companyId;

    @Json(name = "feature_id")
    private Integer _featureId;

    @Json(name = "status")
    private StatusEnum _status;

    @Source
    private JsonObject SOURCE;

    public CompanyFeature() {
        SOURCE = new JsonObject();
    }

    public CompanyFeature(JsonObject obj) {
        SOURCE = obj;
    }

    public void setCompanyFeaturesId(Integer companyFeaturesId) throws ParseException {
        _companyFeaturesId = companyFeaturesId;
        SOURCE.put("company_features_id", companyFeaturesId);
    }

    public Integer getCompanyFeaturesId() {
        try {
            if (_companyFeaturesId == null && SOURCE.has("company_features_id") && SOURCE.get("company_features_id") != null)
                _companyFeaturesId = SOURCE.getInt("company_features_id");
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _companyFeaturesId;
    }

    public CompanyFeature companyFeaturesId(Integer companyFeaturesId) throws ParseException {
        _companyFeaturesId = companyFeaturesId;
        SOURCE.put("company_features_id", companyFeaturesId);
        return this;
    }

    public void setCompanyId(Integer companyId) throws ParseException {
        _companyId = companyId;
        SOURCE.put("company_id", companyId);
    }

    public Integer getCompanyId() {
        try {
            if (_companyId == null && SOURCE.has("company_id") && SOURCE.get("company_id") != null)
                _companyId = SOURCE.getInt("company_id");
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _companyId;
    }

    public CompanyFeature companyId(Integer companyId) throws ParseException {
        _companyId = companyId;
        SOURCE.put("company_id", companyId);
        return this;
    }

    public void setFeatureId(Integer featureId) throws ParseException {
        _featureId = featureId;
        SOURCE.put("feature_id", featureId);
    }

    public Integer getFeatureId() {
        try {
            if (_featureId == null && SOURCE.has("feature_id") && SOURCE.get("feature_id") != null)
                _featureId = SOURCE.getInt("feature_id");
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _featureId;
    }

    public CompanyFeature featureId(Integer featureId) throws ParseException {
        _featureId = featureId;
        SOURCE.put("feature_id", featureId);
        return this;
    }

    public void setStatus(StatusEnum status) throws ParseException {
        _status = status;
        SOURCE.put("status", status.toString());
    }

    public StatusEnum getStatus() {
        try {
            if (_status == null && SOURCE.has("status") && SOURCE.get("status") != null)
                _status = StatusEnum.fromString(SOURCE.getString("status"));
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _status;
    }

    public CompanyFeature status(StatusEnum status) throws ParseException {
        _status = status;
        SOURCE.put("status", status.toString());
        return this;
    }

    /*-******************************-*/
    /*-             Enums            -*/
    /*-******************************-*/
    public enum StatusEnum {
        @Json(name = "denied")
        DENIED("denied"),
        @Json(name = "disabled")
        DISABLED("disabled"),
        @Json(name = "enabled")
        ENABLED("enabled"),
        @Json(name = "requested")
        REQUESTED("requested");

        private String value;

        StatusEnum(String value) {
            this.value = value;
        }

        public static StatusEnum fromString(String value) {
            StatusEnum[] values = values();
            for (StatusEnum v : values) {
                if (v.value.equals(value))
                    return v;
            }
            return null;
        }

        public static StatusEnum[] fromJsonArray(JsonArray jsonArray) {
            StatusEnum[] list = new StatusEnum[jsonArray.size()];
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
    public static JsonArray toJsonArray(CompanyFeature[] array) {
        JsonArray list = new JsonArray();
        for (CompanyFeature item : array) {
            list.add(item.getJson());
        }
        return list;
    }

    public static CompanyFeature[] fromJsonArray(JsonArray array) {
        CompanyFeature[] list = new CompanyFeature[array.size()];
        for (int i = 0; i < array.size(); i++) {
            list[i] = fromJson(array.getJsonObject(i));
        }
        return list;
    }

    public static CompanyFeature fromJson(JsonObject obj) {
        try {
            return new CompanyFeature(obj);
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
    public static final Parcelable.Creator<CompanyFeature> CREATOR = new Parcelable.Creator<CompanyFeature>() {

        @Override
        public CompanyFeature createFromParcel(Parcel source) {
            try {
                return CompanyFeature.fromJson((JsonObject) source.readParcelable(JsonObject.class.getClassLoader()));
            } catch (Exception ex) {
                Log.v(TAG, ex);
                return null;
            }
        }

        @Override
        public CompanyFeature[] newArray(int size) {
            return new CompanyFeature[size];
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
