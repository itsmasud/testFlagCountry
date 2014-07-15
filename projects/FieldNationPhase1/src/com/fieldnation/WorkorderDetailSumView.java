package com.fieldnation;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

public class WorkorderDetailSumView extends LinearLayout {

	public WorkorderDetailSumView(Context context) {
		this(context, null);
	}

	public WorkorderDetailSumView(Context context, AttributeSet attrs) {
		super(context, attrs);
		LayoutInflater.from(context).inflate(R.layout.view_workorder_detail_sum, this);

		if (isInEditMode())
			return;

	}

}
