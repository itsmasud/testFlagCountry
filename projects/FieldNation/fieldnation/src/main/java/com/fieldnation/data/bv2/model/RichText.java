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

public class RichText implements Parcelable {
    private static final String TAG = "RichText";

    @Json(name = "markdown")
    private String _markdown;

    @Json(name = "html")
    private String _html;

    public RichText() {
    }

    public void setMarkdown(String markdown) {
        _markdown = markdown;
    }

    public String getMarkdown() {
        return _markdown;
    }

    public RichText markdown(String markdown) {
        _markdown = markdown;
        return this;
    }

    public void setHtml(String html) {
        _html = html;
    }

    public String getHtml() {
        return _html;
    }

    public RichText html(String html) {
        _html = html;
        return this;
    }

    /*-*****************************-*/
    /*-             Json            -*/
    /*-*****************************-*/
    public static RichText fromJson(JsonObject obj) {
        try {
            return Unserializer.unserializeObject(RichText.class, obj);
        } catch (Exception ex) {
            Log.v(TAG, TAG, ex);
            return null;
        }
    }

    public JsonObject toJson() {
        return toJson(this);
    }

    public static JsonObject toJson(RichText richText) {
        try {
            return Serializer.serializeObject(richText);
        } catch (Exception ex) {
            Log.v(TAG, TAG, ex);
            return null;
        }
    }

    /*-*********************************************-*/
    /*-			Parcelable Implementation           -*/
    /*-*********************************************-*/
    public static final Parcelable.Creator<RichText> CREATOR = new Parcelable.Creator<RichText>() {

        @Override
        public RichText createFromParcel(Parcel source) {
            try {
                return RichText.fromJson((JsonObject) source.readParcelable(JsonObject.class.getClassLoader()));
            } catch (Exception ex) {
                Log.v(TAG, ex);
                return null;
            }
        }

        @Override
        public RichText[] newArray(int size) {
            return new RichText[size];
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
