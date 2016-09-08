package com.fieldnation.data.workorder;

import android.os.Parcel;
import android.os.Parcelable;

import com.fieldnation.fnlog.Log;
import com.fieldnation.fnjson.JsonObject;
import com.fieldnation.fnjson.Serializer;
import com.fieldnation.fnjson.Unserializer;
import com.fieldnation.fnjson.annotations.Json;
import com.fieldnation.fntools.DateUtils;
import com.fieldnation.fntools.ISO8601;
import com.fieldnation.fntools.misc;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;

public class Schedule implements Parcelable {
    private static final String TAG = "Schedule";

    @Json(name = "duration")
    private Double _duration;
    @Json(name = "endTime")
    private String _endTime;
    @Json(name = "startTime")
    private String _startTime;
//    @Json(name = "startTimeHours") // Don't use this because it will give you wrong time
//    private String _startTimeHours;
    @Json(name = "workorderScheduleId")
    private Integer _workorderScheduleId;
    @Json(name = "type")
    private Integer _type;
    @Json(name = "note")
    private String _note;



    public Schedule() {
    }

    public Double getDuration() {
        return _duration;
    }

    public String getEndTime() {
        return _endTime;
    }

    public String getStartTime() {
        return _startTime;
    }

    public Integer getWorkorderScheduleId() {
        return _workorderScheduleId;
    }

    public String getNote() {
        return _note;
    }

    public JsonObject toJson() {
        return toJson(this);
    }

    public static JsonObject toJson(Schedule schedule) {
        try {
            return Serializer.serializeObject(schedule);
        } catch (Exception ex) {
            Log.v(TAG, ex);
            return null;
        }
    }

    public static Schedule fromJson(JsonObject json) {
        try {
            return Unserializer.unserializeObject(Schedule.class, json);
        } catch (Exception ex) {
            Log.v(TAG, ex);
            return null;
        }
    }

    /*-*************************************************-*/
    /*-				Human Generated Code				-*/
    /*-*************************************************-*/
    public Schedule(String startTime) {
        _startTime = startTime;
        _endTime = null;
    }

    public Schedule(String startTime, String endTime) {
        _startTime = startTime;
        _endTime = endTime;
    }

    void setType(Integer type) {
        if (type == null)
            return;

        _type = type;
    }

    public Type getType() {
        if (_type == null) {
            if (isExact())
                return Type.EXACT;
            return Type.OPEN_RAGE;
        }

        return Type.values()[_type - 1];
    }

    public enum Type {
        EXACT, BUSINESS_HOURS, OPEN_RAGE
    }

    public String getFormatedDate() {
        try {
            if (!misc.isEmptyOrNull(getStartTime())) {
                String when = "";
                Calendar cal = null;
                cal = ISO8601.toCalendar(getStartTime());
                when = DateUtils.formatDateReallyLong(cal);

                // Wednesday, Dec 4, 2056
                if (!misc.isEmptyOrNull(getEndTime())) {
                    Calendar ecal = ISO8601.toCalendar(getEndTime());
                    if (ecal.get(Calendar.YEAR) > 2000
                            && (ecal.get(Calendar.DAY_OF_YEAR) != cal.get(Calendar.DAY_OF_YEAR))) {
                        when += "\n";
                        when += DateUtils.formatDateReallyLong(ecal);
                    }
                }
                return when;
            } else {
                return null;
            }
        } catch (Exception ex) {
            Log.v(TAG, ex);
            return null;
        }
    }

    public String getFormatedTime() {
        try {
            if (!misc.isEmptyOrNull(getStartTime())) {
                String when = "";
                Calendar cal = null;
                cal = ISO8601.toCalendar(getStartTime());
                when = DateUtils.formatTime(cal, false);

                if (!misc.isEmptyOrNull(getEndTime())) {
                    cal = ISO8601.toCalendar(getEndTime());
                    if (cal.get(Calendar.YEAR) > 2000) {
                        when += " - ";
                        when += DateUtils.formatTime(cal, false);
                    }
                }
                return when;
            } else {
                return null;
            }
        } catch (Exception ex) {
            Log.v(TAG, ex);
            return null;
        }
    }

