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

public class ScheduleEtaStatus implements Parcelable {
    private static final String TAG = "ScheduleEtaStatus";

    @Json(name = "name")
    private NameEnum _name;

    @Json(name = "updated")
    private Date _updated;

    @Source
    private JsonObject SOURCE = new JsonObject();

    public ScheduleEtaStatus() {
    }

    public void setName(NameEnum name) throws ParseException {
        _name = name;
        SOURCE.put("name", name.toString());
    }

    public NameEnum getName() {
        return _name;
    }

    public ScheduleEtaStatus name(NameEnum name) throws ParseException {
        _name = name;
        SOURCE.put("name", name.toString());
        return this;
    }

    public void setUpdated(Date updated) throws ParseException {
        _updated = updated;
        SOURCE.put("updated", updated.getJson());
    }

    public Date getUpdated() {
        return _updated;
    }

    public ScheduleEtaStatus updated(Date updated) throws ParseException {
        _updated = updated;
        SOURCE.put("updated", updated.getJson());
        return this;
    }

    /*-******************************-*/
    /*-             Enums            -*/
    /*-******************************-*/
    public enum NameEnum {
        @Json(name = "confirmed")
        CONFIRMED("confirmed"),
        @Json(name = "onmyway")
        ONMYWAY("onmyway"),
        @Json(name = "readytogo")
        READYTOGO("readytogo"),
        @Json(name = "unconfirmed")
        UNCONFIRMED("unconfirmed");

        private String value;

        NameEnum(String value) {
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
    public static JsonArray toJsonArray(ScheduleEtaStatus[] array) {
        JsonArray list = new JsonArray();
        for (ScheduleEtaStatus item : array) {
            list.add(item.getJson());
        }
        return list;
    }

    public static ScheduleEtaStatus[] fromJsonArray(JsonArray array) {
        ScheduleEtaStatus[] list = new ScheduleEtaStatus[array.size()];
        for (int i = 0; i < array.size(); i++) {
            list[i] = fromJson(array.getJsonObject(i));
        }
        return list;
    }

    public static ScheduleEtaStatus fromJson(JsonObject obj) {
        try {
            return Unserializer.unserializeObject(ScheduleEtaStatus.class, obj);
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
    public static final Parcelable.Creator<ScheduleEtaStatus> CREATOR = new Parcelable.Creator<ScheduleEtaStatus>() {

        @Override
        public ScheduleEtaStatus createFromParcel(Parcel source) {
            try {
                return ScheduleEtaStatus.fromJson((JsonObject) source.readParcelable(JsonObject.class.getClassLoader()));
            } catch (Exception ex) {
                Log.v(TAG, ex);
                return null;
            }
        }

        @Override
        public ScheduleEtaStatus[] newArray(int size) {
            return new ScheduleEtaStatus[size];
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
