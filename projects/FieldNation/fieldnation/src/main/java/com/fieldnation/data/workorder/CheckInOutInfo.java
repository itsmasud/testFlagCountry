package com.fieldnation.data.workorder;

import com.fieldnation.fnlog.Log;
import com.fieldnation.json.JsonObject;
import com.fieldnation.json.Serializer;
import com.fieldnation.json.Unserializer;
import com.fieldnation.json.annotations.Json;

public class CheckInOutInfo {
    private static final String TAG = "CheckInOutInfo";

    @Json(name = "checkInDate")
    private String _checkInDate;
    //	@Json(name="checkInDistance")
//	private Double _checkInDistance;
    @Json(name = "checkInId")
    private Integer _checkInId;
    @Json(name = "checkInTime")
    private String _checkInTime;
    @Json(name = "checkOutDate")
    private String _checkOutDate;
    //	@Json(name="checkOutDistance")
//	private Double _checkOutDistance;
    @Json(name = "checkOutId")
    private Integer _checkOutId;
    @Json(name = "checkOutTime")
    private String _checkOutTime;
    @Json(name = "totalHours")
    private Double _totalHours;

    public CheckInOutInfo() {
    }

    public String getCheckInDate() {
        return _checkInDate;
    }

//	public Double getCheckInDistance(){
//		return _checkInDistance;
//	}

    public Integer getCheckInId() {
        return _checkInId;
    }

    public String getCheckInTime() {
        return _checkInTime;
    }

    public String getCheckOutDate() {
        return _checkOutDate;
    }

//	public Double getCheckOutDistance(){
//		return _checkOutDistance;
//	}

    public Integer getCheckOutId() {
        return _checkOutId;
    }

    public String getCheckOutTime() {
        return _checkOutTime;
    }

    public Double getTotalHours() {
        return _totalHours;
    }

    public JsonObject toJson() {
        return toJson(this);
    }

    public static JsonObject toJson(CheckInOutInfo checkInOutInfo) {
        try {
            return Serializer.serializeObject(checkInOutInfo);
        } catch (Exception ex) {
            Log.v(TAG, ex);
            return null;
        }
    }

    public static CheckInOutInfo fromJson(JsonObject json) {
        try {
            return Unserializer.unserializeObject(CheckInOutInfo.class, json);
        } catch (Exception ex) {
            Log.v(TAG, ex);
            return null;
        }
    }

}
