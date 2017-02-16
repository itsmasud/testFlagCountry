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

import java.text.ParseException;

/**
 * Created by dmgen from swagger.
 */

public class Qualifications implements Parcelable {
    private static final String TAG = "Qualifications";

    @Json(name = "actions")
    private ActionsEnum[] _actions;

    @Json(name = "selection_rule")
    private SelectionRule _selectionRule;

    @Source
    private JsonObject SOURCE = new JsonObject();

    public Qualifications() {
    }

    public void setActions(ActionsEnum[] actions) throws ParseException {
        _actions = actions;
        JsonArray ja = new JsonArray();
        for (ActionsEnum item : actions) {
            ja.add(item.toString());
        }
        SOURCE.put("actions", ja);
    }

    public ActionsEnum[] getActions() {
        return _actions;
    }

    public Qualifications actions(ActionsEnum[] actions) throws ParseException {
        _actions = actions;
        JsonArray ja = new JsonArray();
        for (ActionsEnum item : actions) {
            ja.add(item.toString());
        }
        SOURCE.put("actions", ja, true);
        return this;
    }

    public void setSelectionRule(SelectionRule selectionRule) throws ParseException {
        _selectionRule = selectionRule;
        SOURCE.put("selection_rule", selectionRule.getJson());
    }

    public SelectionRule getSelectionRule() {
        return _selectionRule;
    }

    public Qualifications selectionRule(SelectionRule selectionRule) throws ParseException {
        _selectionRule = selectionRule;
        SOURCE.put("selection_rule", selectionRule.getJson());
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
    public static JsonArray toJsonArray(Qualifications[] array) {
        JsonArray list = new JsonArray();
        for (Qualifications item : array) {
            list.add(item.getJson());
        }
        return list;
    }

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

    public JsonObject getJson() {
        return SOURCE;
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
        dest.writeParcelable(getJson(), flags);
    }
}
