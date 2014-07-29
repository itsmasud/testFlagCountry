package com.fieldnation.ui;

import java.util.Calendar;

import com.fieldnation.GlobalState;
import com.fieldnation.R;
import com.fieldnation.auth.client.AuthenticationClient;
import com.fieldnation.data.accounting.Payment;
import com.fieldnation.json.JsonArray;
import com.fieldnation.rpc.client.PaymentService;
import com.fieldnation.rpc.common.WebServiceConstants;
import com.fieldnation.rpc.common.WebServiceResultReceiver;
import com.fieldnation.ui.market.MarketActivity;
import com.fieldnation.ui.payment.PaymentListActivity;
import com.fieldnation.ui.workorder.MyWorkActivity;
import com.fieldnation.utils.ISO8601;
import com.fieldnation.utils.misc;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * This view defines what is in the pull out drawer, and what the buttons do.
 * 
 * @author michael.carver
 * 
 */
public class DrawerView extends RelativeLayout {
	private static final String TAG = "ui.DrawerView";

	// UI
	private RelativeLayout _myworkView;
	private RelativeLayout _marketView;
	private RelativeLayout _paymentView;
	private RelativeLayout _settingsView;
	private RelativeLayout _logoutView;
	private TextView _paidAmountTextView;
	private TextView _paidDateTextView;
	private TextView _estimatedAmountTextView;
	private TextView _estimatedDateTextView;
	private RelativeLayout _estimatedLayout;
	private RelativeLayout _paidLayout;

	// Data
	private GlobalState _gs;
	private PaymentService _dataService;
	private boolean _hasPaid = false;
	private boolean _hasEstimated = false;
	private double _paidAmount = 0;
	private long _lastPaidDate = Long.MIN_VALUE;
	private double _estimatedAmount = 0;
	private long _lastEstimatedDate = Long.MIN_VALUE;
	private int _nextPage = 0;

	/*-*************************************-*/
	/*-				Life Cycle				-*/
	/*-*************************************-*/
	public DrawerView(Context context) {
		this(context, null, -1);
	}

	public DrawerView(Context context, AttributeSet attrs) {
		this(context, attrs, -1);
	}

	public DrawerView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		LayoutInflater.from(context).inflate(R.layout.view_drawer, this);

		if (isInEditMode())
			return;

		_gs = (GlobalState) context.getApplicationContext();
		_gs.requestAuthentication(_authClient);

		_myworkView = (RelativeLayout) findViewById(R.id.mywork_view);
		_myworkView.setOnClickListener(_myworkView_onClick);

		_marketView = (RelativeLayout) findViewById(R.id.market_view);
		_marketView.setOnClickListener(_marketView_onClick);

		_paymentView = (RelativeLayout) findViewById(R.id.payment_view);
		_paymentView.setOnClickListener(_paymentView_onClick);

		_settingsView = (RelativeLayout) findViewById(R.id.settings_view);
		_settingsView.setOnClickListener(_settingsView_onClick);

		_logoutView = (RelativeLayout) findViewById(R.id.logout_view);
		_logoutView.setOnClickListener(_logoutView_onClick);

		_paidLayout = (RelativeLayout) findViewById(R.id.paid_layout);
		_paidAmountTextView = (TextView) findViewById(R.id.paidamount_textview);
		_paidDateTextView = (TextView) findViewById(R.id.paiddate_textview);

