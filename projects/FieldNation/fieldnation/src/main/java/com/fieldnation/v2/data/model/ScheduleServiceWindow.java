package com.fieldnation.v2.data.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.fieldnation.fnjson.JsonArray;
import com.fieldnation.fnjson.JsonObject;
import com.fieldnation.fnjson.annotations.Json;
import com.fieldnation.fnjson.annotations.Source;
import com.fieldnation.fnlog.Log;
import com.fieldnation.fntools.DateUtils;
import com.fieldnation.fntools.misc;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * Created by dmgen from swagger.
 */

public class ScheduleServiceWindow implements Parcelable {
    private static final String TAG = "ScheduleServiceWindow";

    @Json(name = "end")
    private Date _end;

    @Json(name = "mode")
    private ModeEnum _mode;

    @Json(name = "start")
    private Date _start;

    @Source
    private JsonObject SOURCE;

    public ScheduleServiceWindow() {
        SOURCE = new JsonObject();
    }

    public ScheduleServiceWindow(JsonObject obj) {
        SOURCE = obj;
    }

    public void setEnd(Date end) throws ParseException {
        _end = end;
        SOURCE.put("end", end.getJson());
    }

    public Date getEnd() {
        try {
            if (_end != null)
                return _end;

            if (SOURCE.has("end") && SOURCE.get("end") != null)
                _end = Date.fromJson(SOURCE.getJsonObject("end"));

        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _end;
    }

    public ScheduleServiceWindow end(Date end) throws ParseException {
        _end = end;
        SOURCE.put("end", end.getJson());
        return this;
    }

    public void setMode(ModeEnum mode) throws ParseException {
        _mode = mode;
        SOURCE.put("mode", mode.toString());
    }

    public ModeEnum getMode() {
        try {
            if (_mode != null)
                return _mode;

            if (SOURCE.has("mode") && SOURCE.get("mode") != null)
                _mode = ModeEnum.fromString(SOURCE.getString("mode"));

        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _mode;
    }

    public ScheduleServiceWindow mode(ModeEnum mode) throws ParseException {
        _mode = mode;
        SOURCE.put("mode", mode.toString());
        return this;
    }

    public void setStart(Date start) throws ParseException {
        _start = start;
        SOURCE.put("start", start.getJson());
    }

    public Date getStart() {
        try {
            if (_start != null)
                return _start;

            if (SOURCE.has("start") && SOURCE.get("start") != null)
                _start = Date.fromJson(SOURCE.getJsonObject("start"));

        } catch (Exception ex) {
            Log.v(TAG, ex);
        }

        return _start;
    }

    public ScheduleServiceWindow start(Date start) throws ParseException {
        _start = start;
        SOURCE.put("start", start.getJson());
        return this;
    }

    /*-******************************-*/
    /*-             Enums            -*/
    /*-******************************-*/
    public enum ModeEnum {
        @Json(name = "between")
        BETWEEN("between"),
        @Json(name = "exact")
        EXACT("exact"),
        @Json(name = "hours")
        HOURS("hours");

        private String value;

        ModeEnum(String value) {
            this.value = value;
        }

        public static ModeEnum fromString(String value) {
            ModeEnum[] values = values();
            for (ModeEnum v : values) {
                if (v.value.equals(value))
                    return v;
            }
            return null;
        }

        public static ModeEnum[] fromJsonArray(JsonArray jsonArray) {
            ModeEnum[] list = new ModeEnum[jsonArray.size()];
            for (int i = 0; i < list.length; i++) {
                list[i] = fromString(jsonArray.getString(i));
            }
            return list;
        }

        @Override
        public String toString() {
            return String.valueOf(value);
        }
    }

    /*-*****************************-*/
    /*-             Json            -*/
    /*-*****************************-*/
    public static JsonArray toJsonArray(ScheduleServiceWindow[] array) {
        JsonArray list = new JsonArray();
        for (ScheduleServiceWindow item : array) {
            list.add(item.getJson());
        }
        return list;
    }

    public static ScheduleServiceWindow[] fromJsonArray(JsonArray array) {
        ScheduleServiceWindow[] list = new ScheduleServiceWindow[array.size()];
        for (int i = 0; i < array.size(); i++) {
            list[i] = fromJson(array.getJsonObject(i));
        }
        return list;
    }

    public static ScheduleServiceWindow fromJson(JsonObject obj) {
        try {
            return new ScheduleServiceWindow(obj);
        } catch (Exception ex) {
            Log.v(TAG, TAG, ex);
            return null;
        }
    }

    public JsonObject getJson() {
        return SOURCE;
    }

    /*-*********************************************-*/
    /*-			Parcelable Implementation           -*/
    /*-*********************************************-*/
    public static final Parcelable.Creator<ScheduleServiceWindow> CREATOR = new Parcelable.Creator<ScheduleServiceWindow>() {

        @Override
        public ScheduleServiceWindow createFromParcel(Parcel source) {
            try {
                return ScheduleServiceWindow.fromJson((JsonObject) source.readParcelable(JsonObject.class.getClassLoader()));
            } catch (Exception ex) {
                Log.v(TAG, ex);
                return null;
            }
        }

        @Override
        public ScheduleServiceWindow[] newArray(int size) {
            return new ScheduleServiceWindow[size];
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
    public String getFormatedDate() {
        try {
            if (!misc.isEmptyOrNull(getStart().getUtc())) {
                String when = "";
                Calendar cal = null;
                cal = getStart().getCalendar();
                when = DateUtils.formatDateReallyLong(cal);

                // Wednesday, Dec 4, 2056
                if (!misc.isEmptyOrNull(getEnd().getUtc())) {
                    Calendar ecal = getEnd().getCalendar();
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
            if (!misc.isEmptyOrNull(getStart().getUtc())) {
                String when = "";
                Calendar cal = null;
                cal = getStart().getCalendar();
                when = DateUtils.formatTime(cal, false);

                if (!misc.isEmptyOrNull(getEnd().getUtc())) {
                    cal = getEnd().getCalendar();
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
            if (!misc.isEmptyOrNull(getStart().getUtc())) {
                String when = "";
                Calendar cal = null;
                Calendar ecal = null;
                cal = getStart().getCalendar();
                when = DateUtils.formatDate(cal);

                if (!misc.isEmptyOrNull(getEnd().getUtc())) {
                    ecal = getEnd().getCalendar();
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
        if (getMode() == ModeEnum.EXACT) {
            try {
                String dayDate;
                String time = "";
                Calendar cal;

                cal = getStart().getCalendar();
                dayDate = new SimpleDateFormat("EEEE", Locale.getDefault()).format(cal.getTime()) + " " + DateUtils.formatDateLong(cal);
                time = DateUtils.formatTime(cal, false) + " " + cal.getTimeZone().getDisplayName(false, java.util.TimeZone.SHORT);

                return "Exactly on " + dayDate + " @ " + time;

            } catch (ParseException e) {
                Log.v(TAG, e);
            }
        } else {
            if (asStartAndDuration) {
                try {
                    Calendar cal = getStart().getCalendar();
                    String dayDate;
                    String time = "";

                    dayDate = new SimpleDateFormat("EEEE", Locale.getDefault()).format(cal.getTime()) + " " + DateUtils.formatDateLong(cal);
                    time = DateUtils.formatTime(cal, false);

                    String msg = "Exactly on " + dayDate + " @ " + time + ".\n\t for ";

                    long length = getEnd().getUtcLong() - getStart().getUtcLong();

                    msg += misc.convertMsToHuman(length);

                    return msg;

                } catch (ParseException e) {
                    Log.v(TAG, e);
                }
            } else {
                try {
                    Calendar cal = getStart().getCalendar();
                    String dayDate;
                    String time = "";

                    dayDate = new SimpleDateFormat("EEEE", Locale.getDefault()).format(cal.getTime()) + " " + DateUtils.formatDateLong(cal);
                    time = DateUtils.formatTime(cal, false);

                    String msg = "Between " + dayDate + " @ " + time + "\nand";

                    Calendar cal2 = getEnd().getCalendar();

                    // same day
                    if (cal.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) && cal.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR)) {
                        time = DateUtils.formatTime(cal2, false) + " " + cal2.getTimeZone().getDisplayName(false, java.util.TimeZone.SHORT);
                        msg += time;

                    } else {
                        dayDate = new SimpleDateFormat("EEEE", Locale.getDefault()).format(cal2.getTime()) + " " + DateUtils.formatDateLong(cal2);
                        time = DateUtils.formatTime(cal2, false) + " " + cal2.getTimeZone().getDisplayName(false, java.util.TimeZone.SHORT);
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

}
