package com.fieldnation.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.fieldnation.R;

/**
 * Created by Michael Carver on 1/12/2016.
 */
public class LeavingActivity extends Activity {

    public static final String INTENT_URI = "INTENT_URI";
    public static final String INTENT_TITLE = "INTENT_TITLE";
    public static final String INTENT_SUBTEXT = "INTENT_SUBTEXT";

    // Ui
    private TextView _titleTextView;
    private TextView _subTextView;
    private Button _cancelButton;
    private Button _continueButton;

    // Data
    private String _title;
    private String _subtext;
    private Uri _uri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.FieldNation_Leaving_Theme);
        setContentView(R.layout.activity_leaving_overlay);

        final Intent intent = getIntent();

        if (intent == null)
            return;

        if (intent.hasExtra(INTENT_TITLE)) {
            _title = intent.getStringExtra(INTENT_TITLE);
        }
        if (intent.hasExtra(INTENT_SUBTEXT)) {
            _subtext = intent.getStringExtra(INTENT_SUBTEXT);
        }
        if (intent.hasExtra(INTENT_URI)) {
            _uri = intent.getParcelableExtra(INTENT_URI);
        }

        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey(INTENT_TITLE))
                _title = savedInstanceState.getString(INTENT_TITLE);

            if (savedInstanceState.containsKey(INTENT_SUBTEXT))
                _subtext = savedInstanceState.getString(INTENT_SUBTEXT);

            if (savedInstanceState.containsKey(INTENT_URI))
                _uri = savedInstanceState.getParcelable(INTENT_URI);
        }

        _titleTextView = (TextView) findViewById(R.id.title_textview);
        _titleTextView.setText(_title);
        _subTextView = (TextView) findViewById(R.id.subtext_textview);
        _subTextView.setText(_subtext);

        _cancelButton = (Button) findViewById(R.id.cancel_button);
        _cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        _continueButton = (Button) findViewById(R.id.continue_button);
        _continueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent todo = new Intent(Intent.ACTION_VIEW, _uri);
                startActivity(todo);

                finish();
            }
        });
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        if (_title != null)
            outState.putString(INTENT_TITLE, _title);

        if (_subtext != null)
            outState.putString(INTENT_SUBTEXT, _subtext);

        if (_uri != null)
            outState.putParcelable(INTENT_URI, _uri);

        super.onSaveInstanceState(outState);
    }

    public static void start(Context context, int titleResId, int subtextResId, Uri uri) {
        Intent intent = new Intent(context, LeavingActivity.class);
        intent.putExtra(INTENT_TITLE, context.getString(titleResId));
        intent.putExtra(INTENT_SUBTEXT, context.getString(subtextResId));
        intent.putExtra(INTENT_URI, uri);
        context.startActivity(intent);
    }

    public static void start(Context context, String title, String subtext, Uri uri) {
        Intent intent = new Intent(context, LeavingActivity.class);
        intent.putExtra(INTENT_TITLE, title);
        intent.putExtra(INTENT_SUBTEXT, subtext);
        intent.putExtra(INTENT_URI, uri);
        context.startActivity(intent);
    }

}
