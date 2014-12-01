package com.fieldnation.ui.workorder.detail;

import com.fieldnation.R;
import com.fieldnation.data.workorder.Workorder;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

public class OnsiteWorkView extends LinearLayout implements WorkorderRenderer {
	private static final String TAG = "ui.workorder.detail.OnsiteWorkView";

	// UI

	// Data
	private Workorder _workorder;

	/*-*************************************-*/
	/*-				Life Cycle				-*/
	/*-*************************************-*/

	public OnsiteWorkView(Context context) {
		this(context, null);
	}

	public OnsiteWorkView(Context context, AttributeSet attrs) {
		super(context, attrs);
		LayoutInflater.from(context).inflate(R.layout.view_wd_onsitework, this);

		if (isInEditMode())
			return;

	}

	/*-*************************************-*/
	/*-				Mutators				-*/
	/*-*************************************-*/

	@Override
	public void setWorkorder(Workorder workorder, boolean isCached) {
		_workorder = workorder;
		refresh();
	}

	private void refresh() {
	}

}
