package com.fieldnation.data.bv2.model;
import android.os.Parcel;
import android.os.Parcelable;

import com.fieldnation.fnjson.JsonObject;
import com.fieldnation.fnjson.Serializer;
import com.fieldnation.fnjson.Unserializer;
import com.fieldnation.fnjson.annotations.Json;
import com.fieldnation.fnlog.Log;

/**
 * Created by dmgen from swagger on 1/30/17.
 */

public class EmailTemplate implements Parcelable {
    private static final String TAG = "EmailTemplate";

    @Json(name = "id")
    private Integer _id;

    @Json(name = "label")
    private String _label;

    public EmailTemplate() {
    }

    public void setId(Integer id) {
        _id = id;
    }

    public Integer getId() {
        return _id;
    }

    public EmailTemplate id(Integer id) {
        _id = id;
        return this;
    }

    public void setLabel(String label) {
        _label = label;
    }

    public String getLabel() {
        return _label;
    }

    public EmailTemplate label(String label) {
        _label = label;
        return this;
    }

    /*-*****************************-*/
    /*-             Json            -*/
    /*-*****************************-*/
    public static EmailTemplate fromJson(JsonObject obj) {
        try {
            return Unserializer.unserializeObject(EmailTemplate.class, obj);
        } catch (Exception ex) {
            Log.v(TAG, TAG, ex);
            return null;
        }
    }

    public JsonObject toJson() {
        return toJson(this);
    }

    public static JsonObject toJson(EmailTemplate emailTemplate) {
        try {
            return Serializer.serializeObject(emailTemplate);
        } catch (Exception ex) {
            Log.v(TAG, TAG, ex);
            return null;
        }
    }

    /*-*********************************************-*/
    /*-			Parcelable Implementation           -*/
    /*-*********************************************-*/
    public static final Parcelable.Creator<EmailTemplate> CREATOR = new Parcelable.Creator<EmailTemplate>() {

        @Override
        public EmailTemplate createFromParcel(Parcel source) {
            try {
                return EmailTemplate.fromJson((JsonObject) source.readParcelable(JsonObject.class.getClassLoader()));
            } catch (Exception ex) {
                Log.v(TAG, ex);
                return null;
            }
        }

        @Override
        public EmailTemplate[] newArray(int size) {
            return new EmailTemplate[size];
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
