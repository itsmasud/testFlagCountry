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

public class Qualifications implements Parcelable {
    private static final String TAG = "Qualifications";

    @Json(name = "selection_rule")
    private SelectionRule _selectionRule;

    @Json(name = "actions")
    private ActionsEnum[] _actions;

    public Qualifications() {
    }

    public void setSelectionRule(SelectionRule selectionRule) {
        _selectionRule = selectionRule;
    }

    public SelectionRule getSelectionRule() {
        return _selectionRule;
    }

    public Qualifications selectionRule(SelectionRule selectionRule) {
        _selectionRule = selectionRule;
        return this;
    }

    public void setActions(ActionsEnum[] actions) {
        _actions = actions;
    }

    public ActionsEnum[] getActions() {
        return _actions;
    }

    public Qualifications actions(ActionsEnum[] actions) {
        _actions = actions;
        return this;
    }

    /*-******************************-*/
    /*-             Enums            -*/
    /*-******************************-*/
    public enum ActionsEnum {
        @Json(name = "edit")
        EDIT("edit");

        private String value;

        ActionsEnum(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return String.valueOf(value);
        }
    }

    /*-*****************************-*/
    /*-             Json            -*/
    /*-*****************************-*/
    public static Qualifications[] fromJsonArray(JsonArray array) {
        Qualifications[] list = new Qualifications[array.size()];
        for (int i = 0; i < array.size(); i++) {
            list[i] = fromJson(array.getJsonObject(i));
        }
        return list;
    }

    public static Qualifications fromJson(JsonObject obj) {
        try {
            return Unserializer.unserializeObject(Qualifications.class, obj);
        } catch (Exception ex) {
            Log.v(TAG, TAG, ex);
            return null;
        }
    }

    public JsonObject toJson() {
        return toJson(this);
    }

    public static JsonObject toJson(Qualifications qualifications) {
        try {
            return Serializer.serializeObject(qualifications);
        } catch (Exception ex) {
            Log.v(TAG, TAG, ex);
            return null;
        }
    }

    /*-*********************************************-*/
    /*-			Parcelable Implementation           -*/
    /*-*********************************************-*/
    public static final Parcelable.Creator<Qualifications> CREATOR = new Parcelable.Creator<Qualifications>() {

        @Override
        public Qualifications createFromParcel(Parcel source) {
            try {
                return Qualifications.fromJson((JsonObject) source.readParcelable(JsonObject.class.getClassLoader()));
            } catch (Exception ex) {
                Log.v(TAG, ex);
                return null;
            }
        }

        @Override
        public Qualifications[] newArray(int size) {
            return new Qualifications[size];
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
