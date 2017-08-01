package com.fieldnation.v2.ui.dialog;

import android.content.Context;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebStorage;
import android.webkit.WebView;

import com.fieldnation.R;
import com.fieldnation.fndialog.Controller;
import com.fieldnation.fndialog.FullScreenDialog;
import com.fieldnation.fnlog.Log;

/**
 * Created by Michael on 9/23/2016.
 */

public class WhatsNewDialog extends FullScreenDialog {
    private static final String TAG = "WhatsNewDialog";

    // Ui
    private View _root;
    private Toolbar _toolbar;
    private WebView _newWebView;

    public WhatsNewDialog(Context context, ViewGroup container) {
        super(context, container);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, Context context, ViewGroup container) {
        Log.v(TAG, "onCreateView");
        _root = inflater.inflate(R.layout.dialog_v2_whats_new, container, false);

        _toolbar = (Toolbar) _root.findViewById(R.id.toolbar);
        _toolbar.setTitle("4.0.5");
        _toolbar.setNavigationIcon(R.drawable.ic_signature_x);

        _newWebView = (WebView) _root.findViewById(R.id.new_webview);

        return _root;
    }

    @Override
    public void onStart() {
        Log.v(TAG, "onStart");
        super.onStart();
        final Context context = _root.getContext();

        _toolbar.setNavigationOnClickListener(_toolbar_onClick);
        _root.addOnAttachStateChangeListener(root_onAttachState);

        final int fontSize = context.getResources().getInteger(R.integer.textSizeReleaseNote);
        WebSettings webSettings = null;

        _newWebView.setVisibility(View.VISIBLE);
        _newWebView.loadData(context.getString(R.string.added_new_feature), "text/html", "utf-8");
        webSettings = _newWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDomStorageEnabled(true);
        webSettings.setDefaultFontSize(fontSize);
    }

    private View.OnAttachStateChangeListener root_onAttachState = new View.OnAttachStateChangeListener() {
        @Override
        public void onViewAttachedToWindow(View v) {
        }

        @Override
        public void onViewDetachedFromWindow(View v) {
            WebStorage.getInstance().deleteAllData();
            _newWebView.destroy();
        }
    };

    @Override
    public void onStop() {
        super.onStop();
    }

    private final View.OnClickListener _toolbar_onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            cancel();
            dismiss(true);
        }
    };

    public static void show(Context context) {
        Controller.show(context, null, WhatsNewDialog.class, null);
    }

    public static void dismiss(Context context) {
        Controller.dismiss(context, null);
    }
}