    // Todo, localize this
    public String getFormatedStartTime() {
        try {
            if (!misc.isEmptyOrNull(getStartTime())) {
                String when = "";
                Calendar cal = null;
                Calendar ecal = null;
                cal = ISO8601.toCalendar(getStartTime());
                when = DateUtils.formatDate(cal);

                if (!misc.isEmptyOrNull(getEndTime())) {
                    ecal = ISO8601.toCalendar(getEndTime());
                    if (ecal.get(Calendar.YEAR) > 2000
                            && (ecal.get(Calendar.DAY_OF_YEAR) != cal.get(Calendar.DAY_OF_YEAR))) {
                        when += " - ";
                        when += DateUtils.formatDate(ecal);
                    }
                }
                when += " @ ";

                when += DateUtils.formatTime(cal, false);

                if (ecal != null
                        && ((ecal.get(Calendar.HOUR_OF_DAY) != cal.get(Calendar.HOUR_OF_DAY))
                        || (ecal.get(Calendar.MINUTE) != cal.get(Calendar.MINUTE)))) {
                    when += " - ";
                    when += DateUtils.formatTime(ecal, false);
                }

                return when;
            } else {
                return null;
            }
        } catch (Exception ex) {
            Log.v(TAG, ex);
            return null;
        }
    }

    public String getDisplayString(boolean asStartAndDuration) {
        if (isExact()) {
            try {
                String dayDate;
                String time = "";
                Calendar cal;

                cal = ISO8601.toCalendar(getStartTime());
                dayDate = new SimpleDateFormat("EEEE", Locale.getDefault()).format(cal.getTime()) + " " + DateUtils.formatDateLong(cal);
                time = DateUtils.formatTime(cal, false) + " " + cal.getTimeZone().getDisplayName(false, TimeZone.SHORT);

                return "Exactly on " + dayDate + " @ " + time;

            } catch (ParseException e) {
                Log.v(TAG, e);
            }
        } else {
            if (asStartAndDuration) {
                try {
                    Calendar cal = ISO8601.toCalendar(getStartTime());
                    String dayDate;
                    String time = "";

                    dayDate = new SimpleDateFormat("EEEE", Locale.getDefault()).format(cal.getTime()) + " " + DateUtils.formatDateLong(cal);
                    time = DateUtils.formatTime(cal, false);

                    String msg = "Exactly on " + dayDate + " @ " + time + ".\n\t for ";

                    long length = ISO8601.toUtc(getEndTime()) - ISO8601.toUtc(getStartTime());

                    msg += misc.convertMsToHuman(length);

                    return msg;

                } catch (ParseException e) {
                    Log.v(TAG, e);
                }
            } else {
                try {
                    Calendar cal = ISO8601.toCalendar(getStartTime());
                    String dayDate;
                    String time = "";

                    dayDate = new SimpleDateFormat("EEEE", Locale.getDefault()).format(cal.getTime()) + " " + DateUtils.formatDateLong(cal);
                    time = DateUtils.formatTime(cal, false);

                    String msg = "Between " + dayDate + " @ " + time + "\nand";

                    Calendar cal2 = ISO8601.toCalendar(getEndTime());

                    // same day
                    if (cal.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) && cal.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR)) {
                        time = DateUtils.formatTime(cal2, false) + " " + cal2.getTimeZone().getDisplayName(false, TimeZone.SHORT);
                        msg += time;

                    } else {
                        dayDate = new SimpleDateFormat("EEEE", Locale.getDefault()).format(cal2.getTime()) + " " + DateUtils.formatDateLong(cal2);
                        time = DateUtils.formatTime(cal2, false) + " " + cal2.getTimeZone().getDisplayName(false, TimeZone.SHORT);
                        msg += dayDate + " @ " + time;
                    }

                    return msg;

                } catch (ParseException e) {
                    Log.v(TAG, e);
                }
            }
        }
        return null;
    }

    public boolean isExact() {
        return misc.isEmptyOrNull(getEndTime());
    }

    /*-*********************************************-*/
    /*-			Parcelable Implementation			-*/
    /*-*********************************************-*/
    public static final Parcelable.Creator<Schedule> CREATOR = new Parcelable.Creator<Schedule>() {

        @Override
        public Schedule createFromParcel(Parcel source) {
            try {
                return Schedule.fromJson((JsonObject) (source.readParcelable(JsonObject.class.getClassLoader())));
            } catch (Exception ex) {
                Log.v(TAG, ex);
            }
            return null;
        }

        @Override
        public Schedule[] newArray(int size) {
            return new Schedule[size];
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
