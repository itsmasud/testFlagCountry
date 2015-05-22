package com.fieldnation.ui.workorder.detail;

import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fieldnation.GoogleAnalyticsTopicClient;
import com.fieldnation.R;
import com.fieldnation.data.workorder.Location;
import com.fieldnation.data.workorder.Workorder;
import com.fieldnation.utils.misc;
import com.mapbox.mapboxsdk.geometry.BoundingBox;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.overlay.Marker;
import com.mapbox.mapboxsdk.views.MapView;

import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

public class LocationView extends LinearLayout implements WorkorderRenderer {
    private static final String TAG = "ui.workorder.detail.LocationView";

    // UI
    private MapView _mapView;
    private View _clickOverlay;
    private TextView _addressTextView;
    private TextView _distanceTextView;
    private Button _navigateButton;

/*
    private TextView _addressTextView;
    private TextView _distanceTextView;
    private TextView _contactInfoTextView;
    private TextView _descriptionTextView;
    private TextView _remoteTextView;
*/

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

        _mapView = (MapView) findViewById(R.id.mapview);

        _clickOverlay = findViewById(R.id.click_overlay);
        _clickOverlay.setOnClickListener(_map_onClick);

        _addressTextView = (TextView) findViewById(R.id.address_textview);

        _distanceTextView = (TextView) findViewById(R.id.distance_textview);

        _navigateButton = (Button) findViewById(R.id.navigate_button);
        _navigateButton.setOnClickListener(_navigate_onClick);

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
            setVisibility(GONE);
            return;
        }

        setVisibility(VISIBLE);

        try {
            Geocoder geocoder = new Geocoder(getContext(), Locale.getDefault());

            List<Address> addrs = geocoder.getFromLocationName(location.getFullAddress(), 1);

            LatLng ll = new LatLng(addrs.get(0).getLatitude(), addrs.get(0).getLongitude());

//            _mapView.setCenter(ll);
            _mapView.setUserLocationEnabled(true);

            Marker marker = new Marker(_mapView, "Work", "Work here dammit!", ll);
            marker.setMarker(getResources().getDrawable(R.drawable.ic_check));
            _mapView.addMarker(marker);

            Set<LatLng> lls = new HashSet<>();

            lls.add(ll);
            lls.add(_mapView.getUserLocation());

            _mapView.zoomToBoundingBox(BoundingBox.fromLatLngs(lls), true, false, true);

            _addressTextView.setText(location.getFullAddressOneLine());
            _distanceTextView.setText(misc.toCurrency(ll.distanceTo(_mapView.getUserLocation()) * 0.000621371).substring(1) + " miles");

        } catch (Exception ex) {
            ex.printStackTrace();
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
    private final View.OnClickListener _navigate_onClick = new View.OnClickListener() {
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
                        String _uriString = "google.navigation:q=" + _fullAddress;
                        Uri _uri = Uri.parse(_uriString);
                        showMap(_uri);
                    } catch (Exception e) {
                    }
                }
            }
        }
    };

    private final View.OnClickListener _map_onClick = new View.OnClickListener() {
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
