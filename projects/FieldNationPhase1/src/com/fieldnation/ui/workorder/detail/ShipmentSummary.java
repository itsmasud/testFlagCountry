package com.fieldnation.ui.workorder.detail;

import com.fieldnation.R;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;

public class ShipmentSummary extends RelativeLayout {

	public ShipmentSummary(Context context) {
		this(context, null, -1);
	}

	public ShipmentSummary(Context context, AttributeSet attrs) {
		this(context, attrs, -1);
	}

	public ShipmentSummary(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		LayoutInflater.from(context).inflate(R.layout.view_shipment_summary, this);

		if (isInEditMode())
			return;
	}

}
