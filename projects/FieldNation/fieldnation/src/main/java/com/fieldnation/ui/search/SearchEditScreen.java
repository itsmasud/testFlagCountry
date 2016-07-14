package com.fieldnation.ui.search;

import android.content.Context;
import android.support.v7.widget.AppCompatSpinner;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.fieldnation.App;
import com.fieldnation.Log;
import com.fieldnation.R;
import com.fieldnation.data.workorder.Workorder;
import com.fieldnation.service.activityresult.ActivityResultClient;
import com.fieldnation.service.data.workorder.WorkorderClient;
import com.fieldnation.ui.RefreshView;
import com.fieldnation.ui.workorder.WorkorderActivity;

/**
 * Created by Michael on 7/14/2016.
 */
public class SearchEditScreen extends RelativeLayout {
    private static final String TAG = "SearchEditScreen";

    // UI
    private RefreshView _loadingView;
    private SearchEditText _searchEditText;
    private AppCompatSpinner _statusSpinner;
    private Button _actionButton;

    // Services
    private WorkorderClient _workorderClient;

    // Crap
    private Listener _listener;


    public SearchEditScreen(Context context) {
        super(context);
        init();
    }

    public SearchEditScreen(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public SearchEditScreen(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.screen_edit_search, this);

        if (isInEditMode())
            return;
        _loadingView = (RefreshView) findViewById(R.id.loading_view);

        _searchEditText = (SearchEditText) findViewById(R.id.searchedittext);
        _searchEditText.setListener(_searchEditText_listener);

        _statusSpinner = (AppCompatSpinner) findViewById(R.id.status_spinner);
        _statusSpinner.setOnItemSelectedListener(_statusSpinner_onItemSelected);
        ArrayAdapter adapter = ArrayAdapter.createFromResource(getContext(),
                R.array.dialog_block_reasons, R.layout.view_spinner_item);
        adapter.setDropDownViewResource(
                android.support.design.R.layout.support_simple_spinner_dropdown_item);
        _statusSpinner.setAdapter(adapter);

        _actionButton = (Button) findViewById(R.id.action_button);
        _actionButton.setOnClickListener(_action_onClick);

        _workorderClient = new WorkorderClient(_workorderClient_listener);
        _workorderClient.connect(App.get());
    }

    @Override
    protected void onDetachedFromWindow() {
        if (_workorderClient != null && _workorderClient.isConnected())
            _workorderClient.disconnect(App.get());

        super.onDetachedFromWindow();
    }

    public void setListener(Listener listener) {
        _listener = listener;
    }

    public void reset() {
        _searchEditText.setText("");
    }

    private final View.OnClickListener _action_onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            try {
                _workorderClient.subGet(Long.parseLong(_searchEditText.getText()));
                WorkorderClient.get(App.get(), Long.parseLong(_searchEditText.getText()), false);
                _loadingView.startRefreshing();
            } catch (Exception ex) {
                Log.v(TAG, ex);
            }
        }
    };

    private final SearchEditText.Listener _searchEditText_listener = new SearchEditText.Listener() {
        @Override
        public void startSearch(String searchString) {
            try {
                _workorderClient.subGet(Long.parseLong(searchString));
                WorkorderClient.get(App.get(), Long.parseLong(searchString), false);
                _loadingView.startRefreshing();
            } catch (Exception ex) {
                Log.v(TAG, ex);
            }
        }
    };

    private final AdapterView.OnItemSelectedListener _statusSpinner_onItemSelected = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };

    private final WorkorderClient.Listener _workorderClient_listener = new WorkorderClient.Listener() {
        @Override
        public void onConnected() {
        }

        @Override
        public void onGet(Workorder workorder, boolean failed, boolean isCached) {
            _loadingView.refreshComplete();
            if (workorder == null || failed) {
                if (_listener != null)
                    _listener.showNotAvailableDialog();
            } else {
                _workorderClient.unsubGet(workorder.getWorkorderId());
                ActivityResultClient.startActivity(App.get(),
                        WorkorderActivity.makeIntentShow(App.get(), workorder.getWorkorderId()));
            }
        }
    };

    public interface Listener {
        void showNotAvailableDialog();
    }
}
