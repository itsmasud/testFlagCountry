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

public class TaskAlert implements Parcelable {
    private static final String TAG = "TaskAlert";

    @Json(name = "id")
    private Integer _id;

    @Json(name = "sent")
    private String _sent;

    @Json(name = "email")
    private String _email;

    public TaskAlert() {
    }

    public void setId(Integer id) {
        _id = id;
    }

    public Integer getId() {
        return _id;
    }

    public TaskAlert id(Integer id) {
        _id = id;
        return this;
    }

    public void setSent(String sent) {
        _sent = sent;
    }

    public String getSent() {
        return _sent;
    }

    public TaskAlert sent(String sent) {
        _sent = sent;
        return this;
    }

    public void setEmail(String email) {
        _email = email;
    }

    public String getEmail() {
        return _email;
    }

    public TaskAlert email(String email) {
        _email = email;
        return this;
    }

    /*-*****************************-*/
    /*-             Json            -*/
    /*-*****************************-*/
    public static TaskAlert[] fromJsonArray(JsonArray array) {
        TaskAlert[] list = new TaskAlert[array.size()];
        for (int i = 0; i < array.size(); i++) {
            list[i] = fromJson(array.getJsonObject(i));
        }
        return list;
    }

    public static TaskAlert fromJson(JsonObject obj) {
        try {
            return Unserializer.unserializeObject(TaskAlert.class, obj);
        } catch (Exception ex) {
            Log.v(TAG, TAG, ex);
            return null;
        }
    }

    public JsonObject toJson() {
        return toJson(this);
    }

    public static JsonObject toJson(TaskAlert taskAlert) {
        try {
            return Serializer.serializeObject(taskAlert);
        } catch (Exception ex) {
            Log.v(TAG, TAG, ex);
            return null;
        }
    }

    /*-*********************************************-*/
    /*-			Parcelable Implementation           -*/
    /*-*********************************************-*/
    public static final Parcelable.Creator<TaskAlert> CREATOR = new Parcelable.Creator<TaskAlert>() {

        @Override
        public TaskAlert createFromParcel(Parcel source) {
            try {
                return TaskAlert.fromJson((JsonObject) source.readParcelable(JsonObject.class.getClassLoader()));
            } catch (Exception ex) {
                Log.v(TAG, ex);
                return null;
            }
        }

        @Override
        public TaskAlert[] newArray(int size) {
            return new TaskAlert[size];
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
