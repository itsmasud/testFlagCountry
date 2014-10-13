package com.fieldnation.ui.workorder.detail;

import com.fieldnation.R;
import com.fieldnation.data.workorder.Location;
import com.fieldnation.data.workorder.Workorder;
import com.fieldnation.utils.misc;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

public class LocationView extends LinearLayout implements WorkorderRenderer {
	private static final String TAG = "ui.workorder.detail.LocationView";

	// UI
	private TextView _addressTextView;
	private TextView _distanceTextView;
	private TextView _contactInfoTextView;
	private TextView _descriptionTextView;

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
		LayoutInflater.from(context).inflate(R.layout.view_wd_location, this);

		if (isInEditMode())
			return;

		_addressTextView = (TextView) findViewById(R.id.address_textview);
		_distanceTextView = (TextView) findViewById(R.id.distance_textview);
		_contactInfoTextView = (TextView) findViewById(R.id.contactinfo_textview);
		_descriptionTextView = (TextView) findViewById(R.id.description_textview);
		setVisibility(View.GONE);
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
		String fullAddr = location.getFullAddress();
		if (!misc.isEmptyOrNull(fullAddr)) {
			_addressTextView.setText(fullAddr);
			_addressTextView.setVisibility(View.VISIBLE);
		} else {
			_addressTextView.setVisibility(View.GONE);
		}

		if (_workorder.getDistance() != null) {
			_distanceTextView.setText(_workorder.getDistance() + " mi");
		} else if (location.getDistance() != null) {
			_distanceTextView.setText(location.getDistance() + " mi");
		}

		String contactInfo = "";

		if (!misc.isEmptyOrNull(location.getContactName()) && !misc.isEmptyOrNull(location.getContactEmail()) && !misc.isEmptyOrNull(location.getContactPhone())) {
			contactInfo += location.getContactName() + "\n";
			contactInfo += location.getContactEmail() + "\n";
			contactInfo += location.getContactPhone();
			_contactInfoTextView.setText(contactInfo);
			_contactInfoTextView.setVisibility(View.VISIBLE);
		} else {
			_contactInfoTextView.setVisibility(View.GONE);
		}

		if (location.getNotes() != null) {
			_descriptionTextView.setText(location.getNotes());
			_descriptionTextView.setVisibility(VISIBLE);
		} else {
			_descriptionTextView.setVisibility(GONE);
		}
		setVisibility(View.VISIBLE);
	}

}
