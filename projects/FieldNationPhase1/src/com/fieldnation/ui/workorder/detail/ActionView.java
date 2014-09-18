package com.fieldnation.ui.workorder.detail;

import com.fieldnation.R;
import com.fieldnation.data.workorder.Workorder;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

public class ActionView extends RelativeLayout implements WorkorderRenderer {
	private static final String TAG = "ui.workorder.detail.ActionView";

	// UI
	private Button _requestButton;
	private Button _notInterestedButton;
	private Button _counterOfferButton;
	private Button _completeButton;

	// DATA

	public ActionView(Context context) {
		super(context);
		init();
	}

	public ActionView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public ActionView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	private void init() {
		LayoutInflater.from(getContext()).inflate(R.layout.view_wd_actions, this);

		if (isInEditMode())
			return;

		_requestButton = (Button) findViewById(R.id.request_button);
		_notInterestedButton = (Button) findViewById(R.id.not_interested_button);
		_counterOfferButton = (Button) findViewById(R.id.counteroffer_button);
		_completeButton = (Button) findViewById(R.id.complete_button);
	}

	public void setRequestOnClickListener(View.OnClickListener listener) {
		_requestButton.setOnClickListener(listener);
	}

	public void setNotInterestedOnClickListener(View.OnClickListener listener) {
		_notInterestedButton.setOnClickListener(listener);
	}

	public void setCounterOfferOnClickListener(View.OnClickListener listener) {
		_counterOfferButton.setOnClickListener(listener);
	}

	public void setCompleteOnClickListener(View.OnClickListener listener) {
		_completeButton.setOnClickListener(listener);
	}

	@Override
	public void setWorkorder(Workorder workorder) {
		Log.v(TAG, "Method Stub: setWorkorder()");

	}
}
