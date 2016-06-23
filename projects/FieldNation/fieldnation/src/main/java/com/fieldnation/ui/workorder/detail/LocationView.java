package com.fieldnation.ui.workorder.detail;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fieldnation.App;
import com.fieldnation.GoogleAnalyticsTopicClient;
import com.fieldnation.Log;
import com.fieldnation.R;
import com.fieldnation.SimpleGps;
import com.fieldnation.data.workorder.Geo;
import com.fieldnation.data.workorder.Location;
import com.fieldnation.data.workorder.Workorder;
import com.fieldnation.service.data.mapbox.MapboxClient;
import com.fieldnation.service.data.mapbox.Marker;
import com.fieldnation.ui.IconFontTextView;
import com.fieldnation.utils.Stopwatch;
import com.fieldnation.utils.misc;

public class LocationView extends LinearLayout implements WorkorderRenderer {
    private static final String TAG = "LocationView";

    // UI
    private TextView _noLocationTextView;

    private RelativeLayout _mapLayout;
    private ImageView _mapImageView;
    private ProgressBar _loadingProgress;

    private LinearLayout _noMapLayout;

    private LinearLayout _addressLayout;
    private TextView _siteTitleTextView;
    private TextView _addressTextView;
    private IconFontTextView _locationIconTextView;
    private TextView _locationTypeTextView;

    private TextView _distanceTextView;
    private TextView _noteTextView;

    private Button _navigateButton;

    // Data
    private Workorder _workorder;
    private boolean _isDrawn = false;
    private android.location.Location _userLocation = null;
    private Bitmap _map = null;
    private boolean _mapUnavailable = false;

    // Services
    private MapboxClient _mapboxClient = null;

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

        _noLocationTextView = (TextView) findViewById(R.id.noLocation_textview);

        _mapLayout = (RelativeLayout) findViewById(R.id.map_layout);
        _mapImageView = (ImageView) findViewById(R.id.mapview);
        _loadingProgress = (ProgressBar) findViewById(R.id.loading_progress);

        _noMapLayout = (LinearLayout) findViewById(R.id.noMap_layout);

        _addressLayout = (LinearLayout) findViewById(R.id.address_layout);
        _addressLayout.setOnClickListener(_map_onClick);

        _siteTitleTextView = (TextView) findViewById(R.id.siteTitle_textview);
        _addressTextView = (TextView) findViewById(R.id.address_textview);
        _locationIconTextView = (IconFontTextView) findViewById(R.id.locationIcon_textview);
        _locationTypeTextView = (TextView) findViewById(R.id.locationType_textview);

        _distanceTextView = (TextView) findViewById(R.id.distance_textview);
        _noteTextView = (TextView) findViewById(R.id.note_textview);

        _navigateButton = (Button) findViewById(R.id.navigate_button);
        _navigateButton.setOnClickListener(_navigate_onClick);

        _mapboxClient = new MapboxClient(_mapboxClient_listener);
        _mapboxClient.connect(App.get());

        SimpleGps.with(getContext()).start(_gpsListener);

        setVisibility(View.GONE);

