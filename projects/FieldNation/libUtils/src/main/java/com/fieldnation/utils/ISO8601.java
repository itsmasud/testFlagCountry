package com.fieldnation.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Helper class for handling ISO 8601 strings of the following format:
 * "2008-03-01T13:00:00+01:00". It also supports parsing the "Z" timezone.
 * <p/>
 * - see:
 * http://stackoverflow.com/questions/2201925/converting-iso8601-compliant
 * -string-to-java-util-date -*
 */
public final class ISO8601 {
    private static SimpleDateFormat _sdf = null;

    private static SimpleDateFormat getSDF() {
        if (_sdf == null) {
            _sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
        }

        return _sdf;
    }


    public static String fromUTC(long utcMilliseconds) {
        final Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(utcMilliseconds);
        return fromCalendar(cal);
    }

    /**
     * Transform Calendar to ISO 8601 string.
     */
    public static String fromCalendar(final Calendar calendar) {
        Date date = calendar.getTime();
        String formatted = getSDF().format(date);
        return formatted.substring(0, 22) + ":" + formatted.substring(22);
    }


    /**
     * Get current date and time formatted as ISO 8601 string.
     */
    public static String now() {
        return fromCalendar(Calendar.getInstance());
    }

    public static Calendar toCalendar(final long utcMilliseconds) {
        final Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(utcMilliseconds);
        return cal;
    }

    /**
     * Transform ISO 8601 string to Calendar.
     */
    public static Calendar toCalendar(final String iso8601string) throws ParseException {
        Calendar calendar = Calendar.getInstance();
        String s = iso8601string.replace("Z", "+00:00");
        try {
            s = s.substring(0, 22) + s.substring(23); // to get rid of the ":"
        } catch (IndexOutOfBoundsException e) {
            throw new ParseException("Invalid length", 0);
        }
        Date date = getSDF().parse(s);
        calendar.setTime(date);
        return calendar;
    }

    public static long toUtc(final String iso8601string) throws ParseException {
        return toCalendar(iso8601string).getTimeInMillis();
    }
}