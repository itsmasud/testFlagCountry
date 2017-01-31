package com.fieldnation.data.bv2.model;
import android.os.Parcel;
import android.os.Parcelable;

import com.fieldnation.fnjson.JsonArray;
import com.fieldnation.fnjson.JsonObject;
import com.fieldnation.fnjson.Serializer;
import com.fieldnation.fnjson.Unserializer;
import com.fieldnation.fnjson.annotations.Json;
import com.fieldnation.fnlog.Log;

/**
 * Created by dmgen from swagger on 1/31/17.
 */

public class AttachmentFolders implements Parcelable {
    private static final String TAG = "AttachmentFolders";

    @Json(name = "metadata")
    private ListEnvelope _metadata;

    @Json(name = "results")
    private AttachmentFolder[] _results;

    @Json(name = "actions")
    private String[] _actions;

    public AttachmentFolders() {
    }

    public void setMetadata(ListEnvelope metadata) {
        _metadata = metadata;
    }

    public ListEnvelope getMetadata() {
        return _metadata;
    }

    public AttachmentFolders metadata(ListEnvelope metadata) {
        _metadata = metadata;
        return this;
    }

    public void setResults(AttachmentFolder[] results) {
        _results = results;
    }

    public AttachmentFolder[] getResults() {
        return _results;
    }

    public AttachmentFolders results(AttachmentFolder[] results) {
        _results = results;
        return this;
    }

    public void setActions(String[] actions) {
        _actions = actions;
    }

    public String[] getActions() {
        return _actions;
    }

    public AttachmentFolders actions(String[] actions) {
        _actions = actions;
        return this;
    }

    /*-*****************************-*/
    /*-             Json            -*/
    /*-*****************************-*/
    public static AttachmentFolders[] fromJsonArray(JsonArray array) {
        AttachmentFolders[] list = new AttachmentFolders[array.size()];
        for (int i = 0; i < array.size(); i++) {
            list[i] = fromJson(array.getJsonObject(i));
        }
        return list;
    }

    public static AttachmentFolders fromJson(JsonObject obj) {
        try {
            return Unserializer.unserializeObject(AttachmentFolders.class, obj);
        } catch (Exception ex) {
            Log.v(TAG, TAG, ex);
            return null;
        }
    }

    public JsonObject toJson() {
        return toJson(this);
    }

    public static JsonObject toJson(AttachmentFolders attachmentFolders) {
        try {
            return Serializer.serializeObject(attachmentFolders);
        } catch (Exception ex) {
            Log.v(TAG, TAG, ex);
            return null;
        }
    }

    /*-*********************************************-*/
    /*-			Parcelable Implementation           -*/
    /*-*********************************************-*/
    public static final Parcelable.Creator<AttachmentFolders> CREATOR = new Parcelable.Creator<AttachmentFolders>() {

        @Override
        public AttachmentFolders createFromParcel(Parcel source) {
            try {
                return AttachmentFolders.fromJson((JsonObject) source.readParcelable(JsonObject.class.getClassLoader()));
            } catch (Exception ex) {
                Log.v(TAG, ex);
                return null;
            }
        }

        @Override
        public AttachmentFolders[] newArray(int size) {
            return new AttachmentFolders[size];
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
