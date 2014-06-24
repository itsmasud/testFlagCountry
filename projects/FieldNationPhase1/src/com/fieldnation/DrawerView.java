package com.fieldnation;

import com.fieldnation.R;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;

/**
 * This view defines what is in the pull out drawer, and what the buttons do.
 * 
 * @author michael.carver
 * 
 */
public class DrawerView extends RelativeLayout {
	private static final String TAG = "DrawerView";

	private RelativeLayout _myworkView;
	private RelativeLayout _marketView;
	private RelativeLayout _paymentView;
	private RelativeLayout _settingsView;
	private RelativeLayout _logoutView;

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
		final LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.view_drawer, this);

		if (isInEditMode())
			return;

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
	}

	/*-*********************************-*/
	/*-				Events				-*/
	/*-*********************************-*/
	private View.OnClickListener _myworkView_onClick = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			Intent intent = new Intent(getContext(), MyWorkActivity.class);
			getContext().startActivity(intent);
		}
	};

	private View.OnClickListener _marketView_onClick = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			Intent intent = new Intent(getContext(), MarketActivity.class);
			getContext().startActivity(intent);
		}
	};
	private View.OnClickListener _paymentView_onClick = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			// TODO Method Stub: onClick()
			Log.v(TAG, "Method Stub: onClick()");
		}
	};
	private View.OnClickListener _settingsView_onClick = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			Intent intent = new Intent(getContext(), SettingsActivity.class);
			getContext().startActivity(intent);
		}
	};
	private View.OnClickListener _logoutView_onClick = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			// TODO Method Stub: onClick()
			Log.v(TAG, "Method Stub: onClick()");
		}
	};

}
