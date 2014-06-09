package com.fieldnation;

import com.fieldnation.R;
import com.fieldnation.R.id;
import com.fieldnation.R.layout;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;

public class DrawerView extends RelativeLayout {
	private RelativeLayout _workorderView;
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
		final LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.view_drawer, this);

		if (isInEditMode())
			return;

		_workorderView = (RelativeLayout) findViewById(R.id.workorder_view);
		_workorderView.setOnClickListener(_list);

		_marketView = (RelativeLayout) findViewById(R.id.market_view);
		_marketView.setOnClickListener(_list);

		_paymentView = (RelativeLayout) findViewById(R.id.payment_view);
		_paymentView.setOnClickListener(_list);

		_settingsView = (RelativeLayout) findViewById(R.id.settings_view);
		_settingsView.setOnClickListener(_list);

		_logoutView = (RelativeLayout) findViewById(R.id.logout_view);
		_logoutView.setOnClickListener(_list);
	}

	/*-*********************************-*/
	/*-				Events				-*/
	/*-*********************************-*/

	private View.OnClickListener _list = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			System.out.println("Method Stub: onClick()");

		}
	};

}
