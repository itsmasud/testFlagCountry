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
import com.fieldnation.data.mapbox.MapboxDirections;
import com.fieldnation.data.mapbox.MapboxRoute;
import com.fieldnation.data.workorder.Geo;
import com.fieldnation.data.workorder.Location;
import com.fieldnation.data.workorder.Workorder;
import com.fieldnation.service.data.mapbox.MapboxClient;
import com.fieldnation.service.data.mapbox.Marker;
import com.fieldnation.service.data.mapbox.Position;
import com.fieldnation.ui.IconFontTextView;
import com.fieldnation.utils.misc;

public class LocationView extends LinearLayout implements WorkorderRenderer {
    private static final String TAG = "LocationView";

    // UI
    private TextView _noLocationTextView;

    private RelativeLayout _mapLayout;
    private ImageView _mapImageView;
    private ProgressBar _loadingProgress;

    private LinearLayout _noMapLayout;
    private TextView _gpsError1TextView;
    private TextView _gpsError2TextView;

    private LinearLayout _addressLayout;
    private TextView _siteTitleTextView;
    private TextView _addressTextView;

    private LinearLayout _locationTypeLayout;
    private IconFontTextView _locationIconTextView;
    private TextView _locationTypeTextView;

    private TextView _distanceTextView;
    private TextView _noteTextView;

    private Button _navigateButton;

    // Data
    private Workorder _workorder;
    private android.location.Location _userLocation = null;
    private Bitmap _map = null;
    private boolean _mapUnavailable = false;
    private MapboxDirections _directions = null;

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
        _gpsError1TextView = (TextView) findViewById(R.id.gpsError1_textview);
        _gpsError2TextView = (TextView) findViewById(R.id.gpsError2_textview);

        _addressLayout = (LinearLayout) findViewById(R.id.address_layout);
        _addressLayout.setOnClickListener(_map_onClick);

        _siteTitleTextView = (TextView) findViewById(R.id.siteTitle_textview);
        _addressTextView = (TextView) findViewById(R.id.address_textview);

        _locationTypeLayout = (LinearLayout) findViewById(R.id.locationType_layout);
        _locationIconTextView = (IconFontTextView) findViewById(R.id.locationIcon_textview);
        _locationTypeTextView = (TextView) findViewById(R.id.locationType_textview);

        _distanceTextView = (TextView) findViewById(R.id.distance_textview);
        _noteTextView = (TextView) findViewById(R.id.note_textview);

        _navigateButton = (Button) findViewById(R.id.navigate_button);
        _navigateButton.setOnClickListener(_navigate_onClick);

        _mapboxClient = new MapboxClient(_mapboxClient_listener);
        _mapboxClient.connect(App.get());

        SimpleGps.with(App.get()).start(_gpsListener);

        setVisibility(View.GONE);

