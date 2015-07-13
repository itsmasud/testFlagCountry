package com.fieldnation.data.workorder;

import android.os.Parcel;
import android.os.Parcelable;

import com.fieldnation.json.JsonObject;
import com.fieldnation.json.Serializer;
import com.fieldnation.json.annotations.Json;
import com.fieldnation.utils.ISO8601;
import com.fieldnation.utils.misc;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;

public class Schedule implements Parcelable {

    @Json(name = "duration")
    private Double _duration;
    @Json(name = "endTime")
    private String _endTime;
    @Json(name = "endTimeHours")
    private String _endTimeHours;
    @Json(name = "startTime")
    private String _startTime;
    @Json(name = "startTimeHours")
    private String _startTimeHours;
    @Json(name = "workorderScheduleId")
    private Integer _workorderScheduleId;

    public Schedule() {
    }

    public Double getDuration() {
        return _duration;
    }

    public String getEndTime() {
        return _endTime;
    }

    public String getEndTimeHours() {
        return _endTimeHours;
    }

    public String getStartTime() {
        return _startTime;
    }

    public String getStartTimeHours() {
        return _startTimeHours;
    }

    public Integer getWorkorderScheduleId() {
        return _workorderScheduleId;
    }

    public JsonObject toJson() {
        return toJson(this);
    }

    public static JsonObject toJson(Schedule schedule) {
        try {
            return Serializer.serializeObject(schedule);
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public static Schedule fromJson(JsonObject json) {
        try {
            return Serializer.unserializeObject(Schedule.class, json);
        } catch (Exception ex) {
            ex.printStackTrace();
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

    public String getFormatedDate() {
        try {
            if (!misc.isEmptyOrNull(getStartTime())) {
                String when = "";
                Calendar cal = null;
                cal = ISO8601.toCalendar(getStartTime());
                when = misc.formatDateReallyLong(cal);

                // Wednesday, Dec 4, 2056
                if (!misc.isEmptyOrNull(getEndTime())) {
                    Calendar ecal = ISO8601.toCalendar(getEndTime());
                    if (ecal.get(Calendar.YEAR) > 2000
                            && (ecal.get(Calendar.DAY_OF_YEAR) != cal.get(Calendar.DAY_OF_YEAR))) {
                        when += "\n";
                        when += misc.formatDateReallyLong(cal);
                    }
                }
                return when;
            } else {
                return null;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public String getFormatedTime() {
        try {
            if (!misc.isEmptyOrNull(getStartTime())) {
                String when = "";
                Calendar cal = null;
                cal = ISO8601.toCalendar(getStartTime());
                when = misc.formatTime(cal, false);

                if (!misc.isEmptyOrNull(getEndTime())) {
                    cal = ISO8601.toCalendar(getEndTime());
                    if (cal.get(Calendar.YEAR) > 2000) {
                        when += " - ";
                        when += misc.formatTime(cal, false);
                    }
                }
                return when;
            } else {
                return null;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
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
                when = misc.formatDate(cal);

                if (!misc.isEmptyOrNull(getEndTime())) {
                    ecal = ISO8601.toCalendar(getEndTime());
                    if (ecal.get(Calendar.YEAR) > 2000
                            && (ecal.get(Calendar.DAY_OF_YEAR) != cal.get(Calendar.DAY_OF_YEAR))) {
                        when += " - ";
                        when += misc.formatDate(ecal);
                    }
                }
                when += " @ ";

                when += misc.formatTime(cal, false);

                if (ecal != null
                        && ((ecal.get(Calendar.HOUR_OF_DAY) != cal.get(Calendar.HOUR_OF_DAY))
                        || (ecal.get(Calendar.MINUTE) != cal.get(Calendar.MINUTE)))) {
                    when += " - ";
                    when += misc.formatTime(ecal, false);
                }

                return when;
            } else {
                return null;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
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
                dayDate = new SimpleDateFormat("EEEE", Locale.getDefault()).format(cal.getTime()) + " " + misc.formatDateLong(cal);
                time = misc.formatTime(cal, false) + " " + cal.getTimeZone().getDisplayName(false, TimeZone.SHORT);

                return "You will need to arrive exactly on " + dayDate + " at " + time + ".";

            } catch (ParseException e) {
                e.printStackTrace();
            }
        } else {
            if (asStartAndDuration) {
                try {
                    Calendar cal = ISO8601.toCalendar(getStartTime());
                    String dayDate;
                    String time = "";

                    dayDate = new SimpleDateFormat("EEEE", Locale.getDefault()).format(cal.getTime()) + " " + misc.formatDateLong(cal);
                    time = misc.formatTime(cal, false);

                    String msg = "You will need to arrive at\n\t" + dayDate + " at " + time + ".\n\tAnd work for ";

                    long length = ISO8601.toUtc(getEndTime()) - ISO8601.toUtc(getStartTime());

                    msg += misc.convertMsToHuman(length);

                    return msg;

                } catch (ParseException e) {
                    e.printStackTrace();
                }
            } else {
                try {
                    Calendar cal = ISO8601.toCalendar(getStartTime());
                    String dayDate;
                    String time = "";

                    dayDate = new SimpleDateFormat("EEEE", Locale.getDefault()).format(cal.getTime()) + " " + misc.formatDateLong(cal);
                    time = misc.formatTime(cal, false);

                    String msg = "You will need to arrive between \n\t" + dayDate + " at " + time + " and\n\t";

                    Calendar cal2 = ISO8601.toCalendar(getEndTime());

                    // same day
                    if (cal.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) && cal.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR)) {
                        time = misc.formatTime(cal2, false) + " " + cal2.getTimeZone().getDisplayName(false, TimeZone.SHORT);
                        msg += time + ".";

                    } else {
                        dayDate = new SimpleDateFormat("EEEE", Locale.getDefault()).format(cal2.getTime()) + " " + misc.formatDateLong(cal2);
                        time = misc.formatTime(cal2, false) + " " + cal2.getTimeZone().getDisplayName(false, TimeZone.SHORT);
                        msg += dayDate + " at " + time + ".";
                    }

                    return msg;

                } catch (ParseException e) {
                    e.printStackTrace();
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
                ex.printStackTrace();
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
