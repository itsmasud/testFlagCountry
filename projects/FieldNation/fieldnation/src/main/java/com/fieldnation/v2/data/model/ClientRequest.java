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

public class ClientRequest implements Parcelable {
    private static final String TAG = "ClientRequest";

    @Json(name = "id")
    private Integer _id;

    @Json(name = "name")
    private String _name;

    @Json(name = "source")
    private SourceEnum _source;

    @Source
    private JsonObject SOURCE;

    public ClientRequest() {
        SOURCE = new JsonObject();
    }

    public ClientRequest(JsonObject obj) {
        SOURCE = obj;
    }

    public void setId(Integer id) throws ParseException {
        _id = id;
        SOURCE.put("id", id);
    }

    public Integer getId() {
        try {
            if (_id == null && SOURCE.has("id") && SOURCE.get("id") != null)
                _id = SOURCE.getInt("id");
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _id;
    }

    public ClientRequest id(Integer id) throws ParseException {
        _id = id;
        SOURCE.put("id", id);
        return this;
    }

    public void setName(String name) throws ParseException {
        _name = name;
        SOURCE.put("name", name);
    }

    public String getName() {
        try {
            if (_name == null && SOURCE.has("name") && SOURCE.get("name") != null)
                _name = SOURCE.getString("name");
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _name;
    }

    public ClientRequest name(String name) throws ParseException {
        _name = name;
        SOURCE.put("name", name);
        return this;
    }

    public void setSource(SourceEnum source) throws ParseException {
        _source = source;
        SOURCE.put("source", source.toString());
    }

    public SourceEnum getSource() {
        try {
            if (_source == null && SOURCE.has("source") && SOURCE.get("source") != null)
                _source = SourceEnum.fromString(SOURCE.getString("source"));
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _source;
    }

    public ClientRequest source(SourceEnum source) throws ParseException {
        _source = source;
        SOURCE.put("source", source.toString());
        return this;
    }

    /*-******************************-*/
    /*-             Enums            -*/
    /*-******************************-*/
    public enum SourceEnum {
        @Json(name = "email")
        EMAIL("email"),
        @Json(name = "portal")
        PORTAL("portal"),
        @Json(name = "scrape")
        SCRAPE("scrape");

        private String value;

        SourceEnum(String value) {
            this.value = value;
        }

        public static SourceEnum fromString(String value) {
            SourceEnum[] values = values();
            for (SourceEnum v : values) {
                if (v.value.equals(value))
                    return v;
            }
            return null;
        }

        public static SourceEnum[] fromJsonArray(JsonArray jsonArray) {
            SourceEnum[] list = new SourceEnum[jsonArray.size()];
            for (int i = 0; i < list.length; i++) {
                list[i] = fromString(jsonArray.getString(i));
            }
            return list;
        }

        @Override
        public String toString() {
            return String.valueOf(value);
        }
    }

    /*-*****************************-*/
    /*-             Json            -*/
    /*-*****************************-*/
    public static JsonArray toJsonArray(ClientRequest[] array) {
        JsonArray list = new JsonArray();
        for (ClientRequest item : array) {
            list.add(item.getJson());
        }
        return list;
    }

    public static ClientRequest[] fromJsonArray(JsonArray array) {
        ClientRequest[] list = new ClientRequest[array.size()];
        for (int i = 0; i < array.size(); i++) {
            list[i] = fromJson(array.getJsonObject(i));
        }
        return list;
    }

    public static ClientRequest fromJson(JsonObject obj) {
        try {
            return new ClientRequest(obj);
        } catch (Exception ex) {
            Log.v(TAG, ex);
            return null;
        }
    }

    public JsonObject getJson() {
        return SOURCE;
    }

    /*-*********************************************-*/
    /*-			Parcelable Implementation           -*/
    /*-*********************************************-*/
    public static final Parcelable.Creator<ClientRequest> CREATOR = new Parcelable.Creator<ClientRequest>() {

        @Override
        public ClientRequest createFromParcel(Parcel source) {
            try {
                return ClientRequest.fromJson((JsonObject) source.readParcelable(JsonObject.class.getClassLoader()));
            } catch (Exception ex) {
                Log.v(TAG, ex);
                return null;
            }
        }

        @Override
        public ClientRequest[] newArray(int size) {
            return new ClientRequest[size];
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
        return getId() != null && getId() != 0;
    }
}
