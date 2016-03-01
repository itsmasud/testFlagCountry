package com.fieldnation.data.workorder;

import android.os.Parcel;
import android.os.Parcelable;

import com.fieldnation.Log;
import com.fieldnation.json.JsonObject;
import com.fieldnation.json.Serializer;
import com.fieldnation.json.annotations.Json;

public class UploadSlot implements Parcelable {
    private static final String TAG = "UploadSlot";

    @Json(name = "maxFileSize")
    private String _maxFileSize;
    @Json(name = "maxFiles")
    private Integer _maxFiles;
    //	@Json(name="minFileSize")
//	private Object _minFileSize;
    @Json(name = "minFiles")
    private Integer _minFiles;
    @Json(name = "slotId")
    private Integer _slotId;
    @Json(name = "slotName")
    private String _slotName;
    @Json(name = "task")
    private Task _task;
    @Json(name = "uploadedDocuments")
    private UploadedDocument[] _uploadedDocuments;

    public UploadSlot() {
    }

    public String getMaxFileSize() {
        return _maxFileSize;
    }

    public Integer getMaxFiles() {
        return _maxFiles;
    }

//	public Object getMinFileSize(){
//		return _minFileSize;
//	}

    public Integer getMinFiles() {
        return _minFiles;
    }

    public Integer getSlotId() {
        return _slotId;
    }

    public String getSlotName() {
        return _slotName;
    }

    public Task getTask() {
        return _task;
    }

    public UploadedDocument[] getUploadedDocuments() {
        return _uploadedDocuments;
    }

    public JsonObject toJson() {
        return toJson(this);
    }

    public static JsonObject toJson(UploadSlot uploadSlot) {
        try {
            return Serializer.serializeObject(uploadSlot);
        } catch (Exception ex) {
            Log.v(TAG, ex);
            return null;
        }
    }

    public static UploadSlot fromJson(JsonObject json) {
        try {
            return Serializer.unserializeObject(UploadSlot.class, json);
        } catch (Exception ex) {
            Log.v(TAG, ex);
            return null;
        }
    }


    /*-*********************************************-*/
    /*-			Parcelable Implementation			-*/
    /*-*********************************************-*/
    public static final Parcelable.Creator<UploadSlot> CREATOR = new Parcelable.Creator<UploadSlot>() {

        @Override
        public UploadSlot createFromParcel(Parcel source) {
            try {
                return UploadSlot.fromJson((JsonObject) source.readParcelable(JsonObject.class.getClassLoader()));
            } catch (Exception ex) {
                Log.v(TAG, ex);
                return null;
            }
        }

        @Override
        public UploadSlot[] newArray(int size) {
            return new UploadSlot[size];
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
