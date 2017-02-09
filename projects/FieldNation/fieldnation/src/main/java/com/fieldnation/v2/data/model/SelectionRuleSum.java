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

    public SelectionRuleSum() {
    }

    public void setMatch(Integer match) {
        _match = match;
    }

    public Integer getMatch() {
        return _match;
    }

    public SelectionRuleSum match(Integer match) {
        _match = match;
        return this;
    }

    public void setNoMatchOptional(Integer noMatchOptional) {
        _noMatchOptional = noMatchOptional;
    }

    public Integer getNoMatchOptional() {
        return _noMatchOptional;
    }

    public SelectionRuleSum noMatchOptional(Integer noMatchOptional) {
        _noMatchOptional = noMatchOptional;
        return this;
    }

    public void setNoMatchRequired(Integer noMatchRequired) {
        _noMatchRequired = noMatchRequired;
    }

    public Integer getNoMatchRequired() {
        return _noMatchRequired;
    }

    public SelectionRuleSum noMatchRequired(Integer noMatchRequired) {
        _noMatchRequired = noMatchRequired;
        return this;
    }

    public void setTotal(Integer total) {
        _total = total;
    }

    public Integer getTotal() {
        return _total;
    }

    public SelectionRuleSum total(Integer total) {
        _total = total;
        return this;
    }

    /*-*****************************-*/
    /*-             Json            -*/
    /*-*****************************-*/
    public static SelectionRuleSum[] fromJsonArray(JsonArray array) {
        SelectionRuleSum[] list = new SelectionRuleSum[array.size()];
        for (int i = 0; i < array.size(); i++) {
            list[i] = fromJson(array.getJsonObject(i));
        }
        return list;
    }

    public static SelectionRuleSum fromJson(JsonObject obj) {
        try {
            return Unserializer.unserializeObject(SelectionRuleSum.class, obj);
        } catch (Exception ex) {
            Log.v(TAG, TAG, ex);
            return null;
        }
    }

    public JsonObject toJson() {
        return toJson(this);
    }

    public static JsonObject toJson(SelectionRuleSum selectionRuleSum) {
        try {
            return Serializer.serializeObject(selectionRuleSum);
        } catch (Exception ex) {
            Log.v(TAG, TAG, ex);
            return null;
        }
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
        dest.writeParcelable(toJson(), flags);
    }
}
