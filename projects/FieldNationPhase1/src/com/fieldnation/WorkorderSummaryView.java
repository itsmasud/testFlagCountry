package com.fieldnation;

import java.text.ParseException;

import com.fieldnation.json.JsonObject;
import com.fieldnation.service.rpc.WorkorderRpc;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class WorkorderSummaryView extends RelativeLayout {
	// UI
	private View _statusView;
	private TextView _titleTextView;
	private TextView _clientNameTextView;
	private Button _detailButton;
	private TextView _cashTextView;
	private LinearLayout _cashLinearLayout;
	private TextView _basisTextView;

	// Data
	private GlobalState _gs;
	private int _state = 0;
	private AttributeSet _attrs;

	private JsonObject _workorder;

	// state lookuptable
	private static final int[] _STATUS_LOOKUP_TABLE = {
			R.drawable.wosum_status_1_no_highlight,
			R.drawable.wosum_status_2_no_highlight,
			R.drawable.wosum_status_3_no_highlight,
			R.drawable.wosum_status_4_no_highlight,
			R.drawable.wosum_status_5_no_highlight,
			R.drawable.wosum_status_6_no_highlight,
			R.drawable.wosum_status_7_no_highlight,
			R.drawable.wosum_status_8_no_highlight,
			R.drawable.wosum_status_9_no_highlight };

	public WorkorderSummaryView(Context context) {
		this(context, null, -1);
	}

	public WorkorderSummaryView(Context context, AttributeSet attrs) {
		this(context, attrs, -1);
	}

	public WorkorderSummaryView(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);
		final LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.view_workorder_summary, this);

		if (isInEditMode())
			return;

		_gs = (GlobalState) getContext().getApplicationContext();

		_statusView = findViewById(R.id.status_view);

		_titleTextView = (TextView) findViewById(R.id.title_textview);
		_clientNameTextView = (TextView) findViewById(R.id.clientname_textview);

		_detailButton = (Button) findViewById(R.id.detail_button);
		_detailButton.setOnClickListener(_detailButton_onClick);

		_cashLinearLayout = (LinearLayout) findViewById(R.id.payment_linearlayout);
		_cashTextView = (TextView) findViewById(R.id.payment_textview);
		_basisTextView = (TextView) findViewById(R.id.basis_textview);

	}

	/*-*********************************-*/
	/*-				Events				-*/
	/*-*********************************-*/
	private View.OnClickListener _detailButton_onClick = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			_state = (_state + 1) % 9;

			_statusView.setBackgroundResource(_STATUS_LOOKUP_TABLE[_state]);

			// TODO Method Stub: onClick()
			System.out.println("Method Stub: onClick()");

		}
	};

	private ResultReceiver _rpcReceiver = new ResultReceiver(new Handler()) {
		@Override
		protected void onReceiveResult(int resultCode, Bundle resultData) {
			if (resultCode == 1) {
				// getDetails
				
			}
			// TODO Method Stub: onReceiveResult()
			System.out.println("Method Stub: onReceiveResult()");
			super.onReceiveResult(resultCode, resultData);
		}
	};

	/*-*********************************-*/
	/*-				Data				-*/
	/*-*********************************-*/
	public void setWorkorder(JsonObject workorder) {
		_workorder = workorder;
		refresh();
	}

	/*-*********************************-*/
	/*-				Util				-*/
	/*-*********************************-*/
	private void refresh() {

		try {
			WorkorderRpc.getDetails(getContext(), _rpcReceiver, 1, _gs.accessToken,
					_workorder.getLong("workorder_id"));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
