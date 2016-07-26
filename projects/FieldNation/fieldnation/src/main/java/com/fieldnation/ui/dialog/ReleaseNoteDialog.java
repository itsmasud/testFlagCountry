package com.fieldnation.ui.dialog;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.TextView;

import com.fieldnation.Log;
import com.fieldnation.R;

/**
 * Created by Shoaib on 7/21/2016.
 */
public class ReleaseNoteDialog extends DialogFragmentBase {
    private static final String TAG = "ReleaseNoteDialog";


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

    /*-*****************************-*/
    /*-         Life Cycle          -*/
    /*-*****************************-*/
    public static ReleaseNoteDialog getInstance(FragmentManager fm, String tag) {
        Log.v(TAG, "getInstance");
        return getInstance(fm, tag, ReleaseNoteDialog.class);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.v(TAG, "onCreate");
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_TITLE, 0);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        Log.v(TAG, "onSaveInstanceState");

        super.onSaveInstanceState(outState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.v(TAG, "onCreateView");
        View v = inflater.inflate(R.layout.dialog_release_note, container, false);

        _addedNewTextView = (TextView) v.findViewById(R.id.addedNew_textview);
        _addedNewWebView = (WebView) v.findViewById(R.id.addedNew_webview);

        _bugFixedTextView = (TextView) v.findViewById(R.id.bugFixed_textview);
        _bugFixedWebView = (WebView) v.findViewById(R.id.bugFixed_webview);

        _workingOnFeatureTextView = (TextView) v.findViewById(R.id.workingOnFeature_textview);
        _workingOnFeatureWebView = (WebView) v.findViewById(R.id.workingOnFeature_webview);

        _cancelButton = (Button) v.findViewById(R.id.cancel_button);
        _cancelButton.setOnClickListener(_cancel_onClick);

        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        return v;
    }

    @Override
    public void onViewStateRestored(Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        Log.v(TAG, "onViewStateRestored");
    }

    @Override
    public void onResume() {
        Log.v(TAG, "onResume");
        super.onResume();
        populateUI();
    }

    private void populateUI() {

        final String dataAddedNewFeature = "<ul>" +
                "<li>Point One</li>" +
                "<li>Point Two</li>" +
                "<li>Point Three</li>" +
                "</ul>";

        final String dataBugsFixed = "<ul>" +
                "<li>Point One</li>" +
                "<li>Point Two</li>" +
                "<li>Point Three</li>" +
                "</ul>";

        final String dataWorkingOnFeatures = "<ul>" +
                "<li>Point One</li>" +
                "<li>Point Two</li>" +
                "<li>Point Three</li>" +
                "</ul>";

        int fontSize = getResources().getInteger(R.integer.textSizeReleaseNote);
        WebSettings webSettings = null;

        webSettings = _addedNewWebView.getSettings();
        _addedNewWebView.loadData(dataAddedNewFeature, "text/html", "utf-8");
        webSettings.setDefaultFontSize(fontSize);

        _bugFixedWebView.loadData(dataBugsFixed, "text/html", "utf-8");
        webSettings = _bugFixedWebView.getSettings();
        webSettings.setDefaultFontSize(fontSize);

        _workingOnFeatureWebView.loadData(dataWorkingOnFeatures, "text/html", "utf-8");
        webSettings = _workingOnFeatureWebView.getSettings();
        webSettings.setDefaultFontSize(fontSize);

    }

    @Override
    public void onDismiss(DialogInterface dialogFragment) {
//        Log.e(TAG, "onDismiss");
        super.onDismiss(dialogFragment);
    }

    public void show(String source) {
        _source = source;
        super.show();
    }


    private final View.OnClickListener _cancel_onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            dismiss();
        }
    };
}
