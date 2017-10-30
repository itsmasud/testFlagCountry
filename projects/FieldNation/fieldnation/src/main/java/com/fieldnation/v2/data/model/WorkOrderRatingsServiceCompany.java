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

public class WorkOrderRatingsServiceCompany implements Parcelable {
    private static final String TAG = "WorkOrderRatingsServiceCompany";

    @Json(name = "overall")
    private WorkOrderRatingsServiceCompanyOverall _overall;

    @Source
    private JsonObject SOURCE;

    public WorkOrderRatingsServiceCompany() {
        SOURCE = new JsonObject();
    }

    public WorkOrderRatingsServiceCompany(JsonObject obj) {
        SOURCE = obj;
    }

    public void setOverall(WorkOrderRatingsServiceCompanyOverall overall) throws ParseException {
        _overall = overall;
        SOURCE.put("overall", overall.getJson());
    }

    public WorkOrderRatingsServiceCompanyOverall getOverall() {
        try {
            if (_overall == null && SOURCE.has("overall") && SOURCE.get("overall") != null)
                _overall = WorkOrderRatingsServiceCompanyOverall.fromJson(SOURCE.getJsonObject("overall"));
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        if (_overall == null)
            _overall = new WorkOrderRatingsServiceCompanyOverall();

            return _overall;
    }

    public WorkOrderRatingsServiceCompany overall(WorkOrderRatingsServiceCompanyOverall overall) throws ParseException {
        _overall = overall;
        SOURCE.put("overall", overall.getJson());
        return this;
    }

    /*-*****************************-*/
    /*-             Json            -*/
    /*-*****************************-*/
    public static JsonArray toJsonArray(WorkOrderRatingsServiceCompany[] array) {
        JsonArray list = new JsonArray();
        for (WorkOrderRatingsServiceCompany item : array) {
            list.add(item.getJson());
        }
        return list;
    }

    public static WorkOrderRatingsServiceCompany[] fromJsonArray(JsonArray array) {
        WorkOrderRatingsServiceCompany[] list = new WorkOrderRatingsServiceCompany[array.size()];
        for (int i = 0; i < array.size(); i++) {
            list[i] = fromJson(array.getJsonObject(i));
        }
        return list;
    }

    public static WorkOrderRatingsServiceCompany fromJson(JsonObject obj) {
        try {
            return new WorkOrderRatingsServiceCompany(obj);
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
    public static final Parcelable.Creator<WorkOrderRatingsServiceCompany> CREATOR = new Parcelable.Creator<WorkOrderRatingsServiceCompany>() {

        @Override
        public WorkOrderRatingsServiceCompany createFromParcel(Parcel source) {
            try {
                return WorkOrderRatingsServiceCompany.fromJson((JsonObject) source.readParcelable(JsonObject.class.getClassLoader()));
            } catch (Exception ex) {
                Log.v(TAG, ex);
                return null;
            }
        }

        @Override
        public WorkOrderRatingsServiceCompany[] newArray(int size) {
            return new WorkOrderRatingsServiceCompany[size];
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
