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

public class EmailTemplates implements Parcelable {
    private static final String TAG = "EmailTemplates";

    @Json(name = "results")
    private EmailTemplate[] _results;

    @Source
    private JsonObject SOURCE;

    public EmailTemplates() {
        SOURCE = new JsonObject();
    }

    public EmailTemplates(JsonObject obj) {
        SOURCE = obj;
    }

    public void setResults(EmailTemplate[] results) throws ParseException {
        _results = results;
        SOURCE.put("results", EmailTemplate.toJsonArray(results));
    }

    public EmailTemplate[] getResults() {
        try {
            if (_results != null)
                return _results;

            if (SOURCE.has("results") && SOURCE.get("results") != null) {
                _results = EmailTemplate.fromJsonArray(SOURCE.getJsonArray("results"));
            }

        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _results;
    }

    public EmailTemplates results(EmailTemplate[] results) throws ParseException {
        _results = results;
        SOURCE.put("results", EmailTemplate.toJsonArray(results), true);
        return this;
    }

    /*-*****************************-*/
    /*-             Json            -*/
    /*-*****************************-*/
    public static JsonArray toJsonArray(EmailTemplates[] array) {
        JsonArray list = new JsonArray();
        for (EmailTemplates item : array) {
            list.add(item.getJson());
        }
        return list;
    }

    public static EmailTemplates[] fromJsonArray(JsonArray array) {
        EmailTemplates[] list = new EmailTemplates[array.size()];
        for (int i = 0; i < array.size(); i++) {
            list[i] = fromJson(array.getJsonObject(i));
        }
        return list;
    }

    public static EmailTemplates fromJson(JsonObject obj) {
        try {
            return new EmailTemplates(obj);
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
    public static final Parcelable.Creator<EmailTemplates> CREATOR = new Parcelable.Creator<EmailTemplates>() {

        @Override
        public EmailTemplates createFromParcel(Parcel source) {
            try {
                return EmailTemplates.fromJson((JsonObject) source.readParcelable(JsonObject.class.getClassLoader()));
            } catch (Exception ex) {
                Log.v(TAG, ex);
                return null;
            }
        }

        @Override
        public EmailTemplates[] newArray(int size) {
            return new EmailTemplates[size];
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
