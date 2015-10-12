package com.fieldnation.data.workorder;

import android.os.Parcel;
import android.os.Parcelable;

import com.fieldnation.json.JsonObject;
import com.fieldnation.json.Serializer;
import com.fieldnation.json.annotations.Json;

public class UploadingDocument {
    private Integer _documentId;
    private String _fileName;
    private String _filePath;
    private Integer _fileSize;
    private String _fileType;

    public UploadingDocument() {
    }

    public UploadingDocument(String fileName) {
        _fileName = fileName;
    }

    public UploadingDocument(String fileName, String filePath) {
        _fileName = fileName;
        _filePath = filePath;
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
