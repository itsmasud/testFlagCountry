package com.fieldnation.ui.workorder.detail;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fieldnation.R;
import com.fieldnation.data.workorder.Location;
import com.fieldnation.data.workorder.Workorder;
import com.fieldnation.utils.misc;

public class LocationView extends LinearLayout implements WorkorderRenderer {
    private static final String TAG = "ui.workorder.detail.LocationView";

    // UI
    private TextView _addressTextView;
    private TextView _distanceTextView;
    private TextView _contactInfoTextView;
    private TextView _descriptionTextView;
    private TextView _openMapLink;

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

        _openMapLink = (TextView) findViewById(R.id.open_map_button);
        _openMapLink.setOnClickListener(_openMapOnClick);

        setVisibility(View.GONE);

    }

    /*-*************************-*/
	/*-			Events			-*/
	/*-*************************-*/
    private View.OnClickListener _openMapOnClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(_workorder != null){
                Location location = _workorder.getLocation();
                if(location != null){
                    try{
                        //@TODO
                        /*double latitude_source = -93.4973;
                        double longitude_source = 45.0935;
                        double latitude_dest = -93.2413;
                        double longitude_dest = 45.0023;
                        String uri = "http://maps.google.com/maps?saddr="+latitude_source+","+longitude_source+"(Y)&daddr="+latitude_dest+","+longitude_dest+"(W)";
                        */
                        showMap(location.getDistanceMapUrl());
                    } catch (Exception e){}
                }
            }
        }
    };

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

        // Todo should be clickable, load google maps
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
        if (!misc.isEmptyOrNull(location.getContactName())) {
            contactInfo += location.getContactName() + "\n";
        }
        // Todo this should be clickable
        if (!misc.isEmptyOrNull(location.getContactEmail())) {
            contactInfo += location.getContactEmail() + "\n";
        }
        // Todo this should be clickable
        if (!misc.isEmptyOrNull(location.getContactPhone())) {
            contactInfo += location.getContactPhone() + "\n";
        }

        contactInfo = contactInfo.trim();

        if (!misc.isEmptyOrNull(contactInfo)) {
            _contactInfoTextView.setText(contactInfo);
            _contactInfoTextView.setVisibility(View.VISIBLE);
        } else {
            _contactInfoTextView.setVisibility(View.GONE);
        }

        if (!misc.isEmptyOrNull(location.getNotes())) {
            _descriptionTextView.setText(location.getNotes());
            _descriptionTextView.setVisibility(VISIBLE);
        } else {
            _descriptionTextView.setVisibility(GONE);
        }

        if(_workorder.getIsRemoteWork()){
           // _openMapButton.setVisibility(GONE);
        }

        setVisibility(View.VISIBLE);

    }

    public void showMap(String geoLocation) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(geoLocation));
        getContext().startActivity(intent);
    }

}
