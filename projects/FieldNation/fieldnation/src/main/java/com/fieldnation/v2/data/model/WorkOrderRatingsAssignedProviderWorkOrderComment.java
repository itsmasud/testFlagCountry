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

public class WorkOrderRatingsAssignedProviderWorkOrderComment implements Parcelable {
    private static final String TAG = "WorkOrderRatingsAssignedProviderWorkOrderComment";

    @Json(name = "message")
    private String _message;

    @Json(name = "private")
    private Boolean _private;

    @Source
    private JsonObject SOURCE;

    public WorkOrderRatingsAssignedProviderWorkOrderComment() {
        SOURCE = new JsonObject();
    }

    public WorkOrderRatingsAssignedProviderWorkOrderComment(JsonObject obj) {
        SOURCE = obj;
    }

    public void setMessage(String message) throws ParseException {
        _message = message;
        SOURCE.put("message", message);
    }

    public String getMessage() {
        try {
            if (_message == null && SOURCE.has("message") && SOURCE.get("message") != null)
                _message = SOURCE.getString("message");
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _message;
    }

    public WorkOrderRatingsAssignedProviderWorkOrderComment message(String message) throws ParseException {
        _message = message;
        SOURCE.put("message", message);
        return this;
    }

    public void setPrivate(Boolean privatee) throws ParseException {
        _private = privatee;
        SOURCE.put("private", privatee);
    }

    public Boolean getPrivate() {
        try {
            if (_private == null && SOURCE.has("private") && SOURCE.get("private") != null)
                _private = SOURCE.getBoolean("private");
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _private;
    }

    public WorkOrderRatingsAssignedProviderWorkOrderComment privatee(Boolean privatee) throws ParseException {
        _private = privatee;
        SOURCE.put("private", privatee);
        return this;
    }

    /*-*****************************-*/
    /*-             Json            -*/
    /*-*****************************-*/
    public static JsonArray toJsonArray(WorkOrderRatingsAssignedProviderWorkOrderComment[] array) {
        JsonArray list = new JsonArray();
        for (WorkOrderRatingsAssignedProviderWorkOrderComment item : array) {
            list.add(item.getJson());
        }
        return list;
    }

    public static WorkOrderRatingsAssignedProviderWorkOrderComment[] fromJsonArray(JsonArray array) {
        WorkOrderRatingsAssignedProviderWorkOrderComment[] list = new WorkOrderRatingsAssignedProviderWorkOrderComment[array.size()];
        for (int i = 0; i < array.size(); i++) {
            list[i] = fromJson(array.getJsonObject(i));
        }
        return list;
    }

    public static WorkOrderRatingsAssignedProviderWorkOrderComment fromJson(JsonObject obj) {
        try {
            return new WorkOrderRatingsAssignedProviderWorkOrderComment(obj);
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
    public static final Parcelable.Creator<WorkOrderRatingsAssignedProviderWorkOrderComment> CREATOR = new Parcelable.Creator<WorkOrderRatingsAssignedProviderWorkOrderComment>() {

        @Override
        public WorkOrderRatingsAssignedProviderWorkOrderComment createFromParcel(Parcel source) {
            try {
                return WorkOrderRatingsAssignedProviderWorkOrderComment.fromJson((JsonObject) source.readParcelable(JsonObject.class.getClassLoader()));
            } catch (Exception ex) {
                Log.v(TAG, ex);
                return null;
            }
        }

        @Override
        public WorkOrderRatingsAssignedProviderWorkOrderComment[] newArray(int size) {
            return new WorkOrderRatingsAssignedProviderWorkOrderComment[size];
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

}
