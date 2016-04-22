package com.fieldnation.data.workorder;

import android.os.Parcel;
import android.os.Parcelable;

import com.fieldnation.Log;
import com.fieldnation.json.JsonObject;
import com.fieldnation.json.Serializer;
import com.fieldnation.json.annotations.Json;

/**
 * Created by Michael on 4/22/2016.
 */
public class ApprovalGrade implements Parcelable {
    private static final String TAG = "ApprovalGrade";

    @Json(name = "reviewPeriodDays")
    private Integer _reviewPeriodDays = 0;

    public ApprovalGrade() {
    }

    public int getReviewPeriodDays() {
        return _reviewPeriodDays == null ? 0 : _reviewPeriodDays;
    }

    public JsonObject toJson() {
        return toJson(this);
    }

    public static JsonObject toJson(ApprovalGrade approvalGrade) {
        try {
            return Serializer.serializeObject(approvalGrade);
        } catch (Exception ex) {
            Log.v(TAG, ex);
            return null;
        }
    }

    public static ApprovalGrade fromJson(JsonObject json) {
        try {
            return Serializer.unserializeObject(ApprovalGrade.class, json);
        } catch (Exception ex) {
            Log.v(TAG, ex);
            return null;
        }
    }


    /*-*********************************************-*/
    /*-			Parcelable Implementation			-*/
    /*-*********************************************-*/
    public static final Parcelable.Creator<ApprovalGrade> CREATOR = new Parcelable.Creator<ApprovalGrade>() {

        @Override
        public ApprovalGrade createFromParcel(Parcel source) {
            try {
                return ApprovalGrade.fromJson((JsonObject) source.readParcelable(JsonObject.class.getClassLoader()));
            } catch (Exception ex) {
                Log.v(TAG, ex);
                return null;
            }
        }

        @Override
        public ApprovalGrade[] newArray(int size) {
            return new ApprovalGrade[size];
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
