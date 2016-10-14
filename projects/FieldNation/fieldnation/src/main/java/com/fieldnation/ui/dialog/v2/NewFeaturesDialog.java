package com.fieldnation.ui.dialog.v2;

import android.content.Context;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.fieldnation.R;
import com.fieldnation.fndialog.FullScreenDialog;

/**
 * Created by Michael on 9/23/2016.
 */

public class NewFeaturesDialog extends FullScreenDialog {

    // Ui
    private View _root;
    private Toolbar _toolbar;
    private WebView _newWebView;
    private WebView _fixedWebView;
    private WebView _nextWebView;

    public NewFeaturesDialog(Context context, ViewGroup container) {
        super(context, container);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, Context context, ViewGroup container) {
        _root = inflater.inflate(R.layout.dialog_v2_new_feature, container, false);

        _toolbar = (Toolbar) _root.findViewById(R.id.toolbar);
        _newWebView = (WebView) _root.findViewById(R.id.new_webview);
        _fixedWebView = (WebView) _root.findViewById(R.id.fixed_webview);
        _nextWebView = (WebView) _root.findViewById(R.id.next_webview);

        return _root;
    }

    @Override
    public void onAdded() {
        super.onAdded();
        final Context context = _root.getContext();

        _toolbar.setTitle("What's New");
        _toolbar.setNavigationIcon(R.drawable.ic_signature_x);
        _toolbar.setNavigationOnClickListener(_toolbar_onClick);

        final int fontSize = context.getResources().getInteger(R.integer.textSizeReleaseNote);
        WebSettings webSettings = null;

        webSettings = _newWebView.getSettings();
        _newWebView.loadData(context.getString(R.string.added_new_feature), "text/html", "utf-8");
        webSettings.setDefaultFontSize(fontSize);

        _fixedWebView.loadData(context.getString(R.string.bugs_fixed), "text/html", "utf-8");
        webSettings = _fixedWebView.getSettings();
        webSettings.setDefaultFontSize(fontSize);

        _nextWebView.loadData(context.getString(R.string.working_on_feature), "text/html", "utf-8");
        webSettings = _nextWebView.getSettings();
        webSettings.setDefaultFontSize(fontSize);
    }

    private final View.OnClickListener _toolbar_onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            cancel();
        }
    };

    public static abstract class Controller extends com.fieldnation.fndialog.Controller {

        public Controller(Context context) {
            super(context, NewFeaturesDialog.class, null);
        }

        public static void show(Context context) {
            show(context, null, NewFeaturesDialog.class, null);
        }

        public static void dismiss(Context context) {
            dismiss(context, null);
        }
    }
}
