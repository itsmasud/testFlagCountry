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
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class SummaryView extends LinearLayout implements WorkorderRenderer {
	private static final String TAG = "ui.workorder.detail.SummaryView";

	// UI
	private WoProgressBar _progress;
	private TextView _projectNameTextView;
	private TextView _workorderIdTextView;
	private TextView _worktypeTextView;
	private TextView _companyTextView;
	private TextView _descriptionTextView;
	private TextView _confidentialTextView;
	private TextView _policiesTextView;
	private LinearLayout _contentLayout;
	private RelativeLayout _loadingLayout;

	// Data
	private Workorder _workorder;

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
		LayoutInflater.from(getContext()).inflate(R.layout.view_wd_sum, this);

		if (isInEditMode())
			return;

		_contentLayout = (LinearLayout) findViewById(R.id.content_layout);
		_progress = (WoProgressBar) findViewById(R.id.substatus_progressbar);
		_projectNameTextView = (TextView) findViewById(R.id.projectname_textview);
		_workorderIdTextView = (TextView) findViewById(R.id.workorderid_textview);
		_worktypeTextView = (TextView) findViewById(R.id.worktype_textview);
		_companyTextView = (TextView) findViewById(R.id.company_textview);
		_descriptionTextView = (TextView) findViewById(R.id.description_textview);

		_confidentialTextView = (TextView) findViewById(R.id.confidential_textview);
		_policiesTextView = (TextView) findViewById(R.id.policies_textview);
		_loadingLayout = (RelativeLayout) findViewById(R.id.loading_layout);

		setVisibility(View.GONE);
	}

	/*-*************************************-*/
	/*-				Mutators				-*/
	/*-*************************************-*/

	@Override
	public void setWorkorder(Workorder workorder) {
		_workorder = workorder;
		_workorder.addListener(_workorder_listener);

		refresh();
	}

	private void setLoading(boolean isLoading) {
		// if (isLoading) {
		// _loadingLayout.setVisibility(View.VISIBLE);
		// _contentLayout.setVisibility(View.GONE);
		// } else {
		// _loadingLayout.setVisibility(View.GONE);
		// _contentLayout.setVisibility(View.VISIBLE);
		// }
	}

	private void refresh() {
		_progress.setSubstatus(_workorder.getStatus().getWorkorderSubstatus());
		_projectNameTextView.setText(_workorder.getTitle());

		_workorderIdTextView.setText("ID " + _workorder.getWorkorderId());

		if (misc.isEmptyOrNull(_workorder.getCompanyName()))
			_companyTextView.setVisibility(GONE);
		else {
			_companyTextView.setVisibility(VISIBLE);
			_companyTextView.setText(_workorder.getCompanyName());
		}

		_descriptionTextView.setText(Html.fromHtml(_workorder.getFullWorkDescription()).toString());

		_worktypeTextView.setText(_workorder.getTypeOfWork());
		setLoading(false);
		setVisibility(View.VISIBLE);

		// TODO hook up policies
		// TODO hook up confidential info
	}

	/*-*********************************-*/
	/*-				Events				-*/
	/*-*********************************-*/
	private Workorder.Listener _workorder_listener = new Workorder.Listener() {
		@Override
		public void onChange(Workorder workorder) {
			setLoading(true);
		}
	};

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