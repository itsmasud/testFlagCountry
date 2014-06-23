package com.fieldnation;

import java.text.ParseException;
import java.util.Hashtable;

import com.fieldnation.json.JsonObject;

/**
 * A lookup table for getting the colors for a work order summary
 * 
 * TODO this table is incomplete!
 * 
 * @author michael.carver
 * 
 */
public class WorkorderStatusLookUp {

	private static Hashtable<String, Integer> _colorTable;
	private static Hashtable<String, String> _labelTable;

	static {
		_colorTable = new Hashtable<String, Integer>();
		// available view
		_colorTable.put("available.2.11", R.drawable.wosum_status_1);
		_colorTable.put("available.9.11", R.drawable.wosum_status_2);
		_colorTable.put("available.2.12", R.drawable.wosum_status_3);
		_colorTable.put("available.2.13", R.drawable.wosum_status_4);

		// assigned view
		_colorTable.put("assigned.3.17", R.drawable.wosum_status_1);
		_colorTable.put("assigned.3.18", R.drawable.wosum_status_1);
		_colorTable.put("assigned.3.16", R.drawable.wosum_status_2);
		_colorTable.put("assigned.3.16.on-hold", R.drawable.wosum_status_3);
		_colorTable.put("assigned.3.17.on-hold", R.drawable.wosum_status_3);
		_colorTable.put("assigned.3.18.on-hold", R.drawable.wosum_status_3);
		_colorTable.put("assigned.3.16.on-hold.acknowledge", R.drawable.wosum_status_4);
		_colorTable.put("assigned.3.17.on-hold.acknowledge", R.drawable.wosum_status_4);
		_colorTable.put("assigned.3.18.on-hold.acknowledge", R.drawable.wosum_status_4);

		// in progress view
		_colorTable.put("inprog.3.1", R.drawable.wosum_status_1);
		_colorTable.put("inprog.3.3", R.drawable.wosum_status_2);
		_colorTable.put("inprog.3.16.on-hold", R.drawable.wosum_status_3);
		_colorTable.put("inprog.3.17.on-hold", R.drawable.wosum_status_3);
		_colorTable.put("inprog.3.18.on-hold", R.drawable.wosum_status_3);
		_colorTable.put("inprog.3.16.on-hold.acknowledge", R.drawable.wosum_status_4);
		_colorTable.put("inprog.3.17.on-hold.acknowledge", R.drawable.wosum_status_4);
		_colorTable.put("inprog.3.18.on-hold.acknowledge", R.drawable.wosum_status_4);

		// completed view
		_colorTable.put("completed.4.19", R.drawable.wosum_status_1);
		_colorTable.put("completed.4.20", R.drawable.wosum_status_2);
		_colorTable.put("completed.4.21", R.drawable.wosum_status_3);
		// TODO paid
		// TODO cacelled

		_labelTable = new Hashtable<String, String>();

		// available view
		_labelTable.put("2.11", "Available");
		_labelTable.put("9.11", "Routed");
		_labelTable.put("2.12", "Requested");
		_labelTable.put("2.13", "Sent Counter");

		// assigned view
		_labelTable.put("3.17", "Confirmed");
		_labelTable.put("3.18", "Confirmed");
		_labelTable.put("3.16", "Unconfirmed");
		_labelTable.put("3.16.on-hold", "On Hold");
		_labelTable.put("3.17.on-hold", "On Hold");
		_labelTable.put("3.18.on-hold", "On Hold");
		_labelTable.put("3.16.on-hold.acknowledge", "");
		_labelTable.put("3.17.on-hold.acknowledge", "");
		_labelTable.put("3.18.on-hold.acknowledge", "");

		// in progress view
		_labelTable.put("3.1", "");
		_labelTable.put("3.3", "");
		_labelTable.put("3.16.on-hold", "");
		_labelTable.put("3.17.on-hold", "");
		_labelTable.put("3.18.on-hold", "");
		_labelTable.put("3.16.on-hold.acknowledge", "");
		_labelTable.put("3.17.on-hold.acknowledge", "");
		_labelTable.put("3.18.on-hold.acknowledge", "");

		// completed view
		_labelTable.put("4.19", "");
		_labelTable.put("4.20", "");
		_labelTable.put("4.21", "");
		// TODO paid
		// TODO cacelled

	}

	public static int getDrawable(int statusId, int labelId) {
		String key = statusId + "." + labelId;
		if (_colorTable.containsKey(key)) {
			return _colorTable.get(key);
		}

		return R.drawable.wosum_status_1;
	}

	public static String getLabel(int statusId, int labelId) {
		String key = statusId + "." + labelId;

		if (_labelTable.containsKey(key)) {
			return _labelTable.get(key);
		}
		return key;
	}

}
