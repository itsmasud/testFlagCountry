package com.fieldnation.data.bv2.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.fieldnation.fnjson.JsonObject;
import com.fieldnation.fnjson.Serializer;
import com.fieldnation.fnjson.Unserializer;
import com.fieldnation.fnjson.annotations.Json;
import com.fieldnation.fnlog.Log;

/**
 * Created by dmgen from swagger on 1/26/17.
 */

public class EmailTemplates implements Parcelable {
    private static final String TAG = "EmailTemplates";

    @Json(name = "results")
    private EmailTemplate[] _results;

    public EmailTemplates() {
    }

    public void setResults(EmailTemplate[] results) {
        _results = results;
    }

    public EmailTemplate[] getResults() {
        return _results;
    }

    public EmailTemplates results(EmailTemplate[] results) {
        _results = results;
        return this;
    }

    /*-*****************************-*/
    /*-             Json            -*/
    /*-*****************************-*/
    public static EmailTemplates fromJson(JsonObject obj) {
        try {
            return Unserializer.unserializeObject(EmailTemplates.class, obj);
        } catch (Exception ex) {
            Log.v(TAG, TAG, ex);
            return null;
        }
    }

    public JsonObject toJson() {
        return toJson(this);
    }

    public static JsonObject toJson(EmailTemplates emailTemplates) {
        try {
            return Serializer.serializeObject(emailTemplates);
        } catch (Exception ex) {
            Log.v(TAG, TAG, ex);
            return null;
        }
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
        dest.writeParcelable(toJson(), flags);
    }
}
