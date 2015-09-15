package com.fieldnation.data.workorder;

import android.os.Parcel;
import android.os.Parcelable;

import com.fieldnation.json.JsonObject;
import com.fieldnation.json.Serializer;
import com.fieldnation.json.annotations.Json;

public class UploadingDocument {
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

    public UploadingDocument() {
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






}
