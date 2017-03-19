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

public class TaskDocumentLink implements Parcelable {
    private static final String TAG = "TaskDocumentLink";

    @Json(name = "expiration")
    private String _expiration;

    @Json(name = "href")
    private String _href;

    @Source
    private JsonObject SOURCE;

    public TaskDocumentLink() {
        SOURCE = new JsonObject();
    }

    public TaskDocumentLink(JsonObject obj) {
        SOURCE = obj;
    }

    public void setExpiration(String expiration) throws ParseException {
        _expiration = expiration;
        SOURCE.put("expiration", expiration);
    }

    public String getExpiration() {
        try {
            if (_expiration == null && SOURCE.has("expiration") && SOURCE.get("expiration") != null)
                _expiration = SOURCE.getString("expiration");
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _expiration;
    }

    public TaskDocumentLink expiration(String expiration) throws ParseException {
        _expiration = expiration;
        SOURCE.put("expiration", expiration);
        return this;
    }

    public void setHref(String href) throws ParseException {
        _href = href;
        SOURCE.put("href", href);
    }

    public String getHref() {
        try {
            if (_href == null && SOURCE.has("href") && SOURCE.get("href") != null)
                _href = SOURCE.getString("href");
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _href;
    }

    public TaskDocumentLink href(String href) throws ParseException {
        _href = href;
        SOURCE.put("href", href);
        return this;
    }

    /*-*****************************-*/
    /*-             Json            -*/
    /*-*****************************-*/
    public static JsonArray toJsonArray(TaskDocumentLink[] array) {
        JsonArray list = new JsonArray();
        for (TaskDocumentLink item : array) {
            list.add(item.getJson());
        }
        return list;
    }

    public static TaskDocumentLink[] fromJsonArray(JsonArray array) {
        TaskDocumentLink[] list = new TaskDocumentLink[array.size()];
        for (int i = 0; i < array.size(); i++) {
            list[i] = fromJson(array.getJsonObject(i));
        }
        return list;
    }

    public static TaskDocumentLink fromJson(JsonObject obj) {
        try {
            return new TaskDocumentLink(obj);
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
    public static final Parcelable.Creator<TaskDocumentLink> CREATOR = new Parcelable.Creator<TaskDocumentLink>() {

        @Override
        public TaskDocumentLink createFromParcel(Parcel source) {
            try {
                return TaskDocumentLink.fromJson((JsonObject) source.readParcelable(JsonObject.class.getClassLoader()));
            } catch (Exception ex) {
                Log.v(TAG, ex);
                return null;
            }
        }

        @Override
        public TaskDocumentLink[] newArray(int size) {
            return new TaskDocumentLink[size];
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
