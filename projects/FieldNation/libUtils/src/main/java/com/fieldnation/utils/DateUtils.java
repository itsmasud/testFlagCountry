package com.fieldnation.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class DateUtils {

    public static String humanReadableAge(Calendar past) {
        long now = System.currentTimeMillis();

        // same day, show time
        if (now - past.getTimeInMillis() < 86400000) {
            return new SimpleDateFormat("h:mm a", Locale.getDefault()).format(past.getTime()).toUpperCase();
        }
        // yesterday show yesterda
        else if (now - past.getTimeInMillis() < 172800000) {
            return "Yesterday";
        }
        // if same week show day
        else if (now - past.getTimeInMillis() < 604800000) {
            return new SimpleDateFormat("EEEE", Locale.getDefault()).format(past.getTime());
        }
        // else show date
        else {
            return new SimpleDateFormat("M/dd/yy", Locale.getDefault()).format(past.getTime());
        }
    }

    public static String formatDateForCF(final Calendar calendar) {
        Date date = calendar.getTime();
        return new SimpleDateFormat("MM/dd/yyyy", Locale.getDefault()).format(date);
    }

    public static String formatTimeForCF(final Calendar calendar) {
        Date date = calendar.getTime();
        return new SimpleDateFormat("h:mm a", Locale.getDefault()).format(date);
    }

    public static String formatDateTimeForCF(final Calendar calendar) {
        Date date = calendar.getTime();
        return new SimpleDateFormat("MM/dd/yyyy h:mm a", Locale.getDefault()).format(date);
    }

    public static String toRoundDuration(long milliseconds) {
        long count = 0;
        if (milliseconds < 60000) {
            count = milliseconds / 1000;
            if (count > 1) {
                return count + " seconds";
            }
            return count + " second";
        }

        if (milliseconds < 3600000) {
            count = milliseconds / 60000;
            if (count > 1) {
                return count + " minutes";
            }
            return count + " minute";
        }

        if (milliseconds < 86400000) {
            count = milliseconds / 3600000;
            if (count > 1) {
                return count + " hours";
            }
            return count + " hour";
        }

        if (milliseconds < 31536000000L) {
            count = milliseconds / 86400000;
            if (count > 1) {
                return count + " days";
            }
            return count + " day";
        }

        count = milliseconds / 31536000000L;
        if (count > 1) {
            return count + " years";
        }
        return count + " year";
    }

    public static Calendar applyTimeZone(Calendar fromCal) {
        TimeZone fromTz = fromCal.getTimeZone();
        TimeZone toTz = TimeZone.getDefault();

        if (toTz.equals(fromTz))
            return fromCal;

        Calendar toCal = Calendar.getInstance(toTz);

        if (toTz.useDaylightTime()) {
            toCal.setTimeInMillis(fromCal.getTimeInMillis() + toTz.getRawOffset() - toTz.getDSTSavings());
        } else {
            toCal.setTimeInMillis(fromCal.getTimeInMillis() + toTz.getRawOffset());
        }

        return toCal;
    }

	/*
     * http://docs.oracle.com/javase/7/docs/api/java/util/Formatter.html#dt
	 */

    /**
     * @param calendar
     * @return June 3, 2014
     */
    public static String formatDateLong(Calendar calendar) {
        calendar = applyTimeZone(calendar);
        return String.format(Locale.US, "%tB", calendar) + " " + calendar.get(Calendar.DAY_OF_MONTH) + ", " + calendar.get(Calendar.YEAR);
    }

    /**
     * @param calendar
     * @return June 3
     */
    public static String formatDateLongNoYear(Calendar calendar) {
        calendar = applyTimeZone(calendar);
        return String.format(Locale.US, "%tB", calendar) + " " + calendar.get(Calendar.DAY_OF_MONTH);
    }

    /**
     * @param calendar
     * @return Wednesday, Jun 3, 2014
     */
    public static String formatDateReallyLong(Calendar calendar) {
        calendar = applyTimeZone(calendar);
        return String.format(Locale.US, "%tA", calendar) + ", " + String.format(Locale.US, "%tb", calendar) + " " + calendar.get(Calendar.DAY_OF_MONTH) + ", " + calendar.get(Calendar.YEAR);
    }

    /**
     * @param calendar
     * @return Wednesday, Jun 3
     */
    public static String formatDateReallyLongNoYear(Calendar calendar) {
        calendar = applyTimeZone(calendar);
        return String.format(Locale.US, "%tA", calendar) + ", " + String.format(Locale.US, "%tb", calendar) + " " + calendar.get(Calendar.DAY_OF_MONTH);
    }

    /**
     * @param calendar
     * @param seconds
     * @return MM/DD/YYYY HH:MM:SS am/pm
     */
    public static String formatDateTime(Calendar calendar, boolean seconds) {
        return formatDate(calendar) + " " + formatTime(calendar, seconds);
    }

    /**
     * @param calendar
     * @return June 3, 2014 @ HH:MM am/pm
     */
    public static String formatDateTimeLong(Calendar calendar) {
        return formatDateLong(calendar) + " @ " + formatTime(calendar, false);
    }

    /**
     * @param calendar
     * @return in the form MM/DD/YYYY
     */
    public static String formatDate(Calendar calendar) {
        calendar = applyTimeZone(calendar);
        int months = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int year = calendar.get(Calendar.YEAR);

        return months + "/" + day + "/" + year;
    }

    public static String formatMessageTime(Calendar calendar) {
        Calendar cal = applyTimeZone(calendar);

        // today
        if (calendar.getTimeInMillis() / 86400000 == Calendar.getInstance().getTimeInMillis() / 86400000) {
            return "Today";
        }

        // > 1 year old
        if (Calendar.getInstance().get(Calendar.YEAR) > calendar.get(Calendar.YEAR)) {
            return String.format(Locale.US, "%tb", calendar) + " " + calendar.get(Calendar.DAY_OF_MONTH) + ", " + calendar.get(Calendar.YEAR);
        }
        return String.format(Locale.US, "%tb", calendar) + " " + calendar.get(Calendar.DAY_OF_MONTH) + " " + formatTime(
                cal, false);
    }

    /**
     * @param calendar
     * @param seconds  if true, then seconds are displayed.
     * @return HH:MM:SSam/pm
     */
    public static String formatTime(Calendar calendar, boolean seconds) {
        calendar = applyTimeZone(calendar);

        String time = "";

        int hours = calendar.get(Calendar.HOUR);

        if (hours == 0) {
            time += "12:";
        } else {
            time += hours + ":";
        }

        int min = calendar.get(Calendar.MINUTE);

        if (min < 10) {
            time += "0" + min;
        } else {
            time += min + "";
        }

        if (seconds) {
            int sec = calendar.get(Calendar.SECOND);

            if (sec < 10) {
                time += ":0" + sec;
            } else {
                time += ":" + sec;
            }

        }

        if (calendar.get(Calendar.AM_PM) == Calendar.AM) {
            time += "am";
        } else {
            time += "pm";
        }

        return time;
    }

    public static String formatTime2(Calendar calendar) {
        calendar = applyTimeZone(calendar);

        String time = "";

        int hours = calendar.get(Calendar.HOUR);

        if (hours == 0) {
            time += "12:";
        } else {
            time += hours + ":";
        }

        int min = calendar.get(Calendar.MINUTE);

        if (min < 10) {
            time += "0" + min;
        } else {
            time += min + "";
        }

        return time;
    }


    /**
     * <p>Checks if two dates are on the same day ignoring time.</p>
     *
     * @param date1 the first date, not altered, not null
     * @param date2 the second date, not altered, not null
     * @return true if they represent the same day
     * @throws IllegalArgumentException if either date is <code>null</code>
     */
    public static boolean isSameDay(Date date1, Date date2) {
        if (date1 == null || date2 == null) {
            throw new IllegalArgumentException("The dates must not be null");
        }
        Calendar cal1 = Calendar.getInstance();
        cal1.setTime(date1);
        Calendar cal2 = Calendar.getInstance();
        cal2.setTime(date2);
        return isSameDay(cal1, cal2);
    }

    /**
     * <p>Checks if two calendars represent the same day ignoring time.</p>
     *
     * @param cal1 the first calendar, not altered, not null
     * @param cal2 the second calendar, not altered, not null
     * @return true if they represent the same day
     * @throws IllegalArgumentException if either calendar is <code>null</code>
     */
    public static boolean isSameDay(Calendar cal1, Calendar cal2) {
        if (cal1 == null || cal2 == null) {
            throw new IllegalArgumentException("The dates must not be null");
        }
        return (cal1.get(Calendar.ERA) == cal2.get(Calendar.ERA) &&
                cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
                cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR));
    }

    /**
     * <p>Checks if a date is today.</p>
     *
     * @param date the date, not altered, not null.
     * @return true if the date is today.
     * @throws IllegalArgumentException if the date is <code>null</code>
     */
    public static boolean isToday(Date date) {
        return isSameDay(date, Calendar.getInstance().getTime());
    }

    /**
     * <p>Checks if a calendar date is today.</p>
     *
     * @param cal the calendar, not altered, not null
     * @return true if cal date is today
     * @throws IllegalArgumentException if the calendar is <code>null</code>
     */
    public static boolean isToday(Calendar cal) {
        return isSameDay(cal, Calendar.getInstance());
    }

    public static boolean isTomorrow(Calendar cal1) {
        Calendar tomorrow = Calendar.getInstance();
        tomorrow.add(Calendar.DAY_OF_YEAR, 1);
        Calendar dayAfter = Calendar.getInstance();
        dayAfter.add(Calendar.DAY_OF_YEAR, 2);

        if (cal1.getTimeInMillis() >= (tomorrow.getTimeInMillis() / 86400000) * 86400000
                && cal1.getTimeInMillis() < (dayAfter.getTimeInMillis() / 86400000) * 86400000)
            return true;

        return false;
    }

    /**
     * <p>Checks if the date is before today.</p>
     *
     * @param date the date to test against
     * @return true if the date is before today
     */
    public static boolean isBeforeToday(Date date) {
        return isBeforeDay(date, Calendar.getInstance().getTime());
    }

    /**
     * <p>Checks if the first date is before the second date ignoring time.</p>
     *
     * @param date1 the first date, not altered, not null
     * @param date2 the second date, not altered, not null
     * @return true if the first date day is before the second date day.
     * @throws IllegalArgumentException if the date is <code>null</code>
     */
    public static boolean isBeforeDay(Date date1, Date date2) {
        if (date1 == null || date2 == null) {
            throw new IllegalArgumentException("The dates must not be null");
        }
        Calendar cal1 = Calendar.getInstance();
        cal1.setTime(date1);
        Calendar cal2 = Calendar.getInstance();
        cal2.setTime(date2);
        return isBeforeDay(cal1, cal2);
    }


    /**
     * <p>Checks if cal is before today</p>
     *
     * @param cal
     * @return
     */
    public static boolean isBeforeToday(Calendar cal) {
        return isBeforeDay(cal, Calendar.getInstance());
    }

    /**
     * <p>Checks if the first calendar date is before the second calendar date ignoring time.</p>
     *
     * @param cal1 the first calendar, not altered, not null.
     * @param cal2 the second calendar, not altered, not null.
     * @return true if cal1 date is before cal2 date ignoring time.
     * @throws IllegalArgumentException if either of the calendars are <code>null</code>
     */
    public static boolean isBeforeDay(Calendar cal1, Calendar cal2) {
        if (cal1 == null || cal2 == null) {
            throw new IllegalArgumentException("The dates must not be null");
        }
        if (cal1.get(Calendar.ERA) < cal2.get(Calendar.ERA)) return true;
        if (cal1.get(Calendar.ERA) > cal2.get(Calendar.ERA)) return false;
        if (cal1.get(Calendar.YEAR) < cal2.get(Calendar.YEAR)) return true;
        if (cal1.get(Calendar.YEAR) > cal2.get(Calendar.YEAR)) return false;
        return cal1.get(Calendar.DAY_OF_YEAR) < cal2.get(Calendar.DAY_OF_YEAR);
    }

    /**
     * <p>Checks if the first date is after the second date ignoring time.</p>
     *
     * @param date1 the first date, not altered, not null
     * @param date2 the second date, not altered, not null
     * @return true if the first date day is after the second date day.
     * @throws IllegalArgumentException if the date is <code>null</code>
     */
    public static boolean isAfterDay(Date date1, Date date2) {
        if (date1 == null || date2 == null) {
            throw new IllegalArgumentException("The dates must not be null");
        }
        Calendar cal1 = Calendar.getInstance();
        cal1.setTime(date1);
        Calendar cal2 = Calendar.getInstance();
        cal2.setTime(date2);
        return isAfterDay(cal1, cal2);
    }

    /**
     * <p>Checks if the first calendar date is after the second calendar date ignoring time.</p>
     *
     * @param cal1 the first calendar, not altered, not null.
     * @param cal2 the second calendar, not altered, not null.
     * @return true if cal1 date is after cal2 date ignoring time.
     * @throws IllegalArgumentException if either of the calendars are <code>null</code>
     */
    public static boolean isAfterDay(Calendar cal1, Calendar cal2) {
        if (cal1 == null || cal2 == null) {
            throw new IllegalArgumentException("The dates must not be null");
        }
        if (cal1.get(Calendar.ERA) < cal2.get(Calendar.ERA)) return false;
        if (cal1.get(Calendar.ERA) > cal2.get(Calendar.ERA)) return true;
        if (cal1.get(Calendar.YEAR) < cal2.get(Calendar.YEAR)) return false;
        if (cal1.get(Calendar.YEAR) > cal2.get(Calendar.YEAR)) return true;
        return cal1.get(Calendar.DAY_OF_YEAR) > cal2.get(Calendar.DAY_OF_YEAR);
    }

    /**
     * <p>Checks if a date is after today and within a number of days in the future.</p>
     *
     * @param date the date to check, not altered, not null.
     * @param days the number of days.
     * @return true if the date day is after today and within days in the future .
     * @throws IllegalArgumentException if the date is <code>null</code>
     */
    public static boolean isWithinDaysFuture(Date date, int days) {
        if (date == null) {
            throw new IllegalArgumentException("The date must not be null");
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return isWithinDaysFuture(cal, days);
    }

    /**
     * <p>Checks if a calendar date is after today and within a number of days in the future.</p>
     *
     * @param cal  the calendar, not altered, not null
     * @param days the number of days.
     * @return true if the calendar date day is after today and within days in the future .
     * @throws IllegalArgumentException if the calendar is <code>null</code>
     */
    public static boolean isWithinDaysFuture(Calendar cal, int days) {
        if (cal == null) {
            throw new IllegalArgumentException("The date must not be null");
        }
        Calendar today = Calendar.getInstance();
        Calendar future = Calendar.getInstance();
        future.add(Calendar.DAY_OF_YEAR, days);
        return (isAfterDay(cal, today) && !isAfterDay(cal, future));
    }

    /**
     * Returns the given date with the time set to the start of the day.
     */
    public static Date getStart(Date date) {
        return clearTime(date);
    }

    /**
     * Returns the given date with the time values cleared.
     */
    public static Date clearTime(Date date) {
        if (date == null) {
            return null;
        }
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
        return c.getTime();
    }

    /** Determines whether or not a date has any time values (hour, minute,
     * seconds or millisecondsReturns the given date with the time values cleared. */

    /**
     * Determines whether or not a date has any time values.
     *
     * @param date The date.
     * @return true iff the date is not null and any of the date's hour, minute,
     * seconds or millisecond values are greater than zero.
     */
    public static boolean hasTime(Date date) {
        if (date == null) {
            return false;
        }
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        if (c.get(Calendar.HOUR_OF_DAY) > 0) {
            return true;
        }
        if (c.get(Calendar.MINUTE) > 0) {
            return true;
        }
        if (c.get(Calendar.SECOND) > 0) {
            return true;
        }
        if (c.get(Calendar.MILLISECOND) > 0) {
            return true;
        }
        return false;
    }

    /**
     * Returns the given date with time set to the end of the day
     */
    public static Date getEnd(Date date) {
        if (date == null) {
            return null;
        }
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.set(Calendar.HOUR_OF_DAY, 23);
        c.set(Calendar.MINUTE, 59);
        c.set(Calendar.SECOND, 59);
        c.set(Calendar.MILLISECOND, 999);
        return c.getTime();
    }

    /**
     * Returns the maximum of two dates. A null date is treated as being less
     * than any non-null date.
     */
    public static Date max(Date d1, Date d2) {
        if (d1 == null && d2 == null) return null;
        if (d1 == null) return d2;
        if (d2 == null) return d1;
        return (d1.after(d2)) ? d1 : d2;
    }

    /**
     * Returns the minimum of two dates. A null date is treated as being greater
     * than any non-null date.
     */
    public static Date min(Date d1, Date d2) {
        if (d1 == null && d2 == null) return null;
        if (d1 == null) return d2;
        if (d2 == null) return d1;
        return (d1.before(d2)) ? d1 : d2;
    }

    /**
     * The maximum date possible.
     */
    public static Date MAX_DATE = new Date(Long.MAX_VALUE);

}