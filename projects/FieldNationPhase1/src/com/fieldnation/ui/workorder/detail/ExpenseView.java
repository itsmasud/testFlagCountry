package com.fieldnation.ui.workorder.detail;

import com.fieldnation.R;
import com.fieldnation.data.workorder.AdditionalExpense;
import com.fieldnation.utils.misc;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ExpenseView extends LinearLayout {
	private static final String TAG = "ui.workorder.detail.ExpenseView";

	// UI
	private TextView _descriptionTextView;
	private TextView _categoryTextView;
	private TextView _costTextView;
	private ImageButton _deleteImageButton;

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

		_descriptionTextView = (TextView) findViewById(R.id.description_textview);
		_categoryTextView = (TextView) findViewById(R.id.category_textview);
		_costTextView = (TextView) findViewById(R.id.cost_textview);
		_deleteImageButton = (ImageButton) findViewById(R.id.delete_imagebutton);

	}

	/*-*********************************-*/
	/*-				Event				-*/
	/*-*********************************-*/

	/*-*************************************-*/
	/*-				Mutators				-*/
	/*-*************************************-*/
	public void setAdditionalExpense(AdditionalExpense expense) {
		_descriptionTextView.setText(expense.getDescription());
		// TODO need to map the ID to a real string
		_categoryTextView.setText(expense.getCategoryId() + "");
		// TODO, need to get quantity and priceper item numbers
		_costTextView.setText(misc.toCurrency(expense.getPrice()));
	}

	public void setOnDeleteClickListener(View.OnClickListener listener) {
		_deleteImageButton.setOnClickListener(listener);
	}

}
