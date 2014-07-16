package com.fieldnation.ui.workorder.detail;

import com.fieldnation.R;
import com.fieldnation.R.layout;
import com.fieldnation.data.workorder.Location;
import com.fieldnation.data.workorder.Workorder;
import com.fieldnation.ui.workorder.WorkorderRenderer;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ExpectedPaymentView extends LinearLayout implements WorkorderRenderer {
	private static final String TAG = "WorkorderDetailExpectedPaymentView";

	// UI

	// Data
	private Workorder _workorder;

	/*-*************************************-*/
	/*-				Life Cycle				-*/
	/*-*************************************-*/

	public ExpectedPaymentView(Context context) {
		this(context, null);
	}

	public ExpectedPaymentView(Context context, AttributeSet attrs) {
		super(context, attrs);
		LayoutInflater.from(context).inflate(R.layout.view_workorder_detail_expected_payment, this);

		if (isInEditMode())
			return;

	}

	/*-*************************************-*/
	/*-				Mutators				-*/
	/*-*************************************-*/

	@Override
	public void setWorkorder(Workorder workorder) {
		_workorder = workorder;
		refresh();
	}

	private void refresh() {
	}

}
