package com.fieldnation.ui.dialog.v2;

import android.content.Context;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebStorage;
import android.webkit.WebView;
import android.widget.TextView;

import com.fieldnation.R;
import com.fieldnation.fndialog.FullScreenDialog;

/**
 * Created by Michael on 9/23/2016.
 */

public class WhatsNewDialog extends FullScreenDialog {
    private static final String TAG = "WhatsNewDialog";

    private static final boolean SHOW_WHATS_NEW = true;
    private static final boolean SHOW_BUGS = false;
    private static final boolean SHOW_FEATURES = false;

    // Ui
    private View _root;
    private Toolbar _toolbar;
    private TextView _newTextView;
    private WebView _newWebView;
    private TextView _fixedTextView;
    private WebView _fixedWebView;
    private TextView _nextTextView;
    private WebView _nextWebView;

    public WhatsNewDialog(Context context, ViewGroup container) {
        super(context, container);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, Context context, ViewGroup container) {
        _root = inflater.inflate(R.layout.dialog_v2_whats_new, container, false);

        _toolbar = (Toolbar) _root.findViewById(R.id.toolbar);
        _toolbar.setTitle("What's New");
        _toolbar.setNavigationIcon(R.drawable.ic_signature_x);

        _newTextView = (TextView) _root.findViewById(R.id.new_textview);
        _newWebView = (WebView) _root.findViewById(R.id.new_webview);
        _fixedTextView = (TextView) _root.findViewById(R.id.fixed_textview);
        _fixedWebView = (WebView) _root.findViewById(R.id.fixed_webview);
        _nextTextView = (TextView) _root.findViewById(R.id.next_textview);
        _nextWebView = (WebView) _root.findViewById(R.id.next_webview);

        return _root;
    }

    @Override
    public void onStart() {
        super.onStart();
        final Context context = _root.getContext();

        _toolbar.setNavigationOnClickListener(_toolbar_onClick);

        final int fontSize = context.getResources().getInteger(R.integer.textSizeReleaseNote);
        WebSettings webSettings = null;

        if (SHOW_WHATS_NEW) {
            _newTextView.setVisibility(View.VISIBLE);
            _newWebView.setVisibility(View.VISIBLE);
            _newWebView.loadData(context.getString(R.string.added_new_feature), "text/html", "utf-8");
            webSettings = _newWebView.getSettings();
            webSettings.setJavaScriptEnabled(true);
            webSettings.setDomStorageEnabled(true);
            webSettings.setDefaultFontSize(fontSize);
        } else {
            _newTextView.setVisibility(View.GONE);
            _newWebView.setVisibility(View.GONE);
        }

        if (SHOW_BUGS) {
            _fixedTextView.setVisibility(View.VISIBLE);
            _fixedWebView.setVisibility(View.VISIBLE);
            _fixedWebView.loadData(context.getString(R.string.bugs_fixed), "text/html", "utf-8");
            webSettings = _fixedWebView.getSettings();
            webSettings.setJavaScriptEnabled(true);
            webSettings.setDomStorageEnabled(true);
            webSettings.setDefaultFontSize(fontSize);
        } else {
            _fixedTextView.setVisibility(View.GONE);
            _fixedWebView.setVisibility(View.GONE);
        }

        if (SHOW_FEATURES) {
            _nextTextView.setVisibility(View.VISIBLE);
            _nextWebView.setVisibility(View.VISIBLE);
            _nextWebView.loadData(context.getString(R.string.working_on_feature), "text/html", "utf-8");
            webSettings = _nextWebView.getSettings();
            webSettings.setJavaScriptEnabled(true);
            webSettings.setDomStorageEnabled(true);
            webSettings.setDefaultFontSize(fontSize);
        } else {
            _nextTextView.setVisibility(View.GONE);
            _nextWebView.setVisibility(View.GONE);
        }
    }

    @Override
    public void dismiss(boolean animate) {
        super.dismiss(animate);
    }

    @Override
    public void onStop() {
        WebStorage.getInstance().deleteAllData();
        _fixedWebView.destroy();
        _newWebView.destroy();
        _nextWebView.destroy();
        super.onStop();
    }

    private final View.OnClickListener _toolbar_onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            cancel();
            dismiss(true);
        }
    };

    public static abstract class Controller extends com.fieldnation.fndialog.Controller {

        public Controller(Context context) {
            super(context, WhatsNewDialog.class, null);
        }

        public static void show(Context context) {
            show(context, null, WhatsNewDialog.class, null);
        }

        public static void dismiss(Context context) {
            dismiss(context, null);
        }
    }
}
