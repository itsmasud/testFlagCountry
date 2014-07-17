package com.fieldnation.ui.workorder.detail;

import com.fieldnation.R;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

public class ExpenseView extends LinearLayout {
	private static final String TAG = "ui.workorder.detail.ExpenseView";

	/*-*************************************-*/
	/*-				Life Cycle				-*/
	/*-*************************************-*/
	public ExpenseView(Context context) {
		this(context, null);
	}

	public ExpenseView(Context context, AttributeSet attrs) {
		super(context, attrs);
		LayoutInflater.from(context).inflate(R.layout.view_expense, this);

		if (isInEditMode())
			return;

	}

	/*-*********************************-*/
	/*-				Event				-*/
	/*-*********************************-*/

}
