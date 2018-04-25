package com.fieldnation.v2.data.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.fieldnation.fnjson.JsonArray;
import com.fieldnation.fnjson.JsonObject;
import com.fieldnation.fnjson.annotations.Json;
import com.fieldnation.fnjson.annotations.Source;
import com.fieldnation.fnlog.Log;

import java.text.ParseException;

public class AssignmentCancelReason implements Parcelable {
    private static final String TAG = "AssignmentCancelReason";

    @Json(name = "event_reason_id")
    private Integer _eventReasonId;

    @Json(name = "event_reason_category_id")
    private Integer _eventReasonCategoryId;

    @Json(name = "reason_text_provider")
    private String _reasonTextProvider;

    @Json(name = "order")
    private Integer _order;

    @Source
    private JsonObject SOURCE;

    public AssignmentCancelReason() {
        SOURCE = new JsonObject();
    }

    public AssignmentCancelReason(JsonObject obj) {
        SOURCE = obj;
    }

    public void setEventReasonId(Integer eventReasonId) throws ParseException {
        _eventReasonId = eventReasonId;
        SOURCE.put("event_reason_id", eventReasonId);
    }

    public Integer getEventReasonId() {
        try {
            if (_eventReasonId == null && SOURCE.has("event_reason_id") && SOURCE.get("event_reason_id") != null)
                _eventReasonId = SOURCE.getInt("event_reason_id");
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _eventReasonId;
    }

    public AssignmentCancelReason eventReasonId(Integer eventReasonId) throws ParseException {
        _eventReasonId = eventReasonId;
        SOURCE.put("event_reason_id", eventReasonId);
        return this;
    }

    public void setEventReasonCategoryId(Integer eventReasonCategoryId) throws ParseException {
        _eventReasonCategoryId = eventReasonCategoryId;
        SOURCE.put("event_reason_category_id", eventReasonCategoryId);
    }

    public Integer getEventReasonCategoryId() {
        try {
            if (_eventReasonCategoryId == null && SOURCE.has("event_reason_category_id") && SOURCE.get("event_reason_category_id") != null)
                _eventReasonCategoryId = SOURCE.getInt("event_reason_category_id");
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _eventReasonCategoryId;
    }

    public AssignmentCancelReason eventReasonCategoryId(Integer eventReasonCategoryId) throws ParseException {
        _eventReasonCategoryId = eventReasonCategoryId;
        SOURCE.put("event_reason_category_id", eventReasonCategoryId);
        return this;
    }

    public void setReasonTextProvider(String reasonTextProvider) throws ParseException {
        _reasonTextProvider = reasonTextProvider;
        SOURCE.put("reason_text_provider", reasonTextProvider);
    }

    public String getReasonTextProvider() {
        try {
            if (_reasonTextProvider == null && SOURCE.has("reason_text_provider") && SOURCE.get("reason_text_provider") != null)
                _reasonTextProvider = SOURCE.getString("reason_text_provider");
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _reasonTextProvider;
    }

    public AssignmentCancelReason reasonTextProvider(String reasonTextProvider) throws ParseException {
        _reasonTextProvider = reasonTextProvider;
        SOURCE.put("reason_text_provider", reasonTextProvider);
        return this;
    }

    public void setOrder(Integer order) throws ParseException {
        _order = order;
        SOURCE.put("order", order);
    }

    public Integer getOrder() {
        try {
            if (_order == null && SOURCE.has("order") && SOURCE.get("order") != null)
                _order = SOURCE.getInt("order");
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _order;
    }

    public AssignmentCancelReason order(Integer order) throws ParseException {
        _order = order;
        SOURCE.put("order", order);
        return this;
    }

    @Override
    public String toString() {
        return getReasonTextProvider();
    }

    /*-*****************************-*/
    /*-             Json            -*/
    /*-*****************************-*/
    public static JsonArray toJsonArray(AssignmentCancelReason[] array) {
        JsonArray list = new JsonArray();
        for (AssignmentCancelReason item : array) {
            list.add(item.getJson());
        }
        return list;
    }

    public static AssignmentCancelReason[] fromJsonArray(JsonArray array) {
        AssignmentCancelReason[] list = new AssignmentCancelReason[array.size()];
        for (int i = 0; i < array.size(); i++) {
            list[i] = fromJson(array.getJsonObject(i));
        }
        return list;
    }

    public static AssignmentCancelReason fromJson(JsonObject obj) {
        try {
            return new AssignmentCancelReason(obj);
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
    public static final Parcelable.Creator<AssignmentCancelReason> CREATOR = new Parcelable.Creator<AssignmentCancelReason>() {

        @Override
        public AssignmentCancelReason createFromParcel(Parcel source) {
            try {
                return AssignmentCancelReason.fromJson((JsonObject) source.readParcelable(JsonObject.class.getClassLoader()));
            } catch (Exception ex) {
                Log.v(TAG, ex);
                return null;
            }
        }

        @Override
        public AssignmentCancelReason[] newArray(int size) {
            return new AssignmentCancelReason[size];
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

}
