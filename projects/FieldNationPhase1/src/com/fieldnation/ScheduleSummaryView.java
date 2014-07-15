package com.fieldnation;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;

public class ScheduleSummaryView extends RelativeLayout {
	private static final String TAG = "ScheduleSummaryView";

	/*-*************************************-*/
	/*-				Life Cycle				-*/
	/*-*************************************-*/

	public ScheduleSummaryView(Context context) {
		this(context, null, -1);
	}

	public ScheduleSummaryView(Context context, AttributeSet attrs) {
		this(context, attrs, -1);
	}

	public ScheduleSummaryView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		LayoutInflater.from(context).inflate(R.layout.view_schedule_summary, this);

		if (isInEditMode())
			return;
	}

}
