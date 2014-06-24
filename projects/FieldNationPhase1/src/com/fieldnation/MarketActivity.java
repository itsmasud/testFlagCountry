package com.fieldnation;

import android.database.DataSetObserver;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

/**
 * Displays all the work orders in the market that are available to this user
 * 
 * @author michael.carver
 * 
 */
public class MarketActivity extends DrawerActivity {
	private static final String TAG = "MarketActivity";

	// UI
	private ListView _workordersListView;
	private ProgressBar _loadingProgressBar;
	private TextView _noDataTextView;
	private ImageButton _refreshButton;

	// Data
	private WorkorderListAdapter _woAdapter;
	private boolean _hasData = false;

	// Services

	/*-*************************************-*/
	/*-				Life Cycle				-*/
	/*-*************************************-*/
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_market);
		setTitle(R.string.market_title);

		_workordersListView = (ListView) findViewById(R.id.workorder_listview);
		_loadingProgressBar = (ProgressBar) findViewById(R.id.loading_progressbar);
		_noDataTextView = (TextView) findViewById(R.id.nodata_textview);
		_refreshButton = (ImageButton) findViewById(R.id.refresh_button);
		_refreshButton.setOnClickListener(_refresh_onClick);

		addActionBarAndDrawer(R.id.container);

		try {
			_woAdapter = new WorkorderListAdapter(MarketActivity.this,
					DataSelector.AVAILABLE);
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		}
		_woAdapter.registerDataSetObserver(_listAdapter_observer);
		_workordersListView.setAdapter(_woAdapter);
		// _woAdapter.update(true);

	}

	@Override
	protected void onStart() {
		super.onStart();
		updateUi();
	}

	@Override
	protected void onPause() {
		super.onPause();

		if (_woAdapter != null) {
			_woAdapter.onStop();
		}
	}

	/*-*********************************-*/
	/*-				Events				-*/
	/*-*********************************-*/
	private View.OnClickListener _refresh_onClick = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			_hasData = false;
			updateUi();
			_woAdapter.update(false);
		}
	};

	private DataSetObserver _listAdapter_observer = new DataSetObserver() {
		@Override
		public void onChanged() {
			_hasData = true;
			updateUi();
			super.onChanged();
		}
	};

	/*-*********************************-*/
	/*-				Util				-*/
	/*-*********************************-*/
	private void updateUi() {
		if (_loadingProgressBar != null) {
			if (_hasData) {
				_loadingProgressBar.setVisibility(View.GONE);
				if (_woAdapter != null) {
					if (_woAdapter.getCount() == 0) {
						_noDataTextView.setVisibility(View.VISIBLE);
						_refreshButton.setVisibility(View.VISIBLE);
					} else {
						_noDataTextView.setVisibility(View.GONE);
						_refreshButton.setVisibility(View.GONE);
					}
				}
			} else {
				_noDataTextView.setVisibility(View.GONE);
				_refreshButton.setVisibility(View.GONE);
				_loadingProgressBar.setVisibility(View.VISIBLE);
			}
		}
	}

}
