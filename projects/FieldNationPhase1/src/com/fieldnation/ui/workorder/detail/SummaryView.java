package com.fieldnation.ui.workorder.detail;

import com.fieldnation.R;
import com.fieldnation.data.workorder.Workorder;
import com.fieldnation.utils.misc;

import android.content.Context;
import android.text.Html;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class SummaryView extends LinearLayout implements WorkorderRenderer {
	private static final String TAG = "ui.workorder.detail.SummaryView";

	// UI
	private TextView _substatusTextView;
	// private View _substatusProgress; // TODO, need to implement!
	private TextView _projectNameTextView;
	private TextView _workorderIdTextView;
	private TextView _worktypeTextView;
	private TextView _companyTextView;
	private TextView _descriptionTextView;
	private Button _confidentialButton;
	private Button _policiesButton;

	// Data
	private Workorder _workorder;
	private String[] _substatus;

	/*-*************************************-*/
	/*-				Life Cycle				-*/
	/*-*************************************-*/

	public SummaryView(Context context) {
		super(context);
		init();
	}

	public SummaryView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	private void init() {
		LayoutInflater.from(getContext()).inflate(R.layout.view_workorder_detail_sum, this);

		if (isInEditMode())
			return;

		_substatusTextView = (TextView) findViewById(R.id.substatus_textview);
		// _substatusProgress = findViewById(R.id.substatus_progress);
		_projectNameTextView = (TextView) findViewById(R.id.projectname_textview);
		_workorderIdTextView = (TextView) findViewById(R.id.workorderid_textview);
		_worktypeTextView = (TextView) findViewById(R.id.worktype_textview);
		_companyTextView = (TextView) findViewById(R.id.company_textview);
		_descriptionTextView = (TextView) findViewById(R.id.description_textview);

		_confidentialButton = (Button) findViewById(R.id.confidential_button);
		_policiesButton = (Button) findViewById(R.id.policies_button);

		_substatus = getResources().getStringArray(R.array.workorder_substatus);
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
		_substatusTextView.setText(_substatus[_workorder.getStatus().getWorkorderSubstatus().ordinal()]);
		// TODO set progress bar here
		_projectNameTextView.setText(_workorder.getTitle());

		_workorderIdTextView.setText("ID " + _workorder.getWorkorderId());

		_workorderIdTextView.setText(_workorder.getTypeOfWork());

		if (misc.isEmptyOrNull(_workorder.getCompanyName()))
			_companyTextView.setVisibility(GONE);
		else {
			_companyTextView.setVisibility(VISIBLE);
			_companyTextView.setText(_workorder.getCompanyName());
		}

		_descriptionTextView.setText(Html.fromHtml(_workorder.getFullWorkDescription()).toString());

		// TODO hook up policies
		// TODO hook up confidential info
	}

	/*-*********************************-*/
	/*-				Events				-*/
	/*-*********************************-*/
	private View.OnClickListener _confidential_onClick = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			// TODO Method Stub: onClick()
			Log.v(TAG, "Method Stub: onClick()");
		}
	};

	private View.OnClickListener _policies_onClick = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			// TODO Method Stub: onClick()
			Log.v(TAG, "Method Stub: onClick()");
		}
	};
}