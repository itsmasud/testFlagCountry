package com.fieldnation.data.workorder;

import android.os.Parcel;
import android.os.Parcelable;

import com.fieldnation.json.JsonObject;
import com.fieldnation.json.Serializer;
import com.fieldnation.json.annotations.Json;

/**
 * Created by michael.carver on 12/9/2014.
 */
public class Signature implements Parcelable {
    @Json(name = "worklog")
    private LoggedWork[] _worklog;
    @Json(name = "signature")
    private String _signature;
    @Json(name = "closingNotes")
    private String _closingNotes;
    @Json(name = "dateSaved")
    private String _dateSaved;
    @Json(name = "signatureFormat")
    private String _signatureFormat;
    @Json(name = "signatureId")
    private long _signatureId;
    @Json(name = "workorderId")
    private int _workorderId;
    @Json(name = "printName")
    private String _printName;

    public Signature() {
    }

    public LoggedWork[] getWorklog() {
        return _worklog;
    }

    public String getSignature() {
        return _signature;
    }

    public String getClosingNotes() {
        return _closingNotes;
    }

    public String getDateSaved() {
        return _dateSaved;
    }

    public String getSignatureFormat() {
        return _signatureFormat;
    }

    public long getSignatureId() {
        return _signatureId;
    }

    public int getWorkorderId() {
        return _workorderId;
    }

    public String getPrintName() {
        return _printName;
    }

    public JsonObject toJson() {
        return toJson(this);
    }

    public static JsonObject toJson(Signature root) {
        try {
            return Serializer.serializeObject(root);
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public static Signature fromJson(JsonObject json) {
        try {
            return Serializer.unserializeObject(Signature.class, json);
        } catch (Exception ex) {
            ex.printStackTrace();
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
                return Signature.fromJson(new JsonObject(source.readString()));
            } catch (Exception ex) {
                ex.printStackTrace();
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
        dest.writeString(toJson().toString());
    }
}
