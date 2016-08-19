package com.fieldnation.data.v2;

import android.os.Parcel;
import android.os.Parcelable;

import com.fieldnation.fnlog.Log;
import com.fieldnation.fnjson.JsonArray;
import com.fieldnation.fnjson.JsonObject;
import com.fieldnation.fnjson.Serializer;
import com.fieldnation.fnjson.Unserializer;
import com.fieldnation.fnjson.annotations.Json;

/**
 * Created by Michael on 7/21/2016.
 */
public class ListEnvelope implements Parcelable {
    private static final String TAG = "ListEnvelope";

    @Json
    private Integer total;
    @Json
    private Integer page;
    @Json
    private Integer per_page;
    @Json
    private JsonArray results;

    private JsonObject _source = null;

    public ListEnvelope() {
    }

    public ListEnvelope(JsonObject object) {
    }

    public Integer getTotal() {
        return total;
    }

    public Integer getPage() {
        return page;
    }

    public Integer getPerPage() {
        return per_page;
    }

    public JsonArray getResults() {
        return results;
    }

    /*-*************************************-*/
    /*-			JSON Implementation			-*/
    /*-*************************************-*/

    public JsonObject toJson() {
        return toJson(this);
    }

    public static JsonObject toJson(ListEnvelope listEnvelope) {
        if (listEnvelope._source != null)
            return listEnvelope._source;

        try {
            return Serializer.serializeObject(listEnvelope);
        } catch (Exception ex) {
            Log.v(TAG, ex);
            return null;
        }

    }

    public static ListEnvelope fromJson(JsonObject json) {
        try {
            ListEnvelope envelope = Unserializer.unserializeObject(ListEnvelope.class, json);
            envelope._source = json;
            return envelope;
        } catch (Exception ex) {
            Log.v(TAG, ex);
            return null;
        }
    }

    /*-*********************************************-*/
    /*-			Parcelable Implementation			-*/
    /*-*********************************************-*/
    public static final Parcelable.Creator<ListEnvelope> CREATOR = new Parcelable.Creator<ListEnvelope>() {

        @Override
        public ListEnvelope createFromParcel(Parcel source) {
            try {
                return ListEnvelope.fromJson((JsonObject) source.readParcelable(JsonObject.class.getClassLoader()));
            } catch (Exception ex) {
                Log.v(TAG, ex);
                return null;
            }
        }

        @Override
        public ListEnvelope[] newArray(int size) {
            return new ListEnvelope[size];
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
