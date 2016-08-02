package com.fieldnation.ui;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.support.v7.internal.view.menu.ActionMenuItemView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.fieldnation.App;
import com.fieldnation.ForLoopRunnable;
import com.fieldnation.GpsLocationService;
import com.fieldnation.Log;
import com.fieldnation.R;
import com.fieldnation.data.workorder.UploadSlot;
import com.fieldnation.data.workorder.UploadingDocument;
import com.fieldnation.data.workorder.Workorder;
import com.fieldnation.data.workorder.WorkorderStatus;
import com.fieldnation.data.workorder.WorkorderSubstatus;
import com.fieldnation.service.data.workorder.WorkorderClient;
import com.fieldnation.ui.workorder.WorkorderActivity;
import com.fieldnation.ui.workorder.WorkorderCardView;
import com.fieldnation.ui.workorder.WorkorderDataSelector;
import com.fieldnation.utils.FileUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by shoaib.ahmed on 08/02/2016.
 */
public class NewFeatureActivity extends AuthFragmentActivity {
    private static final String TAG = "NewFeatureActivity";

    // Ui
    private TextView _addedNewTextView;
    private WebView _addedNewWebView;
    private TextView _bugFixedTextView;
    private WebView _bugFixedWebView;
    private TextView _workingOnFeatureTextView;
    private WebView _workingOnFeatureWebView;
    private Button _cancelButton;

    private ActionBarDrawerView _actionBarView;
    private Toolbar _toolbar;

    // Data
    private String _source;

//    public NewFeatureActivity() {
//        super();
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_release_note);

        _addedNewTextView = (TextView) findViewById(R.id.addedNew_textview);
        _addedNewWebView = (WebView) findViewById(R.id.addedNew_webview);

        _bugFixedTextView = (TextView) findViewById(R.id.bugFixed_textview);
        _bugFixedWebView = (WebView) findViewById(R.id.bugFixed_webview);

        _workingOnFeatureTextView = (TextView) findViewById(R.id.workingOnFeature_textview);
        _workingOnFeatureWebView = (WebView) findViewById(R.id.workingOnFeature_webview);

        _cancelButton = (Button) findViewById(R.id.cancel_button);
        _cancelButton.setOnClickListener(_cancel_onClick);



//        _actionBarView = (ActionBarDrawerView) findViewById(R.id.actionbardrawerview);
//
//        _toolbar = _actionBarView.getToolbar();
//        _toolbar.setTitleTextColor(Color.WHITE);
//        _toolbar.setSubtitleTextColor(Color.WHITE);
//        _toolbar.setTitle(R.string.activity_share_request_title_workorder);
//        _toolbar.setNavigationIcon(R.drawable.back_arrow);

        populateUi();
    }


    @Override
    public void onBackPressed() {
        Log.v(TAG, "onBackPressed");
    }

    private void populateUi() {

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
    protected void onResume() {
        super.onResume();
        Log.v(TAG, "onResume");
    }

    private final View.OnClickListener _cancel_onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            finish();
        }
    };}
