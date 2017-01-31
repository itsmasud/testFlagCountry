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

public class AttachmentFolder implements Parcelable {
    private static final String TAG = "AttachmentFolder";

    @Json(name = "task")
    private Task _task;

    @Json(name = "name")
    private String _name;

    @Json(name = "id")
    private Integer _id;

    @Json(name = "type")
    private TypeEnum _type;

    public AttachmentFolder() {
    }

    public void setTask(Task task) {
        _task = task;
    }

    public Task getTask() {
        return _task;
    }

    public AttachmentFolder task(Task task) {
        _task = task;
        return this;
    }

    public void setName(String name) {
        _name = name;
    }

    public String getName() {
        return _name;
    }

    public AttachmentFolder name(String name) {
        _name = name;
        return this;
    }

    public void setId(Integer id) {
        _id = id;
    }

    public Integer getId() {
        return _id;
    }

    public AttachmentFolder id(Integer id) {
        _id = id;
        return this;
    }

    public void setType(TypeEnum type) {
        _type = type;
    }

    public TypeEnum getType() {
        return _type;
    }

    public AttachmentFolder type(TypeEnum type) {
        _type = type;
        return this;
    }

    /*-*****************************-*/
    /*-             Json            -*/
    /*-*****************************-*/
    public static AttachmentFolder[] fromJsonArray(JsonArray array) {
        AttachmentFolder[] list = new AttachmentFolder[array.size()];
        for (int i = 0; i < array.size(); i++) {
            list[i] = fromJson(array.getJsonObject(i));
        }
        return list;
    }

    public static AttachmentFolder fromJson(JsonObject obj) {
        try {
            return Unserializer.unserializeObject(AttachmentFolder.class, obj);
        } catch (Exception ex) {
            Log.v(TAG, TAG, ex);
            return null;
        }
    }

    public JsonObject toJson() {
        return toJson(this);
    }

    public static JsonObject toJson(AttachmentFolder attachmentFolder) {
        try {
            return Serializer.serializeObject(attachmentFolder);
        } catch (Exception ex) {
            Log.v(TAG, TAG, ex);
            return null;
        }
    }

    /*-*********************************************-*/
    /*-			Parcelable Implementation           -*/
    /*-*********************************************-*/
    public static final Parcelable.Creator<AttachmentFolder> CREATOR = new Parcelable.Creator<AttachmentFolder>() {

        @Override
        public AttachmentFolder createFromParcel(Parcel source) {
            try {
                return AttachmentFolder.fromJson((JsonObject) source.readParcelable(JsonObject.class.getClassLoader()));
            } catch (Exception ex) {
                Log.v(TAG, ex);
                return null;
            }
        }

        @Override
        public AttachmentFolder[] newArray(int size) {
            return new AttachmentFolder[size];
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
