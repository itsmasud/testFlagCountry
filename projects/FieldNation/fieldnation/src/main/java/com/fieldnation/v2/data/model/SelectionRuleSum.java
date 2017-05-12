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

public class SelectionRuleSum implements Parcelable {
    private static final String TAG = "SelectionRuleSum";

    @Json(name = "match")
    private Integer _match;

    @Json(name = "no_match_optional")
    private Integer _noMatchOptional;

    @Json(name = "no_match_required")
    private Integer _noMatchRequired;

    @Json(name = "total")
    private Integer _total;

    @Source
    private JsonObject SOURCE;

    public SelectionRuleSum() {
        SOURCE = new JsonObject();
    }

    public SelectionRuleSum(JsonObject obj) {
        SOURCE = obj;
    }

    public void setMatch(Integer match) throws ParseException {
        _match = match;
        SOURCE.put("match", match);
    }

    public Integer getMatch() {
        try {
            if (_match == null && SOURCE.has("match") && SOURCE.get("match") != null)
                _match = SOURCE.getInt("match");
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _match;
    }

    public SelectionRuleSum match(Integer match) throws ParseException {
        _match = match;
        SOURCE.put("match", match);
        return this;
    }

    public void setNoMatchOptional(Integer noMatchOptional) throws ParseException {
        _noMatchOptional = noMatchOptional;
        SOURCE.put("no_match_optional", noMatchOptional);
    }

    public Integer getNoMatchOptional() {
        try {
            if (_noMatchOptional == null && SOURCE.has("no_match_optional") && SOURCE.get("no_match_optional") != null)
                _noMatchOptional = SOURCE.getInt("no_match_optional");
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _noMatchOptional;
    }

    public SelectionRuleSum noMatchOptional(Integer noMatchOptional) throws ParseException {
        _noMatchOptional = noMatchOptional;
        SOURCE.put("no_match_optional", noMatchOptional);
        return this;
    }

    public void setNoMatchRequired(Integer noMatchRequired) throws ParseException {
        _noMatchRequired = noMatchRequired;
        SOURCE.put("no_match_required", noMatchRequired);
    }

    public Integer getNoMatchRequired() {
        try {
            if (_noMatchRequired == null && SOURCE.has("no_match_required") && SOURCE.get("no_match_required") != null)
                _noMatchRequired = SOURCE.getInt("no_match_required");
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _noMatchRequired;
    }

    public SelectionRuleSum noMatchRequired(Integer noMatchRequired) throws ParseException {
        _noMatchRequired = noMatchRequired;
        SOURCE.put("no_match_required", noMatchRequired);
        return this;
    }

    public void setTotal(Integer total) throws ParseException {
        _total = total;
        SOURCE.put("total", total);
    }

    public Integer getTotal() {
        try {
            if (_total == null && SOURCE.has("total") && SOURCE.get("total") != null)
                _total = SOURCE.getInt("total");
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _total;
    }

    public SelectionRuleSum total(Integer total) throws ParseException {
        _total = total;
        SOURCE.put("total", total);
        return this;
    }

    /*-*****************************-*/
    /*-             Json            -*/
    /*-*****************************-*/
    public static JsonArray toJsonArray(SelectionRuleSum[] array) {
        JsonArray list = new JsonArray();
        for (SelectionRuleSum item : array) {
            list.add(item.getJson());
        }
        return list;
    }

    public static SelectionRuleSum[] fromJsonArray(JsonArray array) {
        SelectionRuleSum[] list = new SelectionRuleSum[array.size()];
        for (int i = 0; i < array.size(); i++) {
            list[i] = fromJson(array.getJsonObject(i));
        }
        return list;
    }

    public static SelectionRuleSum fromJson(JsonObject obj) {
        try {
            return new SelectionRuleSum(obj);
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
    public static final Parcelable.Creator<SelectionRuleSum> CREATOR = new Parcelable.Creator<SelectionRuleSum>() {

        @Override
        public SelectionRuleSum createFromParcel(Parcel source) {
            try {
                return SelectionRuleSum.fromJson((JsonObject) source.readParcelable(JsonObject.class.getClassLoader()));
            } catch (Exception ex) {
                Log.v(TAG, ex);
                return null;
            }
        }

        @Override
        public SelectionRuleSum[] newArray(int size) {
            return new SelectionRuleSum[size];
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
