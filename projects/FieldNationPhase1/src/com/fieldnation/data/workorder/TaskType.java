package com.fieldnation.data.workorder;

import android.content.Context;

import com.fieldnation.R;

public enum TaskType {
	CONFIRM_ASSIGNMENT(1),
	CLOSE_OUT_NOTES(2),
	CHECKIN(3),
	CHECKOUT(4),
	UPLOAD_FILE(5),
	UPLOAD_PICTURE(6),
	CUSTOM_FIELD(7),
	PHONE(8),
	EMAIL(9),
	SIGNATURE(10),
	SHPIMENT_TRACKING(11),
	DOWNLOAD(12);

	private int _id;

	private TaskType(int id) {
		_id = id;
	}

	public String getDisplay(Context context) {
		return context.getResources().getStringArray(R.array.task_type)[_id];
	}

	public static TaskType fromId(int id) {
		TaskType[] types = values();

		for (int i = 0; i < types.length; i++) {
			if (id == types[i]._id)
				return types[i];
		}
		return null;
	}

}
