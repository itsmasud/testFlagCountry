package com.fieldnation.ui.workorder.detail;

import com.fieldnation.R;
import com.fieldnation.R.id;
import com.fieldnation.R.layout;
import com.fieldnation.data.workorder.Location;
import com.fieldnation.data.workorder.Workorder;
import com.fieldnation.ui.workorder.WorkorderRenderer;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

public class LocationView extends LinearLayout implements WorkorderRenderer {
	private static final String TAG = "WorkorderDetailLocationView";

	// UI
	private TextView _addressTextView;
	private TextView _distanceTextView;
	private TextView _contactInfoTextView;

	// Data
	private Workorder _workorder;

	/*-*************************************-*/
	/*-				Life Cycle				-*/
	/*-*************************************-*/

	public LocationView(Context context) {
		this(context, null);
	}

	public LocationView(Context context, AttributeSet attrs) {
		super(context, attrs);
		LayoutInflater.from(context).inflate(R.layout.view_workorder_detail_location, this);

		if (isInEditMode())
			return;

		_addressTextView = (TextView) findViewById(R.id.address_textview);
		_distanceTextView = (TextView) findViewById(R.id.distance_textview);
		_contactInfoTextView = (TextView) findViewById(R.id.contactinfo_textview);
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
		Location location = _workorder.getLocation();

		if (location == null) {
			// TODO, EPIC FAIL, AND A BAD SOLUTION, MAKE THIS BETTER
			this.setVisibility(GONE);
			return;
		}

		_addressTextView.setText(location.getFullAddress());

		if (_workorder.getDistance() != null) {
			_distanceTextView.setText(_workorder.getDistance() + " mi");
		} else if (location.getDistance() != null) {
			_distanceTextView.setText(location.getDistance() + " mi");
		}

		String contactInfo = "";

		contactInfo += location.getContactName() + "\n";
		contactInfo += location.getContactEmail() + "\n";
		contactInfo += location.getContactPhone();
		_contactInfoTextView.setText(contactInfo);

	}

}
