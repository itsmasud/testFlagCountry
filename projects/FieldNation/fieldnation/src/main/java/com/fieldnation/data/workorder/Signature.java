package com.fieldnation.data.workorder;

import android.os.Parcel;
import android.os.Parcelable;

import com.fieldnation.fnlog.Log;
import com.fieldnation.json.JsonObject;
import com.fieldnation.json.Serializer;
import com.fieldnation.json.Unserializer;
import com.fieldnation.json.annotations.Json;

/**
 * Created by michael.carver on 12/9/2014.
 */
public class Signature implements Parcelable {
    private static final String TAG = "Signature";

    @Json(name = "closingNotes")
    private String _closingNotes;
    @Json(name = "dateSaved")
    private String _dateSaved;
    @Json(name = "formattedTime")
    private String _formattedTime;
    @Json(name = "printName")
    private String _printName;
    @Json(name = "signature")
    private String _signature;
    @Json(name = "signatureFormat")
    private String _signatureFormat;
    @Json(name = "signatureId")
    private long _signatureId;
    @Json(name = "worklog")
    private LoggedWork[] _worklog;
    @Json(name = "workorderId")
    private int _workorderId;
    @Json(name = "workorderTaskId")
    private Integer _workorderTaskId;

    public Signature() {
    }

    public String getClosingNotes() {
        return _closingNotes;
    }

    public String getDateSaved() {
        return _dateSaved;
    }

    public String getFormattedTime() {
        return _formattedTime;
    }

    public String getPrintName() {
        return _printName;
    }

    public String getSignature() {
        return _signature;
    }

    public String getSignatureFormat() {
        return _signatureFormat;
    }

    public long getSignatureId() {
        return _signatureId;
    }

    public LoggedWork[] getWorklog() {
        return _worklog;
    }

    public int getWorkorderId() {
        return _workorderId;
    }

    public Integer getWorkorderTaskId() {
        return _workorderTaskId;
    }


    public JsonObject toJson() {
        return toJson(this);
    }

    public static JsonObject toJson(Signature root) {
        try {
            return Serializer.serializeObject(root);
        } catch (Exception ex) {
            Log.v(TAG, ex);
            return null;
        }
    }

    public static Signature fromJson(JsonObject json) {
        try {
            return Unserializer.unserializeObject(Signature.class, json);
        } catch (Exception ex) {
            Log.v(TAG, ex);
            return null;
        }
    }

    /*-*************************************-*/
    /*-             Human Code              -*/
    /*-*************************************-*/

    public boolean isJsonSignature() {
        return "json".equals(_signatureFormat);
    }

    public boolean isSvgSignature() {
        return "svg".equals(_signatureFormat);
    }

    public static final Parcelable.Creator<Signature> CREATOR = new Creator<Signature>() {
        @Override
        public Signature createFromParcel(Parcel source) {
            try {
                return Signature.fromJson((JsonObject) (source.readParcelable(JsonObject.class.getClassLoader())));
            } catch (Exception ex) {
                Log.v(TAG, ex);
            }
            return null;
        }

        @Override
        public Signature[] newArray(int size) {
            return new Signature[size];
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
