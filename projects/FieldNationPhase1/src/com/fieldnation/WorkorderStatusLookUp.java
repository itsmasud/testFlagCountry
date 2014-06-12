package com.fieldnation;

import java.text.ParseException;

import com.fieldnation.json.JsonObject;

public class WorkorderStatusLookUp {

	private static JsonObject _table;

	static {
		try {
			// available/requested
			_table.put("2", R.drawable.wosum_status_1);
			_table.put("2.11", R.drawable.wosum_status_1);

			_table.put("9", R.drawable.wosum_status_1);
			_table.put("9.11", R.drawable.wosum_status_1);

			// in progress 
			_table.put("3", R.drawable.wosum_status_3);
			_table.put("3.1", R.drawable.wosum_status_3);
			_table.put("3.2", R.drawable.wosum_status_3);
			_table.put("3.26", R.drawable.wosum_status_4); //? two states here?
			
			// assigned
			_table.put("3.6", R.drawable.wosum_status_3);
			_table.put("3.7", R.drawable.wosum_status_3);
			_table.put("3.8", R.drawable.wosum_status_3);
			_table.put("3.16", R.drawable.wosum_status_3);
			_table.put("3.17", R.drawable.wosum_status_3);
			_table.put("3.18", R.drawable.wosum_status_3);
			_table.put("3.22", R.drawable.wosum_status_3);
			_table.put("3.27", R.drawable.wosum_status_3);

		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static int getDrawable(int statusId, int labelId) {

		return R.drawable.wosum_status_1;
	}

}
