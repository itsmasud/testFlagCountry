package com.fieldnation.data.workorder;

import android.os.Parcel;
import android.os.Parcelable;

import com.fieldnation.fnjson.JsonObject;
import com.fieldnation.fnjson.Serializer;
import com.fieldnation.fnjson.Unserializer;
import com.fieldnation.fnjson.annotations.Json;
import com.fieldnation.fnlog.Log;

public class Document implements Parcelable {
    private static final String TAG = "Document";

    @Json(name = "documentId")
    private Integer _documentId;
    @Json(name = "fileName")
    private String _fileName;
    @Json(name = "filePath")
    private String _filePath;
    @Json(name = "fileSize")
    private Integer _fileSize;
    @Json(name = "fileType")
    private String _fileType;
    @Json(name = "lastUpdated")
    private String _lastUpdated;
    @Json(name = "thumbNail")
    private String _thumbNail;
    @Json(name = "updatedBy")
    private User _updatedBy;

    public Document() {
    }

    public Integer getDocumentId() {
        return _documentId;
    }

    public String getFileName() {
        return _fileName;
    }

    public String getFilePath() {
        return _filePath;
    }

    public Integer getFileSize() {
        return _fileSize;
    }

    public String getFileType() {
        return _fileType;
    }

    public String getLastUpdated() {
        return _lastUpdated;
    }

    public String getThumbNail() {
        return _thumbNail;
    }

    public User getUpdatedBy() {
        return _updatedBy;
    }

    public JsonObject toJson() {
        return toJson(this);
    }

    public static JsonObject toJson(Document document) {
        try {
            return Serializer.serializeObject(document);
        } catch (Exception ex) {
            Log.v(TAG, ex);
            return null;
        }
    }

    public static Document fromJson(JsonObject json) {
        try {
            return Unserializer.unserializeObject(Document.class, json);
        } catch (Exception ex) {
            Log.v(TAG, ex);
            return null;
        }
    }


    /*-*********************************************-*/
    /*-			Parcelable Implementation			-*/
    /*-*********************************************-*/
    public static final Parcelable.Creator<Document> CREATOR = new Parcelable.Creator<Document>() {

        @Override
        public Document createFromParcel(Parcel source) {
            try {
                return Document.fromJson((JsonObject) source.readParcelable(JsonObject.class.getClassLoader()));
            } catch (Exception ex) {
                Log.v(TAG, ex);
                return null;
            }
        }

        @Override
        public Document[] newArray(int size) {
            return new Document[size];
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
