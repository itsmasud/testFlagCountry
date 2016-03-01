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
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fieldnation.AsyncTaskEx;
import com.fieldnation.GoogleAnalyticsTopicClient;
import com.fieldnation.Log;
import com.fieldnation.R;
import com.fieldnation.data.workorder.Location;
import com.fieldnation.data.workorder.Workorder;
import com.fieldnation.ui.IconFontTextView;
import com.fieldnation.utils.Stopwatch;
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
    private static final String TAG = "LocationView";

    // UI
    private MapView _mapView;
    private View _clickOverlay;
    private TextView _siteTitleTextView;
    private TextView _addressTextView;
    private IconFontTextView _locationIconTextView;
    private TextView _locationTypeTextView;
    private TextView _distanceTextView;
    private TextView _noteShortTextView;
    private TextView _noteLongTextView;
    private Button _readMoreButton;
    private Button _navigateButton;
    private RelativeLayout _mapLayout;
    private TextView _noLocationTextView;
    private LinearLayout _addressLayout;
    private RelativeLayout _noMapLayout;

    // Data
    private Workorder _workorder;
    private boolean _isDrawn = false;

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

        _siteTitleTextView = (TextView) findViewById(R.id.siteTitle_textview);

        _addressTextView = (TextView) findViewById(R.id.address_textview);

        _locationIconTextView = (IconFontTextView) findViewById(R.id.locationIcon_textview);

        _locationTypeTextView = (TextView) findViewById(R.id.locationType_textview);

        _distanceTextView = (TextView) findViewById(R.id.distance_textview);

        _noteShortTextView = (TextView) findViewById(R.id.noteShort_textview);

        _noteLongTextView = (TextView) findViewById(R.id.noteLong_textview);

        _readMoreButton = (Button) findViewById(R.id.readMore_button);
        _readMoreButton.setOnClickListener(_readMore_onClick);

        _navigateButton = (Button) findViewById(R.id.navigate_button);
        _navigateButton.setOnClickListener(_navigate_onClick);

        _mapLayout = (RelativeLayout) findViewById(R.id.map_layout);

        _noLocationTextView = (TextView) findViewById(R.id.noLocation_textview);

        _addressLayout = (LinearLayout) findViewById(R.id.address_layout);
        _addressLayout.setOnClickListener(_map_onClick);

        _noMapLayout = (RelativeLayout) findViewById(R.id.noMap_layout);

        setVisibility(View.GONE);
    }

    @Override
    public void setWorkorder(Workorder workorder) {
        try {
            if (_workorder == null || workorder == null)
                _isDrawn = false;
        } catch (Exception ex) {
            _isDrawn = false;
            Log.v(TAG, ex);
        }

        _workorder = workorder;
        populateUi();
    }

    private void populateUi() {
        Stopwatch stopwatch = new Stopwatch(true);
        if (_workorder == null)
            return;

        if (_mapView == null)
            return;

        _addressLayout.setBackgroundColor(getResources().getColor(R.color.fn_clickable_bg));
        _distanceTextView.setText("Could not calculate distance.");
        _noMapLayout.setVisibility(GONE);

        setVisibility(VISIBLE);

        Location location = _workorder.getLocation();
        if (location != null) {
            String title = "";
            if (!misc.isEmptyOrNull(location.getGroupName())) {
                title = location.getGroupName().trim() + "/";
            }
            if (!misc.isEmptyOrNull(location.getName())) {
                title += location.getName();
            }
            if (!misc.isEmptyOrNull(title)) {
                _siteTitleTextView.setText(title);
                _siteTitleTextView.setVisibility(VISIBLE);
            } else {
                _siteTitleTextView.setVisibility(GONE);
            }
            _addressTextView.setText(location.getFullAddressOneLine());

            if (location.getType() != null) {
                _locationTypeTextView.setVisibility(VISIBLE);
                _locationIconTextView.setVisibility(VISIBLE);
                switch (location.getType()) {
                    case "Commercial":
                        _locationIconTextView.setText(R.string.icon_commercial);
                        _locationTypeTextView.setText("Commercial");
                        break;
                    case "Government":
                        _locationIconTextView.setText(R.string.icon_government);
                        _locationTypeTextView.setText("Government");
                        break;
                    case "Residential":
                        _locationIconTextView.setText(R.string.icon_house);
                        _locationTypeTextView.setText("Residential");
                        break;
                    case "Educational":
                        _locationIconTextView.setText(R.string.icon_educational);
                        _locationTypeTextView.setText("Educational");
                        break;
                    default:
                        _locationTypeTextView.setVisibility(GONE);
                        _locationIconTextView.setVisibility(GONE);
                        break;
                }
            } else {
                _locationTypeTextView.setVisibility(GONE);
                _locationIconTextView.setVisibility(GONE);
            }

            if (misc.isEmptyOrNull(location.getNotes())) {
                _noteShortTextView.setVisibility(GONE);
                _noteLongTextView.setVisibility(GONE);
                _readMoreButton.setVisibility(GONE);
            } else {
                _noteShortTextView.setVisibility(VISIBLE);
                _readMoreButton.setVisibility(VISIBLE);
                _readMoreButton.setText(R.string.btn_read_more);
                _noteShortTextView.setText(location.getNotes());
                _noteLongTextView.setText(location.getNotes());
            }
        }

        if (location == null || _workorder.getIsRemoteWork()) {
            _mapView.setVisibility(GONE);
            _mapLayout.setVisibility(GONE);
            _noLocationTextView.setVisibility(VISIBLE);
            _addressLayout.setVisibility(GONE);
            _navigateButton.setVisibility(GONE);
            Log.v(TAG, "no location time: " + stopwatch.finish());
            return;
        } else {
            _mapView.setVisibility(VISIBLE);
            _mapLayout.setVisibility(VISIBLE);
            _noLocationTextView.setVisibility(GONE);
            _addressLayout.setVisibility(VISIBLE);
            _navigateButton.setVisibility(VISIBLE);
        }

        new AsyncTaskEx<Object, Object, LatLng>() {
            @Override
            protected LatLng doInBackground(Object... params) {
                Context context = (Context) params[0];
                try {
                    if (_isDrawn)
                        return null;

                    // _isDrawn = true;

                    Stopwatch stopwatch = new Stopwatch(true);

                    Location location = _workorder.getLocation();
                    // get address location
                    Geocoder geocoder = new Geocoder(context, Locale.getDefault());
                    List<Address> addrs = geocoder.getFromLocationName(location.getFullAddressOneLine(), 1);
                    if (addrs == null || addrs.size() == 0) {
                        // can't get a location, should render accordingly
                        Log.v(TAG, "inBackground time: " + stopwatch.finish());
                        return null;
                    }

                    Log.v(TAG, "inBackground time: " + stopwatch.finish());
                    return new LatLng(addrs.get(0).getLatitude(), addrs.get(0).getLongitude());
                } catch (Exception ex) {
                    Log.v(TAG, ex);
                }
                return null;
            }

            @Override
            protected void onPostExecute(LatLng destination) {
                Stopwatch watch = new Stopwatch(true);
                _mapView.clear();

                try {
                    if (destination != null) {
                        Log.v(TAG, "Getting user location");
                        _mapView.setUserLocationEnabled(true);
                        LatLng user = _mapView.getUserLocation();
                        _mapView.setUserLocationEnabled(false);
                        Log.v(TAG, "Getting user location done");

                        Marker marker = new Marker(_mapView, "Work", "", destination);
                        marker.setMarker(getResources().getDrawable(R.drawable.ic_location_pin));
                        _mapView.addMarker(marker);

                        marker = new Marker(_mapView, "Me", "", user);
                        marker.setMarker(getResources().getDrawable(R.drawable.ic_user_location));
                        _mapView.addMarker(marker);

                        Set<LatLng> lls = new HashSet<>();
                        lls.add(destination);
                        lls.add(user);

                        _mapView.zoomToBoundingBox(BoundingBox.fromLatLngs(lls), true, true, false);
                        _mapView.setZoom(_mapView.getZoomLevel() - 1);

                        _distanceTextView.setText(misc.to2Decimal(destination.distanceTo(user) * 0.000621371) + " miles");
                        _addressLayout.setBackgroundColor(getResources().getColor(R.color.fn_transparent));
                    } else {
                        _navigateButton.setVisibility(GONE);
                        _mapLayout.setVisibility(GONE);
                    }
                } catch (Exception ex) {
                    Log.v(TAG, ex);
                    _noMapLayout.setVisibility(VISIBLE);
                    _mapView.setVisibility(GONE);
                }
                Log.v(TAG, "onPostExecute time: " + watch.finish());
            }
        }.executeEx(getContext());
        Log.v(TAG, "populateUi time: " + stopwatch.finish());
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
    private final View.OnClickListener _readMore_onClick = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            if (_noteShortTextView.getVisibility() == VISIBLE) {
                _noteLongTextView.setVisibility(View.VISIBLE);
                _noteShortTextView.setVisibility(View.GONE);
                _readMoreButton.setText(R.string.btn_read_less);
            } else {
                _noteLongTextView.setVisibility(View.GONE);
                _noteShortTextView.setVisibility(View.VISIBLE);
                _readMoreButton.setText(R.string.btn_read_more);
            }
        }
    };

    private final View.OnClickListener _navigate_onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Log.v(TAG, "_navigate_onClick");
            if (_workorder != null && !_workorder.getIsRemoteWork()) {
                Location location = _workorder.getLocation();
                if (location != null) {
                    try {
                        GoogleAnalyticsTopicClient
                                .dispatchEvent(getContext(), "WorkorderActivity",
                                        GoogleAnalyticsTopicClient.EventAction.START_MAP,
                                        "WorkFragment", 1);
                        String _fullAddress = misc.escapeForURL(location.getFullAddressOneLine());
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
            Log.v(TAG, "_map_onClick");
            if (_workorder != null && !_workorder.getIsRemoteWork()) {
                Location location = _workorder.getLocation();
                if (location != null) {
                    try {
                        GoogleAnalyticsTopicClient
                                .dispatchEvent(getContext(), "WorkorderActivity",
                                        GoogleAnalyticsTopicClient.EventAction.START_MAP,
                                        "WorkFragment", 1);
                        String _fullAddress = misc.escapeForURL(location.getFullAddressOneLine());
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