        getViewTreeObserver().addOnGlobalLayoutListener(_layoutObserver);
    }

    private final ViewTreeObserver.OnGlobalLayoutListener _layoutObserver = new ViewTreeObserver.OnGlobalLayoutListener() {
        @Override
        public void onGlobalLayout() {
            lookupMap();
            getViewTreeObserver().removeGlobalOnLayoutListener(_layoutObserver);
        }
    };


    @Override
    protected void onDetachedFromWindow() {
        if (_mapboxClient != null && _mapboxClient.isConnected()) {
            _mapboxClient.disconnect(App.get());
        }
        super.onDetachedFromWindow();
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

        if (_mapboxClient != null && _mapboxClient.isConnected()) {
            _mapboxClient.subStaticMapClassic(_workorder.getWorkorderId());
            lookupMap();
        }
        populateUi();
    }

    private void populateUi() {
        Stopwatch stopwatch = new Stopwatch(true);
        if (_workorder == null)
            return;

        if (_mapImageView == null)
            return;

        setVisibility(VISIBLE);

        // Map Stuff
        // Map not avilable
        if (_mapUnavailable) {
            _loadingProgress.setVisibility(GONE);
            _mapImageView.setImageResource(R.drawable.no_map);
            _noMapLayout.setVisibility(VISIBLE);

            // loading
        } else if (_map == null) {
            _loadingProgress.setVisibility(VISIBLE);
            _mapImageView.setImageResource(R.drawable.no_map);
            _noMapLayout.setVisibility(GONE);

            // have all the things
        } else {
            _loadingProgress.setVisibility(GONE);
            _mapImageView.setImageBitmap(_map);
            _noMapLayout.setVisibility(GONE);
        }

        // Address info
        Location loc = _workorder.getLocation();
        if (loc != null) {

            // set site title
            String title = "";
            if (!misc.isEmptyOrNull(loc.getGroupName())) {
                title = loc.getGroupName().trim() + "/";
            }
            if (!misc.isEmptyOrNull(loc.getName())) {
                title += loc.getName();
            }
            if (!misc.isEmptyOrNull(title)) {
                _siteTitleTextView.setText(title);
                _siteTitleTextView.setVisibility(VISIBLE);
            } else {
                _siteTitleTextView.setVisibility(GONE);
            }

            // set address line
            _addressTextView.setText(loc.getFullAddressOneLine());


            // set locatoin type
            if (loc.getType() != null) {
                _locationTypeTextView.setVisibility(VISIBLE);
                _locationIconTextView.setVisibility(VISIBLE);
                switch (loc.getType()) {
                    case "Commercial":
                        _locationIconTextView.setText(R.string.icon_commercial);
                        _locationTypeTextView.setText(R.string.commercial);
                        break;
                    case "Government":
                        _locationIconTextView.setText(R.string.icon_government);
                        _locationTypeTextView.setText(R.string.government);
                        break;
                    case "Residential":
                        _locationIconTextView.setText(R.string.icon_house);
                        _locationTypeTextView.setText(R.string.residential);
                        break;
                    case "Educational":
                        _locationIconTextView.setText(R.string.icon_educational);
                        _locationTypeTextView.setText(R.string.educational);
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

            // display location notes
            if (misc.isEmptyOrNull(loc.getNotes())) {
                _noteTextView.setVisibility(GONE);
            } else {
                _noteTextView.setVisibility(VISIBLE);
                _noteTextView.setText(loc.getNotes());
            }
            // location == null
        } else if (_workorder.getIsRemoteWork()) {
            _mapImageView.setVisibility(GONE);
            _mapLayout.setVisibility(GONE);
            _noLocationTextView.setVisibility(VISIBLE);
            _addressLayout.setVisibility(GONE);
            _navigateButton.setVisibility(GONE);
            Log.v(TAG, "no location time: " + stopwatch.finish());
        }

        Log.v(TAG, "populateUi time: " + stopwatch.finish());
    }

    private void lookupMap() {
        if (_mapboxClient == null || !_mapboxClient.isConnected())
            return;

        if (_workorder == null)
            return;

        if (_userLocation == null)
            return;

        if (_mapImageView == null)
            return;

        if (_mapImageView.getWidth() == 0 || _mapImageView.getHeight() == 0)
            return;

        Marker start = new Marker(_userLocation.getLongitude(), _userLocation.getLatitude(),
                getContext().getString(R.string.mapbox_startMarkerUrl));

        Marker end = null;
        Location loc = _workorder.getLocation();
        if (loc != null) {
            Geo geo = loc.getGeo();

            if (geo != null) {
                if (geo.getObfuscated() || !geo.getPrecise()) {
                    end = new Marker(geo.getLongitude(), geo.getLatitude(),
                            getContext().getString(R.string.mapbox_inpreciseMarkerUrl));
                } else {
                    end = new Marker(geo.getLongitude(), geo.getLatitude(),
                            getContext().getString(R.string.mapbox_endMarkerUrl));
                }
            }
        }

        if (end != null) {
            MapboxClient.getStaticMapClassic(App.get(), _workorder.getWorkorderId(), start, end, 400, 200);
        } else {
            _mapUnavailable = true;
            populateUi();
        }
    }

    private void showMapActivity(Uri geoLocation) {
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
                        showMapActivity(_uri);
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
                        showMapActivity(_uri);
                    } catch (Exception e) {
                    }
                }
            }
        }
    };

    private final SimpleGps.Listener _gpsListener = new SimpleGps.Listener() {
        @Override
        public void onLocation(android.location.Location location) {
            Log.v(TAG, "_gpsListener");
            _userLocation = location;
            SimpleGps.with(getContext()).stop();
            lookupMap();
        }
    };

    private final MapboxClient.Listener _mapboxClient_listener = new MapboxClient.Listener() {
        @Override
        public void onConnected() {
            if (_workorder != null) {
                _mapboxClient.subStaticMapClassic(_workorder.getWorkorderId());
                lookupMap();
            }
        }

        @Override
        public void onStaticMapClassic(long workorderId, Bitmap bitmap) {
            Log.v(TAG, "onStaticMapClassic");
            _map = bitmap;
            _mapUnavailable = false;
            populateUi();
        }
    };
}
