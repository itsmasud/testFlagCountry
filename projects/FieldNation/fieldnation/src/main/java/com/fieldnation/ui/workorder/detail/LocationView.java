package com.fieldnation.ui.workorder.detail;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.util.Linkify;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fieldnation.GoogleAnalyticsTopicClient;
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
    private TextView _remoteTextView;

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
        _addressTextView.setOnClickListener(_openMapOnClick);

        _distanceTextView = (TextView) findViewById(R.id.distance_textview);
        _distanceTextView.setOnClickListener(_openMapOnClick);

        _remoteTextView = (TextView) findViewById(R.id.remotely_textview);

        _contactInfoTextView = (TextView) findViewById(R.id.contactinfo_textview);
        _descriptionTextView = (TextView) findViewById(R.id.description_textview);

        setVisibility(View.GONE);

    }

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

        String fullAddr = location.getFullAddressAndContactName();
        if (!misc.isEmptyOrNull(fullAddr)) {
            _addressTextView.setText(fullAddr);
            _addressTextView.setVisibility(View.VISIBLE);

        } else {
            _addressTextView.setVisibility(View.GONE);
        }

        _remoteTextView.setVisibility(View.GONE);
        if (_workorder.getIsRemoteWork()) {
            _remoteTextView.setVisibility(View.VISIBLE);
            _distanceTextView.setVisibility(View.GONE);
        } else if (_workorder.getDistance() != null) {
            _distanceTextView.setText(_workorder.getDistance() + " mi");
        } else if (location.getDistance() != null) {
            _distanceTextView.setText(location.getDistance() + " mi");
        }

        String contactInfo = "";
        if (!misc.isEmptyOrNull(location.getContactName())) {
            contactInfo += location.getContactName() + "\n";
        }
        if (!misc.isEmptyOrNull(location.getContactEmail())) {
            contactInfo += location.getContactEmail() + "\n";
        }
        if (!misc.isEmptyOrNull(location.getContactPhone())) {
            contactInfo += location.getContactPhone() + "\n";
        }

        contactInfo = contactInfo.trim();

        if (!misc.isEmptyOrNull(contactInfo)) {
            _contactInfoTextView.setText(contactInfo);
            Linkify.addLinks(_contactInfoTextView, Linkify.ALL);
            _contactInfoTextView.setVisibility(View.VISIBLE);
        } else {
            _contactInfoTextView.setVisibility(View.GONE);
        }

        if (!misc.isEmptyOrNull(location.getNotes())) {
            _descriptionTextView.setText(location.getNotes());
            Linkify.addLinks(_descriptionTextView, Linkify.ALL);
            _descriptionTextView.setVisibility(VISIBLE);
        } else {
            _descriptionTextView.setVisibility(GONE);
        }

        if (_addressTextView.getVisibility() != VISIBLE
                && _descriptionTextView.getVisibility() != VISIBLE
                && _contactInfoTextView.getVisibility() != VISIBLE
                && _distanceTextView.getVisibility() != VISIBLE
                && _remoteTextView.getVisibility() != VISIBLE) {
            setVisibility(View.GONE);
        } else {
            setVisibility(View.VISIBLE);
        }
    }

    public void showMap(Uri geoLocation) {
        Intent _intent = new Intent(Intent.ACTION_VIEW);
        _intent.setData(geoLocation);
        if (_intent.resolveActivity(getContext().getPackageManager()) != null) {
            getContext().startActivity(_intent);
        }
    }

    /*-*************************-*/
    /*-			Events			-*/
    /*-*************************-*/
    private View.OnClickListener _openMapOnClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (_workorder != null && !_workorder.getIsRemoteWork()) {
                Location location = _workorder.getLocation();
                if (location != null) {
                    try {
                        GoogleAnalyticsTopicClient
                                .dispatchEvent(getContext(), "WorkorderActivity",
                                        GoogleAnalyticsTopicClient.EventAction.START_MAP,
                                        "WorkFragment",
                                        1
                                );
                        String _fullAddress = misc.escapeForURL(location.getFullAddress());
                        String _uriString = "geo:0,0?q=" + _fullAddress;
                        Uri _uri = Uri.parse(_uriString);
                        showMap(_uri);
                    } catch (Exception e) {
                    }
                }
            }
        }
    };


}
