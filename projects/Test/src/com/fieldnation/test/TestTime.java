package com.fieldnation.test;

import java.util.Calendar;
import java.util.TimeZone;

import com.fieldnation.utils.ISO8601;
import com.fieldnation.utils.misc;

public class TestTime {

	public static void main(String[] args) {
		try {
			String startTime = "2014-10-07T12:00:00-04:00";

			Calendar start = ISO8601.toCalendar(startTime);

			System.out.println(misc.formatDateTime(start, true));
		} catch (Exception ex) {
			ex.printStackTrace();
		}

	}
}
