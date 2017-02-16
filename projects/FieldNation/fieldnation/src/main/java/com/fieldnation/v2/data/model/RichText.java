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

public class RichText implements Parcelable {
    private static final String TAG = "RichText";

    @Json(name = "html")
    private String _html;

    @Json(name = "markdown")
    private String _markdown;

    @Source
    private JsonObject SOURCE = new JsonObject();

    public RichText() {
    }

    public void setHtml(String html) throws ParseException {
        _html = html;
        SOURCE.put("html", html);
    }

    public String getHtml() {
        return _html;
    }

    public RichText html(String html) throws ParseException {
        _html = html;
        SOURCE.put("html", html);
        return this;
    }

    public void setMarkdown(String markdown) throws ParseException {
        _markdown = markdown;
        SOURCE.put("markdown", markdown);
    }

    public String getMarkdown() {
        return _markdown;
    }

    public RichText markdown(String markdown) throws ParseException {
        _markdown = markdown;
        SOURCE.put("markdown", markdown);
        return this;
    }

    /*-*****************************-*/
    /*-             Json            -*/
    /*-*****************************-*/
    public static JsonArray toJsonArray(RichText[] array) {
        JsonArray list = new JsonArray();
        for (RichText item : array) {
            list.add(item.getJson());
        }
        return list;
    }

    public static RichText[] fromJsonArray(JsonArray array) {
        RichText[] list = new RichText[array.size()];
        for (int i = 0; i < array.size(); i++) {
            list[i] = fromJson(array.getJsonObject(i));
        }
        return list;
    }

    public static RichText fromJson(JsonObject obj) {
        try {
            return Unserializer.unserializeObject(RichText.class, obj);
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
        dest.writeParcelable(getJson(), flags);
    }
}