        getViewTreeObserver().addOnGlobalLayoutListener(_layoutObserver);
    }

    private final ViewTreeObserver.OnGlobalLayoutListener _layoutObserver = new ViewTreeObserver.OnGlobalLayoutListener() {
        @Override
        public void onGlobalLayout() {
            Log.v(TAG, "layoutComplete");
            lookupMap();
            getViewTreeObserver().removeGlobalOnLayoutListener(_layoutObserver);
        }
    };

    @Override
    protected void onDetachedFromWindow() {
        if (_mapboxClient != null && _mapboxClient.isConnected()) {
            _mapboxClient.disconnect(App.get());
        }
        SimpleGps.with(App.get()).stop();
        super.onDetachedFromWindow();
    }

    @Override
    public void setWorkorder(Workorder workorder) {
        _workorder = workorder;

        if (_mapboxClient != null && _mapboxClient.isConnected()) {
            _mapboxClient.subStaticMapClassic(_workorder.getWorkorderId());
            _mapboxClient.subDirections(_workorder.getWorkorderId());
            lookupMap();
        }
        populateUi();
    }

    private void populateUi() {
        if (_workorder == null)
            return;

        if (_mapImageView == null)
            return;

        setVisibility(VISIBLE);

        populateAddressTile();
        populateMap();
        calculateAddressTileVisibility();
    }

    private void calculateAddressTileVisibility() {
        _navigateButton.setVisibility(VISIBLE);
        if (_workorder.getIsRemoteWork() || _workorder.getLocation() == null)
            _navigateButton.setVisibility(GONE);

        // hide stuff that shouldn't be seen
        if (_workorder.getIsRemoteWork()) {
            _mapLayout.setVisibility(GONE);
            _noLocationTextView.setVisibility(VISIBLE);
            _distanceTextView.setVisibility(GONE);
            _addressLayout.setVisibility(GONE);
        }
    }

    private void populateAddressTile() {
        // Address info
        Location loc = _workorder.getLocation();
        if (loc == null)
            return;

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
        if (!misc.isEmptyOrNull(loc.getFullAddressOneLine())) {
            _addressTextView.setVisibility(VISIBLE);
            _addressTextView.setText(loc.getFullAddressOneLine());
        } else {
            _addressTextView.setVisibility(GONE);
        }

        // set location type
        if (loc.getType() == null) {
            _locationTypeLayout.setVisibility(GONE);
        } else {
            _locationTypeLayout.setVisibility(VISIBLE);
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
                    _locationTypeLayout.setVisibility(GONE);
                    break;
            }
        }

        // distance
        _distanceTextView.setVisibility(VISIBLE);
        if (!SimpleGps.with(App.get()).isLocationEnabled()) {
            _distanceTextView.setText(R.string.gps_disabled_cant_calc_dist);
        } else if (_directions != null) {
            double miles = 0.0;
            MapboxRoute[] routes = _directions.getRoutes();
            for (MapboxRoute route : routes) {
                miles += route.getDistanceMi();
            }
            _distanceTextView.setText(getResources().getString(R.string.num_mi_driving, misc.to2Decimal(miles)));
        } else if (_userLocation != null && loc.getGeo() != null) {
            try {
                Position siteLoc = new Position(loc.getGeo().getLongitude(), loc.getGeo().getLatitude());
                Position myLoc = new Position(_userLocation.getLongitude(), _userLocation.getLatitude());

                _distanceTextView.setText(getResources().getString(R.string.num_mi_straight_line, misc.to2Decimal(myLoc.distanceTo(siteLoc))));
            } catch (Exception ex) {
                Log.v(TAG, ex);
                _distanceTextView.setText(R.string.cannot_display_distance);
            }
        } else if (loc.getGeo() == null) {
            _distanceTextView.setText(R.string.cannot_display_distance);
        } else {
            _distanceTextView.setText(R.string.fetching_distance);
        }

        // display location notes
        if (misc.isEmptyOrNull(loc.getNotes())) {
            _noteTextView.setVisibility(GONE);
        } else {
            _noteTextView.setVisibility(VISIBLE);
            _noteTextView.setText(loc.getNotes());
        }
    }

    private void populateMap() {
        //        no gps - !isLocationEnabled()
        if (!SimpleGps.with(App.get()).isLocationEnabled()) {
            _loadingProgress.setVisibility(GONE);
            _mapImageView.setImageResource(R.drawable.no_map);
            _noMapLayout.setVisibility(VISIBLE);
            _gpsError1TextView.setText(R.string.map_not_available);
            _gpsError2TextView.setText(R.string.gps_is_currently_disabled);

        } else if (_workorder.getIsRemoteWork()) {
//        remote work
            _noMapLayout.setVisibility(GONE);

        } else if (_workorder.getLocation() == null || _workorder.getLocation().getGeo() == null) {
//        no geo data - ie bad address - _workorder.getLocation() == null || _workord
            _loadingProgress.setVisibility(GONE);
            _mapImageView.setImageResource(R.drawable.no_map);
            _noMapLayout.setVisibility(VISIBLE);
            _gpsError1TextView.setText(R.string.map_not_available);
            _gpsError2TextView.setText(R.string.no_location_provided_by_buyer);

        } else {
            if (_mapUnavailable) {
                _loadingProgress.setVisibility(GONE);
                _mapImageView.setImageResource(R.drawable.no_map);
                _noMapLayout.setVisibility(VISIBLE);
                _gpsError1TextView.setText(R.string.map_not_available);
                _gpsError2TextView.setText(R.string.error_looking_up_map);

                // loading
            } else if (_map == null) { //  !_mapUnavailable
                _loadingProgress.setVisibility(VISIBLE);
                _mapImageView.setImageResource(R.drawable.no_map);
                _noMapLayout.setVisibility(GONE);

                // have all the things
            } else {
                _loadingProgress.setVisibility(GONE);
                _mapImageView.setImageBitmap(_map);
                _noMapLayout.setVisibility(GONE);
            }
        }
    }

    private void lookupMap() {
        Log.v(TAG, "lookupMap");
        if (_mapboxClient == null || !_mapboxClient.isConnected())
            return;

        Log.v(TAG, "lookupMap - 1");
        if (_workorder == null)
            return;

        Log.v(TAG, "lookupMap - 2");
        if (_userLocation == null)
            return;

        Log.v(TAG, "lookupMap - 3");
        if (_mapImageView == null)
            return;

        Log.v(TAG, "lookupMap - 4");
        if (_mapImageView.getWidth() == 0 || _mapImageView.getHeight() == 0) {
            postDelayed(new Runnable() {
                @Override
                public void run() {
                    lookupMap();
                }
            }, 100);
            return;
        }

        Log.v(TAG, "lookupMap - 5");
        Marker start = new Marker(_userLocation.getLongitude(), _userLocation.getLatitude(),
                getContext().getString(R.string.mapbox_startMarkerUrl));
        Position startPos = new Position(_userLocation.getLongitude(), _userLocation.getLatitude());

        Marker end = null;
        Position endPos = null;
        Location loc = _workorder.getLocation();
        if (loc != null) {
            Geo geo = loc.getGeo();
            if (geo != null) {
                endPos = new Position(geo.getLongitude(), geo.getLatitude());
                if (geo.getObfuscated() || !geo.getPrecise()) {
                    end = new Marker(geo.getLongitude(), geo.getLatitude(),
                            getContext().getString(R.string.mapbox_inpreciseMarkerUrl));
                } else {
                    MapboxClient.getDirections(App.get(), _workorder.getWorkorderId(), startPos, endPos);
                    end = new Marker(geo.getLongitude(), geo.getLatitude(),
                            getContext().getString(R.string.mapbox_endMarkerUrl));
                }
            }
        }

        if (end != null) {
            MapboxClient.getStaticMapClassic(App.get(), _workorder.getWorkorderId(), start, end,
                    _mapImageView.getWidth() / 3, _mapImageView.getHeight() / 3);
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
            lookupMap();
        }
    };

    private final MapboxClient.Listener _mapboxClient_listener = new MapboxClient.Listener() {
        @Override
        public void onConnected() {
            if (_workorder != null) {
                _mapboxClient.subStaticMapClassic(_workorder.getWorkorderId());
                _mapboxClient.subDirections(_workorder.getWorkorderId());
                lookupMap();
            }
        }

        @Override
        public void onStaticMapClassic(long workorderId, Bitmap bitmap, boolean failed) {
            Log.v(TAG, "onStaticMapClassic");
            if (bitmap == null || failed) {
                _map = null;
                _mapUnavailable = true;
            } else {
                _map = bitmap;
                _mapUnavailable = false;
            }
            populateUi();
        }

        @Override
        public void onDirections(long workorderId, MapboxDirections directions) {
            Log.v(TAG, "onDirections");

            if (directions != null && directions.getCode().equals("Ok")) {
                _directions = directions;
                populateUi();
            }
        }
    };
}
