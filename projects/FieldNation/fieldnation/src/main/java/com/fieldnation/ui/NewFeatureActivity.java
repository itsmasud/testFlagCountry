package com.fieldnation.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.TextView;

import com.fieldnation.R;
import com.fieldnation.service.activityresult.ActivityResultClient;

/**
 * Created by shoaib.ahmed on 08/02/2016.
 */
public class NewFeatureActivity extends Activity {
    private static final String TAG = "NewFeatureActivity";

    // Ui
    private TextView _addedNewTextView;
    private WebView _addedNewWebView;
    private TextView _bugFixedTextView;
    private WebView _bugFixedWebView;
    private TextView _workingOnFeatureTextView;
    private WebView _workingOnFeatureWebView;
    private Button _cancelButton;

    // Data
    private String _source;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_feature);

        _addedNewTextView = (TextView) findViewById(R.id.addedNew_textview);
        _addedNewWebView = (WebView) findViewById(R.id.addedNew_webview);

        _bugFixedTextView = (TextView) findViewById(R.id.bugFixed_textview);
        _bugFixedWebView = (WebView) findViewById(R.id.bugFixed_webview);

        _workingOnFeatureTextView = (TextView) findViewById(R.id.workingOnFeature_textview);
        _workingOnFeatureWebView = (WebView) findViewById(R.id.workingOnFeature_webview);

        _cancelButton = (Button) findViewById(R.id.cancel_button);
        _cancelButton.setOnClickListener(_cancel_onClick);

        populateUi();
    }

    private void populateUi() {

        int fontSize = getResources().getInteger(R.integer.textSizeReleaseNote);
        WebSettings webSettings = null;

        webSettings = _addedNewWebView.getSettings();
        _addedNewWebView.loadData(getString(R.string.added_new_feature), "text/html", "utf-8");
        webSettings.setDefaultFontSize(fontSize);

        _bugFixedWebView.loadData(getString(R.string.bugs_fixed), "text/html", "utf-8");
        webSettings = _bugFixedWebView.getSettings();
        webSettings.setDefaultFontSize(fontSize);

        _workingOnFeatureWebView.loadData(getString(R.string.working_on_feature), "text/html", "utf-8");
        webSettings = _workingOnFeatureWebView.getSettings();
        webSettings.setDefaultFontSize(fontSize);

    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.hold, R.anim.fg_slide_out_bottom);
    }

    private final View.OnClickListener _cancel_onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            onBackPressed();
        }
    };

    public static void startNew(Context context) {
        Intent intent = new Intent(context, NewFeatureActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        ActivityResultClient.startActivity(context, intent, R.anim.activity_slide_in_bottom, R.anim.hold);
    }
}
