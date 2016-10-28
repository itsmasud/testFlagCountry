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
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fieldnation.App;
import com.fieldnation.R;
import com.fieldnation.service.activityresult.ActivityResultClient;
import com.fieldnation.service.activityresult.ActivityResultConstants;
import com.fieldnation.ui.IconFontTextView;
import com.fieldnation.fntools.misc;

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

    // Data
    private ActivityResultClient _activityResultClient;
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
        _searchTermEditText.setOnEditorActionListener(_onEditor);
        _searchTermEditText.addTextChangedListener(_searchTermEditText_textChangedListener);

        _micIconFont = (IconFontTextView) findViewById(R.id.right_textview);
        _micIconFont.setOnClickListener(_micIconFont_onClick);

        _activityResultClient = new ActivityResultClient(_activityResultClient_listener);
        _activityResultClient.connect(App.get());
    }

    public void setListener(Listener listener) {
        _listener = listener;
    }

    public void setText(CharSequence charSequence) {
        _searchTermEditText.setText(charSequence);
    }

    public String getText() {
        return _searchTermEditText.getText().toString();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();

        if (_activityResultClient != null && _activityResultClient.isConnected())
            _activityResultClient.disconnect(App.get());
    }

    private final View.OnClickListener _search_onClick = new OnClickListener() {
        @Override
        public void onClick(View v) {
            _listener.startSearch(_searchTermEditText.getText().toString());
        }
    };

    private final TextView.OnEditorActionListener _onEditor = new TextView.OnEditorActionListener() {
        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                _search_onClick.onClick(v);
                return true;
            }
            return false;
        }
    };

    private final TextWatcher _searchTermEditText_textChangedListener = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (_listener != null)
                _listener.onTextChanged(s);
        }

        @Override
        public void afterTextChanged(Editable s) {
        }
    };

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

    public interface Listener {
        void startSearch(String searchString);

        void onTextChanged(CharSequence s);
    }
}