		_estimatedLayout = (RelativeLayout) findViewById(R.id.estimated_layout);
		_estimatedAmountTextView = (TextView) findViewById(R.id.estimatedamount_textview);
		_estimatedDateTextView = (TextView) findViewById(R.id.estimateddate_textview);

	}

	/*-*********************************-*/
	/*-				Events				-*/
	/*-*********************************-*/
	private View.OnClickListener _myworkView_onClick = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			Intent intent = new Intent(getContext(), MyWorkActivity.class);
			getContext().startActivity(intent);
			attachAnimations();
		}
	};
	private View.OnClickListener _marketView_onClick = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			Intent intent = new Intent(getContext(), MarketActivity.class);
			getContext().startActivity(intent);
			attachAnimations();
		}
	};
	private View.OnClickListener _paymentView_onClick = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			Intent intent = new Intent(getContext(), PaymentListActivity.class);
			getContext().startActivity(intent);
			attachAnimations();
		}
	};
	private View.OnClickListener _settingsView_onClick = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			Intent intent = new Intent(getContext(), SettingsActivity.class);
			getContext().startActivity(intent);
			attachAnimations();
		}
	};
	private View.OnClickListener _logoutView_onClick = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			_gs.requestRemoveAccount(_authClient);
		}
	};

	private AuthenticationClient _authClient = new AuthenticationClient() {

		@Override
		public void onAuthenticationFailed(Exception ex) {
			Log.v(TAG, "onAuthenticationFailed(), delayed re-request");
			_gs.requestAuthenticationDelayed(_authClient);
		}

		@Override
		public void onAuthentication(String username, String authToken) {
			_dataService = new PaymentService(getContext(), username, authToken, _resultReciever);

			getContext().startService(_dataService.getAll(1, 0, true));
			_nextPage = 1;
		}

		@Override
		public GlobalState getGlobalState() {
			return _gs;
		}

		public void onAccountRemoved(boolean status) {
			Log.v(TAG, "Account removed: " + status);
		};
	};

	private WebServiceResultReceiver _resultReciever = new WebServiceResultReceiver(new Handler()) {
		@Override
		public void onSuccess(int resultCode, Bundle resultData) {
			try {
				JsonArray ja = new JsonArray(new String(resultData.getByteArray(WebServiceConstants.KEY_RESPONSE_DATA)));

				for (int i = 0; i < ja.size(); i++) {
					Payment payment = Payment.fromJson(ja.getJsonObject(i));

					if (payment == null) {
						continue;
					}

					long date = ISO8601.toUtc(payment.getDatePaid());

					if ("paid".equals(payment.getStatus())) {
						if (date >= _lastPaidDate) {
							_lastPaidDate = date;
							_paidAmount = payment.getAmount();
							_hasPaid = true;
						}
					} else {
						if (date >= _lastEstimatedDate) {
							_lastEstimatedDate = date;
							_estimatedAmount = payment.getAmount();
							_hasEstimated = true;
						}
					}
				}

				if (ja.size() == 0) {
					if (_hasEstimated) {
						_estimatedLayout.setVisibility(VISIBLE);
						_estimatedAmountTextView.setText(misc.toCurrency(_estimatedAmount));
						Calendar cal = Calendar.getInstance();
						cal.setTimeInMillis(_lastEstimatedDate);
						_estimatedDateTextView.setText("Estimated " + misc.formatDate(cal));
					} else {
						_estimatedLayout.setVisibility(GONE);
					}

					if (_hasPaid) {
						_paidLayout.setVisibility(VISIBLE);
						_paidAmountTextView.setText(misc.toCurrency(_paidAmount));
						Calendar cal = Calendar.getInstance();
						cal.setTimeInMillis(_lastPaidDate);
						_paidDateTextView.setText("Paid " + misc.formatDate(cal));
					} else {
						_paidLayout.setVisibility(GONE);
					}
				} else {
					getContext().startService(_dataService.getAll(1, _nextPage, true));
					_nextPage++;
				}
			} catch (Exception e) {
				getContext().startService(_dataService.getAll(1, 0, true));
				_nextPage = 1;
			}
			Log.v(TAG, "WebServiceResultReceiver.onSuccess");
		}

		@Override
		public void onError(int resultCode, Bundle resultData, String errorType) {
			// TODO Method Stub: onError()
			Log.v(TAG, "Method Stub: onError()");
		}
	};

	private void attachAnimations() {
		Context context = getContext();
		if (context instanceof Activity) {
			((Activity) context).overridePendingTransition(R.anim.activity_slide_in, 0);
		}

	}
}
