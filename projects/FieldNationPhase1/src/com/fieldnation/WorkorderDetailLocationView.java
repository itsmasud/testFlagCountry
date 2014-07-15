package com.fieldnation;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

public class WorkorderDetailLocationView extends LinearLayout {
	public WorkorderDetailLocationView(Context context) {
		this(context, null);
	}

	public WorkorderDetailLocationView(Context context, AttributeSet attrs) {
		super(context, attrs);
		LayoutInflater.from(context).inflate(R.layout.view_workorder_detail_location, this);

		if (isInEditMode())
			return;
	}

}
