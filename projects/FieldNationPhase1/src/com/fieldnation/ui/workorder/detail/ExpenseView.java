package com.fieldnation.ui.workorder.detail;

import com.fieldnation.R;
import com.fieldnation.R.layout;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

public class ExpenseView extends LinearLayout {

	public ExpenseView(Context context) {
		this(context, null);
	}

	public ExpenseView(Context context, AttributeSet attrs) {
		super(context, attrs);
		LayoutInflater.from(context).inflate(R.layout.view_expense, this);

		if (isInEditMode())
			return;

	}

}
