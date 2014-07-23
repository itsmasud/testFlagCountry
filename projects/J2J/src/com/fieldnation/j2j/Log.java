package com.fieldnation.j2j;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.Calendar;

import com.fieldnation.utils.misc;

public class Log {
	private static Log _instance = null;

	private FileOutputStream _log;

	private static Log getInstance() {
		if (_instance == null) {
			_instance = new Log();
		}
		return _instance;
	}

	private Log() {
		new File(Config.LogPath).mkdirs();

		try {
			_log = new FileOutputStream(Config.LogPath + "log.txt", true);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		_print("************************************\r\n");
		_print("Start: " + misc.formatDateTime(Calendar.getInstance(), true) + "\r\n");
		_print("************************************\r\n");
	}

	@Override
	protected void finalize() throws Throwable {
		if (_log != null) {
			_print("************************************\r\n");
			_print("Finish: " + misc.formatDateTime(Calendar.getInstance(), true) + "\r\n");
			_print("************************************\r\n");
			_log.close();
		}
		super.finalize();
	}

	private void _print(String message) {
		try {
			System.out.print(message);
			_log.write(message.getBytes());
			_log.flush();
		} catch (Exception ex) {
			ex.printStackTrace();
		}

	}

	public static void print(Object message) {
		getInstance()._print(message.toString());
	}

	public static void println(Object message) {
		print(message + "\r\n");
	}

}
