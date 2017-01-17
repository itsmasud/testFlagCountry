package com.fieldnation.ui.search;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.speech.RecognizerIntent;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fieldnation.App;
import com.fieldnation.R;
import com.fieldnation.analytics.trackers.SearchTracker;
import com.fieldnation.data.workorder.Workorder;
import com.fieldnation.fnlog.Log;
import com.fieldnation.fntools.misc;
import com.fieldnation.service.activityresult.ActivityResultClient;
import com.fieldnation.service.activityresult.ActivityResultConstants;
import com.fieldnation.service.data.workorder.WorkorderClient;
import com.fieldnation.ui.IconFontTextView;

import java.util.ArrayList;

/**
 * Created by Michael on 7/8/2016.
 */
public class SearchEditText extends RelativeLayout {
    private static final String TAG = "SearchEditText";

    // UI
    private IconFontTextView _searchIconFont;
    private EditText _searchTermEditText;
    private IconFontTextView _micIconFont;
    private ProgressBar _progressBar;

    // Data
    private ActivityResultClient _activityResultClient;
    private WorkorderClient _workorderClient;
    private Listener _listener;

    public SearchEditText(Context context) {
        super(context);
        init();
    }

    public SearchEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public SearchEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.view_search_edit_text, this);

        if (isInEditMode())
            return;

        _searchIconFont = (IconFontTextView) findViewById(R.id.left_textview);
        _searchIconFont.setOnClickListener(_search_onClick);

        _searchTermEditText = (EditText) findViewById(R.id.search_edittext);
        _searchTermEditText.setOnEditorActionListener(_searchTermEditText_onEdit);
        _searchTermEditText.addTextChangedListener(_searchTermEditText_watcher);

        _micIconFont = (IconFontTextView) findViewById(R.id.right_textview);
        _micIconFont.setOnClickListener(_micIconFont_onClick);

        _progressBar = (ProgressBar) findViewById(R.id.progress_view);

        _activityResultClient = new ActivityResultClient(_activityResultClient_listener);
        _activityResultClient.connect(App.get());

        _workorderClient = new WorkorderClient(_workorderClient_listener);
        _workorderClient.connect(App.get());

        _searchIconFont.setEnabled(_searchTermEditText.getText().toString().length() > 0);
    }

    public void setListener(Listener listener) {
        _listener = listener;
    }

    public void setText(CharSequence charSequence) {
        _searchTermEditText.setText(charSequence);

        _searchIconFont.setEnabled(_searchTermEditText.getText().toString().length() > 0);
    }

    public String getText() {
        return _searchTermEditText.getText().toString();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();

        if (_activityResultClient != null && _activityResultClient.isConnected())
            _activityResultClient.disconnect(App.get());

        if (_workorderClient != null && _workorderClient.isConnected())
            _workorderClient.disconnect(App.get());
    }

    private final TextView.OnEditorActionListener _searchTermEditText_onEdit = new TextView.OnEditorActionListener() {
        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            if (actionId == EditorInfo.IME_ACTION_SEARCH && _searchTermEditText.getText().toString().length() > 0) {
                try {
                    SearchTracker.onSearch(App.get(),
                            SearchTracker.Item.KEYBOARD,
                            Long.parseLong(_searchTermEditText.getText().toString()));
                } catch (Exception ex) {
                }
                doWorkorderLookup();
                return true;
            }
            return false;
        }
    };

    private final TextWatcher _searchTermEditText_watcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        @Override
        public void afterTextChanged(Editable s) {
            _searchIconFont.setEnabled(_searchTermEditText.getText().toString().length() > 0);
        }
    };

    private final View.OnClickListener _search_onClick = new OnClickListener() {
        @Override
        public void onClick(View v) {
            try {
                SearchTracker.onSearch(App.get(),
                        SearchTracker.Item.SEARCH_BAR,
                        Long.parseLong(_searchTermEditText.getText().toString()));
            } catch (Exception ex) {
            }
            doWorkorderLookup();
        }
    };

    private void doWorkorderLookup() {
        try {
            _progressBar.setVisibility(VISIBLE);
            long workOrderId = Long.parseLong(_searchTermEditText.getText().toString());
            _workorderClient.subGet(workOrderId);
            WorkorderClient.get(App.get(), workOrderId, false);
        } catch (Exception ex) {
            Log.v(TAG, ex);
        }
    }

    private final View.OnClickListener _micIconFont_onClick = new OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
            intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Please speak slowly and enunciate clearly.");
            ActivityResultClient.startActivityForResult(App.get(), intent, ActivityResultConstants.RESULT_CODE_VOICE_REQUEST);
        }
    };

    private final ActivityResultClient.Listener _activityResultClient_listener = new ActivityResultClient.ResultListener() {
        @Override
        public void onConnected() {
            _activityResultClient.subOnActivityResult(ActivityResultConstants.RESULT_CODE_VOICE_REQUEST);
        }

        @Override
        public ActivityResultClient getClient() {
            return _activityResultClient;
        }

        @Override
        public void onActivityResult(int requestCode, int resultCode, Intent data) {
            if (resultCode == Activity.RESULT_OK) {
                ArrayList matches = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                String firstMatch = (String) matches.get(0);
                _searchTermEditText.setText(misc.extractNumbers(firstMatch));
            }
        }
    };

    private final WorkorderClient.Listener _workorderClient_listener = new WorkorderClient.Listener() {
        @Override
        public void onConnected() {
        }

        @Override
        public void onGet(long workorderId, Workorder workorder, boolean failed, boolean isCached) {
            _progressBar.setVisibility(GONE);
            _workorderClient.unsubGet(workorderId);
            if (workorder == null || failed) {
            } else {
                if (_listener != null)
                    _listener.onLookupWorkOrder(workorderId);
            }
        }
    };

    public interface Listener {
        void onLookupWorkOrder(long workOrderId);
    }
}
